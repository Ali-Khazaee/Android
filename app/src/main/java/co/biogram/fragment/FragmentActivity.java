package co.biogram.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import co.biogram.main.handler.MiscHandler;

public class FragmentActivity extends Activity
{
    private OnPermissionListener PermissionListener;
    private FragmentManager Manager;
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

        if (PermissionListener == null)
            return;

        for (int I = 0; I < Permissions.length; I++)
        {
            if (Permissions[I].equals(Permission))
            {
                if (GrantResults[I] == PackageManager.PERMISSION_GRANTED)
                    PermissionListener.OnGranted();
                else
                    PermissionListener.OnDenied();
            }
        }
    }

    public void RequestPermission(String permission, OnPermissionListener Listener)
    {
        Permission = permission;
        PermissionListener = Listener;

        if (MiscHandler.HasPermission(this, Permission))
        {
            PermissionListener.OnGranted();
            return;
        }

        ActivityCompat.requestPermissions(this, new String[] { Permission }, 555);
    }

    public interface OnPermissionListener
    {
        void OnGranted();
        void OnDenied();
    }
}
