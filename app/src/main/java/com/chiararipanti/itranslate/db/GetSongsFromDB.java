package com.chiararipanti.itranslate.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.chiararipanti.itranslate.model.Song;
import com.chiararipanti.itranslate.util.EnglishGameConstraint;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class GetSongsFromDB extends AsyncTask<String, Void, ArrayList<Song>> {
    ArrayList<Song> songs;
    final String TAG = "GetSongsFromDB";
    private String requestBaseUrl = EnglishGameConstraint.HTTP_REQUEST_BASE_URL;
    private String requestResource = "getSongs.php";


    public GetSongsFromDB(Context context) {
        songs = new ArrayList<Song>();

    }

    protected ArrayList<Song> doInBackground(String... params) {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(requestBaseUrl + requestResource);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs);
            request.setEntity(formEntity);

            //execute httpPost
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }

        if (is != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                is.close();
                result = sb.toString();
                Log.d(TAG, result);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    ObjectMapper mapper = new ObjectMapper();
                    Song song = mapper.readValue(String.valueOf(json_data), Song.class);
                    songs.add(song);
                }
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }


        } else {
            Log.e(TAG,"No Songs founded");
        }
        return songs;
    }

    @Override
    protected void onPostExecute(ArrayList<Song> result) {}
} //fine AsyncTask