package com.chiararipanti.itranslate.util;

import java.util.ArrayList;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class TestSession {
    private String question;
    private String correctAnswer;
    private ArrayList<String> alternatives;

    // constructor
    public TestSession(String question, String correctAnswer, ArrayList<String> alternatives) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.alternatives = alternatives;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAlternatives() {
        return alternatives;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}

