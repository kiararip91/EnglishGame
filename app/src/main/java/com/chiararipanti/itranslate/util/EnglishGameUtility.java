package com.chiararipanti.itranslate.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.chiararipanti.itranslate.R;

/**
 * @author chiararipanti
 * date 29/01/2018
 */

public class EnglishGameUtility {

    /**
     * Declaring Variables
     */
    private SessionManager session;
    private Context context;

    private MediaPlayer wrongSound;
    private MediaPlayer correctSound;
    private MediaPlayer overSound;
    private Vibrator vibrator;


    /**
     * Initialize varibales
     * @param context
     */
    public EnglishGameUtility(Context context){
        this.wrongSound = MediaPlayer.create(context, R.raw.wrong);
        this.correctSound = MediaPlayer.create(context, R.raw.correct);
        this.overSound = MediaPlayer.create(context, R.raw.over);

        this.context = context;
        this.session = new SessionManager(context);
    }

    public void soundCorrect(){
        if(session.getSuono()){
            this.correctSound.start();
        }
    }

    public void soundWrong() {
        if (session.getSuono()){
            this.wrongSound.start();
        }
    }

    public void soundOver() {
        if (session.getSuono()){
            this.overSound.start();
        }
    }

    public void vibrate(){
        if(session.getVibrazione()){
            vibrator.vibrate(500);
        }

    }


}
