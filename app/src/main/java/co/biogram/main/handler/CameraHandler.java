package co.biogram.main.handler;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.Surface;
import android.view.TextureView;

import co.biogram.main.fragment.FragmentActivity;

@SuppressWarnings("deprecation")
public class CameraHandler extends TextureView
{
    private int CameraFlash = 0;
    private int CameraID = 0;
    private Camera camera;

    public CameraHandler(Context context)
    {
        super(context);

        camera = Camera.open(CameraID);
        camera.setDisplayOrientation(90);

        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);

        camera.setParameters(parameters);

        setSurfaceTextureListener(new SurfaceTextureListener()
        {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture s, int w, int h)
            {
                try
                {
                    camera.setPreviewTexture(s);
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("CameraHandler-OnSurfaceTextureAvailable" + e.toString());
                }
            }

            @Override public void onSurfaceTextureSizeChanged(SurfaceTexture s, int w, int h) { }
            @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture s) { return false; }
            @Override public void onSurfaceTextureUpdated(SurfaceTexture s) { }
        });
    }

    public void TakePicture(final CameraListener Listener)
    {
        try
        {
            camera.takePicture(null, null, new Camera.PictureCallback()
            {
                @Override
                public void onPictureTaken(byte[] Data, Camera camera)
                {
                    Listener.OnCapture(Data);
                }
            });
        }
        catch (Exception e)
        {
            Listener.OnFailed();
            MiscHandler.Debug("CameraHandler-TakePicture: " + e.toString());
        }
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
            camera.setPreviewTexture(getSurfaceTexture());
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

    public interface CameraListener
    {
        void OnCapture(byte[] Data);
        void OnFailed();
    }
}
