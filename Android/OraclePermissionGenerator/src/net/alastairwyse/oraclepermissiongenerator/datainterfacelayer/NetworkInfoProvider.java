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

import java.net.InetAddress;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.alastairwyse.oraclepermissiongenerator.frameworkabstraction.*;

/**
 * Retrieves information about the networks the host device is connected to.
 * @author Alastair Wyse
 */
public class NetworkInfoProvider implements INetworkInfoProvider {

    private Context context;
    private NetworkInfo mobileNetworkInfo;
    private INetworkInterface networkInterface;
    
    /**
     * Initialises a new instance of the NetworkInfoProvider class.
     * @param  context  The context under which the NetworkInfoProvider is being initialized.
     */
    public NetworkInfoProvider(Context context) {
        this.context = context;
        networkInterface = new NetworkInterface();
    }
    
    /**
     * Initialises a new instance of the NetworkInfoProvider class.
     * <b>Note</b> this is an additional constructor to facilitate unit tests, and should not be used to instantiate the class under normal conditions.
     * @param  context           A test (mock) context.
     * @param  networkInfo       A test (mock) NetworkInfo object.
     * @param  networkInterface  A test (mock) network interface.
     */
    public NetworkInfoProvider(Context context, NetworkInfo networkInfo, INetworkInterface networkInterface) {
        this(context);
        mobileNetworkInfo = networkInfo;
        this.networkInterface = networkInterface;
    }
    
    /**
     * Connects to the Android operating system to enable retrieval of network information.
     */
    public void Connect() {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    }
    
    @Override
    public InetAddress getMobileIpAddress() {
        // TODO: Currently if multiple network interfaces and/or IP addresses are found, simply the first non-loopback one will be returned.
        //         Would be good to make this algorithm a bit more sophisticated.
        if (mobileNetworkInfo.isAvailable() == true) {
            try {
                for (Enumeration<java.net.NetworkInterface> networkInterfaces = networkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements() == true; ) {
                    java.net.NetworkInterface currentNetworkInterface = networkInterfaces.nextElement();
                    for (Enumeration<InetAddress> networkAddresses = currentNetworkInterface.getInetAddresses(); networkAddresses.hasMoreElements() == true; ) {
                        InetAddress currentNetworkAddress = networkAddresses.nextElement();
                        if (currentNetworkAddress.isLoopbackAddress() == false) {
                            return currentNetworkAddress;
                        }
                    }
                }
            }
            catch (Exception e) {
                return null;
            }
        }

        return null;
    }
}
