package com.keypr.test.tracker;

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
	}
	
	public void stopTracking() {
		this.listener = null;
	}
	
	/**
	 * Check the status immediate.
	 */
	public void check() {
		listener.onStateChanged(wifiMonitor.checkWiFi(storage.getWifiName())
				|| locationMonitor.checkLocation(storage.getLocationLat(), storage.getLocationLon(), storage.getRadius()));
	}
	
	public interface Listener {
		void onStateChanged(boolean inside);
	}
}
