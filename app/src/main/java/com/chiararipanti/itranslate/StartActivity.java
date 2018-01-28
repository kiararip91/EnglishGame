package com.chiararipanti.itranslate;

import java.util.Locale;
import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StartActivity extends Activity {

    SessionManager session;
    Boolean suono;
    MyConnectivityManager connectivityManager;
    AlertDialogManager alertDialog;
    String TAG="StartActivity";
    Button paroleQuizButton;
    Button filmQuizButton;
    Button canzoniQuizButton;
    Button imparaButton;
    Context mcontext;
    ImageButton suono_button;

    //****************variabili per il bunner pubblicitario***************************
    /** The view to show the ad. */
    private AdView adView;
    //********************fine bunner pubblicitario******************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        connectivityManager=new MyConnectivityManager(getApplicationContext());
        alertDialog=new AlertDialogManager();
        paroleQuizButton=(Button)(findViewById(R.id.quizp));
        canzoniQuizButton=(Button)(findViewById(R.id.quizm));

        if(!Locale.getDefault().getLanguage().equals("it"))
            canzoniQuizButton.setVisibility(View.GONE);

        imparaButton=(Button)(findViewById(R.id.impara));
        mcontext=getApplicationContext();
        suono_button=(ImageButton)(findViewById(R.id.suono));
        session = new SessionManager(getApplicationContext());
        suono=session.getSuono();

        if(suono)
            suono_button.setImageResource(R.drawable.sound);
        else
            suono_button.setImageResource(R.drawable.no_sound);

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

    public void quizm(View view){
        if(connectivityManager.check()){
            Intent intent=new Intent(this,MusicaActivity.class);
            startActivity(intent);
        }else
            alertDialog.showAlertDialog(StartActivity.this, "OPS!", getString(R.string.attiva_connessione), false);
    }

    public void quizf(View view){
        Toast.makeText(getApplicationContext(),"Quiz film" , Toast.LENGTH_SHORT).show();
    }

    public void impara(View view){
        Intent intent=new Intent(this,ImparaargomentiActivity.class);
        intent.putExtra("action", "impara");
        startActivity(intent);
    }

    public void quizp(View view){
        Intent intent=new Intent(this,ArgomentiActivity.class);
        startActivity(intent);
    }

    public void quizt(View view){
        Intent e=new Intent(StartActivity.this, ImparaargomentiActivity.class);
        e.putExtra("action", "traduci");
        startActivity(e);
    }

    public void speech(View view){

        if(connectivityManager.check()){
            Intent e=new Intent(StartActivity.this, SpeechActivity.class);
            startActivity(e);
        }else
            alertDialog.showAlertDialog(StartActivity.this, "OPS!", getString(R.string.attiva_connessione), false);
    }

    public void quiz(View view){

        if(connectivityManager.check()){
            Intent e=new Intent(StartActivity.this, QuizLivelliActivity.class);
            startActivity(e);
        }else
            alertDialog.showAlertDialog(StartActivity.this, "OPS!", getString(R.string.attiva_connessione), false);
    }

    public void rate(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getString(R.string.package_app_name))));
    }

    public void volume(View view){

        if(suono=session.getSuono()){
            session.setSuono(false);
            suono=false;
            suono_button.setImageResource(R.drawable.no_sound);
        }else{
            session.setSuono(true);
            suono=true;
            suono_button.setImageResource(R.drawable.sound);
        }
    }
}
