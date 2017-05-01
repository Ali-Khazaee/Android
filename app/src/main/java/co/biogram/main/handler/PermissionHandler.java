package co.biogram.main.handler;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionHandler
{
    private AppCompatActivity _AppCompatActivity;
    private PermissionEvent _PermissionEvent;
    private Fragment _Fragment;
    private String _Permission;
    private int _RequestCode;

    public PermissionHandler(String P, int RC, AppCompatActivity A, PermissionEvent PE)
    {
        _AppCompatActivity = A;
        _PermissionEvent = PE;
        _RequestCode = RC;
        _Permission = P;

        CheckPermission();
    }

    public PermissionHandler(String P, int RC, Fragment F, PermissionEvent PE)
    {
        _Fragment = F;
        _PermissionEvent = PE;
        _RequestCode = RC;
        _Permission = P;

        CheckPermission();
    }

    private void CheckPermission()
    {
        int PermissionCheck;

        if (_AppCompatActivity != null)
            PermissionCheck = ContextCompat.checkSelfPermission(_AppCompatActivity, _Permission);
        else
            PermissionCheck = ContextCompat.checkSelfPermission(_Fragment.getContext(), _Permission);

        if (PermissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            _PermissionEvent.OnGranted();
            return;
        }

        if (_AppCompatActivity != null)
            ActivityCompat.requestPermissions(_AppCompatActivity, new String[] { _Permission }, _RequestCode);
        else
            _Fragment.requestPermissions(new String[] { _Permission }, _RequestCode);
    }

    public void GetRequestPermissionResult(int RC, String Permissions[], int[] GrantResults)
    {
        if (_RequestCode == RC)
        {
            for (int i = 0; i < Permissions.length; i++)
            {
                if (Permissions[i].equals(_Permission))
                {
                    if (GrantResults[i] == PackageManager.PERMISSION_GRANTED)
                        _PermissionEvent.OnGranted();
                    else
                        _PermissionEvent.OnFailed();
                }
            }
        }
    }

    public interface PermissionEvent
    {
        void OnGranted();
        void OnFailed();
    }
}
