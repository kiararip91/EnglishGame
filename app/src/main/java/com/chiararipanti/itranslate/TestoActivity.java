package com.chiararipanti.itranslate;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class TestoActivity extends Activity {

    /**
     * Declaring Variables
     */
    String url;
    TextView it_tv;
    TextView eng_tv;
    Elements ing;
    Elements it;
    String testo_italiano;
    String testo_inglese;
    String autore;
    String titolo;
    ProgressDialog caricamento;
    AlertDialog.Builder alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testo);
        it_tv = findViewById(R.id.it_tv);
        eng_tv = findViewById(R.id.eng_tv);
        testo_italiano="";
        testo_inglese="";
        Intent intent=getIntent();
        autore=intent.getStringExtra("autore");
        titolo=intent.getStringExtra("titolo");
        url= "http://www.testitradotti.it/canzoni/"+autore+"/"+titolo;
        Log.v("url", url);
        caricamento = new ProgressDialog(TestoActivity.this);
        caricamento.setTitle(R.string.attendi);
        caricamento.setMessage(getString(R.string.caricamento));
        caricamento.show();
        ActionBar actionBar = getActionBar();

        if(actionBar == null){
            String LOG = "TestoActivity";
            Log.e(LOG, "ActionBar null");
        }else{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        alertDialog = new AlertDialog.Builder(
                TestoActivity.this);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onBackPressed();
                    }
                });
        //Titolo della finestra di dialogo
        alertDialog.setTitle(getString(R.string.testo_non_disp_title));
        //Messaggio mostrato
        alertDialog.setMessage(getString(R.string.testo_non_disp));


        //****************inserimento bunner pubblicitario***************************
        //create adView
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.unit_id));
        // Add the AdView to the view hierarchy.
        RelativeLayout layout = findViewById(R.id.footer);
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

        new GetTesto().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected class GetTesto extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Using Elements to get the class data
                ing = document.select("div[class=lyric]");
                Elements p=ing.select("p");

                for(Element elem : p){
                    testo_italiano = testo_italiano + elem.text().toString() + "\n";
                }

                it = document.select("div[class=translation]");
                Elements p2 = it.select("p");

                for(Element elem : p2){
                    testo_inglese = testo_inglese + elem.text().toString() + "\n";
                }

            } catch (IOException e) {
                e.printStackTrace();
                caricamento.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set downloaded image into ImageView
            if(testo_italiano.equals(""))
                alertDialog.show();
            else{
                it_tv.setText(testo_italiano);
                eng_tv.setText(testo_inglese);
                caricamento.dismiss();
            }
        }
    }
}
