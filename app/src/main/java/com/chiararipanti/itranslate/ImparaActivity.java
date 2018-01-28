package com.chiararipanti.itranslate;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.chiararipanti.itranslate.db.Vocabolo;
import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.GetVocaboliFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImparaActivity extends Activity {



    String categoria;
    ArrayList<Vocabolo> vocaboli;
    int prossimo;
    Vocabolo voc;
    MyConnectivityManager connectivityManager;
    AlertDialogManager alertDialog;
    String TAG="StartActivity";
    ImageButton ascolta;
    Button next;
    TextView parola_italiano_tv;
    TextView parola_inglese_tv;
    TextView frase_tv;
    TextView livello;
    MediaPlayer audio;
    boolean ascoltata;
    boolean sol;

    Context mcontext;

    //****************variabili per il bunner pubblicitario***************************
    /** The log tag. */
    //private static final String LOG_TAG = "BannerAdListener";
    /** The view to show the ad. */
    private AdView adView;
    //********************fine bunner pubblicitario******************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impara);
        Log.v(TAG,"0");

        Intent intent=getIntent();
        categoria=intent.getStringExtra("categoria");
        prossimo=0;
        ascoltata=false;
        sol=false;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        connectivityManager=new MyConnectivityManager(getApplicationContext());
        alertDialog=new AlertDialogManager();
        mcontext=getApplicationContext();

        ascolta=(ImageButton)(findViewById(R.id.audio));
        next=(Button)(findViewById(R.id.next));
        parola_italiano_tv=(TextView) findViewById(R.id.parola_italiano);
        parola_italiano_tv.setVisibility(View.GONE);
        parola_inglese_tv=(TextView) findViewById(R.id.parola_inglese);
        frase_tv=(TextView) findViewById(R.id.frase);
        livello=(TextView) findViewById(R.id.level);
        livello.setText(intent.getStringExtra("categoria1"));


        //****************inserimento bunner pubblicitario***************************
        //create adView
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.unit_id));
        // Add the AdView to the view hierarchy.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.footer);
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
                .build();
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        //******************  FINE  bunner pubblicitario***************************

        if(connectivityManager.check())
        {
            getVocaboli();
            impostaParola();
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent=new Intent(this,StartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getVocaboli()
    {
        //attraverso l'asinctask memorizzo dieci vocaboli della categoria scelta
        Log.v(TAG,"impara"+categoria);
        GetVocaboliFromDB getvocTask=new GetVocaboliFromDB(this,categoria);
        try {
            getvocTask.execute();
            vocaboli=getvocTask.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            alertDialog.showAlertDialog(ImparaActivity.this, "OPS!", getString(R.string.errore), false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            alertDialog.showAlertDialog(ImparaActivity.this, "OPS!",  getString(R.string.errore), false);
        }
        Log.v(TAG,"impara2"+vocaboli);
        voc=vocaboli.get(prossimo);
    }

    public void impostaParola()
    {
        ascoltata=false;
        parola_italiano_tv.setText(voc.getLingua_nativa());
        parola_inglese_tv.setText(voc.getInglese());
        parola_italiano_tv.setText(voc.getLingua_nativa());
        frase_tv.setText(voc.getFrase());
        Log.v("Vocabolo", voc.getInglese());
        Log.v("Img", voc.getImg());
        AudioRequest ar=new AudioRequest();
        Log.v("task","prima di execute");
        String ingl=voc.getInglese().toLowerCase();
        ingl=ingl.replaceAll("to ","");
        ingl=ingl.replaceAll("\\s","_");
        ingl=ingl.replaceAll("_\\[sb\\]","");
        ingl=ingl.replaceAll("_\\[sth\\]","");
        ingl=ingl.replaceAll("_\\[smb\\]","");
        ingl=ingl.replaceAll("\\[sb\\]","");
        ingl=ingl.replaceAll("\\[sth\\]","");
        ingl=ingl.replaceAll("\\[smb\\]","");

        String url="https://ssl.gstatic.com/dictionary/static/sounds/de/0/"+ingl+".mp3";
        if(connectivityManager.check())
        {
            ar.execute(url);

            new DownloadImageTask((ImageView) findViewById(R.id.immagine))
                    .execute(voc.getImg());
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();



    }


    public void next(View view)
    {
        if(connectivityManager.check())
        {
            if(!sol)
            {
                sol=true;
                next.setText("Next");
                parola_italiano_tv.setVisibility(View.VISIBLE);

            }
            else
            {
                sol=false;
                next.setText(getString(R.string.soluzione));
                parola_italiano_tv.setVisibility(View.GONE);
                prossimo++;
                if(prossimo<10)
                    voc=vocaboli.get(prossimo);
                else
                {
                    if(connectivityManager.check())
                    {
                        GetVocaboliFromDB getVocaboli=new GetVocaboliFromDB(this,categoria);
                        getVocaboli.execute();
                        try {
                            vocaboli=getVocaboli.get();
                            prossimo=0;
                            voc=vocaboli.get(prossimo);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            alertDialog.showAlertDialog(ImparaActivity.this, "OPS!", getString(R.string.errore), false);
                        } catch (ExecutionException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            alertDialog.showAlertDialog(ImparaActivity.this, "OPS!", getString(R.string.errore), false);
                        }

                    }
                    else
                    {
                        //Log.v("no connessione","no connessione");
                        alertDialog.showAlertDialog(ImparaActivity.this, getString(R.string.attenzione), getString(R.string.attiva_connessione), true);

                    }

                }
                impostaParola();
            }

        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();


    }

    public void ascolta(View view)
    {
        if(ascoltata)
            audio.start();
        else
            Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();


    }

    private class AudioRequest extends AsyncTask<String, Void, String> {
        int statusCode;
        String url;
        @Override
        protected String doInBackground(String... urls) {

            url=urls[0];
            // params comes from the execute() call: params[0] is the url.
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet(urls[0]));
                statusCode = response.getStatusLine().getStatusCode();

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
                audio= new MediaPlayer();
                try {
                    audio.setDataSource(url);
                    audio.prepare();
                    audio.start();
                    ascoltata=true;

                }
                catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
                    return;
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
                    return;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else
                Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();

        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }




}
