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

import java.util.*;

import android.test.AndroidTestCase;

import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for class oraclepermissiongenerator.datainterfacelayer.DataInterfaceService.
 * @author Alastair Wyse
 */
public class DataInterfaceServiceTests extends AndroidTestCase {

    private DataInterfaceService testDataInterfaceService;
    private IRemoteDataModelProxy mockRemoteDataModelProxy;
    private ILocationProvider mockLocationProvider;
    private INetworkInfoProvider mockNetworkInfoProvider;
    private ILocalSettingsPersister mockLocalSettingsPersister;
    private IScriptPersister mockScriptPersister;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        mockRemoteDataModelProxy = mock(IRemoteDataModelProxy.class);
        mockLocationProvider = mock(ILocationProvider.class);
        mockNetworkInfoProvider = mock(INetworkInfoProvider.class);
        mockLocalSettingsPersister = mock(ILocalSettingsPersister.class);
        mockScriptPersister = mock(IScriptPersister.class);
        testDataInterfaceService = new DataInterfaceService(mockLocationProvider, mockNetworkInfoProvider, mockLocalSettingsPersister, mockScriptPersister);
        testDataInterfaceService.setRemoteDataModelProxy(mockRemoteDataModelProxy);
    }
    
    public void testAddObjectPermissionSetSuccessTest() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        final String testObjectType = "Stored Procedure";
        final String testObjectOwner = "DEFAULT_OWNER";
        final boolean testAddFlag = true;
        final boolean testRemoveFlag = false;
        ArrayList<RoleToPermissionMap> testObjectPermissions = new ArrayList<RoleToPermissionMap>();
        testObjectPermissions.add(new RoleToPermissionMap("XYZON_APP_ROLE", "EXECUTE"));
        testObjectPermissions.add(new RoleToPermissionMap("XYZON_POWER_ROLE", "EXECUTE"));

        testDataInterfaceService.AddObjectPermissionSet(testObjectName, testObjectType, testObjectOwner, testAddFlag, testRemoveFlag, testObjectPermissions);
        
        verify(mockRemoteDataModelProxy).AddObjectPermissionSet(eq(testObjectName), eq(testObjectType), eq(testObjectOwner), eq(testAddFlag), eq(testRemoveFlag), eq(testObjectPermissions), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testRemoveObjectPermissionSet() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        
        testDataInterfaceService.RemoveObjectPermissionSet(testObjectName);
        
        verify(mockRemoteDataModelProxy).RemoveObjectPermissionSet(eq(testObjectName), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testObjectNameValidate() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        ValidationResult testValidationResult = new ValidationResult(false, "The object 'SP_CUSTOMERS_INS' already exists");
        
        when(mockRemoteDataModelProxy.ObjectNameValidate(eq(testObjectName), any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testValidationResult);
        
        ValidationResult returnedValidationResult = testDataInterfaceService.ObjectNameValidate(testObjectName);
        
        verify(mockRemoteDataModelProxy).ObjectNameValidate(eq(testObjectName), any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testValidationResult, returnedValidationResult);
    }
    
    public void testObjectOwnerValidate() throws Exception {
        final String testObjectOwner = "LONG_OBJECT_OWNER_NAME012345678";
        ValidationResult testValidationResult = new ValidationResult(false, "The object owner must be between 1 and 30 characters in length");
        
        when(mockRemoteDataModelProxy.ObjectOwnerValidate(eq(testObjectOwner), any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testValidationResult);
        
        ValidationResult returnedValidationResult = testDataInterfaceService.ObjectOwnerValidate(testObjectOwner);
        
        verify(mockRemoteDataModelProxy).ObjectOwnerValidate(eq(testObjectOwner), any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testValidationResult, returnedValidationResult);
    }
    
    public void testgetDefaultObjectOwner() throws Exception {
        final String testSoapResponse = "XYZON";
        
        when(mockRemoteDataModelProxy.getDefaultObjectOwner(any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testSoapResponse);
        
        String returnedDefaultObjectOwner = testDataInterfaceService.getDefaultObjectOwner();
        
        verify(mockRemoteDataModelProxy).getDefaultObjectOwner(any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals("XYZON", returnedDefaultObjectOwner);
    }
    
    public void testsetDefaultObjectOwner() throws Exception {
        final String testObjectOwner = "OBJECT_OWNER";
        
        testDataInterfaceService.setDefaultObjectOwner(testObjectOwner);
        
        verify(mockRemoteDataModelProxy).setDefaultObjectOwner(eq(testObjectOwner), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testgetObjectTypes() throws Exception {
        ArrayList<String> testObjectTypeList = new ArrayList<String>(Arrays.asList("View", "Stored Procedure", "Table"));
        
        when(mockRemoteDataModelProxy.getObjectTypes(any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testObjectTypeList);
        
        ArrayList<String> returnedObjectTypeList = testDataInterfaceService.getObjectTypes();
        
        verify(mockRemoteDataModelProxy).getObjectTypes(any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testObjectTypeList, returnedObjectTypeList);
    }
    
    public void testgetObjects() throws Exception {
        ArrayList<OracleObjectPermissionSet> testObjects = new ArrayList<OracleObjectPermissionSet>();
        
        when(mockRemoteDataModelProxy.getObjects(any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testObjects);
        
        ArrayList<OracleObjectPermissionSet> returnedObjects = testDataInterfaceService.getObjects();
        
        verify(mockRemoteDataModelProxy).getObjects(any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testObjects, returnedObjects);
    }
    
    public void testSetAddFlag() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        
        testDataInterfaceService.SetAddFlag(testObjectName, true);
        
        verify(mockRemoteDataModelProxy).SetAddFlag(eq(testObjectName), eq(true), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testSetRemoveFlag() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        
        testDataInterfaceService.SetRemoveFlag(testObjectName, true);
        
        verify(mockRemoteDataModelProxy).SetRemoveFlag(eq(testObjectName), eq(true), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testAddPermission() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        final String testRole = "XYZON_APP_ROLE";
        final String testPermission = "EXECUTE";
        
        testDataInterfaceService.AddPermission(testObjectName, testRole, testPermission);
        
        verify(mockRemoteDataModelProxy).AddPermission(eq(testObjectName), eq(testRole), eq(testPermission), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testRemovePermission() throws Exception {
        final String testObjectName = "SP_CUSTOMERS_INS";
        final String testRole = "XYZON_APP_ROLE";
        final String testPermission = "EXECUTE";
        
        testDataInterfaceService.RemovePermission(testObjectName, testRole, testPermission);
        
        verify(mockRemoteDataModelProxy).RemovePermission(eq(testObjectName), eq(testRole), eq(testPermission), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testgetRoles() throws Exception {
        ArrayList<String> testRoleList = new ArrayList<String>(Arrays.asList("XYZON_READ_ROLE", "XYZON_POWER_ROLE", "XYZON_APP_ROLE"));
        
        when(mockRemoteDataModelProxy.getRoles(any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testRoleList);
        
        ArrayList<String> returnedRoleList = testDataInterfaceService.getRoles();
        
        verify(mockRemoteDataModelProxy).getRoles(any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testRoleList, returnedRoleList);
    }
    
    public void testgetPermissionsForObjectType() throws Exception {
        ArrayList<String> testPermissionList = new ArrayList<String>(Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE"));
        final String testObjectType = "TABLE";
        
        when(mockRemoteDataModelProxy.getPermissions(eq(testObjectType), any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testPermissionList);
        
        ArrayList<String> returnedPermissionList = testDataInterfaceService.getPermissions(testObjectType);
        
        verify(mockRemoteDataModelProxy).getPermissions(eq(testObjectType), any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testPermissionList, returnedPermissionList);
    }
    
    public void testgetPermissionsForObjectNameAndRole() throws Exception {
        ArrayList<String> testPermissionList = new ArrayList<String>(Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE"));
        final String testObjectName = "CUSTOMERS";
        final String testRole = "XYZON_POWER_ROLE";
        
        when(mockRemoteDataModelProxy.getPermissions(eq(testObjectName), eq(testRole), any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testPermissionList);
        
        ArrayList<String> returnedPermissionList = testDataInterfaceService.getPermissions(testObjectName, testRole);
        
        verify(mockRemoteDataModelProxy).getPermissions(eq(testObjectName), eq(testRole), any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testPermissionList, returnedPermissionList);
    }
    
    public void testgetMasterRoleToUserMapCollection() throws Exception {
        ArrayList<RoleToUserMap> testRoleToUserMapList = new ArrayList<RoleToUserMap>();
        testRoleToUserMapList.add(new RoleToUserMap("XYZON_APP_ROLE", "XYZON_APP_USER"));
        testRoleToUserMapList.add(new RoleToUserMap("XYZON_POWER_ROLE", "XYZON_POWER_USER"));
        testRoleToUserMapList.add(new RoleToUserMap("XYZON_READ_ROLE", "XYZON_READ_USER"));
        
        when(mockRemoteDataModelProxy.getMasterRoleToUserMapCollection(any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testRoleToUserMapList);
        
        ArrayList<RoleToUserMap> returnedRoleToUserMapList = testDataInterfaceService.getMasterRoleToUserMapCollection();
        
        verify(mockRemoteDataModelProxy).getMasterRoleToUserMapCollection(any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testRoleToUserMapList, returnedRoleToUserMapList);
    }
    
    public void testRoleGetReferencingObjects() throws Exception {
        ArrayList<String> testObjectList = new ArrayList<String>(Arrays.asList("SP_CUSTOMERS_INS", "SP_CUSTOMERS_UPD"));
        final String testRole = "XYZON_APP_ROLE";
        
        when(mockRemoteDataModelProxy.RoleGetReferencingObjects(eq(testRole), any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testObjectList);
        
        ArrayList<String> returnedObjectList = testDataInterfaceService.RoleGetReferencingObjects(testRole);
        
        verify(mockRemoteDataModelProxy).RoleGetReferencingObjects(eq(testRole), any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testObjectList, returnedObjectList);
    }
    
    public void testAddRoleToUserMap() throws Exception {
        final String testRole = "XYZON_APP_ROLE";
        final String testUser = "XYZON_APP_USER";
        
        testDataInterfaceService.AddRoleToUserMap(testRole, testUser);
        
        verify(mockRemoteDataModelProxy).AddRoleToUserMap(eq(testRole), eq(testUser), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testRemoveRoleToUserMap() throws Exception {
        final String testRole = "XYZON_APP_ROLE";
        final String testUser = "XYZON_APP_USER";
        
        testDataInterfaceService.RemoveRoleToUserMap(testRole, testUser);
        
        verify(mockRemoteDataModelProxy).RemoveRoleToUserMap(eq(testRole), eq(testUser), any(AuthenticationContext.class), any(TrackingData.class));
    }
    
    public void testRoleToUserMapValidate() throws Exception {
        final String testRole = "XYZON_APP_ROLE";
        final String testUser = "XYZON_APP_USER";
        ValidationResult testValidationResult = new ValidationResult(false, "Role to user mapping for 'XYZON_APP_ROLE' and 'XYZON_APP_USER' already exists");
        
        when(mockRemoteDataModelProxy.RoleToUserMapValidate(eq(testRole), eq(testUser), any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testValidationResult);
        
        ValidationResult returnedValidationResult = testDataInterfaceService.RoleToUserMapValidate(testRole, testUser);
        
        verify(mockRemoteDataModelProxy).RoleToUserMapValidate(eq(testRole), eq(testUser), any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testValidationResult, returnedValidationResult);
    }

    public void testsetLocalSettings() throws Exception {
        LocalSettings testLocalSettings = new LocalSettings();
        testLocalSettings.setUserIdentifier("user@tempuri.org");
        testLocalSettings.setRemoteDataModelProxyType(RemoteDataModelProxyType.SOAP);
        testLocalSettings.setSoapDataServiceLocation("192.168.1.101:5000");
        testLocalSettings.setRestDataServiceLocation("192.168.1.101:5001");
        
        testDataInterfaceService.setLocalSettings(testLocalSettings);
        
        verify(mockLocalSettingsPersister).Write(testLocalSettings);
    }
    
    public void testgetLocalSettings() throws Exception {
        LocalSettings testLocalSettings = new LocalSettings();
        testLocalSettings.setUserIdentifier("user@tempuri.org");
        testLocalSettings.setRemoteDataModelProxyType(RemoteDataModelProxyType.SOAP);
        testLocalSettings.setSoapDataServiceLocation("192.168.1.101:5000");
        testLocalSettings.setRestDataServiceLocation("192.168.1.101:5001");
        
        when(mockLocalSettingsPersister.Read()).thenReturn(testLocalSettings);
        
        LocalSettings returnedLocalSettings = testDataInterfaceService.getLocalSettings();
        
        verify(mockLocalSettingsPersister).Read();
        assertEquals(testLocalSettings, returnedLocalSettings);
    }

    public void testCreatePrivilegeScript() throws Exception {
        final String testPrivilegeScript = "GRANT EXECUTE ON SP_CUSTOMERS_INS TO XYZON_APP_ROLE;\r\nGRANT EXECUTE ON SP_CUSTOMERS_UPD TO XYZON_APP_ROLE;\r\nGRANT EXECUTE ON SP_CUSTOMERS_INS TO XYZON_POWER_ROLE;\r\nGRANT EXECUTE ON SP_CUSTOMERS_UPD TO XYZON_POWER_ROLE;\r\n";
        final ScriptType testScriptType = ScriptType.Rollout;
        
        when(mockRemoteDataModelProxy.CreatePrivilegeScript(eq(testScriptType), eq(false), any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testPrivilegeScript);
        
        String returnedPrivilegeScript = testDataInterfaceService.CreatePrivilegeScript(testScriptType, false);
        
        verify(mockRemoteDataModelProxy).CreatePrivilegeScript(eq(testScriptType), eq(false), any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testPrivilegeScript, returnedPrivilegeScript);
    }
    
    public void testCreateSynonymScript() throws Exception {
        final String testSynonymScript = "CREATE SYNONYM XYZON_APP_USER.SP_CUSTOMERS_INS FOR XYZON.SP_CUSTOMERS_INS;\r\nCREATE SYNONYM XYZON_APP_USER.SP_CUSTOMERS_UPD FOR XYZON.SP_CUSTOMERS_UPD;\r\nCREATE SYNONYM XYZON_POWER_USER.SP_CUSTOMERS_INS FOR XYZON.SP_CUSTOMERS_INS;\r\nCREATE SYNONYM XYZON_POWER_USER.SP_CUSTOMERS_UPD FOR XYZON.SP_CUSTOMERS_UPD;";
        final ScriptType testScriptType = ScriptType.Rollout;
        
        when(mockRemoteDataModelProxy.CreateSynonymScript(eq(testScriptType), any(AuthenticationContext.class), any(TrackingData.class))).thenReturn(testSynonymScript);
        
        String returnedPrivilegeScript = testDataInterfaceService.CreateSynonymScript(testScriptType);
        
        verify(mockRemoteDataModelProxy).CreateSynonymScript(eq(testScriptType), any(AuthenticationContext.class), any(TrackingData.class));
        assertEquals(testSynonymScript, returnedPrivilegeScript);
    }

    public void testWriteScript() throws Exception {
        final String testScript = "CREATE SYNONYM XYZON_APP_USER.SP_CUSTOMERS_INS FOR XYZON.SP_CUSTOMERS_INS;\r\nCREATE SYNONYM XYZON_APP_USER.SP_CUSTOMERS_UPD FOR XYZON.SP_CUSTOMERS_UPD;\r\nCREATE SYNONYM XYZON_POWER_USER.SP_CUSTOMERS_INS FOR XYZON.SP_CUSTOMERS_INS;\r\nCREATE SYNONYM XYZON_POWER_USER.SP_CUSTOMERS_UPD FOR XYZON.SP_CUSTOMERS_UPD;";
        
        testDataInterfaceService.WriteScript(testScript);
        
        verify(mockScriptPersister).Write(testScript);
    }
}
