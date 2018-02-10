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


    public void goToLearn(View view) {
        if (connectivityManager.check()) {

            int buttonId = view.getId();
            int level = 0;

            switch (buttonId){
                case R.id.naive:
                    level = 1;
                    break;
                case R.id.base:
                    level = 2;
                    break;
                case R.id.intermediate:
                    level = 3;
                    break;
                case R.id.expert:
                    level = 4;
                case R.id.animal:
                    level = 5;
                case R.id.life:
                    level = 6;
                case R.id.food:
                    level = 7;
                    break;
                case R.id.travel:
                    level = 8;
                    break;

            }

            Button pushedButton = (Button) view;
            String levelName  = pushedButton.getText().toString().toLowerCase().replace(" ", "_");

            Intent detinationIntent;
            if (action.equals("impara")) {
                detinationIntent = new Intent(getApplicationContext(), LearnActivity.class);
            }
            else {
                detinationIntent = new Intent(getApplicationContext(), TranslationActivity.class);
            }
            detinationIntent.putExtra("category", levelName);
            detinationIntent.putExtra("level", level + "");
            startActivity(detinationIntent);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
    }
}