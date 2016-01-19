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

package net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.serialization.unittests;

import java.util.*;

import android.test.*;

import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.serialization.*;

/**
 * Unit tests for class oraclepermissiongenerator.datainterfacelayer.serialization.ContainerObjectJsonSerializer.
 * @author Alastair Wyse
 */
public class ContainerObjectJsonSerializerTests extends AndroidTestCase {
    
    private ContainerObjectJsonSerializer testContainerObjectJsonSerializer;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        testContainerObjectJsonSerializer = new ContainerObjectJsonSerializer();
    }

    public void testDeserializeOracleObjectPermissionSetArrayListSuccessTest() throws Exception {
        final String serializedList = "[{\"AddFlag\":true,\"ObjectName\":\"SP_INFORCE_INS\",\"ObjectOwner\":\"TEST_OWNER\",\"ObjectPermissions\":[{\"Permission\":\"EXECUTE\",\"Role\":\"APP_ROLE\"},{\"Permission\":\"EXECUTE\",\"Role\":\"GUI_ROLE\"}],\"ObjectType\":\"Stored Procedure\",\"RemoveFlag\":false},{\"AddFlag\":true,\"ObjectName\":\"SP_INFORCE_DEL\",\"ObjectOwner\":\"TEST_OWNER\",\"ObjectPermissions\":[{\"Permission\":\"EXECUTE\",\"Role\":\"APP_ROLE\"},{\"Permission\":\"EXECUTE\",\"Role\":\"GUI_ROLE\"}],\"ObjectType\":\"Stored Procedure\",\"RemoveFlag\":false},{\"AddFlag\":false,\"ObjectName\":\"VW_INFORCE\",\"ObjectOwner\":\"TEST_OWNER\",\"ObjectPermissions\":[{\"Permission\":\"SELECT\",\"Role\":\"APP_ROLE\"},{\"Permission\":\"SELECT\",\"Role\":\"READ_ROLE\"}],\"ObjectType\":\"View\",\"RemoveFlag\":true}]";
        
        ArrayList<OracleObjectPermissionSet> returnedList = testContainerObjectJsonSerializer.DeserializeOracleObjectPermissionSetArrayList(serializedList);
        
        assertEquals(3, returnedList.size());
        assertEquals("SP_INFORCE_INS", returnedList.get(0).getObjectName());
        assertEquals("Stored Procedure", returnedList.get(0).getObjectType());
        assertEquals("TEST_OWNER", returnedList.get(0).getObjectOwner());
        assertEquals(true, returnedList.get(0).getAddFlag());
        assertEquals(false, returnedList.get(0).getRemoveFlag());
        assertEquals("APP_ROLE", returnedList.get(0).getObjectPermissions().get(0).getRole());
        assertEquals("EXECUTE", returnedList.get(0).getObjectPermissions().get(0).getPermission());
        assertEquals("GUI_ROLE", returnedList.get(0).getObjectPermissions().get(1).getRole());
        assertEquals("EXECUTE", returnedList.get(0).getObjectPermissions().get(1).getPermission());
        
        assertEquals(3, returnedList.size());
        assertEquals("SP_INFORCE_DEL", returnedList.get(1).getObjectName());
        assertEquals("Stored Procedure", returnedList.get(1).getObjectType());
        assertEquals("TEST_OWNER", returnedList.get(1).getObjectOwner());
        assertEquals(true, returnedList.get(1).getAddFlag());
        assertEquals(false, returnedList.get(1).getRemoveFlag());
        assertEquals("APP_ROLE", returnedList.get(1).getObjectPermissions().get(0).getRole());
        assertEquals("EXECUTE", returnedList.get(1).getObjectPermissions().get(0).getPermission());
        assertEquals("GUI_ROLE", returnedList.get(1).getObjectPermissions().get(1).getRole());
        assertEquals("EXECUTE", returnedList.get(1).getObjectPermissions().get(1).getPermission());
        
        assertEquals(3, returnedList.size());
        assertEquals("VW_INFORCE", returnedList.get(2).getObjectName());
        assertEquals("View", returnedList.get(2).getObjectType());
        assertEquals("TEST_OWNER", returnedList.get(2).getObjectOwner());
        assertEquals(false, returnedList.get(2).getAddFlag());
        assertEquals(true, returnedList.get(2).getRemoveFlag());
        assertEquals("APP_ROLE", returnedList.get(2).getObjectPermissions().get(0).getRole());
        assertEquals("SELECT", returnedList.get(2).getObjectPermissions().get(0).getPermission());
        assertEquals("READ_ROLE", returnedList.get(2).getObjectPermissions().get(1).getRole());
        assertEquals("SELECT", returnedList.get(2).getObjectPermissions().get(1).getPermission());
    }
    
    public void testSerializeAuthenticationContextSuccessTest() throws Exception {
        final String expectedSerializedAuthenticationContext = "{\"UserIdentifier\":\"test@tempuri.org\"}";
        AuthenticationContext authenticationContext = new AuthenticationContext("test@tempuri.org");
        
        String returnedString = testContainerObjectJsonSerializer.SerializeAuthenticationContext(authenticationContext);
        
        assertEquals(expectedSerializedAuthenticationContext, returnedString);
    }
    
    public void testSerializeTrackingDataSuccessTest() throws Exception {
        final String expectedSerializedTrackingData = "{\"IpV4Address\":[192,168,2,101],\"Location\":{\"Latitude\":35.6895,\"SecondsSinceUpdate\":23,\"Longitude\":139.6917}}";
        TrackingData trackingData = new TrackingData();
        trackingData.setLocation(new Location(35.6895, 139.6917, 23));
        trackingData.setIpV4Address(new byte[]{(byte)192, (byte)168, (byte)2, (byte)101});
        
        String returnedString = testContainerObjectJsonSerializer.SerializeTrackingData(trackingData);
        
        assertEquals(expectedSerializedTrackingData, returnedString);
    }
    
    public void testSerializeTrackingDataIpAddressNull() throws Exception {
        final String expectedSerializedTrackingData = "{\"Location\":{\"Latitude\":35.6895,\"SecondsSinceUpdate\":17,\"Longitude\":139.6917}}";
        TrackingData testTrackingData = new TrackingData();
        testTrackingData.setLocation(new Location(35.6895, 139.6917, 17));
        testTrackingData.setIpV4Address(null);
        
        String returnedString = testContainerObjectJsonSerializer.SerializeTrackingData(testTrackingData);
        
        assertEquals(expectedSerializedTrackingData, returnedString);
    }
    
    public void testSerializeTrackingDataLocationNull() throws Exception {
        final String expectedSerializedTrackingData = "{\"IpV4Address\":[192,168,2,101]}";
        TrackingData testTrackingData = new TrackingData();
        testTrackingData.setLocation(null);
        testTrackingData.setIpV4Address(new byte[]{(byte)192, (byte)168, (byte)2, (byte)101});
        
        String returnedString = testContainerObjectJsonSerializer.SerializeTrackingData(testTrackingData);
        
        assertEquals(expectedSerializedTrackingData, returnedString);
    }
    
    public void testSerializeTrackingDataBothMembersNull() throws Exception {
        final String expectedSerializedTrackingData = "{}";
        TrackingData testTrackingData = new TrackingData();
        testTrackingData.setLocation(null);
        testTrackingData.setIpV4Address(null);

        String returnedString = testContainerObjectJsonSerializer.SerializeTrackingData(testTrackingData);
        
        assertEquals(expectedSerializedTrackingData, returnedString);
    }
    
    public void testSerializeStringSuccessTest() throws Exception {
        final String expectedSerializedString = "{\"objectName\":\"SP_INFORCE_INS\"}";
        
        String returnedString = testContainerObjectJsonSerializer.SerializeString("objectName", "SP_INFORCE_INS");
        
        assertEquals(expectedSerializedString, returnedString);
    }
    
    public void testSerializeBooleanSuccessTest() throws Exception {
        String expectedSerializedString = "{\"addFlagValue\":false}";
        
        String returnedString = testContainerObjectJsonSerializer.SerializeBoolean("addFlagValue", false);
        
        assertEquals(expectedSerializedString, returnedString);
        
        expectedSerializedString = "{\"addFlagValue\":true}";
        
        returnedString = testContainerObjectJsonSerializer.SerializeBoolean("addFlagValue", true);
        
        assertEquals(expectedSerializedString, returnedString);
    }
    
    public void testSerializeRoleToPermissionMapArrayListSuccessTest() throws Exception {
        String expectedSerializedRoleToPermissionMap = "[{\"Role\":\"XYZON_READ_ROLE\",\"Permission\":\"SELECT\"},{\"Role\":\"XZYON_APP_ROLE\",\"Permission\":\"SELECT\"},{\"Role\":\"XZYON_APP_ROLE\",\"Permission\":\"INSERT\"}]";
        
        ArrayList<RoleToPermissionMap> testRoleToPermissionMapArrayList = new ArrayList<RoleToPermissionMap>();
        testRoleToPermissionMapArrayList.add(new RoleToPermissionMap("XYZON_READ_ROLE", "SELECT"));
        testRoleToPermissionMapArrayList.add(new RoleToPermissionMap("XZYON_APP_ROLE", "SELECT"));
        testRoleToPermissionMapArrayList.add(new RoleToPermissionMap("XZYON_APP_ROLE", "INSERT"));
        
        String returnedString = testContainerObjectJsonSerializer.SerializeRoleToPermissionMapArrayList(testRoleToPermissionMapArrayList);
        
        assertEquals(expectedSerializedRoleToPermissionMap, returnedString);
    }
    
    public void testDeserializeStringArrayListSuccessTest() throws Exception {
        String serializedString = "[\"View\",\"Stored Procedure\",\"Table\"]";
        
        ArrayList<String> returnedArrayList = testContainerObjectJsonSerializer.DeserializeStringArrayList(serializedString);
        
        assertEquals("View", returnedArrayList.get(0));
        assertEquals("Stored Procedure", returnedArrayList.get(1));
        assertEquals("Table", returnedArrayList.get(2));
        
        serializedString = "[]";

        returnedArrayList = testContainerObjectJsonSerializer.DeserializeStringArrayList(serializedString);
        
        assertEquals(0, returnedArrayList.size());
    }
    
    public void testDeserializeValidationResultSuccessTest() throws Exception {
        String serializedValidationResult = "{\"IsValid\":false,\"ValidationError\":\"The object 'SP_INFORCE_INS' already exists\"}";
        
        ValidationResult returnedValidationResult = testContainerObjectJsonSerializer.DeserializeValidationResult(serializedValidationResult);
        
        assertEquals(returnedValidationResult.getIsValid(), false);
        assertEquals(returnedValidationResult.getValidationError(), "The object 'SP_INFORCE_INS' already exists");
        
        serializedValidationResult = "{\"IsValid\":true,\"ValidationError\":\"\"}";
        
        returnedValidationResult = testContainerObjectJsonSerializer.DeserializeValidationResult(serializedValidationResult);
        
        assertEquals(returnedValidationResult.getIsValid(), true);
        assertEquals(returnedValidationResult.getValidationError(), "");
    }
    
    public void testDeserializeRoleToPermissionMapArrayListSuccessTest() throws Exception {
        String serializedRoleToPermissionMap = "[{\"Role\":\"XYZON_READ_ROLE\",\"Permission\":\"SELECT\"},{\"Role\":\"XZYON_APP_ROLE\",\"Permission\":\"SELECT\"},{\"Role\":\"XZYON_APP_ROLE\",\"Permission\":\"INSERT\"}]";
        
        ArrayList<RoleToPermissionMap> returnedRoleToPermissionMapArrayList = testContainerObjectJsonSerializer.DeserializeRoleToPermissionMapArrayList(serializedRoleToPermissionMap);
        
        assertEquals(returnedRoleToPermissionMapArrayList.get(0).getRole(), "XYZON_READ_ROLE");
        assertEquals(returnedRoleToPermissionMapArrayList.get(0).getPermission(), "SELECT");
        assertEquals(returnedRoleToPermissionMapArrayList.get(1).getRole(), "XZYON_APP_ROLE");
        assertEquals(returnedRoleToPermissionMapArrayList.get(1).getPermission(), "SELECT");
        assertEquals(returnedRoleToPermissionMapArrayList.get(2).getRole(), "XZYON_APP_ROLE");
        assertEquals(returnedRoleToPermissionMapArrayList.get(2).getPermission(), "INSERT");
        assertEquals(returnedRoleToPermissionMapArrayList.size(), 3);
        
        serializedRoleToPermissionMap = "[]";
        
        returnedRoleToPermissionMapArrayList = testContainerObjectJsonSerializer.DeserializeRoleToPermissionMapArrayList(serializedRoleToPermissionMap);
        
        assertEquals(returnedRoleToPermissionMapArrayList.size(), 0);
    }
    
    public void testDeserializeRoleToUserMapArrayListSuccessTest() throws Exception {
        String serializedRoleToUserMap = "[{\"Role\":\"XYZON_POWER_ROLE\",\"User\":\"XYZON_POWER_USER\"},{\"Role\":\"XYZON_READ_ROLE\",\"User\":\"JONES_SAM\"},{\"Role\":\"XYZON_READ_ROLE\",\"User\":\"SMITH_JOHN\"}]";
        
        ArrayList<RoleToUserMap> returnedRoleToUserMapArrayList = testContainerObjectJsonSerializer.DeserializeRoleToUserMapArrayList(serializedRoleToUserMap);
        
        assertEquals(returnedRoleToUserMapArrayList.get(0).getRole(), "XYZON_POWER_ROLE");
        assertEquals(returnedRoleToUserMapArrayList.get(0).getUser(), "XYZON_POWER_USER");
        assertEquals(returnedRoleToUserMapArrayList.get(1).getRole(), "XYZON_READ_ROLE");
        assertEquals(returnedRoleToUserMapArrayList.get(1).getUser(), "JONES_SAM");
        assertEquals(returnedRoleToUserMapArrayList.get(2).getRole(), "XYZON_READ_ROLE");
        assertEquals(returnedRoleToUserMapArrayList.get(2).getUser(), "SMITH_JOHN");
        assertEquals(returnedRoleToUserMapArrayList.size(), 3);
        
        serializedRoleToUserMap = "[]";
        
        returnedRoleToUserMapArrayList = testContainerObjectJsonSerializer.DeserializeRoleToUserMapArrayList(serializedRoleToUserMap);
        
        assertEquals(returnedRoleToUserMapArrayList.size(), 0);
    }
}
