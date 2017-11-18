package co.biogram.main.handler;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;

@SuppressWarnings("deprecation")
public class CameraHandler extends TextureView
{
    private int CameraFlash = 0;
    private int CameraID = 0;
    private Camera camera;
    private boolean IsAvaiable = false;

    public CameraHandler(Context context)
    {
        super(context);
    }

    private void Init()
    {
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
                    camera.startPreview();
                    IsAvaiable = true;
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("CameraHandler-OnSurfaceTextureAvailable" + e.toString());
                }
            }

            @Override public void onSurfaceTextureSizeChanged(SurfaceTexture s, int w, int h) { }
            @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture s) { IsAvaiable = false; return false; }
            @Override public void onSurfaceTextureUpdated(SurfaceTexture s) { }
        });
    }

    public void TakePicture(final CameraListener Listener)
    {
        if (!IsAvaiable)
        {
            Listener.OnFailed();
            return;
        }

        try
        {
            camera.takePicture(null, null, new Camera.PictureCallback()
            {
                @Override
                public void onPictureTaken(byte[] Data, Camera camera)
                {
                    Listener.OnCapture(Data, CameraID == Camera.CameraInfo.CAMERA_FACING_BACK ? 90 : -90);
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
        if (!IsAvaiable)
            return 0;

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
        if (!IsAvaiable)
            return;

        camera.stopPreview();
        camera.release();

        if (CameraID == Camera.CameraInfo.CAMERA_FACING_BACK)
            CameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
        else
            CameraID = Camera.CameraInfo.CAMERA_FACING_BACK;

        camera = Camera.open(CameraID);
        camera.setDisplayOrientation(90);

        try
        {
            camera.setPreviewTexture(getSurfaceTexture());
            camera.startPreview();
        }
        catch (Exception e)
        {
            MiscHandler.Debug("CameraHandler-SwitchCamera: " + e.toString());
        }
    }

    public void Start()
    {
        if (IsAvaiable && camera != null)
            camera.startPreview();
        else
            Init();
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
        void OnCapture(byte[] Data, int O);
        void OnFailed();
    }
}
