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

//TODO: Refactoring variables e resources, elimina putExtra1

public class LearnSubjectsActivity extends Activity {
    Button principianteb;
    Button baseb;
    Button intermediob;
    Button espertob;
    Button animalib;
    Button viaggib;
    Button vitab;
    Button cibob;
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

        principianteb = findViewById(R.id.principiante);
        baseb = findViewById(R.id.base);
        intermediob = findViewById(R.id.intermedio);
        espertob = findViewById(R.id.esperto);
        animalib = findViewById(R.id.animali);
        viaggib = findViewById(R.id.viaggi);
        vitab = findViewById(R.id.life);
        cibob = findViewById(R.id.cibo);

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
            Intent p;
            if (action.equals("impara"))
                p = new Intent(getApplicationContext(), LearnActivity.class);
            else
                p = new Intent(getApplicationContext(), TranslationActivity.class);
            p.putExtra("categoria1", getString(R.string.principiante));
            p.putExtra("category", "principiante");
            startActivity(p);
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
            b.putExtra("categoria1", getString(R.string.base));
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
            i.putExtra("categoria1", getString(R.string.intermedio));
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
            e.putExtra("categoria1", getString(R.string.esperto));
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
            e.putExtra("categoria1", getString(R.string.cibo));
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
            e.putExtra("categoria1", getString(R.string.viaggi));
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
            e.putExtra("categoria1", getString(R.string.vita_quotidiana));
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
            e.putExtra("categoria1", getString(R.string.animali));
            e.putExtra("category", "animali");
            startActivity(e);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
    }
}