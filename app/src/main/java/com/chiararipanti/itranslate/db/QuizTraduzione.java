package com.chiararipanti.itranslate.db;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class QuizTraduzione {
	String text;
	String correctAnswer;
	
	// constructor
    public QuizTraduzione(String text, String correctAnswer){
    	this.text=text;
    	this.correctAnswer=correctAnswer;  
    }
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getCorrectAnswer() {
		return correctAnswer;
	}
}
