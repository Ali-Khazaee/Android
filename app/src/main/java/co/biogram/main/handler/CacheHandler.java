package co.biogram.main.handler;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CacheHandler
{
    public static void SetUp(Context context)
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

            File FolderCache = new File(Environment.getExternalStorageDirectory(), "BioGram/Cache");
            FolderCache.mkdirs();

            for (String File : FolderCache.list())
            {
                File TempFile = new File(FolderCache.getAbsolutePath() + "/" + File);

                if (!TempFile.isDirectory())
                    TempFile.delete();
            }

            new File(FolderCache, ".nomedia").createNewFile();

            File CacheFile = new File(context.getCacheDir(), "BioGramCache");

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
            MiscHandler.Debug("CacheHandler-SetUp: " + e.toString());
        }
    }

    public static boolean LinkIsCache(String Name)
    {
        File LinkFile = new File(Environment.getExternalStorageDirectory(), "BioGram/Link");

        return LinkFile.exists() && new File(LinkFile, Name.replaceAll("[^a-zA-Z0-9.-]", "")).exists();
    }

    public static String[] FindLink(String Name)
    {
        try
        {
            File LinkFile = new File(Environment.getExternalStorageDirectory(), "/BioGram/Link/" + Name.replaceAll("[^a-zA-Z0-9.-]", ""));

            BufferedReader Reader = new BufferedReader(new FileReader(LinkFile));
            String[] Result = Reader.readLine().split(":::");
            Reader.close();

            return Result;
        }
        catch (Exception e)
        {
            MiscHandler.Debug("CacheHandler-GetLink: " + e.toString());
        }

        return null;
    }

    public static void StoreLink(Context context, String Name, String Title, String Description, String Image)
    {
        try
        {
            File FolderLink = new File(context.getCacheDir(), "BioGramCache");
            boolean IsCreated = true;

            if (!FolderLink.exists())
                IsCreated = FolderLink.mkdirs();

            if (!IsCreated)
                return;

            Name = Name.replaceAll("[^a-zA-Z0-9.-]", "");
            File LinkFile = new File(FolderLink, Name);

            if (!LinkFile.createNewFile())
                return;

            PrintWriter Writer = new PrintWriter(new FileWriter(LinkFile));
            Writer.println(Title + ":::" + Description + ":::" + Image);
            Writer.flush();
            Writer.close();

            StoreCacheData(context, ":::4:::" + Name);
        }
        catch (Exception e)
        {
            MiscHandler.Debug("CacheHandler-StoreLink: " + e.toString());
        }
    }

    private static void StoreCacheData(Context context, String Data)
    {
        try
        {
            File CacheList = new File(context.getCacheDir(), "BioGramCache");
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
            MiscHandler.Debug("CacheHandler-StoreCacheData: " + e.toString());
        }
    }
}
