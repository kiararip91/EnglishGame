package com.chiararipanti.itranslate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.chiararipanti.itranslate.model.Word;
import com.chiararipanti.itranslate.util.AlertDialogManager;
import com.chiararipanti.itranslate.util.AudioRequest;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.db.GetWordsFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class MainGameActivity extends Activity {

    private final String CATEGORY_PARAM_NAME = "category";

    /**
     * Layout Elements
     */
    private LinearLayout firstLineOfRandomButtons;
    private LinearLayout secondLineOfRandomButtons;

    private LinearLayout firstLineOfSelectedButtons;
    private LinearLayout secondLineOfSelectedButtons;

    private TextView errorsTextView;
    private TextView sentenceTextView;
    private TextView pointTextView;
    private TextView englishWordTextView;

    private ImageButton helpButton;
    private ImageButton solutionButton;

    private LinearLayout rightAnswerLayout;
    private LinearLayout gameOverLayout;
    private LinearLayout mainContent;

    /**
     * Game Properties
     */
    private EnglishGameUtility gameUtils;
    private List<Word> words;
    private int index;
    private Word word;

    private float record;
    private float points;
    private int errors;
    private int proportion;
    private boolean secondLineNeeded;
    private int helpsNumber;
    int solutionsNumber;

    private SessionManager session;

    /**
     * Da Capire se si possono ottimizzare tutte queste altre variabili
     */
    private final String TAG = "MainGameActivity";
    private boolean listened;
    private Character previousCharacter;
    private HashMap<Integer, Button> pushedButtonList;
    private String pushedButtonString = "";

    //array nel quale memorizzo gli identificatori delle lettere premuti
    private ArrayList<Integer> pushedButtonsArray;

    private ArrayList<String> selectedWord;
    private String category;
    private AlertDialogManager alertDialog;
    private MyConnectivityManager connectivityManager;
    private ArrayList<Button> buttonArrayList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        alertDialog = new AlertDialogManager();
        AlertDialogManager settingDialog = new AlertDialogManager();
        Intent intent = getIntent();
        category = intent.getStringExtra(CATEGORY_PARAM_NAME);
        connectivityManager = new MyConnectivityManager(getApplicationContext());

        //DA CAPIRE
        Display display;
        display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        proportion = size.x / 9;

        pushedButtonList = new HashMap<>();
        pushedButtonsArray = new ArrayList<>();


        /**
         * Checked
         */

        //Initialize Activity
        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        session = new SessionManager(getApplicationContext());
        record = session.getRecord(category);

        //Buttons Container Layout
        firstLineOfRandomButtons = findViewById(R.id.firstRandomButtonsLine);
        secondLineOfRandomButtons = findViewById(R.id.secondRandomButtonsLine);
        firstLineOfSelectedButtons = findViewById(R.id.firstSelectedButtonsLine);
        secondLineOfSelectedButtons = findViewById(R.id.secondSelectedButtonsLine);

        //Main Container Layout
        mainContent = findViewById(R.id.contentLayout);

        //Set Level On the Top
        TextView levelTextView = findViewById(R.id.level);
        String categoryUpperCaseFirstLetter = Character.toUpperCase(category.charAt(0)) + category.substring(1);
        levelTextView.setText(getString(R.string.livello) + ": " + categoryUpperCaseFirstLetter.replace("_", " "));

        //Initialize Sentence Help TextView
        sentenceTextView = findViewById(R.id.sentence);
        sentenceTextView.setText("");

        //Initialize Errors TextView
        errorsTextView = findViewById(R.id.errors);

        //Initialize Points TextView, Help TextView, ShowSolution TextView, EnglishWord TextView
        pointTextView = findViewById(R.id.points);
        helpButton = findViewById(R.id.helpButton);
        solutionButton = findViewById(R.id.showSolution);
        englishWordTextView = findViewById(R.id.englishWord);

        //Initialize Record TextView
        TextView recordTextView = findViewById(R.id.record);
        recordTextView.setText(getString(R.string.tuo_record1) + " " + record);

        //Initialize Hidden Layout
        rightAnswerLayout = findViewById(R.id.rigthAnswerLayout);
        rightAnswerLayout.setVisibility(View.GONE);
        gameOverLayout = findViewById(R.id.gameoverLayout);
        gameOverLayout.setVisibility(View.GONE);



        //Initialize Variables
        secondLineNeeded = false;
        helpsNumber = 0;
        solutionsNumber = 0;
        errors = 0;
        points = 0;

        //Start Game
        this.getWordsAndSetLetters();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            gameUtils.manageSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieve a list of word form DataSource
     */
    private void getWordsAndSetLetters() {
        List<Word> words = new ArrayList<>();

        if (!connectivityManager.check()) {
            Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(this, StartActivity.class);
            startActivity(intent1);
        } else {
            this.index = 0;
            GetWordsFromDB getvocTask = new GetWordsFromDB(category);
            words = new ArrayList<>();
            try {
                getvocTask.execute();
                words = getvocTask.get();
            } catch (Exception e) {
                Log.e(TAG, "Error in retriving vocaboli " + e.getMessage());
                alertDialog.showAlertDialog(MainGameActivity.this, "OPS!", getString(R.string.errore), false);
            }
        }
        this.setLetters(words, this.index);
    }

    public void setLetters(List<Word> words, int index) {
        this.word = words.get(index);
        this.listened = false;
        //ripristino le visibilta dei layout
        firstLineOfRandomButtons.setVisibility(View.VISIBLE);
        secondLineOfRandomButtons.setVisibility(View.VISIBLE);
        pointTextView.setText(getString(R.string.punteggio) + points);
        errorsTextView.setText(getString(R.string.errori) + errors);
        firstLineOfRandomButtons.removeAllViews();
        secondLineOfRandomButtons.removeAllViews();
        firstLineOfSelectedButtons.removeAllViews();
        secondLineOfSelectedButtons.removeAllViews();
        this.pushedButtonsArray = new ArrayList<>();
        this.secondLineNeeded = false;
        this.solutionButton.setClickable(true);
        helpButton.setClickable(true);
        rightAnswerLayout.setVisibility(View.GONE);

        //array di botton inizialmente neri
        buttonArrayList = new ArrayList<>();

        //la parola che si sta formando in italiano clicckando sulle lettere
        selectedWord = new ArrayList<>();

        //lettere di cui e composta la parola in italiano
        String[] nativeWords = this.word.getNativeTranslation().split(",");

        //prendo la prima traduzione, ignoro i sinonimi
        final String nativeWord = nativeWords[0];

        //estraggo le lettere dalla parola
        String[] letters = nativeWord.toLowerCase().split("(?!^)");
        int numberOfLetters = letters.length;

        if (numberOfLetters > 8)
            secondLineNeeded = true;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, proportion);
        lp.weight = 1;
        lp.leftMargin = 5;
        lp.rightMargin = 5;

        //Generation buttons related to the extraced word
        for (int i = 0; i < numberOfLetters; i++) {
            final int indice = i;

            if (!letters[i].equalsIgnoreCase(" ")) {
                final Button img = new Button(getApplicationContext());
                img.setId(indice);
                img.setLayoutParams(lp);
                final String carattere = letters[i];
                String sfondo_lettera = gameUtils.substituteSpecialChar(letters[i].charAt(0));
                int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                img.setBackground(getResources().getDrawable(j));

                //Manage button letter click
                img.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int letterNumber = selectedWord.size();

                        //Song is not ended
                        if (letterNumber < nativeWord.length() - 1) {
                            Character car_italiano = nativeWord.charAt(selectedWord.size());
                            //The selected letter is not a space
                            if (!(String.valueOf(car_italiano).equalsIgnoreCase(" "))) {
                                selectedWord.add(carattere);
                                pushedButtonsArray.add(indice);
                                //estraggo il bottone nero successivo e gli cambio lo sfondo in base alla lettera selezionata
                                Button buttonLetterSolution = buttonArrayList.get(letterNumber);
                                String sfondo_lettera = gameUtils.substituteSpecialChar(carattere.charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                //Change the background of the Button-letter-solution to the chosen letter background
                                buttonLetterSolution.setBackground(getResources().getDrawable(w));

                                //Change che background of the Button-Random-Letter to Black and make in unclickable
                                img.setBackground(getResources().getDrawable(R.drawable.black));
                                img.setClickable(false);
                                v.setClickable(false);
                            }

                            //The selected letter is a space --> skip a button and change next square position
                            else {
                                selectedWord.add(" ");
                                selectedWord.add(carattere);
                                pushedButtonsArray.add(100);
                                pushedButtonsArray.add(indice);
                                Button buttonLetterSolution = buttonArrayList.get(letterNumber + 1);
                                String sfondo_lettera = gameUtils.substituteSpecialChar(carattere.charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                buttonLetterSolution.setBackground(getResources().getDrawable(w));
                                img.setBackground(getResources().getDrawable(R.drawable.black));
                                //img.setClickable(false);
                            }
                        }

                        //Last letter selected
                        else if (letterNumber == nativeWord.length() - 1) {
                            //Adding letter to the string word
                            selectedWord.add(carattere);
                            Log.v("onclick", carattere + " " + indice);
                            pushedButtonsArray.add(indice);
                            //Changing background of last button
                            Button buttonLetterSolution = buttonArrayList.get(letterNumber);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(carattere.charAt(0));
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            buttonLetterSolution.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));
                            //img.setClickable(false);

                            //Song-Convalidation
                            String stringToConvalidate = "";

                            for (int i = 0; i < selectedWord.size(); i++) {
                                stringToConvalidate = stringToConvalidate + (selectedWord.get(i));
                            }
                            if (nativeWord.equalsIgnoreCase(stringToConvalidate)) {
                                buttonLetterSolution.setClickable(false);
                                rightAnswerLayout.setVisibility(View.VISIBLE);

                                gameUtils.soundCorrect();
                                gameUtils.vibrate();

                                points = points + 1;
                                pointTextView.setText(getString(R.string.punteggio) + points);
                                firstLineOfRandomButtons.setVisibility(View.GONE);
                                secondLineOfRandomButtons.setVisibility(View.GONE);
                                sentenceTextView.setText("");

                                pushedButtonString = "";
                            } else {
                                errors = errors + 1;

                                //GAME OVER ----> oh no!
                                if (errors > 2) {
                                    gameUtils.soundOver();

                                    helpsNumber = 0;
                                    solutionsNumber = 0;

                                    gameUtils.vibrate();

                                    session.incrPartite(category);
                                    mainContent.setVisibility(View.GONE);
                                    helpButton.setClickable(false);
                                    solutionButton.setClickable(false);
                                    gameOverLayout.setVisibility(View.VISIBLE);

                                    if (isNewRecord(points)) {
                                        alertDialog.showAlertDialog(MainGameActivity.this, getString(R.string.nuovo_record_title), getString(R.string.nuovo_record) + points, false);
                                    }
                                } else {
                                    errorsTextView.setText(getString(R.string.errori) + errors);
                                }

                                gameUtils.soundWrong();
                                gameUtils.vibrate();
                            }

                            img.setClickable(false);

                            for (int i = 0; i < pushedButtonsArray.size(); i++) {
                                pushedButtonString += ", " + pushedButtonsArray.get(i);
                            }
                            pushedButtonString = "";
                        }
                    }

                });

                pushedButtonList.put(indice, img);

                //Generate black button
                final Button black = new Button(getApplicationContext());
                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(0, proportion);

                lpb.weight = 1;
                lpb.leftMargin = 5;
                lpb.rightMargin = 5;
                black.setLayoutParams(lpb);
                black.setBackground(getResources().getDrawable(R.drawable.black));
                black.setClickable(false);

                //Manage letter declick
                black.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //Last inserted letter selected --> REMOVE
                        if (indice == selectedWord.size() - 1) {
                            if (selectedWord.size() > 1)
                                previousCharacter = nativeWord.charAt(selectedWord.size() - 2);
                            else
                                previousCharacter = nativeWord.charAt(selectedWord.size() - 1);

                            //il carattere precedente alla precendente non e' uno spazio
                            if (!String.valueOf(previousCharacter).equalsIgnoreCase(" ")) {
                                black.setBackground(getResources().getDrawable(R.drawable.black));
                                int ripristina = pushedButtonsArray.get(indice);

                                for (int i = 0; i < pushedButtonsArray.size(); i++) {
                                    pushedButtonString += ", " + pushedButtonsArray.get(i);
                                }
                                pushedButtonString = "";
                                pushedButtonsArray.remove(indice);

                                for (int i = 0; i < pushedButtonsArray.size(); i++) {
                                    pushedButtonString += ", " + pushedButtonsArray.get(i);
                                }
                                pushedButtonString = "";
                                Button b = pushedButtonList.get(ripristina);
                                String sfondo_lettera = gameUtils.substituteSpecialChar(selectedWord.get(indice).toString().charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                b.setBackground(getResources().getDrawable(w));
                                b.setClickable(true);
                                selectedWord.remove(indice);

                            } else {
                                black.setBackground(getResources().getDrawable(R.drawable.black));

                                int ripristina = pushedButtonsArray.get(indice);

                                for (int i = 0; i < pushedButtonsArray.size(); i++) {
                                    pushedButtonString += ", " + pushedButtonsArray.get(i);
                                }

                                pushedButtonString = "";
                                pushedButtonsArray.remove(indice);
                                pushedButtonsArray.remove(pushedButtonsArray.indexOf(100));

                                for (int i = 0; i < pushedButtonsArray.size(); i++) {
                                    pushedButtonString += ", " + pushedButtonsArray.get(i);
                                }

                                pushedButtonString = "";
                                Button b = pushedButtonList.get(ripristina);
                                b.setClickable(true);
                                String sfondo_lettera = gameUtils.substituteSpecialChar(selectedWord.get(indice).charAt(0));
                                int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                                b.setBackground(getResources().getDrawable(w));
                                selectedWord.remove(indice);
                                selectedWord.remove(" ");
                            }
                        }
                    }
                });
                buttonArrayList.add(black);

                if (i < 8) {
                    firstLineOfSelectedButtons.addView(black);
                } else {
                    secondLineOfSelectedButtons.addView(black);
                }
            } else {
                //Space
                Button black = new Button(getApplicationContext());
                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(0, proportion);
                lpb.weight = 1;
                lpb.leftMargin = 5;
                lpb.rightMargin = 5;
                black.setLayoutParams(lpb);
                black.setBackgroundColor(Color.TRANSPARENT);
                black.setClickable(false);
                buttonArrayList.add(black);
                if (i < 8) {
                    firstLineOfSelectedButtons.addView(black);
                } else {
                    secondLineOfSelectedButtons.addView(black);
                }
            }
        }

        String randomLetters = getString(R.string.alfabeto);
        Random rnd = new Random();

        //Generating random letters (the total letters mut be 16)
        //Inserting fake white buttons to reach the correct square element dimension
        for (int i = numberOfLetters; i < 16; i++) {
            final Button fasullo = new Button(getApplicationContext());
            fasullo.setLayoutParams(lp);
            fasullo.setBackgroundColor(Color.TRANSPARENT);
            fasullo.setClickable(false);

            if (i < 8) {
                firstLineOfSelectedButtons.addView(fasullo);
            } else {
                if (secondLineNeeded) {
                    secondLineOfSelectedButtons.addView(fasullo);
                }
            }

            final int indicer = i;
            final Button img = new Button(getApplicationContext());
            img.setLayoutParams(lp);
            int randomGen = rnd.nextInt(randomLetters.length());
            final Character randomChar = randomLetters.charAt(randomGen);

            //Setting random numeber as id button
            img.setId(randomGen);

            //Click random letter
            img.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                public void onClick(View v) {
                    int letterNumber = selectedWord.size();

                    //non sto inserendo l'ultima lettera
                    if (letterNumber < nativeWord.length() - 1) {
                        Character car_italiano = nativeWord.charAt(selectedWord.size());

                        //la lettera corrispondente non e' uno spazio ma una casella nera
                        if (!(String.valueOf(car_italiano).equalsIgnoreCase(" "))) {
                            pushedButtonsArray.add(indicer);
                            selectedWord.add(randomChar.toString());
                            Button black_button = buttonArrayList.get(letterNumber);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            black_button.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));
                            img.setClickable(false);

                        } else {
                            //la lettera che devo inserire va dopo lo spazio
                            selectedWord.add(" ");
                            selectedWord.add(randomChar.toString());
                            pushedButtonsArray.add(100);
                            pushedButtonsArray.add(indicer);
                            Button black_button = buttonArrayList.get(letterNumber + 1);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
                            int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            black_button.setBackground(getResources().getDrawable(w));
                            img.setBackground(getResources().getDrawable(R.drawable.black));
                        }

                    } else if (letterNumber == nativeWord.length() - 1) {
                        //sto inserendo l'ultima lettera
                        pushedButtonsArray.add(indicer);
                        selectedWord.add(randomChar.toString());
                        Button black_button = buttonArrayList.get(letterNumber);
                        String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
                        int w = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                        black_button.setBackground(getResources().getDrawable(w));
                        img.setBackground(getResources().getDrawable(R.drawable.black));
                        String da_convalidare = "";

                        for (int i = 0; i < selectedWord.size(); i++) {
                            da_convalidare = da_convalidare + (selectedWord.get(i));
                        }

                        if (nativeWord.equalsIgnoreCase(da_convalidare)) {
                            black_button.setClickable(false);
                            rightAnswerLayout.setVisibility(View.VISIBLE);

                            gameUtils.soundCorrect();
                            gameUtils.vibrate();

                            points = points + 1;
                            pointTextView.setText(getString(R.string.punteggio) + points);
                            firstLineOfRandomButtons.setVisibility(View.GONE);
                            secondLineOfRandomButtons.setVisibility(View.GONE);

                            pushedButtonString = "";
                        } else {
                            errors = errors + 1;

                            //GAME OVER
                            if (errors > 2) {

                                gameUtils.soundOver();

                                helpsNumber = 0;
                                solutionsNumber = 0;
                                session.incrPartite(category);
                                mainContent.setVisibility(View.GONE);
                                helpButton.setClickable(false);
                                solutionButton.setClickable(false);
                                gameOverLayout.setVisibility(View.VISIBLE);

                                if (isNewRecord(points)) {
                                    alertDialog.showAlertDialog(MainGameActivity.this, getString(R.string.nuovo_record_title), getString(R.string.nuovo_record) + points, false);
                                }
                            } else {
                                errorsTextView.setText(getString(R.string.errori) + errors);
                            }

                            gameUtils.soundWrong();
                            gameUtils.vibrate();

                        }

                        img.setClickable(false);

                        for (int i = 0; i < pushedButtonsArray.size(); i++) {
                            pushedButtonString += ", " + pushedButtonsArray.get(i);
                        }
                        pushedButtonString = "";
                    }
                }
            });

            String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
            img.setBackground(getResources().getDrawable(j));
            pushedButtonList.put(indicer, img);
        }

        //Key List
        List keys = new ArrayList(pushedButtonList.keySet());

        //Random ordering key
        Collections.shuffle(keys);
        //Generate Hashmap
        HashMap<Integer, Button> lettereCasuali = new HashMap<>();
        int num = 0;

        for (Object o : keys) {
            // Access keys/values in a random order
            lettereCasuali.put(o.hashCode(), pushedButtonList.get(o));

            if (num < 8) {
                firstLineOfRandomButtons.addView(pushedButtonList.get(o));
            } else {
                secondLineOfRandomButtons.addView(pushedButtonList.get(o));
            }

            num++;
        }

        englishWordTextView.setText(word.getEnglishWord());
    }

    public void next(View view) {

        if (this.index++ == 20) {
            this.getWordsAndSetLetters();
        }else{
            this.setLetters(this.words, this.index);
        }
    }

    public void showSolution(View view) {

        solutionButton.setClickable(false);
        AlertDialog.Builder alertD = new AlertDialog.Builder(
                MainGameActivity.this);
        alertD.setTitle(getString(R.string.soluzione));
        alertD.setMessage(getString(R.string.soluzione_msg));
        alertD.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String[] translations = word.getNativeTranslation().split(",");

                //Take the first translation, ignoring sysnonimous
                final String nativeWord = translations[0];
                String[] letters = nativeWord.toLowerCase().split("(?!^)");

                for (int i = 0; i < letters.length; i++) {
                    Button black_button = buttonArrayList.get(i);

                    if (!letters[i].equalsIgnoreCase(" ")) {
                        String sfondo_lettera = gameUtils.substituteSpecialChar(letters[i].charAt(0));
                        int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                        black_button.setBackground(getResources().getDrawable(j));
                        black_button.setClickable(false);
                    } else {
                        black_button.setBackgroundColor(Color.TRANSPARENT);
                        black_button.setClickable(false);
                    }
                }
                errors++;
                errorsTextView.setText(getString(R.string.errori) + " " + errors);
                rightAnswerLayout.setVisibility(View.VISIBLE);
                firstLineOfRandomButtons.setVisibility(View.GONE);
                secondLineOfRandomButtons.setVisibility(View.GONE);

                gameUtils.soundCorrect();
                gameUtils.vibrate();

                sentenceTextView.setText("");
            }
        });

        alertD.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertD.show();
        solutionsNumber++;
    }

    public void showHelp(View view) {

        final Word word = this.word;

        if (helpsNumber < 3) {
            AlertDialog.Builder alertD = new AlertDialog.Builder(MainGameActivity.this);
            alertD.setTitle(getString(R.string.aiuto));
            alertD.setMessage(getString(R.string.aiuto_msg));
            alertD.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    //Estraggo la traduzione, ignoro i sinonimi
                    String[] translations = word.getNativeTranslation().split(",");
                    final String nativeWord = translations[0];

                    //estraggo le singole lettere
                    String[] letters = nativeWord.toLowerCase().split("(?!^)");

                    //calcolo il numero di lettere da mostrare nell'aiuto
                    int lunghezza_tot = nativeWord.length();
                    int stringToShowLength = lunghezza_tot / 3;

                    String stringToShow = nativeWord.substring(0, stringToShowLength);
                    //sottostringa.
                    if (stringToShow.matches(".*\\s+.*")) {

                        AlertDialogManager alertD = new AlertDialogManager();
                        alertD.showAlertDialog(MainGameActivity.this, "Help", getString(R.string.prime_lettere) + stringToShow, false);
                    } else {
                        //reset delle lettere inserite dall utente
                        selectedWord = new ArrayList<>();
                        pushedButtonsArray = new ArrayList<>();

                        //riprisino i background dei bottoni eventualmente pushedButtonsArray dall'utente
                        for (int i = 0; i < lunghezza_tot; i++) {
                            //ripristino caselle cliccabili
                            Button b = pushedButtonList.get(i);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(letters[i].charAt(0));
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            b.setBackground(getResources().getDrawable(j));
                            b.setClickable(true);

                            //Black Buttons reset
                            Button black_b = buttonArrayList.get(i);
                            black_b.setBackground(getResources().getDrawable(R.drawable.black));
                        }

                        String randomLetters = getString(R.string.alfabeto);

                        for (int i = lunghezza_tot; i < 16; i++) {
                            Button b = pushedButtonList.get(i);
                            int id = b.getId();
                            final Character randomChar = randomLetters.charAt(id);

                            b.setClickable(true);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(randomChar);
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            b.setBackground(getResources().getDrawable(j));
                        }

                        for (int i = 0; i < stringToShowLength; i++) {
                            Button black_button = buttonArrayList.get(i);
                            String sfondo_lettera = gameUtils.substituteSpecialChar(letters[i].charAt(0));
                            int j = getResources().getIdentifier(sfondo_lettera, "drawable", getPackageName());
                            //Showing soluttion letters
                            black_button.setBackground(getResources().getDrawable(j));
                            //aggiungo le lettere prescelte alla soluzione che sto generando
                            selectedWord.add(letters[i]);
                            //rendo le lettere dell'aiuto non deselezionabili
                            black_button.setClickable(false);

                            //cambio lo sfondo delle lettere selezionate rendendolo nero, e disabilito il click su esse
                            Button b = pushedButtonList.get(i);
                            b.setBackground(getResources().getDrawable(R.drawable.black));
                            b.setClickable(false);

                            //aggiungo gli id dei button clickati alla lista dei premuti
                            pushedButtonsArray.add(i);
                        }
                    }

                    //Disable help button click
                    helpButton.setClickable(false);

                    //scalo i punti dovuti all'aiuto e visualizzo il totale aggiornato
                    points = points - 1;
                    pointTextView.setText(getString(R.string.punteggio) + " " + points);
                }
            });

            alertD.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            alertD.show();
            helpsNumber++;

        } else {
            alertDialog.showAlertDialog(MainGameActivity.this, getString(R.string.aiuti_terminati_title), getString(R.string.aiuti_terminati), false);
        }
    }

    @SuppressLint("SetTextI18n")
    public void restart(View view) {
        pushedButtonsArray = new ArrayList<>();
        points = 0;
        errors = 0;
        errorsTextView.setText(getString(R.string.errori) + " " + errors);
        mainContent.setVisibility(View.VISIBLE);
        helpButton.setClickable(true);
        solutionButton.setClickable(true);
        gameOverLayout.setVisibility(View.GONE);
        this.getWordsAndSetLetters();
    }

    @SuppressLint("SetTextI18n")
    public boolean isNewRecord(float punti) {
        boolean returnValue = false;
        record = session.getRecord(category);

        if (punti > record) {
            session.setRecord(category, punti);
            TextView recordTextView = findViewById(R.id.record);
            recordTextView.setText(getString(R.string.record) + " " + punti);
            returnValue = true;
        }

        return returnValue;

    }

    public void showSentence(View view) {
        sentenceTextView.setText(this.word.getSentence());
    }

    public void listenToWord(View view) {

        String englishWord = this.word.getNativeTranslation().toLowerCase();
        englishWord = gameUtils.substituteSpecialCharWordToPronunce(englishWord);

        String url = "https://ssl.gstatic.com/dictionary/static/sounds/oxford/" + englishWord + "--_gb_1.mp3";
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            Log.e(TAG, "Unable to load audio");
        }
        if (!this.listened) {
            if (connectivityManager.check()) {
                AudioRequest ar = new AudioRequest(this, mediaPlayer);
                ar.execute(url);
                this.listened = true;
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e(TAG, "Unable to load audio");
            }
        }
    }
}