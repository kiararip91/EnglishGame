package com.chiararipanti.itranslate;

import java.util.ArrayList;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import com.chiararipanti.itranslate.db.Quiz;
import com.chiararipanti.itranslate.util.GetQuizFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class QuizActivity extends Activity {

    /**
     * Declaring variables
     */
    MyConnectivityManager connectivityManager;
    MediaPlayer wrongSound;
    MediaPlayer correctSound;
    SessionManager session;
    Boolean suono;
    int tipoQuiz;
    int quizTot;
    int quizNow;
    Quiz quiz;
    ArrayList<Quiz> quizs;
    int prossimo;
    int score;
    TextView prop;
    TextView text;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);
        correctSound = MediaPlayer.create(this, R.raw.correct);
        session = new SessionManager(getApplicationContext());
        suono=session.getSuono();
        text = findViewById(R.id.text);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        prop = findViewById(R.id.prop);
        alertBuilder = new AlertDialog.Builder(QuizActivity.this);
        score = 0;
        connectivityManager=new MyConnectivityManager(getApplicationContext());
        ActionBar actionBar = getActionBar();

        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        tipoQuiz=intent.getIntExtra("quiz",0);
        quizTot = 0;
        quizNow = 1;

        populateQuiz(tipoQuiz);


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
            super.onBackPressed();
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

            ll.addView(sbSound,layoutParams);
            AlertDialog.Builder builder;
            AlertDialog alertDialog;
            builder = new AlertDialog.Builder(this);
            builder.setView(ll);
            builder.setTitle("Edit settings");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            alertDialog = builder.create();

            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateQuiz(int type){
        GetQuizFromDB getquizsTask=new GetQuizFromDB(this, type);
        try {
            getquizsTask.execute();
            quizs=getquizsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        quiz=quizs.get(prossimo);
        quizTot=quizs.size();
        prop.setText(quizNow + "\\" + quizTot);
        impostaQuiz();
    }

    public void impostaQuiz(){
        prop.setText(quizNow + "\\" + quizTot);
        text.setText(quiz.getText());
        ArrayList<String> alt=quiz.get_alternative();
        alt.add(quiz.getCorrectAnswer());
        Collections.shuffle(alt);
        b1.setText(alt.get(0));
        b2.setText(alt.get(1));
        b3.setText(alt.get(2));
        b4.setText(alt.get(3));
    }

    public void next(View view){
        quizNow++;
        final Button b = (Button)view;
        String buttonText = b.getText().toString();
        if(buttonText.equals(quiz.getCorrectAnswer())){
            b.setBackgroundColor(Color.GREEN);
            score++;
            if(suono)
                correctSound.start();
        }else{
            b.setBackgroundColor(Color.RED);
            if(suono)
                wrongSound.start();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                b.setBackgroundResource(R.drawable.shape_button);
                prossimo++;
                if(prossimo+1<=quizTot){
                    quiz=quizs.get(prossimo);
                    impostaQuiz();
                } else{
                    alertBuilder.setTitle("The END");
                    alertBuilder
                            .setMessage("Final SCORE: "+score)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i=new Intent(QuizActivity.this, QuizLivelliActivity.class);
                                    startActivity(i);
                                }
                            });
                    alertBuilder.show();
                }
            }
        }, 1000);
    }
}
