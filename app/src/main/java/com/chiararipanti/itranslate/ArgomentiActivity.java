package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    //****************variabili per il bunner pubblicitario***************************
    /** The log tag. */
    //private static final String LOG_TAG = "BannerAdListener";
    /** The view to show the ad. */
    private AdView adView;
    //********************fine bunner pubblicitario******************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argomenti);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());

        mcontext=getApplicationContext();

        record_vq=(TextView) findViewById(R.id.record_vq);
        record_a=(TextView) findViewById(R.id.record_a);
        record_v=(TextView) findViewById(R.id.record_v);
        record_c=(TextView) findViewById(R.id.record_c);

        partite_vq=(TextView) findViewById(R.id.partite_giocate_vq);
        partite_a=(TextView) findViewById(R.id.partite_giocate_a);
        partite_v=(TextView) findViewById(R.id.partite_giocate_v);
        partite_c=(TextView) findViewById(R.id.partite_giocate_c);

        recordVQ=session.getRecord(getString(R.string.vita_quotidiana));
        record_vq.setText(getString(R.string.record)+": "+recordVQ);

        recordA=session.getRecord(getString(R.string.animali));
        record_a.setText(getString(R.string.record)+": "+recordA);

        recordV=session.getRecord(getString(R.string.viaggi));
        record_v.setText(getString(R.string.record)+": "+recordV);

        recordC=session.getRecord(getString(R.string.cibo));
        record_c.setText(getString(R.string.record)+": "+recordC);


        partiteVQ=session.getPartite(getString(R.string.vita_quotidiana));
        partite_vq.setText(getString(R.string.partite)+": "+partiteVQ);

        partiteA=session.getPartite(getString(R.string.animali));
        partite_a.setText(getString(R.string.partite)+": "+partiteA);

        partiteV=session.getPartite(getString(R.string.viaggi));
        partite_v.setText(getString(R.string.partite)+": "+partiteV);

        partiteC=session.getPartite(getString(R.string.cibo));
        partite_c.setText(getString(R.string.partite)+": "+partiteC);


        record_p=(TextView) findViewById(R.id.record_p);
        record_b=(TextView) findViewById(R.id.record_b);
        record_i=(TextView) findViewById(R.id.record_i);
        record_e=(TextView) findViewById(R.id.record_e);

        partite_p=(TextView) findViewById(R.id.partite_giocate_p);
        partite_b=(TextView) findViewById(R.id.partite_giocate_b);
        partite_i=(TextView) findViewById(R.id.partite_giocate_i);
        partite_e=(TextView) findViewById(R.id.partite_giocate_e);

        recordP=session.getRecord(getString(R.string.principiante));
        record_p.setText(getString(R.string.record)+": "+recordP);

        recordB=session.getRecord(getString(R.string.base));
        record_b.setText(getString(R.string.record)+": "+recordB);

        recordI=session.getRecord(getString(R.string.intermedio));
        record_i.setText(getString(R.string.record)+": "+recordI);

        recordE=session.getRecord(getString(R.string.esperto));
        record_e.setText(getString(R.string.record)+": "+recordE);


        partiteP=session.getPartite(getString(R.string.principiante));
        partite_p.setText(getString(R.string.partite)+": "+partiteP);

        partiteB=session.getPartite(getString(R.string.base));
        partite_b.setText(getString(R.string.partite)+": "+partiteB);

        partiteI=session.getPartite(getString(R.string.intermedio));
        partite_i.setText(getString(R.string.partite)+": "+partiteI);

        partiteE=session.getPartite(getString(R.string.esperto));
        partite_e.setText(getString(R.string.partite)+": "+partiteE);

        //****************inserimento bunner pubblicitario***************************
        //create adView
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.unit_id));
        // Add the AdView to the view hierarchy.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.footer);
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
                .build();
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        //******************  FINE  bunner pubblicitario***************************
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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
        Intent e=new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra("categoria", "animali");
        e.putExtra("categoria1", (getString(R.string.animali)));
        startActivity(e);
    }

    public void cibo(View view)
    {
        Intent e=new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra("categoria", "cibo");
        e.putExtra("categoria1", (getString(R.string.cibo)));
        startActivity(e);
    }

    public void viaggi(View view)
    {
        Intent e=new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra("categoria", "viaggi");
        e.putExtra("categoria1", (getString(R.string.viaggi)));
        startActivity(e);
    }

    public void vitaquotidiana(View view)
    {
        Intent e=new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra("categoria", "vita_quotidiana");
        e.putExtra("categoria1", (getString(R.string.vita_quotidiana)));
        startActivity(e);
    }

    public void principiante(View view)
    {
        Intent p=new Intent(ArgomentiActivity.this, MainActivity.class);
        p.putExtra("categoria", "principiante");
        p.putExtra("categoria1", (getString(R.string.principiante)));
        startActivity(p);
    }

    public void base(View view)
    {
        Intent b=new Intent(ArgomentiActivity.this, MainActivity.class);
        b.putExtra("categoria", "base");
        b.putExtra("categoria1", (getString(R.string.base)));
        startActivity(b);
    }

    public void intermedio(View view)
    {
        Intent i=new Intent(ArgomentiActivity.this, MainActivity.class);
        i.putExtra("categoria", "intermedio");
        i.putExtra("categoria1", (getString(R.string.intermedio)));
        startActivity(i);
    }

    public void esperto(View view)
    {
        Intent e=new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra("categoria", "esperto");
        e.putExtra("categoria1", (getString(R.string.esperto)));
        startActivity(e);
    }

    public void home(View view)
    {
        Intent h=new Intent(ArgomentiActivity.this, StartActivity.class);
        startActivity(h);

    }
}
