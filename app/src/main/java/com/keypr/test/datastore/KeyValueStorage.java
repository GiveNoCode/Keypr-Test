package com.keypr.test.datastore;

public interface KeyValueStorage {
	
	void saveLocationLat(float locationLat);
	
	float getLocationLat();
	
	void saveLocationLon(float locationLon);
	
	float getLocationLon();
	
	void saveRadius(float radius);
	
	float getRadius();
	
	void saveWifiName(String wifiName);
	
	String getWifiName();
	
	void clear();
	
	void registerListener(SettingsUpdateListener listener);
	
	void unregisterListener(SettingsUpdateListener listener);
	
	interface SettingsUpdateListener {
		void onSettingsUpdate();
	}
}
