package com.keypr.test.datastore;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesStorage implements KeyValueStorage{
	
	private static final String PREF_NAME = "com.keypr.test.PREFERENCES";
	
	private Context context;
	private SharedPreferences.OnSharedPreferenceChangeListener internalListener;
	private SettingsUpdateListener listener;
	
	public PreferencesStorage(Context context) {
		this.context = context;
	}
	
	@Override
	public void registerListener(final SettingsUpdateListener listener) {
		internalListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				if (listener != null) {
					listener.onSettingsUpdate();
				}
			}
		};
		getPreferences().registerOnSharedPreferenceChangeListener(internalListener);
	}
	
	@Override
	public void unregisterListener(SettingsUpdateListener listener) {
		if (listener == this.listener) {
			getPreferences().unregisterOnSharedPreferenceChangeListener(internalListener);
		}
	}
	
	private SharedPreferences getPreferences() {
		return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}
	
	@Override
	public synchronized void saveLocationLat(float locationLat) {
		getPreferences().edit()
				.putFloat("LAT", locationLat)
				.apply();
	}
	
	@Override
	public float getLocationLat() {
		return getPreferences()
				.getFloat("LAT", -1);
	}
	
	@Override
	public synchronized void saveLocationLon(float locationLon) {
		getPreferences().edit()
				.putFloat("LON", locationLon)
				.apply();
	}
	
	@Override
	public float getLocationLon() {
		return getPreferences()
				.getFloat("LON", -1);
	}
	
	@Override
	public synchronized void saveRadius(float radius) {
		getPreferences().edit()
				.putFloat("RAD", radius)
				.apply();
	}
	
	@Override
	public float getRadius() {
		return getPreferences()
				.getFloat("RAD", -1);
	}
	
	@Override
	public synchronized void saveWifiName(String wifiName) {
		getPreferences().edit()
				.putString("WIFI", wifiName)
				.apply();
	}
	
	@Override
	public String getWifiName() {
		return getPreferences()
				.getString("WIFI", null);
	}
	
	@Override
	public synchronized void clear() {
		getPreferences().edit()
				.clear()
				.apply();
	}
}
