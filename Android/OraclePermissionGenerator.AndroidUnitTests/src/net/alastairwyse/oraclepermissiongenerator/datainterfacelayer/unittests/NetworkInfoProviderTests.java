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

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.test.*;
import android.net.NetworkInfo;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for class oraclepermissiongenerator.datainterfacelayer.NetworkInfoProvider.
 * @author Alastair Wyse
 */
public class NetworkInfoProviderTests extends AndroidTestCase {
    
    private NetworkInfoProvider testNetworkInfoProvider;
    private Context mockContext;
    private NetworkInfo mockNetworkInfo;
    private net.alastairwyse.oraclepermissiongenerator.frameworkabstraction.INetworkInterface mockNetworkInterface;
    
    // Note - unfortunately success case for the getMobileIpAddress() method cannot be tested, as the java.net.NetworkInterface class 
    //   (which is returned in a collection by the NetworkInterface.getNetworkInterfaces() method) cannot be instantiated, and also is
    //   static, so cannot be mocked by Mockito.
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        mockContext = mock(Context.class);
        mockNetworkInfo = mock(NetworkInfo.class);
        mockNetworkInterface = mock(net.alastairwyse.oraclepermissiongenerator.frameworkabstraction.INetworkInterface.class);
        testNetworkInfoProvider = new NetworkInfoProvider(mockContext, mockNetworkInfo, mockNetworkInterface);
    }
    
    public void testGetMobileIpAddressNetworkNotAvailable() throws Exception {
        when(mockNetworkInfo.isAvailable()).thenReturn(false);

        InetAddress returnedIpAddress = testNetworkInfoProvider.getMobileIpAddress();
        
        verify(mockNetworkInfo).isAvailable();
        assertNull(returnedIpAddress);
        verifyNoMoreInteractions(mockContext, mockNetworkInfo, mockNetworkInterface);
    }
    
    public void testGetMobileIpAddressNetworkInterfacesSocketException() throws Exception {
        when(mockNetworkInfo.isAvailable()).thenReturn(true);
        when(mockNetworkInterface.getNetworkInterfaces()).thenThrow(new SocketException("Mock SocketException."));
        
        InetAddress returnedIpAddress = testNetworkInfoProvider.getMobileIpAddress();
        
        verify(mockNetworkInfo).isAvailable();
        verify(mockNetworkInterface).getNetworkInterfaces();
        assertNull(returnedIpAddress);
        verifyNoMoreInteractions(mockContext, mockNetworkInfo, mockNetworkInterface);
    }
    
    public void testGetMobileIpAddressNoNetworkInterfaces() throws Exception {
        ArrayList<java.net.NetworkInterface> emptyNetworkInterfaceList = new ArrayList<java.net.NetworkInterface>();
        
        when(mockNetworkInfo.isAvailable()).thenReturn(true);
        when(mockNetworkInterface.getNetworkInterfaces()).thenReturn(Collections.enumeration(emptyNetworkInterfaceList));
        
        InetAddress returnedIpAddress = testNetworkInfoProvider.getMobileIpAddress();
        
        verify(mockNetworkInfo).isAvailable();
        verify(mockNetworkInterface).getNetworkInterfaces();
        assertNull(returnedIpAddress);
        verifyNoMoreInteractions(mockContext, mockNetworkInfo, mockNetworkInterface);
    }
}
