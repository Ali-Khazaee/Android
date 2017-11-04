package co.biogram.main.handler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

class UpdateHandler
{
    public static void SetUp(final Context context)
    {
        AndroidNetworking.post(MiscHandler.GetRandomServer("Update"))
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    PackageInfo Package = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && Result.getInt("VersionCode") > Package.versionCode)
                    {
                        File UpdateFolder = new File(context.getCacheDir(), "BioGramUpdate");

                        if (UpdateFolder.exists() && UpdateFolder.isDirectory())
                        {
                            File[] UpdateFiles = UpdateFolder.listFiles();

                            if (UpdateFiles != null)
                            {
                                boolean UpdateFound = false;
                                String FileName = "biogram-" + String.valueOf(Result.getInt("VersionCode")) + ".apk";

                                for (File file : UpdateFiles)
                                {
                                    if (!FileName.equals(file.getName()))
                                        file.delete();
                                    else
                                        UpdateFound = true;
                                }

                                if (UpdateFound)
                                    return;
                            }
                        }

                        if (Result.getInt("VersionCode") > Package.versionCode)
                            DownloadUpdate(context, Result.getInt("VersionCode"));
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("CrashHandler-RequestCrash: " + e.toString());
                }
            }

            @Override public void onError(ANError anError) { }
        });
    }

    private static void DownloadUpdate(final Context context, final int Version)
    {
        String Filename = "biogram-" + String.valueOf(Version) + ".apk";
        File UpdateFolder = new File(context.getCacheDir(), "BioGramUpdate");

        if (!UpdateFolder.exists())
            UpdateFolder.mkdir();

        AndroidNetworking.download(MiscHandler.GetRandomServer("biogram.apk"), UpdateFolder.getAbsolutePath(), Filename)
        .setPriority(Priority.MEDIUM)
        .build()
        .startDownload(new DownloadListener()
        {
            @Override
            public void onDownloadComplete()
            {
                SharedHandler.SetString(context, "AppVersion", String.valueOf(Version));
            }

            @Override public void onError(ANError anError) { }
        });
    }

    public static boolean IsUpdateAvailable(Context context)
    {
        try
        {
            PackageInfo Package = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            if (Integer.parseInt(SharedHandler.GetString(context, "AppVersion")) <= Package.versionCode)
                return true;

            File UpdateFolder = new File(context.getCacheDir(), "BioGramUpdate");

            if (UpdateFolder.exists() && UpdateFolder.isDirectory())
            {
                File[] UpdateFiles = UpdateFolder.listFiles();

                if (UpdateFiles != null)
                {
                    String FileName = "biogram-" + SharedHandler.GetString(context, "AppVersion") + ".apk";

                    for (File file : UpdateFiles)
                    {
                        if (FileName.equals(file.getName()))
                        {
                            int Result = context.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                            if (Result == PackageManager.PERMISSION_GRANTED)
                            {
                                InputStream IS = new FileInputStream(file.getAbsoluteFile());
                                OutputStream OS = new FileOutputStream(Environment.getExternalStorageDirectory() + "/biogram.apk");

                                int Reader;
                                byte[] Buffer = new byte[2048];

                                while ((Reader = IS.read(Buffer)) != -1)
                                {
                                    OS.write(Buffer, 0, Reader);
                                }

                                OS.flush();
                                OS.close();
                                IS.close();

                                return false;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            MiscHandler.Debug("UpdateHandler-IsUpdateAvailable: " + e.toString());
        }

        return true;
    }
}
