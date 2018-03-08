package co.biogram.main.ui.general;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.Misc;

public class CropViewUI extends FragmentView
{
    private String Path;
    private OnCropListener Crop;

    public CropViewUI(String path, OnCropListener l)
    {
        Path = path;
        Crop = l;
    }

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextWhite);
        RelativeLayoutMain.setClickable(true);

        final CropImageView CropImageViewMain = new CropImageView(Activity);
        CropImageViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        CropImageViewMain.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        CropImageViewMain.setFixedAspectRatio(false);
        CropImageViewMain.setAutoZoomEnabled(true);
        CropImageViewMain.setImageBitmap(BitmapFactory.decodeFile(Path));

        RelativeLayoutMain.addView(CropImageViewMain);

        RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewDoneParam.addRule(Misc.Align("L"));

        ImageView ImageViewDone = new ImageView(Activity);
        ImageViewDone.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
        ImageViewDone.setLayoutParams(ImageViewDoneParam);
        ImageViewDone.setImageResource(R.drawable.done_white);
        ImageViewDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                    CropImageViewMain.getCroppedImage().compress(Bitmap.CompressFormat.PNG, 100, BAOS);

                    File file = new File(CacheHandler.TempDir(Activity), System.currentTimeMillis() + ".jpg");
                    file.createNewFile();

                    FileOutputStream FOS = new FileOutputStream(file);
                    FOS.write(BAOS.toByteArray());
                    FOS.flush();
                    FOS.close();

                    Crop.OnCrop(file.getAbsolutePath());
                }
                catch (Exception e)
                {
                    Misc.Debug("CropViewUI: " + e.toString());
                }

                Activity.onBackPressed();
            }
        });

        RelativeLayoutMain.addView(ImageViewDone);

        RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewCloseParam.addRule(Misc.Align("R"));

        ImageView ImageViewClose = new ImageView(Activity);
        ImageViewClose.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
        ImageViewClose.setLayoutParams(ImageViewCloseParam);
        ImageViewClose.setImageResource(R.drawable.close_white);
        ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });

        RelativeLayoutMain.addView(ImageViewClose);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        Activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause()
    {
        Activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public interface OnCropListener
    {
        void OnCrop(String Path);
    }
}
