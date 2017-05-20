package co.biogram.main.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import co.biogram.main.BuildConfig;

public class RequestHandler
{
    private ThreadPoolExecutor ThreadExecutor;
    private final List<FutureTask> RunningTaskList = new ArrayList<>();
    private final Map<String, List<Runnable>> QueueTaskList = new HashMap<>();

    private static RequestHandler MainRequestHandler = new RequestHandler();

    public static RequestHandler Core()
    {
        if (MainRequestHandler == null)
            MainRequestHandler = new RequestHandler();

        return MainRequestHandler;
    }

    private ThreadPoolExecutor Executor()
    {
        if (ThreadExecutor == null)
            ThreadExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        return ThreadExecutor;
    }

    private class FutureTask
    {
        private final int ID;
        private final String Tag;
        private final Future Task;

        FutureTask(String tag, Future future, int id)
        {
            ID = id;
            Tag = tag;
            Task = future;
        }

        void Cancel()
        {
            Task.cancel(true);
        }

        int GetID()
        {
            return ID;
        }

        String GetTag()
        {
            return Tag;
        }
    }

    private void AddTask(final Builder builder)
    {
        Runnable Task = new Runnable()
        {
            @Override
            public void run()
            {
                switch (builder.Method)
                {
                    case "GET":           PerformGet(builder);          break;
                    case "POST":          PerformPost(builder);         break;
                    case "BITMAP":        PerformBitmap(builder);       break;
                    case "UPLOAD":        PerformUpload(builder);       break;
                    case "DOWNLOAD":      PerformDownload(builder);     break;
                    case "BITMAP_OPTION": PerformBitmapOption(builder); break;
                }

                if (QueueTaskList.size() > 0)
                {
                    for (int I = 0; I < QueueTaskList.size(); I++)
                    {
                        Map.Entry<String, List<Runnable>> Entry = QueueTaskList.entrySet().iterator().next();
                        String Key = Entry.getKey();
                        List<Runnable> Value = Entry.getValue();

                        if (Value.size() > 0)
                        {
                            Future future = Executor().submit(Value.get(0));
                            RunningTaskList.add(new FutureTask(Key, future, builder.ID));

                            Value.remove(0);

                            QueueTaskList.remove(Key);
                            QueueTaskList.put(Key, Value);
                            break;
                        }

                        QueueTaskList.remove(Key);
                    }
                }

                if (RunningTaskList.size() > 0)
                {
                    for (int I = 0; I < RunningTaskList.size(); I++)
                    {
                        FutureTask futureTask = RunningTaskList.get(I);

                        if (futureTask.GetID() == builder.ID)
                            RunningTaskList.remove(I);
                    }
                }
            }
        };

        if (RunningTaskList.size() < 32)
        {
            Future future = Executor().submit(Task);
            RunningTaskList.add(new FutureTask(builder.Tag, future, builder.ID));
            return;
        }

        List<Runnable> TempList;

        if (QueueTaskList.containsKey(builder.Tag))
        {
            TempList = QueueTaskList.get(builder.Tag);

            if (TempList == null)
                TempList = new ArrayList<>();

            TempList.add(Task);
        }
        else
        {
            TempList = new ArrayList<>();
            TempList.add(Task);
        }

        QueueTaskList.put(builder.Tag, TempList);
    }

    public void Cancel(String Tag)
    {
        if (QueueTaskList.containsKey(Tag))
        {
            List<Runnable> TaskList = QueueTaskList.get(Tag);

            if (TaskList != null)
                TaskList.clear();

            QueueTaskList.remove(Tag);
        }

        if (RunningTaskList.size() > 0)
        {
            for (int I = 0; I < RunningTaskList.size(); I++)
            {
                FutureTask futureTask = RunningTaskList.get(I);

                if (futureTask.GetTag().equals(Tag))
                {
                    futureTask.Cancel();
                    RunningTaskList.remove(I);
                }
            }
        }
    }

    public Builder Method(String Method)
    {
        return new Builder(Method);
    }

    @SuppressWarnings("all")
    public class Builder
    {
        private int ID = MiscHandler.GenerateViewID();

        private int ReadTime = 20000;
        private int ConnectTime = 20000;

        private String Method = "";
        private String Address = "";
        private String OutPath = "";
        private String Tag = "BioGram";

        private int BitmapWidth = 0;
        private int BitmapHeight = 0;
        private String BitmapName = "";
        private boolean BitmapCache = true;

        private OnBitmapCallBack OnBitmapListener;
        private OnProgressCallBack OnProgressListener;
        private OnCompleteCallBack OnCompleteListener;

        private Map<String, File> FileMap = new HashMap<>();
        private Map<String, String> ParamMap = new HashMap<>();
        private Map<String, String> HeaderMap = new HashMap<>();

        Builder(String Method)
        {
            this.Method = Method;
        }

        public Builder Address(String address)
        {
            Address = address;

            return this;
        }

        Builder BitmapWidth(int Width)
        {
            BitmapWidth = Width;

            return this;
        }

        Builder BitmapName(String Name)
        {
            BitmapName = Name;

            return this;
        }

        Builder BitmapCache(boolean Cache)
        {
            BitmapCache = Cache;

            return this;
        }

        Builder BitmapHeight(int Height)
        {
            BitmapHeight = Height;

            return this;
        }

        public Builder File(String Key, File Value)
        {
            FileMap.put(Key, Value);

            return this;
        }

        public Builder File(Map<String, File> fileMap)
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

        public Builder Tag(String tag)
        {
            Tag = tag;

            return this;
        }

        public Builder Read(int readTime)
        {
            ReadTime = readTime;

            return this;
        }

        public Builder Connect(int connectTime)
        {
            ConnectTime = connectTime;

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

            AddTask(this);
        }

        public void Build(OnBitmapCallBack CallBack)
        {
            OnBitmapListener = CallBack;

            AddTask(this);
        }

        public void Build()
        {
            AddTask(this);
        }
    }

    private interface OnBitmapCallBack
    {
        void OnFinish(Bitmap Response);
    }

    public interface OnCompleteCallBack
    {
        void OnFinish(String Response, int Status);
    }

    public interface OnProgressCallBack
    {
        void OnProgress(long Received, long Total);
    }

    public void LoadImage(final ImageView view, String Address, String Tag, boolean Cache)
    {
        if (Address.equals(""))
            return;

        String Name = Address.split("/")[Address.split("/").length - 1];

        if (Cache && CacheHandler.ImageCache(Name))
        {
            CacheHandler.ImageLoad(Name, view);
            return;
        }

        new Builder("BITMAP").Address(Address).Tag(Tag).BitmapName(Name).BitmapCache(Cache).Build(new OnBitmapCallBack()
        {
            @Override
            public void OnFinish(final Bitmap Response)
            {
                if (!Thread.interrupted())
                {
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.setImageBitmap(Response);
                        }
                    });
                }
            }
        });
    }

    public void LoadImage(final ImageView view, String Address, String Tag, int DesiredWidth, int DesiredHeight, boolean Cache)
    {
        if (Address.equals(""))
            return;

        String Name = Address.split("/")[Address.split("/").length - 1];

        if (Cache && CacheHandler.ImageCache(Name))
        {
            CacheHandler.ImageLoad(Name, view);
            return;
        }

        new Builder("BITMAP_OPTION").Address(Address).Tag(Tag).BitmapName(Name).BitmapWidth(DesiredWidth).BitmapHeight(DesiredHeight).BitmapCache(Cache).Build(new OnBitmapCallBack()
        {
            @Override
            public void OnFinish(final Bitmap Response)
            {
                if (!Thread.interrupted())
                {
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.setImageBitmap(Response);
                        }
                    });
                }
            }
        });
    }

    private void PerformBitmap(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setRequestProperty("User-Agent", "BioGram-" + BuildConfig.VERSION_NAME);
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setRequestMethod("GET");
            Conn.setDoInput(true);

            ByteArrayOutputStream ByteArray = new ByteArrayOutputStream();
            InputStream IS = Conn.getInputStream();
            byte[] Buffer = new byte[1024];
            int Length;

            while ((Length = IS.read(Buffer)) != -1)
            {
                if (Thread.interrupted())
                    break;

                ByteArray.write(Buffer, 0, Length);
            }

            byte[] BitmapResponse = ByteArray.toByteArray();
            ByteArray.close();
            IS.close();

            Bitmap bitmap = BitmapFactory.decodeByteArray(BitmapResponse, 0, BitmapResponse.length);

            if (!Thread.interrupted())
            {
                if (builder.OnBitmapListener != null)
                    builder.OnBitmapListener.OnFinish(bitmap);
            }

            if (builder.BitmapCache)
                CacheHandler.ImageSave(builder.BitmapName, bitmap);
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }

        if (Conn != null)
            Conn.disconnect();
    }

    private void PerformBitmapOption(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setRequestProperty("User-Agent", "BioGram-" + BuildConfig.VERSION_NAME);
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setRequestMethod("GET");
            Conn.setDoInput(true);

            ByteArrayOutputStream ByteArray = new ByteArrayOutputStream();
            InputStream IS = Conn.getInputStream();
            byte[] Buffer = new byte[1024];
            int Length;

            while ((Length = IS.read(Buffer)) != -1)
            {
                if (Thread.interrupted())
                    break;

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

            if (Height > builder.BitmapWidth || Width > builder.BitmapHeight)
            {
                int HalfHeight = Height / 2;
                int HalfWidth = Width / 2;

                while ((HalfHeight / SampleSize) >= builder.BitmapHeight && (HalfWidth / SampleSize) >= builder.BitmapWidth)
                {
                    SampleSize *= 2;
                }
            }

            o.inSampleSize = SampleSize;
            o.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeByteArray(BitmapResponse, 0, BitmapResponse.length, o);

            if (!Thread.interrupted())
            {
                if (builder.OnBitmapListener != null)
                    builder.OnBitmapListener.OnFinish(bitmap);
            }

            if (builder.BitmapCache)
                CacheHandler.ImageSave(builder.BitmapName, bitmap);
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }

        if (Conn != null)
            Conn.disconnect();
    }

    private void PerformGet(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setRequestProperty("User-Agent", "BioGram-" + BuildConfig.VERSION_NAME);
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setRequestMethod("GET");
            Conn.setUseCaches(false);
            Conn.setDoInput(true);

            ByteArrayOutputStream ByteArray = new ByteArrayOutputStream();
            InputStream IS = Conn.getInputStream();
            byte[] Buffer = new byte[1024];
            int ByteRead;

            while ((ByteRead = IS.read(Buffer)) != -1)
            {
                if (Thread.interrupted())
                    break;

                ByteArray.write(Buffer, 0, ByteRead);
            }

            final String Response = new String(ByteArray.toByteArray());
            final int Status = Conn.getResponseCode();
            ByteArray.close();
            IS.close();

            if (!Thread.interrupted())
            {
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
        }
        catch (final Exception e)
        {
            if (!Thread.interrupted())
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

        if (Conn != null)
            Conn.disconnect();
    }

    private void PerformPost(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setRequestProperty("User-Agent", "BioGram-" + BuildConfig.VERSION_NAME);
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setRequestMethod("POST");
            Conn.setUseCaches(false);
            Conn.setDoOutput(true);
            Conn.setDoInput(true);

            for (Map.Entry<String, String> Entry : builder.HeaderMap.entrySet())
                Conn.setRequestProperty(URLEncoder.encode(Entry.getKey(), "UTF-8"), URLEncoder.encode(Entry.getValue(), "UTF-8"));

            boolean FirstParam = true;
            OutputStream OS = Conn.getOutputStream();

            for (Map.Entry<String, String> Entry : builder.ParamMap.entrySet())
            {
                if (FirstParam)
                    FirstParam = false;
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
            int ByteRead;

            while ((ByteRead = IS.read(Buffer)) != -1)
            {
                if (Thread.interrupted())
                    break;

                ByteArray.write(Buffer, 0, ByteRead);
            }

            final String Response = new String(ByteArray.toByteArray());
            final int Status = Conn.getResponseCode();
            ByteArray.close();
            IS.close();

            if (!Thread.interrupted())
            {
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
        }
        catch (final Exception e)
        {
            if (!Thread.interrupted())
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

        if (Conn != null)
            Conn.disconnect();
    }

    private void PerformUpload(final Builder builder)
    {
        String Hyphen = "--";
        String EndLine = "\r\n";
        String Boundary = "***";
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
            Conn.setRequestProperty("User-Agent", "BioGram-" + BuildConfig.VERSION_NAME);
            Conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + Boundary);

            DataOutputStream OS = new DataOutputStream(Conn.getOutputStream());

            for (Map.Entry<String, String> Entry : builder.ParamMap.entrySet())
            {
                OS.writeBytes(Hyphen + Boundary + EndLine);
                OS.writeBytes("Content-Disposition: form-data; name=\"" + URLEncoder.encode(Entry.getKey(), "UTF-8") + "\"" + EndLine);
                OS.writeBytes(EndLine);
                OS.writeBytes(URLEncoder.encode(Entry.getValue(), "UTF-8"));
                OS.writeBytes(EndLine);
                OS.flush();
            }

            int FileLengthTemp = 0;

            for (Map.Entry<String, File> Entry : builder.FileMap.entrySet())
                FileLengthTemp += Entry.getValue().length();

            for (Map.Entry<String, File> Entry : builder.FileMap.entrySet())
            {
                OS.writeBytes(Hyphen + Boundary + EndLine);
                OS.writeBytes("Content-Disposition: form-data; name=\"" + URLEncoder.encode(Entry.getKey(), "UTF-8") + "\"; filename=\"" + URLEncoder.encode(Entry.getValue().getName(), "UTF-8") + "\"" + EndLine);
                OS.writeBytes(EndLine);
                OS.flush();

                FileInputStream FIP = new FileInputStream(Entry.getValue());
                final int FileLength = FileLengthTemp;
                byte[] Buffer = new byte[1024];
                int SentTemp = 0;
                int ByteRead;

                while ((ByteRead = FIP.read(Buffer)) != -1)
                {
                    if (Thread.interrupted())
                        break;

                    SentTemp += ByteRead;
                    OS.write(Buffer, 0, ByteRead);

                    if (!Thread.interrupted())
                    {
                        final int Sent = SentTemp;

                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (builder.OnProgressListener != null)
                                    builder.OnProgressListener.OnProgress(Sent, FileLength);
                            }
                        });
                    }
                }

                FIP.close();

                OS.writeBytes(EndLine);
                OS.flush();
            }

            OS.writeBytes(Hyphen + Boundary + Hyphen + EndLine);
            OS.flush();
            OS.close();

            ByteArrayOutputStream ByteArray = new ByteArrayOutputStream();
            InputStream IS = Conn.getInputStream();
            byte[] Buffer = new byte[1024];
            int ByteRead;

            while ((ByteRead = IS.read(Buffer)) != -1)
            {
                if (Thread.interrupted())
                    break;

                ByteArray.write(Buffer, 0, ByteRead);
            }

            final String Response = new String(ByteArray.toByteArray());
            final int Status = Conn.getResponseCode();
            ByteArray.close();
            IS.close();

            if (!Thread.interrupted())
            {
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
        }
        catch (final Exception e)
        {
            if (!Thread.interrupted())
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

        if (Conn != null)
            Conn.disconnect();
    }

    private void PerformDownload(final Builder builder)
    {
        HttpURLConnection Conn = null;

        try
        {
            Conn = (HttpURLConnection) new URL(builder.Address).openConnection();
            Conn.setRequestProperty("User-Agent", "BioGram-" + BuildConfig.VERSION_NAME);
            Conn.setConnectTimeout(builder.ConnectTime);
            Conn.setReadTimeout(builder.ReadTime);
            Conn.setRequestMethod("GET");
            Conn.setDoInput(true);

            FileOutputStream FOS = new FileOutputStream(builder.OutPath);
            final int ResponseCode = Conn.getResponseCode();
            int FileLength = Conn.getContentLength();
            InputStream IS = Conn.getInputStream();
            byte[] Buffer = new byte[1024];
            int ReceiveSize = 0;
            int ByteRead;

            while ((ByteRead = IS.read(Buffer)) > 0)
            {
                if (Thread.interrupted())
                    break;

                ReceiveSize += ByteRead;
                FOS.write(Buffer, 0, ByteRead);

                if (!Thread.interrupted())
                {
                    if (builder.OnProgressListener != null)
                        builder.OnProgressListener.OnProgress(ReceiveSize, FileLength);
                }
            }

            FOS.close();
            IS.close();

            if (!Thread.interrupted())
            {
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (builder.OnCompleteListener != null)
                            builder.OnCompleteListener.OnFinish("", ResponseCode);
                    }
                });
            }
        }
        catch (final Exception e)
        {
            if (!Thread.interrupted())
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

        if (Conn != null)
            Conn.disconnect();
    }
}
