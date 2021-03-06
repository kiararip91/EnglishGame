package com.chiararipanti.itranslate;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;

import com.chiararipanti.itranslate.model.Word;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.db.GetWordsFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
//TODO: Controlla utilitaq variabili logica e layout

public class TranslationActivity extends Activity {

    /**
     * Declaring Variables
     */
    EnglishGameUtility gameUtils;
    String category;
    int level;
    ArrayList<Word> words;
    int next;
    Word word;
    EditText englishWordTextView;
    TextView italianWordTextView;
    TextView levelTextView;
    TextView helpTextView;
    InputMethodManager imm;
    MyConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        imm = (InputMethodManager) getSystemService(this.getApplicationContext().INPUT_METHOD_SERVICE);
        italianWordTextView = findViewById(R.id.native_text);
        englishWordTextView = findViewById(R.id.english_translation);
        connectivityManager = new MyConnectivityManager(getApplicationContext());
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        level = Integer.parseInt(intent.getStringExtra("level"));
        next = 0;
        helpTextView = findViewById(R.id.help);
        levelTextView = findViewById(R.id.level);
        String labelcategory = (Character.toUpperCase(category.charAt(0)) + category.substring(1)).replace("_", " ");
        levelTextView.setText(labelcategory);

        if (connectivityManager.check()) {
            this.getWords();
            this.setupWord();
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
    }

    public void getWords() {
        GetWordsFromDB getWordsTask = new GetWordsFromDB(level);
        try {
            getWordsTask.execute();
            words = getWordsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        word = words.get(next);
    }

    public void setupWord() {
        String nativeTranslation = word.getItalian();
        italianWordTextView.setText(nativeTranslation);
        englishWordTextView.setText("");
        helpTextView.setVisibility(View.GONE);
        View view = this.getCurrentFocus();
        if (imm == null) {
            String TAG = "TranslationActivity";
            Log.e(TAG, "imm null");
        } else if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            gameUtils.manageSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void conferma(View view) {

        if (englishWordTextView.getText().toString().equalsIgnoreCase(word.getEnglish())) {
            next++;
            Toast.makeText(getApplicationContext(), getString(R.string.esatta), Toast.LENGTH_SHORT).show();
            gameUtils.soundCorrect();

            if (next < 20)
                word = words.get(next);
            else {

                if (connectivityManager.check()) {
                    GetWordsFromDB getVocaboli = new GetWordsFromDB(level);
                    getVocaboli.execute();

                    try {
                        words = getVocaboli.get();
                        next = 0;
                        word = words.get(next);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
                }

            }

            setupWord();

        } else {
            gameUtils.soundWrong();

            Toast.makeText(getApplicationContext(), getString(R.string.sbagliata), Toast.LENGTH_SHORT).show();
        }
    }

    public void showSolution(View view) {
        englishWordTextView.setText(word.getEnglish());
    }

    public void showAiuto(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        helpTextView.setVisibility(View.VISIBLE);
        int lenght = word.getEnglish().length();
        String help = word.getEnglish().substring(0, 2) + " ";
        for (int i = 2; i < lenght; i++) {
            if (Character.isWhitespace(word.getEnglish().charAt(i)))
                help = help.concat(" ");
            else
                help = help.concat("_ ");
        }

        helpTextView.setText(help);
        Toast.makeText(getApplicationContext(), getString(R.string.aiuto), Toast.LENGTH_SHORT).show();
    }
}
