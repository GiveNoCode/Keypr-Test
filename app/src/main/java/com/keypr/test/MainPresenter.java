package com.keypr.test;

import com.keypr.test.datastore.KeyValueStorage;
import com.keypr.test.tracker.GeofenceTracker;
import com.keypr.test.tracker.LocationMonitor;

public class MainPresenter {
	
	private final KeyValueStorage storage;
	private final GeofenceTracker tracker;
	
	private MainView view = MainView.EMPTY;
	
	private KeyValueStorage.SettingsUpdateListener storageListener = new KeyValueStorage.SettingsUpdateListener() {
		@Override
		public void onSettingsUpdate() {
			tracker.check();
		}
	};
	
	public MainPresenter(KeyValueStorage storage, GeofenceTracker tracker) {
		this.storage = storage;
		this.tracker = tracker;
	}
	
	public void attachView(MainView view) {
		this.view = view;
		this.view.showLocationLat(storage.getLocationLat());
		this.view.showLocationLon(storage.getLocationLon());
		this.view.showLocationRadius(storage.getRadius());
		this.view.showWifiName(storage.getWifiName());
	}
	
	public void detachView() {
		this.view = MainView.EMPTY;
	}
	
	public void startTracking() {
		tracker.startTracking(new GeofenceTracker.Listener() {
			
			@Override
			public void onStateChanged(boolean inside) {
				if (inside) {
					view.showStatusInside();
				} else {
					view.showStatusOutside();
				}
			}
		});
		
		// Request update from tracker when values are changed
		this.storage.registerListener(storageListener);
		
		// Request current status
		tracker.check();
	}
	
	public void stopTracking() {
		this.storage.unregisterListener(storageListener);
		tracker.stopTracking();
	}
	
	/**
	 * Just return storage to avoid delegation
	 */
	public KeyValueStorage getStorage() {
		return storage;
	}
	
	public LocationMonitor getLocationMonitor() {
		return tracker.getLocationMonitor();
	}
}
