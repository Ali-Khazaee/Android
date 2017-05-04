package co.biogram.main.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RequestHandler
{
    private static ArrayList<Request> QueueRequestList = new ArrayList<>();
    private static ArrayList<Request> RunningRequestList = new ArrayList<>();
    private static ThreadPoolExecutor ThreadExecutor;

    private static synchronized ThreadPoolExecutor Executor()
    {
        if (ThreadExecutor == null)
            ThreadExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        return ThreadExecutor;
    }

    private static synchronized void AddRequest(Request request)
    {
        if (RunningRequestList.size() < 32)
        {
            Executor().execute(request);
            RunningRequestList.add(request);
            return;
        }

        QueueRequestList.add(request);
    }

    private static abstract class Request extends Thread
    {
        private String Tag;

        Request(String Tag)
        {
            this.Tag = Tag;
        }

        @Override
        public void run()
        {
            if (Looper.myLooper() == null)
                Looper.prepare();

            String OldName = getName();
            setName(Tag);

            try
            {
                Run();
            }
            finally
            {
                setName(OldName);
                UpdateRequestList();
            }
        }

        String GetTag()
        {
            return Tag;
        }

        void Cancel()
        {
            interrupt();
        }

        abstract void Run();
    }

    private static synchronized void UpdateRequestList()
    {
        if (QueueRequestList.size() > 0)
        {
            Executor().execute(QueueRequestList.get(0));
            QueueRequestList.remove(0);
        }
    }

    public static synchronized void Cancel(String Tag)
    {
        for (int I = 0; I < QueueRequestList.size(); I++)
        {
            if (QueueRequestList.get(I).GetTag().equals(Tag))
            {
                QueueRequestList.get(I).Cancel();
                QueueRequestList.remove(I);
            }
        }

        for (int I = 0; I < RunningRequestList.size(); I++)
        {
            if (RunningRequestList.get(I).GetTag().equals(Tag))
            {
                RunningRequestList.get(I).Cancel();
                RunningRequestList.remove(I);
            }
        }
    }

    public static Builder Method(String Method)
    {
        return new Builder(Method);
    }

    public static class Builder
    {
        private int ReadTime = 30000;
        private int ConnectTime = 30000;

        private String METHOD = "";
        private String Address = "";
        private String OutPath = "";
        private String Tag = "BioGram";

        private OnProgressCallBack OnProgressListener;
        private OnCompleteCallBack OnCompleteListener;

        private HashMap<String, File> FileMap = new HashMap<>();
        private HashMap<String, String> ParamMap = new HashMap<>();
        private HashMap<String, String> HeaderMap = new HashMap<>();

        Builder(String Method)
        {
            METHOD = Method;
        }

        public Builder Address(String address)
        {
            Address = address;

            return this;
        }

        @SuppressWarnings("unused")
        public Builder File(String Key, File Value)
        {
            FileMap.put(Key, Value);

            return this;
        }

        public Builder File(HashMap<String, File> fileMap)
        {
            FileMap.putAll(fileMap);

            return this;
        }

        public Builder Param(String Key, String Value)
        {
            ParamMap.put(Key, Value);

            return this;
        }

        public Builder Header(String Key, String Value)
        {
            HeaderMap.put(Key, Value);

            return this;
        }

        public Builder Tag(String Tag)
        {
            this.Tag = Tag;

            return this;
        }

        @SuppressWarnings("unused")
        public Builder Read(int ReadTime)
        {
            this.ReadTime = ReadTime;

            return this;
        }

        @SuppressWarnings("unused")
        public Builder Connect(int ConnectTime)
        {
            this.ConnectTime = ConnectTime;

            return this;
        }

        public Builder Listen(OnProgressCallBack  CallBack)
        {
            OnProgressListener = CallBack;

            return this;
        }

        public Builder OutPath(String Path)
        {
            OutPath = Path;

            return this;
        }

        public void Build(OnCompleteCallBack CallBack)
        {
            OnCompleteListener = CallBack;

            Request request = new Request(Tag)
            {
                @Override
                void Run()
                {
                    switch (METHOD)
                    {
                        case "POST":     PerformPost(Builder.this);     break;
                        case "Upload":   PerformUpload(Builder.this);   break;
                        case "DOWNLOAD": PerformDownload(Builder.this); break;
                    }
                }
            };

            AddRequest(request);
        }
    }

    public interface OnCompleteCallBack
    {
        void OnFinish(String Response, int Status);
    }

    public interface OnProgressCallBack
    {
        void OnProgress(long Received, long Total);
    }

    public static void GetImage(final ImageView view, final String Address, String Tag, final boolean NoCache)
    {
        if (Address.equals(""))
            return;

        final String Name = Address.split("/")[Address.split("/").length - 1];

        if (!NoCache && CacheHandler.ImageCache(Name))
        {
            CacheHandler.ImageLoad(Name, view);
            return;
        }

        Request request = new Request(Tag)
        {
            @Override
            void Run()
            {
                try
                {
                    HttpURLConnection Conn = (HttpURLConnection) new URL(Address).openConnection();
                    Conn.setDoInput(true);
                    Conn.connect();

                    if (Conn.getResponseCode() == 200)
                    {
                        InputStream input = Conn.getInputStream();

                        final Bitmap bitmap = BitmapFactory.decodeStream(input);

                        view.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                view.setImageBitmap(bitmap);
                            }
                        });

                        if (!NoCache)
                            CacheHandler.ImageSave(Name, bitmap);

                        input.close();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }
            }
        };

        AddRequest(request);
    }

    public static void GetImage(final ImageView view, final String Address, String Tag, final int DesiredWidth, final int DesiredHeight, final boolean NoCache)
    {
        if (Address.equals(""))
            return;

        final String Name = Address.split("/")[Address.split("/").length - 1];

        if (!NoCache && CacheHandler.ImageCache(Name))
        {
            CacheHandler.ImageLoad(Name, view);
            return;
        }

        Request request = new Request(Tag)
        {
            @Override
            void Run()
            {
                try
                {
                    HttpURLConnection Conn = (HttpURLConnection) new URL(Address).openConnection();
                    Conn.setDoInput(true);
                    Conn.connect();

                    if (Conn.getResponseCode() == 200)
                    {
                        InputStream input = Conn.getInputStream();

                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;

                        BitmapFactory.decodeStream(input, null, o);

                        int Height = o.outHeight;
                        int Width = o.outWidth;
                        int inSampleSize = 1;

                        if (Height > DesiredWidth || Width > DesiredHeight)
                        {
                            int HalfHeight = Height / 2;
                            int HalfWidth = Width / 2;

                            while ((HalfHeight / inSampleSize) >= DesiredHeight && (HalfWidth / inSampleSize) >= DesiredWidth)
                            {
                                inSampleSize *= 2;
                            }
                        }

                        o.inSampleSize = inSampleSize;
                        o.inJustDecodeBounds = false;

                        final Bitmap bitmap = BitmapFactory.decodeStream(input, null, o);

                        view.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                view.setImageBitmap(bitmap);
                            }
                        });

                        if (!NoCache)
                            CacheHandler.ImageSave(Name, bitmap);

                        input.close();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }
            }
        };

        AddRequest(request);
    }

    private static void PerformPost(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setChunkedStreamingMode(4096);
            Conn.setRequestMethod("POST");
            Conn.setUseCaches(false);
            Conn.setDoOutput(true);
            Conn.setDoInput(true);

            for (Map.Entry<String, String> Entry : builder.HeaderMap.entrySet())
                Conn.setRequestProperty(URLEncoder.encode(Entry.getKey(), "UTF-8"), URLEncoder.encode(Entry.getValue(), "UTF-8"));

            boolean First = true;
            OutputStream OS = Conn.getOutputStream();

            for (Map.Entry<String, String> Entry : builder.ParamMap.entrySet())
            {
                if (First)
                    First = false;
                else
                    OS.write("&".getBytes());

                OS.write(URLEncoder.encode(Entry.getKey(), "UTF-8").getBytes());
                OS.write("=".getBytes());
                OS.write(URLEncoder.encode(Entry.getValue(), "UTF-8").getBytes());
            }

            OS.close();

            ByteArrayOutputStream ByteArray = new ByteArrayOutputStream();
            InputStream IS = Conn.getInputStream();
            byte[] Buffer = new byte[1024];
            int Reader;

            while ((Reader = IS.read(Buffer)) != -1)
            {
                ByteArray.write(Buffer, 0, Reader);
            }

            final String Response = new String(ByteArray.toByteArray());
            final int Status = Conn.getResponseCode();
            ByteArray.close();
            IS.close();

            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish(Response, Status);
                }
            });
        }
        catch (final Exception e)
        {
            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish(e.toString(), -1);
                }
            });
        }

        if (Conn != null)
            Conn.disconnect();
    }

    private static void PerformUpload(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setRequestMethod("POST");
            Conn.setUseCaches(false);
            Conn.setDoOutput(true);
            Conn.setDoInput(true);

            for (Map.Entry<String, String> Entry : builder.HeaderMap.entrySet())
                Conn.setRequestProperty(URLEncoder.encode(Entry.getKey(), "UTF-8"), URLEncoder.encode(Entry.getValue(), "UTF-8"));

            Conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=Bio");

            DataOutputStream DataOS = new DataOutputStream(Conn.getOutputStream());

            for (Map.Entry<String, File> Entry : builder.FileMap.entrySet())
            {
                DataOS.writeBytes("--Bio\r\n");
                DataOS.writeBytes("Content-Disposition: form-data; name=\"" + URLEncoder.encode(Entry.getKey(), "UTF-8") + "\"; filename=\"" + URLEncoder.encode(Entry.getValue().getName(), "UTF-8") + "\"" + "\r\n");
                DataOS.writeBytes("Content-Type: BioGram/Upload\r\n");
                DataOS.writeBytes("\r\n");

                FileInputStream FIP = new FileInputStream(Entry.getValue());
                byte[] Buffer = new byte[4096];
                int BytesRead;

                while ((BytesRead = FIP.read(Buffer)) != -1)
                {
                    DataOS.write(Buffer, 0, BytesRead);
                }

                FIP.close();

                DataOS.writeBytes("\r\n");
                DataOS.writeBytes("--Bio");
                DataOS.flush();
            }

            DataOS.writeBytes("--");
            DataOS.flush();
            DataOS.close();

            if (Conn.getResponseCode() != 200)
                throw new Exception("Response Code: %d " + Conn.getResponseCode());

            InputStream IS = Conn.getInputStream();
            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
            byte[] Bytes = new byte[1024];
            int BytesRead;

            while((BytesRead = IS.read(Bytes)) != -1)
            {
                BAOS.write(Bytes, 0, BytesRead);
            }

            byte[] BytesReceived = BAOS.toByteArray();
            BAOS.close();
            IS.close();

            final String Response = new String(BytesReceived);

            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish(Response, 0);
                }
            });
        }
        catch (final Exception e)
        {
            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish(e.toString(), -1);
                }
            });
        }

        if (Conn != null)
            Conn.disconnect();
    }

    private static void PerformDownload(final Builder builder)
    {
        try
        {
            URL Address = new URL(builder.Address);
            URLConnection Conn = Address.openConnection();
            Conn.connect();

            int FileLength = Conn.getContentLength();
            InputStream IS = new BufferedInputStream(Address.openStream(), 2048);
            FileOutputStream FOS = new FileOutputStream(builder.OutPath);

            byte[] Buffer = new byte[1024];
            long Receive = 0;
            int Count;

            while ((Count = IS.read(Buffer)) != -1)
            {
                Receive += Count;
                FOS.write(Buffer, 0, Count);

                if (builder.OnProgressListener != null)
                    builder.OnProgressListener.OnProgress(Receive, FileLength);
            }

            FOS.flush();
            FOS.close();
            IS.close();

            InputStream IS2 = Conn.getInputStream();
            ByteArrayOutputStream ByteArray = new ByteArrayOutputStream();
            byte[] Bytes = new byte[1024];
            int BytesRead;

            while((BytesRead = IS2.read(Bytes)) != -1)
            {
                ByteArray.write(Bytes, 0, BytesRead);
            }

            final String Response = new String(ByteArray.toByteArray());
            ByteArray.close();
            IS2.close();

            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish(Response, 200);
                }
            });
        }
        catch (final Exception e)
        {
            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish(e.toString(), -1);
                }
            });
        }
    }
}
