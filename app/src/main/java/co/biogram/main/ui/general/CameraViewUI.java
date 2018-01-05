package co.biogram.main.ui.general;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.CameraHandler;
import co.biogram.main.handler.Misc;

public class CameraViewUI extends FragmentView
{
    private CameraHandler Camera;

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextWhite);

        FrameLayout FrameLayoutMain = new FrameLayout(GetActivity());
        FrameLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        RelativeLayoutMain.addView(FrameLayoutMain);

        Camera = new CameraHandler(GetActivity());

        FrameLayoutMain.addView(Camera);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(75));
        RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutMain.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams ImageViewPickParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
        ImageViewPickParam.setMargins(Misc.ToDP(50), 0, Misc.ToDP(50), 0);
        ImageViewPickParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final ImageView ImageViewPick = new ImageView(GetActivity());
        ImageViewPick.setLayoutParams(ImageViewPickParam);
        ImageViewPick.setImageResource(R.drawable.camera_pick_white);
        ImageViewPick.setId(Misc.GenerateViewID());
        ImageViewPick.setOnClickListener(new View.OnClickListener()
        {
            boolean IsClicked = false;

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
                    public void OnCapture(byte[] Data, int O)
                    {
                        IsClicked = false;
                        GetActivity().GetManager().OpenView(new ImagePreviewUI(GetActivity(), Data, O), R.id.ContainerFull, "ImagePreviewUI");
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

        RelativeLayout.LayoutParams ImageViewFlashParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
        ImageViewFlashParam.addRule(RelativeLayout.LEFT_OF, ImageViewPick.getId());
        ImageViewFlashParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final ImageView ImageViewFlash = new ImageView(GetActivity());
        ImageViewFlash.setLayoutParams(ImageViewFlashParam);
        ImageViewFlash.setImageResource(R.drawable.flash_auto_white);
        ImageViewFlash.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
        ImageViewFlash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (Camera.SwitchFlash())
                {
                    case 0: ImageViewFlash.setImageResource(R.drawable.flash_auto_white); break;
                    case 1: ImageViewFlash.setImageResource(R.drawable.flash_on_white); break;
                    case 2: ImageViewFlash.setImageResource(R.drawable.flash_off_white); break;
                }
            }
        });

        RelativeLayoutBottom.addView(ImageViewFlash);

        RelativeLayout.LayoutParams ImageViewSwitchParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
        ImageViewSwitchParam.addRule(RelativeLayout.RIGHT_OF, ImageViewPick.getId());
        ImageViewSwitchParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewSwitch = new ImageView(GetActivity());
        ImageViewSwitch.setLayoutParams(ImageViewSwitchParam);
        ImageViewSwitch.setImageResource(R.drawable.camera_switch_white);
        ImageViewSwitch.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
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
