package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class ArgomentiActivity extends Activity {

    SessionManager session;
    Context mcontext;

    int partiteVQ;
    int partiteA;
    int partiteV;
    int partiteC;

    float recordVQ;
    float recordA;
    float recordV;
    float recordC;

    TextView record_vq;
    TextView record_a;
    TextView record_v;
    TextView record_c;

    TextView partite_vq;
    TextView partite_a;
    TextView partite_v;
    TextView partite_c;

    int partiteP;
    int partiteB;
    int partiteI;
    int partiteE;

    float recordP;
    float recordB;
    float recordI;
    float recordE;

    TextView record_p;
    TextView record_b;
    TextView record_i;
    TextView record_e;

    TextView partite_p;
    TextView partite_b;
    TextView partite_i;
    TextView partite_e;

    EnglishGameUtility gameUtils;

    private static String CATEGORY_PARAM_NAME = "category";

    //********************fine bunner pubblicitario******************************

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argomenti);

        session = new SessionManager(getApplicationContext());
        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        mcontext=getApplicationContext();

        record_vq = findViewById(R.id.record_vq);
        record_a =  findViewById(R.id.record_a);
        record_v = findViewById(R.id.record_v);
        record_c = findViewById(R.id.record_c);

        partite_vq = findViewById(R.id.partite_giocate_vq);
        partite_a = findViewById(R.id.partite_giocate_a);
        partite_v = findViewById(R.id.partite_giocate_v);
        partite_c = findViewById(R.id.partite_giocate_c);

        recordVQ = session.getRecord(getString(R.string.vita_quotidiana));
        record_vq.setText((R.string.record) + ": " + recordVQ);

        recordA = session.getRecord(getString(R.string.animali));
        record_a.setText(getString(R.string.record)+": "+ recordA);

        recordV= session.getRecord(getString(R.string.viaggi));
        record_v.setText(getString(R.string.record) + recordV);

        recordC = session.getRecord(getString(R.string.cibo));
        record_c.setText(getString(R.string.record)+": "+ recordC);


        partiteVQ = session.getPartite(getString(R.string.vita_quotidiana));
        partite_vq.setText(getString(R.string.partite)+": "+ partiteVQ);

        partiteA = session.getPartite(getString(R.string.animali));
        partite_a.setText(getString(R.string.partite)+": "+ partiteA);

        partiteV = session.getPartite(getString(R.string.viaggi));
        partite_v.setText(getString(R.string.partite)+": "+ partiteV);

        partiteC = session.getPartite(getString(R.string.cibo));
        partite_c.setText(getString(R.string.partite)+": "+ partiteC);


        record_p = findViewById(R.id.record_p);
        record_b = findViewById(R.id.record_b);
        record_i= findViewById(R.id.record_i);
        record_e= findViewById(R.id.record_e);

        partite_p= findViewById(R.id.partite_giocate_p);
        partite_b= findViewById(R.id.partite_giocate_b);
        partite_i= findViewById(R.id.partite_giocate_i);
        partite_e= findViewById(R.id.partite_giocate_e);

        recordP= session.getRecord(getString(R.string.principiante));
        record_p.setText(getString(R.string.record)+": "+ recordP);

        recordB= session.getRecord(getString(R.string.base));
        record_b.setText(getString(R.string.record)+": "+ recordB);

        recordI= session.getRecord(getString(R.string.intermedio));
        record_i.setText(getString(R.string.record)+": "+ recordI);

        recordE= session.getRecord(getString(R.string.esperto));
        record_e.setText(getString(R.string.record)+": "+ recordE);


        partiteP= session.getPartite(getString(R.string.principiante));
        partite_p.setText(getString(R.string.partite)+": "+ partiteP);

        partiteB= session.getPartite(getString(R.string.base));
        partite_b.setText(getString(R.string.partite)+": "+ partiteB);

        partiteI= session.getPartite(getString(R.string.intermedio));
        partite_i.setText(getString(R.string.partite)+": "+ partiteI);

        partiteE= session.getPartite(getString(R.string.esperto));
        partite_e.setText(getString(R.string.partite)+": "+ partiteE);
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



    public void animali(View view)
    {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "animali");
        startActivity(e);
    }

    public void cibo(View view)
    {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "cibo");
        startActivity(e);
    }

    public void viaggi(View view)
    {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "viaggi");
        startActivity(e);
    }

    public void vitaquotidiana(View view)
    {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "vita_quotidiana");
        startActivity(e);
    }

    public void principiante(View view)
    {
        Intent p=new Intent(ArgomentiActivity.this, MainActivity.class);
        p.putExtra(CATEGORY_PARAM_NAME, "principiante");
        startActivity(p);
    }

    public void base(View view)
    {
        Intent b=new Intent(ArgomentiActivity.this, MainActivity.class);
        b.putExtra(CATEGORY_PARAM_NAME, "base");
        startActivity(b);
    }

    public void intermedio(View view)
    {
        Intent i=new Intent(ArgomentiActivity.this, MainActivity.class);
        i.putExtra(CATEGORY_PARAM_NAME, "intermedio");
        startActivity(i);
    }

    public void esperto(View view)
    {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "esperto");
        startActivity(e);
    }

    public void home(View view)
    {
        Intent h=new Intent(ArgomentiActivity.this, StartActivity.class);
        startActivity(h);

    }
}
