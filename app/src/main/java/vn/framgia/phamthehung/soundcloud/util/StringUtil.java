package vn.framgia.phamthehung.soundcloud.util;

import vn.framgia.phamthehung.soundcloud.BuildConfig;

public class StringUtil {
    public static String initGenreApi(String keyGenre) {
        return String.format(Constants.BASE_URL_GENRE
                , keyGenre
                , BuildConfig.ApiKey
                , Constants.LIMIT
                , Constants.OFFSET);
    }
    public static String initDownloadApi(String url) {
        return String.format(url, Constants.PARAMETER_ID, BuildConfig.ApiKey);
    }

    public static String initString(String a, String b) {
        return String.format(Constants.STRING_FORMAT, a, b);
    }

    public static String initStreamApi(int trackId) {
        return String.format(Constants.BASE_URL_STREAM
                , trackId
                , BuildConfig.ApiKey);
    }
    public static String initImageFull(String url){
        return url.replace(Constants.STRING_LARGE,Constants.STRING_IMAGE);
    }
}
