package co.biogram.main.service;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;

public class SyncService extends Service
{
    private static SyncAdapter syncAdapter = null;
    private static final Object SyncAdapterLock = new Object();

    @Override
    public void onCreate()
    {
        synchronized (SyncAdapterLock)
        {
            if (syncAdapter == null)
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return syncAdapter.getSyncAdapterBinder();
    }

    private class SyncAdapter extends AbstractThreadedSyncAdapter
    {
        SyncAdapter(Context context, boolean autoInitialize)
        {
            super(context, autoInitialize);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
        {

        }
    }
}
