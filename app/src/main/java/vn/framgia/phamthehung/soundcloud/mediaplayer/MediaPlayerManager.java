package vn.framgia.phamthehung.soundcloud.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.framgia.phamthehung.soundcloud.data.model.Track;
import vn.framgia.phamthehung.soundcloud.service.PlayMusicService;

public class MediaPlayerManager extends MediaPlayerSetting implements MediaPlayInterface {
    private static MediaPlayerManager sInstance;
    private List<Track> mTracks;
    private Track mTrack;
    private MediaPlayer mMediaPlayer;
    private PlayMusicService mPlayMusicService;
    private Context mContext;


    public MediaPlayerManager(PlayMusicService playMusicService) {
        mTracks = new ArrayList<>();
        super.setLoopType(LoopType.NONE);
        super.setShuffleType(ShuffleType.OFF);
        super.setState(StateType.PAUSE);
        mPlayMusicService = playMusicService;
        mContext = mPlayMusicService;
    }

    public static MediaPlayerManager getInstance(PlayMusicService musicService) {
        if (sInstance == null) {
            sInstance = new MediaPlayerManager(musicService);
        }
        return sInstance;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }

    public Track getTrack() {
        return mTrack;
    }

    public void setTrack(Track track) {
        mTrack = track;
    }

    public void initPlayerOnline() {
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void create(Track track) {
        initPlayerOnline();
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(track.getStreamUrl()));
            mMediaPlayer.setOnPreparedListener(mPlayMusicService);
            mMediaPlayer.setOnCompletionListener(mPlayMusicService);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start() {
        setState(StateType.PLAY);
        mMediaPlayer.start();
    }

    @Override
    public void changeTrack(Track track) {
        mTrack = track;
        create(track);
    }

    @Override
    public void pause() {
        setState(StateType.PAUSE);
        mMediaPlayer.pause();
    }

    @Override
    public void previous() {
        if(getShuffleType()== ShuffleType.OFF){
            changeTrack(getPreviousTrack());
        }
        else{
            changeTrack(getRandomTrack());
        }
    }

    @Override
    public void next() {
        if(getShuffleType()== ShuffleType.OFF){
            changeTrack(getNextTrack());
        }
        else{
            changeTrack(getRandomTrack());
        }
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
    }

    @Override
    public void release() {
        mMediaPlayer.release();
    }

    @Override
    public void reset() {
        mMediaPlayer.reset();
    }

    @Override
    public void seek(int milis) {
        mMediaPlayer.seekTo(milis);
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public long getCurrentDuration() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void shuffleTracks() {
        super.setShuffleType(ShuffleType.ON);
    }

    @Override
    public void unShuffleTracks() {
        super.setShuffleType(ShuffleType.OFF);
    }

    @Override
    public int getLoopType() {
        return super.getLoopType();
    }

    @Override
    public void setLoopType(int loopType) {
        super.setLoopType(loopType);
    }

    private Track getPreviousTrack() {
        int position = mTracks.indexOf(mTrack);
        if (position == 0) {
            return mTracks.get(mTracks.size() -1);
        }
        return mTracks.get(position - 1);
    }

    private Track getNextTrack() {
        int position = mTracks.indexOf(mTrack);
        if (position == mTracks.size() - 1) {
            return mTracks.get(0);
        }
        return mTracks.get(position + 1);
    }
    private Track getRandomTrack(){
        Random random = new Random();
        return mTracks.get(random.nextInt(mTracks.size()));
    }
}
