package com.chiararipanti.itranslate;


import java.util.ArrayList;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import com.chiararipanti.itranslate.model.Song;
import com.chiararipanti.itranslate.util.EnglishGameUtility;
import com.chiararipanti.itranslate.db.GetSongsFromDB;
import com.chiararipanti.itranslate.util.MyConnectivityManager;
import com.chiararipanti.itranslate.util.SessionManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//TODO: Ottimizza le chiamate, Controlla logica del codice

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class MusicActivity extends Activity {
    ArrayList<Song> songs;
    int next;
    Song song;
    TextView songTitleTextView;
    Button firstOptionButton;
    Button secondOptionButton;
    Button thirdOptionButton;
    Button forthOptionButton;
    MyConnectivityManager connectivityManager;
    EnglishGameUtility gameUtils;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        session = new SessionManager(getApplicationContext());
        gameUtils = new EnglishGameUtility(this);
        gameUtils.setHomeButtonEnabled();
        gameUtils.addAdBunner();

        connectivityManager = new MyConnectivityManager(getApplicationContext());

        songTitleTextView = findViewById(R.id.song_title);
        firstOptionButton = findViewById(R.id.first_option);
        secondOptionButton = findViewById(R.id.second_option);
        thirdOptionButton = findViewById(R.id.third_option);
        forthOptionButton = findViewById(R.id.forth_option);

        next = 0;

        //TODO: Controlla la connessione (nel getSongs)
        this.getSongs();
        this.setUpSong();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    public void getSongs() {
        //attraverso l'asynctask memorizzo dieci vocaboli della categoria scelta
        GetSongsFromDB getsongsTask = new GetSongsFromDB(this);
        try {
            getsongsTask.execute();
            songs = getsongsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        song = songs.get(next);
    }

    @SuppressLint("SetTextI18n")
    public void setUpSong() {
        songTitleTextView.setText(song.getTitle() + " - " + song.getAuthor());
        ArrayList<String> alt = song.getAlternatives();
        alt.add(song.getTranslation());
        Collections.shuffle(alt);
        firstOptionButton.setText(alt.get(0));
        secondOptionButton.setText(alt.get(1));
        thirdOptionButton.setText(alt.get(2));
        forthOptionButton.setText(alt.get(3));
    }

    public void next(View view) {
        final Button selectedButton = (Button) view;
        String buttonText = selectedButton.getText().toString();
        if (buttonText.equals(song.getTranslation())) {
            selectedButton.setBackgroundColor(Color.GREEN);
            gameUtils.soundCorrect();
        } else {
            selectedButton.setBackgroundColor(Color.RED);
            gameUtils.soundWrong();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedButton.setBackgroundResource(R.drawable.shape_button);
                next++;
                if (next < 11)
                    song = songs.get(next);
                else {
                    //TODO: Richiama il getSong
                    if (connectivityManager.check()) {
                        GetSongsFromDB getCanzoni = new GetSongsFromDB(getApplicationContext());
                        getCanzoni.execute();
                        try {
                            songs = getCanzoni.get();
                            next = 0;
                            song = songs.get(next);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.attiva_connessione), Toast.LENGTH_SHORT).show();
                    }
                }
                setUpSong();

            }
        }, 1000);
    }

    public void showSongText(View view) {
        Intent goToSongText = new Intent(getApplicationContext(), SongTextActivity.class);
        goToSongText.putExtra("autore", song.getAuthor().toLowerCase().replace(" ", "-"));
        goToSongText.putExtra("songTitleTextView", song.getTitle().toLowerCase().replace(" ", "-").replace("?", "").replace("'", ""));
        startActivity(goToSongText);
    }

}
