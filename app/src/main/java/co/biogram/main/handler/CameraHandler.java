package co.biogram.main.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import co.biogram.fragment.FragmentActivity;

public class CameraHandler extends SurfaceView
{
    private int CameraFlash = 0;
    private int CameraID = 0;
    private Camera camera;

    public CameraHandler(Context context)
    {
        super(context);

        camera = Camera.open(CameraID);
        camera.setDisplayOrientation(90);

        getHolder().addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                try
                {
                    camera.setPreviewDisplay(holder);
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("CameraHandler-SurfaceCreated: " + e.toString());
                }
            }

            @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
            @Override public void surfaceDestroyed(SurfaceHolder holder) { }
        });
    }

    public int SwitchFlash()
    {
        Camera.Parameters parameters = camera.getParameters();

        switch (CameraFlash)
        {
            case 0:
                CameraFlash = 1;
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                break;
            case 1:
                CameraFlash = 2;
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
            case 2:
                CameraFlash = 0;
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
        }

        camera.setParameters(parameters);
        camera.startPreview();

        return CameraFlash;
    }

    public void SwitchCamera()
    {
        camera.stopPreview();
        camera.release();

        if (CameraID == Camera.CameraInfo.CAMERA_FACING_BACK)
            CameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
        else
            CameraID = Camera.CameraInfo.CAMERA_FACING_BACK;

        camera = Camera.open(CameraID);

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(CameraID, info);

        int Degrees = 0;

        switch (((FragmentActivity) getContext()).getWindowManager().getDefaultDisplay().getRotation())
        {
            case Surface.ROTATION_0: Degrees = 0; break;
            case Surface.ROTATION_90: Degrees = 90; break;
            case Surface.ROTATION_180: Degrees = 180; break;
            case Surface.ROTATION_270: Degrees = 270; break;
        }

        int Result = (info.orientation - Degrees + 360) % 360;

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            Result = (info.orientation + Degrees) % 360;
            Result = (360 - Result) % 360;
        }

        camera.setDisplayOrientation(Result);

        try
        {
            camera.setPreviewDisplay(getHolder());
        }
        catch (Exception e)
        {
            MiscHandler.Debug("CameraHandler-SwitchCamera: " + e.toString());
        }

        camera.startPreview();
    }

    public void Start()
    {
        if (camera != null)
            camera.startPreview();
    }

    public void Stop()
    {
        if (camera != null)
            camera.stopPreview();
    }

    public void Release()
    {
        if (camera != null)
        {
            camera.release();
            camera = null;
        }
    }
}
