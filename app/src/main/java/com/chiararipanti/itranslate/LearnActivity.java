package com.chiararipanti.itranslate;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import com.chiararipanti.itranslate.model.Word;
import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.AudioRequest;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.db.GetWordsFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author chiararipanti
 * date 04/05/2013
 */
//FIXME: Va in eccezione, non ci sono parole recuperate
public class LearnActivity extends Activity {
    /**
     * Declaring variables
     */
    String category;
    ArrayList<Word> words;
    int next;
    Word word;
    MyConnectivityManager connectivityManager;
    AlertDialogManager alertDialog;
    TextView wordTranslationTextView;
    TextView wordEnglishTextView;
    TextView sentenceTextView;
    boolean showSolution;
    Button nextButton;

    /**
     * Checked
     */
    private MediaPlayer mediaPlayer;
    private boolean listened;
    private EnglishGameUtility gameUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        /*
          Checked
         */
        gameUtils = new EnglishGameUtility(this);
        gameUtils.addAdBunner();
        gameUtils.setHomeButtonEnabled();

        this.listened = false;
        /*
          Checked
         */

        category = getIntent().getStringExtra("category");
        String labelcategory = (Character.toUpperCase(category.charAt(0)) + category.substring(1)).replace("_", " ");
        next = 0;

        showSolution = false;

        connectivityManager = new MyConnectivityManager(getApplicationContext());
        alertDialog = new AlertDialogManager();

        nextButton = (findViewById(R.id.next));
        wordTranslationTextView = findViewById(R.id.native_word);
        wordTranslationTextView.setVisibility(View.GONE);
        wordEnglishTextView = findViewById(R.id.english_word);
        sentenceTextView = findViewById(R.id.sentence);

        TextView levelTextView = findViewById(R.id.level);
        levelTextView.setText(labelcategory);

        if(connectivityManager.check()){
            getWords();
            setWord();
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent startActivityIntent = new Intent(this,StartActivity.class);
            startActivity(startActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getWords(){
        //attraverso l'asinctask memorizzo dieci vocaboli della categoria scelta
        GetWordsFromDB getvocTask=new GetWordsFromDB(category);
        try {
            getvocTask.execute();
            words = getvocTask.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            alertDialog.showAlertDialog(LearnActivity.this, "OPS!", getString(R.string.errore), false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            alertDialog.showAlertDialog(LearnActivity.this, "OPS!",  getString(R.string.errore), false);
        }
        word = words.get(next);
    }

    public void setWord(){
        this.listened = false;
        wordTranslationTextView.setText(word.getNativeTranslation());
        wordEnglishTextView.setText(word.getEnglishWord());
        sentenceTextView.setText(word.getSentence());

        this.mediaPlayer = new MediaPlayer();
        AudioRequest ar = new AudioRequest(this, mediaPlayer);

        String english = word.getEnglishWord().toLowerCase();
        english = gameUtils.substituteSpecialCharWordToPronunce(english);

        String url="https://ssl.gstatic.com/dictionary/static/sounds/oxford/" + english + "--_gb_1.mp3";

        try {
            mediaPlayer.setDataSource(url);
            if(connectivityManager.check()){
                ar.execute(url);
                this.listened = true;

                new DownloadImageTask((ImageView) findViewById(R.id.immagine)).execute(word.getImage());
            }
            else{
                Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e("LearnActivity", "fail to load madiaPlayer");
        }
    }

    public void next(View view){
        if(connectivityManager.check()){
            if(!showSolution){
                showSolution = true;
                nextButton.setText(getString(R.string.next));
                wordTranslationTextView.setVisibility(View.VISIBLE);
            }
            else{
                showSolution = false; //inutile
                nextButton.setText(getString(R.string.soluzione));
                wordTranslationTextView.setVisibility(View.GONE);
                next++;
                
                if(next < 10)
                    word = words.get(next);
                else{
                    if(connectivityManager.check()){
                        GetWordsFromDB getVocaboli=new GetWordsFromDB(category);
                        getVocaboli.execute();
                        try {
                            words = getVocaboli.get();
                            next=0;
                            word = words.get(next);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            alertDialog.showAlertDialog(LearnActivity.this, "OPS!", getString(R.string.errore), false);
                        } catch (ExecutionException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            alertDialog.showAlertDialog(LearnActivity.this, "OPS!", getString(R.string.errore), false);
                        }

                    }
                    else{
                        alertDialog.showAlertDialog(LearnActivity.this, getString(R.string.attenzione), getString(R.string.attiva_connessione), true);

                    }

                }
                setWord();
            }

        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void listenToWord(View view)
    {
        if(this.listened) {
            mediaPlayer.start();
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
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
