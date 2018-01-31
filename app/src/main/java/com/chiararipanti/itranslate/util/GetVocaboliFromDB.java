package com.chiararipanti.itranslate.util;

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

import com.chiararipanti.itranslate.db.Vocabolo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetVocaboliFromDB extends AsyncTask<String, Void, ArrayList<Vocabolo>> {
    String tipo;
    ArrayList<Vocabolo> vocaboli;
    final String TAG = "GetVocaboliFromDB";
    String lingua;


    public GetVocaboliFromDB(Context context, String tipo) {
        this.tipo = tipo;
        vocaboli = new ArrayList<Vocabolo>();

    }

    protected void onPreExecute() {
        lingua = Locale.getDefault().getLanguage();
        if ((!lingua.equals("it")) && (!lingua.equals("es")) && (!lingua.equals("fr")) && (!lingua.equals("de")))
            lingua = "it";

    }


    protected ArrayList<Vocabolo> doInBackground(String... params) {

        //String stringa_ingrediente="";
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("tipo", tipo));
        Log.v(TAG, "lingua" + lingua);
        nameValuePairs.add(new BasicNameValuePair("lingua", lingua));
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://sfidaricette.altervista.org/getVocabolibycategoria.php");
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs);
            request.setEntity(formEntity);

            //execute httpPost
            HttpResponse response = client.execute(request);
            Log.v(TAG, "prova2" + lingua);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            //Log.e(TAG,"sono nel catch");
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

                ////Log.v("risposta",result);
            } catch (Exception e) {
                //Log.e(TAG,"sono nel catch");
            }


            //parsing dei dati in formato json
            //Log.v(TAG,"prima di try");
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String lingua_nativa = json_data.getString(lingua);
                    String inglese = json_data.getString("inglese");
                    String frase = json_data.getString("frase");
                    String img = json_data.getString("img");

                    Vocabolo vocabolo = new Vocabolo(inglese, lingua_nativa, frase, img);
                    vocaboli.add(vocabolo);
                }
            } catch (JSONException e) {
                //Log.v(TAG,"catch");
            }


        } else {
            //Log.e(TAG,"non ho trovato niente");
        }
        return vocaboli;

    }


    @Override
    protected void onPostExecute(ArrayList<Vocabolo> result) {

        //Log.v(TAG,"post");
    } //fine onPost
} //fine AsyncTask