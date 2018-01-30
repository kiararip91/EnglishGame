package com.chiararipanti.itranslate.util;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

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
        this.vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

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
            this.vibrator.vibrate(500);
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

    public void manageSettings(){
        LinearLayout ll = new LinearLayout(this.activity);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(100, 10, 100, 10);
        ll.setLayoutParams(layoutParams);
        Switch sbSound;
        sbSound = new Switch(this.activity);
        sbSound.setTextOn(this.activity.getString(R.string.sound_on));
        sbSound.setTextOff(this.activity.getString(R.string.sound_off));

        if(session.getSuono())
            sbSound.setChecked(true);

        sbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    session.setSuono(true);
                } else {
                    session.setSuono(false);
                }
            }
        });

        Switch sbVibra;
        sbVibra = new Switch(this.activity);
        sbVibra.setTextOn(this.activity.getString(R.string.vibration_on));
        sbVibra.setTextOff(this.activity.getString(R.string.vibration_off));

        if(session.getVibrazione())
            sbVibra.setChecked(true);

        sbVibra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    session.setVibrazione(true);
                } else {
                    session.setVibrazione(false);
                }
            }
        });

        ll.addView(sbVibra,layoutParams);
        ll.addView(sbSound,layoutParams);
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        builder = new AlertDialog.Builder(this.activity);
        builder.setView(ll);
        builder.setTitle("Edit settings");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

    }

    public String substituteSpecialChar(Character c){
        String res;
        switch(c) {

            case 'ß':
                res="sb";
                break;

            case 'á':
                res="aacuta";
                break;

            case 'ó':
                res="oacuta";
                break;


            case 'ö':
                res="opt";
                break;




            case 'ñ':
                res="ntilde";
                break;

            case 'í':
                res="iacuta";
                break;

            case 'é':
                res="eacuta";
                break;

            case 'è':
                res="egrave";
                break;

            case 'ê':
                res="eflex";
                break;

            case 'ë':
                res="ept";
                break;

            case 'à':
                res="agrave";
                break;

            case 'â':
                res="aflex";
                break;

            case 'ä':
                res="apt";
                break;

            case 'î':
                res="iflex";
                break;

            case 'ï':
                res="ipt";
                break;

            case 'ô':
                res="oflex";
                break;

            case 'ù':
                res="ugrave";
                break;

            case 'û':
                res="uflex";
                break;

            case 'ú':
                res="uacuta";
                break;

            case 'ü':
                res="upt";
                break;

            case 'ÿ':
                res="ypt";
                break;



            default:
                res=c.toString();
        }
        return res;
    }
    
    public String substituteSpecialCharWordToPronunce(String englishWord){
        return englishWord.replaceAll("to ","")
                .replaceAll("\\s","_")
                .replaceAll("_\\[sb\\]","")
                .replaceAll("_\\[sth\\]","")
                .replaceAll("_\\[smb\\]","")
                .replaceAll("\\[sb\\]","")
                .replaceAll("\\[sth\\]","")
                .replaceAll("\\[smb\\]","");
    }

}
