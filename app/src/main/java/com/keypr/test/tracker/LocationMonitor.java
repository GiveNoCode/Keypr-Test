package com.keypr.test.tracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.keypr.test.R;

public class LocationMonitor {
	public static final int PERMISSION_REQUEST_CODE = 123;
	
	
	private final Context context;
	
	private Activity activity;
	
	private LocationManager locationManager;
	private Location currentLocation;
	
	private LocationChangedListener locationChangedListener;
	private LocationListener internalListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			currentLocation = location;
			locationChangedListener.onLocationChanged();
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			
		}
	};
	
	
	public LocationMonitor(Context context) {
		this.context = context;
		
		currentLocation = getLastLocation();
	}
	
	private LocationManager getLocationManager() {
		if (locationManager == null) {
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
		return locationManager;
	}
	
	private String getProvider() {
		Criteria criteria = new Criteria();
		return getLocationManager().getBestProvider(criteria, false);
	}
	
	private Location getLastLocation(){
		if (!checkPermission()) {
			return null;
		}
		return getLocationManager().getLastKnownLocation(getProvider());
	}
	
	public void attachActivity(Activity activity) {
		this.activity = activity;
	}
	
	public void detachActivity() {
		this.activity = null;
	}
	
	
	public boolean checkLocation(float locationLat, float locationLon, float radius) {
		if(currentLocation != null) {
			float[] dist = new float[1];
			Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), locationLat, locationLon, dist);
			return dist[0]/1000f <= radius;
		}
		
		return false;
	}
	
	public void startTracking(LocationChangedListener locationChangedListener) {
		this.locationChangedListener = locationChangedListener;
		if (!checkPermission()) {
			return;
		}
		getLocationManager().requestLocationUpdates(getProvider(), 400, 1, internalListener);
	}
	
	public void stopTracking() {
		getLocationManager().removeUpdates(internalListener);
		this.locationChangedListener = null;
	}

	public boolean checkPermission() {
		if (activity == null) {
			return false;
		}
		if (Build.VERSION.SDK_INT < 23) {
			return true;
		}
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
					Manifest.permission.ACCESS_FINE_LOCATION)
					|| ActivityCompat.shouldShowRequestPermissionRationale(activity,
					Manifest.permission.ACCESS_FINE_LOCATION)) {

				
				Toast.makeText(context, R.string.title_location_permission, Toast.LENGTH_SHORT)
						.show();


			} else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(activity,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
						PERMISSION_REQUEST_CODE);
			}
			return false;
		} else {
			return true;
		}
	}
	
	public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (!checkPermission()) {
				return;
			}
			currentLocation = getLastLocation();
			
			if (locationChangedListener != null) {
				getLocationManager().requestLocationUpdates(getProvider(), 400, 1, internalListener);
			}
		}
	}
	
	public interface LocationChangedListener {
		void onLocationChanged();
	}
}
