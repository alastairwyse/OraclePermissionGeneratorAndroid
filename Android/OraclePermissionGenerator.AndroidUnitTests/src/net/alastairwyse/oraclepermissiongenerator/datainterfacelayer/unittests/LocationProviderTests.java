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

import java.util.Date;

import android.content.Context;
import android.test.AndroidTestCase;
import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;
import net.alastairwyse.oraclepermissiongenerator.frameworkabstraction.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for class oraclepermissiongenerator.datainterfacelayer.LocationProvider.
 * @author Alastair Wyse
 */
public class LocationProviderTests extends AndroidTestCase {
    
    private LocationProvider testLocationProvider;
    private Context mockContext;
    private IDateProvider mockDateProvider;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        mockContext = mock(Context.class);
        mockDateProvider = mock(IDateProvider.class);
        testLocationProvider = new LocationProvider(mockContext, mockDateProvider);
    }
    
    public void testGetLastKnownLocationLastKnownLocationNull() throws Exception {
        // Tests the case where the call to LocationManager.getLastKnownLocation() inside the Connect() method returns null
        Location returnedLocation = testLocationProvider.getLastKnownLocation();
        
        assertNull(returnedLocation);
        verifyNoMoreInteractions(mockContext, mockDateProvider);
    }
    
    public void testGetLastKnownLocationSuccessTest() throws Exception {
        android.location.Location testLocation = new android.location.Location("UnitTestCode");
        testLocation.setTime(1437745580);
        testLocation.setLatitude(35.6833);
        testLocation.setLongitude(139.6833);
        Date testDate = new Date(1437748581);

        when(mockDateProvider.getDate()).thenReturn(testDate);
        
        testLocationProvider.onLocationChanged(testLocation);
        Location returnedLocation = testLocationProvider.getLastKnownLocation();
        
        verify(mockDateProvider).getDate();
        assertEquals(35.6833, returnedLocation.getLatitude());
        assertEquals(139.6833, returnedLocation.getLongitude());
        assertEquals(3, returnedLocation.getSecondsSinceUpdate());
        verifyNoMoreInteractions(mockContext, mockDateProvider);
    }
}
