package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//TODO: fFai un unico metodo parametrizzato con intent e click bottone, Fai update db e passa interi invece di stringhe

public class LearnSubjectsActivity extends Activity {
    Button naiveButton;
    Button baseButton;
    Button intermediateButton;
    Button expertButton;
    Button animalButton;
    Button travelButton;
    Button lifeButton;
    Button foodButton;
    MyConnectivityManager connectivityManager;
    String action;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_subjects);

        EnglishGameUtility gameUtils = new EnglishGameUtility(this);
        gameUtils.addAdBunner();
        gameUtils.setHomeButtonEnabled();

        naiveButton = findViewById(R.id.naive);
        baseButton = findViewById(R.id.base);
        intermediateButton = findViewById(R.id.intermediate);
        expertButton = findViewById(R.id.expert);
        animalButton = findViewById(R.id.animal);
        travelButton = findViewById(R.id.travel);
        lifeButton = findViewById(R.id.life);
        foodButton = findViewById(R.id.food);

        connectivityManager = new MyConnectivityManager(getApplicationContext());

        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        session = new SessionManager(getApplicationContext());
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

    public void principiante(View view) {
        if (connectivityManager.check()) {
            Intent destinationActivity;
            if (action.equals("impara"))
                destinationActivity = new Intent(getApplicationContext(), LearnActivity.class);
            else
                destinationActivity = new Intent(getApplicationContext(), TranslationActivity.class);
            destinationActivity.putExtra("category", "principiante");
            startActivity(destinationActivity);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();

    }

    public void base(View view) {
        if (connectivityManager.check()) {

            Intent b;
            if (action.equals("impara"))
                b = new Intent(getApplicationContext(), LearnActivity.class);
            else
                b = new Intent(getApplicationContext(), TranslationActivity.class);
            b.putExtra("category", "base");
            startActivity(b);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();

    }

    public void intermedio(View view) {
        if (connectivityManager.check()) {

            Intent i;
            if (action.equals("impara"))
                i = new Intent(getApplicationContext(), LearnActivity.class);
            else
                i = new Intent(getApplicationContext(), TranslationActivity.class);
            i.putExtra("category", "intermedio");
            startActivity(i);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();

    }

    public void esperto(View view) {
        if (connectivityManager.check()) {

            Intent e;
            if (action.equals("impara"))
                e = new Intent(getApplicationContext(), LearnActivity.class);
            else
                e = new Intent(getApplicationContext(), TranslationActivity.class);
            e.putExtra("category", "esperto");
            startActivity(e);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();

    }

    public void cibo(View view) {
        if (connectivityManager.check()) {

            Intent e;
            if (action.equals("impara"))
                e = new Intent(getApplicationContext(), LearnActivity.class);
            else
                e = new Intent(getApplicationContext(), TranslationActivity.class);
            e.putExtra("category", "cibo");
            startActivity(e);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();

    }

    public void viaggi(View view) {
        if (connectivityManager.check()) {

            Intent e;
            if (action.equals("impara"))
                e = new Intent(getApplicationContext(), LearnActivity.class);
            else
                e = new Intent(getApplicationContext(), TranslationActivity.class);
            e.putExtra("category", "viaggi");
            startActivity(e);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();

    }

    public void vita(View view) {
        if (connectivityManager.check()) {

            Intent e;
            if (action.equals("impara"))
                e = new Intent(getApplicationContext(), LearnActivity.class);
            else
                e = new Intent(getApplicationContext(), TranslationActivity.class);
            e.putExtra("category", "vita_quotidiana");
            startActivity(e);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
    }

    public void animali(View view) {
        if (connectivityManager.check()) {
            Intent e;
            if (action.equals("impara"))
                e = new Intent(getApplicationContext(), LearnActivity.class);
            else
                e = new Intent(getApplicationContext(), TranslationActivity.class);
            e.putExtra("category", "animali");
            startActivity(e);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
    }
}