package co.biogram.main.ui.general;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.fragment.FragmentView;

public class CameraViewUI extends FragmentView {
    private int Width = 0;
    private int Height = 0;
    private boolean IsProfile = true;
    private CameraView CameraViewMain;
    private OnCaptureListener Listener;

    public CameraViewUI(int W, int H, boolean P, OnCaptureListener L) {
        Width = W;
        Height = H;
        IsProfile = P;
        Listener = L;
    }

    private static Bitmap DecodeBitmap(byte[] Source, int MW, int MH) {
        int O = 0;
        boolean Flip = false;

        try {
            InputStream IS = new ByteArrayInputStream(Source);
            Integer O2 = new ExifInterface(IS).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            IS.close();

            switch (O2) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    O = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    O = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    O = 270;
                    break;
            }

            Flip = O2 == ExifInterface.ORIENTATION_FLIP_HORIZONTAL || O2 == ExifInterface.ORIENTATION_FLIP_VERTICAL || O2 == ExifInterface.ORIENTATION_TRANSPOSE || O2 == ExifInterface.ORIENTATION_TRANSVERSE;
        } catch (Exception e) {
            Misc.Debug("CameraViewUI-DecodeBitmap: " + e.toString());
        }

        Bitmap bitmap;

        if (MW != 0 || MH != 0) {
            BitmapFactory.Options O2 = new BitmapFactory.Options();
            O2.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(Source, 0, Source.length, O2);

            int H = O2.outHeight;
            int W = O2.outWidth;

            if (O % 180 != 0) {
                H = O2.outWidth;
                W = O2.outHeight;
            }

            O2.inSampleSize = Misc.SampleSize(W, H, MW, MH);
            O2.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeByteArray(Source, 0, Source.length, O2);
        } else {
            bitmap = BitmapFactory.decodeByteArray(Source, 0, Source.length);
        }

        if (O != 0 || Flip) {
            Matrix matrix = new Matrix();
            matrix.setRotate(O);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    @Override
    public void OnCreate() {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);

        CameraViewMain = new CameraView(Activity);
        CameraViewMain.setLayoutParams(new CameraView.LayoutParams(CameraView.LayoutParams.MATCH_PARENT, CameraView.LayoutParams.MATCH_PARENT));
        CameraViewMain.setKeepScreenOn(true);
        CameraViewMain.setJpegQuality(100);
        CameraViewMain.setFlash(Flash.AUTO);
        CameraViewMain.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        CameraViewMain.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER);
        CameraViewMain.mapGesture(Gesture.LONG_TAP, GestureAction.CAPTURE);
        CameraViewMain.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] Source) {
                Activity.GetManager().OpenView(new CropViewUI(DecodeBitmap(Source, Width, Height), IsProfile, new CropViewUI.OnCropListener() {
                    @Override
                    public void OnCrop(Bitmap bitmap) {
                        Listener.OnCapture(bitmap);
                        Activity.onBackPressed();
                    }
                }), "CropViewUI");
            }
        });

        RelativeLayoutMain.addView(CameraViewMain);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(75));
        RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(Activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutMain.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams ImageViewPickParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
        ImageViewPickParam.setMargins(Misc.ToDP(50), 0, Misc.ToDP(50), 0);
        ImageViewPickParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewPick = new ImageView(Activity);
        ImageViewPick.setLayoutParams(ImageViewPickParam);
        ImageViewPick.setImageResource(R.drawable.___general_camera_pick);
        ImageViewPick.setId(Misc.generateViewId());
        ImageViewPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator SizeX = ObjectAnimator.ofFloat(v, "scaleX", 1.35f);
                SizeX.setDuration(200);

                ObjectAnimator SizeY = ObjectAnimator.ofFloat(v, "scaleY", 1.35f);
                SizeY.setDuration(200);

                ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(v, "scaleX", 1f);
                SizeX2.setDuration(200);
                SizeX2.setStartDelay(200);

                ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(v, "scaleY", 1f);
                SizeY2.setDuration(200);
                SizeY2.setStartDelay(200);

                AnimatorSet AnimationSet = new AnimatorSet();
                AnimationSet.playTogether(SizeX, SizeY, SizeX2, SizeY2);
                AnimationSet.start();

                CameraViewMain.capturePicture();
            }
        });

        RelativeLayoutBottom.addView(ImageViewPick);

        RelativeLayout.LayoutParams ImageViewFlashParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
        ImageViewFlashParam.addRule(RelativeLayout.LEFT_OF, ImageViewPick.getId());
        ImageViewFlashParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final ImageView ImageViewFlash = new ImageView(Activity);
        ImageViewFlash.setLayoutParams(ImageViewFlashParam);
        ImageViewFlash.setImageResource(R.drawable.___general_camera_flash_auto);
        ImageViewFlash.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
        ImageViewFlash.setOnClickListener(new View.OnClickListener() {
            private int Type = 2;

            @Override
            public void onClick(View v) {
                switch (Type) {
                    case 0:
                        ImageViewFlash.setImageResource(R.drawable.___general_camera_flash_on);
                        CameraViewMain.setFlash(Flash.ON);
                        Type = 1;
                        break;
                    case 1:
                        ImageViewFlash.setImageResource(R.drawable.___general_camera_flash_auto);
                        CameraViewMain.setFlash(Flash.AUTO);
                        Type = 2;
                        break;
                    case 2:
                        ImageViewFlash.setImageResource(R.drawable.___general_camera_flash_off);
                        CameraViewMain.setFlash(Flash.OFF);
                        Type = 0;
                        break;
                }
            }
        });

        RelativeLayoutBottom.addView(ImageViewFlash);

        RelativeLayout.LayoutParams ImageViewSwitchParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
        ImageViewSwitchParam.addRule(RelativeLayout.RIGHT_OF, ImageViewPick.getId());
        ImageViewSwitchParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewSwitch = new ImageView(Activity);
        ImageViewSwitch.setLayoutParams(ImageViewSwitchParam);
        ImageViewSwitch.setImageResource(R.drawable.___general_camera_switch);
        ImageViewSwitch.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
        ImageViewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraViewMain.toggleFacing();
            }
        });

        RelativeLayoutBottom.addView(ImageViewSwitch);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume() {
        Activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CameraViewMain.start();
    }

    @Override
    public void OnPause() {
        Activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CameraViewMain.stop();
    }

    @Override
    public void OnDestroy() {
        CameraViewMain.destroy();
    }

    public interface OnCaptureListener {
        void OnCapture(Bitmap bitmap);
    }
}
