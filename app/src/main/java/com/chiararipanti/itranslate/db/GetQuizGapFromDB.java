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

import com.chiararipanti.itranslate.model.QuizGap;
import com.chiararipanti.itranslate.model.Translation;
import com.chiararipanti.itranslate.util.EnglishGameConstraint;
import com.fasterxml.jackson.databind.ObjectMapper;

//TODO: Capire perche non viene usata e ottimizzare/pulire codice
public class GetQuizGapFromDB extends AsyncTask<String, Void, ArrayList<QuizGap>> {
    ArrayList<QuizGap> quizGaps;
    final String TAG = "GetQuizGapFromDB";
    int type;
    private String requestBaseUrl = EnglishGameConstraint.HTTP_REQUEST_BASE_URL;
    private String requestResource = "getQuizGapByType.php";


    public GetQuizGapFromDB(Context context, int type) {
        this.type = type;
        quizGaps = new ArrayList<>();

    }

    protected ArrayList<QuizGap> doInBackground(String... params) {
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

            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    ObjectMapper mapper = new ObjectMapper();
                    QuizGap quizGap = mapper.readValue(String.valueOf(json_data), QuizGap.class);
                    quizGaps.add(quizGap);
                }
            } catch (Exception e) {
                Log.v(TAG, e.getMessage());
            }


        } else {
            Log.e(TAG,"Null response");
        }
        return quizGaps;
    }


    @Override
    protected void onPostExecute(ArrayList<QuizGap> result) {}
}