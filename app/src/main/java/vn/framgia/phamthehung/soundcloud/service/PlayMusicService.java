package vn.framgia.phamthehung.soundcloud.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import vn.framgia.phamthehung.soundcloud.R;
import vn.framgia.phamthehung.soundcloud.data.model.Track;
import vn.framgia.phamthehung.soundcloud.mediaplayer.MediaPlayerManager;
import vn.framgia.phamthehung.soundcloud.mediaplayer.MediaPlayerSetting;
import vn.framgia.phamthehung.soundcloud.ui.playmusic.PlayMusicActivity;

public class PlayMusicService extends Service implements PlayMusicInterface,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public static final String TAG = "Service";
    private static final String NAME_CHANNEL = "NAME_CHANNEL";
    private static final String ID_CHANNEL = "ID_CHANNEL";
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 1000;
    private static final int CODE_PLAY = 1001;
    private static final int CODE_PAUSE = 1002;
    private static final int CODE_NEXT = 1003;
    private static final int CODE_PREVIOUS = 1004;
    private static final String ACTION_PREVIOUS = "vn.framgia.phamthehung.soundcloud.ACTION_PREVIOUS";
    private static final String ACTION_PLAY = "vn.framgia.phamthehung.soundcloud.ACTION_PLAY";
    private static final String ACTION_PAUSE = "vn.framgia.phamthehung.soundcloud.ACTION_PAUSE";
    private static final String ACTION_NEXT = "vn.framgia.phamthehung.soundcloud.ACTION_NEXT";
    private final IBinder mIBinder = new LocalBinder();
    private Notification.Builder mBuilder;
    private Bitmap mBitmap;
    private boolean isBind;
    private Track mTrack;
    private List<PlayMusicListener> mListeners;
    private PlayMusicListener mPlayMusicListener;
    private MediaPlayerManager mMediaPlayerManager;
    @Override
    public IBinder onBind(Intent intent) {
        isBind = true;
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBind = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIncomingActions(intent);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayerManager = MediaPlayerManager.getInstance(this);
        mListeners = new ArrayList<>();
    }

    public Track getTrack() {
        return mTrack;
    }

    public void setTrack(Track track) {
        mTrack = track;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }

    private List<Track> mTracks;

    public void setPlayMusicListener(PlayMusicListener playMusicListener) {
        mPlayMusicListener = playMusicListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification buidNotification() {
        createNotificationChannel();
        loadImage();
        int actionPlayPauseImage;
        PendingIntent playPauseAction;
        if (mMediaPlayerManager.getState() == MediaPlayerSetting.StateType.PAUSE) {
            actionPlayPauseImage = R.drawable.ic_pause;
            playPauseAction = getPlayBackAction(CODE_PAUSE);
        } else {
            actionPlayPauseImage = R.drawable.ic_play;
            playPauseAction = getPlayBackAction(CODE_PLAY);
        }
        Intent intent = new Intent(this, PlayMusicActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent
                        , PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new Notification.Builder(this)
                .setShowWhen(false)
                .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2))
                .setSmallIcon(R.drawable.heads)
                .setContentTitle(getTrack().getTitle())
                .setContentText(getTrack().getArtist())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_previous
                        , getString(R.string.title_previous), getPlayBackAction(CODE_PREVIOUS))
                .addAction(actionPlayPauseImage
                        , getString(R.string.title_play_pause), playPauseAction)
                .addAction(R.drawable.ic_next
                        , getString(R.string.title_next), getPlayBackAction(CODE_NEXT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(ID_CHANNEL);
        }
        return mBuilder.build();
    }


    public void loadImage() {
        Glide.with(this)
                .asBitmap()
                .load(getTrack().getArtworkUrl())
                .apply(new RequestOptions().error(R.drawable.genre))
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        mBitmap = resource;
                        mBuilder.setLargeIcon(mBitmap);
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                    }
                });
    }

    private void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(ID_CHANNEL, NAME_CHANNEL, importance);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    private PendingIntent getPlayBackAction(int actionNumber) {
        Intent playBackAction = new Intent(this, PlayMusicActivity.class);
        switch (actionNumber) {
            case CODE_PLAY:
                playBackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playBackAction, 0);
            case CODE_PAUSE:
                playBackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playBackAction, 0);
            case CODE_NEXT:
                playBackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playBackAction, 0);
            case CODE_PREVIOUS:
                playBackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playBackAction, 0);
            default:
                break;
        }
        return null;
    }


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;
        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            playAndPauseTrack();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            playAndPauseTrack();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            nextTrack();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            previousTrack();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startTrack() {
        mMediaPlayerManager.start();
        startForeground(NOTIFICATION_ID, buidNotification());
    }

    @Override
    public void changeTrack(Track track) {
        mMediaPlayerManager.changeTrack(track);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void pauseTrack() {
        mMediaPlayerManager.pause();
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, buidNotification());
    }

    @Override
    public void previousTrack() {
        mMediaPlayerManager.previous();
    }

    @Override
    public void nextTrack() {
        mMediaPlayerManager.next();
    }

    @Override
    public void stopTrack() {
        mMediaPlayerManager.stop();
    }

    @Override
    public void seek(int milis) {
        mMediaPlayerManager.seek(milis);
    }

    @Override
    public long getDuration() {
        return mMediaPlayerManager.getDuration();
    }

    @Override
    public long getCurrentDuration() {
        return mMediaPlayerManager.getCurrentDuration();
    }

    @Override
    public void shuffleTracks() {
        mMediaPlayerManager.shuffleTracks();
    }

    @Override
    public void unShuffleTracks() {
        mMediaPlayerManager.unShuffleTracks();
    }

    @Override
    public int getMediaPlayerState() {
        return 0;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (mMediaPlayerManager.getLoopType()) {
            case MediaPlayerSetting.LoopType.ONE:
                changeTrack(getTrack());
                break;
            case MediaPlayerSetting.LoopType.ALL:
                nextTrack();
                break;
            case MediaPlayerSetting.LoopType.NONE:
                if (isLastTracks(getTrack())) {
                    stopTrack();
                } else {
                    nextTrack();
                }
                break;
            default:
                break;
        }
    }

    private boolean isLastTracks(Track currentTrack) {
        return mMediaPlayerManager.getTracks().indexOf(currentTrack) == mMediaPlayerManager
                .getTracks().size() - 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void playAndPauseTrack() {
        if (getMediaPlayerState() == MediaPlayerSetting.StateType.PAUSE) {
            startTrack();
        } else {
            pauseTrack();
        }
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, buidNotification());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPrepared(MediaPlayer mp) {
        startForeground(NOTIFICATION_ID, buidNotification());
        startTrack();
    }

    public class LocalBinder extends Binder {
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }
}

