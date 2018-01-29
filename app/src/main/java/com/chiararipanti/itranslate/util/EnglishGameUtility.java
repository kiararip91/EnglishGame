package com.chiararipanti.itranslate.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.RelativeLayout;

import com.chiararipanti.itranslate.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * @author chiararipanti
 * date 29/01/2018
 */

public class EnglishGameUtility {

    /**
     * Declaring Variables
     */
    private SessionManager session;
    private Activity activity;

    private MediaPlayer wrongSound;
    private MediaPlayer correctSound;
    private MediaPlayer overSound;
    private Vibrator vibrator;


    /**
     * Initialize varibales
     * @param activity
     */
    public EnglishGameUtility(Activity activity){
        this.wrongSound = MediaPlayer.create(activity, R.raw.wrong);
        this.correctSound = MediaPlayer.create(activity, R.raw.correct);
        this.overSound = MediaPlayer.create(activity, R.raw.over);

        this.activity = activity;
        this.session = new SessionManager(activity);
    }

    public void soundCorrect(){
        if(this.session.getSuono()){
            this.correctSound.start();
        }
    }

    public void soundWrong() {
        if (this.session.getSuono()){
            this.wrongSound.start();
        }
    }

    public void soundOver() {
        if (this.session.getSuono()){
            this.overSound.start();
        }
    }

    public void vibrate(){
        if(this.session.getVibrazione()){
            vibrator.vibrate(500);
        }
    }

    public void setHomeButtonEnabled() {
        ActionBar actionBar = this.activity.getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void addAdBunner(){
        AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(this.activity.getString(R.string.unit_id));
        // Add the AdView to the view hierarchy.
        RelativeLayout layout = activity.findViewById(R.id.footer);
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }




}
