package com.chiararipanti.itranslate.db;

import java.util.ArrayList;

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
	
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
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
	public void set_alternative(ArrayList<String> _alternative) {
		this._alternative = _alternative;
	}
	
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
}

