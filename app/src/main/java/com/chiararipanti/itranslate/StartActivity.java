package com.chiararipanti.itranslate;

import java.util.ArrayList;
import java.util.Locale;

import com.chiararipanti.itranslate.db.GetWordsFromDataSource;
import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/*
 * Entry point of the App
 *
 *
 * TODO: cambia nomi variabili e metodi e risorse
 *  - Rivedi Codice (Rinomina variabili/metodi)
 */

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class StartActivity extends Activity {

    /**
     * Variable Declarations
     */
    private String TAG = "StartActivity";
    SessionManager session;
    Boolean isSoundEnabled;
    MyConnectivityManager connectivityManager;
    AlertDialogManager alertDialog;
    Button canzoniQuizButton;
    Context mcontext;
    ImageButton suono_button;
    FirebaseAuth mAuth;
    EnglishGameUtility gameUtils;

    /**
     * Manage User Anonimous Auth
     */
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) or register him.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                            } else {
                                // Sign in fails,
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                            }
                        }
                    });
        }

        canzoniQuizButton = findViewById(R.id.quizm);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up page layout
        setContentView(R.layout.activity_start);
        connectivityManager=new MyConnectivityManager(getApplicationContext());

        gameUtils = new EnglishGameUtility(this);
        gameUtils.addAdBunner();

        alertDialog=new AlertDialogManager();

        //Game Mode custom for Italian Users
        if(!Locale.getDefault().getLanguage().equals("it")) {
            canzoniQuizButton.setVisibility(View.GONE);
        }
        mcontext=getApplicationContext();
        suono_button=findViewById(R.id.suono);
        session = new SessionManager(getApplicationContext());
        isSoundEnabled = session.getSuono();

        if(isSoundEnabled)
            suono_button.setImageResource(R.drawable.sound);
        else
            suono_button.setImageResource(R.drawable.no_sound);
    }

    public void quizm(View view){
        if(connectivityManager.check()){
            Intent intent=new Intent(this,MusicActivity.class);
            startActivity(intent);
        }else {
            //FIXME: Metti messaggio nel file delle stringhe
            alertDialog.showAlertDialog(StartActivity.this, "OPS!", getString(R.string.attiva_connessione), false);
        }
    }

    public void quizf(View view){
        Toast.makeText(getApplicationContext(),"Quiz film" , Toast.LENGTH_SHORT).show();
    }

    public void impara(View view){
        Intent intent=new Intent(this,LearnSubjectsActivity.class);
        intent.putExtra("action", "impara");
        startActivity(intent);
    }

    public void quizp(View view){
        Intent intent=new Intent(this,MainGameSubjectActivity.class);
        startActivity(intent);
    }

    public void quizt(View view){
        Intent e=new Intent(getApplicationContext(), LearnSubjectsActivity.class);
        e.putExtra("action", "traduci");
        startActivity(e);
    }

    public void speech(View view){

        if(connectivityManager.check()){
            Intent e=new Intent(StartActivity.this, SpeechActivity.class);
            startActivity(e);
        }else {
            //FIXME: Metti messaggio nel file delle stringhe
            alertDialog.showAlertDialog(StartActivity.this, "OPS!", getString(R.string.attiva_connessione), false);
        }
    }

    public void quiz(View view){

        if(connectivityManager.check()){
            Intent e=new Intent(StartActivity.this, TestSubjectsActivity.class);
            startActivity(e);
        }else {
            //FIXME: Metti messaggio nel file delle stringhe
            alertDialog.showAlertDialog(StartActivity.this, "OPS!", getString(R.string.attiva_connessione), false);
        }
    }

    public void rate(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getString(R.string.package_app_name))));
    }

    public void volume(View view){

        if(isSoundEnabled = session.getSuono()){
            session.setSuono(false);
            isSoundEnabled = false;
            suono_button.setImageResource(R.drawable.no_sound);
        }else{
            session.setSuono(true);
            isSoundEnabled = true;
            suono_button.setImageResource(R.drawable.sound);
        }
    }
}
