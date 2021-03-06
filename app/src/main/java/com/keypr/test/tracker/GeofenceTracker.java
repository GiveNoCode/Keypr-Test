package com.keypr.test.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.keypr.test.datastore.KeyValueStorage;

public class GeofenceTracker {
	
	private final KeyValueStorage storage;
	private final WifiMonitor wifiMonitor;
	private final LocationMonitor locationMonitor;
	
	private Listener listener;
	
	public GeofenceTracker(KeyValueStorage storage, WifiMonitor wifiMonitor, LocationMonitor locationMonitor) {
		this.storage = storage;
		this.wifiMonitor = wifiMonitor;
		this.locationMonitor = locationMonitor;
	}
	
	/**
	 * Starts NetworkState and Location monitoring
	 */
	public void startTracking(Listener listener) {
		this.listener = listener;
		wifiMonitor.startTracking(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				check();
			}
		});
		locationMonitor.startTracking(new LocationMonitor.LocationChangedListener() {
			@Override
			public void onLocationChanged() {
				check();
			}
		});
	}
	
	public void stopTracking() {
		this.listener = null;
		wifiMonitor.stopTracking();
		locationMonitor.stopTracking();
	}
	
	/**
	 * Check the status immediate.
	 */
	public void check() {
		listener.onStateChanged(wifiMonitor.checkWiFi(storage.getWifiName())
				|| locationMonitor.checkLocation(storage.getLocationLat(), storage.getLocationLon(), storage.getRadius()));
	}
	
	public LocationMonitor getLocationMonitor() {
		return locationMonitor;
	}
	
	public interface Listener {
		void onStateChanged(boolean inside);
	}
}
