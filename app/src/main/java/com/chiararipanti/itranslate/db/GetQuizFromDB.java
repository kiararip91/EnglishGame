package com.chiararipanti.itranslate.db;

import java.io.BufferedReader;
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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.chiararipanti.itranslate.util.TestSession;

//TODO: Integra con FireBase
public class GetQuizFromDB extends AsyncTask<String, Void, ArrayList<TestSession>> {
    private ArrayList<TestSession> quizs;
    private int type;


    public GetQuizFromDB(int type) {
        this.type = type;
        quizs = new ArrayList<>();
    }

    protected ArrayList<TestSession> doInBackground(String... params) {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("type", type + ""));
        String TAG = "GetQuizFromDB";

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://sfidaricette.altervista.org/getQuiz.php");
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
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                is.close();
                result = sb.toString();

                ////Log.v("risposta",result);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            //parsing dei dati in formato json
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String question = json_data.getString("text");
                    String[] alternativesArray = json_data.getString("alternative").split(",");
                    String answer = alternativesArray[0];
                    ArrayList<String> wrongAlternatives = new ArrayList<>();
                    wrongAlternatives.add(alternativesArray[1]);
                    wrongAlternatives.add(alternativesArray[2]);
                    wrongAlternatives.add(alternativesArray[3]);
                    TestSession test = new TestSession(question, answer, wrongAlternatives);
                    quizs.add(test);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

        }
        return quizs;

    }


    @Override
    protected void onPostExecute(ArrayList<TestSession> result) {
    } //fine onPost
} //fine AsyncTask