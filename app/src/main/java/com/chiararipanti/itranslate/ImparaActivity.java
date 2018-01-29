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
import com.chiararipanti.itranslate.util.AudioRequest;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class ImparaActivity extends Activity {
    /**
     * Declaring variables
     */
    String categoria;
    ArrayList<Vocabolo> vocaboli;
    int prossimo;
    Vocabolo voc;
    MyConnectivityManager connectivityManager;
    AlertDialogManager alertDialog;
    ImageButton ascolta;
    Button next;
    TextView parola_italiano_tv;
    TextView parola_inglese_tv;
    TextView frase_tv;
    TextView livello;
    boolean sol;

    Context mcontext;

    /**
     * Checked
     */
    private MediaPlayer mediaPlayer;
    private boolean listened;
    private EnglishGameUtility gameUtils;


    String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impara);

        /**
         * Checked
         */

        this.gameUtils = new EnglishGameUtility(this);
        this.gameUtils.addAdBunner();
        this.gameUtils.setHomeButtonEnabled();
        this.listened = false;

        /**
         * Checked
         */

        Intent intent=getIntent();
        categoria=intent.getStringExtra("categoria");
        prossimo=0;

        sol=false;

        connectivityManager=new MyConnectivityManager(getApplicationContext());
        alertDialog=new AlertDialogManager();
        mcontext=getApplicationContext();

        ascolta = (findViewById(R.id.audio));
        next = (findViewById(R.id.next));
        parola_italiano_tv = findViewById(R.id.parola_italiano);
        parola_italiano_tv.setVisibility(View.GONE);
        parola_inglese_tv = findViewById(R.id.parola_inglese);
        frase_tv = findViewById(R.id.frase);
        livello = findViewById(R.id.level);
        livello.setText(intent.getStringExtra("categoria1"));

        if(connectivityManager.check()){
            getVocaboli();
            impostaParola();
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent=new Intent(this,StartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getVocaboli(){
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

    public void impostaParola(){
        this.listened = false;
        parola_italiano_tv.setText(voc.getLingua_nativa());
        parola_inglese_tv.setText(voc.getInglese());
        parola_italiano_tv.setText(voc.getLingua_nativa());
        frase_tv.setText(voc.getFrase());

        this.mediaPlayer = new MediaPlayer();
        AudioRequest ar=new AudioRequest(this, mediaPlayer);

        String ingl=voc.getInglese().toLowerCase();
        ingl=ingl.replaceAll("to ","");
        ingl=ingl.replaceAll("\\s","_");
        ingl=ingl.replaceAll("_\\[sb\\]","");
        ingl=ingl.replaceAll("_\\[sth\\]","");
        ingl=ingl.replaceAll("_\\[smb\\]","");
        ingl=ingl.replaceAll("\\[sb\\]","");
        ingl=ingl.replaceAll("\\[sth\\]","");
        ingl=ingl.replaceAll("\\[smb\\]","");

        String url="https://ssl.gstatic.com/dictionary/static/sounds/oxford/"+ingl+"--_gb_1.mp3";

        try {
            mediaPlayer.setDataSource(url);
            if(connectivityManager.check()){
                ar.execute(url);
                this.listened = true;

                new DownloadImageTask((ImageView) findViewById(R.id.immagine)).execute(voc.getImg());
            }
            else{
                Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e("IMPARAACTIVITY", "fail to load madiaPlayer");
        }

    }


    public void next(View view){
        if(connectivityManager.check()){
            if(!sol){
                sol=true;
                next.setText(getString(R.string.next));
                parola_italiano_tv.setVisibility(View.VISIBLE);

            }
            else{
                sol=false;
                next.setText(getString(R.string.soluzione));
                parola_italiano_tv.setVisibility(View.GONE);
                prossimo++;
                if(prossimo<10)
                    voc=vocaboli.get(prossimo);
                else{
                    if(connectivityManager.check()){
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
                    else{
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
        if(this.listened) {
            mediaPlayer.start();
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();

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
