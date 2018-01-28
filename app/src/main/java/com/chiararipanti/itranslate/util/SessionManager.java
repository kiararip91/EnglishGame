package com.chiararipanti.itranslate.util;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
/*
 * creo una classe apposita da poter richiamare nelle activities al momento del bisogno
 */

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class SessionManager{
    // Shared Preferences
    private SharedPreferences pref;

    // Editor per modificare le preferenze condivise
    private Editor editor;

    // Contesto
    private Context _context;
    private static final String PREF_NAME = "ItranslatePref";
    // Shared pref mode
    private int PRIVATE_MODE = 0;
    // variabili public, in modo da potervi accedere dall'esterno della classe
    public static final String principiante = "record_principiante";
    public static final String base = "record_base";
    public static final String intermedio = "record_intermedio";
    public static final String esperto = "record_esperto";
    public static final String vita_quotidiana = "record_vita_quotidiana";
    public static final String cibo = "record_cibo";
    public static final String animali = "record_animali";
    public static final String viaggi = "record_viaggi";
    private static final String KEY_SUONO = "suono";
    private static final String KEY_VIBRAZIONE = "vibrazione";

    // Costruttore
    public SessionManager(Context context){
        // prendo il contesto
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        // associo l'editore
        editor = pref.edit();

    }

    public void setVibrazione(Boolean vibrazione)
    {
        editor.putBoolean(KEY_VIBRAZIONE, vibrazione);
        editor.commit();
    }

    public boolean getVibrazione()
    {
        return pref.getBoolean(KEY_VIBRAZIONE, true);
    }

    public void setSuono(Boolean suono)
    {
        editor.putBoolean(KEY_SUONO, suono);
        editor.commit();
    }

    public boolean getSuono()
    {
        return pref.getBoolean(KEY_SUONO, true);
    }

    public int getPartite(String categoria){
        categoria="p"+categoria;
        return pref.getInt(categoria, 0);
    }

    public void incrPartite(String categoria){
        categoria="p"+categoria;
        int partite=getPartite(categoria);
        partite++;
        editor.putInt(categoria, partite);
        editor.commit();
    }

    public float getRecord(String categoria){
        return pref.getFloat(categoria, 0);
    }

    public void setRecord(String categoria, float punteggio){
        editor.putFloat(categoria, punteggio);
        editor.commit();
    }
}
