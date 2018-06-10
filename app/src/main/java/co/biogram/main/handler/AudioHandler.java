package co.biogram.main.handler;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.URI;

/**
 * Created by soh_mil97 on 5/17/18.
 */

public class AudioHandler {

    private MP_STATES State;
    private MediaPlayer Player;
    private OnStartListener OnStartListener;
    private OnResumeListener OnResumeListener;
    private OnPauseListener OnPauseListener;
    private SeekListener OnSeekListener;

    public AudioHandler() {
        Player = new MediaPlayer();
        State = MP_STATES.MPS_IDLE;
    }

    public void setData(String filePath) {

        bringToIdleState();

        try {
            Player.setDataSource(filePath);
            State = MP_STATES.MPS_INITIALIZED;
            Player.prepare();
            State = MP_STATES.MPS_PREPARING;
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }

    }

    public void setData(@NonNull Context context, @NonNull Uri uri) {

        bringToIdleState();

        try {
            Player.setDataSource(context, uri);
            State = MP_STATES.MPS_INITIALIZED;
            Player.prepare();
            State = MP_STATES.MPS_PREPARING;
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }

    }

    public void play() {
        switch (State) {
            case MPS_PREPARED:
            case MPS_PAUSED:
                if (OnStartListener != null)
                    OnStartListener.onStart();
                Player.start();
                State = MP_STATES.MPS_STARTED;
                break;
            default:
                // !! throw exception
        }
    }

    public void pause() {
        switch (State) {
            case MPS_STARTED:
                if (OnPauseListener != null)
                    OnPauseListener.onPause();
                Player.pause();
                State = MP_STATES.MPS_PAUSED;
                break;
            default:
                // !! throw exception
        }
    }

    public void release() {
        Player.release();
        Player = null;
    }

    public void seekTo(int mSec) {
        if (OnSeekListener != null)
            OnSeekListener.onSeekChanged();
        Player.seekTo(mSec);
    }

    private void bringToIdleState() {
        // reset() should bring MP to IDLE
        Player.reset();
        State = MP_STATES.MPS_IDLE;
    }

    public OnStartListener getOnStartListener() {
        return OnStartListener;
    }

    public void setOnStartListener(OnStartListener onStartListener) {
        OnStartListener = onStartListener;
    }


    // Set Listeners

    public OnResumeListener getOnResumeListener() {
        return OnResumeListener;
    }

    public void setOnResumeListener(OnResumeListener onResumeListener) {
        OnResumeListener = onResumeListener;
    }

    public OnPauseListener getOnPauseListener() {
        return OnPauseListener;
    }

    public void setOnPauseListener(OnPauseListener onPauseListener) {
        OnPauseListener = onPauseListener;
    }

    public SeekListener getOnSeekListener() {
        return OnSeekListener;
    }

    public void setOnSeekListener(SeekListener onSeekListener) {
        OnSeekListener = onSeekListener;
    }

    public MediaPlayer getPlayer() {
        return Player;
    }

    public MP_STATES getState() {
        return State;
    }


    // Getter

    public void setState(MP_STATES state) {
        State = state;
    }

    public static enum MP_STATES {
        MPS_IDLE,
        MPS_INITIALIZED,
        MPS_PREPARING,
        MPS_PREPARED,
        MPS_STARTED,
        MPS_STOPPED,
        MPS_PAUSED,
        MPS_PLAYBACK_COMPLETED,
        MPS_ERROR,
        MPS_END,
    }


    // Custom Cycles

    public interface OnPauseListener {
        void onPause();
    }

    public interface OnResumeListener {
        void onResume();
    }

    public interface OnStartListener {
        void onStart();
    }

    public interface SeekListener {
        void onSeekChanged();
    }

}