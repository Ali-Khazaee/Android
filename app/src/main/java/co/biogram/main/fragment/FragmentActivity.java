package co.biogram.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import co.biogram.main.handler.Misc;

public abstract class FragmentActivity extends Activity
{
    private OnPermissionListener PermissionListener;
    private FragmentManager Manager;
    private boolean Resume = true;
    private String Permission;

    public FragmentManager GetManager()
    {
        if (Manager == null)
            Manager = new FragmentManager(this);

        return Manager;
    }

    @Override
    public void onBackPressed()
    {
        if (GetManager().HandleBack())
            super.onBackPressed();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        GetManager().OnPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (Resume)
        {
            Resume = false;
            return;
        }

        GetManager().OnResume();
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        super.onActivityResult(RequestCode, ResultCode, intent);
        GetManager().OnActivityResult(RequestCode, ResultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);

        for (int I = 0; I < Permissions.length; I++)
            if (PermissionListener != null && Permissions[I].equals(Permission))
                PermissionListener.OnResult(GrantResults[I] == PackageManager.PERMISSION_GRANTED);
    }

    public void RequestPermission(@NonNull String permission, @NonNull OnPermissionListener listener)
    {
        if (Misc.checkPermission(permission))
        {
            listener.OnResult(true);
            return;
        }

        Permission = permission;
        PermissionListener = listener;

        ActivityCompat.requestPermissions(this, new String[] { Permission }, 555);
    }

    public interface OnPermissionListener
    {
        void OnResult(boolean Granted);
    }
}
