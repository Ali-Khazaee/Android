package co.biogram.main.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.CameraHandler;
import co.biogram.main.handler.MiscHandler;

class CameraViewUI extends FragmentBase
{
    private CameraHandler Camera;

    @Override
    public void OnCreate()
    {
        int D50 = MiscHandler.ToDimension(GetActivity(), 50);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.Black);

        FrameLayout FrameLayoutMain = new FrameLayout(GetActivity());
        FrameLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        RelativeLayoutMain.addView(FrameLayoutMain);

        Camera = new CameraHandler(GetActivity());

        FrameLayoutMain.addView(Camera);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 75));
        RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutMain.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams ImageViewPickParam = new RelativeLayout.LayoutParams(D50, D50);
        ImageViewPickParam.setMargins(D50, 0, D50, 0);
        ImageViewPickParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final ImageView ImageViewPick = new ImageView(GetActivity());
        ImageViewPick.setLayoutParams(ImageViewPickParam);
        ImageViewPick.setImageResource(R.drawable.ic_camera_pick);
        ImageViewPick.setId(MiscHandler.GenerateViewID());
        ImageViewPick.setOnClickListener(new View.OnClickListener()
        {
            private boolean IsClicked = false;

            @Override
            public void onClick(View v)
            {
                if (IsClicked)
                    return;

                IsClicked = true;

                ObjectAnimator SizeX = ObjectAnimator.ofFloat(ImageViewPick, "scaleX", 1.35f);
                SizeX.setDuration(200);

                ObjectAnimator SizeY = ObjectAnimator.ofFloat(ImageViewPick, "scaleY", 1.35f);
                SizeY.setDuration(200);

                ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(ImageViewPick, "scaleX", 1f);
                SizeX2.setDuration(200);
                SizeX2.setStartDelay(200);

                ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(ImageViewPick, "scaleY", 1f);
                SizeY2.setDuration(200);
                SizeY2.setStartDelay(200);

                AnimatorSet AnimationSet = new AnimatorSet();
                AnimationSet.playTogether(SizeX, SizeY, SizeX2, SizeY2);
                AnimationSet.start();

                Camera.TakePicture(new CameraHandler.CameraListener()
                {
                    @Override
                    public void OnCapture(byte[] Data)
                    {
                        IsClicked = false;
                        GetActivity().GetManager().OpenView(new ImagePreviewUI(GetActivity(), Data), R.id.WelcomeActivityContainer, "ImagePreviewUI");
                    }

                    @Override
                    public void OnFailed()
                    {
                        IsClicked = false;
                    }
                });
            }
        });

        RelativeLayoutBottom.addView(ImageViewPick);

        RelativeLayout.LayoutParams ImageViewFlashParam = new RelativeLayout.LayoutParams(D50, D50);
        ImageViewFlashParam.addRule(RelativeLayout.LEFT_OF, ImageViewPick.getId());
        ImageViewFlashParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final ImageView ImageViewFlash = new ImageView(GetActivity());
        ImageViewFlash.setLayoutParams(ImageViewFlashParam);
        ImageViewFlash.setImageResource(R.drawable.ic_camera_flash_auto);
        ImageViewFlash.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
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

        RelativeLayout.LayoutParams ImageViewSwitchParam = new RelativeLayout.LayoutParams(D50, D50);
        ImageViewSwitchParam.addRule(RelativeLayout.RIGHT_OF, ImageViewPick.getId());
        ImageViewSwitchParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewSwitch = new ImageView(GetActivity());
        ImageViewSwitch.setLayoutParams(ImageViewSwitchParam);
        ImageViewSwitch.setImageResource(R.drawable.ic_camera_switch);
        ImageViewSwitch.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
        ImageViewSwitch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Camera.SwitchCamera(); } });

        RelativeLayoutBottom.addView(ImageViewSwitch);

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
        GetActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Camera.Stop();
    }

    @Override
    public void OnDestroy()
    {
        Camera.Release();
        super.OnDestroy();
    }
}
