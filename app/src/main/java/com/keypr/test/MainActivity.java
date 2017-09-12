package com.keypr.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	
	private TextView tvStatus;
	private EditText etLocationLat;
	private EditText etLocationLon;
	private EditText etLocationRadius;
	private EditText etWifiName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bindViews();
		
		
	}
	
	private void bindViews() {
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		etLocationLat = (EditText) findViewById(R.id.etLocationLat);
		etLocationLon = (EditText) findViewById(R.id.etLocationLon);
		etLocationRadius = (EditText) findViewById(R.id.etLocationRadius);
		etWifiName = (EditText) findViewById(R.id.etWifiName);
		
	}
}
