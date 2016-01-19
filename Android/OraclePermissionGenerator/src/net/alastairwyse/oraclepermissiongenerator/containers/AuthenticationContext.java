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
 * Container class holding items and properties used to authenticate the user of the application.
 * @author Alastair Wyse
 */
public class AuthenticationContext {

    private String userIdentifier;
    
    /**
     * Initialises a new instance of the AuthenticationContext class.
     */
    public AuthenticationContext() {
    }
    
    /**
     * Initialises a new instance of the AuthenticationContext class.
     * @param  userIdentifier  A string that uniquely identifies a user.
     */
    public AuthenticationContext(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
    
    /**
      * @return  A string that uniquely identifies a user.
     */
    public String getUserIdentifier() {
        return userIdentifier;
    }
    
    /**
     * @param  userIdentifier  A string that uniquely identifies a user.
     */
    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
}
