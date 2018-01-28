package com.chiararipanti.itranslate.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chiararipanti.itranslate.db.Quiz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;




public class GetQuizFromDB extends AsyncTask<String,Void,ArrayList<Quiz>> 
{
	ArrayList<Quiz> quizs;
	final String TAG="GetQuizFromDB";
	int type;
	
	
	public GetQuizFromDB(Context context,int type)
	{
		this.type=type;
		quizs=new ArrayList<Quiz>();
		
	}
	
	protected ArrayList<Quiz> doInBackground(String... params)
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
	   		        HttpPost request = new HttpPost("http://sfidaricette.altervista.org/getQuiz.php");   		         
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
		        			String[] alter=json_data.getString("alternative").split(",");
		        			Log.v(TAG,"c");
		        			String traduzione=alter[0];
		        			Log.v(TAG,"c");
		        			ArrayList<String> alternative=new ArrayList();
		        			Log.v(TAG,"c");
		        			alternative.add(alter[1]);
		        			alternative.add(alter[2]);
		        			alternative.add(alter[3]);
		        			Log.v(TAG,"text"+text);
		        			Quiz quiz=new Quiz(text,traduzione,alternative);
		        			Log.v(TAG,"c");
		        			quizs.add(quiz);
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
	   protected void onPostExecute(ArrayList<Quiz> result)
	   {
		   
		   //Log.v(TAG,"post");
	   } //fine onPost
	   } //fine AsyncTask