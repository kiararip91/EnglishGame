package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImparaargomentiActivity extends Activity {
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
        setContentView(R.layout.activity_imparaargomenti);

        principianteb = findViewById(R.id.principiante);
        baseb = findViewById(R.id.base);
        intermediob = findViewById(R.id.intermedio);
        espertob = findViewById(R.id.esperto);
        animalib = findViewById(R.id.animali);
        viaggib = findViewById(R.id.viaggi);
        vitab = findViewById(R.id.vita_quotidiana);
        cibob = findViewById(R.id.cibo);

        connectivityManager=new MyConnectivityManager(getApplicationContext());

        Intent intent=getIntent();
        action=intent.getStringExtra("action");

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //suono_button=(ImageButton)(findViewById(R.id.suono));
        session = new SessionManager(getApplicationContext());
	    /*suono=session.getSuono();
	    if(suono)
	    	suono_button.setImageResource(R.drawable.sound);
	    else
	    	suono_button.setImageResource(R.drawable.no_sound);*/



        //****************inserimento bunner pubblicitario***************************
        //create adView
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.unit_id));
        // Add the AdView to the view hierarchy.
        RelativeLayout layout = findViewById(R.id.footer);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    public void principiante(View view)
    {
        if(connectivityManager.check())
        {
            Intent p;
            if(action.equals("impara"))
                p=new Intent(ImparaargomentiActivity.this, ImparaActivity.class);
            else
                p=new Intent(ImparaargomentiActivity.this, TraduzioneActivity.class);
            p.putExtra("categoria1", getString(R.string.principiante));
            p.putExtra("categoria", "principiante");
            startActivity(p);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();

    }

    public void base(View view)
    {
        if(connectivityManager.check())
        {

            Intent b;
            if(action.equals("impara"))
                b=new Intent(ImparaargomentiActivity.this, ImparaActivity.class);
            else
                b=new Intent(ImparaargomentiActivity.this, TraduzioneActivity.class);
            b.putExtra("categoria1", getString(R.string.base));
            b.putExtra("categoria", "base");
            startActivity(b);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();

    }

    public void intermedio(View view)
    {
        if(connectivityManager.check())
        {

            Intent i;
            if(action.equals("impara"))
                i=new Intent(ImparaargomentiActivity.this, ImparaActivity.class);
            else
                i=new Intent(ImparaargomentiActivity.this, TraduzioneActivity.class);
            i.putExtra("categoria1", getString(R.string.intermedio));
            i.putExtra("categoria", "intermedio");
            startActivity(i);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();

    }

    public void esperto(View view)
    {
        if(connectivityManager.check())
        {

            Intent e;
            if(action.equals("impara"))
                e=new Intent(ImparaargomentiActivity.this, ImparaActivity.class);
            else
                e=new Intent(ImparaargomentiActivity.this, TraduzioneActivity.class);
            e.putExtra("categoria1", getString(R.string.esperto));
            e.putExtra("categoria", "esperto");
            startActivity(e);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();

    }

    public void cibo(View view)
    {
        if(connectivityManager.check())
        {

            Intent e;
            if(action.equals("impara"))
                e=new Intent(ImparaargomentiActivity.this, ImparaActivity.class);
            else
                e=new Intent(ImparaargomentiActivity.this, TraduzioneActivity.class);
            e.putExtra("categoria1", getString(R.string.cibo));
            e.putExtra("categoria", "cibo");
            startActivity(e);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();

    }

    public void viaggi(View view)
    {
        if(connectivityManager.check())
        {

            Intent e;
            if(action.equals("impara"))
                e=new Intent(ImparaargomentiActivity.this, ImparaActivity.class);
            else
                e=new Intent(ImparaargomentiActivity.this, TraduzioneActivity.class);
            e.putExtra("categoria1", getString(R.string.viaggi));
            e.putExtra("categoria", "viaggi");
            startActivity(e);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();

    }

    public void vita(View view)
    {
        if(connectivityManager.check())
        {

            Intent e;
            if(action.equals("impara"))
                e=new Intent(ImparaargomentiActivity.this, ImparaActivity.class);
            else
                e=new Intent(ImparaargomentiActivity.this, TraduzioneActivity.class);
            e.putExtra("categoria1", getString(R.string.vita_quotidiana));
            e.putExtra("categoria", "vita_quotidiana");
            startActivity(e);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();

    }

    public void animali(View view)
    {
        if(connectivityManager.check())
        {
            Intent e;
            if(action.equals("impara"))
                e=new Intent(ImparaargomentiActivity.this, ImparaActivity.class);
            else
                e=new Intent(ImparaargomentiActivity.this, TraduzioneActivity.class);
            e.putExtra("categoria1", getString(R.string.animali));
            e.putExtra("categoria", "animali");
            startActivity(e);
        }
        else
            Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();

    }

	/*public void rate(View view)
	{
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getString(R.string.package_app_name))));

	}

	public void volume(View view)
	{
		if(suono=session.getSuono())
		{
			session.setSuono(false);
			suono=false;
			suono_button.setImageResource(R.drawable.no_sound);
		}
		else
		{
			session.setSuono(true);
			suono=true;
			suono_button.setImageResource(R.drawable.sound);

		}

	}

	public void home(View view)
	{
		if(connectivityManager.check())
		{

		Intent h=new Intent(ImparaargomentiActivity.this, StartActivity.class);
		startActivity(h);
		}
		else
			Toast.makeText(getApplicationContext(),getString(R.string.attiva_connessione) , Toast.LENGTH_SHORT).show();


	}*/
}
