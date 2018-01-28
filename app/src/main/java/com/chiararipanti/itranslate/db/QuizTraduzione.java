package com.chiararipanti.itranslate.db;



public class QuizTraduzione {
	int tipo;
	String text;
	String correctAnswer;
	
	// constructor
    public QuizTraduzione(String text, String correctAnswer){
    	this.text=text;
    	this.correctAnswer=correctAnswer;  
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
	
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
}
