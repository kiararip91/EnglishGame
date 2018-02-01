package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class QuizLivelliActivity extends Activity {

    /**
     * Declaring variables
     */
    MyConnectivityManager connectivityManager;
    EnglishGameUtility gameUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_livelli);

        connectivityManager=new MyConnectivityManager(getApplicationContext());

        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 10);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void adjectives(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 11);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void question_words(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 12);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }


    public void past_simple(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 13);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }


    public void regular_irregular_verbs(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizTraduzioneActivity.class);
            p.putExtra("quiz", 20);
            startActivity(p);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void negative_forms(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizTraduzioneActivity.class);
            p.putExtra("quiz", 21);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void some_any_much_many(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizTraduzioneActivity.class);
            p.putExtra("quiz", 22);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void articles(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 25);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void prepositions(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizTraduzioneActivity.class);
            p.putExtra("quiz", 36);
            startActivity(p);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void common_verbs(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 30);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }


    public void modal_verbs(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 37);
            startActivity(p);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void idioms(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 40);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void word_conversion(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizTraduzioneActivity.class);
            p.putExtra("quiz", 42);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void on_pv(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizTraduzioneActivity.class);
            p.putExtra("quiz", 32);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void out_pv(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizTraduzioneActivity.class);
            p.putExtra("quiz", 33);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }


    public void word_conversion_be(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizTraduzioneActivity.class);
            p.putExtra("quiz", 50);
            startActivity(p);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    public void antonyms_and_synonyms(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 51);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }
}
