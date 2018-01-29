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
import com.chiararipanti.itranslate.util.AudioRequest;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
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

    /**
     * Checked
     */
    private EnglishGameUtility gameUtils;
    private List<Vocabolo> words;
    private int index;
    private Vocabolo word;

    /**
     * Checked
     */


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
    SessionManager session;
    ArrayList<String> parola_selezionata;
    int prossimo;
    String categoria;
    AlertDialogManager alertDialog;
    AlertDialogManager settingDialog;
    MyConnectivityManager connectivityManager;
    ArrayList<Button> bb;
    private boolean listened;
    String lingua;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameUtils = new EnglishGameUtility(this);
        prossimo = 0;
        alertDialog = new AlertDialogManager();
        settingDialog = new AlertDialogManager();
        Intent intent = getIntent();
        categoria=intent.getStringExtra("categoria");
        mySwitch = findViewById(R.id.switchForActionBar);
        connectivityManager = new MyConnectivityManager(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        display= getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        rapporto = size.x /9;
        seconda_riga = false;
        lingua = Locale.getDefault().getLanguage() ;
        numeroAiuti = 0;
        numeroSoluzioni = 0;

        lb=new HashMap<>();
        record=session.getRecord(categoria);
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

        this.words = this.getWords();
        this.setLetters(words,this.index);

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
            gameUtils.manageSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieve a list of word form DataSource
     */
    private List<Vocabolo> getWords(){
        List<Vocabolo> words = new ArrayList<>();

        if(!connectivityManager.check()) {
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(this,StartActivity.class);
            startActivity(intent1);
        }else{
            this.index = 0;
            GetVocaboliFromDB getvocTask=new GetVocaboliFromDB(this,categoria);
            words = new ArrayList<>();
            try {
                getvocTask.execute();
                words = getvocTask.get();
            } catch (Exception e) {
                String TAG = "MainActivity";
                Log.e(TAG, "Error in retriving vocaboli "+ e.getMessage());
                alertDialog.showAlertDialog(MainActivity.this, "OPS!", getString(R.string.errore), false);
            }
        }
        return words;
    }

    public void setLetters(List<Vocabolo> words, int index){
        this.word = words.get(index);
        this.listened = false;
        //ripristino le visibilta dei layout
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.VISIBLE);
        punteggio.setText(getString(R.string.punteggio)+punti);
        errori.setText(getString(R.string.errori)+err);
        ll1.removeAllViews();
        ll2.removeAllViews();
        ll_black.removeAllViews();
        ll_black2.removeAllViews();
        this.premuti = new ArrayList<>();
        this.seconda_riga = false;
        this.soluzione.setClickable(true);
        aiuto.setClickable(true);
        linear_right.setVisibility(View.GONE);

        //array di botton inizialmente neri
        bb = new ArrayList<>();

        //la parola che si sta formando in italiano clicckando sulle lettere
        parola_selezionata = new ArrayList<>();

        //lettere di cui e composta la parola in italiano
        String[] prima=this.word.getLingua_nativa().split(",");

        //prendo la prima traduzione, ignoro i sinonimi
        final String italiano = prima[0];

        //estraggo le lettere dalla parola
        String[] arr=italiano.toLowerCase().split("(?!^)");
        int nlettere = arr.length;

        if(nlettere>8)
            seconda_riga=true;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, rapporto);
        lp.weight = 1;
        lp.leftMargin = 5;
        lp.rightMargin = 5;

        //genero i pulsanti con le lettere della parola
        for(int i=0;i<nlettere; i++){
            final int indice = i;

            if(!arr[i].equalsIgnoreCase(" ")){
                final Button img = new Button(getApplicationContext());
                img.setId(indice);
                img.setLayoutParams(lp);
                final String carattere = arr[i];
                String sfondo_lettera = gameUtils.substituteSpecialChar(arr[i].charAt(0));
                int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                img.setBackground(getResources().getDrawable(j));

                //click lettera parola
                img.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int numero_lettera = parola_selezionata.size();

                        //parola non ancora terminata
                        if(numero_lettera < italiano.length()-1){
                            Character car_italiano = italiano.charAt(parola_selezionata.size());
                            //la lettera corrispondente non e' uno spazio
                            if(!(String.valueOf(car_italiano).equalsIgnoreCase(" "))){
                                parola_selezionata.add(carattere);
                                premuti.add(indice);
                                //estraggo il bottone nero successivo e gli cambio lo sfondo in base alla lettera selezionata
                                Button black_button = bb.get(numero_lettera);
                                String sfondo_lettera = gameUtils.substituteSpecialChar(carattere.charAt(0));
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
                                Button black_button = bb.get(numero_lettera+1);
                                String sfondo_lettera = gameUtils.substituteSpecialChar(carattere.charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                black_button.setBackground(getResources().getDrawable(w));
                                img.setBackground(getResources().getDrawable(R.drawable.black));
                            }
                        }

                        //ho selezionato l'ultima lettera
                        else if(numero_lettera == italiano.length()-1){
                            //aggiungo il carattere selezionato alla parola che sto formando
                            parola_selezionata.add(carattere);
                            Log.v("onclick",carattere+" "+indice);
                            premuti.add(indice);
                            //cambio lo sfondo dell'ultimo bottone
                            Button black_button=bb.get(numero_lettera);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(carattere.charAt(0));
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            black_button.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));

                            //convalido la stringa formata
                            String da_convalidare = "";

                            for(int i=0; i<parola_selezionata.size(); i++){
                                da_convalidare = da_convalidare+(parola_selezionata.get(i));
                            }
                            if(italiano.equalsIgnoreCase(da_convalidare)){
                                black_button.setClickable(false);
                                linear_right.setVisibility(View.VISIBLE);

                                gameUtils.soundCorrect();
                                gameUtils.vibrate();

                                punti=punti+1;
                                punteggio.setText(getString(R.string.punteggio)+punti);
                                ll1.setVisibility(View.GONE);
                                ll2.setVisibility(View.GONE);
                                frase_tv.setText("");

                                premutiLog = "";
                            }
                            else{
                                err = err+1;

                                //GAME OVER
                                if(err > 2){
                                    gameUtils.soundOver();

                                    numeroAiuti = 0;
                                    numeroSoluzioni = 0;

                                    gameUtils.vibrate();

                                    session.incrPartite(categoria);
                                    linear_content.setVisibility(View.GONE);
                                    aiuto.setClickable(false);
                                    soluzione.setClickable(false);
                                    linear_gameover.setVisibility(View.VISIBLE);

                                    if(isNewRecord(punti)){
                                        alertDialog.showAlertDialog(MainActivity.this, getString(R.string.nuovo_record_title),getString(R.string.nuovo_record)+punti,false);
                                    }
                                }else{
                                    errori.setText(getString(R.string.errori)+err);
                                }

                                gameUtils.soundWrong();
                                gameUtils.vibrate();
                            }

                            img.setClickable(false);

                            for(int i = 0;i<premuti.size(); i++) {
                                premutiLog+=", "+premuti.get(i);
                            }
                            premutiLog = "";
                        }
                    }

                });

                lb.put(indice,img);

                //genero pulsante nero
                final Button black=new Button(getApplicationContext());
                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(0, rapporto);

                lpb.weight = 1;
                lpb.leftMargin = 5;
                lpb.rightMargin = 5;
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
                                int ripristina = premuti.get(indice);

                                for (int i = 0;i<premuti.size(); i++){
                                    premutiLog+=", "+premuti.get(i);
                                }
                                premutiLog = "";
                                premuti.remove(indice);

                                for (int i = 0;i<premuti.size(); i++){
                                    premutiLog+= ", "+premuti.get(i);
                                }
                                premutiLog = "";
                                Button b = lb.get(ripristina);
                                String sfondo_lettera = gameUtils.substituteSpecialChar(parola_selezionata.get(indice).toString().charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                b.setBackground(getResources().getDrawable(w));
                                b.setClickable(true);
                                parola_selezionata.remove(indice);

                            }else{
                                black.setBackground(getResources().getDrawable(R.drawable.black));

                                int ripristina = premuti.get(indice);

                                for (int i = 0;i<premuti.size(); i++){
                                    premutiLog+= ", " + premuti.get(i);
                                }

                                premutiLog = "";
                                premuti.remove(indice);
                                premuti.remove(premuti.indexOf(100));

                                for (int i = 0; i<premuti.size(); i++){
                                    premutiLog+=", " + premuti.get(i);
                                }

                                premutiLog = "";
                                Button b = lb.get(ripristina);
                                b.setClickable(true);
                                String sfondo_lettera = gameUtils.substituteSpecialChar(parola_selezionata.get(indice).charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                b.setBackground(getResources().getDrawable(w));
                                parola_selezionata.remove(indice);
                                parola_selezionata.remove(" ");
                            }
                        }
                    }
                });
                bb.add(black);

                if(i < 8){
                    ll_black.addView(black);
                }else{
                    ll_black2.addView(black);
                }
            }else{
                //Space
                Button black=new Button(getApplicationContext());
                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(0, rapporto);
                lpb.weight=1;
                lpb.leftMargin=5;
                lpb.rightMargin=5;
                black.setLayoutParams(lpb);
                black.setBackgroundColor(Color.TRANSPARENT);
                black.setClickable(false);
                bb.add(black);
                if(i < 8){
                    ll_black.addView(black);
                }else{
                    ll_black2.addView(black);
                }
            }
        }

        String randomLetters = getString(R.string.alfabeto);
        Random rnd = new Random();

        //inserisco altre lettere random in tot ne avro 16
        //inserisco anche finti bottoni bianchi affinaco ai neri per avere la giusta dimensione relativa delle caselle
        for(int i=nlettere;i<16;i++){
            final Button fasullo = new Button(getApplicationContext());
            fasullo.setLayoutParams(lp);
            fasullo.setBackgroundColor(Color.TRANSPARENT);
            fasullo.setClickable(false);

            if(i < 8){
                ll_black.addView(fasullo);
            }else{
                if(seconda_riga){
                    ll_black2.addView(fasullo);
                }
            }

            final int indicer = i;
            final Button img = new Button(getApplicationContext());
            img.setLayoutParams(lp);
            int randomGen = rnd.nextInt(randomLetters.length());
            final Character randomChar = randomLetters.charAt(randomGen);

            //assegno il numero random genrerato come id del bottone per poter risalire al carattere corrispondente
            img.setId(randomGen);

            //click lettera random
            img.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                public void onClick(View v) {
                    int numero_lettera = parola_selezionata.size();

                    //non sto inserendo l'ultima lettera
                    if(numero_lettera < italiano.length()-1){
                        Character car_italiano = italiano.charAt(parola_selezionata.size());

                        //la lettera corrispondente non e' uno spazio ma una casella nera
                        if(!(String.valueOf(car_italiano).equalsIgnoreCase(" "))){
                            premuti.add(indicer);
                            parola_selezionata.add(randomChar.toString());
                            Button black_button = bb.get(numero_lettera);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            black_button.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));

                        }else{
                            //la lettera che devo inserire va dopo lo spazio
                            parola_selezionata.add(" ");
                            parola_selezionata.add(randomChar.toString());
                            premuti.add(100);
                            premuti.add(indicer);
                            Button black_button = bb.get(numero_lettera+1);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            black_button.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));
                        }

                    }else if(numero_lettera==italiano.length()-1){
                        //sto inserendo l'ultima lettera
                        premuti.add(indicer);
                        parola_selezionata.add(randomChar.toString());
                        Button black_button = bb.get(numero_lettera);
                        String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
                        int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                        black_button.setBackground(getResources().getDrawable(w));
                        img.setBackground(getResources().getDrawable(R.drawable.black));
                        String da_convalidare = "";

                        for(int i = 0; i < parola_selezionata.size(); i++){
                            da_convalidare = da_convalidare + (parola_selezionata.get(i));
                        }

                        if(italiano.equalsIgnoreCase(da_convalidare)){
                            black_button.setClickable(false);
                            linear_right.setVisibility(View.VISIBLE);

                            gameUtils.soundCorrect();
                            gameUtils.vibrate();

                            punti = punti+1;
                            punteggio.setText(getString(R.string.punteggio)+punti);
                            ll1.setVisibility(View.GONE);
                            ll2.setVisibility(View.GONE);

                            premutiLog = "";
                        }
                        else{
                            err = err+1;

                            //GAME OVER
                            if(err>2){

                                gameUtils.soundOver();

                                numeroAiuti = 0;
                                numeroSoluzioni = 0;
                                session.incrPartite(categoria);
                                linear_content.setVisibility(View.GONE);
                                aiuto.setClickable(false);
                                soluzione.setClickable(false);
                                linear_gameover.setVisibility(View.VISIBLE);

                                if(isNewRecord(punti)){
                                    alertDialog.showAlertDialog(MainActivity.this, getString(R.string.nuovo_record_title),getString(R.string.nuovo_record)+punti,false);
                                }
                            }else{
                                errori.setText(getString(R.string.errori)+err);
                            }

                            gameUtils.soundWrong();
                            gameUtils.vibrate();

                        }

                        img.setClickable(false);

                        for(int i = 0; i < premuti.size(); i++){
                            premutiLog+=", " + premuti.get(i);
                        }
                        premutiLog = "";
                    }
                }
            });

            String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
            img.setBackground(getResources().getDrawable(j));
            lb.put(indicer,img);
        }

        //lista di chiavi
        List keys = new ArrayList(lb.keySet());

        //ordino la lista di chiavi in maniera casuale
        Collections.shuffle(keys);
        //creo una nuova hashmap
        HashMap<Integer, Button> lettereCasuali = new HashMap<>();
        int num = 0;

        for (Object o : keys) {
            // Access keys/values in a random order
            lettereCasuali.put(o.hashCode(),lb.get(o));

            if(num < 8){
                ll1.addView(lb.get(o));
            }
            else{
                ll2.addView(lb.get(o));
            }

            num++;
        }

        parola_inglese.setText(word.getInglese());
    }

    public void next(View view){
        this.index++;

        if(this.index++ == 20){
            this.getWords();
        }
        this.setLetters(this.words, this.index);
    }

    public void showSolution(View view){

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
                String[] prima = word.getLingua_nativa().split(",");

                //prendo la prima traduzione, ignoro i sinonimi
                final String italiano = prima[0];
                String[] arr = italiano.toLowerCase().split("(?!^)");

                for(int i = 0; i < arr.length; i++) {
                    Button black_button = bb.get(i);

                    if(!arr[i].equalsIgnoreCase(" ")){
                        String sfondo_lettera = gameUtils.substituteSpecialChar(arr[i].charAt(0));
                        int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                        black_button.setBackground(getResources().getDrawable(j));
                        black_button.setClickable(false);
                    }else{
                        black_button.setBackgroundColor(Color.TRANSPARENT);
                        black_button.setClickable(false);
                    }
                }
                err++;
                errori.setText(getString(R.string.errori) + " " + err);
                linear_right.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);

                gameUtils.soundCorrect();
                gameUtils.vibrate();

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

    public void showAiuto(View view) {

        final Vocabolo word = this.word;

        if(numeroAiuti < 3){
            AlertDialog.Builder alertD = new AlertDialog.Builder(MainActivity.this);
            alertD.setTitle(getString(R.string.aiuto));
            alertD.setMessage(getString(R.string.aiuto_msg));
            alertD.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    //Estraggo la traduzione, ignoro i sinonimi
                    String[] prima = word.getLingua_nativa().split(",");
                    final String italiano = prima[0];

                    //estraggo le singole lettere
                    String[] arr = italiano.toLowerCase().split("(?!^)");

                    //calcolo il numero di lettere da mostrare nell'aiuto
                    int lunghezza_tot = italiano.length();
                    int lettere_da_mostrare = lunghezza_tot/3;

                    String sottostringa = italiano.substring(0, lettere_da_mostrare);
                    //sottostringa.
                    if(sottostringa.matches(".*\\s+.*")) {

                        AlertDialogManager alertD = new AlertDialogManager();
                        alertD.showAlertDialog(MainActivity.this, "Help", getString(R.string.prime_lettere)+sottostringa, false);
                    }
                    else
                    {
                        //reset delle lettere inserite dall utente
                        parola_selezionata = new ArrayList<>();
                        premuti = new ArrayList<>();

                        //riprisino i background dei bottoni eventualmente premuti dall'utente
                        for(int i = 0; i < lunghezza_tot; i++){
                            //ripristino caselle cliccabili
                            Button b = lb.get(i);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(arr[i].charAt(0));
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            b.setBackground(getResources().getDrawable(j));
                            b.setClickable(true);

                            //ripristino black button
                            Button black_b = bb.get(i);
                            black_b.setBackground(getResources().getDrawable(R.drawable.black));
                        }

                        String randomLetters = getString(R.string.alfabeto);

                        for(int i = lunghezza_tot; i < 16; i++){
                            Button b = lb.get(i);
                            int id = b.getId();
                            final Character randomChar = randomLetters.charAt(id);

                            b.setClickable(true);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            b.setBackground(getResources().getDrawable(j));
                        }

                        for(int i=0; i<lettere_da_mostrare; i++){
                            Button black_button = bb.get(i);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(arr[i].charAt(0));
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            //mostro le lettere della soluzione
                            black_button.setBackground(getResources().getDrawable(j));
                            //aggiungo le lettere prescelte alla soluzione che sto generando
                            parola_selezionata.add(arr[i]);
                            //rendo le lettere dell'aiuto non deselezionabili
                            black_button.setClickable(false);

                            //cambio lo sfondo delle lettere selezionate rendendolo nero, e disabilito il click su esse
                            Button b = lb.get(i);
                            b.setBackground(getResources().getDrawable(R.drawable.black));
                            b.setClickable(false);

                            //aggiungo gli id dei button clickati alla lista dei premuti
                            premuti.add(i);
                        }
                    }

                    //disabilito ulteriori click al bottone di aiuto
                    aiuto.setClickable(false);

                    //scalo i punti dovuti all'aiuto e visualizzo il totale aggiornato
                    punti = punti-1;
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

        }else{
            alertDialog.showAlertDialog(MainActivity.this, getString(R.string.aiuti_terminati_title), getString(R.string.aiuti_terminati), false);
        }
    }

    @SuppressLint("SetTextI18n")
    public void restart(View view){
        premuti = new ArrayList<Integer>();
        punti = 0;
        err = 0;
        errori.setText(getString(R.string.errori)+" "+err);
        linear_content.setVisibility(View.VISIBLE);
        aiuto.setClickable(true);
        soluzione.setClickable(true);
        linear_gameover.setVisibility(View.GONE);
        this.getWords();
        this.setLetters(this.words, this.index);
    }

    @SuppressLint("SetTextI18n")
    public boolean isNewRecord(float punti){
        boolean returnValue = false;
        record = session.getRecord(categoria);

        if(punti > record){
            session.setRecord(categoria, punti);
            record_tv.setText(getString(R.string.record)+" "+punti);
            returnValue = true;
        }

        return returnValue;

    }

    public void showSentence(View view){
        frase_tv.setText(this.word.getFrase());
    }

    public void listenToWord(View view){

        String englishWord = this.word.getInglese().toLowerCase();
        englishWord = gameUtils.substituteSpecialCharWordToPronunce(englishWord);

        String url = "https://ssl.gstatic.com/dictionary/static/sounds/oxford/" + englishWord + "--_gb_1.mp3";
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            String TAG = "MainActivity";
            Log.e(  TAG,"Unable to load audio");
        }
        if(!this.listened){
            if(connectivityManager.check()){
                AudioRequest ar = new AudioRequest(this, mediaPlayer);
                ar.execute(url);
                this.listened = true;
            }else {
                Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
            }
        }else {
            try {
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                String TAG = "MainActivity";
                Log.e(  TAG,"Unable to load audio");
            }
        }
    }
}