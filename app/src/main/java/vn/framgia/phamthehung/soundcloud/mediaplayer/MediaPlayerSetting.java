package vn.framgia.phamthehung.soundcloud.mediaplayer;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MediaPlayerSetting {
    protected int mLoopType;
    protected int mShuffleType;
    protected int mState;


    public int getLoopType() {
        return mLoopType;
    }

    public void setLoopType(int loopType) {
        mLoopType = loopType;
    }

    public int getShuffleType() {
        return mShuffleType;
    }

    public void setShuffleType(int shuffleType) {
        mShuffleType = shuffleType;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LoopType.NONE, LoopType.ONE, LoopType.ALL})
    public @interface LoopType {
        int NONE = 0;
        int ONE = 1;
        int ALL = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ShuffleType.OFF, ShuffleType.ON})
    public @interface ShuffleType {
        int OFF = 0;
        int ON = 1;
    }
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({StateType.PLAY, StateType.PAUSE})
    public @interface StateType {
        int PLAY = 0;
        int PAUSE = 1;
    }
}
