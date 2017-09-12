package com.keypr.test.tracker;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

public class WifiMonitor {
	
	private final Context context;
	
	private BroadcastReceiver reciever;
	
	public WifiMonitor(Context context) {
		this.context = context;
	}
	
	public boolean checkWiFi(String wifiName) {
		if (TextUtils.isEmpty(wifiName)) {
			return false;
		}
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMan.getActiveNetworkInfo();
		if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService (Context.WIFI_SERVICE);
			WifiInfo info = wifiManager.getConnectionInfo ();
			String ssid  = info.getSSID();
			if (ssid != null && format(wifiName).toLowerCase().equals(ssid.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public void startTracking(BroadcastReceiver receiver) {
		this.reciever = receiver;
		context.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	public void stopTracking() {
		context.unregisterReceiver(reciever);
	}
	
	/**
	 * adds " to both sides of a string
	 */
	private String format(String wifiName) {
		return "\""+wifiName+"\"";
	}
}
