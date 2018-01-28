package com.chiararipanti.itranslate.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;

/**
 * @author chiararipanti
 * date 04/05/2013
 */
public class MyConnectivityManager {
    private Context context;

    public MyConnectivityManager(Context context) {
        this.context=context;
    }

    public boolean check(){
        Boolean returnValue = false;
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager != null){
            boolean isWifi = manager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting();
            NetworkInfo result = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            boolean is3g=false;
            if(result!=null){
                is3g = manager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE)
                        .isConnectedOrConnecting();
            }
            returnValue = !(!is3g && !isWifi);
        }
        return returnValue;
    }

}
