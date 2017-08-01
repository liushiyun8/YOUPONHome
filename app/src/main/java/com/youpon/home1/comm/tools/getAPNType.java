package com.youpon.home1.comm.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class getAPNType {
	private static final int WIFI=1;
	private static final int CMWAP=2;
	private static final int CMNET=3;
	Context context;
    public  getAPNType(Context context){ 
    	this.context=context;
		
    }
    public int getNet(){
        int netType = -1;  

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); 

         

        if(networkInfo==null){ 

            return netType; 

        } 

        int nType = networkInfo.getType(); 

        if(nType==ConnectivityManager.TYPE_MOBILE){ 

            Log.e("networkInfo", "networkInfo.getExtraInfo() is "+networkInfo.getExtraInfo());

            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){ 

                netType = CMNET; 

            } 

            else{ 

                netType = CMWAP; 

            } 

        } 

        else if(nType==ConnectivityManager.TYPE_WIFI){ 

            netType = WIFI; 

        } 

        return netType; 

    }
}
