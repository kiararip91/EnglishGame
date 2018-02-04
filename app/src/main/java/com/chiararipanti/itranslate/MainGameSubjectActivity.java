package com.chiararipanti.itranslate;

import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.util.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// TODO: Cambiare sul db le categorie (?) Risolvi stringhe Parametrizzate

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class MainGameSubjectActivity extends Activity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game_subject);

        session = new SessionManager(getApplicationContext());
        EnglishGameUtility gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        /*
         * Setting up Layout
         */
        TextView recordLifeTextView = findViewById(R.id.record_life);
        TextView recordAnimalsTextView = findViewById(R.id.record_animals);
        TextView recordTravelTextView = findViewById(R.id.record_travel);
        TextView recordFoodTextView = findViewById(R.id.record_food);

        TextView gameNumberLifeTextView = findViewById(R.id.game_number_life);
        TextView gameNumberAnimalsTextView = findViewById(R.id.game_number_animals);
        TextView gameNumberTravelTextView = findViewById(R.id.game_number_travel);
        TextView gameNumberFoodTextView = findViewById(R.id.game_number_food);

        float recordLifeValue = session.getRecord(getString(R.string.vita_quotidiana));
        recordLifeTextView.setText(getString(R.string.record, recordLifeValue));

        float recordAnimalValue = session.getRecord(getString(R.string.animali));
        recordAnimalsTextView.setText(getString(R.string.record, recordAnimalValue));

        float recordTravelValue = session.getRecord(getString(R.string.viaggi));
        recordTravelTextView.setText(getString(R.string.record, recordTravelValue));

        float recordFoodValue = session.getRecord(getString(R.string.cibo));
        recordFoodTextView.setText(getString(R.string.record, recordFoodValue));


        int gameNumberLifeValue = session.getPartite(getString(R.string.vita_quotidiana));
        gameNumberLifeTextView.setText(getString(R.string.partite, gameNumberLifeValue));

        int gameNumberAnimalsvalue = session.getPartite(getString(R.string.animali));
        gameNumberAnimalsTextView.setText(getString(R.string.partite, gameNumberAnimalsvalue));

        int gameNumberTravelValue = session.getPartite(getString(R.string.viaggi));
        gameNumberTravelTextView.setText(getString(R.string.partite, gameNumberTravelValue));

        int gameNumberFoodValue = session.getPartite(getString(R.string.cibo));
        gameNumberFoodTextView.setText(getString(R.string.partite, gameNumberFoodValue));

        TextView recordNaiveTextView = findViewById(R.id.record_naive);
        TextView recordBaseTextView = findViewById(R.id.record_base);
        TextView recordIntermediateTextView = findViewById(R.id.record_intermediate);
        TextView recordExpertTextView = findViewById(R.id.record_expert);

        TextView gameNumberNaiveTextView = findViewById(R.id.game_number_naive);
        TextView gameNumberBaseTextView = findViewById(R.id.game_number_base);
        TextView gameNumberIntermediateTextView = findViewById(R.id.game_number_intermediate);
        TextView gameNumberExpertTextView = findViewById(R.id.game_number_expert);

        float recordNaiveValue = session.getRecord(getString(R.string.principiante));
        recordNaiveTextView.setText(getString(R.string.record, recordNaiveValue));

        float recordBaseValue = session.getRecord(getString(R.string.base));
        recordBaseTextView.setText(getString(R.string.record, recordBaseValue));

        float recordIntermediateValue = session.getRecord(getString(R.string.intermedio));
        recordIntermediateTextView.setText(getString(R.string.record, recordIntermediateValue));

        float recordExpertValue = session.getRecord(getString(R.string.esperto));
        recordExpertTextView.setText(getString(R.string.record, recordExpertValue));


        float gameNumbersNaiveValue = session.getPartite(getString(R.string.principiante));
        //gameNumberNaiveTextView.setText(getString(R.string.partite, gameNumbersNaiveValue));

        float gameNumbersBaseValue = session.getPartite(getString(R.string.base));
        //gameNumberBaseTextView.setText(getString(R.string.partite, gameNumbersBaseValue));

        float gameNumbersIntermediateValue = session.getPartite(getString(R.string.intermedio));
        //gameNumberIntermediateTextView.setText(getString(R.string.partite, gameNumbersIntermediateValue));

        float gameNumbersExpertValue = session.getPartite(getString(R.string.esperto));
        //gameNumberExpertTextView.setText(getString(R.string.partite, gameNumbersExpertValue));

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

    public void home(View view) {
        Intent homeIntentActivity = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(homeIntentActivity);
    }

    public void goToGame(View view) {
        Button pushedButton = (Button) view;
        String category  = pushedButton.getText().toString().toLowerCase().replace(" ", "_");
        Intent gameIntentActivity = new Intent(getApplicationContext() , MainGameActivity.class);
        String CATEGORY_PARAM_NAME = "category";
        gameIntentActivity.putExtra(CATEGORY_PARAM_NAME, category);
        startActivity(gameIntentActivity);
    }

    /*private String mapCategory(String englishCategory) {
        String mappedCategory = null;

        switch (englishCategory) {
            case "expert":
                mappedCategory = "esperto";
                break;
            case "intermediate":
                mappedCategory = "intermedio";
                break;
            case "base":
                mappedCategory = "base";
                break;
            case "naive":
                mappedCategory = "principiante";
                break;
            case "life":
                mappedCategory = "vita_quotidiana";
                break;
            case "travel":
                mappedCategory = "viaggi";
                break;
            case "food":
                mappedCategory = "cibo";
                break;
            case "animal":
                mappedCategory = "animali";
                break;

            default:
                mappedCategory = null;
        }
        return mappedCategory;
    }*/
}