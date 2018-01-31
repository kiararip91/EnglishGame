package com.chiararipanti.itranslate.db;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class Vocabolo {

    int _id;
    String _lingua_nativa;
    String _categoria;
    String _inglese;
    String _frase;
    String _img;
    int _esatti;
    int _sbagliati;

    public Vocabolo() {
    }

    // constructor
    public Vocabolo(String inglese, String lingua_nativa, String categoria, int esatti, int sbagliati, String frase, String img) {
        this._inglese = inglese;
        this._lingua_nativa = lingua_nativa;
        this._categoria = categoria;
        this._esatti = esatti;
        this._sbagliati = sbagliati;
        this._frase = frase;
        this._img = img;
    }

    public Vocabolo(String inglese, String lingua_nativa, String frase, String img) {
        this._inglese = inglese;
        this._lingua_nativa = lingua_nativa;
        this._frase = frase;
        this._img = img;
    }


    public int getID() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getLingua_nativa() {
        return this._lingua_nativa;
    }

    public String getInglese() {
        return this._inglese;
    }

    public String getFrase() {
        return this._frase;
    }

    public String getImg() {
        return this._img;
    }
}
