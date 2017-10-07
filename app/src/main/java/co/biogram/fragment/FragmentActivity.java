package co.biogram.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class FragmentActivity extends Activity
{
    private FragmentManager Manager;

    public FragmentManager GetManager()
    {
        if (Manager == null)
            Manager = new FragmentManager(this);

        return Manager;
    }

    @Override
    public void onBackPressed()
    {
        if (Manager.HandleBack())
            super.onBackPressed();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Manager.OnPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Manager.OnResume();
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        super.onActivityResult(RequestCode, ResultCode, intent);
        Manager.OnActivityResult(RequestCode, ResultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);
        Manager.OnPermissionResult(RequestCode, Permissions, GrantResults);

        if (GrantResults.length > 0 && GrantResults[0] == PackageManager.PERMISSION_GRANTED)
        {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.
        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
        }
        return;
    }
    }

    public void RequestPermission(String[] Permissions, int RequestCode)
    {
        int PermissionCheck = ContextCompat.checkSelfPermission(this, _Permission);

        if (PermissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            _PermissionEvent.OnGranted();
            return;
        }

        ActivityCompat.requestPermissions(this, Permissions, RequestCode);
    }
}
