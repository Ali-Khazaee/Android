package co.biogram.main.ui.social;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.ui.view.TextView;

public class InboxUI extends FragmentBase
{
    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 56)));
        RelativeLayoutHeader.setBackgroundResource(MiscHandler.IsDark(GetActivity()) ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewAddParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
        ImageViewAddParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewWrite = new ImageView(GetActivity());
        ImageViewWrite.setLayoutParams(ImageViewAddParam);
        ImageViewWrite.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewWrite.setId(MiscHandler.GenerateViewID());
        ImageViewWrite.setImageResource(R.drawable.ic_write_plus);
        ImageViewWrite.setPadding(MiscHandler.ToDimension(GetActivity(), 13), MiscHandler.ToDimension(GetActivity(), 13), MiscHandler.ToDimension(GetActivity(), 13), MiscHandler.ToDimension(GetActivity(), 13));
        ImageViewWrite.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { } });

        RelativeLayoutHeader.addView(ImageViewWrite);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewWrite.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(GetActivity(), MiscHandler.IsDark(GetActivity()) ? R.color.TextDark : R.color.TextWhite));
        TextViewTitle.setPadding(0, MiscHandler.ToDimension(GetActivity(), 6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.InboxUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
        ImageViewSearchParam.addRule(MiscHandler.Align("L"));

        ImageView ImageViewSearch = new ImageView(GetActivity());
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewSearch.setId(MiscHandler.GenerateViewID());
        ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
        ImageViewSearch.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        ImageViewSearch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {  } });

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ImageViewBookmarkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
        ImageViewBookmarkParam.addRule(MiscHandler.AlignTo("L"), ImageViewSearch.getId());

        ImageView ImageViewBookmark = new ImageView(GetActivity());
        ImageViewBookmark.setLayoutParams(ImageViewBookmarkParam);
        ImageViewBookmark.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBookmark.setId(MiscHandler.GenerateViewID());
        ImageViewBookmark.setImageResource(R.drawable.ic_bookmark_blue);
        ImageViewBookmark.setPadding(MiscHandler.ToDimension(GetActivity(), 16), MiscHandler.ToDimension(GetActivity(), 16), MiscHandler.ToDimension(GetActivity(), 16), MiscHandler.ToDimension(GetActivity(), 16));
        ImageViewBookmark.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {  } });

        RelativeLayoutHeader.addView(ImageViewBookmark);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(MiscHandler.IsDark(GetActivity()) ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        ViewMain = RelativeLayoutMain;
    }
}
