package com.chiararipanti.itranslate;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;
import com.chiararipanti.itranslate.db.Vocabolo;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.GetVocaboliFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class TraduzioneActivity extends Activity {

    /**
     * Declaring Variables
     */
    EnglishGameUtility gameUtils;
    String categoria;
    ArrayList<Vocabolo> vocaboli;
    int prossimo;
    Vocabolo voc;
    EditText parola_ing;
    TextView parola_it;
    TextView livello;
    TextView help;
    InputMethodManager imm;
    MyConnectivityManager connectivityManager;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traduzione);
        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        session = new SessionManager(getApplicationContext());
        imm = (InputMethodManager)getSystemService(this.getApplicationContext().INPUT_METHOD_SERVICE);
        parola_it = findViewById(R.id.parola_italiano);
        parola_ing = findViewById(R.id.traduzione);
        connectivityManager=new MyConnectivityManager(getApplicationContext());
        Intent intent=getIntent();
        categoria=intent.getStringExtra("categoria");
        prossimo=0;
        help = findViewById(R.id.help);
        livello = findViewById(R.id.level);
        livello.setText(intent.getStringExtra("categoria1"));

        if(connectivityManager.check()){
            getVocaboli();
            impostaParola();
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void getVocaboli(){
        //attraverso l'asinctask memorizzo dieci vocaboli della categoria scelta
        GetVocaboliFromDB getvocTask=new GetVocaboliFromDB(this,categoria);
        try {
            getvocTask.execute();
            vocaboli=getvocTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        voc=vocaboli.get(prossimo);
    }

    public void impostaParola(){
        parola_it.setText(voc.getLingua_nativa());
        parola_ing.setText("");
        help.setVisibility(View.GONE);
        View view = this.getCurrentFocus();
        if(imm == null){
            String TAG = "TraduzioneActivity";
            Log.e(TAG, "imm null");
        }else if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent=new Intent(this,StartActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(100, 10, 100, 10);
            ll.setLayoutParams(layoutParams);
            Switch sbSound;
            sbSound = new Switch(this);
            sbSound.setText(R.string.sound);

            if(session.getSuono())
                sbSound.setChecked(true);

            sbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        session.setSuono(true);
                    } else {
                        session.setSuono(false);
                    }
                }
            });

            ll.addView(sbSound,layoutParams);
            AlertDialog.Builder builder;
            AlertDialog alertDialog;
            builder = new AlertDialog.Builder(this);
            builder.setView(ll);
            builder.setTitle("Edit settings");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog = builder.create();

            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void conferma(View view){

        if(parola_ing.getText().toString().equalsIgnoreCase(voc.getInglese())){
            prossimo++;
            Toast.makeText(getApplicationContext(),getString(R.string.esatta) , Toast.LENGTH_SHORT).show();
            gameUtils.soundCorrect();

            if(prossimo<20)
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
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
                }

            }

            impostaParola();

        }else{
            gameUtils.soundWrong();

            Toast.makeText(getApplicationContext(),getString(R.string.sbagliata) , Toast.LENGTH_SHORT).show();
        }
    }

    public void showSolution(View view){
        parola_ing.setText(voc.getInglese());
    }

    public void showAiuto(View view){
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        help.setVisibility(View.VISIBLE);
        int lunghezza=voc.getInglese().length();
        String aiuto=voc.getInglese().substring(0, 2)+" ";
        for(int i=2; i<lunghezza; i++){
            if(Character.isWhitespace(voc.getInglese().charAt(i)))
                aiuto = aiuto.concat(" ");
            else
                aiuto = aiuto.concat("_ ");
        }

        help.setText(aiuto);
        Toast.makeText(getApplicationContext(),getString(R.string.aiuto) , Toast.LENGTH_SHORT).show();
    }
}
