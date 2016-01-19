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
 * Unit tests for class oraclepermissiongenerator.containers.OracleObjectPermissionSetTests.
 * @author Alastair Wyse
 */
public class OracleObjectPermissionSetTests {

    private OracleObjectPermissionSet testOracleObjectPermissionSet;
    
    @Before
    public void setUp() {
        testOracleObjectPermissionSet = new OracleObjectPermissionSet();
    }
    
    @Test
    public void SetObjectNameSuccessTest()
    {
        testOracleObjectPermissionSet.setObjectName("customers");
        assertEquals("CUSTOMERS", testOracleObjectPermissionSet.getObjectName());
    }
    @Test
    public void SetObjectTypeSuccessTest()
    {
        testOracleObjectPermissionSet.setObjectType("Stored Procedure");
        assertEquals("Stored Procedure", testOracleObjectPermissionSet.getObjectType());
    }

    @Test
    public void SetObjectOwnerSuccessTest()
    {
        testOracleObjectPermissionSet.setObjectOwner("xyzon");
        assertEquals("XYZON", testOracleObjectPermissionSet.getObjectOwner());
    }
}
