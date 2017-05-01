package co.biogram.main.handler;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import co.biogram.main.App;

public class CacheHandler
{
    public static boolean VideoCache(String Name)
    {
        if (Name.equals(""))
            return false;

        boolean IsCreated = true;
        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Video");

        if (!Root.exists())
            IsCreated = Root.mkdirs();

        return IsCreated && new File(Root, Name).exists();
    }

    public static String VideoLoad(String Name)
    {
        if (Name.equals(""))
            return "";

        boolean IsCreated = true;
        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Video");

        if (!Root.exists())
            IsCreated = Root.mkdirs();

        if (!IsCreated)
            return "";

        return Root.getAbsolutePath() + "/" + Name;
    }

    public static void VideoSave(String Name, String Address, String Tag)
    {
        if (Name.equals(""))
            return;

        boolean IsCreated = true;
        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Video");

        if (!Root.exists())
            IsCreated = Root.mkdirs();

        if (!IsCreated)
            return;

        RequestHandler.Method("DOWNLOAD").Address(Address).Tag(Tag).OutPath(Root.getAbsolutePath() + "/" + Name).Build(null);

        SaveMetaData(":::3:::" + Name);
    }

    public static boolean ImageCache(String Name)
    {
        if (Name.equals(""))
            return false;

        boolean IsCreated = true;
        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Picture");

        if (!Root.exists())
            IsCreated = Root.mkdirs();

        return IsCreated && new File(Root, Name).exists();
    }

    public static void ImageLoad(String Name, ImageView Image)
    {
        if (Name.equals(""))
            return;

        Image.setImageURI(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/BioGram/Picture/" + Name)));
    }

    public static void ImageSave(String Name, Bitmap bitmap)
    {
        if (Name.equals(""))
            return;

        boolean IsCreated = true;
        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Picture");

        if (!Root.exists())
            IsCreated = Root.mkdirs();

        if (!IsCreated)
            return;

        ByteArrayOutputStream BOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, BOS);

        try
        {
            FileOutputStream FOS = new FileOutputStream(new File(Root, Name));
            FOS.write(BOS.toByteArray());
            FOS.flush();
            FOS.close();

            SaveMetaData(":::1:::" + Name);
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }
    }

    public static boolean LinkCache(String Name)
    {
        if (Name.equals(""))
            return false;

        boolean IsCreated = true;
        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Link");

        if (!Root.exists())
            IsCreated = Root.mkdirs();

        return IsCreated && new File(Root, Name.replaceAll("[^a-zA-Z0-9.-]", "_")).exists();
    }

    public static String[] LinkLoad(String Name)
    {
        if (Name.equals(""))
            return null;

        try
        {
            File Link = new File(Environment.getExternalStorageDirectory(), "/BioGram/Link/" + Name.replaceAll("[^a-zA-Z0-9.-]", "_"));
            BufferedReader Reader = new BufferedReader(new FileReader(Link));

            return Reader.readLine().split(":::");
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }

        return null;
    }

    public static void LinkSave(String Name, String Title, String Description, String Image)
    {
        if (Name.equals(""))
            return;

        try
        {
            boolean IsCreated = true;
            File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Link");

            if (!Root.exists())
                IsCreated = Root.mkdirs();

            if (!IsCreated)
                return;

            Name = Name.replaceAll("[^a-zA-Z0-9.-]", "_");

            File LinkFile = new File(Root, Name);

            IsCreated = LinkFile.createNewFile();

            if (!IsCreated)
                return;

            PrintWriter Writer = new PrintWriter(new FileWriter(LinkFile));
            Writer.println(Title + ":::" + Description + ":::" + Image);
            Writer.flush();
            Writer.close();

            SaveMetaData(":::2:::" + Name);
        }
        catch (Exception e)
        {

            // Leave Me Alone
        }
    }

    public static void ClearExpired()
    {
        try
        {
            File CacheList = new File(App.GetContext().getCacheDir(), "Cache.List");
            File TempFile = new File(CacheList.getAbsolutePath() + ".tmp");

            if (!CacheList.exists())
                return;

            BufferedReader Reader = new BufferedReader(new FileReader(CacheList));
            PrintWriter Writer = new PrintWriter(new FileWriter(TempFile));

            for (String Row; (Row = Reader.readLine()) != null;)
            {
                String[] Data = Row.split(":::");

                if (Integer.parseInt(Data[0]) < (System.currentTimeMillis() / 1000))
                {
                    String Type = "";

                    switch (Data[1])
                    {
                        case "1": Type = "/BioGram/Picture/"; break;
                        case "2": Type = "/BioGram/Link/";    break;
                    }

                    File CacheFile = new File(Environment.getExternalStorageDirectory() + Type + Data[2]);

                    if (CacheFile.exists())
                        // noinspection all
                        CacheFile.delete();
                }
                else
                {
                    Writer.println(Row);
                    Writer.flush();
                }
            }

            Writer.close();
            Reader.close();

            // noinspection all
            CacheList.delete();

            // noinspection all
            TempFile.renameTo(CacheList);
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }
    }

    private static void SaveMetaData(String Data)
    {
        try
        {
            boolean IsCreated = true;
            File CacheDir = new File(App.GetContext().getCacheDir(), "Cache.List");

            if (!CacheDir.exists())
                IsCreated = CacheDir.createNewFile();

            if (!IsCreated)
                return;

            String ExpiredTime = String.valueOf((System.currentTimeMillis() / 1000) + 604800);

            OutputStreamWriter Stream = new OutputStreamWriter(new FileOutputStream(CacheDir, true));
            Stream.append(ExpiredTime);
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
