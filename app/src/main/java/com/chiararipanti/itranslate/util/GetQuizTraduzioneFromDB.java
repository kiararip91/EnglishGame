package com.chiararipanti.itranslate.util;

import java.io.BufferedReader;



import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chiararipanti.itranslate.db.QuizTraduzione;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;




public class GetQuizTraduzioneFromDB extends AsyncTask<String,Void,ArrayList<QuizTraduzione>> 
{
	ArrayList<QuizTraduzione> quizs;
	final String TAG="GetQuizTraduzioneFromDB";
	int type;
	
	
	public GetQuizTraduzioneFromDB(Context context,int type)
	{
		this.type=type;
		quizs=new ArrayList<QuizTraduzione>();
		
	}
	
	protected ArrayList<QuizTraduzione> doInBackground(String... params)
	   {
		   		//String stringa_ingrediente="";
		        InputStream is=null;
		        String result="";
		 	   	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 	   	nameValuePairs.add(new BasicNameValuePair("type", type+""));
		 	   Log.v(TAG,"type:"+type );
		 	    Log.v(TAG,"prima del try");
		        try {
		            HttpClient client = new DefaultHttpClient();
	   		        HttpPost request = new HttpPost("http://sfidaricette.altervista.org/getQuizTraduzione.php");   		         
		            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs);
		            request.setEntity(formEntity);
		            Log.v(TAG,"prima di execute");
		            //execute httpPost
		            HttpResponse response = client.execute(request);
		            HttpEntity entity= response.getEntity();
		            is =entity.getContent();
		        }
		        catch(Exception e)
		        {
		        	Log.e(TAG,"sono nel catch1");
		        }
			   
			   
		        /*converto la risposta in stringa*/
		        if(is!=null)
		        {
		        	try
		        	{
			        	BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			        	StringBuilder sb= new StringBuilder();
			        	String line=null;
			            while ((line = reader.readLine()) != null) 
			            {
			                sb.append(line + "\n");
			            }
			            
			            is.close();
			            result=sb.toString();
			         
			            ////Log.v("risposta",result);
		        	}
			        catch(Exception e)
			        {
			        	Log.e(TAG,"sono nel catch2");
			        }
		        	
		        	
		        	//parsing dei dati in formato json
		        	//Log.v(TAG,"prima di try");
		        	try
		        	{
		        		JSONArray jArray=new JSONArray(result);
		        		for(int i=0;i<jArray.length();i++)
		        		{
		        			JSONObject json_data=jArray.getJSONObject(i);
		        			String text=json_data.getString("text");
		        			Log.v(TAG,text);
		        			String traduzione=json_data.getString("soluzione");
		        			Log.v(TAG,"c");
		        			Log.v(TAG,"text"+text);
		        			QuizTraduzione quizTraduzione=new QuizTraduzione(text,traduzione);
		        			Log.v(TAG,"c");
		        			quizs.add(quizTraduzione);
		        			Log.v(TAG,"c");
		        		}
		        		Log.v(TAG,"fine for");
		        	}
			        catch(JSONException e)
			        {
			        	Log.v(TAG,"catch");
			        }
			        
			        
		        }
		        else
		        {
		        	//Log.e(TAG,"non ho trovato niente");
		        }
		            return quizs;
		   
     }


	   @Override
	   protected void onPostExecute(ArrayList<QuizTraduzione> result)
	   {
		   
		   //Log.v(TAG,"post");
	   } //fine onPost
	   } //fine AsyncTask