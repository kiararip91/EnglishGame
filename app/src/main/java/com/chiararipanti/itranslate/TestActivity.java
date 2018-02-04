package com.chiararipanti.itranslate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import com.chiararipanti.itranslate.db.TestSession;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.GetQuizFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//TODO: controlla variabili inutili, integra con fireBase
/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class TestActivity extends Activity {

    /**
     * Declaring variables
     */
    MyConnectivityManager connectivityManager;
    int tipoQuiz;
    int quizTot;
    int quizNow;
    TestSession test;
    ArrayList<TestSession> testSessions;
    int next;
    int score;
    TextView remainingProportionTextView;
    TextView quizQuestionTextView;
    Button firstOptionButton;
    Button secondOptionButton;
    Button thirdOptionButton;
    Button forthOptionButton;
    AlertDialog.Builder alertBuilder;
    EnglishGameUtility gameUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        gameUtils = new EnglishGameUtility(this);
        quizQuestionTextView = findViewById(R.id.quiz_question);
        firstOptionButton = findViewById(R.id.first_option);
        secondOptionButton = findViewById(R.id.second_option);
        thirdOptionButton = findViewById(R.id.third_option);
        forthOptionButton = findViewById(R.id.forth_option);
        remainingProportionTextView = findViewById(R.id.remaining_proportion);
        alertBuilder = new AlertDialog.Builder(TestActivity.this);
        score = 0;
        connectivityManager=new MyConnectivityManager(getApplicationContext());

        Intent intent=getIntent();
        tipoQuiz=intent.getIntExtra("quiz",0);
        quizTot = 0;
        quizNow = 1;

        populateQuiz(tipoQuiz);
        setUpQuiz();

        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();
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
        GetQuizFromDB getquizsTask = new GetQuizFromDB(type);
        try {
            getquizsTask.execute();
            testSessions = getquizsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        test = testSessions.get(next);
        quizTot = testSessions.size();
        remainingProportionTextView.setText(new StringBuilder().append(quizNow).append("\\").append(quizTot));
    }

    public void setUpQuiz(){
        remainingProportionTextView.setText(new StringBuilder().append(quizNow).append("\\").append(quizTot));
        quizQuestionTextView.setText(test.getQuestion());
        ArrayList<String> alternatives = test.getAlternatives();
        alternatives.add(test.getCorrectAnswer());
        Collections.shuffle(alternatives);
        firstOptionButton.setText(alternatives.get(0));
        secondOptionButton.setText(alternatives.get(1));
        thirdOptionButton.setText(alternatives.get(2));
        forthOptionButton.setText(alternatives.get(3));
    }

    public void next(View view){
        quizNow++;
        final Button selectedButton = (Button)view;
        String buttonText = selectedButton.getText().toString();
        if(buttonText.equals(test.getCorrectAnswer())){
            selectedButton.setBackgroundColor(Color.GREEN);
            score++;
            gameUtils.soundCorrect();
        }else{
            selectedButton.setBackgroundColor(Color.RED);
            gameUtils.soundWrong();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedButton.setBackgroundResource(R.drawable.shape_button);
                next++;
                if(next+1<=quizTot){
                    test = testSessions.get(next);
                    setUpQuiz();
                } else{
                    alertBuilder.setTitle("The END");
                    alertBuilder
                            .setMessage("Final SCORE: "+score)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent quizLevelIntent = new Intent(TestActivity.this, TestSubjectsActivity.class);
                                    startActivity(quizLevelIntent);
                                }
                            });
                    alertBuilder.show();
                }
            }
        }, 1000);
    }
}
