package com.chiararipanti.itranslate;


import java.util.ArrayList;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import com.chiararipanti.itranslate.db.Canzone;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.GetCanzoniFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class MusicaActivity extends Activity {
    ArrayList<Canzone> canzoni;
    int prossimo;
    Canzone song;
    TextView titolo;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    MyConnectivityManager connectivityManager;
    EnglishGameUtility gameUtils;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);
        session = new SessionManager(getApplicationContext());
        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        connectivityManager=new MyConnectivityManager(getApplicationContext());

        titolo = findViewById(R.id.titolo);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        prossimo=0;

        getCanzoni();

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
            gameUtils.manageSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCanzoni(){
        //attraverso l'asynctask memorizzo dieci vocaboli della categoria scelta
        GetCanzoniFromDB getsongsTask=new GetCanzoniFromDB(this);
        try {
            getsongsTask.execute();
            canzoni=getsongsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        song=canzoni.get(prossimo);
        impostaCanzone();
    }

    @SuppressLint("SetTextI18n")
    public void impostaCanzone(){
        titolo.setText(song.getTitolo()+" - "+song.getAutore());
        ArrayList<String> alt=song.getAlternative();
        alt.add(song.getTraduzione());
        Collections.shuffle(alt);
        b1.setText(alt.get(0));
        b2.setText(alt.get(1));
        b3.setText(alt.get(2));
        b4.setText(alt.get(3));
    }

    public void next(View view){
        final Button b = (Button)view;
        String buttonText = b.getText().toString();
        if(buttonText.equals(song.getTraduzione())){
            b.setBackgroundColor(Color.GREEN);
            gameUtils.soundCorrect();
        }else {
            b.setBackgroundColor(Color.RED);
            gameUtils.soundWrong();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                b.setBackgroundResource(R.drawable.shape_button);
                prossimo++;
                if(prossimo<11)
                    song=canzoni.get(prossimo);
                else{
                    if(connectivityManager.check()){
                        GetCanzoniFromDB getCanzoni=new GetCanzoniFromDB(MusicaActivity.this);
                        getCanzoni.execute();
                        try {
                            canzoni=getCanzoni.get();
                            prossimo=0;
                            song=canzoni.get(prossimo);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
                    }
                }
                impostaCanzone();

            }
        }, 1000);
    }

    public void testo(View view){
        Intent e=new Intent(MusicaActivity.this, TestoActivity.class);
        e.putExtra("autore", song.getAutore().toLowerCase().replace(" ", "-"));
        e.putExtra("titolo", song.getTitolo().toLowerCase().replace(" ", "-").replace("?","").replace("'", ""));
        startActivity(e);
    }

}
