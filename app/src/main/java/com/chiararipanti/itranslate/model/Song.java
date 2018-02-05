package com.chiararipanti.itranslate.model;

import java.util.ArrayList;

//CR: Revision Finished

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class Song {
    private String title;
    private String author;
    private String translation;
    private ArrayList<String> alternatives;

    // constructor
    public Song(String title, String author, String translation, ArrayList<String> alternatives) {
        this.title = title;
        this.author = translation;
        this.translation = translation;
        this.alternatives = alternatives;
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTranslation() {
        return this.translation;
    }

    public ArrayList<String> getAlternatives() {
        return this.alternatives;
    }
}
