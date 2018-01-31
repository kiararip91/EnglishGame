package com.chiararipanti.itranslate.util;

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

import com.chiararipanti.itranslate.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;


public class GetSentencesFromDB extends AsyncTask<String, Void, ArrayList<String>> {
    int level;
    ArrayList<String> sentences;
    final String TAG = "GetSentencesFromDB";


    public GetSentencesFromDB(Context context, int level) {
        this.level = level;
        sentences = new ArrayList<String>();

    }


    protected ArrayList<String> doInBackground(String... params) {

        //String stringa_ingrediente="";
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("level", level + ""));
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://sfidaricette.altervista.org/getSentencesByLevel.php");
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs);
            request.setEntity(formEntity);

            //execute httpPost
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e(TAG, "sono nel catch");
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

                Log.v("risposta", result);
            } catch (Exception e) {
                Log.e(TAG, "sono nel catch");
            }


            //parsing dei dati in formato json
            Log.v(TAG, "prima di try");
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    sentences.add(json_data.getString("sent"));
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