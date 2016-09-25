package com.nnenkov.mymusicplayer;

import android.content.ComponentName;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements IRecycleViewSelectedElement, Observer, IMyMusicPlayerServiceCommunication, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    Context mCtx;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static ArrayList<SongInfo> songsList;
    public int playNowPosition = 0;

    private String TAG = MainActivity.class.getSimpleName();

    ImageButton playButton, stopButton, nextButton, previousButton, forwardStraigthButton, forwardBackButton;
    int playButtonStatus = 0; // 0 - no active play ; 1 - is play ; 2 - is pause ;

    TextView playNowTextView, songCurrentDurationTextView;
    SeekBar mSeekBar;

    LinearLayout mSongInfoLinearLayout;

    //MyReceiver receiver;
    Intent myMysicPlayerServiceIntent;

    /**
     * Messenger for communicating with the service.
     */
    Messenger mServiceMessenger = null;

    /**
     * Flag indicating whether we have called bind on the service.
     */
    boolean mBound = false;

    MyMusicPlayerService mMyMusicPlayerService;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mCtx = this;

        playButton = (ImageButton) findViewById(R.id.playImageButton);
        stopButton = (ImageButton) findViewById(R.id.stopImageButton);
        nextButton = (ImageButton) findViewById(R.id.nextImageButton);
        previousButton = (ImageButton) findViewById(R.id.previousImageButton);
        forwardStraigthButton = (ImageButton) findViewById(R.id.forwardStraightImageButton);
        forwardBackButton = (ImageButton) findViewById(R.id.forwardBackImageButton);

        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        forwardStraigthButton.setOnClickListener(this);
        forwardBackButton.setOnClickListener(this);

        playNowTextView = (TextView) findViewById(R.id.playNowTextView);
        playNowTextView.setText("");
        playNowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        mSongInfoLinearLayout = (LinearLayout)findViewById(R.id.songInfoLinearLayout);
        songCurrentDurationTextView = (TextView) findViewById(R.id.songCurrentDurationTextView);
        songCurrentDurationTextView.setText(R.string.zeroTime);

        mSeekBar = (SeekBar) findViewById(R.id.mSeekBar);

        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(this);

        songsList = new ArrayList<SongInfo>();
        songsList.add(0, new SongInfo(0, "1", "Fireworks", "Roxette", null, R.raw.fireworks));
        songsList.add(1, new SongInfo(1, "2", "Crash! Boom! Bang!", "Roxette", null, R.raw.crash_boom_bang));
        songsList.add(2, new SongInfo(2, "3", "Harleys & Indians", "Roxette", null, R.raw.harleys_indians_riders_in_t));
        songsList.add(3, new SongInfo(3, "4", "Place Your Love", "Roxette", null, R.raw.place_your_love));
        songsList.add(4, new SongInfo(4, "5", "Run to You", "Roxette", null, R.raw.run_to_you));
        songsList.add(5, new SongInfo(5, "6", "Sleeping in My Car", "Roxette", null, R.raw.sleeping_in_my_car));
        songsList.add(6, new SongInfo(6, "7", "The First Girl on the Moon", "Roxette", null, R.raw.the_first_girl_on_the_moon));
        songsList.add(7, new SongInfo(7, "8", "Vulnerable", "Roxette", null, R.raw.vulnerable));
//getSongsList();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new RecycleViewAdapter(songsList, this);

        mRecyclerView.setAdapter(mAdapter);

        RecycleViewCustomDecoration itemCustomDecoration = new RecycleViewCustomDecoration();
        mRecyclerView.addItemDecoration(itemCustomDecoration);

        startMyMusicPlayerService();
        updatemSeekBar();


    }


//    public void getSongsList(){
//        ContentResolver musicResolver = getContentResolver();
//
//        Uri musicUri = android.provider.MediaStore.Audio.Media.getContentUri("/");
//        grantUriPermission(this.getPackageName(),musicUri, getIntent().FLAG_GRANT_READ_URI_PERMISSION);
//        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
//
//        if(musicCursor!=null && musicCursor.moveToFirst()){
//            //get columns
//            int titleColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.TITLE);
//            int idColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media._ID);
//            int artistColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.ARTIST);
//            int counter=0;
//            //add songs to list
//            do {
//                counter++;
//                long thisId = musicCursor.getLong(idColumn);
//                String thisTitle = musicCursor.getString(titleColumn);
//                String thisArtist = musicCursor.getString(artistColumn);
//                //songList.add(new Song(thisId, thisTitle, thisArtist));
//                songsList.add(new SongInfo(thisId,String.valueOf(counter), thisTitle,thisArtist, null, null));
//            }
//            while (musicCursor.moveToNext());
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int position) {
        //Toast.makeText(this, String.valueOf(position), Toast.LENGTH_LONG).show();
        setUnsetPlayNow(position);
        sendMsgToService(MyMusicPlayerService.MSG_PLAY);
        playButton.setImageResource(R.drawable.media_pause);
        playButtonStatus = 1;
    }

    public void setUnsetPlayNow(int position) {
        songsList.get(playNowPosition).setPlayNow(false);
        mAdapter.notifyItemChanged(playNowPosition);
        songsList.get(position).setPlayNow(true);
        mAdapter.notifyItemChanged(position);
        playNowPosition = position;
        playNowTextView.setText(songsList.get(position).getsTitle().toString() +" - " + songsList.get(position).getsSinger().toString());
        //playNowTextView.startAnimation();
        mSongInfoLinearLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation));
    }


    @Override
    public void onMyMusicPlayerServiceCustomInvocation(int event) {

        if (event == 0) {
            if ((playNowPosition + 1 < songsList.size())) {
                playButton.setImageResource(R.drawable.media_pause);
                playButtonStatus = 1;
                setUnsetPlayNow(playNowPosition + 1);
                sendMsgToService(MyMusicPlayerService.MSG_PLAY);
            } else playButton.setImageResource(R.drawable.media_play);
        }

    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(mCtx, "Service is ready to roll !!!", Toast.LENGTH_SHORT).show();

            MyMusicPlayerService.MyMusicPlayerServiceBinder serviceToOperate = (MyMusicPlayerService.MyMusicPlayerServiceBinder) service;

            mMyMusicPlayerService = serviceToOperate.getService();

            serviceToOperate.getService().setServiceCallback(MainActivity.this);
            mServiceMessenger = new Messenger(serviceToOperate.getService().mMessenger.getBinder());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(mCtx, "Service is out of the game !!!", Toast.LENGTH_SHORT).show();
            mServiceMessenger = null;
            mBound = false;
        }
    };

    public void startMyMusicPlayerService() {

        myMysicPlayerServiceIntent = new Intent(this, MyMusicPlayerService.class);
        //myMysicPlayerServiceIntent.putExtra("Action", "none");
        bindService(myMysicPlayerServiceIntent, conn, Context.BIND_AUTO_CREATE);
        startService(myMysicPlayerServiceIntent);

    }


    @Override
    public void update(Observable o, Object arg) {
        Toast.makeText(this, "Observer passed data to activity !!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playImageButton:
                if (playButtonStatus == 0) {
                    sendMsgToService(MyMusicPlayerService.MSG_PLAY);
                    playButton.setImageResource(R.drawable.media_pause);
                    playButtonStatus = 1;
                    setUnsetPlayNow(playNowPosition);
                } else if (playButtonStatus == 1) {
                    sendMsgToService(MyMusicPlayerService.MSG_PAUSE);
                    playButton.setImageResource(R.drawable.media_play);
                    playButtonStatus = 2;
                } else if (playButtonStatus == 2) {
                    sendMsgToService(MyMusicPlayerService.MSG_PLAY_AFTHER_PAUSE);
                    playButton.setImageResource(R.drawable.media_pause);
                    playButtonStatus = 1;
                }

                break;
            case R.id.stopImageButton:
                playButton.setImageResource(R.drawable.media_play);
                playButtonStatus = 0;
                sendMsgToService(MyMusicPlayerService.MSG_STOP);
                break;
            case R.id.nextImageButton:
                playButton.setImageResource(R.drawable.media_pause);
                playButtonStatus = 1;
                if ((playNowPosition + 1 < songsList.size())) {
                    setUnsetPlayNow(playNowPosition + 1);
                    sendMsgToService(MyMusicPlayerService.MSG_PLAY);
                } else {
                    setUnsetPlayNow(0);
                    sendMsgToService(MyMusicPlayerService.MSG_PLAY);
                }

                break;
            case R.id.previousImageButton:
                playButton.setImageResource(R.drawable.media_pause);
                playButtonStatus = 1;
                if (playNowPosition > 0) {
                    setUnsetPlayNow(playNowPosition - 1);
                    sendMsgToService(MyMusicPlayerService.MSG_PLAY);
                } else {
                    setUnsetPlayNow(songsList.size() - 1);
                    sendMsgToService(MyMusicPlayerService.MSG_PLAY);
                }

                break;
            case R.id.forwardStraightImageButton:
                sendMsgToService(MyMusicPlayerService.MSG_FORWARD_STRAIGTH);

                break;
            case R.id.forwardBackImageButton:
                sendMsgToService(MyMusicPlayerService.MSG_FORWARD_BACK);
                break;
            default:
                break;
        }
    }

    private void sendMsgToService(int playerCommand) {

        if (!mBound) return;
        //if (!songsList.get(playNowPosition).getPlayNow()) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, playerCommand, songsList.get(playNowPosition));
        try {
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress handler
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (playButtonStatus > 0) {
            mHandler.removeCallbacks(mUpdateTimeTask);
            int totalDuration = mMyMusicPlayerService.getDuration();
            int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

            // forward or backward to certain seconds
            mMyMusicPlayerService.seek(currentPosition);

            // update timer progress again
            updatemSeekBar();
        }
    }

    public void updatemSeekBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration;
            long currentDuration;
            if ((mMyMusicPlayerService != null) && (playButtonStatus > 0)) {
                totalDuration = mMyMusicPlayerService.getDuration();
                currentDuration = mMyMusicPlayerService.getPosition();


                // Displaying Total Duration time
                //songTotalDurationLabel.setText("" + milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                songCurrentDurationTextView.setText(milliSecondsToTimer(currentDuration).toString());

                // Updating progress bar
                int progress = (int) (getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                mSeekBar.setProgress(progress);


            }
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        String minutesString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

}

