package co.biogram.main.misc;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import co.biogram.main.handler.MiscHandler;

public class VideoView extends RelativeLayout
{
    private MediaPlayer mediaPlayer;
    private TextureView textureView;
    private Surface surface;
    private Uri VideoUri = null;
    private boolean VideoLoop = true;
    private boolean VideoMute = true;

    public VideoView(Context context)
    {
        super(context);
    }

    public VideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private void AddSurfaceView()
    {
        LayoutParams textureViewParams = new LayoutParams(-1, -1);
        textureView = new TextureView(getContext());
        textureView.setLayoutParams(textureViewParams);
        textureView.setSurfaceTextureListener(new SurfaceTextureListener()
        {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1)
            {
                surface = new Surface(surfaceTexture);
                SetMediaPlayerDataSource();
            }

            @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) { }
            @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) { return false; }
            @Override public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) { }
        });

        addView(textureView, 0);
    }

    private void PrepareMediaPlayer()
    {
        if (mediaPlayer != null)
            Release();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setAudioSessionId(1);
        mediaPlayer.setOnPreparedListener(new OnPreparedListener()
        {
            public void onPrepared(MediaPlayer mediaPlayer)
            {
                ScalePlayer();

                AudioManager audioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                if (VideoMute)
                    mediaPlayer.setVolume(0.0f, 0.0f);

                try
                {
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.start();
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("VideoView3: " + e.toString());
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                if (VideoLoop)
                {
                    mp.seekTo(0);
                    mp.start();
                }
            }
        });

        mediaPlayer.setOnErrorListener(new OnErrorListener()
        {
            public boolean onError(MediaPlayer mp, int what, int extra)
            {
                MiscHandler.Debug("VideoView2: " + "Code: " + what + ", Extra: " + extra);
                return true;
            }
        });
    }

    private void SetMediaPlayerDataSource()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    mediaPlayer.setDataSource(getContext(), VideoUri);
                    mediaPlayer.prepareAsync();
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("VideoView1: " + e.toString());
                }
            }
        }).start();
    }

    private void ScalePlayer()
    {
        int VideoWidth = mediaPlayer.getVideoWidth();
        int VideoHeight = mediaPlayer.getVideoHeight();
        int ScreenWidth = getWidth();

        int Height;

        if (VideoWidth > ScreenWidth)
        {
            int Width = VideoWidth - ScreenWidth;
            Height = VideoHeight - Width;
        }
        else
        {
            int Width = ScreenWidth - VideoWidth;
            Height = VideoHeight + Width;
        }

        ViewGroup ParentView = (ViewGroup) getParent();

        ViewGroup.LayoutParams ParentViewParam = ParentView.getLayoutParams();
        ParentViewParam.height = Height;

        ParentView.setLayoutParams(ParentViewParam);

        ViewGroup.LayoutParams TextureViewParam = textureView.getLayoutParams();
        TextureViewParam.width = ScreenWidth;
        TextureViewParam.height = Height;

        textureView.setLayoutParams(TextureViewParam);
    }

    public void Start(String url)
    {
        Start(Uri.parse(url));
    }

    public void Start(Uri uri)
    {
        VideoUri = uri;

        if (textureView == null)
        {
            AddSurfaceView();
            PrepareMediaPlayer();
        }
        else
        {
            PrepareMediaPlayer();
            SetMediaPlayerDataSource();
        }
    }

    public void Play()
    {
        if (!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    public void Pause()
    {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public void Release()
    {
        try
        {
            AudioManager audioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.abandonAudioFocus(null);

            mediaPlayer.stop();
            mediaPlayer.release();
        }
        catch (Exception e)
        {
            //
        }

        mediaPlayer = null;
    }

    public boolean IsPlaying()
    {
        try
        {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
