package com.keypr.test;

public interface MainView {
	
	void showStatusInside();
	
	void showStatusOutside();
	
	void showLocationLat(float locationLat);
	
	void showLocationLon(float locationLon);
	
	void showLocationRadius(float locationRadius);
	
	void showWifiName(String wifiName);
	
	/**
	 * Empty view realization to avoid null checks.
	 */
	MainView EMPTY = new MainView() {
		@Override
		public void showStatusInside() {
			
		}
		
		@Override
		public void showStatusOutside() {
			
		}
		
		@Override
		public void showLocationLat(float locationLat) {
			
		}
		
		@Override
		public void showLocationLon(float locationLon) {
			
		}
		
		@Override
		public void showLocationRadius(float locationRadius) {
			
		}
		
		@Override
		public void showWifiName(String wifiName) {
			
		}
	};
}
