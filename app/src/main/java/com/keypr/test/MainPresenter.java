package com.keypr.test;

import com.keypr.test.datastore.KeyValueStorage;

public class MainPresenter {
	
	private final KeyValueStorage storage;
	
	private MainView view = MainView.EMPTY;
	
	public MainPresenter(KeyValueStorage storage) {
		this.storage = storage;
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
	
	/**
	 * Just return storage to avoid delegation
	 */
	public KeyValueStorage getStorage() {
		return storage;
	}
}
