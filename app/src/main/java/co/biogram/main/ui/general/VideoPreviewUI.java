package co.biogram.main.ui.general;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import co.biogram.main.App;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

public class VideoPreviewUI extends FragmentView {
    private SimpleExoPlayerView SimpleExoPlayerViewMain;
    private SimpleExoPlayer SimpleExoPlayerMain;
    private OnSelectListener SelectListener;
    private boolean Selected = false;
    private boolean IsLocal = false;
    private boolean Anim = false;
    private String VideoURL = "";
    private Runnable runnable;

    public VideoPreviewUI(String URL, boolean isLocal, boolean anim) {
        Anim = anim;
        VideoURL = URL;
        IsLocal = isLocal;
    }

    @Override
    public void OnCreate() {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        final RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundColor(Color.parseColor("#20000000"));
        RelativeLayoutHeader.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.general_back : R.drawable.general_back);
        ImageViewBack.setId(Misc.generateViewId());
        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.VideoPreviewUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        /*RelativeLayout.LayoutParams ImageViewDownloadParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewDownloadParam.addRule(Misc.Align("L"));

        ImageView ImageViewDownload = new ImageView(Activity);
        ImageViewDownload.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewDownload.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewDownload.setId(Misc.generateViewId());
        ImageViewDownload.setLayoutParams(ImageViewDownloadParam);
        ImageViewDownload.setImageResource(R.drawable._general_download);
        ImageViewDownload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsLocal)
                    return;

                final int NotifyID = (int) (System.currentTimeMillis() / 1000);

                if (PendingIntent.getActivity(Activity, NotifyID, new Intent(Activity, SocialActivity.class), PendingIntent.FLAG_NO_CREATE) != null)
                    return;

                final NotificationManager NotifyManager = (NotificationManager) Activity.getSystemService(Context.NOTIFICATION_SERVICE);
                final NotificationCompat.Builder NotifyBuilder = new NotificationCompat.Builder(Activity, "BioVideo");

                Intent i = new Intent(Activity, SocialActivity.class);
                i.putExtra("VideoCancel", true);
                i.putExtra("VideoID", NotifyID);

                NotifyBuilder.setContentTitle("Bio Video Download").setContentText("Download in progress").setSmallIcon(R.drawable._general_download).addAction(0, "Cancel", PendingIntent.getActivity(Activity, NotifyID, i, 0));

                AndroidNetworking.download(VideoURL, CacheHandler.GetDir().getAbsolutePath(), DateFormat.format("yyyy_mm_dd_hh_mm_ss", new Date().getCurrentTime()).toString() + ".mp4")
                .setTag(NotifyID)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener()
                {
                    private int Last = 0;

                    @Override
                    public void onProgress(long D, long T)
                    {
                        int Percent = (int) (D * 100 / T);

                        if (Last != Percent && Percent % audio_start == 0 && NotifyManager != null)
                        {
                            Last = Percent;

                            NotifyBuilder.setProgress(100, Percent, false);
                            NotifyManager.notify(NotifyID, NotifyBuilder.build());
                        }
                    }
                })
                .startDownload(new DownloadListener()
                {
                    @Override
                    public void onDownloadComplete()
                    {
                        NotifyBuilder.setContentText("Download completed").setProgress(0, 0, false);
                        NotifyBuilder.mActions.clear();

                        if (NotifyManager != null)
                            NotifyManager.notify(NotifyID, NotifyBuilder.build());
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        NotifyBuilder.setContentText("Download failed");
                        NotifyBuilder.mActions.clear();

                        if (NotifyManager != null)
                            NotifyManager.notify(NotifyID, NotifyBuilder.build());
                    }
                });
            }
        });

        if (!IsLocal)
            RelativeLayoutHeader.addView(ImageViewDownload);*/

        if (SelectListener != null) {
            final GradientDrawable DrawableSelect = new GradientDrawable();
            DrawableSelect.setShape(GradientDrawable.OVAL);
            DrawableSelect.setStroke(Misc.ToDP(2), Color.WHITE);

            final GradientDrawable DrawableSelected = new GradientDrawable();
            DrawableSelected.setShape(GradientDrawable.OVAL);
            DrawableSelected.setColor(ContextCompat.getColor(Activity, R.color.Primary));
            DrawableSelected.setStroke(Misc.ToDP(2), Color.WHITE);

            RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
            ViewCircleParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
            ViewCircleParam.addRule(RelativeLayout.CENTER_VERTICAL);
            ViewCircleParam.addRule(Misc.Align("L"));

            View ViewCircle = new View(Activity);
            ViewCircle.setLayoutParams(ViewCircleParam);
            ViewCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity.onBackPressed();

                    if (Selected) {
                        Selected = false;
                        v.setBackground(DrawableSelect);
                    } else {
                        Selected = true;
                        v.setBackground(DrawableSelected);
                    }

                    SelectListener.OnSelect();
                }
            });

            if (Selected)
                ViewCircle.setBackground(DrawableSelected);
            else
                ViewCircle.setBackground(DrawableSelect);

            RelativeLayoutHeader.addView(ViewCircle);
        }

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        LoadingViewMainParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        final LoadingView LoadingViewMain = new LoadingView(Activity);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);
        LoadingViewMain.SetColor(R.color.White);
        LoadingViewMain.SetScale(1.7f);
        LoadingViewMain.SetSize(5);

        RelativeLayout.LayoutParams RelativeLayoutControlParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        RelativeLayoutControlParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        final RelativeLayout RelativeLayoutControl = new RelativeLayout(Activity);
        RelativeLayoutControl.setLayoutParams(RelativeLayoutControlParam);
        RelativeLayoutControl.setBackgroundColor(Color.parseColor("#20000000"));

        RelativeLayoutMain.addView(RelativeLayoutControl);

        final ImageView ImageViewPlay = new ImageView(Activity);
        ImageViewPlay.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        ImageViewPlay.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewPlay.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56)));
        ImageViewPlay.setImageResource(R.drawable.pause_white);
        ImageViewPlay.setId(Misc.generateViewId());

        RelativeLayoutControl.addView(ImageViewPlay);

        RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextViewTimeParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTimeParam.setMargins(Misc.ToDP(10), Misc.ToDP(3), Misc.ToDP(10), 0);

        final TextView TextViewTime = new TextView(Activity, 14, false);
        TextViewTime.setLayoutParams(TextViewTimeParam);
        TextViewTime.setId(Misc.generateViewId());
        TextViewTime.setText((StringForTime(0) + " / " + StringForTime(0)));

        RelativeLayoutControl.addView(TextViewTime);

        RelativeLayout.LayoutParams SeekBarMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        SeekBarMainParam.addRule(RelativeLayout.RIGHT_OF, ImageViewPlay.getId());
        SeekBarMainParam.addRule(RelativeLayout.LEFT_OF, TextViewTime.getId());
        SeekBarMainParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final SeekBar SeekBarMain = new SeekBar(Activity, null, android.R.attr.progressBarStyleHorizontal);
        SeekBarMain.setLayoutParams(SeekBarMainParam);
        SeekBarMain.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(Activity, R.color.White), PorterDuff.Mode.MULTIPLY));
        SeekBarMain.setMax(1000);
        SeekBarMain.setProgress(1);

        RelativeLayoutControl.addView(SeekBarMain);

        RelativeLayout.LayoutParams SimpleExoPlayerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        SimpleExoPlayerViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        SimpleExoPlayerMain = ExoPlayerFactory.newSimpleInstance(Activity, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));

        SimpleExoPlayerViewMain = new SimpleExoPlayerView(Activity);
        SimpleExoPlayerViewMain.setLayoutParams(SimpleExoPlayerViewMainParam);
        SimpleExoPlayerViewMain.setUseController(false);
        SimpleExoPlayerViewMain.setVisibility(View.GONE);
        SimpleExoPlayerViewMain.setPlayer(SimpleExoPlayerMain);
        SimpleExoPlayerViewMain.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        SimpleExoPlayerViewMain.getVideoSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long Current = SimpleExoPlayerMain.getCurrentPosition();
                long Duration = SimpleExoPlayerMain.getDuration();

                TextViewTime.setText((StringForTime(Current) + " / " + StringForTime(Duration)));
                SeekBarMain.setProgress((int) (1000L * Current / Duration));

                if (SimpleExoPlayerMain.getPlayWhenReady()) {
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    RelativeLayoutControl.setVisibility(View.VISIBLE);
                    ImageViewPlay.setImageResource(R.drawable.play_white);
                    SimpleExoPlayerMain.setPlayWhenReady(false);
                    SimpleExoPlayerViewMain.removeCallbacks(runnable);
                } else {
                    RelativeLayoutHeader.setVisibility(View.GONE);
                    RelativeLayoutControl.setVisibility(View.GONE);
                    ImageViewPlay.setImageResource(R.drawable.pause_white);
                    SimpleExoPlayerMain.setPlayWhenReady(true);
                    SimpleExoPlayerViewMain.post(runnable);
                }
            }
        });

        RelativeLayoutMain.addView(SimpleExoPlayerViewMain);
        RelativeLayoutMain.addView(LoadingViewMain);

        MediaSource MediaSourceMain = null;

        try {
            if (IsLocal) {
                final FileDataSource FDS = new FileDataSource();
                FDS.open(new DataSpec(Uri.parse(VideoURL)));
                DataSource.Factory DataSourceMain = new DataSource.Factory() {
                    @Override
                    public DataSource createDataSource() {
                        return FDS;
                    }
                };
                MediaSourceMain = new ExtractorMediaSource(FDS.getUri(), DataSourceMain, new DefaultExtractorsFactory(), null, null);
            } else {
                Cache CacheMain = new SimpleCache(new File(Activity.getCacheDir(), "BioVideo"), new LeastRecentlyUsedCacheEvictor(256 * 1024 * 1024));
                //DataSource.Factory DataSourceMain = new CacheDataSourceFactory(CacheMain, new OkHttpDataSourceFactory(App.GetOKClient(), "Bio", null), CacheDataSource.FLAG_BLOCK_ON_CACHE, 256 * 1024 * 1024);
                //MediaSourceMain = new ExtractorMediaSource(Uri.parse(VideoURL), DataSourceMain, new DefaultExtractorsFactory(), null, null);
            }
        } catch (Exception e) {
            Misc.Debug("VideoPreviewUI-MediaSource: " + e.toString());
        }

        SimpleExoPlayerMain.prepare(MediaSourceMain);
        SimpleExoPlayerMain.setPlayWhenReady(true);
        SimpleExoPlayerMain.addListener(new Player.EventListener() {
            boolean IsLoading = false;

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity() {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                IsLoading = isLoading;

                Misc.UIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (IsLoading)
                            LoadingViewMain.Start();
                        else
                            LoadingViewMain.Stop();
                    }
                }, 200);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == 4) {
                    SeekBarMain.setProgress(1000);
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    RelativeLayoutControl.setVisibility(View.VISIBLE);
                    ImageViewPlay.setImageResource(R.drawable.play_white);
                    SimpleExoPlayerMain.setPlayWhenReady(false);
                    SeekBarMain.setProgress(0);
                    SimpleExoPlayerMain.seekTo(0);
                    SimpleExoPlayerViewMain.removeCallbacks(runnable);
                } else if (playbackState == 3) {
                    if (SimpleExoPlayerViewMain.getVisibility() == View.GONE)
                        SimpleExoPlayerViewMain.setVisibility(View.VISIBLE);

                    TextViewTime.setText((StringForTime(SimpleExoPlayerMain.getCurrentPosition()) + " / " + StringForTime(SimpleExoPlayerMain.getDuration())));
                }
            }
        });

        SeekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    long Duration = SimpleExoPlayerMain.getDuration();
                    long NewPosition = (Duration * progress) / 1000L;

                    SimpleExoPlayerMain.seekTo((int) NewPosition);
                    TextViewTime.setText((StringForTime(NewPosition) + " / " + StringForTime(Duration)));
                }
            }
        });

        ImageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long Current = SimpleExoPlayerMain.getCurrentPosition();
                long Duration = SimpleExoPlayerMain.getDuration();

                SeekBarMain.setProgress((int) (1000L * Current / Duration));
                TextViewTime.setText((StringForTime(Current) + " / " + StringForTime(Duration)));

                if (SimpleExoPlayerMain.getPlayWhenReady()) {
                    RelativeLayoutControl.setVisibility(View.VISIBLE);
                    RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    ImageViewPlay.setImageResource(R.drawable.play_white);
                    SimpleExoPlayerMain.setPlayWhenReady(false);
                    SimpleExoPlayerViewMain.removeCallbacks(runnable);
                } else {
                    RelativeLayoutControl.setVisibility(View.GONE);
                    RelativeLayoutHeader.setVisibility(View.GONE);
                    ImageViewPlay.setImageResource(R.drawable.pause_white);
                    SimpleExoPlayerMain.setPlayWhenReady(true);
                    SimpleExoPlayerViewMain.post(runnable);
                }
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (SimpleExoPlayerMain.getPlayWhenReady()) {
                        long Current = SimpleExoPlayerMain.getCurrentPosition();
                        long Duration = SimpleExoPlayerMain.getDuration();
                        long Position = 1000L * Current / Duration;

                        SeekBarMain.setProgress((int) Position);
                        SeekBarMain.setSecondaryProgress(SimpleExoPlayerMain.getBufferedPercentage() * 10);
                        TextViewTime.setText((StringForTime(Current) + " / " + StringForTime(Duration)));
                    }
                } catch (Exception e) {
                    Misc.Debug("VideoPreviewUI-Runnable: " + e.toString());
                }

                SimpleExoPlayerViewMain.postDelayed(runnable, 500);
            }
        };

        RelativeLayoutHeader.bringToFront();
        RelativeLayoutControl.bringToFront();

        if (Anim) {
            TranslateAnimation Trans = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
            Trans.setDuration(200);

            RelativeLayoutMain.startAnimation(Trans);
        }

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume() {
        SimpleExoPlayerViewMain.postDelayed(runnable, 500);

        Activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(Color.BLACK);
    }

    @Override
    public void OnPause() {
        SimpleExoPlayerViewMain.removeCallbacks(runnable);
        SimpleExoPlayerMain.setPlayWhenReady(false);
        Activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void OnDestroy() {
        SimpleExoPlayerMain.release();

        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(Misc.Color(Misc.IsDark() ? R.color.White : R.color.White));
    }

    void SetType(boolean select, OnSelectListener l) {
        Selected = select;
        SelectListener = l;
    }

    private String StringForTime(long Time) {
        if (Time < 0)
            Time = 0;

        long TotalSeconds = Time / 1000;
        long Seconds = TotalSeconds % 60;
        long Minutes = (TotalSeconds / 60) % 60;
        long Hours = TotalSeconds / 3600;

        if (Hours > 0)
            return String.valueOf(Hours) + ":" + String.valueOf(Minutes) + ":" + String.valueOf(Seconds);

        return String.valueOf(Minutes) + ":" + String.valueOf(Seconds);
    }

    interface OnSelectListener {
        void OnSelect();
    }
}
