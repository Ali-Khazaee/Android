package co.biogram.main.ui.social;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.view.StickyScrollView;
import co.biogram.main.ui.view.TextView;

public class ProfileUI extends FragmentView
{
    public ProfileUI() { }

    public ProfileUI(String username)
    {

    }

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(Misc.Align("R"));

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.ProfileUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSettingParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewSettingParam.addRule(Misc.Align("L"));

        ImageView ImageViewSetting = new ImageView(Activity);
        ImageViewSetting.setLayoutParams(ImageViewSettingParam);
        ImageViewSetting.setId(Misc.ViewID());
        ImageViewSetting.setImageResource(R.drawable._inbox_search);
        ImageViewSetting.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewSetting.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { /* TODO Search */  } });

        RelativeLayoutHeader.addView(ImageViewSetting);

        RelativeLayout.LayoutParams ImageViewWriteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewWriteParam.addRule(Misc.AlignTo("L"), ImageViewSetting.getId());

        ImageView ImageViewProfile = new ImageView(Activity);
        ImageViewProfile.setLayoutParams(ImageViewWriteParam);
        ImageViewProfile.setImageResource(R.drawable._inbox_write);
        ImageViewProfile.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));
        ImageViewProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new WriteUI(), R.id.ContainerFull, "WriteUI");  } });

        RelativeLayoutHeader.addView(ImageViewProfile);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        StickyScrollView ScrollViewMain = new StickyScrollView(Activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollViewMain.addView(RelativeLayoutScroll);





        ViewMain = RelativeLayoutMain;
    }
}
