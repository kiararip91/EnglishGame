package com.chiararipanti.itranslate.db;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.chiararipanti.itranslate.model.Speak;
import com.chiararipanti.itranslate.model.Word;
import com.chiararipanti.itranslate.util.EnglishGameConstraint;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;



public class GetSpeakFromDB extends AsyncTask<String, Void, ArrayList<Speak>> {
    private int level;
    private ArrayList<Speak> speaks;
    private final String TAG = "GetSpeakFromDB";
    private String requestBaseUrl = EnglishGameConstraint.HTTP_REQUEST_BASE_URL;
    private String requestResource = "getSpeakByLevel.php";


    public GetSpeakFromDB(Context context, int level) {
        this.level = level;
        speaks = new ArrayList<>();

    }


    protected ArrayList<Speak> doInBackground(String... params) {

        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("level", level + ""));
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
            Log.e(TAG, e.getMessage());
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

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    ObjectMapper mapper = new ObjectMapper();
                    Speak speak = mapper.readValue(String.valueOf(json_data), Speak.class);
                    speaks.add(speak);
                }
            } catch (Exception e) {
                Log.v(TAG, e.getMessage());
            }


        } else {
            Log.e(TAG,"Nothing Found");
        }
        return speaks;

    }

    protected void onPostExecute(ArrayList<Word> result) {} //fine onPost
}