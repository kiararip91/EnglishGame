package com.chiararipanti.itranslate.db;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class Word {

    private int id;
    private String nativeTranslation;
    private String category; //TODO: Metodo in questa classe per questa proprietà?
    private String englishWord;
    private String sentence;
    private String image;
    private int correctAttempts; //TODO: Metodo in questa classe per questa proprietà?
    private int wrongAttempts; //TODO: Metodo in questa classe per questa proprietà?

    public Word() {
    }

    // constructor
    public Word(String englishWord, String nativeTranslation, String category, int correctAttempts, int wrongAttempts, String sentence, String image) {
        this.englishWord = englishWord;
        this.nativeTranslation = nativeTranslation;
        this.category = category;
        this.correctAttempts = correctAttempts;
        this.wrongAttempts = wrongAttempts;
        this.sentence = sentence;
        this.image = image;
    }

    public Word(String englishWord, String nativeTranslation, String sentence, String image) {
        this.englishWord = englishWord;
        this.nativeTranslation = nativeTranslation;
        this.sentence = sentence;
        this.image = image;
    }


    public int getIs() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNativeTranslation() {
        return this.nativeTranslation;
    }

    public String getEnglishWord() {
        return this.englishWord;
    }

    public String getSentence() {
        return this.sentence;
    }

    public String getImage() {
        return this.image;
    }
}
