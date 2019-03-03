package vn.framgia.phamthehung.soundcloud.service;

public interface PlayMusicListener {
    void playbackStatusChange(boolean isPlaying);

    void updateCurrentTime(int position);

    void updateSongImageAndDuration(byte[] songImage, int duration);
}
