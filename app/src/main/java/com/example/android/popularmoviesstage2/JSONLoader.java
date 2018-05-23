package com.example.android.popularmoviesstage2;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONLoader {
    private static final String API_KEY=BuildConfig.API_KEY;
    private static final String MOVIE_BASE_URI = "http://api.themoviedb.org/3";
    private static final String TAG = JSONLoader.class.getSimpleName();

    public static JSONObject load(String loadUri) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;
        Log.e(TAG,"loadUri:"+loadUri);

        try {
            Uri builtUri = Uri.parse(MOVIE_BASE_URI + loadUri).buildUpon()
                    .appendQueryParameter("api_key", API_KEY).build();
            URL url = new URL(builtUri.toString());
            Log.v(TAG,"Built URI " +builtUri.toString() );

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();

            if(inputStream == null) {
                Log.e(TAG,"load inputStream is null");
                return null;
            }

            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            if(buffer.length() == 0) {
                //Stream is empty. So no need to parse.
                Log.e(TAG,"load network buffer is null");
                return null;
            }

            movieJsonStr = buffer.toString();
            Log.v(TAG,"Movie JSON String:"+ movieJsonStr);
            JSONObject jObj = new JSONObject(movieJsonStr);
            return jObj;

        } catch(IOException | JSONException e) {
            Log.e(TAG, "Error:", e);
            return null;
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream:",e);
                }
            }
        }

    }
}

