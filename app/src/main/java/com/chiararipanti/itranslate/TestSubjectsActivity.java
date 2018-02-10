package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//FIXME: Crasha su un sacco di categorie, capire quali

/*
 * @author chiararipanti
 * date 04/05/2013
 */

/**
 * Mapping:
 *
 *  1 --> verbs
 *  2 --> adjectives
 *  3 --> question_words
 *  4 --> past_simple
 *  5 --> regular_irregular_verbs -- 20 ok
 *  6 --> negative_forms -- 21 ok
 *  7 --> some_any_much_many -- 22 ok
 *  8 --> articles
 *  9 --> prepositions -- 36 ok
 *  10 --> common_verbs
 *  11 --> idioms
 *  12 --> modal_verbs
 *  13 --> word_conversion -- 42 ok
 *  14 --> phrasal_verbs_on -- 32 ok
 *  15 --> phrasal_verbs_out -- 33 ok
 *  16 --> relative_pronouns
 *  17 --> word_conversion_be -- 50 ok
 *  18 --> antonyms_and_synonyms
 *
 */
public class TestSubjectsActivity extends Activity {

    /**
     * Declaring variables
     */
    private MyConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_subjects);

        connectivityManager=new MyConnectivityManager(getApplicationContext());

        EnglishGameUtility gameUtils = new EnglishGameUtility(this);
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
        return super.onOptionsItemSelected(item);
    }

    public void goToTestChoose(View view){
        if(connectivityManager.check()){
            String INTENT_PARAM = "quiz";
            int type = mapButtonIdToType(view.getId());

            Intent quizIntent = new Intent(getApplicationContext(), TestChooseActivity.class);
            quizIntent.putExtra(INTENT_PARAM, type);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void goToTestGap(View view){
        if(connectivityManager.check()){
            String INTENT_PARAM_CATEGORY = "category";
            String INTENT_PARAM_TYPE = "type";
            int type = mapButtonIdToType(view.getId());
            Button selectedButton = (Button) view;

            Intent quizIntent = new Intent(getApplicationContext(), TestGapActivity.class);
            quizIntent.putExtra(INTENT_PARAM_CATEGORY, selectedButton.getText());
            quizIntent.putExtra(INTENT_PARAM_TYPE, type+"");
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    private int mapButtonIdToType(int butonId){
        int mappedTypeId = 1;
        switch ((butonId)){
            case R.id.verbs:
                mappedTypeId = 1;
                break;
            case R.id.adjectives:
                mappedTypeId = 2;
                break;
            case R.id.question_words:
                mappedTypeId = 3;
                break;
            case R.id.past_simple:
                mappedTypeId = 4;
                break;
            case R.id.regular_irregular_verbs:
                mappedTypeId = 5;
                break;
            case R.id.negative_forms:
                mappedTypeId = 6;
                break;
            case R.id.some_any_much_many:
                mappedTypeId = 7;
                break;
            case R.id.articles:
                mappedTypeId = 8;
                break;
            case R.id.prepositions:
                mappedTypeId = 9;
                break;
            case R.id.common_verbs:
                mappedTypeId = 10;
                break;
            case R.id.idioms:
                mappedTypeId = 11;
                break;
            case R.id.modal_verbs:
                mappedTypeId = 12;
                break;
            case R.id.word_conversion:
                mappedTypeId = 13;
                break;
            case R.id.phrasal_verbs_on:
                mappedTypeId = 14;
                break;
            case R.id.phrasal_verbs_out:
                mappedTypeId = 15;
                break;
            case R.id.relative_pronouns:
                mappedTypeId = 16;
                break;
            case R.id.word_conversion_be:
                mappedTypeId = 17;
                break;
            case R.id.antonyms_and_synonyms:
                mappedTypeId = 18;
                break;
        }

        return mappedTypeId;
    }

}
