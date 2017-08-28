package co.biogram.main.handler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class CrashHandler
{
    public static void SetUp(final Context context)
    {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException(Thread t, Throwable e)
            {
                StringBuilder Report = new StringBuilder();
                Report.append("Error Report: ").append(new Date().toString());
                Report.append("\n\nInformation:\n");

                try
                {
                    PackageInfo Package = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

                    Report.append("\nVERSION:\t ").append(Package.versionName).append(" - ").append(Package.versionCode);
                    Report.append("\nPACKAGE:\t ").append(Package.packageName);
                    Report.append("\nPHONE-MODEL:\t ").append(Build.MODEL);
                    Report.append("\nANDROID-VERSION:\t ").append(Build.VERSION.RELEASE);
                    Report.append("\nBOARD:\t ").append(Build.BOARD);
                    Report.append("\nBRAND:\t ").append(Build.BRAND);
                    Report.append("\nDEVICE:\t ").append(Build.DEVICE);
                    Report.append("\nDISPLAY:\t ").append(Build.DISPLAY);
                    Report.append("\nFINGER-PRINT:\t ").append(Build.FINGERPRINT);
                    Report.append("\nHOST:\t ").append(Build.HOST);
                    Report.append("\nID:\t ").append(Build.ID);
                    Report.append("\nPRODUCT:\t ").append(Build.PRODUCT);
                    Report.append("\nMANUFACTURER:\t ").append(Build.MANUFACTURER);
                    Report.append("\nTAGS:\t ").append(Build.TAGS);
                    Report.append("\nTIME:\t ").append(Build.TIME);
                    Report.append("\nTYPE:\t ").append(Build.TYPE);
                    Report.append("\nUSER:\t ").append(Build.USER);

                    StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
                    // noinspection all
                    int BlockSize = stat.getBlockSize();
                    // noinspection all
                    int TotalBlocks = stat.getBlockCount();
                    // noinspection all
                    long AvailableBlocks = stat.getAvailableBlocks();
                    long TotalMemory = (TotalBlocks * BlockSize) / (1024 * 1024);
                    long AvailableMemory = (AvailableBlocks * BlockSize) / (1024 * 1024);

                    Report.append("\nTOTAL-INTERNAL-MEMORY     : ").append(TotalMemory).append("MB");
                    Report.append("\nAVAILABLE-INTERNAL-MEMORY : ").append(AvailableMemory).append("MB");
                }
                catch (Exception e2)
                {
                    MiscHandler.Debug("UncaughtExceptionHandler: " + e2.toString());
                }

                Report.append("\n\nStack :\n");

                Writer StackResult = new StringWriter();
                PrintWriter StackWriter = new PrintWriter(StackResult);

                e.printStackTrace(StackWriter);

                Report.append(StackResult.toString());
                Report.append("\n\nCause :\n");

                Throwable cause = e.getCause();

                while (cause != null)
                {
                    cause.printStackTrace(StackWriter);
                    Report.append(StackResult.toString());
                    cause = cause.getCause();
                }

                StackWriter.close();

                Report.append("\n\n**** End of Report ***");

                try
                {
                    String FileName = System.currentTimeMillis() + ".Crash";

                    FileOutputStream trace = context.openFileOutput(FileName, Context.MODE_PRIVATE);
                    trace.write(Report.toString().getBytes());
                    trace.close();
                }
                catch (Exception e3)
                {
                    MiscHandler.Debug("UncaughtExceptionHandler-SaveFile: " + e3.toString());
                }

                MiscHandler.Debug("\n\n" + Report.toString() + "\n\n");

                UploadCrash(context);

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
        });

        UploadCrash(context);
    }

    private static void UploadCrash(Context context)
    {
        final File CrashDir = new File(context.getFilesDir().getAbsolutePath());

        CrashDir.mkdir();

        FilenameFilter CrashFilter = new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".Crash");
            }
        };

        final String[] FileList = CrashDir.list(CrashFilter);

        if (FileList.length < 0)
            return;

        int Index = 0;
        int MaxFile = 5;
        StringBuilder WholeErrorText = new StringBuilder();

        for (String CurrentString : FileList)
        {
            if (Index++ <= MaxFile)
            {
                BufferedReader Reader = null;

                try
                {
                    String Line;
                    String FilePath = CrashDir + "/" + CurrentString;
                    Reader = new BufferedReader(new FileReader(FilePath));

                    while ((Line = Reader.readLine()) != null)
                    {
                        WholeErrorText.append(Line).append("\n");
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("CrashHandler-UploadCrash: " + e.toString());
                }
                finally
                {
                    try
                    {
                        if (Reader != null)
                            Reader.close();
                    }
                    catch (Exception e)
                    {
                        MiscHandler.Debug("CrashHandler-UploadCrash2: " + e.toString());
                    }
                }
            }
        }

        AndroidNetworking.post(MiscHandler.GetRandomServer("Crash"))
        .addBodyParameter("Crash", WholeErrorText.toString())
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    if (new JSONObject(Response).getInt("Message") == 1000)
                    {
                        for (String CurrentString : FileList)
                        {
                            File CurrentFile = new File(CrashDir + "/" + CurrentString);
                            CurrentFile.delete();
                        }
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
}
