package com.chiararipanti.itranslate.db;

import java.io.BufferedReader;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.chiararipanti.itranslate.model.Translation;
import com.chiararipanti.itranslate.util.EnglishGameConstraint;

//TODO: Capire perche non viene usata e ottimizzare/pulire codice
public class GetQuizTraduzioneFromDB extends AsyncTask<String, Void, ArrayList<Translation>> {
    ArrayList<Translation> quizs;
    final String TAG = "GetQuizTraduzioneFromDB";
    int type;
    private String requestBaseUrl = EnglishGameConstraint.HTTP_REQUEST_BASE_URL;
    private String requestResource = "getQuizGapByType.php";


    public GetQuizTraduzioneFromDB(Context context, int type) {
        this.type = type;
        quizs = new ArrayList<Translation>();

    }

    protected ArrayList<Translation> doInBackground(String... params) {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("type", type + ""));
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
                Log.e(TAG, e.getMessage());
            }

            //parsing dei dati in formato json
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String text = json_data.getString("question");
                    String traduzione = json_data.getString("answer");
                    Translation quizTraduzione = new Translation(text, traduzione);
                    quizs.add(quizTraduzione);
                }
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }


        } else {
            Log.e(TAG,"Null response");
        }
        return quizs;
    }


    @Override
    protected void onPostExecute(ArrayList<Translation> result) {
    } //fine onPost
} //fine AsyncTask