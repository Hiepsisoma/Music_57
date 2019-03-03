package vn.framgia.phamthehung.soundcloud.mediaplayer;

import vn.framgia.phamthehung.soundcloud.data.model.Track;

public interface MediaPlayInterface {
    void create(Track track);

    void start();

    void changeTrack(Track track);

    void pause();

    void previous();

    void next();

    void stop();

    void release();

    void reset();

    void seek(int milis);

    long getDuration();

    long getCurrentDuration();

    void shuffleTracks();

    void unShuffleTracks();
}
