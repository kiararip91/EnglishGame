package com.chiararipanti.itranslate.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.content.Context;

public class MyConnectivityManager {
    Context context;

    public MyConnectivityManager(Context context) {
        this.context=context;
        // TODO Auto-generated constructor stub
    }

    public boolean check(){
        //Log.v("connessione","connessione1");
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Log.v("connessione","connessione2");



        //controllo se ho il wifi attivo
        boolean isWifi = manager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        //Log.v("connessione","isWifi"+isWifi);
        //Log.v("connessione",ConnectivityManager.TYPE_MOBILE+"");
        NetworkInfo result = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean is3g=false;
        if(result!=null){
            is3g = manager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE)
                    .isConnectedOrConnecting();



            //Log.v("connessione","connessione4");
        }

        // Log.v("TEST",is3g +isWifi);

        if (!is3g && !isWifi)
            return false;
        else
            return true;
    }

}
