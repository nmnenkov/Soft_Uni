package com.nnenkov.mymusicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by nik on 22.09.16.
 */

public class MyMusicPlayerService extends Service {

    public static final int MSG_PLAY = 0;
    public static final int MSG_STOP = 1;
    public static final int MSG_PAUSE = 2;
    public static final int MSG_NEXT = 3;
    public static final int MSG_PREVIOUS = 4;
    public static final int MSG_PLAY_AFTHER_PAUSE = 5;
    public static final int MSG_FORWARD_STRAIGTH = 6;
    public static final int MSG_FORWARD_BACK = 7;

    private static final int FORWARD_STEP = 1000;



    static Context mCtx;

    IBinder binder = new MyMusicPlayerServiceBinder();
    IMyMusicPlayerServiceCommunication callback;
    MediaPlayer player;

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    public void setServiceCallback(IMyMusicPlayerServiceCommunication callback) {
        this.callback = callback;
    }


    public class MyMusicPlayerServiceBinder extends Binder {
        MyMusicPlayerService getService() {
            return MyMusicPlayerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //return mMessenger.getBinder();//binder;
        return binder;
    }

    @Override
    public void onCreate() {
        player = null;//new MediaPlayer();//.create(this,R.raw.crash_boom_bang);

        mCtx = this;
        //Toast.makeText(this, "Service on create ", Toast.LENGTH_SHORT).show();
        //initMediaPlayerPreference();

    }

    private void mediaPlayerFunctions(int operation, int resId) {
        //player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        //player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.reset();
        player = MediaPlayer.create(this, resId);

        try {
            //player.create(this,R.raw.crash_boom_bang);// setDataSource(R.raw.crash_boom_bang);
            //player.setDataSource("/system/");
            //player.prepare();
            player.setLooping(false);
            player.start();
        } catch (Exception ex) {
            Toast.makeText(this, "Media Player error", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.hasExtra("Data")) {
//            Toast.makeText(this, "Service Started With Data : " + intent.getStringExtra("Data"), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Service Started !!!", Toast.LENGTH_SHORT).show();
//        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PLAY:
                    if (player != null) player.reset();
                    SongInfo songInfo = (SongInfo) msg.obj;
                    player = MediaPlayer.create(mCtx, songInfo.getResName());
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                    //Toast.makeText(mCtx, "The song completed!!!", Toast.LENGTH_LONG).show();
                    if (callback != null) callback.onMyMusicPlayerServiceCustomInvocation(0);
                }
                    });

                    try {
                        //player.create(this,R.raw.crash_boom_bang);// setDataSource(R.raw.crash_boom_bang);
                        //player.setDataSource("/system/");
                        //player.prepare();
                        player.setLooping(false);
                        player.start();
                    } catch (Exception ex) {
                        Toast.makeText(mCtx, "Media Player error", Toast.LENGTH_SHORT).show();
                    }

                    //Toast.makeText(getApplicationContext(), "Service Play!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAUSE:
                    if (player != null) {
                        player.pause();
                    }
                    //Toast.makeText(getApplicationContext(), "Service Pause!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PLAY_AFTHER_PAUSE:
                    if (player != null) player.start();
                    break;
                case MSG_STOP:
                    if (player != null) {
                        player.stop();
                    }
                    //Toast.makeText(getApplicationContext(), "Service Stop!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_FORWARD_STRAIGTH:
                    if (isPlaying()) {
                        int currentPosition = getPosition();
                        if (currentPosition < (getDuration() - (FORWARD_STEP + 10))) {
                            seek(currentPosition + FORWARD_STEP);
                        }
                    }
                    break;
                case MSG_FORWARD_BACK:
                    if (isPlaying()) {
                        int currentPosition = getPosition();
                        if (currentPosition > (0 + (FORWARD_STEP + 10))) {
                            seek(currentPosition - FORWARD_STEP);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    public int getPosition() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        int duration = 0;
        if (player != null) duration = player.getDuration();
        return duration;
    }

    public boolean isPlaying() {
        boolean mIsPlaying = false;
        if (player != null) mIsPlaying = true;
        return mIsPlaying;
    }


    public void seek(int posn) {
        player.seekTo(posn);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Killed !!!", Toast.LENGTH_SHORT).show();
        if (player != null)
            player.stop();
        player = null;
    }
}

