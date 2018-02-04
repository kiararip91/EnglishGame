package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//TODO: 1) Descrizione Attività, 2) A che corrispondono questi numeri
//FIXME: Crasha su un sacco di categorie, capire quali

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class TestSubjectsActivity extends Activity {

    /**
     * Declaring variables
     */
    private MyConnectivityManager connectivityManager;
    private Intent quizIntent;

    private String INTENT_PARAM = "quiz";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_subjects);

        connectivityManager=new MyConnectivityManager(getApplicationContext());

        EnglishGameUtility gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        quizIntent = new Intent(getApplicationContext(), TestActivity.class);

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

    public void verbs(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 10);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void adjectives(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 11);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void question_words(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 12);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }


    public void past_simple(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 13);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }


    public void regular_irregular_verbs(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 20);
            startActivity(quizIntent);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void negative_forms(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 21);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void some_any_much_many(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 22);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void articles(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 25);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void prepositions(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 36);
            startActivity(quizIntent);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void common_verbs(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 30);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }


    public void modal_verbs(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 37);
            startActivity(quizIntent);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void idioms(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 40);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void word_conversion(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 42);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void on_pv(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 32);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void out_pv(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 33);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void relative_pronouns(View view){ //FIXME: Capire a che numero corrisponde
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 33);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }


    public void word_conversion_be(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 50);
            startActivity(quizIntent);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void antonyms_and_synonyms(View view){
        if(connectivityManager.check()){
            quizIntent.putExtra(INTENT_PARAM, 51);
            startActivity(quizIntent);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

}
