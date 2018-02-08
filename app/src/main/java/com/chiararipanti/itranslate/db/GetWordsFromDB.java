package com.chiararipanti.itranslate.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

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

import com.chiararipanti.itranslate.model.Word;
import com.chiararipanti.itranslate.util.EnglishGameConstraint;
import com.fasterxml.jackson.databind.ObjectMapper;


public class GetWordsFromDB extends AsyncTask<String, Void, ArrayList<Word>> {
    private int level;
    private ArrayList<Word> words;
    private final String TAG = "GetwordsFromDB";
    private String language;
    private String requestBaseUrl = EnglishGameConstraint.HTTP_REQUEST_BASE_URL;
    private String requestResource = "getWordsByLevel.php";


    public GetWordsFromDB(int level) {
        this.level = level;
        words = new ArrayList<Word>();

    }

    protected void onPreExecute() {
         language = Locale.getDefault().getLanguage();
        if ((!language.equals("it")) && (!language.equals("es")) && (!language.equals("fr")) && (!language.equals("de")))
            language = "it";

    }


    protected ArrayList<Word> doInBackground(String... params) {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("level", level+""));
        Log.v(TAG, "language" + language);
        nameValuePairs.add(new BasicNameValuePair("language", language));
        try {
            HttpClient client = new DefaultHttpClient();
            Log.v(TAG, "url" + requestBaseUrl + requestResource);
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
                Log.e(TAG,e.getMessage());
            }

            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    ObjectMapper mapper = new ObjectMapper();
                    Word word = mapper.readValue(String.valueOf(json_data), Word.class);
                    words.add(word);
                }
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }

        }
        return words;

    }

    @Override
    protected void onPostExecute(ArrayList<Word> result) {}
} //fine AsyncTask