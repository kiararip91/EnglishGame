package com.chiararipanti.itranslate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chiararipanti.itranslate.db.Vocabolo;
import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.GetVocaboliFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class MainActivity extends Activity {
    Boolean suono;
    Boolean vibra;
    LinearLayout ll;
    LinearLayout ll1;
    LinearLayout ll2;
    LinearLayout ll_black;
    LinearLayout ll_black2;
    LinearLayout linear_right;
    LinearLayout linear_gameover;
    LinearLayout linear_content;
    TextView parola_inglese;
    TextView punteggio;
    TextView traduci;
    TextView errori;
    TextView record_tv;
    TextView livello_tv;
    TextView frase_tv;
    ImageButton aiuto;
    ImageButton soluzione;
    Button fine;
    ImageButton ascaudio;
    ImageButton fraseb;
    Character letteraPrecedente;
    HashMap<Integer,Button> lb;
    Switch mySwitch;
    String premutiLog="";
    float record;
    float punti;
    int err;
    Display display;
    int rapporto;
    boolean seconda_riga;
    int numeroAiuti;
    int numeroSoluzioni;

    //array nel quale memorizzo gli identificatori delle lettere premuti
    ArrayList<Integer> premuti;
    Vocabolo voc;
    SessionManager session;
    ArrayList<String> parola_selezionata;
    Vibrator vibrator;
    ArrayList<Vocabolo> vocaboli;
    int prossimo;
    String categoria;
    AlertDialogManager alertDialog;
    AlertDialogManager settingDialog;
    MyConnectivityManager connectivityManager;
    ArrayList<Button> bb;
    MediaPlayer wrongSound;
    MediaPlayer correctSound;
    MediaPlayer overSound;
    MediaPlayer ascolta;
    boolean ascoltata;
    String lingua;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);
        correctSound = MediaPlayer.create(this, R.raw.correct);
        overSound = MediaPlayer.create(this, R.raw.over);
        ascoltata=false;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        prossimo = 0;
        alertDialog = new AlertDialogManager();
        settingDialog = new AlertDialogManager();
        Intent intent = getIntent();
        categoria=intent.getStringExtra("categoria");
        mySwitch = findViewById(R.id.switchForActionBar);
        connectivityManager = new MyConnectivityManager(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        suono=session.getSuono();
        vibra=session.getVibrazione();
        ActionBar actionBar = getActionBar();

        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        display= getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        rapporto = size.x /9;
        seconda_riga = false;
        lingua = Locale.getDefault().getLanguage() ;
        numeroAiuti = 0;
        numeroSoluzioni = 0;

        //****************inserimento bunner pubblicitario***************************
        //create adView
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.unit_id));
        // Add the AdView to the view hierarchy.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.footer);
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        //******************  FINE  bunner pubblicitario***************************

        lb=new HashMap<>();
        record=session.getRecord(categoria);
        ll = findViewById(R.id.linearLayout1);
        ll1 = findViewById(R.id.linearletter);
        ll2 = findViewById(R.id.linearletter2);
        ll_black = findViewById(R.id.black);
        ll_black2 = findViewById(R.id.black2);
        linear_right = findViewById(R.id.linearRight);
        linear_right.setVisibility(View.GONE);
        linear_gameover = findViewById(R.id.linearGameover);
        linear_gameover.setVisibility(View.GONE);
        linear_content = findViewById(R.id.contentLayout);
        livello_tv = findViewById(R.id.level);
        frase_tv = findViewById(R.id.frasetext);
        parola_inglese = findViewById(R.id.parola_inglese);
        punteggio = findViewById(R.id.punteggio);
        traduci = findViewById(R.id.traduci);
        errori = findViewById(R.id.errori);
        record_tv = findViewById(R.id.record);
        record_tv.setText(getString(R.string.tuo_record1)+" "+record);
        aiuto = findViewById(R.id.aiuto);
        soluzione = findViewById(R.id.soluzione);
        ascaudio = findViewById(R.id.audio);
        fine = findViewById(R.id.fine);
        fraseb = findViewById(R.id.frase);
        punti=0;
        err=0;
        premuti=new ArrayList<>();
        livello_tv.setText(getString(R.string.livello)+": "+intent.getStringExtra("categoria1"));
        frase_tv.setText("");
        if(connectivityManager.check())
            getVocaboli();
        else
        {
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
            Intent intent1=new Intent(this,StartActivity.class);
            startActivity(intent1);
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
            sbSound.setTextOn(getString(R.string.sound_on));
            sbSound.setTextOff(getString(R.string.sound_off));

            if(suono)
                sbSound.setChecked(true);

            sbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        session.setSuono(true);
                        suono=true;
                    } else {
                        session.setSuono(false);
                        suono=false;
                    }
                }
            });

            Switch sbVibra;
            sbVibra = new Switch(this);
            sbVibra.setTextOn(getString(R.string.vibration_on));
            sbVibra.setTextOff(getString(R.string.vibration_off));

            if(vibra)
                sbVibra.setChecked(true);

            sbVibra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        session.setVibrazione(true);
                        vibra=true;
                    } else {
                        session.setVibrazione(false);
                        vibra=false;
                    }
                }
            });

            ll.addView(sbVibra,layoutParams);
            ll.addView(sbSound,layoutParams);
            AlertDialog.Builder builder;
            AlertDialog alertDialog;
            builder = new AlertDialog.Builder(this);
            builder.setView(ll);
            builder.setTitle("Edit settings");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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


    public void onBackPressed() {
        //finish();
    }

    public void getVocaboli()
    {
        //attraverso l'asinctask memorizzo dieci vocaboli della categoria scelta
        GetVocaboliFromDB getvocTask=new GetVocaboliFromDB(this,categoria);
        try {
            getvocTask.execute();
            vocaboli=getvocTask.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            alertDialog.showAlertDialog(MainActivity.this, "OPS!", getString(R.string.errore), false);
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            alertDialog.showAlertDialog(MainActivity.this, "OPS!", getString(R.string.errore), false);
        }
        voc=vocaboli.get(prossimo);
        setLettere(voc);
    }



    public void setLettere(Vocabolo voc)
    {
        //ripristino le visibilta dei layout
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.VISIBLE);
        punteggio.setText(getString(R.string.punteggio)+punti);
        errori.setText(getString(R.string.errori)+err);
        ll1.removeAllViews();
        ll2.removeAllViews();
        ll_black.removeAllViews();
        ll_black2.removeAllViews();

        //array di botton inizialmente neri
        bb=new ArrayList<Button>();

        //la parola che si sta formando in italiano clicckando sulle lettere
        parola_selezionata=new ArrayList<String>();

        //lettere di cui e composta la parola in italiano
        String[] prima=voc.getLingua_nativa().split(",");

        //prendo la prima traduzione, ignoro i sinonimi
        final String italiano=prima[0];

        //estraggo le lettere dalla parola
        String[] arr=italiano.toLowerCase().split("(?!^)");
        int nlettere=arr.length;

        if(nlettere>8)
            seconda_riga=true;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, rapporto);
        lp.weight=1;
        lp.leftMargin=5;
        lp.rightMargin=5;

        //genero i pulsanti con le lettere della parola
        for(int i=0;i<nlettere; i++){
            final int indice=i;

            if(!arr[i].equalsIgnoreCase(" ")){
                final Button img=new Button(getApplicationContext());
                img.setId(indice);
                img.setLayoutParams(lp);
                final String carattere=arr[i];
                String sfondo_lettera=sostituisciSpecialChar(arr[i].charAt(0));
                int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                img.setBackground(getResources().getDrawable(j));

                //click lettera parola
                img.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int numero_lettera=parola_selezionata.size();

                        //parola non ancora terminata
                        if(numero_lettera<italiano.length()-1){
                            Character car_italiano=italiano.charAt(parola_selezionata.size());
                            //la lettera corrispondente non e' uno spazio
                            if(!(String.valueOf(car_italiano).equalsIgnoreCase(" "))){
                                parola_selezionata.add(carattere);
                                premuti.add(indice);
                                //estraggo il bottone nero successivo e gli cambio lo sfondo in base alla lettera selezionata
                                Button black_button=bb.get(numero_lettera);
                                String sfondo_lettera=sostituisciSpecialChar(carattere.charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                black_button.setBackground(getResources().getDrawable(w));
                                img.setBackground(getResources().getDrawable(R.drawable.black));
                            }

                            //la lettera nella posizione corrente e' uno spazio,
                            //devo saltare un bottone e cambiare lo sfondo del successivo
                            else{
                                parola_selezionata.add(" ");
                                parola_selezionata.add(carattere);
                                premuti.add(100);
                                premuti.add(indice);
                                Button black_button=bb.get(numero_lettera+1);
                                String sfondo_lettera=sostituisciSpecialChar(carattere.charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                black_button.setBackground(getResources().getDrawable(w));
                                img.setBackground(getResources().getDrawable(R.drawable.black));
                            }

                        }

                        //ho selezionato l'ultima lettera
                        else if(numero_lettera==italiano.length()-1){
                            //aggiungo il carattere selezionato alla parola che sto formando
                            parola_selezionata.add(carattere);
                            Log.v("onclick",carattere+" "+indice);
                            premuti.add(indice);
                            //cambio lo sfondo dell'ultimo bottone
                            Button black_button=bb.get(numero_lettera);
                            String sfondo_lettera=sostituisciSpecialChar(carattere.charAt(0));
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            black_button.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));

                            //convalido la stringa formata
                            String da_convalidare="";

                            for(int i=0; i<parola_selezionata.size(); i++){
                                da_convalidare=da_convalidare+(parola_selezionata.get(i));
                            }
                            if(italiano.equalsIgnoreCase(da_convalidare)){
                                black_button.setClickable(false);
                                linear_right.setVisibility(View.VISIBLE);
                                if(suono)
                                    correctSound.start();
                                if(vibra)
                                    vibrator.vibrate(500);
                                punti=punti+1;
                                punteggio.setText(getString(R.string.punteggio)+punti);
                                ll1.setVisibility(View.GONE);
                                ll2.setVisibility(View.GONE);
                                frase_tv.setText("");

                                premutiLog="";
                            }
                            else{
                                err=err+1;

                                //GAME OVER
                                if(err>2){
                                    if(suono){
                                        overSound.start();
                                    }

                                    numeroAiuti=0;
                                    numeroSoluzioni=0;

                                    if(vibra)
                                        vibrator.vibrate(500);

                                    session.incrPartite(categoria);
                                    linear_content.setVisibility(View.GONE);
                                    aiuto.setClickable(false);
                                    soluzione.setClickable(false);
                                    linear_gameover.setVisibility(View.VISIBLE);
                                    if(nuovoRecord(punti))
                                        alertDialog.showAlertDialog(MainActivity.this, getString(R.string.nuovo_record_title),getString(R.string.nuovo_record)+punti,false);

                                }
                                else
                                    errori.setText(getString(R.string.errori)+err);

                                if(suono)
                                    wrongSound.start();
                                if(vibra)
                                    vibrator.vibrate(500);
                            }

                            img.setClickable(false);
                            for(int i=0;i<premuti.size(); i++)
                            {
                                premutiLog+=", "+premuti.get(i);
                            }


                            //Log.v("premutilog",premutiLog);
                            premutiLog="";

                        }
                        //parola completata, ma continuo a premere lettere
                        else if(numero_lettera==italiano.length())
                        {
                            //non fare niente
                        }


                    }

                });

                lb.put(indice,img);

                //genero pulsante nero
                final Button black=new Button(getApplicationContext());
                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(0, rapporto);

                lpb.weight=1;
                lpb.leftMargin=5;
                lpb.rightMargin=5;
                black.setLayoutParams(lpb);
                black.setBackground(getResources().getDrawable(R.drawable.black));

                //declick lettera
                black.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //se ho selezionato l ultima lettera inserita
                        if(indice==parola_selezionata.size()-1){
                            if(parola_selezionata.size()>1)
                                letteraPrecedente=italiano.charAt(parola_selezionata.size()-2);
                            else
                                letteraPrecedente=italiano.charAt(parola_selezionata.size()-1);

                            //il carattere precedente alla precendente non e' uno spazio
                            if(!String.valueOf(letteraPrecedente).equalsIgnoreCase(" ")){
                                black.setBackground(getResources().getDrawable(R.drawable.black));
                                int ripristina=premuti.get(indice);

                                for (int i=0;i<premuti.size(); i++){
                                    premutiLog+=", "+premuti.get(i);
                                }
                                premutiLog="";
                                premuti.remove(indice);

                                for (int i=0;i<premuti.size(); i++){
                                    premutiLog+=", "+premuti.get(i);
                                }
                                premutiLog="";
                                Button b=lb.get(ripristina);
                                String sfondo_lettera=sostituisciSpecialChar(parola_selezionata.get(indice).toString().charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                b.setBackground(getResources().getDrawable(w));
                                b.setClickable(true);
                                parola_selezionata.remove(indice);

                            }else{
                                black.setBackground(getResources().getDrawable(R.drawable.black));

                                int ripristina = premuti.get(indice);

                                for (int i=0;i<premuti.size(); i++){
                                    premutiLog+=", "+premuti.get(i);
                                }

                                premutiLog="";
                                premuti.remove(indice);
                                premuti.remove(premuti.indexOf(100));

                                for (int i=0;i<premuti.size(); i++){
                                    premutiLog+=", "+premuti.get(i);
                                }

                                premutiLog="";
                                Button b=lb.get(ripristina);
                                b.setClickable(true);
                                String sfondo_lettera = sostituisciSpecialChar(parola_selezionata.get(indice).charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                b.setBackground(getResources().getDrawable(w));
                                parola_selezionata.remove(indice);
                                parola_selezionata.remove(" ");
                            }
                            //altrimenti non faccio nulla
                        }
                    }
                });
                bb.add(black);

                if(i<8){
                    ll_black.addView(black);
                }
                else
                    ll_black2.addView(black);
            }
            //spazio
            else{
                Button black=new Button(getApplicationContext());
                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(0, rapporto);
                lpb.weight=1;
                lpb.leftMargin=5;
                lpb.rightMargin=5;
                black.setLayoutParams(lpb);
                black.setBackgroundColor(Color.TRANSPARENT);
                black.setClickable(false);
                bb.add(black);
                if(i<8)
                    ll_black.addView(black);
                else
                    ll_black2.addView(black);

            }

        }

        String randomLetters = getString(R.string.alfabeto);
        Random rnd = new Random();

        //inserisco altre lettere random in tot ne avro 16
        //inserisco anche finti bottoni bianchi affinaco ai neri per avere la giusta dimensione relativa delle caselle
        for(int i=nlettere;i<16;i++){
            final Button fasullo=new Button(getApplicationContext());
            fasullo.setLayoutParams(lp);
            fasullo.setBackgroundColor(Color.TRANSPARENT);
            fasullo.setClickable(false);

            if(i<8){
                ll_black.addView(fasullo);
            }
            else{
                if(seconda_riga)
                    ll_black2.addView(fasullo);

            }

            final int indicer=i;
            final Button img=new Button(getApplicationContext());
            img.setLayoutParams(lp);
            int randomGen=rnd.nextInt(randomLetters.length());
            final Character randomChar=randomLetters.charAt(randomGen);

            //assegno il numero random genrerato come id del bottone per poter risalire al carattere corrispondente
            img.setId(randomGen);

            //click lettera random
            img.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                public void onClick(View v) {
                    int numero_lettera=parola_selezionata.size();

                    //non sto inserendo l'ultima lettera
                    if(numero_lettera<italiano.length()-1){
                        Character car_italiano=italiano.charAt(parola_selezionata.size());

                        //la lettera corrispondente non e' uno spazio ma una casella nera
                        if(!(String.valueOf(car_italiano).equalsIgnoreCase(" "))){
                            premuti.add(indicer);
                            parola_selezionata.add(randomChar.toString());
                            Button black_button=bb.get(numero_lettera);
                            String sfondo_lettera=sostituisciSpecialChar(randomChar);
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            black_button.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));
                        }
                        //la lettera che devo inserire va dopo lo spazio
                        else{
                            parola_selezionata.add(" ");
                            parola_selezionata.add(randomChar.toString());
                            premuti.add(100);
                            premuti.add(indicer);
                            Button black_button=bb.get(numero_lettera+1);
                            String sfondo_lettera=sostituisciSpecialChar(randomChar);
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            black_button.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));
                        }

                    }
                    //sto inserendo l'ultima lettera
                    else if(numero_lettera==italiano.length()-1){
                        premuti.add(indicer);
                        parola_selezionata.add(randomChar.toString());
                        Button black_button=bb.get(numero_lettera);
                        String sfondo_lettera=sostituisciSpecialChar(randomChar);
                        int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                        black_button.setBackground(getResources().getDrawable(w));
                        img.setBackground(getResources().getDrawable(R.drawable.black));
                        String da_convalidare="";

                        for(int i=0; i<parola_selezionata.size(); i++)
                            da_convalidare=da_convalidare + (parola_selezionata.get(i));

                        if(italiano.equalsIgnoreCase(da_convalidare)){
                            black_button.setClickable(false);
                            linear_right.setVisibility(View.VISIBLE);
                            if(suono)
                                correctSound.start();
                            if(vibra)
                                vibrator.vibrate(500);
                            punti=punti+1;
                            punteggio.setText(getString(R.string.punteggio)+punti);
                            ll1.setVisibility(View.GONE);
                            ll2.setVisibility(View.GONE);

                            premutiLog="";
                        }
                        else{
                            err=err+1;

                            //GAME OVER
                            if(err>2){

                                if(suono){
                                    overSound.start();
                                }

                                numeroAiuti=0;
                                numeroSoluzioni=0;
                                session.incrPartite(categoria);
                                linear_content.setVisibility(View.GONE);
                                aiuto.setClickable(false);
                                soluzione.setClickable(false);
                                linear_gameover.setVisibility(View.VISIBLE);
                                if(nuovoRecord(punti))
                                    alertDialog.showAlertDialog(MainActivity.this, getString(R.string.nuovo_record_title),getString(R.string.nuovo_record)+punti,false);


                            }
                            else
                                errori.setText(getString(R.string.errori)+err);
                            if(suono)
                                wrongSound.start();
                            if(vibra)
                                vibrator.vibrate(500);

                        }

                        img.setClickable(false);

                        for(int i=0;i<premuti.size(); i++){
                            premutiLog+=", "+premuti.get(i);
                        }
                        premutiLog="";
                    }
                }
            });


            String sfondo_lettera=sostituisciSpecialChar(randomChar);
            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
            img.setBackground(getResources().getDrawable(j));
            lb.put(indicer,img);

        }

        //lista di chiavi
        List keys = new ArrayList(lb.keySet());

        //ordino la lista di chiavi in maniera casuale
        Collections.shuffle(keys);
        //creo una nuova hashmap
        HashMap<Integer, Button> lettereCasuali=new HashMap<>();
        int num=0;

        for (Object o : keys) {
            // Access keys/values in a random order
            lettereCasuali.put(o.hashCode(),lb.get(o));
            if(num<8)
                ll1.addView(lb.get(o));
            else
                ll2.addView(lb.get(o));
            num++;
        }
        parola_inglese.setText(voc.getInglese());
    }

    public void next(View view){
        ascoltata=false;
        premuti=new ArrayList<>();
        seconda_riga=false;
        soluzione.setClickable(true);
        aiuto.setClickable(true);
        prossimo++;
        linear_right.setVisibility(View.GONE);

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
                    alertDialog.showAlertDialog(MainActivity.this, "OPS!", getString(R.string.errore), false);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    alertDialog.showAlertDialog(MainActivity.this, "OPS!", getString(R.string.errore), false);
                }

            }
            else{
                alertDialog.showAlertDialog(MainActivity.this, getString(R.string.attenzione), getString(R.string.attiva_connessione), true);
            }

        }
        setLettere(voc);
    }

    public void showSolution(View view)
    {
        soluzione.setClickable(false);
        AlertDialog.Builder alertD = new AlertDialog.Builder(
                MainActivity.this);
        alertD.setTitle(getString(R.string.soluzione));
        alertD.setMessage(getString(R.string.soluzione_msg));
        alertD.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String[] prima=voc.getLingua_nativa().split(",");

                //prendo la prima traduzione, ignoro i sinonimi
                final String italiano=prima[0];
                String[] arr=italiano.toLowerCase().split("(?!^)");
                for(int i=0; i<arr.length; i++)
                {
                    Button black_button=bb.get(i);
                    if(!arr[i].equalsIgnoreCase(" "))
                    {
                        String sfondo_lettera=sostituisciSpecialChar(arr[i].charAt(0));
                        int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                        black_button.setBackground(getResources().getDrawable(j));
                        black_button.setClickable(false);
                    }
                    else
                    {
                        black_button.setBackgroundColor(Color.TRANSPARENT);
                        black_button.setClickable(false);
                    }
                }
                err++;
                errori.setText(getString(R.string.errori) + " " + err);
                linear_right.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                if(suono)
                    correctSound.start();
                if(vibra)
                    vibrator.vibrate(500);
                frase_tv.setText("");
            }
        });

        alertD.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertD.show();
        numeroSoluzioni++;
    }

    public void showAiuto(View view)
    {
        if(numeroAiuti<3)
        {
            AlertDialog.Builder alertD = new AlertDialog.Builder(MainActivity.this);
            alertD.setTitle(getString(R.string.aiuto));
            alertD.setMessage(getString(R.string.aiuto_msg));
            alertD.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    //Estraggo la traduzione, ignoro i sinonimi
                    String[] prima=voc.getLingua_nativa().split(",");
                    final String italiano=prima[0];

                    //estraggo le singole lettere
                    String[] arr=italiano.toLowerCase().split("(?!^)");

                    //calcolo il numero di lettere da mostrare nell'aiuto
                    int lunghezza_tot = italiano.length();
                    int lettere_da_mostrare = lunghezza_tot/3;

                    String sottostringa=italiano.substring(0, lettere_da_mostrare);
                    //sottostringa.
                    if(sottostringa.matches(".*\\s+.*"))
                    {
                        AlertDialogManager alertD=new AlertDialogManager();
                        alertD.showAlertDialog(MainActivity.this, "Help", getString(R.string.prime_lettere)+sottostringa, false);
                    }
                    else
                    {
                        //reset delle lettere inserite dall utente
                        parola_selezionata = new ArrayList<>();
                        premuti = new ArrayList<>();

                        //riprisino i background dei bottoni eventualmente premuti dall'utente
                        for(int i=0; i<lunghezza_tot; i++)
                        {
                            //ripristino caselle cliccabili
                            Button b=lb.get(i);
                            String sfondo_lettera = sostituisciSpecialChar(arr[i].charAt(0));
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            b.setBackground(getResources().getDrawable(j));
                            b.setClickable(true);

                            //ripristino black button
                            Button black_b=bb.get(i);
                            black_b.setBackground(getResources().getDrawable(R.drawable.black));
                        }

                        String randomLetters = getString(R.string.alfabeto);

                        for(int i=lunghezza_tot; i<16; i++)
                        {
                            Button b=lb.get(i);
                            int id=b.getId();
                            final Character randomChar=randomLetters.charAt(id);

                            b.setClickable(true);
                            String sfondo_lettera=sostituisciSpecialChar(randomChar);
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            b.setBackground(getResources().getDrawable(j));
                        }

                        for(int i=0; i<lettere_da_mostrare; i++)
                        {
                            Button black_button=bb.get(i);
                            String sfondo_lettera=sostituisciSpecialChar(arr[i].charAt(0));
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            //mostro le lettere della soluzione
                            black_button.setBackground(getResources().getDrawable(j));
                            //aggiungo le lettere prescelte alla soluzione che sto generando
                            parola_selezionata.add(arr[i]);
                            //Log.v("onclick",arr[i]);
                            //rendo le lettere dell'aiuto non deselezionabili
                            black_button.setClickable(false);

                            //cambio lo sfondo delle lettere selezionate rendendolo nero, e disabilito il click su esse
                            Button b=lb.get(i);
                            b.setBackground(getResources().getDrawable(R.drawable.black));
                            b.setClickable(false);

                            //aggiungo gli id dei button clickati alla lista dei premuti
                            premuti.add(i);

                        }
                    }

                    //disabilito ulteriori click al bottone di aiuto
                    aiuto.setClickable(false);

                    //scalo i punti dovuti all'aiuto e visualizzo il totale aggiornato
                    punti=punti-1;
                    punteggio.setText(getString(R.string.punteggio)+" "+punti);
                }
            });

            alertD.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            alertD.show();
            numeroAiuti++;
        }
        else
        {
            alertDialog.showAlertDialog(MainActivity.this, getString(R.string.aiuti_terminati_title), getString(R.string.aiuti_terminati), false);
        }



    }

    @SuppressLint("SetTextI18n")
    public void restart(View view)
    {
        premuti=new ArrayList<Integer>();
        punti=0;
        err=0;
        errori.setText(getString(R.string.errori)+" "+err);
        linear_content.setVisibility(View.VISIBLE);
        aiuto.setClickable(true);
        soluzione.setClickable(true);
        linear_gameover.setVisibility(View.GONE);
        getVocaboli();

    }

    @SuppressLint("SetTextI18n")
    public boolean nuovoRecord(float punti)
    {
        record=session.getRecord(categoria);
        if(punti>record)
        {
            session.setRecord(categoria, punti);
            record_tv.setText(getString(R.string.record)+" "+punti);
            return true;
        }
        else
            return false;

    }

    public void showFrase(View view)
    {
        frase_tv.setText(voc.getFrase());

    }

    public void ascoltaParola(View view)
    {
        if(!ascoltata)
        {
            if(connectivityManager.check()){
                AudioRequest ar=new AudioRequest();
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

                ar.execute(url);
            }
            else
                Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();

        }
        else
            ascolta.start();

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

                Log.d("test", statusCode + "");
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
                ascolta= new MediaPlayer();
                try {
                    ascolta.setDataSource(url);
                    ascolta.prepare();
                    ascolta.start();
                    ascoltata=true;

                }
                catch (SecurityException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(getApplicationContext(),getString(R.string.no_audio) , Toast.LENGTH_SHORT).show();

        }
    }

    //

    public String sostituisciSpecialChar(Character c)
    {
        String res;
        switch(c) {

            case 'ß':
                res="sb";
                break;

            case 'á':
                res="aacuta";
                break;

            case 'ó':
                res="oacuta";
                break;


            case 'ö':
                res="opt";
                break;




            case 'ñ':
                res="ntilde";
                break;

            case 'í':
                res="iacuta";
                break;

            case 'é':
                res="eacuta";
                break;

            case 'è':
                res="egrave";
                break;

            case 'ê':
                res="eflex";
                break;

            case 'ë':
                res="ept";
                break;

            case 'à':
                res="agrave";
                break;

            case 'â':
                res="aflex";
                break;

            case 'ä':
                res="apt";
                break;

            case 'î':
                res="iflex";
                break;

            case 'ï':
                res="ipt";
                break;

            case 'ô':
                res="oflex";
                break;

            case 'ù':
                res="ugrave";
                break;

            case 'û':
                res="uflex";
                break;

            case 'ú':
                res="uacuta";
                break;

            case 'ü':
                res="upt";
                break;

            case 'ÿ':
                res="ypt";
                break;



            default:
                res=c.toString();
        }
        return res;
    }




}


