package com.chiararipanti.itranslate.db;

import java.util.ArrayList;
/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class Quiz {
	int tipo;
	String text;
	String correctAnswer;
	ArrayList<String> _alternative;
	
	// constructor
    public Quiz(String text, String correctAnswer, ArrayList<String> alternative){
    	this.text=text;
    	this.correctAnswer=correctAnswer;  
    	this._alternative=alternative;
    }

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public ArrayList<String> get_alternative() {
		return _alternative;
	}
	
	public String getCorrectAnswer() {
		return correctAnswer;
	}
}

