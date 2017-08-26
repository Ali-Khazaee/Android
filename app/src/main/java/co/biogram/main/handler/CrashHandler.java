package co.biogram.main.handler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import co.biogram.main.activity.CrashActivity;

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
                Report.append("Error Report : ").append(new Date().toString());
                Report.append("\n\nInformation :\n");

                try
                {
                    PackageInfo Package = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

                    Report.append("\nVERSION         : ").append(Package.versionName);
                    Report.append("\nPACKAGE         : ").append(Package.packageName);
                    Report.append("\nPHONE-MODEL     : ").append(Build.MODEL);
                    Report.append("\nANDROID-VERSION : ").append(Build.VERSION.RELEASE);
                    Report.append("\nBOARD           : ").append(Build.BOARD);
                    Report.append("\nBRAND           : ").append(Build.BRAND);
                    Report.append("\nDEVICE          : ").append(Build.DEVICE);
                    Report.append("\nDISPLAY         : ").append(Build.DISPLAY);
                    Report.append("\nFINGER-PRINT    : ").append(Build.FINGERPRINT);
                    Report.append("\nHOST            : ").append(Build.HOST);
                    Report.append("\nID              : ").append(Build.ID);
                    Report.append("\nPRODUCT         : ").append(Build.PRODUCT);
                    Report.append("\nMANUFACTURER    : ").append(Build.MANUFACTURER);
                    Report.append("\nTAGS            : ").append(Build.TAGS);
                    Report.append("\nTIME            : ").append(Build.TIME);
                    Report.append("\nTYPE            : ").append(Build.TYPE);
                    Report.append("\nUSER            : ").append(Build.USER);

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

                /*Intent intent = new Intent(context, CrashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
        });
    }
}
