package com.chiararipanti.itranslate;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;
import com.chiararipanti.itranslate.db.QuizTraduzione;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.GetQuizTraduzioneFromDB;
import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class QuizTraduzioneActivity extends Activity {

    /**
     * Declaring Variables
     */
    SessionManager session;
    EnglishGameUtility gameUtils;
    QuizTraduzione quiz;
    ArrayList<QuizTraduzione> quizs;
    int tipoQuiz;
    int quizTot;
    int quizNow;
    int prossimo;
    int score;
    TextView text;
    TextView sol;
    TextView prop;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_traduzione);
        ActionBar actionBar = getActionBar();

        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        gameUtils = new EnglishGameUtility(this);
        text = findViewById(R.id.text);
        sol = findViewById(R.id.sol);
        alertBuilder = new AlertDialog.Builder(QuizTraduzioneActivity.this);
        Intent intent = getIntent();
        tipoQuiz = intent.getIntExtra("quiz",0);
        quizTot = 0;
        quizNow = 1;
        prossimo = 0;
        prop = findViewById(R.id.prop);
        score = 0;
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
            builder.setTitle(R.string.edit_pref);
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

    public void populateQuiz(int type){
        GetQuizTraduzioneFromDB getquizsTask=new GetQuizTraduzioneFromDB(this, type);
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
    }

    public void conferma(View view){
        if(sol.getText().toString().equalsIgnoreCase(quiz.getCorrectAnswer())){
            quizNow++;
            prossimo++;
            score++;
            gameUtils.soundCorrect();

            if(prossimo+1<quizTot){
                quiz=quizs.get(prossimo);
                sol.setText("");
                impostaQuiz();
            }else{
                alertBuilder.setTitle("The END");
                alertBuilder
                        .setMessage("Final SCORE: "+score)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(QuizTraduzioneActivity.this, QuizLivelliActivity.class);
                                startActivity(i);
                            }
                        });
                alertBuilder.show();
            }
        }else{
            gameUtils.soundWrong();
        }
    }
    public void next(View view){
        quizNow++;
        prossimo++;

        if(prossimo+1<=quizTot){
            quiz=quizs.get(prossimo);
            impostaQuiz();
        }else{
            alertBuilder.setTitle("The END");
            alertBuilder.setMessage("Final SCORE: "+score).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(QuizTraduzioneActivity.this, QuizLivelliActivity.class);
                    startActivity(i);
                }
            });
            alertBuilder.show();
        }
    }
}
