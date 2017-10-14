package co.biogram.main.ui;

import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.CameraHandler;
import co.biogram.main.handler.MiscHandler;

class CameraView extends FragmentBase
{
    private CameraHandler Camera;

    @Override
    public void OnCreate()
    {
        FragmentActivity activity = GetActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        FrameLayout FrameLayoutMain = new FrameLayout(activity);
        FrameLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        RelativeLayoutMain.addView(FrameLayoutMain);

        Camera = new CameraHandler(activity);

        FrameLayoutMain.addView(Camera);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 75));
        RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayout.LayoutParams ImageViewPickParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 50), MiscHandler.ToDimension(activity, 50));
        ImageViewPickParam.setMargins(MiscHandler.ToDimension(activity, 50), 0, MiscHandler.ToDimension(activity, 50), 0);
        ImageViewPickParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewPick = new ImageView(activity);
        ImageViewPick.setLayoutParams(ImageViewPickParam);
        ImageViewPick.setImageResource(R.drawable.ic_camera_pick);
        ImageViewPick.setId(MiscHandler.GenerateViewID());

        RelativeLayoutBottom.addView(ImageViewPick);

        RelativeLayout.LayoutParams ImageViewFlashParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 50), MiscHandler.ToDimension(activity, 50));
        ImageViewFlashParam.addRule(RelativeLayout.LEFT_OF, ImageViewPick.getId());
        ImageViewFlashParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final ImageView ImageViewFlash = new ImageView(activity);
        ImageViewFlash.setLayoutParams(ImageViewFlashParam);
        ImageViewFlash.setImageResource(R.drawable.ic_camera_flash_auto);
        ImageViewFlash.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewFlash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (Camera.SwitchFlash())
                {
                    case 0: ImageViewFlash.setImageResource(R.drawable.ic_camera_flash_auto); break;
                    case 1: ImageViewFlash.setImageResource(R.drawable.ic_camera_flash_on); break;
                    case 2: ImageViewFlash.setImageResource(R.drawable.ic_camera_flash_off); break;
                }
            }
        });

        RelativeLayoutBottom.addView(ImageViewFlash);

        RelativeLayout.LayoutParams ImageViewSwitchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 50), MiscHandler.ToDimension(activity, 50));
        ImageViewSwitchParam.addRule(RelativeLayout.RIGHT_OF, ImageViewPick.getId());
        ImageViewSwitchParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewSwitch = new ImageView(activity);
        ImageViewSwitch.setLayoutParams(ImageViewSwitchParam);
        ImageViewSwitch.setImageResource(R.drawable.ic_camera_switch);
        ImageViewSwitch.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewSwitch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Camera.SwitchCamera(); } });

        RelativeLayoutBottom.addView(ImageViewSwitch);

        RelativeLayoutMain.addView(RelativeLayoutBottom);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        GetActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Camera.Start();
    }

    @Override
    public void OnPause()
    {
        Camera.Stop();
    }

    @Override
    public void OnDestroy()
    {
        GetActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Camera.Release();
        super.OnDestroy();
    }
}
