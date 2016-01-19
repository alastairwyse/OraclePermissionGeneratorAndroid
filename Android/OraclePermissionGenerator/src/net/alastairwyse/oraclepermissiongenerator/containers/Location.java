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

package net.alastairwyse.oraclepermissiongenerator.containers;

/**
 * Container class holding the physical location (in terms of latitude and longitude) of the device running the application.
 * @author Alastair Wyse
 */
public class Location {
    private double latitude;
    private double longitude;
    private int secondsSinceUpdate;
    
    /**
     * Initialises a new instance of the Location class.
     */
    public Location() {
    }
    
    /**
     * Initialises a new instance of the Location class.
     * @param  latitude            The latitude of the location.
     * @param  longitude           The longitude of the location.
     * @param  secondsSinceUpdate  The number of seconds that have elapsed since the location was recorded.
     */
    public Location(double latitude, double longitude, int secondsSinceUpdate) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.secondsSinceUpdate = secondsSinceUpdate;
    }
    
    /**
     * @return  The latitude of the location.
     */
    public double getLatitude() {
        return latitude;
    }
    
    /**
     * @param latitude  The latitude of the location.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    /**
     * @return  The longitude of the location.
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * @param longitude  The longitude of the location.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    /**
     * @return  The number of seconds that have elapsed since the location was recorded.
     */
    public int getSecondsSinceUpdate() {
        return secondsSinceUpdate;
    }
    
    /**
     * @param  secondsSinceUpdate  The number of seconds that have elapsed since the location was recorded.
     */
    public void setSecondsSinceUpdate(int secondsSinceUpdate) {
        this.secondsSinceUpdate = secondsSinceUpdate;
    }
}
