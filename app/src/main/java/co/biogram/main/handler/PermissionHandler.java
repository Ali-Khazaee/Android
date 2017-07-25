package co.biogram.main.handler;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

public class PermissionHandler
{
    private FragmentActivity _Activity;
    private Fragment _Fragment;
    private final PermissionEvent _PermissionEvent;
    private final String _Permission;
    private final int _RequestCode;

    public PermissionHandler(String permission, int requestCode, FragmentActivity activity, PermissionEvent permissionEvent)
    {
        _Activity = activity;
        _PermissionEvent = permissionEvent;
        _RequestCode = requestCode;
        _Permission = permission;

        CheckPermission();
    }

    public PermissionHandler(String permission, int requestCode, Fragment fragment, PermissionEvent permissionEvent)
    {
        _Fragment = fragment;
        _PermissionEvent = permissionEvent;
        _RequestCode = requestCode;
        _Permission = permission;

        CheckPermission();
    }

    private void CheckPermission()
    {
        int PermissionCheck;

        if (_Activity != null)
            PermissionCheck = ContextCompat.checkSelfPermission(_Activity, _Permission);
        else
            PermissionCheck = ContextCompat.checkSelfPermission(_Fragment.getContext(), _Permission);

        if (PermissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            _PermissionEvent.OnGranted();
            return;
        }

        if (_Activity != null)
            ActivityCompat.requestPermissions(_Activity, new String[] { _Permission }, _RequestCode);
        else
            _Fragment.requestPermissions(new String[] { _Permission }, _RequestCode);
    }

    public void GetRequestPermissionResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (_RequestCode == requestCode)
        {
            for (int i = 0; i < permissions.length; i++)
            {
                if (permissions[i].equals(_Permission))
                {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
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
