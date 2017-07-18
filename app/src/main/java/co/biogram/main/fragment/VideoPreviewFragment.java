package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.danikula.videocache.HttpProxyCacheServer;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.TextureVideoView;

public class VideoPreviewFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String VideoURL = "";
        Context context = getActivity();

        if (getArguments() != null)
            VideoURL = getArguments().getString("VideoURL", "");

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.Black);
        RelativeLayoutMain.setClickable(true);

        final RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundColor(Color.parseColor("#20ffffff"));

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setImageResource(R.drawable.ic_back_white);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { getActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.White));
        TextViewTitle.setText(getString(R.string.VideoPreviewFragment));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams RelativeLayoutControlParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        RelativeLayoutControlParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        final RelativeLayout RelativeLayoutControl = new RelativeLayout(context);
        RelativeLayoutControl.setLayoutParams(RelativeLayoutControlParam);
        RelativeLayoutControl.setBackgroundColor(Color.parseColor("#20ffffff"));

        RelativeLayoutMain.addView(RelativeLayoutControl);

        ImageView ImageViewPlay = new ImageView(context);
        ImageViewPlay.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewPlay.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewPlay.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewPlay.setImageResource(R.drawable.ic_play_arrow_white);
        ImageViewPlay.setId(MiscHandler.GenerateViewID());

        RelativeLayoutControl.addView(ImageViewPlay);

        RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextViewTimeParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTimeParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        final TextView TextViewTime = new TextView(context);
        TextViewTime.setLayoutParams(TextViewTimeParam);
        TextViewTime.setId(MiscHandler.GenerateViewID());
        TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.White));

        RelativeLayoutControl.addView(TextViewTime);

        RelativeLayout.LayoutParams SeekBarMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        SeekBarMainParam.addRule(RelativeLayout.RIGHT_OF, ImageViewPlay.getId());
        SeekBarMainParam.addRule(RelativeLayout.LEFT_OF, TextViewTime.getId());
        SeekBarMainParam.addRule(RelativeLayout.CENTER_VERTICAL);

        SeekBar SeekBarMain = new SeekBar(context, null, android.R.attr.progressBarStyleHorizontal);
        SeekBarMain.setLayoutParams(SeekBarMainParam);
        SeekBarMain.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.White), PorterDuff.Mode.MULTIPLY));
        SeekBarMain.setMax(1000);
        SeekBarMain.setProgress(1);

        RelativeLayoutControl.addView(SeekBarMain);

        RelativeLayout.LayoutParams TextureVideoViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        TextureVideoViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        VideoURL = App.GetProxy(context).getProxyUrl(VideoURL);

        final TextureVideoView TextureVideoViewMain = new TextureVideoView(context);
        TextureVideoViewMain.setLayoutParams(TextureVideoViewMainParam);
        TextureVideoViewMain.SetVideoURI(VideoURL);
        TextureVideoViewMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (TextureVideoViewMain.isPlaying())
                {
                    RelativeLayoutControl.setVisibility(View.VISIBLE);
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    TextureVideoViewMain.pause();
                }
                else
                {
                    RelativeLayoutControl.setVisibility(View.GONE);
                    RelativeLayoutHeader.setVisibility(View.GONE);
                    TextureVideoViewMain.start();
                }

                long Current = TextureVideoViewMain.getCurrentPosition();
                long Duration = TextureVideoViewMain.getDuration();

                TextViewTime.setText(StringForTime(Current) + " / " + StringForTime(Duration));
            }
        });
        TextureVideoViewMain.SetOnCompletion(new TextureVideoView.OnVideoCompletion()
        {
            @Override
            public void OnCompletion(MediaPlayer mp)
            {
                RelativeLayoutControl.setVisibility(View.VISIBLE);
                RelativeLayoutHeader.setVisibility(View.VISIBLE);
            }
        });
        TextureVideoViewMain.SetOnPrepared(new TextureVideoView.OnVideoPrepared()
        {
            @Override
            public void OnPrepared(MediaPlayer mp)
            {
                long Duration = TextureVideoViewMain.getDuration();

                TextViewTime.setText(StringForTime(0) + " / " + StringForTime(Duration));
            }
        });

        SeekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                long Duration = TextureVideoViewMain.getDuration();
                long NewPosition = (Duration * progress) / 1000L;

                TextureVideoViewMain.seekTo((int) NewPosition);
                TextViewTime.setText(StringForTime(NewPosition) + " / " + StringForTime(Duration));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        ImageViewPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (TextureVideoViewMain.isPlaying())
                {
                    RelativeLayoutControl.setVisibility(View.VISIBLE);
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    TextureVideoViewMain.pause();
                }
                else
                {
                    RelativeLayoutControl.setVisibility(View.GONE);
                    RelativeLayoutHeader.setVisibility(View.GONE);
                    TextureVideoViewMain.start();
                }

                long Current = TextureVideoViewMain.getCurrentPosition();
                long Duration = TextureVideoViewMain.getDuration();

                TextViewTime.setText(StringForTime(Current) + " / " + StringForTime(Duration));
            }
        });

        RelativeLayoutMain.addView(TextureVideoViewMain);

        return RelativeLayoutMain;
    }

    private String StringForTime(long Time)
    {
        long TotalSeconds = Time / 1000;
        long Seconds = TotalSeconds % 60;
        long Minutes = (TotalSeconds / 60) % 60;
        long Hours   = TotalSeconds / 3600;

        if (Hours > 0)
            return String.valueOf(Hours) + ":" + String.valueOf(Minutes) + ":" + String.valueOf(Seconds);

        return String.valueOf(Minutes) + ":" + String.valueOf(Seconds);
    }
}
