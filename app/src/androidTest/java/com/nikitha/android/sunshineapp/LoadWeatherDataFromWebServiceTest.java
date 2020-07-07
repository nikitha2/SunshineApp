package com.nikitha.android.sunshineapp;

import android.content.Intent;
import android.os.IBinder;

import com.nikitha.android.sunshineapp.sync.LoadWeatherDataFromWebService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ServiceTestRule;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class LoadWeatherDataFromWebServiceTest {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Test
    public void testWithBoundService() throws TimeoutException {

//        Intent intent1 = new Intent(getApplicationContext(), LoadWeatherDataFromWebService.class);
//        intent1.putExtra("uri","https://community-open-weather-map.p.rapidapi.com/forecast?units=Imperial&q=Albuquerque%2C%20N.M.");
//        getApplicationContext().startService(intent1);
//
//        // Bind the service and grab a reference to the binder.
//        IBinder binder = mServiceRule.bindService(serviceIntent);
//
//        // Get the reference to the service, or you can call public methods on the binder directly.
//        LocalService service = ((LocalService.LocalBinder) binder).getService();
//
//        // Verify that the service is working correctly.
//        assertThat(service.getRandomInt(), is(any(Integer.class)));
    }
}
