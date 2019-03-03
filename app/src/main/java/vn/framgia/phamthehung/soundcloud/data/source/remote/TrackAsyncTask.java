package vn.framgia.phamthehung.soundcloud.data.source.remote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.framgia.phamthehung.soundcloud.data.model.Track;
import vn.framgia.phamthehung.soundcloud.data.source.TrackDataSource;
import vn.framgia.phamthehung.soundcloud.util.StringUtil;

public class TrackAsyncTask extends BaseAsyncTask<Track> {
    private static final String ARTWORK_URL = "artwork_url";
    private static final String COLLECTION = "collection";
    public static final String ID = "id";
    private static final String KEY_USER = "user";
    private static final String KEY_USER_NAME = "username";
    private static final String TITLE = "title";
    private static final String TRACK = "track";
    private static final String DOWNLOADABLE = "downloadable";
    private static final String DOWNLOAD_URL = "download_url";
    private static final String LIKES_COUNT = "likes_count";
    private static final String PLAYBACK_COUNT = "playback_count";
    private static final String COMMENT_COUNT = "comment_count";

    public TrackAsyncTask(TrackDataSource.DataCallback<Track> callback) {
        super(callback);
    }

    @Override
    public List convertJson(String jsonString) {
        List<Track> tracks = new ArrayList<>();
        try {
            JSONObject result = new JSONObject(jsonString);
            JSONArray collection = result.getJSONArray(COLLECTION);
            for (int i = 0; i < collection.length(); i++) {
                JSONObject trackInfo = collection.getJSONObject(i);
                JSONObject track = trackInfo.getJSONObject(TRACK);
                int id = track.getInt(ID);
                String title = track.getString(TITLE);
                String artworkUrl = StringUtil.initImageFull(track.getString(ARTWORK_URL));
                int likesCount = track.getInt(LIKES_COUNT);
                int playbackCount = track.getInt(PLAYBACK_COUNT);
                int commentCount = track.getInt(COMMENT_COUNT);
                String artist = track.getJSONObject(KEY_USER)
                        .getString(KEY_USER_NAME);
                boolean isDownloadable = track.getBoolean(DOWNLOADABLE);
                String downloadUrl = null;
                if (isDownloadable) {
                    downloadUrl = StringUtil.initDownloadApi(track.getString(DOWNLOAD_URL));
                }
                Track trackObject = new Track(id, title, artist);
                trackObject.setArtworkUrl(artworkUrl);
                trackObject.setDownloadable(isDownloadable);
                trackObject.setDownloadUrl(downloadUrl);
                trackObject.setLikesCount(likesCount);
                trackObject.setPlaybackCount(playbackCount);
                trackObject.setCommentCount(commentCount);
                tracks.add(trackObject);
            }
        } catch (JSONException e) {
            mException = e;
        }
        return tracks;
    }
}
