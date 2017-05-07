package co.biogram.main.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static Builder Method(String Method)
    {
        return new Builder(Method);
    }

    @SuppressWarnings("all")
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

        public Builder File(String Key, File Value)
        {
            FileMap.put(Key, Value);

            return this;
        }

        public Builder File(HashMap<String, File> fileMap)
        {
            if (fileMap != null)
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

        public Builder Read(int ReadTime)
        {
            this.ReadTime = ReadTime;

            return this;
        }

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
                        case "GET":      PerformGet(Builder.this);      break;
                        case "POST":     PerformPost(Builder.this);     break;
                        case "UPLOAD":   PerformUpload(Builder.this);   break;
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

    public static void GetImage(final ImageView view, final String Address, String Tag, final boolean Cache)
    {
        if (Address.equals(""))
            return;

        final String Name = Address.split("/")[Address.split("/").length - 1];

        if (Cache && CacheHandler.ImageCache(Name))
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

                        if (Cache)
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

    public static void GetImage(final ImageView view, final String Address, String Tag, final int DesiredWidth, final int DesiredHeight, final boolean Cache)
    {
        if (Address.equals(""))
            return;

        final String Name = Address.split("/")[Address.split("/").length - 1];

        if (Cache && CacheHandler.ImageCache(Name))
        {
            CacheHandler.ImageLoad(Name, view);
            return;
        }

        Request request = new Request(Tag)
        {
            @Override
            void Run()
            {
                HttpURLConnection Conn = null;

                try
                {
                    Conn = (HttpURLConnection) new URL(Address).openConnection();
                    Conn.setConnectTimeout(20000);
                    Conn.setReadTimeout(20000);
                    Conn.setRequestMethod("GET");
                    Conn.setDoInput(true);
                    Conn.connect();


                    ByteArrayOutputStream ByteArray = new ByteArrayOutputStream();
                    InputStream IS = Conn.getInputStream();
                    byte[] Buffer = new byte[1024];
                    int Length;

                    while ((Length = IS.read(Buffer)) != -1)
                    {
                        ByteArray.write(Buffer, 0, Length);
                    }

                    byte[] BitmapResponse = ByteArray.toByteArray();
                    ByteArray.close();
                    IS.close();

                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;

                    BitmapFactory.decodeByteArray(BitmapResponse, 0, BitmapResponse.length, o);

                    int Height = o.outHeight;
                    int Width = o.outWidth;
                    int SampleSize = 1;

                    if (Height > DesiredWidth || Width > DesiredHeight)
                    {
                        int HalfHeight = Height / 2;
                        int HalfWidth = Width / 2;

                        while ((HalfHeight / SampleSize) >= DesiredHeight && (HalfWidth / SampleSize) >= DesiredWidth)
                        {
                            SampleSize *= 2;
                        }
                    }

                    o.inSampleSize = SampleSize;
                    o.inJustDecodeBounds = false;

                    final Bitmap bitmap = BitmapFactory.decodeByteArray(BitmapResponse, 0, BitmapResponse.length, o);

                    view.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.setImageBitmap(bitmap);
                        }
                    });

                    if (Cache)
                        CacheHandler.ImageSave(Name, bitmap);
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                if (Conn != null)
                    Conn.disconnect();
            }
        };

        AddRequest(request);
    }

    private static void PerformGet(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setRequestMethod("GET");
            Conn.setUseCaches(false);
            Conn.setDoInput(true);
            Conn.connect();

            BufferedReader Reader = new BufferedReader(new InputStreamReader(Conn.getInputStream()));
            final StringBuilder Result = new StringBuilder();
            final int Status = Conn.getResponseCode();
            String Line;

            while ((Line = Reader.readLine()) != null)
            {
                Result.append(Line);
            }

            Reader.close();

            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish(Result.toString(), Status);
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
            int Length;

            while ((Length = IS.read(Buffer)) != -1)
            {
                ByteArray.write(Buffer, 0, Length);
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
        String EndLine = "\r\n";
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

            Conn.setRequestProperty("Connection", "Keep-Alive");
            Conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=***");

            DataOutputStream OS = new DataOutputStream(Conn.getOutputStream());

            for (Map.Entry<String, String> Entry : builder.ParamMap.entrySet())
            {
                OS.writeBytes("--***" + EndLine);
                OS.writeBytes("Content-Disposition: form-data; name=\"" + URLEncoder.encode(Entry.getKey(), "UTF-8") + "\"" + EndLine);
                OS.writeBytes(EndLine);
                OS.writeBytes(URLEncoder.encode(Entry.getValue(), "UTF-8"));
                OS.writeBytes(EndLine);
                OS.writeBytes("--***" + EndLine);
            }

            int FileLength = 0 ;

            for (Map.Entry<String, File> Entry : builder.FileMap.entrySet())
                FileLength += Entry.getValue().length();

            for (Map.Entry<String, File> Entry : builder.FileMap.entrySet())
            {
                OS.writeBytes("--***" + EndLine);
                OS.writeBytes("Content-Disposition: form-data; name=\"" + URLEncoder.encode(Entry.getKey(), "UTF-8") + "\";filename=\"" + URLEncoder.encode(Entry.getValue().getName(), "UTF-8") + "\"" + EndLine);
                OS.writeBytes(EndLine);

                FileInputStream FIP = new FileInputStream(Entry.getValue());
                byte[] Buffer = new byte[4096];
                int ByteRead;
                int Sent = 0;

                while ((ByteRead = FIP.read(Buffer)) != -1)
                {
                    Sent += ByteRead;
                    OS.write(Buffer, 0, ByteRead);

                    if (builder.OnProgressListener != null)
                        builder.OnProgressListener.OnProgress(Sent, FileLength);
                }

                FIP.close();

                OS.writeBytes(EndLine);
                OS.writeBytes("--***" + EndLine);
            }

            OS.writeBytes("--***--" + EndLine);
            OS.flush();
            OS.close();

            ByteArrayOutputStream ByteArray = new ByteArrayOutputStream();
            InputStream IS = Conn.getInputStream();
            byte[] Buffer = new byte[4096];
            int Length;

            while ((Length = IS.read(Buffer)) != -1)
            {
                ByteArray.write(Buffer, 0, Length);
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

    private static void PerformDownload(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setRequestMethod("GET");
            Conn.setDoInput(true);
            Conn.connect();

            FileOutputStream FOS = new FileOutputStream(builder.OutPath);
            InputStream IS = Conn.getInputStream();

            int FileLength = Conn.getContentLength();
            byte[] Buffer = new byte[2048];
            int Receive = 0;
            int Length;

            while ((Length = IS.read(Buffer)) > 0)
            {
                Receive += Length;
                FOS.write(Buffer, 0, Length);

                if (builder.OnProgressListener != null)
                    builder.OnProgressListener.OnProgress(Receive, FileLength);
            }

            FOS.close();
            IS.close();

            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish("", 200);
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
}
