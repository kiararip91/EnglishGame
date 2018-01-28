package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_livelli);

        connectivityManager=new MyConnectivityManager(getApplicationContext());
        ActionBar actionBar = getActionBar();

        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //****************inserimento bunner pubblicitario***************************
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

    private void vocabulary(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 1);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    private void families(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 1);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    private void clothes(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 1);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    private void animals(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 1);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    private void food_vocabulary(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 1);
            startActivity(p);
        }else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }

    private void relative_pronouns(View view){
        if(connectivityManager.check()){
            Intent p;
            p=new Intent(QuizLivelliActivity.this, QuizActivity.class);
            p.putExtra("quiz", 38);
            startActivity(p);
        } else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();
    }
}
