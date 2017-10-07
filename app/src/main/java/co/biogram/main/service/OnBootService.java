package co.biogram.main.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootService extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED"))
            context.startService(new Intent(context, SocketService.class));
    }
}
