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

import com.chiararipanti.itranslate.model.QuizChoose;
import com.chiararipanti.itranslate.util.EnglishGameConstraint;
import com.chiararipanti.itranslate.util.TestSession;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetQuizChooseFromDB extends AsyncTask<String, Void, ArrayList<QuizChoose>> {
    private ArrayList<QuizChoose> quizChooses;
    private int type;
    private String requestBaseUrl = EnglishGameConstraint.HTTP_REQUEST_BASE_URL;
    private String requestResource = "getQuizChooseByType.php";


    public GetQuizChooseFromDB(int type) {
        this.type = type;
        quizChooses = new ArrayList<>();
    }

    protected ArrayList<QuizChoose> doInBackground(String... params) {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("type", type + ""));
        String TAG = "GetQuizChooseFromDB";

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
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
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
                    QuizChoose quizChoose = mapper.readValue(String.valueOf(json_data), QuizChoose.class);
                    quizChooses.add(quizChoose);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

        }
        return quizChooses;

    }


    @Override
    protected void onPostExecute(ArrayList<QuizChoose> result) {
    } //fine onPost
} //fine AsyncTask