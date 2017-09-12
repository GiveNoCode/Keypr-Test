package com.keypr.test.datastore;

import android.support.test.runner.AndroidJUnit4;

import com.keypr.test.App;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@RunWith(AndroidJUnit4.class)
public class KeyValueStorageTest {
	
	private static final float FLOAT_DELTA = 0.0001f;
	
	private KeyValueStorage keyValueStorage;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		keyValueStorage = new PreferencesStorage(App.getContext());
	}
	
	@After
	public void tearDown() throws Exception {
		keyValueStorage.clear();
	}
	
	@Test
	public void saveLocationLat() throws Exception {
		float lat = 32.12f;
		keyValueStorage.saveLocationLat(lat);
		
		assertEquals(lat, keyValueStorage.getLocationLat(), FLOAT_DELTA);
	}
	
	@Test
	public void saveLocationLon() throws Exception {
		float lon = 643.578f;
		keyValueStorage.saveLocationLon(lon);
		
		assertEquals(lon, keyValueStorage.getLocationLon(), FLOAT_DELTA);
	}
	
	@Test
	public void saveRadius() throws Exception {
		float rad = 4593f;
		keyValueStorage.saveRadius(rad);
		
		assertEquals(rad, keyValueStorage.getRadius(), FLOAT_DELTA);
	}
	
	@Test
	public void saveWifiName() throws Exception {
		String name = "my_wifi";
		keyValueStorage.saveWifiName(name);
		
		assertEquals(name, keyValueStorage.getWifiName());
	}
	
	@Test
	public void testOverWriteValue() throws Exception {
		String name1 = "my_wifi";
		String name2 = "another_wifi";
		keyValueStorage.saveWifiName(name1);
		keyValueStorage.saveWifiName(name2);
		
		assertEquals(name2, keyValueStorage.getWifiName());
	}
	
	@Test
	public void clear() throws Exception {
		String name = "my_wifi";
		float lat = 32.12f;
		float lon = 643.578f;
		float rad = 4593f;
		keyValueStorage.saveRadius(rad);
		keyValueStorage.saveLocationLon(lon);
		keyValueStorage.saveLocationLat(lat);
		keyValueStorage.saveWifiName(name);
		
		keyValueStorage.clear();
		
		assertEquals(-1, keyValueStorage.getLocationLon(), FLOAT_DELTA);
		assertEquals(-1, keyValueStorage.getLocationLat(), FLOAT_DELTA);
		assertEquals(-1, keyValueStorage.getRadius(), FLOAT_DELTA);
		assertNull(keyValueStorage.getWifiName());
	}
	
	@Test
	public void registerListener() throws Exception {
		KeyValueStorage.SettingsUpdateListener listener  = Mockito.mock(KeyValueStorage.SettingsUpdateListener.class);
		
		keyValueStorage.registerListener(listener);
		
		float rad = 4593f;
		keyValueStorage.saveRadius(rad);
		
		Thread.sleep(500);
		
		verify(listener).onSettingsUpdate();
		
		keyValueStorage.unregisterListener(listener);
		
		
		float lon = 643.578f;
		keyValueStorage.saveLocationLon(lon);
		
		verifyNoMoreInteractions(listener);
	}
	
}