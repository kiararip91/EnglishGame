package com.chiararipanti.itranslate.db;

import java.util.ArrayList;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class Canzone {
    String _titolo;
    String _autore;
    String _traduzione;
    ArrayList<String> _alternative;

    // constructor
    public Canzone(String titolo, String autore, String traduzione, ArrayList<String> alternative) {
        this._titolo = titolo;
        this._autore = autore;
        this._traduzione = traduzione;
        this._alternative = alternative;
    }

    public void setTitolo(String titolo) {
        this._titolo = titolo;
    }

    public String getTitolo() {
        return this._titolo;
    }

    public String getAutore() {
        return this._autore;
    }

    public String getTraduzione() {
        return this._traduzione;
    }

    public ArrayList<String> getAlternative() {
        return this._alternative;
    }
}
