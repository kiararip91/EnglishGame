package com.chiararipanti.itranslate.db;

import java.util.List;

public class Vocabolo {
    
	int _id;
    String _lingua_nativa;
    String _categoria;
    String _inglese;
    String _frase;
    String _img;
    int _esatti;
    int _sbagliati;
    List<String> _lingua_nativa2;
    
    public Vocabolo(){}
    
    // constructor
    public Vocabolo(String inglese, String lingua_nativa, String categoria, int esatti, int sbagliati, String frase, String img){
    	this._inglese=inglese;
    	this._lingua_nativa=lingua_nativa;
    	this._categoria=categoria;  
    	this._esatti=esatti;
    	this._sbagliati=sbagliati;
    	this._frase=frase;
    	this._img=img;
    }

    
    public Vocabolo(int id,String inglese, String lingua_nativa, String categoria, int esatti, int sbagliati, String frase, String img){
    	this._id=id;
    	this._inglese=inglese;
    	this._lingua_nativa=lingua_nativa;
    	this._categoria=categoria;
    	this._esatti=esatti;
    	this._sbagliati=sbagliati;
    	this._frase=frase;
    	this._img=img;
    }
    
    public Vocabolo(String inglese, String lingua_nativa, String frase, String img){
    	this._inglese=inglese;
    	this._lingua_nativa=lingua_nativa;
    	this._frase=frase;
    	this._img=img;
    }
    
    public int getID(){
        return this._id;
    }
    
    public void setId(int id){
        this._id = id;
    }
     
    public void setLingua_nativa(String lingua_nativa){
        this._lingua_nativa = lingua_nativa;
    }
     
    public String getLingua_nativa(){
        return this._lingua_nativa;
    }
     
    
    public void setInglese(String inglese){
        this._inglese = inglese;
    }
    
    public String getInglese(){
        return this._inglese;
    }
    
    public void setFrase(String frase){
        this._frase = frase;
    }
    
    public String getFrase(){
        return this._frase;
    }
    
    public void setImg(String img){
        this._img = img;
    }
    
    public String getImg(){
        return this._img;
    }
    
    public List<String> getSinonimi(){
        return this._lingua_nativa2;
    }
    
    public String getCategoria(){
        return this._categoria;
    }
    
    public void setCategoria(String categoria){
        this._inglese = categoria;
    }
    
    public int getEsatti(){
        return this._esatti;
    }
    
    public void incrEsatti(){
        this._esatti = _esatti+1;
    }
    
    public int getSbagliati(){
        return this._sbagliati;
    }
    
    public void incrSbagliati(){
        this._sbagliati = _sbagliati+1;
    }
}
