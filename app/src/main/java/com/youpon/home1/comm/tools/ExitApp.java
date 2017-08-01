package com.youpon.home1.comm.tools;

import android.content.Context;
import android.content.Intent;

public class ExitApp {
	private Context context;
	
	public ExitApp(Context context){
		this.context=context;
	}
	public void exit() {
		 Intent intent = new Intent();  
	        intent.setAction("exit");  
	        context.sendBroadcast(intent);
	}
}
