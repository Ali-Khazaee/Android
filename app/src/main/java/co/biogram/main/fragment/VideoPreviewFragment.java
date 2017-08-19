package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheEvictor;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.misc.TextureVideoView;

public class VideoPreviewFragment extends Fragment
{
    private TextureVideoView TextureVideoViewMain;
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

        SimpleExoPlayerView SimpleExoPlayerViewMain = new SimpleExoPlayerView(context);
        SimpleExoPlayerViewMain.setLayoutParams(SimpleExoPlayerViewMainParam);

        RelativeLayoutMain.addView(SimpleExoPlayerViewMain);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        SimpleExoPlayerViewMain.setPlayer(player);

        DataSource.Factory dataSourceFactory = new OkHttpDataSourceFactory(App.GetOKClient(), "BioGram", null);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();



        CacheEvictor cacheEvictor = new LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024);
        Cache cache = new SimpleCache(new File(context.getCacheDir(), "media_cache"), cacheEvictor);
        DataSource.Factory dataSourceFactory2 = new CacheDataSourceFactory(cache, dataSourceFactory, CacheDataSource.FLAG_BLOCK_ON_CACHE, 100 * 1024 * 1024);
        //return new HlsMediaSource(uri, dataSourceFactory, mainHandler, eventLogger);


        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(VideoURL), dataSourceFactory2, extractorsFactory, null, null);

        player.prepare(videoSource);



        /*if (VideoURL.startsWith("http"))
            VideoURL = App.GetProxy(context).getProxyUrl(VideoURL);

        TextureVideoViewMain = new TextureVideoView(context);
        TextureVideoViewMain.setLayoutParams(TextureVideoViewMainParam);
        TextureVideoViewMain.SetVideoURI(VideoURL);
        TextureVideoViewMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long Current = TextureVideoViewMain.getCurrentPosition();
                long Duration = TextureVideoViewMain.getDuration();

                TextViewTime.setText(StringForTime(Current) + " / " + StringForTime(Duration));

                long Position = 1000L * Current / Duration;
                SeekBarMain.setProgress((int) Position);

                if (TextureVideoViewMain.isPlaying())
                {
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    ImageViewPlay.setImageResource(R.drawable.ic_play_white);
                    TextureVideoViewMain.pause();
                    TextureVideoViewMain.removeCallbacks(runnable);
                }
                else
                {
                    RelativeLayoutHeader.setVisibility(View.GONE);
                    ImageViewPlay.setImageResource(R.drawable.ic_pause);
                    TextureVideoViewMain.start();
                    TextureVideoViewMain.post(runnable);
                }
            }
        });
        TextureVideoViewMain.SetOnCompletion(new TextureVideoView.OnVideoCompletion()
        {
            @Override
            public void OnCompletion(MediaPlayer mp)
            {
                SeekBarMain.setProgress(1000);
                RelativeLayoutHeader.setVisibility(View.VISIBLE);
                ImageViewPlay.setImageResource(R.drawable.ic_play_white);
                TextureVideoViewMain.removeCallbacks(runnable);
            }
        });
        TextureVideoViewMain.SetOnPrepared(new TextureVideoView.OnVideoPrepared()
        {
            @Override
            public void OnPrepared(MediaPlayer mp)
            {
                TextViewTime.setText(StringForTime(0) + " / " + StringForTime(mp.getDuration()));
            }
        });

        SeekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    long Duration = TextureVideoViewMain.getDuration();
                    long NewPosition = (Duration * progress) / 1000L;

                    TextureVideoViewMain.seekTo((int) NewPosition);
                    TextViewTime.setText(StringForTime(NewPosition) + " / " + StringForTime(Duration));
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        ImageViewPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long Current = TextureVideoViewMain.getCurrentPosition();
                long Duration = TextureVideoViewMain.getDuration();
                long Position = 1000L * Current / Duration;

                SeekBarMain.setProgress((int) Position);
                TextViewTime.setText(StringForTime(Current) + " / " + StringForTime(Duration));

                if (TextureVideoViewMain.isPlaying())
                {
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    ImageViewPlay.setImageResource(R.drawable.ic_play_white);
                    TextureVideoViewMain.pause();
                    TextureVideoViewMain.removeCallbacks(runnable);
                }
                else
                {
                    RelativeLayoutHeader.setVisibility(View.GONE);
                    ImageViewPlay.setImageResource(R.drawable.ic_pause);
                    TextureVideoViewMain.start();
                    TextureVideoViewMain.post(runnable);
                }
            }
        });

        RelativeLayoutMain.addView(TextureVideoViewMain);

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (!TextureVideoViewMain.isPlaying())
                        TextureVideoViewMain.start();

                    long Current = TextureVideoViewMain.getCurrentPosition();
                    long Duration = TextureVideoViewMain.getDuration();
                    final long Position = 1000L * Current / Duration;

                    SeekBarMain.setProgress((int) Position);
                    TextViewTime.setText(StringForTime(Current) + " / " + StringForTime(Duration));
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("VideoPreviewFragment: " + e.toString());
                }

                TextureVideoViewMain.postDelayed(runnable, 500);
            }
        };

        TextureVideoViewMain.postDelayed(runnable, 500);*/

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //TextureVideoViewMain.removeCallbacks(runnable);
        //TextureVideoViewMain.ClearPlayback();
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
