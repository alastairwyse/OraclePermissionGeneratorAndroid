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
 * Container class storing tracking information about a user of the application.
 * @author Alastair Wyse
 */
public class TrackingData {
    private Location location;
    private byte[] ipV4Address;
    
    /**
     * Initialises a new instance of the TrackingData class.
     */
    public TrackingData() {
    }
    
    /**
     * @return  The physical location of the user.
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * @param location  The physical location of the user.
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    
    /**
     * @return  The IPV4 address of the device running the application.
     */
    public byte[] getIpV4Address() {
        return ipV4Address;
    }
    
    /**
     * @param ipV4Address  The IPV4 address of the device running the application.
     */
    public void setIpV4Address(byte[] ipV4Address) {
        this.ipV4Address = ipV4Address;
    }
}
