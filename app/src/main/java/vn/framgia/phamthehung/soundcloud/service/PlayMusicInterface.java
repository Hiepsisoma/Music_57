package vn.framgia.phamthehung.soundcloud.service;

import vn.framgia.phamthehung.soundcloud.data.model.Track;

public interface PlayMusicInterface {
    void startTrack();

    void changeTrack(Track track);

    void pauseTrack();

    void previousTrack();

    void nextTrack();

    void stopTrack();

    void seek(int milis);

    long getDuration();

    long getCurrentDuration();

    void shuffleTracks();

    void unShuffleTracks();

    int getMediaPlayerState();
}
