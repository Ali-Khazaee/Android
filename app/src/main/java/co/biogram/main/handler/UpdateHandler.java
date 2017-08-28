package co.biogram.main.handler;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import java.io.File;

public class UpdateHandler
{
    public static void SetUp(final Context context)
    {
        try
        {
            final PackageInfo Package = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            AndroidNetworking.post(MiscHandler.GetRandomServer("Update"))
            .addBodyParameter("Version", String.valueOf(Package.versionCode))
            .build()
            .getAsString(new StringRequestListener()
            {
                @Override
                public void onResponse(String Response)
                {
                    try
                    {
                        JSONObject Result = new JSONObject(Response);

                        if (Result.getInt("Message") == 1000)
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
        catch (Exception e)
        {
            MiscHandler.Debug("UpdateHandler-Request: " + e.toString());
        }
    }

    private static void DownloadUpdate(Context context, int Version)
    {
        String Filename = "biogram-" + String.valueOf(Version) + ".apk";
        File UpdateFolder = new File(context.getCacheDir(), "BioGramUpdate");

        if (!UpdateFolder.exists())
            UpdateFolder.mkdir();

        AndroidNetworking.download(MiscHandler.GetRandomServer("biogram.apk"), UpdateFolder.getAbsolutePath(), Filename)
        .setPriority(Priority.MEDIUM)
        .build()
        .startDownload(null);
    }
}
