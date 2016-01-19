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

package net.alastairwyse.oraclepermissiongenerator.containers.unittests;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for class oraclepermissiongenerator.containers.RoleToPermissionMap.
 * @author Alastair Wyse
 */
public class RoleToPermissionMapTests {

    private RoleToPermissionMap testRoleToPermissionMap;
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void InvalidRoleArgument() {
        try {
            testRoleToPermissionMap = new RoleToPermissionMap("", "SELECT");
            fail("Exception was not thrown.");
        }
        catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Argument 'Role' must be between 1 and 30 characters in length."));
        }
        
        try {
            testRoleToPermissionMap = new RoleToPermissionMap("0123456789012345678901234567891", "SELECT");
            fail("Exception was not thrown.");
        }
        catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Argument 'Role' must be between 1 and 30 characters in length."));
        }
    }
    
    @Test
    public void InvalidPermissionArgument() {
        try {
            testRoleToPermissionMap = new RoleToPermissionMap("XYZON_READ_ROLE", "");
            fail("Exception was not thrown.");
        }
        catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Argument 'Permission' must be between 1 and 30 characters in length."));
        }
        
        try {
            testRoleToPermissionMap = new RoleToPermissionMap("XYZON_READ_ROLE", "0123456789012345678901234567891");
            fail("Exception was not thrown.");
        }
        catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Argument 'Permission' must be between 1 and 30 characters in length."));
        }
    }
}
