package com.chiararipanti.itranslate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chiararipanti.itranslate.db.GetQuizGapFromDB;
import com.chiararipanti.itranslate.model.QuizGap;
import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.EnglishGameUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class TestGapActivity extends Activity {

    private int type;
    private String category;
    private EnglishGameUtility gameUtils;

    private TextView categoryTextView;
    private TextView questionTextView;
    private TextView urserInputTextView;
    private TextView remainingProportionTextView;
    AlertDialog.Builder alertBuilder;


    private List<QuizGap> quizGaps;
    private QuizGap quizGap;
    private int totalQuestion;
    private int doneQuestion;
    private int next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gap);

        //Setting up Activity
        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        //Retrieve intent info
        Intent intent=getIntent();
        type = Integer.parseInt(intent.getStringExtra("type"));
        category = intent.getStringExtra("category");

        //Setting up layout
        questionTextView = findViewById(R.id.question);
        categoryTextView = findViewById(R.id.category);
        urserInputTextView = findViewById(R.id.user_input);
        remainingProportionTextView = findViewById(R.id.remaining_proportion);
        categoryTextView.setText(category);
        alertBuilder = new AlertDialog.Builder(TestGapActivity.this);

        doneQuestion = 0;

        getQuiz(type);
        setUpQuiz();

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

    public void next(View view){
        next++;
        doneQuestion++;
        if(next < quizGaps.size()) {
            quizGap = quizGaps.get(next);
            setUpQuiz();
        }else{
            //End of avaliable word
            endGame();
        }
    }

    public void verifySolution(View view){
        String userInput = urserInputTextView.getText().toString();

        //Correct Answer
        if (urserInputTextView.getText().toString().equalsIgnoreCase(quizGap.getAnswer())){
            gameUtils.soundCorrect();
            Toast.makeText(getApplicationContext(), R.string.esatta, Toast.LENGTH_SHORT).show();
            next++;
            doneQuestion++;
            if(next < quizGaps.size()){
                quizGap = quizGaps.get(next);
                setUpQuiz();
            }else{
                //End of avaliable word
                endGame();

            }
        }
        //Wrong Answer
        else{
            gameUtils.soundWrong();
            Toast.makeText(getApplicationContext(), R.string.wrong_answer, Toast.LENGTH_SHORT).show();
        }

    }

    private void getQuiz(int type){

        quizGaps = new ArrayList<>();
        //quizTot = 0;
        next = 0;

        GetQuizGapFromDB getquizsTask = new GetQuizGapFromDB(type);
        try {
            getquizsTask.execute();
            quizGaps = getquizsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        quizGap = quizGaps.get(next);
        totalQuestion = quizGaps.size();
        remainingProportionTextView.setText(new StringBuilder().append(doneQuestion).append("\\").append(totalQuestion));

    }

    private void setUpQuiz(){
        String question = quizGap.getQuestion();
        questionTextView.setText(question);
        urserInputTextView.setText("");
        remainingProportionTextView.setText(new StringBuilder().append(doneQuestion).append("\\").append(totalQuestion));
    }

    private void endGame(){
        alertBuilder.setTitle("The END");
        alertBuilder
                .setMessage("You have completed this section")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backToSubjectIntent = new Intent(TestGapActivity.this, TestSubjectsActivity.class);
                        startActivity(backToSubjectIntent);
                    }
                });
        alertBuilder.show();
    }

}
