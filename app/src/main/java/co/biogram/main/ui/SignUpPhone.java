package co.biogram.main.ui;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

class SignUpPhone extends FragmentBase
{
    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();

        if (Build.VERSION.SDK_INT > 20)
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.BlueLight));

        RelativeLayout RelativeLayoutMain = new RelativeLayout(activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollView ScrollViewMain = new ScrollView(activity);
        ScrollViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ScrollViewMain.setBackgroundResource(R.color.White);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutMain2 = new RelativeLayout(activity);
        RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutMain2);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());
        RelativeLayoutHeader.setBackgroundResource(R.color.RedLike);

        RelativeLayoutMain2.addView(RelativeLayoutHeader);

        ImageView ImageViewHeader = new ImageView(activity);
        ImageViewHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewHeader.setImageResource(R.drawable.ic_logo);

        RelativeLayoutMain2.addView(ImageViewHeader);

        ImageView ImageViewHeader1 = new ImageView(activity);
        ImageViewHeader1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewHeader1.setImageResource(R.drawable.icon);

        RelativeLayoutMain2.addView(ImageViewHeader1);

        ImageView ImageViewHeader2 = new ImageView(activity);
        ImageViewHeader2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewHeader2.setImageResource(R.drawable.icon);

        RelativeLayoutMain2.addView(ImageViewHeader2);

        ImageView ImageViewHeader3 = new ImageView(activity);
        ImageViewHeader3.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewHeader3.setImageResource(R.drawable.ic_logo);

        RelativeLayoutMain2.addView(ImageViewHeader3);

        ImageView ImageViewHeader4 = new ImageView(activity);
        ImageViewHeader4.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewHeader4.setImageResource(R.drawable.icon);

        RelativeLayoutMain2.addView(ImageViewHeader4);


        RelativeLayoutMain.startAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left));

        SetRootView(RelativeLayoutMain);
    }
}
