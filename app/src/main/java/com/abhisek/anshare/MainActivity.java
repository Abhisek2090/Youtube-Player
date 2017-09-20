package com.abhisek.anshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private Context mContext;
    String url;
    YouTubePlayer youTubePlayer;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;


        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        this.youTubePlayer=youTubePlayer;
        if (!b) {
           //youTubePlayer.cueVideo("cdgQpa1pUUE");
           // youTubePlayer.cuePlaylist("*");
            new getData().execute();

            String url = "https://youtu.be/o6IBwJWDOn8";
            youTubePlayer.cueVideo(getVideoId(url));


        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public static String getVideoId(String watchLink) {
        return watchLink.substring(watchLink.length() - 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DeveloperKey.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private class getData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        @Override
        protected String doInBackground(Void... urls) {

            //  String access_token = session.pref.getString(ACCESSTOKEN, null);

            try {


                URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=8LDkrSLSJP8CNRLmkc7teY4LMsxl0w676NAlV2hq");
                //  String access_token = "$2b$12$jF.B4dv5SwmXr5vA7TCzf.pKjUU/ruMUuvBXPRePm47AXIVW.4txe";
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // urlConnection.setRequestProperty("access-token", access_token);


                //  String encodedAuth="Basic "+ new String(new Base64().encode(authorization.getBytes()));
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }

        }

        protected void onPostExecute(String response) {

            Log.i("response", response);

            try {

                JSONObject data = new JSONObject(response);
                url = data.getString("url");
                Log.i("url", url);

            }catch (Exception e) {
                e.printStackTrace();
            }

         //   youTubePlayer.cueVideo(url);


        }
    }


}
