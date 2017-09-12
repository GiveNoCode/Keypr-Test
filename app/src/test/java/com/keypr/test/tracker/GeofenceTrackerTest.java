package com.keypr.test.tracker;

import com.keypr.test.datastore.KeyValueStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(JUnit4.class)
public class GeofenceTrackerTest {
	
	@Mock
	KeyValueStorage keyValueStorage;
	@Mock
	LocationMonitor locationMonitor;
	@Mock
	WifiMonitor wifiMonitor;
	@Mock
	GeofenceTracker.Listener listener;
	
	@InjectMocks
	GeofenceTracker tracker;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		given(keyValueStorage.getWifiName()).willReturn("wifi");
		given(keyValueStorage.getLocationLat()).willReturn(1.1f);
		given(keyValueStorage.getLocationLon()).willReturn(1.1f);
		given(keyValueStorage.getRadius()).willReturn(5f);
		
		given(wifiMonitor.checkWiFi(anyString())).willReturn(false);
		given(locationMonitor.checkLocation(anyFloat(), anyFloat(), anyFloat())).willReturn(false);
		
		tracker.startTracking(listener);
	}
	
	@After
	public void tearDown() {
		tracker.stopTracking();
	}
	
	@Test
	public void testCkeckOutside() {
		
		tracker.check();
		
		verify(wifiMonitor).checkWiFi("wifi");
		verify(locationMonitor).checkLocation(1.1f, 1.1f, 5f);
		
		verify(listener).onStateChanged(false);
	}
	
	@Test
	public void testCheckInsideViaWifi() {
		given(wifiMonitor.checkWiFi("wifi")).willReturn(true);
		
		tracker.check();
		
		verify(keyValueStorage).getWifiName();
		verifyNoMoreInteractions(keyValueStorage);
		
		verify(wifiMonitor).checkWiFi("wifi");
		verifyNoMoreInteractions(wifiMonitor);
		
		verifyZeroInteractions(locationMonitor);
		
		verify(listener).onStateChanged(true);
		verifyNoMoreInteractions(listener);
	}
	
	@Test
	public void testCheckViaLocation() {
		given(locationMonitor.checkLocation(1.1f, 1.1f, 5f)).willReturn(true);
		
		tracker.check();
		
		verify(keyValueStorage).getLocationLat();
		verify(keyValueStorage).getLocationLon();
		verify(keyValueStorage).getRadius();
		
		verify(listener).onStateChanged(true);
		verifyNoMoreInteractions(listener);
	}
	
	
	
	
	
}