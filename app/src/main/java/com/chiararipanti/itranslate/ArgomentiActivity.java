package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.SessionManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class ArgomentiActivity extends Activity {

    SessionManager session;

    private static String CATEGORY_PARAM_NAME = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argomenti);

        session = new SessionManager(getApplicationContext());
        EnglishGameUtility gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        TextView record_vq = findViewById(R.id.record_vq);
        TextView record_a = findViewById(R.id.record_a);
        TextView record_v = findViewById(R.id.record_v);
        TextView record_c = findViewById(R.id.record_c);

        TextView partite_vq = findViewById(R.id.partite_giocate_vq);
        TextView partite_a = findViewById(R.id.partite_giocate_a);
        TextView partite_v = findViewById(R.id.partite_giocate_v);
        TextView partite_c = findViewById(R.id.partite_giocate_c);

        float recordVQ = session.getRecord(getString(R.string.vita_quotidiana));
        //record_vq.setText((R.string.record, recordVQ));

        float recordA = session.getRecord(getString(R.string.animali));
        //record_a.setText(getString(R.string.record, recordA));

        float recordV = session.getRecord(getString(R.string.viaggi));
        //record_v.setText(getString(R.string.record, recordV));

        float recordC = session.getRecord(getString(R.string.cibo));
        //record_c.setText(getString(R.string.record, recordC));


        int partiteVQ = session.getPartite(getString(R.string.vita_quotidiana));
        partite_vq.setText(getString(R.string.partite, partiteVQ));

        int partiteA = session.getPartite(getString(R.string.animali));
        partite_a.setText(getString(R.string.partite, partiteA));

        int partiteV = session.getPartite(getString(R.string.viaggi));
        partite_v.setText(getString(R.string.partite, partiteV));

        int partiteC = session.getPartite(getString(R.string.cibo));
        partite_c.setText(getString(R.string.partite, partiteC));


        TextView record_p = findViewById(R.id.record_p);
        TextView record_b = findViewById(R.id.record_b);
        TextView record_i = findViewById(R.id.record_i);
        TextView record_e = findViewById(R.id.record_e);

        TextView partite_p = findViewById(R.id.partite_giocate_p);
        TextView partite_b = findViewById(R.id.partite_giocate_b);
        TextView partite_i = findViewById(R.id.partite_giocate_i);
        TextView partite_e = findViewById(R.id.partite_giocate_e);

        float recordP = session.getRecord(getString(R.string.principiante));
        //record_p.setText(getString(R.string.record, recordP));

        float recordB = session.getRecord(getString(R.string.base));
        //record_b.setText(getString(R.string.record, recordB));

        float recordI = session.getRecord(getString(R.string.intermedio));
        //record_i.setText(getString(R.string.record, recordI));

        float recordE = session.getRecord(getString(R.string.esperto));
        //record_e.setText(getString(R.string.record, recordE));


        float partiteP = session.getPartite(getString(R.string.principiante));
        partite_p.setText(getString(R.string.partite, partiteP));

        float partiteB = session.getPartite(getString(R.string.base));
        partite_b.setText(getString(R.string.partite, partiteB));

        float partiteI = session.getPartite(getString(R.string.intermedio));
        partite_i.setText(getString(R.string.partite, partiteI));

        float partiteE = session.getPartite(getString(R.string.esperto));
        partite_e.setText(getString(R.string.partite, partiteE));
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


    public void animali(View view) {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "animali");
        startActivity(e);
    }

    public void cibo(View view) {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "cibo");
        startActivity(e);
    }

    public void viaggi(View view) {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "viaggi");
        startActivity(e);
    }

    public void vitaquotidiana(View view) {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "vita_quotidiana");
        startActivity(e);
    }

    public void principiante(View view) {
        Intent p = new Intent(ArgomentiActivity.this, MainActivity.class);
        p.putExtra(CATEGORY_PARAM_NAME, "principiante");
        startActivity(p);
    }

    public void base(View view) {
        Intent b = new Intent(ArgomentiActivity.this, MainActivity.class);
        b.putExtra(CATEGORY_PARAM_NAME, "base");
        startActivity(b);
    }

    public void intermedio(View view) {
        Intent i = new Intent(ArgomentiActivity.this, MainActivity.class);
        i.putExtra(CATEGORY_PARAM_NAME, "intermedio");
        startActivity(i);
    }

    public void esperto(View view) {
        Intent e = new Intent(ArgomentiActivity.this, MainActivity.class);
        e.putExtra(CATEGORY_PARAM_NAME, "esperto");
        startActivity(e);
    }

    public void home(View view) {
        Intent h = new Intent(ArgomentiActivity.this, StartActivity.class);
        startActivity(h);

    }
}
