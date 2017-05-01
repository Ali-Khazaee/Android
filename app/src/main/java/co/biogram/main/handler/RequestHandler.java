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
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setRequestMethod("POST");
            Conn.setChunkedStreamingMode(4096);
            Conn.setDoInput(true);
            Conn.setDoOutput(true);

            for (Map.Entry<String, String> entry : builder.HeaderMap.entrySet())
                Conn.setRequestProperty(URLEncoder.encode(entry.getKey(), "UTF-8"), URLEncoder.encode(entry.getValue(), "UTF-8"));

            boolean FirstParam = true;
            StringBuilder Param = new StringBuilder();

            for (Map.Entry<String, String> entry : builder.ParamMap.entrySet())
            {
                if (FirstParam)
                    FirstParam = false;
                else
                    Param.append("&");

                Param.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                Param.append("=");
                Param.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            OutputStream OStream = new BufferedOutputStream(Conn.getOutputStream());
            BufferedWriter Writer = new BufferedWriter(new OutputStreamWriter(OStream, "UTF-8"));
            Writer.write(Param.toString());
            Writer.flush();
            Writer.close();
            OStream.close();

            String Line;
            final int Status = Conn.getResponseCode();
            final StringBuilder Response = new StringBuilder();
            BufferedReader Reader = new BufferedReader(new InputStreamReader(Conn.getInputStream(), "UTF-8"));

            while ((Line = Reader.readLine()) != null)
            {
                Response.append(Line);
            }

            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish(Response.toString(), Status);
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

            InputStream input = new BufferedInputStream(Address.openStream(), 4096);
            OutputStream output = new FileOutputStream(builder.OutPath);

            int Count;
            long Received = 0;
            byte Data[] = new byte[1024];

            while ((Count = input.read(Data)) != -1)
            {
                Received += Count;
                output.write(Data, 0, Count);

                if (builder.OnProgressListener != null)
                    builder.OnProgressListener.OnProgress(Received, FileLength);
            }

            output.flush();
            output.close();
            input.close();

            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (builder.OnCompleteListener != null)
                        builder.OnCompleteListener.OnFinish("Finish", 0);
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
