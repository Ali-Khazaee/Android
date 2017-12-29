package co.biogram.main.ui.general;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.Misc;

public class CropViewUI extends FragmentBase
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
        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextWhite);
        RelativeLayoutMain.setClickable(true);

        final CropImageView CropImageViewMain = new CropImageView(GetActivity());
        CropImageViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        CropImageViewMain.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        CropImageViewMain.setFixedAspectRatio(true);
        CropImageViewMain.setAutoZoomEnabled(true);
        CropImageViewMain.setImageBitmap(BitmapFactory.decodeFile(Path));

        RelativeLayoutMain.addView(CropImageViewMain);

        RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewDoneParam.addRule(Misc.Align("L"));

        ImageView ImageViewDone = new ImageView(GetActivity());
        ImageViewDone.setPadding(Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6));
        ImageViewDone.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
                    CropImageViewMain.getCroppedImage().compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                    File file = new File(CacheHandler.CacheDir(GetActivity()), System.currentTimeMillis() + ".jpg");
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

                GetActivity().onBackPressed();
            }
        });

        RelativeLayoutMain.addView(ImageViewDone);

        RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewCloseParam.addRule(Misc.Align("R"));

        ImageView ImageViewClose = new ImageView(GetActivity());
        ImageViewClose.setPadding(Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6));
        ImageViewClose.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewClose.setLayoutParams(ImageViewCloseParam);
        ImageViewClose.setImageResource(R.drawable.close_white);
        ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutMain.addView(ImageViewClose);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        GetActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause()
    {
        GetActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public interface OnCropListener
    {
        void OnCrop(String Path);
    }
}
