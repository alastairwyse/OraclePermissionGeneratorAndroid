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
 * Container class holding a set of local settings for the application.
 * @author Alastair Wyse
 */
public class LocalSettings {

    private String userIdentifier; 
    private RemoteDataModelProxyType remoteDataModelProxyType;
    private String soapDataServiceLocation;
    private String restDataServiceLocation;
    
    /**
     * @return  The unique identifier of the user of the application.
     */
    public String getUserIdentifier() {
        return userIdentifier;
    }
    
    /**
     * @param  userIdentifier  The unique identifier of the user of the application.
     */
    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
    
    /**
     * @return  The mechanism of proxy to the remote instance of the data model.
     */
    public RemoteDataModelProxyType getRemoteDataModelProxyType() {
        return remoteDataModelProxyType;
    }
    
    /**
     * 
     * @param  remoteDataModelProxyType  The mechanism of proxy to the remote instance of the data model.
     */
    public void setRemoteDataModelProxyType(RemoteDataModelProxyType remoteDataModelProxyType) {
        this.remoteDataModelProxyType = remoteDataModelProxyType;
    }
    
    /**
     * @return  The location (IP address or hostname and port) of the SOAP data service.
     */
    public String getSoapDataServiceLocation() {
        return soapDataServiceLocation;
    }
    
    /**
     * @param  soapDataServiceLocation  The location (IP address or hostname and port) of the SOAP data service, for example '192.168.0.101:5000'.
     */
    public void setSoapDataServiceLocation(String soapDataServiceLocation) {
        this.soapDataServiceLocation = soapDataServiceLocation;
    }
    
    /**
     * @return  The location (IP address or hostname and port) of the REST data service.
     */
    public String getRestDataServiceLocation() {
        return restDataServiceLocation;
    }
    
    /**
     * @param  restDataServiceLocation  The location (IP address or hostname and port) of the REST data service, for example '192.168.0.101:5001'.
     */
    public void setRestDataServiceLocation(String restDataServiceLocation) {
        this.restDataServiceLocation = restDataServiceLocation;
    }
}
