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

package net.alastairwyse.oraclepermissiongenerator.frameworkabstraction;

import java.net.SocketException;
import java.util.Enumeration;

/**
 * Provides an abstraction of the java.net.NetworkInterface class to facilitate mocking and unit testing.
 * @author Alastair Wyse
 */
public class NetworkInterface implements INetworkInterface {

    @Override
    public Enumeration<java.net.NetworkInterface> getNetworkInterfaces() throws SocketException {
        return java.net.NetworkInterface.getNetworkInterfaces();
    }

}
