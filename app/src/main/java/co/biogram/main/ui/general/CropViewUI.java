package co.biogram.main.ui.general;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.theartofdev.edmodo.cropper.CropImageView;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.fragment.FragmentView;

public class CropViewUI extends FragmentView {
    private Bitmap bitmap;
    private boolean Fixed = false;
    private OnCropListener Listener;

    public CropViewUI(String path, boolean isFixed, OnCropListener l) {
        bitmap = BitmapFactory.decodeFile(path);
        Fixed = isFixed;
        Listener = l;
    }

    CropViewUI(Bitmap Source, boolean isFixed, OnCropListener l) {
        bitmap = Source;
        Fixed = isFixed;
        Listener = l;
    }

    @Override
    public void OnCreate() {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextWhite);

        final CropImageView CropImageViewMain = new CropImageView(Activity);
        CropImageViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        CropImageViewMain.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        CropImageViewMain.setFixedAspectRatio(Fixed);
        CropImageViewMain.setImageBitmap(bitmap);

        RelativeLayoutMain.addView(CropImageViewMain);

        RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewDoneParam.addRule(Misc.Align("L"));

        ImageView ImageViewDone = new ImageView(Activity);
        ImageViewDone.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
        ImageViewDone.setLayoutParams(ImageViewDoneParam);
        ImageViewDone.setImageResource(R.drawable.___general_done_white);
        ImageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.OnCrop(CropImageViewMain.getCroppedImage());
                Activity.onBackPressed();
            }
        });

        RelativeLayoutMain.addView(ImageViewDone);

        RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewCloseParam.addRule(Misc.Align("R"));

        ImageView ImageViewClose = new ImageView(Activity);
        ImageViewClose.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
        ImageViewClose.setLayoutParams(ImageViewCloseParam);
        ImageViewClose.setImageResource(R.drawable.___general_close_white);
        ImageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();
            }
        });

        RelativeLayoutMain.addView(ImageViewClose);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume() {
        Activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause() {
        Activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public interface OnCropListener {
        void OnCrop(Bitmap bitmap);
    }
}
