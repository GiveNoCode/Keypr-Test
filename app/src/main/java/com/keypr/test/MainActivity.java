package com.keypr.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.keypr.test.datastore.KeyValueStorage;
import com.keypr.test.datastore.PreferencesStorage;
import com.keypr.test.tracker.GeofenceTracker;
import com.keypr.test.tracker.LocationMonitor;
import com.keypr.test.tracker.WifiMonitor;

public class MainActivity extends AppCompatActivity implements MainView {
	
	private TextView tvStatus;
	private EditText etLocationLat;
	private EditText etLocationLon;
	private EditText etLocationRadius;
	private EditText etWifiName;
	
	private MainPresenter presenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bindViews();
		
		presenter = (MainPresenter) getLastCustomNonConfigurationInstance();
		if (presenter == null) {
			// Initialize and inject dependencies via constructors
			Context context = getApplicationContext();
			KeyValueStorage storage = new PreferencesStorage(context);
			presenter = new MainPresenter(storage, new GeofenceTracker(storage, new WifiMonitor(context), new LocationMonitor(context)));
		}
		presenter.attachView(this);
		presenter.getLocationMonitor().attachActivity(this);
		
		etLocationLat.addTextChangedListener(new TextChangedListener() {
			@Override
			public void afterTextChanged(Editable s) {
				// TODO validate input
				// TODO get rid of try catch. I use it here to save time
				try {
					presenter.getStorage().saveLocationLat(Float.parseFloat(s.toString()));
				} catch (Exception e) {
				}
			}
		});
		
		etLocationLon.addTextChangedListener(new TextChangedListener() {
			@Override
			public void afterTextChanged(Editable s) {
				try {
					presenter.getStorage().saveLocationLat(Float.parseFloat(s.toString()));
				} catch (Exception e) {
				}
			}
		});
		
		etLocationRadius.addTextChangedListener(new TextChangedListener() {
			@Override
			public void afterTextChanged(Editable s) {
				
				try {
					presenter.getStorage().saveRadius(Float.parseFloat(s.toString()));
				} catch (Exception e) {
				}
			}
		});
		
		etWifiName.addTextChangedListener(new TextChangedListener() {
			@Override
			public void afterTextChanged(Editable s) {
				presenter.getStorage().saveWifiName(s.toString());
			}
		});
		
	}
	
	private void bindViews() {
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		etLocationLat = (EditText) findViewById(R.id.etLocationLat);
		etLocationLon = (EditText) findViewById(R.id.etLocationLon);
		etLocationRadius = (EditText) findViewById(R.id.etLocationRadius);
		etWifiName = (EditText) findViewById(R.id.etWifiName);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		presenter.startTracking();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		presenter.stopTracking();
	}
	
	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		// Save presenter between config changes
		return presenter;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.detachView();
		presenter.getLocationMonitor().detachActivity();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		presenter.getLocationMonitor().onRequestPermissionResult(requestCode, permissions, grantResults);
	}
	
	@Override
	public void showStatusInside() {
		tvStatus.setText(R.string.status_inside);
	}
	
	@Override
	public void showStatusOutside() {
		tvStatus.setText(R.string.status_outside);
	}
	
	@Override
	public void showLocationLat(float locationLat) {
		if (locationLat < 0) {
			etLocationLat.setText("");
		} else {
			etLocationLat.setText(String.valueOf(locationLat));
		}
	}
	
	@Override
	public void showLocationLon(float locationLon) {
		if (locationLon < 0) {
			etLocationLon.setText("");
		} else {
			etLocationLon.setText(String.valueOf(locationLon));
		}
	}
	
	@Override
	public void showLocationRadius(float locationRadius) {
		if (locationRadius < 0) {
			etLocationRadius.setText("");
		} else {
			etLocationRadius.setText(String.valueOf(locationRadius));
		}
	}
	
	@Override
	public void showWifiName(String wifiName) {
		if (wifiName == null) {
			etWifiName.setText("");
		} else {
			etWifiName.setText(wifiName);
		}
	}
	
	static abstract class TextChangedListener implements TextWatcher {
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
	}
}
