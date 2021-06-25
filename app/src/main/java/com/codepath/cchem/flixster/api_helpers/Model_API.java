package com.codepath.cchem.flixster.api_helpers;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

import static com.codepath.cchem.flixster.models.Movie.helpSetYoutubekey;

public class Model_API {
    public static String YOUTUBE_TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=1c84fc98c01175a473765df933f04306";
    public static final String TAG = "Movie API";


    /*
        I'm keeping the process to get the youtube link as a child of the movie
        In the future, each model should have a helper class that does it's API calls.
     */
    public static void setYoutubelinkAPI(int movieId) {
        // API PROCESS
        // 1. Create an http client
        AsyncHttpClient client = new AsyncHttpClient();
        // 2. Use the client to get data through our API
        client.get(String.format(YOUTUBE_TRAILER_URL, movieId), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    if(results.length()==0){
                        return;
                    }
                    String youtubeKey = (String) results.getJSONObject(0).getString("key");
                    helpSetYoutubekey(youtubeKey);
                    Log.d(TAG, youtubeKey);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}
/*

*/
