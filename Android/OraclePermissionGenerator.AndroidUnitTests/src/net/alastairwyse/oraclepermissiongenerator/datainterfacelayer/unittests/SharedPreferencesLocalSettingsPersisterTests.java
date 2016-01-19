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

package net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.unittests;

import android.content.Context;
import android.test.AndroidTestCase;
import android.content.SharedPreferences;

import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for class oraclepermissiongenerator.datainterfacelayer.SharedPreferencesLocalSettingsPersister.
 * @author Alastair Wyse
 */
public class SharedPreferencesLocalSettingsPersisterTests extends AndroidTestCase {

    private final String settingsName = "OraclePermissionGeneratorLocalSettings";
    private final String userIdentifierKey = "userIdentifier";
    private final String remoteDataModelProxyTypeKey = "remoteDataModelProxyTypeKey";
    private final String soapDataServiceLocationKey = "soapDataServiceLocation";
    private final String restDataServiceLocationKey = "restDataServiceLocation";
    
    private SharedPreferencesLocalSettingsPersister testSharedPreferencesLocalSettingsPersister;
    private Context mockContext;
    private SharedPreferences mockSharedPreferences;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        mockContext = mock(Context.class);
        mockSharedPreferences = mock(SharedPreferences.class);
        testSharedPreferencesLocalSettingsPersister = new SharedPreferencesLocalSettingsPersister(mockContext);
    }
    
    public void testWriteSuccessTest() throws Exception {
        // TODO: Need to implement this.
        //   Was unable to mock the android.content.SharedPreferences.Editor class used in the Write() method, as Mockito doesn't seem to offer a way of mocking retrieving an inner class from a class.
        //   Hence I could not mock getting the Editor class from the SharedPreferences class.
        //   Might be able to get around this by using Mockito spy (https://mockito.googlecode.com/svn/branches/1.7/javadoc/org/mockito/Mockito.html).
    }
    
    public void testReadSuccessTests() throws Exception {
        final String testUserIdentifier = "user@tempuri.org";
        final RemoteDataModelProxyType testRemoteDataModelProxyType = RemoteDataModelProxyType.SOAP;
        final String testSoapDataServiceLocation = "192.168.1.101:5000";
        final String testRestDataServiceLocation = "192.168.1.101:5001";
        LocalSettings expectedLocalSettings = new LocalSettings();
        expectedLocalSettings.setUserIdentifier(testUserIdentifier);
        expectedLocalSettings.setRemoteDataModelProxyType(testRemoteDataModelProxyType);
        expectedLocalSettings.setSoapDataServiceLocation(testSoapDataServiceLocation);
        expectedLocalSettings.setRestDataServiceLocation(testRestDataServiceLocation);
        
        when(mockContext.getSharedPreferences(settingsName, 0)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.getString(userIdentifierKey, "")).thenReturn(testUserIdentifier);
        when(mockSharedPreferences.getString(remoteDataModelProxyTypeKey, RemoteDataModelProxyType.SOAP.name()))
            .thenReturn(testRemoteDataModelProxyType.name())
            .thenReturn(RemoteDataModelProxyType.REST.name());
        when(mockSharedPreferences.getString(soapDataServiceLocationKey, "")).thenReturn(testSoapDataServiceLocation);
        when(mockSharedPreferences.getString(restDataServiceLocationKey, "")).thenReturn(testRestDataServiceLocation);
        
        LocalSettings returnedLocalSettings = testSharedPreferencesLocalSettingsPersister.Read();
        
        assertEquals(returnedLocalSettings.getUserIdentifier(), expectedLocalSettings.getUserIdentifier());
        assertEquals(returnedLocalSettings.getRemoteDataModelProxyType(), expectedLocalSettings.getRemoteDataModelProxyType());
        assertEquals(returnedLocalSettings.getSoapDataServiceLocation(), expectedLocalSettings.getSoapDataServiceLocation());
        assertEquals(returnedLocalSettings.getRestDataServiceLocation(), expectedLocalSettings.getRestDataServiceLocation());
        
        // Test again returning RemoteDataModelProxyType.REST from the SharedPreferences mock, to ensure both values in the RemoteDataModelProxyType enum are properly deserialized from strings
        expectedLocalSettings.setRemoteDataModelProxyType(RemoteDataModelProxyType.REST);
        
        returnedLocalSettings = testSharedPreferencesLocalSettingsPersister.Read();
        
        assertEquals(returnedLocalSettings.getUserIdentifier(), expectedLocalSettings.getUserIdentifier());
        assertEquals(returnedLocalSettings.getRemoteDataModelProxyType(), RemoteDataModelProxyType.REST);
        assertEquals(returnedLocalSettings.getSoapDataServiceLocation(), expectedLocalSettings.getSoapDataServiceLocation());
        assertEquals(returnedLocalSettings.getRestDataServiceLocation(), expectedLocalSettings.getRestDataServiceLocation());
    }
}
