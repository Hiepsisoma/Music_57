package vn.framgia.phamthehung.soundcloud.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import vn.framgia.phamthehung.soundcloud.util.StringUtil;

public class Track implements Parcelable {
    private int mId;
    private int mDuration;
    private String mTitle;
    private String mArtist;
    private String mStreamUrl;
    private String mDownloadUrl;
    private String mArtworkUrl;
    private boolean mIsDownloadable;
    private boolean mIsOffline;
    private String mGenre;
    private int mLikesCount;
    private int mPlaybackCount;
    private int mCommentCount;

    public Track(int id, String title, String artist) {
        mId = id;
        mTitle = title;
        mArtist = artist;
    }

    protected Track(Parcel in) {
        mId = in.readInt();
        mDuration = in.readInt();
        mTitle = in.readString();
        mArtist = in.readString();
        mStreamUrl = in.readString();
        mDownloadUrl = in.readString();
        mArtworkUrl = in.readString();
        mIsDownloadable = in.readByte() != 0;
        mIsOffline = in.readByte() != 0;
        mGenre = in.readString();
        mLikesCount = in.readInt();
        mPlaybackCount = in.readInt();
        mCommentCount = in.readInt();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getStreamUrl() {
        return StringUtil.initStreamApi(getId());
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public boolean isDownloadable() {
        return mIsDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        mIsDownloadable = downloadable;
    }

    public boolean isOffline() {
        return mIsOffline;
    }

    public void setOffline(boolean offline) {
        mIsOffline = offline;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public void setLikesCount(int likesCount) {
        mLikesCount = likesCount;
    }

    public int getPlaybackCount() {
        return mPlaybackCount;
    }

    public void setPlaybackCount(int playbackCount) {
        mPlaybackCount = playbackCount;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mDuration);
        dest.writeString(mTitle);
        dest.writeString(mArtist);
        dest.writeString(mStreamUrl);
        dest.writeString(mDownloadUrl);
        dest.writeString(mArtworkUrl);
        dest.writeByte((byte) (mIsDownloadable ? 1 : 0));
        dest.writeByte((byte) (mIsOffline ? 1 : 0));
        dest.writeString(mGenre);
        dest.writeInt(mLikesCount);
        dest.writeInt(mPlaybackCount);
        dest.writeInt(mCommentCount);
    }
}
