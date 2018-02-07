package com.chiararipanti.itranslate.db;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chiararipanti.itranslate.MainGameActivity;
import com.chiararipanti.itranslate.util.EnglishGameConstraint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;


public class GetSpeakFromDB extends AsyncTask<String, Void, ArrayList<String>> {
    private int level;
    private ArrayList<String> sentences;
    private final String TAG = "GetSpeakFromDB";
    private String requestBaseUrl = EnglishGameConstraint.HTTP_REQUEST_BASE_URL;
    private String requestResource = "getSpeakByLevel.php";


    public GetSpeakFromDB(Context context, int level) {
        this.level = level;
        sentences = new ArrayList<String>();

    }


    protected ArrayList<String> doInBackground(String... params) {

        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
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

			   
		        /*converto la risposta in stringa*/
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
                Log.e(TAG, "sono nel catch");
            }


            //parsing dei dati in formato json
            //FIXME: Parsing automatico dal json
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    sentences.add(json_data.getString("sentence"));
                }
            } catch (JSONException e) {
                Log.v(TAG, "catch");
            }


        } else {
            //Log.e(TAG,"non ho trovato niente");
        }
        return sentences;

    }


    @Override
    protected void onPostExecute(ArrayList<String> result) {

        //Log.v(TAG,"post");
    } //fine onPost
} //fine AsyncTask