package com.chiararipanti.itranslate;

import java.util.ArrayList;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.GetSentencesFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class SpeechActivity extends Activity implements TextToSpeech.OnInitListener {

    /**
     * Declaring Variables
     */
    private TextToSpeech tts;
    private ImageButton audio;
    private TextView level_tv;
    private TextView txtText;
    private TextView txtSpeechInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String sent;
    AlertDialog ad;
    ArrayList<String> sentences;
    int prossimo;
    int livello;
    AlertDialogManager alertDialog;
    MyConnectivityManager connectivityManager;
    AlertDialog.Builder alertChangeLevel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        ActionBar actionBar = getActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tts = new TextToSpeech(this, this);
        audio = findViewById(R.id.audio);
        level_tv = findViewById(R.id.level1);
        level_tv.setText(getString(R.string.livello) + ": " + getString(R.string.principiante));
        livello=1;
        txtText = findViewById(R.id.txtText);
        txtSpeechInput = findViewById(R.id.txtSpeechInput);
        ImageButton btnSpeak1 = findViewById(R.id.btnSpeak1);
        sentences= new ArrayList<>();
        alertDialog=new AlertDialogManager();
        prossimo=0;
        connectivityManager=new MyConnectivityManager(getApplicationContext());


        //****************inserimento bunner pubblicitario***************************
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

        btnSpeak1.setOnClickListener(new View.OnClickListener() {
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
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    sent=sent.replace(".", "");
                    sent=sent.replace(",", "");
                    sent=sent.replace("?", "");
                    String res=result.get(0);
                    res=res.replace(".", "");
                    res=res.replace(",", "");
                    res=res.replace("?", "");
                    if(res.equalsIgnoreCase(sent)){
                        Toast.makeText(getApplicationContext(),getString(R.string.esatta),Toast.LENGTH_SHORT).show();
                        prossimo();
                    }else
                        Toast.makeText(getApplicationContext(),getString(R.string.sbagliata),Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
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
            int result = tts.setLanguage(Locale.US);

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
            Intent intent=new Intent(this,StartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void speakOut() {
        String text = txtText.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void getSentences()
    {
        if(connectivityManager.check()){
            //attraverso l'asinctask memorizzo dieci vocaboli della categoria scelta
            GetSentencesFromDB getSentTask=new GetSentencesFromDB(this,livello);
            try {
                getSentTask.execute();
                sentences=getSentTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                alertDialog.showAlertDialog(SpeechActivity.this, "OPS!", getString(R.string.errore), false);
            } catch (ExecutionException e) {
                e.printStackTrace();
                alertDialog.showAlertDialog(SpeechActivity.this, "OPS!", getString(R.string.errore), false);
            }

            sent=sentences.get(prossimo);
            txtText.setText(sent);
            speakOut();
        } else
            alertDialog.showAlertDialog(SpeechActivity.this, getString(R.string.attenzione), getString(R.string.attiva_connessione), false);
    }

    public void next(View view) {
        prossimo();
    }

    public void changeLevel(View view) {
        alertChangeLevel = new AlertDialog.Builder(SpeechActivity.this);
        alertChangeLevel.setTitle(getString(R.string.livello));
        alertChangeLevel.setItems(R.array.level_array, new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(DialogInterface dialog, int which) {
                livello=which+1;
                if(livello==1)
                    level_tv.setText(getString(R.string.livello)+": "+getString(R.string.principiante));
                else if(livello==2)
                    level_tv.setText(getString(R.string.livello)+": "+getString(R.string.intermedio));
                else if(livello==3)
                    level_tv.setText(getString(R.string.livello)+": "+getString(R.string.esperto));
                getSentences();
                ad.dismiss();
            }
        });

        ad = alertChangeLevel.show();
    }

    public void prossimo(){
        prossimo++;

        if(prossimo<20)
            sent=sentences.get(prossimo);
        else {
            if(connectivityManager.check()){
                GetSentencesFromDB getSentTask=new GetSentencesFromDB(this,livello);
                getSentTask.execute();
                try {
                    sentences=getSentTask.get();
                    prossimo=0;
                    sent=sentences.get(prossimo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    alertDialog.showAlertDialog(SpeechActivity.this, "OPS!", getString(R.string.errore), false);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    alertDialog.showAlertDialog(SpeechActivity.this, "OPS!", getString(R.string.errore), false);
                }

            } else {
                alertDialog.showAlertDialog(SpeechActivity.this, getString(R.string.attenzione), getString(R.string.attiva_connessione), true);
            }
        }
        txtText.setText(sent);
        speakOut();
    }
}
