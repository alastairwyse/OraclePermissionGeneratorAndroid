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
 * Defines a mapping between a database role and a user.
 * @author Alastair Wyse
 */
public class RoleToUserMap extends StringMap {

    /**
     * Initialises a new instance of the RoleToUserMap class.
     * @param role  The role.
     * @param user  The user.
     */
    public RoleToUserMap(String role, String user) {
        super(1, 30, "Role", "User");
        setMappedFrom(role);
        setMappedTo(user);
    }
    
    /**
     * @return  The role
     */
    public String getRole() {
        return mappedFrom;
    }
    
    /**
     * @return  The user.
     */
    public String getUser() {
        return mappedTo;
    }
}
