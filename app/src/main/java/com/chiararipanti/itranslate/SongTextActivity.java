package com.chiararipanti.itranslate;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chiararipanti.itranslate.util.EnglishGameUtility;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
//TODO: Controlla utilita variabili, nomi metodi asynctTask static, stringa parmaterizzata
public class SongTextActivity extends Activity {

    /**
     * Declaring Variables
     */
    String url;
    TextView italianTextTexView;
    TextView englishTextTextView;
    String italianTranslation; //FIXME: to remove
    String originalText; //FIXME: to remove
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;
    EnglishGameUtility gameUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_text);

        /*
         * Set up layout
         */
        italianTextTexView = findViewById(R.id.italian_text);
        englishTextTextView = findViewById(R.id.english_text);

        /*
         * Set up activity
         */
        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        /*
         * Initialize variables
         */
        italianTranslation = "";
        originalText = "";
        Intent intent = getIntent();
        String songAuthor = intent.getStringExtra("autore");
        String title = intent.getStringExtra("titolo");
        url = "http://www.testitradotti.it/canzoni/" + songAuthor + "/" + title;
        progressDialog = new ProgressDialog(SongTextActivity.this);
        progressDialog.setTitle(R.string.attendi);
        progressDialog.setMessage(getString(R.string.caricamento));
        progressDialog.show();

        alertDialog = new AlertDialog.Builder(
                SongTextActivity.this);
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

        new GetSongText().execute();

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

    protected class GetSongText extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Elements englishElement;
                Elements italianElement;
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Using Elements to get the class data
                englishElement = document.select("div[class=lyric]");
                Elements paragraph = englishElement.select("p");

                for (Element elem : paragraph) {
                    italianTranslation = new StringBuilder(italianTranslation).append(elem.text()).append("\n").toString();
                }

                italianElement = document.select("div[class=translation]");
                Elements paragraph2 = italianElement.select("p");

                for (Element elem : paragraph2) {
                    originalText = new StringBuilder().append(originalText).append(elem.text()).append("\n").toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
            //TODO: ritrona la traduzione dall asyncttask, cosi non devo dichiarare la variabile fuori
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set downloaded image into ImageView
            if (italianTranslation.equals(""))
                alertDialog.show();
            else {
                italianTextTexView.setText(italianTranslation);
                englishTextTextView.setText(originalText);
                progressDialog.dismiss();
            }
        }
    }
}
