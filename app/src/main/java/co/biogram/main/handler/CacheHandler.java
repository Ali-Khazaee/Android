package co.biogram.main.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CacheHandler
{
    private static final LruCache<String, Bitmap> MemoryCache = new LruCache<String, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8)
    {
        @Override
        protected int sizeOf(String key, Bitmap bitmap)
        {
            return bitmap.getByteCount() / 1024;
        }
    };

    private static void AddBitmapToMemoryCache(String Key, Bitmap bitmap)
    {
        if (MemoryCache.get(Key) == null)
            MemoryCache.put(Key, bitmap);
    }

    private static Bitmap GetBitmapFromMemCache(String Key)
    {
        return MemoryCache.get(Key);
    }

    static boolean HasPicture(String Name)
    {
        File PictureFile = new File(Environment.getExternalStorageDirectory(), "BioGram/Picture");
        boolean IsCreated = true;

        if (!PictureFile.exists())
            IsCreated = PictureFile.mkdirs();

        return IsCreated && new File(PictureFile, Name).exists();
    }

    static void GetPicture(String Name, ImageView Image)
    {
        Bitmap CacheBitmap = GetBitmapFromMemCache(Name);

        if (CacheBitmap != null)
        {
            Image.setImageBitmap(CacheBitmap);
            return;
        }

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap DecodeBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/BioGram/Picture/" + Name, o);
        Image.setImageBitmap(DecodeBitmap);

        AddBitmapToMemoryCache(Name, DecodeBitmap);
    }

    static void StorePicture(String Name, byte[] Data)
    {
        try
        {
            File PictureFile = new File(Environment.getExternalStorageDirectory(), "BioGram/Picture");
            boolean IsCreated = true;

            if (!PictureFile.exists())
                IsCreated = PictureFile.mkdirs();

            if (!IsCreated)
                return;

            FileOutputStream FOS = new FileOutputStream(new File(PictureFile, Name));
            FOS.write(Data);
            FOS.flush();
            FOS.close();

            StoreCacheData(":::2:::" + Name);
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }
    }

    public static boolean HasLink(String Name)
    {
        File LinkFile = new File(Environment.getExternalStorageDirectory(), "BioGram/Link");
        boolean IsCreated = true;

        if (!LinkFile.exists())
            IsCreated = LinkFile.mkdirs();

        return IsCreated && new File(LinkFile, Name.replaceAll("[^a-zA-Z0-9.-]", "_")).exists();
    }

    public static String[] GetLink(String Name)
    {
        try
        {
            File LinkFile = new File(Environment.getExternalStorageDirectory(), "/BioGram/Link/" + Name.replaceAll("[^a-zA-Z0-9.-]", "_"));

            BufferedReader Reader = new BufferedReader(new FileReader(LinkFile));
            String[] Result = Reader.readLine().split(":::");
            Reader.close();

            return Result;
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }

        return null;
    }

    public static void StoreLink(String Name, String Title, String Description, String Image)
    {
        try
        {
            File FolderLink = new File(Environment.getExternalStorageDirectory(), "BioGram/Link");
            boolean IsCreated = true;

            if (!FolderLink.exists())
                IsCreated = FolderLink.mkdirs();

            if (!IsCreated)
                return;

            Name = Name.replaceAll("[^a-zA-Z0-9.-]", "_");
            File LinkFile = new File(FolderLink, Name);

            if (!LinkFile.createNewFile())
                return;

            PrintWriter Writer = new PrintWriter(new FileWriter(LinkFile));
            Writer.println(Title + ":::" + Description + ":::" + Image);
            Writer.flush();
            Writer.close();

            StoreCacheData(":::4:::" + Name);
        }
        catch (Exception e)
        {

            // Leave Me Alone
        }
    }

    public static void SetUp()
    {
        try
        {
            File FolderDocument = new File(Environment.getExternalStorageDirectory(), "BioGram/Document");
            FolderDocument.mkdirs();

            new File(FolderDocument, ".nomedia").createNewFile();

            File FolderPicture = new File(Environment.getExternalStorageDirectory(), "BioGram/Picture");
            FolderPicture.mkdirs();

            new File(FolderPicture, ".nomedia").createNewFile();

            File FolderVideo = new File(Environment.getExternalStorageDirectory(), "BioGram/Video");
            FolderVideo.mkdirs();

            new File(FolderVideo, ".nomedia").createNewFile();

            File FolderLink = new File(Environment.getExternalStorageDirectory(), "BioGram/Link");
            FolderLink.mkdirs();

            new File(FolderLink, ".nomedia").createNewFile();

            File CacheFile = new File(FolderDocument, "Cache.List");

            if (!CacheFile.exists())
                return;

            File CacheTemp = new File(CacheFile.getAbsolutePath() + ".tmp");

            if (!CacheTemp.exists())
                return;

            BufferedReader Reader = new BufferedReader(new FileReader(CacheFile));
            PrintWriter Writer = new PrintWriter(new FileWriter(CacheTemp));
            String Row;

            while ((Row = Reader.readLine()) != null)
            {
                String[] Data = Row.split(":::");

                if (Integer.parseInt(Data[0]) < (System.currentTimeMillis() / 1000))
                {
                    String Type = "";

                    switch (Data[1])
                    {
                        case "1": Type = "/BioGram/Document/"; break;
                        case "2": Type = "/BioGram/Picture/";  break;
                        case "3": Type = "/BioGram/Video/";    break;
                        case "4": Type = "/BioGram/Link/";     break;
                    }

                    new File(Environment.getExternalStorageDirectory() + Type + Data[2]).delete();
                }
                else
                {
                    Writer.println(Row);
                    Writer.flush();
                }
            }

            Writer.close();
            Reader.close();

            if (CacheFile.delete())
                CacheTemp.renameTo(CacheFile);
        }
        catch (Exception e)
        {
            MiscHandler.Log(e.toString());
        }
    }

    private static void StoreCacheData(String Data)
    {
        try
        {
            File CacheList = new File(new File(Environment.getExternalStorageDirectory(), "BioGram/Document"), "Cache.List");
            boolean IsCreated = true;

            if (!CacheList.exists())
                IsCreated = CacheList.createNewFile();

            if (!IsCreated)
                return;

            OutputStreamWriter Stream = new OutputStreamWriter(new FileOutputStream(CacheList, true));
            Stream.append(String.valueOf((System.currentTimeMillis() / 1000) + 604800));
            Stream.append(Data);
            Stream.append("\n");
            Stream.flush();
            Stream.close();
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }
    }
}
