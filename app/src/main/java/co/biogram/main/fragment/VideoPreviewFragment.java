package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheEvictor;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

public class VideoPreviewFragment extends Fragment
{
    private SimpleExoPlayerView SimpleExoPlayerViewMain;
    private SimpleExoPlayer SimpleExoPlayerMain;
    private Runnable runnable;

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
        RelativeLayoutHeader.setVisibility(View.GONE);

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

        final ImageView ImageViewPlay = new ImageView(context);
        ImageViewPlay.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewPlay.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewPlay.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewPlay.setImageResource(R.drawable.ic_pause);
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
        TextViewTime.setText(StringForTime(0) + " / " + StringForTime(0));

        RelativeLayoutControl.addView(TextViewTime);

        RelativeLayout.LayoutParams SeekBarMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        SeekBarMainParam.addRule(RelativeLayout.RIGHT_OF, ImageViewPlay.getId());
        SeekBarMainParam.addRule(RelativeLayout.LEFT_OF, TextViewTime.getId());
        SeekBarMainParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final SeekBar SeekBarMain = new SeekBar(context, null, android.R.attr.progressBarStyleHorizontal);
        SeekBarMain.setLayoutParams(SeekBarMainParam);
        SeekBarMain.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.White), PorterDuff.Mode.MULTIPLY));
        SeekBarMain.setMax(1000);
        SeekBarMain.setProgress(1);

        RelativeLayoutControl.addView(SeekBarMain);

        RelativeLayout.LayoutParams SimpleExoPlayerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        SimpleExoPlayerViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        BandwidthMeter BandwidthMeterMain = new DefaultBandwidthMeter();
        TrackSelection.Factory TrackSelectionMain = new AdaptiveTrackSelection.Factory(BandwidthMeterMain);
        TrackSelector TrackSelectorMain = new DefaultTrackSelector(TrackSelectionMain);
        SimpleExoPlayerMain = ExoPlayerFactory.newSimpleInstance(context, TrackSelectorMain);

        SimpleExoPlayerViewMain = new SimpleExoPlayerView(context);
        SimpleExoPlayerViewMain.setLayoutParams(SimpleExoPlayerViewMainParam);
        SimpleExoPlayerViewMain.setUseController(false);
        SimpleExoPlayerViewMain.setPlayer(SimpleExoPlayerMain);
        SimpleExoPlayerViewMain.getVideoSurfaceView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long Current = SimpleExoPlayerMain.getCurrentPosition();
                long Duration = SimpleExoPlayerMain.getDuration();

                TextViewTime.setText(StringForTime(Current) + " / " + StringForTime(Duration));

                long Position = 1000L * Current / Duration;
                SeekBarMain.setProgress((int) Position);

                if (SimpleExoPlayerMain.getPlayWhenReady())
                {
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    ImageViewPlay.setImageResource(R.drawable.ic_play_white);
                    SimpleExoPlayerMain.setPlayWhenReady(false);
                    SimpleExoPlayerViewMain.removeCallbacks(runnable);
                }
                else
                {
                    RelativeLayoutHeader.setVisibility(View.GONE);
                    ImageViewPlay.setImageResource(R.drawable.ic_pause);
                    SimpleExoPlayerMain.setPlayWhenReady(true);
                    SimpleExoPlayerViewMain.post(runnable);
                }
            }
        });

        RelativeLayoutMain.addView(SimpleExoPlayerViewMain);

        DataSource.Factory DataSourceMain = new OkHttpDataSourceFactory(App.GetOKClient(), "BioGram", null);

        ExtractorsFactory ExtractorsFactoryMain = new DefaultExtractorsFactory();
        CacheEvictor CacheEvictorMain = new LeastRecentlyUsedCacheEvictor(256 * 1024 * 1024);
        Cache CacheMain = new SimpleCache(new File(context.getCacheDir(), "BioGramVideo"), CacheEvictorMain);
        DataSource.Factory DataSourceMain2 = new CacheDataSourceFactory(CacheMain, DataSourceMain, CacheDataSource.FLAG_BLOCK_ON_CACHE, 256 * 1024 * 1024);
        MediaSource MediaSourceMain = new ExtractorMediaSource(Uri.parse(VideoURL), DataSourceMain2, ExtractorsFactoryMain, null, null);

        SimpleExoPlayerMain.prepare(MediaSourceMain);
        SimpleExoPlayerMain.setPlayWhenReady(true);
        SimpleExoPlayerMain.addListener(new Player.EventListener()
        {
            @Override public void onTimelineChanged(Timeline timeline, Object manifest) { }
            @Override public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) { }
            @Override public void onLoadingChanged(boolean isLoading) { }
            @Override public void onRepeatModeChanged(int repeatMode) { }
            @Override public void onPlayerError(ExoPlaybackException error) { }
            @Override public void onPositionDiscontinuity() { }
            @Override public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) { }
            @Override public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
            {
                if (playbackState == 4)
                {
                    SeekBarMain.setProgress(1000);
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    ImageViewPlay.setImageResource(R.drawable.ic_play_white);
                    SimpleExoPlayerMain.setPlayWhenReady(false);
                    SeekBarMain.setProgress(0);
                    SimpleExoPlayerMain.seekTo(0);
                    SimpleExoPlayerViewMain.removeCallbacks(runnable);
                }
                else if (playbackState == 3)
                {
                    TextViewTime.setText(StringForTime(SimpleExoPlayerMain.getCurrentPosition()) + " / " + StringForTime(SimpleExoPlayerMain.getDuration()));
                }
            }
        });

        SeekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    long Duration = SimpleExoPlayerMain.getDuration();
                    long NewPosition = (Duration * progress) / 1000L;

                    SimpleExoPlayerMain.seekTo((int) NewPosition);
                    TextViewTime.setText(StringForTime(NewPosition) + " / " + StringForTime(Duration));
                }
            }
        });

        ImageViewPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long Current = SimpleExoPlayerMain.getCurrentPosition();
                long Duration = SimpleExoPlayerMain.getDuration();
                long Position = 1000L * Current / Duration;

                SeekBarMain.setProgress((int) Position);
                TextViewTime.setText(StringForTime(Current) + " / " + StringForTime(Duration));

                if (SimpleExoPlayerMain.getPlayWhenReady())
                {
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    ImageViewPlay.setImageResource(R.drawable.ic_play_white);
                    SimpleExoPlayerMain.setPlayWhenReady(false);
                    SimpleExoPlayerViewMain.removeCallbacks(runnable);
                }
                else
                {
                    RelativeLayoutHeader.setVisibility(View.GONE);
                    ImageViewPlay.setImageResource(R.drawable.ic_pause);
                    SimpleExoPlayerMain.setPlayWhenReady(true);
                    SimpleExoPlayerViewMain.post(runnable);
                }
            }
        });

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (SimpleExoPlayerMain.getPlayWhenReady())
                    {
                        long Current = SimpleExoPlayerMain.getCurrentPosition();
                        long Duration = SimpleExoPlayerMain.getDuration();
                        final long Position = 1000L * Current / Duration;

                        SeekBarMain.setProgress((int) Position);
                        TextViewTime.setText(StringForTime(Current) + " / " + StringForTime(Duration));
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("VideoPreviewFragment: " + e.toString());
                }

                SimpleExoPlayerViewMain.postDelayed(runnable, 500);
            }
        };

        SimpleExoPlayerViewMain.postDelayed(runnable, 500);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SimpleExoPlayerMain.setPlayWhenReady(false);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        SimpleExoPlayerViewMain.removeCallbacks(runnable);
        SimpleExoPlayerMain.release();
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
