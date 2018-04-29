package co.biogram.main.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public abstract class FragmentActivity extends AppCompatActivity
{
    private OnPermissionListener Listener;
    private boolean FirstCall = true;
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

        if (FirstCall)
        {
            FirstCall = false;
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

        if (Listener != null)
        {
            for (int I = 0; I < Permissions.length; I++)
            {
                if (Permissions[I].equals(Permission))
                {
                    Listener.OnPermission(GrantResults[I] == PackageManager.PERMISSION_GRANTED);
                }
            }
        }
    }

    public void RequestPermission(String p, OnPermissionListener l)
    {
        Listener = l;
        Permission = p;

        ActivityCompat.requestPermissions(this, new String[] { p }, 555);
    }

    public interface OnPermissionListener
    {
        void OnPermission(boolean Result);
    }
}
