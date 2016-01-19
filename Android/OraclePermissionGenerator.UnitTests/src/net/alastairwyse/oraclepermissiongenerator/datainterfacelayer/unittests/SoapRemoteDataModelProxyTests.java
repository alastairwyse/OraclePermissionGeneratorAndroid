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

import java.util.ArrayList;
import java.util.Arrays;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.serialization.ContainerObjectJsonSerializer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for class oraclepermissiongenerator.datainterfacelayer.SoapRemoteDataModelProxy.
 * @author Alastair Wyse
 */
public class SoapRemoteDataModelProxyTests {

    private final String testDataServiceLocation = "192.168.0.1:5000";
    private final String testUserIdentifier = "test_user@tempuri.org";
    private AuthenticationContext testAuthenticationContext;
    private TrackingData testTrackingData;
    
    private SoapRemoteDataModelProxy testSoapRemoteDataModelProxy;
    private ContainerObjectJsonSerializer mockJsonSerializer;
    private SoapObject mockSoapObject;
    private SoapSerializationEnvelope mockSoapSerializationEnvelope;
    private HttpTransportSE mockHttpTransportSE;
    
    @Before
    public void setUp() {
        testAuthenticationContext = new AuthenticationContext(testUserIdentifier);
        Location testLocation = new Location();
        testLocation.setLatitude(35.6895);
        testLocation.setLongitude(139.6917);
        testLocation.setSecondsSinceUpdate(23);
        byte[] ipV4Address = new byte[] {60, 40, -128, -127};
        testTrackingData = new TrackingData();
        testTrackingData.setLocation(testLocation);
        testTrackingData.setIpV4Address(ipV4Address);
        
        mockJsonSerializer = mock(ContainerObjectJsonSerializer.class);
        mockSoapObject = mock(SoapObject.class);
        mockSoapSerializationEnvelope = mock(SoapSerializationEnvelope.class);
        mockHttpTransportSE = mock(HttpTransportSE.class);
        testSoapRemoteDataModelProxy = new SoapRemoteDataModelProxy(mockJsonSerializer, mockSoapObject, mockSoapSerializationEnvelope, mockHttpTransportSE);
        testSoapRemoteDataModelProxy.setDataModelLocation(testDataServiceLocation);
    }
    
    @Test
    public void DeserializeSoapStringSuccessTests() throws Exception {
        // Tests private method DeserializeSoapString() inside the SoapRemoteDataModelProxy class via public method getDefaultObjectOwner()
        
        final String testDefaultObjectOwner = "DEFAULT_OWNER";
        final String nullDefaultObjectOwner = "anyType{}";
        final String delimitedDefaultObjectOwner = "DEF\"AULT_OW\"NER";

        when(mockSoapSerializationEnvelope.getResponse())
            .thenReturn(testDefaultObjectOwner)
            .thenReturn(nullDefaultObjectOwner)
            .thenReturn(delimitedDefaultObjectOwner);
        
        String returnedDefaultObjectOwner = testSoapRemoteDataModelProxy.getDefaultObjectOwner(new AuthenticationContext(), new TrackingData());

        assertEquals(testDefaultObjectOwner, returnedDefaultObjectOwner);
        
        // If a null is returned, this is serialized as the string "anyType{}"... confirm this is deserialized to a blank string
        returnedDefaultObjectOwner = testSoapRemoteDataModelProxy.getDefaultObjectOwner(new AuthenticationContext(), new TrackingData());
        
        assertEquals("", returnedDefaultObjectOwner);
        
        // Check that delimiting characters are correctly preserved in went received in the serialized response
        returnedDefaultObjectOwner = testSoapRemoteDataModelProxy.getDefaultObjectOwner(new AuthenticationContext(), new TrackingData());
        
        assertEquals("DEF\"AULT_OW\"NER", returnedDefaultObjectOwner);
        
        verify(mockSoapSerializationEnvelope, times(3)).getResponse();
    }
    
    @Test
    public void MakeSoapRequestSuccessTest() throws Exception {
        // Tests common method calls in private method MakeSoapRequest() inside the SoapRemoteDataModelProxy class via public method getDefaultObjectOwner()
        
        final String testSerializedAuthenticationContext = "{\"UserIdentifier\":\"test_user@tempuri.org\"}";
        final String testDefaultObjectOwner = "DEFAULT_OWNER";
        final String testSerializedTrackingData = "{\"IpV4Address\":[192,168,0,1],\"Location\":{\"Latitude\":35.6895,\"SecondsSinceUpdate\":23,\"Longitude\":139.6917}}";
        final String expectedXmlVersionTag = "<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->";
        
        when(mockJsonSerializer.SerializeAuthenticationContext(testAuthenticationContext)).thenReturn(testSerializedAuthenticationContext);
        when(mockJsonSerializer.SerializeTrackingData(testTrackingData)).thenReturn(testSerializedTrackingData);
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testDefaultObjectOwner);

        testSoapRemoteDataModelProxy.getDefaultObjectOwner(testAuthenticationContext, testTrackingData);
        
        verify(mockJsonSerializer).SerializeAuthenticationContext(testAuthenticationContext);
        verify(mockSoapObject).addProperty("authenticationContext", testSerializedAuthenticationContext);
        verify(mockJsonSerializer).SerializeTrackingData(testTrackingData);
        verify(mockSoapObject).addProperty("trackingData", testSerializedTrackingData);
        verify(mockSoapSerializationEnvelope).setOutputSoapObject(mockSoapObject);
        verify(mockHttpTransportSE).setXmlVersionTag(expectedXmlVersionTag);
        verify(mockSoapSerializationEnvelope).getResponse();
    }
    
    @Test
    public void AddObjectPermissionSetSuccessTest() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        final String testObjectType = "Stored Procedure";
        final String testObjectOwner = "DEFAULT_OWNER";
        final boolean testAddFlag = true;
        final boolean testRemoveFlag = false;
        ArrayList<RoleToPermissionMap> testObjectPermissions = new ArrayList<RoleToPermissionMap>();
        testObjectPermissions.add(new RoleToPermissionMap("XYZON_APP_ROLE", "EXECUTE"));
        testObjectPermissions.add(new RoleToPermissionMap("XYZON_POWER_ROLE", "EXECUTE"));
        final String expectedSerializedRoleToPermissionMap = "[{\"Role\":\"XYZON_APP_ROLE\",\"Permission\":\"EXECUTE\"},{\"Role\":\"XYZON_POWER_ROLE\",\"Permission\":\"EXECUTE\"}]";
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/AddObjectPermissionSet";
        
        when(mockJsonSerializer.SerializeRoleToPermissionMapArrayList(testObjectPermissions)).thenReturn(expectedSerializedRoleToPermissionMap);
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(null);
        
        testSoapRemoteDataModelProxy.AddObjectPermissionSet(testObjectName, testObjectType, testObjectOwner, testAddFlag, testRemoveFlag, testObjectPermissions, testAuthenticationContext, testTrackingData);
        
        verify(mockJsonSerializer).SerializeRoleToPermissionMapArrayList(testObjectPermissions);
        verify(mockSoapObject).addProperty("objectName", testObjectName);
        verify(mockSoapObject).addProperty("objectType", testObjectType);
        verify(mockSoapObject).addProperty("objectOwner", testObjectOwner);
        verify(mockSoapObject).addProperty("addFlag", testAddFlag);
        verify(mockSoapObject).addProperty("removeFlag", testRemoveFlag);
        verify(mockSoapObject).addProperty("objectPermissions", expectedSerializedRoleToPermissionMap);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
    }
    
    @Test
    public void RemoveObjectPermissionSetSuccessTest() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/RemoveObjectPermissionSet";
        
        testSoapRemoteDataModelProxy.RemoveObjectPermissionSet(testObjectName, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("objectName", testObjectName);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
    }
    
    @Test
    public void ObjectNameValidateSuccessTest() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        final String testSoapResponse = "{\"IsValid\":false,\"ValidationError\":\"The object 'SP_CUSTOMERS_INS' already exists\"}";
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/ObjectNameValidate";
        ValidationResult testValidationResult = new ValidationResult(false, "The object 'SP_CUSTOMERS_INS' already exists");
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeValidationResult(testSoapResponse)).thenReturn(testValidationResult);
        
        ValidationResult returnedValidationResult = testSoapRemoteDataModelProxy.ObjectNameValidate(testObjectName, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("objectName", testObjectName);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeValidationResult(testSoapResponse);
        assertEquals(testValidationResult, returnedValidationResult);
    }
    
    @Test
    public void ObjectOwnerValidateSuccessTest() throws Exception {
        final String testObjectOwner = "LONG_OBJECT_OWNER_NAME012345678";
        final String testSoapResponse = "{\"IsValid\":false,\"ValidationError\":\"The object owner must be between 1 and 30 characters in length\"}";
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/ObjectOwnerValidate";
        ValidationResult testValidationResult = new ValidationResult(false, "The object owner must be between 1 and 30 characters in length");
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeValidationResult(testSoapResponse)).thenReturn(testValidationResult);
        
        ValidationResult returnedValidationResult = testSoapRemoteDataModelProxy.ObjectOwnerValidate(testObjectOwner, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("objectOwner", testObjectOwner);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeValidationResult(testSoapResponse);
        assertEquals(testValidationResult, returnedValidationResult);
    }
    
    @Test
    public void getDefaultObjectOwnerSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/GetDefaultObjectOwner";
        final String testSoapResponse = "XYZON";
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        
        String returnedDefaultObjectOwner = testSoapRemoteDataModelProxy.getDefaultObjectOwner(testAuthenticationContext, testTrackingData);
        
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        assertEquals("XYZON", returnedDefaultObjectOwner);
    }
    
    @Test
    public void setDefaultObjectOwnerSuccessTest() throws Exception {
        final String testObjectOwner = "OBJECT_OWNER";
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/SetDefaultObjectOwner";
        
        testSoapRemoteDataModelProxy.setDefaultObjectOwner(testObjectOwner, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("defaultObjectOwner", testObjectOwner);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
    }
    
    @Test
    public void getObjectTypesSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/GetObjectTypes";
        final String testSoapResponse = "[\"View\",\"Stored Procedure\",\"Table\"]";
        ArrayList<String> testObjectTypeList = new ArrayList<String>(Arrays.asList("View", "Stored Procedure", "Table"));
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeStringArrayList(testSoapResponse)).thenReturn(testObjectTypeList);
        
        ArrayList<String> returnedObjectTypeList = testSoapRemoteDataModelProxy.getObjectTypes(testAuthenticationContext, testTrackingData);
        
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeStringArrayList(testSoapResponse);
        assertEquals(testObjectTypeList, returnedObjectTypeList);
    }
    
    @Test
    public void getObjectsSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/GetObjects";
        final String testSoapResponse = "[{\"AddFlag\":false,\"ObjectName\":\"CUSTOMERS\",\"ObjectOwner\":\"XYZON\",\"ObjectPermissions\":[{\"Permission\":\"SELECT\",\"Role\":\"XYZON_POWER_ROLE\"},{\"Permission\":\"SELECT\",\"Role\":\"XYZON_READ_ROLE\"}],\"ObjectType\":\"Table\",\"RemoveFlag\":false},{\"AddFlag\":true,\"ObjectName\":\"SP_CUSTOMERS_INS\",\"ObjectOwner\":\"XYZON\",\"ObjectPermissions\":[{\"Permission\":\"EXECUTE\",\"Role\":\"XYZON_POWER_ROLE\"},{\"Permission\":\"EXECUTE\",\"Role\":\"XYZON_APP_ROLE\"}],\"ObjectType\":\"Stored Procedure\",\"RemoveFlag\":false},{\"AddFlag\":true,\"ObjectName\":\"SP_CUSTOMERS_UPD\",\"ObjectOwner\":\"XYZON\",\"ObjectPermissions\":[{\"Permission\":\"EXECUTE\",\"Role\":\"XYZON_POWER_ROLE\"},{\"Permission\":\"EXECUTE\",\"Role\":\"XYZON_APP_ROLE\"}],\"ObjectType\":\"Stored Procedure\",\"RemoveFlag\":false}]";
        ArrayList<OracleObjectPermissionSet> testObjects = new ArrayList<OracleObjectPermissionSet>();
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeOracleObjectPermissionSetArrayList(testSoapResponse)).thenReturn(testObjects);
        
        ArrayList<OracleObjectPermissionSet> returnedObjects = testSoapRemoteDataModelProxy.getObjects(testAuthenticationContext, testTrackingData);
        
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeOracleObjectPermissionSetArrayList(testSoapResponse);
        assertEquals(testObjects, returnedObjects);
    }
    
    @Test
    public void SetAddFlagSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/SetAddFlag";
        final String testObjectName = "SP_CUSTOMERS_INS";
        
        testSoapRemoteDataModelProxy.SetAddFlag(testObjectName, true, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("objectName", testObjectName);
        verify(mockSoapObject).addProperty("addFlagValue", true);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
    }

    @Test
    public void SetRemoveFlagSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/SetRemoveFlag";
        final String testObjectName = "SP_CUSTOMERS_INS";
        
        testSoapRemoteDataModelProxy.SetRemoveFlag(testObjectName, true, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("objectName", testObjectName);
        verify(mockSoapObject).addProperty("removeFlagValue", true);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
    }
    
    @Test
    public void AddPermissionSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/AddPermission";
        final String testObjectName = "SP_CUSTOMERS_INS";
        final String testRole = "XYZON_APP_ROLE";
        final String testPermission = "EXECUTE";
        
        testSoapRemoteDataModelProxy.AddPermission(testObjectName, testRole, testPermission, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("objectName", testObjectName);
        verify(mockSoapObject).addProperty("role", testRole);
        verify(mockSoapObject).addProperty("permission", testPermission);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
    }
    
    @Test
    public void RemovePermissionSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/RemovePermission";
        final String testObjectName = "SP_CUSTOMERS_INS";
        final String testRole = "XYZON_APP_ROLE";
        final String testPermission = "EXECUTE";
        
        testSoapRemoteDataModelProxy.RemovePermission(testObjectName, testRole, testPermission, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("objectName", testObjectName);
        verify(mockSoapObject).addProperty("role", testRole);
        verify(mockSoapObject).addProperty("permission", testPermission);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
    }
    
    @Test
    public void getRolesSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/GetRoles";
        final String testSoapResponse = "[\"XYZON_READ_ROLE\",\"XYZON_POWER_ROLE\",\"XYZON_APP_ROLE\"]";
        ArrayList<String> testRoleList = new ArrayList<String>(Arrays.asList("XYZON_READ_ROLE", "XYZON_POWER_ROLE", "XYZON_APP_ROLE"));
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeStringArrayList(testSoapResponse)).thenReturn(testRoleList);
        
        ArrayList<String> returnedRoleList = testSoapRemoteDataModelProxy.getRoles(testAuthenticationContext, testTrackingData);
        
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeStringArrayList(testSoapResponse);
        assertEquals(testRoleList, returnedRoleList);
    }
    
    @Test
    public void getPermissionsForObjectTypeSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/GetPermissions";
        final String testSoapResponse = "[\"SELECT\",\"INSERT\",\"UPDATE\",\"DELETE\"]";
        ArrayList<String> testPermissionList = new ArrayList<String>(Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE"));
        final String testObjectType = "TABLE";
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeStringArrayList(testSoapResponse)).thenReturn(testPermissionList);
        
        ArrayList<String> returnedPermissionList = testSoapRemoteDataModelProxy.getPermissions(testObjectType, testAuthenticationContext, testTrackingData);
        
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeStringArrayList(testSoapResponse);
        assertEquals(testPermissionList, returnedPermissionList);
    }
    
    @Test
    public void getPermissionsForObjectNameAndRoleSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/GetPermissionsForObject";
        final String testSoapResponse = "[\"SELECT\",\"INSERT\",\"UPDATE\",\"DELETE\"]";
        ArrayList<String> testPermissionList = new ArrayList<String>(Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE"));
        final String testObjectName = "CUSTOMERS";
        final String testRole = "XYZON_POWER_ROLE";
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeStringArrayList(testSoapResponse)).thenReturn(testPermissionList);
        
        ArrayList<String> returnedPermissionList = testSoapRemoteDataModelProxy.getPermissions(testObjectName, testRole, testAuthenticationContext, testTrackingData);
        
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeStringArrayList(testSoapResponse);
        assertEquals(testPermissionList, returnedPermissionList);
    }
    
    @Test
    public void getMasterRoleToUserMapCollectionSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/GetMasterRoleToUserMapCollection";
        final String testSoapResponse = "[{\"Role\":\"XYZON_APP_ROLE\",\"User\":\"XYZON_APP_USER\"},{\"Role\":\"XYZON_POWER_ROLE\",\"User\":\"XYZON_POWER_USER\"},{\"Role\":\"XYZON_READ_ROLE\",\"User\":\"JONES_SAM\"}]";
        ArrayList<RoleToUserMap> testRoleToUserMapList = new ArrayList<RoleToUserMap>();
        testRoleToUserMapList.add(new RoleToUserMap("XYZON_APP_ROLE", "XYZON_APP_USER"));
        testRoleToUserMapList.add(new RoleToUserMap("XYZON_POWER_ROLE", "XYZON_POWER_USER"));
        testRoleToUserMapList.add(new RoleToUserMap("XYZON_READ_ROLE", "XYZON_READ_USER"));
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeRoleToUserMapArrayList(testSoapResponse)).thenReturn(testRoleToUserMapList);
        
        ArrayList<RoleToUserMap> returnedRoleToUserMapList = testSoapRemoteDataModelProxy.getMasterRoleToUserMapCollection(testAuthenticationContext, testTrackingData);
        
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeRoleToUserMapArrayList(testSoapResponse);
        assertEquals(testRoleToUserMapList, returnedRoleToUserMapList);
    }
    
    @Test
    public void RoleGetReferencingObjectsSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/RoleGetReferencingObjects";
        final String testSoapResponse = "[\"SP_CUSTOMERS_INS\",\"SP_CUSTOMERS_UPD\"]";
        ArrayList<String> testObjectList = new ArrayList<String>(Arrays.asList("SP_CUSTOMERS_INS", "SP_CUSTOMERS_UPD"));
        final String testRole = "XYZON_APP_ROLE";
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeStringArrayList(testSoapResponse)).thenReturn(testObjectList);
        
        ArrayList<String> returnedObjectList = testSoapRemoteDataModelProxy.RoleGetReferencingObjects(testRole, testAuthenticationContext, testTrackingData);
        
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeStringArrayList(testSoapResponse);
        assertEquals(testObjectList, returnedObjectList);
    }
    
    @Test
    public void AddRoleToUserMapSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/AddRoleToUserMap";
        final String testRole = "XYZON_APP_ROLE";
        final String testUser = "XYZON_APP_USER";
        
        testSoapRemoteDataModelProxy.AddRoleToUserMap(testRole, testUser, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("role", testRole);
        verify(mockSoapObject).addProperty("user", testUser);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
    }
    
    @Test
    public void RemoveRoleToUserMapSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/RemoveRoleToUserMap";
        final String testRole = "XYZON_APP_ROLE";
        final String testUser = "XYZON_APP_USER";
        
        testSoapRemoteDataModelProxy.RemoveRoleToUserMap(testRole, testUser, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("role", testRole);
        verify(mockSoapObject).addProperty("user", testUser);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
    }
    
    @Test
    public void RoleToUserMapValidateSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/RoleToUserMapValidate";
        final String testSoapResponse = "{\"IsValid\":false,\"ValidationError\":\"Role to user mapping for 'XYZON_APP_ROLE' and 'XYZON_APP_USER' already exists\"}";
        final String testRole = "XYZON_APP_ROLE";
        final String testUser = "XYZON_APP_USER";
        ValidationResult testValidationResult = new ValidationResult(false, "Role to user mapping for 'XYZON_APP_ROLE' and 'XYZON_APP_USER' already exists");
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        when(mockJsonSerializer.DeserializeValidationResult(testSoapResponse)).thenReturn(testValidationResult);
        
        ValidationResult returnedValidationResult = testSoapRemoteDataModelProxy.RoleToUserMapValidate(testRole, testUser, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("role", testRole);
        verify(mockSoapObject).addProperty("user", testUser);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        verify(mockJsonSerializer).DeserializeValidationResult(testSoapResponse);
        assertEquals(testValidationResult, returnedValidationResult);
    }
    
    @Test
    public void CreatePrivilegeScriptSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/CreatePrivilegeScript";
        final String testSoapResponse = "GRANT EXECUTE ON SP_CUSTOMERS_INS TO XYZON_APP_ROLE;\r\nGRANT EXECUTE ON SP_CUSTOMERS_UPD TO XYZON_APP_ROLE;\r\nGRANT EXECUTE ON SP_CUSTOMERS_INS TO XYZON_POWER_ROLE;\r\nGRANT EXECUTE ON SP_CUSTOMERS_UPD TO XYZON_POWER_ROLE;\r\n";
        final ScriptType testScriptType = ScriptType.Rollout;
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        
        String returnedPrivilegeScript = testSoapRemoteDataModelProxy.CreatePrivilegeScript(testScriptType, false, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("scriptType", testScriptType.name());
        verify(mockSoapObject).addProperty("generateRevokeStatements", false);
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        assertEquals(testSoapResponse, returnedPrivilegeScript);
    }
    
    @Test
    public void CreateSynonymScriptSuccessTest() throws Exception {
        final String expectedSoapAction = "http://tempuri.org/ISoapWebServiceApi/CreateSynonymScript";
        final String testSoapResponse = "CREATE SYNONYM XYZON_APP_USER.SP_CUSTOMERS_INS FOR XYZON.SP_CUSTOMERS_INS;\r\nCREATE SYNONYM XYZON_APP_USER.SP_CUSTOMERS_UPD FOR XYZON.SP_CUSTOMERS_UPD;\r\nCREATE SYNONYM XYZON_POWER_USER.SP_CUSTOMERS_INS FOR XYZON.SP_CUSTOMERS_INS;\r\nCREATE SYNONYM XYZON_POWER_USER.SP_CUSTOMERS_UPD FOR XYZON.SP_CUSTOMERS_UPD;";
        final ScriptType testScriptType = ScriptType.Rollout;
        
        when(mockSoapSerializationEnvelope.getResponse()).thenReturn(testSoapResponse);
        
        String returnedPrivilegeScript = testSoapRemoteDataModelProxy.CreateSynonymScript(testScriptType, testAuthenticationContext, testTrackingData);
        
        verify(mockSoapObject).addProperty("scriptType", testScriptType.name());
        verify(mockHttpTransportSE).call(expectedSoapAction, mockSoapSerializationEnvelope);
        verify(mockSoapSerializationEnvelope).getResponse();
        assertEquals(testSoapResponse, returnedPrivilegeScript);
    }
}
