package vn.framgia.phamthehung.soundcloud.ui.playmusic;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import vn.framgia.phamthehung.soundcloud.R;
import vn.framgia.phamthehung.soundcloud.data.model.Track;
import vn.framgia.phamthehung.soundcloud.mediaplayer.MediaPlayerManager;
import vn.framgia.phamthehung.soundcloud.service.PlayMusicListener;
import vn.framgia.phamthehung.soundcloud.service.PlayMusicService;

public class PlayMusicActivity extends AppCompatActivity
        implements ServiceConnection, View.OnClickListener, SeekBar.OnSeekBarChangeListener, PlayMusicListener {
    public List<Track> mTracks;
    private PlayMusicService mPlayMusicService;
    private ServiceConnection mConnection = (ServiceConnection) this;
    private TextView mTextNameTrack;
    private TextView mTextArtist;
    private ImageView mImageDisk;
    private ImageButton mImageButtonFavorite;
    private ImageButton mImageButtonDownload;
    private TextView mTexTimeStart;
    private TextView mTextTimeFinish;
    private SeekBar mSeekBarMusic;
    private ImageButton mImageButtonPausePlay;
    private ImageButton mImageButtonNext;
    private ImageButton mImageButtonPrevious;
    private ImageButton mImageButtonShuffle;
    private ImageButton mImageButtonRepeat;
    private ObjectAnimator mObjectAnimator;
    private boolean mIsPlaying;
    private int mCurrentTime;
    private SeekBar mSeekBar;
    private MediaPlayerManager mManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        initView();
        initToolBar();
    }

    @Override
    protected void onStart() {
        Intent intent = new Intent(this,PlayMusicService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
        super.onStart();
    }

    @Override
    public void onLocalVoiceInteractionStopped() {
        super.onLocalVoiceInteractionStopped();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
    }

    public void initView() {
        mTextNameTrack = findViewById(R.id.text_name);
        mTextArtist = findViewById(R.id.text_artist);
        mImageDisk = findViewById(R.id.image_disk);
        mImageButtonFavorite = findViewById(R.id.image_button_favorite);
        mImageButtonDownload = findViewById(R.id.image_button_download);
        mTexTimeStart = findViewById(R.id.text_start);
        mTextTimeFinish = findViewById(R.id.text_finish);
        mSeekBarMusic = findViewById(R.id.seek_bar_music);
        mImageButtonPausePlay = findViewById(R.id.image_button_play);
        mImageButtonNext = findViewById(R.id.image_button_next);
        mImageButtonPrevious = findViewById(R.id.image_button_previous);
        mImageButtonShuffle = findViewById(R.id.image_button_shuffle);
        mImageButtonRepeat = findViewById(R.id.image_button_repeat);
        mImageButtonFavorite.setOnClickListener(this);
        mImageButtonDownload.setOnClickListener(this);
        mImageButtonPausePlay.setOnClickListener(this);
        mImageButtonNext.setOnClickListener(this);
        mImageButtonPrevious.setOnClickListener(this);
        mImageButtonShuffle.setOnClickListener(this);
        mImageButtonRepeat.setOnClickListener(this);
        mObjectAnimator = ObjectAnimator.ofFloat(mImageDisk, "rotation", 0, 360);
        mObjectAnimator.setDuration(15000);
        mObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
    }
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.null_string);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, PlayMusicActivity.class);
        return intent;
    }

    public void getDataIntent() {
        Intent intent = getIntent();
    }
    private void getDataView(){
        Track track = mPlayMusicService.getTrack();
        mTextNameTrack.setText(mPlayMusicService.getTrack().getTitle());
        mTextArtist.setText(mPlayMusicService.getTrack().getArtist());
        Glide.with(mImageDisk)
                .load(mPlayMusicService.getTrack().getArtworkUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_disc).transforms(new CircleCrop()))
                .into(mImageDisk);
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
        mPlayMusicService = binder.getService();
        mPlayMusicService.setPlayMusicListener(this);
        getDataView();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mPlayMusicService.setPlayMusicListener(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpPlayPauseImage(boolean isPlaying) {
        if (isPlaying) {
            mImageButtonPausePlay.setImageResource(R.drawable.ic_pause);
            mObjectAnimator.resume();
        }
        else {
            mImageButtonPausePlay.setImageResource(R.drawable.ic_play);
            mObjectAnimator.pause();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_button_play:
                mPlayMusicService.startTrack();
                break;
            case R.id.image_button_previous:
                mObjectAnimator.end();
                break;
            case R.id.image_button_next:
                mObjectAnimator.end();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void playbackStatusChange(boolean isPlaying) {

    }

    @Override
    public void updateCurrentTime(int position) {

    }

    @Override
    public void updateSongImageAndDuration(byte[] songImage, int duration) {

    }
}
