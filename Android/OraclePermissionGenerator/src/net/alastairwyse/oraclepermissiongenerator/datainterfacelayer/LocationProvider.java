/*
 * Copyright 2015 Alastair Wyse (http://www.oraclepermissiongenerator.net/oraclepermissiongeneratorandroid/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.alastairwyse.oraclepermissiongenerator.datainterfacelayer;

import android.content.Context;
import android.location.*;
import android.os.Bundle;

import net.alastairwyse.oraclepermissiongenerator.frameworkabstraction.*;

/**
 * Retrieves the current geographical location of the host device from underlying Android services.
 * @author Alastair Wyse
 */
public class LocationProvider implements ILocationProvider, LocationListener {

    private final int minimumTimeBetweenLocationUpdates = 120000;   // In milliseconds
    private final int minimumDistanceBetweenLocationUpdates = 100;  // In metres
    private Context context;
    private IDateProvider dateProvider;
    private LocationManager locationManager;
    private Location lastKnownLocation;
    private Object lastKnownLocationLock;  // Use this object as a lock/mutex around the lastKnownLocation member.  Android examples don't stipulate that this is required, but I think it's possible this could be accessed by 2 threads (OS thread updating with callbacks, and the main thread of the application).
    
    // TODO: This class doesn't seem to work properly on the emulator...
    //         LocationManager.getLastKnownLocation() always returns null
    //         LocationListener.onLocationChanged() callback is never called
    
    /**
     * Initialises a new instance of the LocationProvider class.
     * <b>Note</b> - This class provides a basic method of getting the device location using the LocationManager.requestLocationUpdates() method (as described in http://developer.android.com/guide/topics/location/strategies.html).  For later API levels, it may be preferable to use Google Play Service instead.   
     * @param  context  The context under which the LocationProvider is being initialized.
     */
    public LocationProvider(Context context) {
        this.context = context;
        dateProvider = new DateProvider();
        lastKnownLocationLock = new Object();
    }
    
    /**
     * Initialises a new instance of the LocationProvider class.
     * <b>Note</b> this is an additional constructor to facilitate unit tests, and should not be used to instantiate the class under normal conditions.
     * @param  context       A test (mock) Context object 
     * @param  dateProvider  A test (mock) IDateProvider object 
     */
    public LocationProvider(Context context, IDateProvider dateProvider) {
        this(context);
        this.dateProvider = dateProvider;
    }
    
    /**
     * Connects to the Android operating system to start receiving updates on the current location.
     */
    public void Connect() {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        synchronized(lastKnownLocationLock) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minimumTimeBetweenLocationUpdates, minimumDistanceBetweenLocationUpdates, this);
    }
    
    @Override
    public net.alastairwyse.oraclepermissiongenerator.containers.Location getLastKnownLocation() {
        Location lastKnownLocationCopy = null;
        synchronized(lastKnownLocationLock) {
            if (lastKnownLocation == null) {
                return null;
            }
            else {
                lastKnownLocationCopy = new Location(lastKnownLocation);
            }
        }
        
        int secondsSinceUpdate = (int) ((dateProvider.getDate().getTime() - lastKnownLocationCopy.getTime()) / 1000);
        net.alastairwyse.oraclepermissiongenerator.containers.Location returnLocation = new net.alastairwyse.oraclepermissiongenerator.containers.Location();
        returnLocation.setLatitude(lastKnownLocationCopy.getLatitude());
        returnLocation.setLongitude(lastKnownLocationCopy.getLongitude());
        returnLocation.setSecondsSinceUpdate(secondsSinceUpdate);
        return returnLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        synchronized(lastKnownLocationLock) {
            lastKnownLocation = location;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}
