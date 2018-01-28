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
public class SessionManager{
    // Shared Preferences
    SharedPreferences pref;

    // Editor per modificare le preferenze condivise
    Editor editor;

    // Contesto
    Context _context;
    private static final String PREF_NAME = "ItranslatePref";
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // variabili public, in modo da potervi accedere dall'esterno della classe
    //public static final String KEY_RECORD = "record";
    public static final String principiante = "record_principiante";
    public static final String base = "record_base";
    public static final String intermedio = "record_intermedio";
    public static final String esperto = "record_esperto";
    public static final String vita_quotidiana = "record_vita_quotidiana";
    public static final String cibo = "record_cibo";
    public static final String animali = "record_animali";
    public static final String viaggi = "record_viaggi";
    public static final String pprincipiante = "record_principiante";
    public static final String pbase = "partite_base";
    public static final String pintermedio = "partite_intermedio";
    public static final String pesperto = "partite_esperto";
    public static final String pvita_quotidiana = "partite_vita_quotidiana";
    public static final String pcibo = "partite_cibo";
    public static final String panimali = "partite_animali";
    public static final String pviaggi = "partite_viaggi";
    public static final String KEY_ACCESSO = "primo_accesso";
    public static final String KEY_SCARICATI = "scaricati";
    public static final String KEY_IDUTENTE = "id_utente";
    public static final String KEY_SUONO = "suono";
    public static final String KEY_VIBRAZIONE = "vibrazione";

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

    public boolean isPrimoAccesso()
    {
        return pref.getBoolean(KEY_ACCESSO, true);
    }

    public void accessoEffuettuato()
    {
        editor.putBoolean(KEY_ACCESSO, false);
        editor.commit();
    }

    public String getIDutente(){
        String idUtente = pref.getString(KEY_IDUTENTE, null);
        if(idUtente==null){
            setIDutente();
            idUtente = pref.getString(KEY_IDUTENTE, null);
        }

        return idUtente;
    }

    public void setIDutente(){
        editor.putString(KEY_IDUTENTE, UUID.randomUUID().toString());
        editor.commit();
    }


    /*
    public float getRecord(){
        float record = pref.getFloat(KEY_RECORD, 0);
        return record;
    }

    public void setRecord(float punteggio){
        editor.putFloat(KEY_RECORD, punteggio);
        editor.commit();
    }
    */
    public int getPartite(String categoria){
        categoria="p"+categoria;
        int partite = pref.getInt(categoria, 0);
        return partite;
    }

    public void incrPartite(String categoria){
        categoria="p"+categoria;
        int partite=getPartite(categoria);
        partite++;
        editor.putInt(categoria, partite);
        editor.commit();
    }

    public float getRecord(String categoria){
        float record = pref.getFloat(categoria, 0);
        return record;
    }

    public void setRecord(String categoria, float punteggio){
        editor.putFloat(categoria, punteggio);
        editor.commit();
    }

    public Set<String> getScaricati(){
        Set<String> scaricati =new HashSet<String>(pref.getStringSet(KEY_SCARICATI, new HashSet<String>()));
        //Set<String> scaricati = pref.getStringSet(KEY_SCARICATI, new HashSet<String>());
        return scaricati;
    }

    public void setScaricati(String scaricato){
        Log.v("session categora",scaricato);
        Set<String> scaricati = pref.getStringSet(KEY_SCARICATI, new HashSet<String>());
        //Set<String> scaricati =new HashSet<String>(pref.getStringSet(KEY_SCARICATI, new HashSet<String>()));
        scaricati.add(scaricato);
        //editor.remove(KEY_SCARICATI);
        //editor.putStringSet(KEY_SCARICATI, scaricati);
        editor=pref.edit();
        //editor.putString(scaricati);
        editor.apply();
    }

    public void eliminaScaricato(String scaricato){
        Set<String> scaricati =new HashSet<String>(pref.getStringSet(KEY_SCARICATI, new HashSet<String>()));
        //Set<String> scaricati = pref.getStringSet(KEY_SCARICATI, new HashSet<String>());
        scaricati.remove(scaricato);
        editor.remove(KEY_SCARICATI);
        editor.putStringSet(KEY_SCARICATI, scaricati);
        editor=pref.edit();
        editor.apply();
    }
}
