package com.chiararipanti.itranslate;

import java.util.ArrayList;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import com.chiararipanti.itranslate.db.Quiz;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
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
    SessionManager session;
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
    EnglishGameUtility gameUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        gameUtils = new EnglishGameUtility(this);
        session = new SessionManager(getApplicationContext());
        text = findViewById(R.id.text);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        prop = findViewById(R.id.prop);
        alertBuilder = new AlertDialog.Builder(QuizActivity.this);
        score = 0;
        connectivityManager=new MyConnectivityManager(getApplicationContext());

        Intent intent=getIntent();
        tipoQuiz=intent.getIntExtra("quiz",0);
        quizTot = 0;
        quizNow = 1;

        populateQuiz(tipoQuiz);

        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();
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
            gameUtils.manageSettings();
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
            gameUtils.soundCorrect();
        }else{
            b.setBackgroundColor(Color.RED);
            gameUtils.soundWrong();
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
