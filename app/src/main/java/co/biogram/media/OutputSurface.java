package co.biogram.media;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.view.Surface;

@TargetApi(18)
class OutputSurface implements SurfaceTexture.OnFrameAvailableListener {
    private final Object mFrameSyncObject = new Object();
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private boolean mFrameAvailable;
    private TextureRender mTextureRender;

    OutputSurface() {
        mTextureRender = new TextureRender();
        mTextureRender.surfaceCreated();
        mSurfaceTexture = new SurfaceTexture(mTextureRender.getTextureId());
        mSurfaceTexture.setOnFrameAvailableListener(this);
        mSurface = new Surface(mSurfaceTexture);
    }

    public void release() {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface);
            EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(mEGLDisplay);
        }

        mSurface.release();
        mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        mEGLContext = EGL14.EGL_NO_CONTEXT;
        mEGLSurface = EGL14.EGL_NO_SURFACE;
        mTextureRender = null;
        mSurface = null;
        mSurfaceTexture = null;
    }

    Surface getSurface() {
        return mSurface;
    }

    void awaitNewImage() {
        final int TIMEOUT_MS = 10000;

        synchronized (mFrameSyncObject) {
            while (!mFrameAvailable) {
                try {
                    mFrameSyncObject.wait(TIMEOUT_MS);

                    if (!mFrameAvailable)
                        throw new RuntimeException("Surface frame wait timed out");
                } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }

            mFrameAvailable = false;
        }

        mTextureRender.checkGlError("before updateTexImage");
        mSurfaceTexture.updateTexImage();
    }

    void drawImage() {
        mTextureRender.drawFrame(mSurfaceTexture);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture st) {
        synchronized (mFrameSyncObject) {
            if (mFrameAvailable)
                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");

            mFrameAvailable = true;
            mFrameSyncObject.notifyAll();
        }
    }
}
