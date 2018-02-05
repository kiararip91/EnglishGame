package com.chiararipanti.itranslate.model;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class Translation {
    private String englishWord;
    private String correctAnswer;

    // constructor
    public Translation(String englishWord, String correctAnswer) {
        this.englishWord = englishWord;
        this.correctAnswer = correctAnswer;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setText(String text) {
        this.englishWord = englishWord;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
