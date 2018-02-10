package com.chiararipanti.itranslate;

import java.util.ArrayList;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.chiararipanti.itranslate.db.GetSpeakFromDB;
import com.chiararipanti.itranslate.model.Speak;
import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.MyConnectivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */

//TODO: Controlla variabili, metodi e logica, concatenazione stringhe risorsa //Gestione dei livelli?

public class SpeechActivity extends Activity implements TextToSpeech.OnInitListener {

    /**
     * Declaring Variables
     */
    private TextToSpeech textToPronounce;
    private ImageButton audio;
    private TextView levelTextView;
    private TextView txtText;
    private TextView txtSpeechInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Speak speak;
    AlertDialog aletDialog;
    ArrayList<Speak> speaks;
    int next;
    int level;
    AlertDialogManager alertDialog;
    MyConnectivityManager connectivityManager;
    AlertDialog.Builder alertChangeLevel;
    EnglishGameUtility gameUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        textToPronounce = new TextToSpeech(this, this);
        audio = findViewById(R.id.audio);
        levelTextView = findViewById(R.id.level);
        levelTextView.setText(getString(R.string.livello) + ": " + getString(R.string.principiante));
        level = 1;
        txtText = findViewById(R.id.txtText);
        txtSpeechInput = findViewById(R.id.txtSpeechInput);
        ImageButton speakButton = findViewById(R.id.speak_button);
        speaks = new ArrayList<>();
        alertDialog = new AlertDialogManager();
        next = 0;
        connectivityManager = new MyConnectivityManager(getApplicationContext());

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        // button on click event
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }

        });

        getSentences();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));

                    String sentenceToPronounce = speak.getSentence().replace(".", "").replace(",", "").replace("?", "");
                    String res = result.get(0);
                    res = res.replace(".", "");
                    res = res.replace(",", "");
                    res = res.replace("?", "");
                    if (res.equalsIgnoreCase(sentenceToPronounce)) {
                        Toast.makeText(getApplicationContext(), getString(R.string.esatta), Toast.LENGTH_SHORT).show();
                        next();
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.sbagliata), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        if (textToPronounce != null) {
            textToPronounce.stop();
            textToPronounce.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToPronounce.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            } else {
                audio.setEnabled(true);
            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent startActivityIntent = new Intent(this, StartActivity.class);
            startActivity(startActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void speakOut() {
        String text = txtText.getText().toString();
        textToPronounce.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void getSentences() {
        if (connectivityManager.check()) {
            //attraverso l'asinctask memorizzo dieci vocaboli della categoria scelta
            GetSpeakFromDB getSentTask = new GetSpeakFromDB(this, level);
            try {
                getSentTask.execute();
                speaks = getSentTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                alertDialog.showAlertDialog(getApplicationContext(), "OPS!", getString(R.string.errore), false);
            } catch (ExecutionException e) {
                e.printStackTrace();
                alertDialog.showAlertDialog(getApplicationContext(), "OPS!", getString(R.string.errore), false);
            }

            speak = speaks.get(next);
            txtText.setText(speak.getSentence());
            speakOut();
        } else
            alertDialog.showAlertDialog(SpeechActivity.this, getString(R.string.attenzione), getString(R.string.attiva_connessione), false);
    }

    public void next(View view) {
        next();
    }

    public void changeLevel(View view) {
        alertChangeLevel = new AlertDialog.Builder(getApplicationContext());
        alertChangeLevel.setTitle(getString(R.string.livello));
        alertChangeLevel.setItems(R.array.level_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                level = which + 1;
                if (level == 1)
                    levelTextView.setText(getString(R.string.livello) + ": " + getString(R.string.principiante));
                else if (level == 2)
                    levelTextView.setText(getString(R.string.livello) + ": " + getString(R.string.intermedio));
                else if (level == 3)
                    levelTextView.setText(getString(R.string.livello) + ": " + getString(R.string.esperto));
                getSentences();
                aletDialog.dismiss();
            }
        });

        aletDialog = alertChangeLevel.show();
    }

    public void next() {
        next++;

        if (next < 20)
            speak = speaks.get(next);
        else {
            if (connectivityManager.check()) {
                GetSpeakFromDB getSpeaktTask = new GetSpeakFromDB(this, level);
                getSpeaktTask.execute();
                try {
                    speaks = getSpeaktTask.get();
                    next = 0;
                    speak = speaks.get(next);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    alertDialog.showAlertDialog(getApplicationContext(), "OPS!", getString(R.string.errore), false);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    alertDialog.showAlertDialog(getApplicationContext(), "OPS!", getString(R.string.errore), false);
                }

            } else {
                alertDialog.showAlertDialog(getApplicationContext(), getString(R.string.attenzione), getString(R.string.attiva_connessione), true);
            }
        }
        txtText.setText(speak.getSentence());
        speakOut();
    }
}
