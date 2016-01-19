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

package net.alastairwyse.oraclepermissiongenerator;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * Defines the methods of an MVP pattern view, which displays and allows modifying of the connection settings of the application. 
 * @author Alastair Wyse
 */
public interface IConnectionSettingsView extends IMvpView {

    /**
     * Populates the view with the unique identifier for the user.
     * @param  userIdentifier  The user identifier.
     */
    void PopulateUserIdentifier(String userIdentifier);
    
    /**
     * Populates the view the with type of proxy to the remote data model instance.
     * @param  remoteDataModelProxyType  The type of proxy to the remote data model instance.
     */
    void PopulateRemoteDataModelProxyType(RemoteDataModelProxyType remoteDataModelProxyType);
    
    /**
     * Populates the view with the network location of the SOAP data service.
     * @param  soapDataServiceLocation  The location of the SOAP data service.
     */
    void PopulateSoapDataServiceLocation(String soapDataServiceLocation);
    
    /**
     * Populates the view with the network location of the REST data service.
     * @param  restDataServiceLocation  The location of the REST data service.
     */
    void PopulateRestDataServiceLocation(String restDataServiceLocation);
}
