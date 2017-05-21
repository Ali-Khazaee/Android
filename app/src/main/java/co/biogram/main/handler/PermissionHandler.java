package co.biogram.main.handler;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

public class PermissionHandler
{
    private FragmentActivity _Activity;
    private final PermissionEvent _PermissionEvent;
    private Fragment _Fragment;
    private final String _Permission;
    private final int _RequestCode;

    public PermissionHandler(String P, int RC, FragmentActivity A, PermissionEvent PE)
    {
        _Activity = A;
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
