package com.chiararipanti.itranslate.util;

import android.app.Activity;
import android.app.Application;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.chiararipanti.itranslate.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * @author chiararipanti
 * date 29/01/18.
 */

public class AudioRequest extends AsyncTask<String, Void, String> {
    private int statusCode;
    private Activity activity;
    private MediaPlayer mediaPlayer;

    public AudioRequest(Activity activity, MediaPlayer mediaPlayer){
        this.activity = activity;
        this.mediaPlayer = mediaPlayer;

    }
    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(urls[0]));
            statusCode = response.getStatusLine().getStatusCode();

            Log.d("test", statusCode + "");
            if(statusCode==200)
                return "ok";

        } catch (IOException e) {
            return "errore";
        }
        return "errore";
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        if(result.equalsIgnoreCase("ok")){
            try {
                this.mediaPlayer.prepare();
                this.mediaPlayer.start();

            }
            catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(activity,activity.getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Toast.makeText(activity,activity.getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity,activity.getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(activity,activity.getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();

    }
}
