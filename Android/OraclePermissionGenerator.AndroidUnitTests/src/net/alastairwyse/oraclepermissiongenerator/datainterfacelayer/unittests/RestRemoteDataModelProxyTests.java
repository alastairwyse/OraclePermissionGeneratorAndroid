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

import java.io.*;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.json.JSONException;

import android.net.Uri;
import android.test.AndroidTestCase;
import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.serialization.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.hamcrest.Description;

/**
 * Unit tests for class oraclepermissiongenerator.datainterfacelayer.RestRemoteDataModelProxy.
 * @author Alastair Wyse
 */
public class RestRemoteDataModelProxyTests extends AndroidTestCase {

    private final String testDataServiceLocation = "192.168.0.1:5001";
    private final String testUserIdentifier = "test_user@tempuri.org";
    private final String serializedAuthenticationContext = "{\"UserIdentifier\":\"test_user@tempuri.org\"}";
    private final String serializedTrackingData = "{\"IpV4Address\":[192,168,2,101],\"Location\":{\"Latitude\":35.6895,\"Longitude\":139.6917,\"SecondsSinceUpdate\":23}";
    private final String httpHeaderName = "Content-Type";
    private final String httpHeaderValue = "text/json; charset=UTF-8";
    private AuthenticationContext testAuthenticationContext;
    private TrackingData testTrackingData;
    
    private RestRemoteDataModelProxy testRestRemoteDataModelProxy;
    private ContainerObjectJsonSerializer mockJsonSerializer;
    private HttpClient mockHttpClient;
    private HttpGet mockHttpGet;
    private HttpPut mockHttpPut;
    private HttpPost mockHttpPost;
    private HttpDelete mockHttpDelete;
    private HttpResponse mockHttpResponse;
    private HttpEntity mockHttpEntity;
    private InputStream mockInputStream;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
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
        mockHttpClient = mock(HttpClient.class);
        mockHttpGet = mock(HttpGet.class);
        mockHttpPut = mock(HttpPut.class);
        mockHttpPost = mock(HttpPost.class);
        mockHttpDelete = mock(HttpDelete.class);
        mockHttpResponse = mock(HttpResponse.class);
        mockHttpEntity = mock(HttpEntity.class);
        mockInputStream = mock(InputStream.class);
        testRestRemoteDataModelProxy = new RestRemoteDataModelProxy(mockJsonSerializer, mockHttpClient, mockHttpGet, mockHttpPut, mockHttpPost, mockHttpDelete);
        testRestRemoteDataModelProxy.setDataModelLocation(testDataServiceLocation);
    }
    
    public void testGetObjects() throws Exception {
        String expectedUrl = BuildExpectedUrl("Objects", "");
        String expectedStringResponse = "[{\"AddFlag\":false,\"ObjectName\":\"CUSTOMERS\",\"ObjectOwner\":\"XYZON\",\"ObjectPermissions\":[{\"Permission\":\"SELECT\",\"Role\":\"XYZON_POWER_ROLE\"},{\"Permission\":\"SELECT\",\"Role\":\"XYZON_READ_ROLE\"}],\"ObjectType\":\"Table\",\"RemoveFlag\":false}]";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.getObjects(testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeOracleObjectPermissionSetArrayList(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testObjectNameValidate() throws Exception {
        final String newObjectName = "SP_APPLICATION_STATS_INS";
        String expectedUrl = BuildExpectedUrl("Validations/ObjectName/" + Uri.encode(newObjectName), "");
        String expectedStringResponse = "{\"IsValid\":true,\"ValidationError\":\"\"}";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.ObjectNameValidate(newObjectName, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeValidationResult(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testObjectTypeValidate() throws Exception {
        final String objectType = "Stored Procedure";
        String expectedUrl = BuildExpectedUrl("Validations/ObjectType/" + Uri.encode(objectType), "");
        String expectedStringResponse = "{\"IsValid\":true,\"ValidationError\":\"\"}";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.ObjectTypeValidate(objectType, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeValidationResult(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testObjectOwnerValidate() throws Exception {
        final String newObjectOwner = "NEW_OWNER";
        String expectedUrl = BuildExpectedUrl("Validations/ObjectOwner/" + Uri.encode(newObjectOwner), "");
        String expectedStringResponse = "{\"IsValid\":true,\"ValidationError\":\"\"}";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.ObjectOwnerValidate(newObjectOwner, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeValidationResult(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testgetDefaultObjectOwner() throws Exception {
        final String expectedObjectOwner = "OBJECT_OWNER";
        String expectedUrl = BuildExpectedUrl("DefaultObjectOwner", "");
        String expectedStringResponse = "\"" + expectedObjectOwner + "\"";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        String returnedDefaultObjectOwner = testRestRemoteDataModelProxy.getDefaultObjectOwner(testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        assertEquals(expectedObjectOwner, returnedDefaultObjectOwner);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testsetDefaultObjectOwner() throws Exception {
        final String objectOwner = "NEW_OWNER";
        String expectedUrl = BuildExpectedUrl("DefaultObjectOwner", "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.setDefaultObjectOwner(objectOwner, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpPut).setURI(new URI(expectedUrl));
        verify(mockHttpPut).setHeader(httpHeaderName, httpHeaderValue);
        verify(mockHttpPut).setEntity(argThat(new StringEntityMatcher(new StringEntity("\"" + objectOwner + "\""))));
        verify(mockHttpClient).execute(mockHttpPut);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testgetMasterRoleToUserMapCollection() throws Exception {
        String expectedUrl = BuildExpectedUrl("RoleToUserMappings", "");
        String expectedStringResponse = "[{\"Role\":\"XYZON_APP_ROLE\",\"User\":\"XYZON_APP_USER\"},{\"Role\":\"XYZON_POWER_ROLE\",\"User\":\"XYZON_POWER_USER\"},{\"Role\":\"XYZON_READ_ROLE\",\"User\":\"JONES_SAM\"},{\"Role\":\"XYZON_READ_ROLE\",\"User\":\"SMITH_JOHN\"}]";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.getMasterRoleToUserMapCollection(testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeRoleToUserMapArrayList(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testRoleGetReferencingObjects() throws Exception {
        final String testRole = "XYZON_READ_ROLE";
        String expectedUrl = BuildExpectedUrl("ReferencingObjects/" + testRole, "entity=role&");
        String expectedStringResponse = "[\"APPLICATION_STATS\",\"ORDER_STATUSES\"]";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.RoleGetReferencingObjects(testRole, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeStringArrayList(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testAddRoleToUserMap() throws Exception {
        final String testRole = "XYZON_READ_ROLE";
        final String testUser = "NEW_USER";
        String expectedUrl = BuildExpectedUrl("RoleToUserMappings", "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.AddRoleToUserMap(testRole, testUser, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpPost).setURI(new URI(expectedUrl));
        verify(mockHttpPost).setHeader(httpHeaderName, httpHeaderValue);
        verify(mockHttpPost).setEntity(argThat(new StringEntityMatcher(new StringEntity("{\"user\":\"" + testUser + "\",\"role\":\"" + testRole + "\"}"))));
        verify(mockHttpClient).execute(mockHttpPost);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testRemoveRoleToUserMap() throws Exception {
        final String testRole = "XYZON_READ_ROLE";
        final String testUser = "NEW_USER";
        String expectedUrl = BuildExpectedUrl("RoleToUserMappings/" + testRole + "/" + testUser, "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.RemoveRoleToUserMap(testRole, testUser, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpDelete).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpDelete);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testRoleToUserMapValidate() throws Exception {
        final String testRole = "XYZON_READ_ROLE";
        final String testUser = "NEW_USER";
        String expectedUrl = BuildExpectedUrl("Validations/RoleToUserMap/" + testRole + "/" + testUser, "");
        String expectedStringResponse = "{\"IsValid\":true,\"ValidationError\":\"\"}";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.RoleToUserMapValidate(testRole, testUser, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeValidationResult(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testSetAddFlag() throws Exception {
        final String objectName = "ORDER_STATUSES";
        final boolean addFlagValue = true;
        String expectedUrl = BuildExpectedUrl("Objects/" + objectName + "/AddFlag", "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.SetAddFlag(objectName, addFlagValue, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpPut).setURI(new URI(expectedUrl));
        verify(mockHttpPut).setHeader(httpHeaderName, httpHeaderValue);
        verify(mockHttpPut).setEntity(argThat(new StringEntityMatcher(new StringEntity("\"true\""))));
        verify(mockHttpClient).execute(mockHttpPut);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testSetRemoveFlag() throws Exception {
        final String objectName = "ORDER_STATUSES";
        final boolean addFlagValue = false;
        String expectedUrl = BuildExpectedUrl("Objects/" + objectName + "/RemoveFlag", "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.SetRemoveFlag(objectName, addFlagValue, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpPut).setURI(new URI(expectedUrl));
        verify(mockHttpPut).setHeader(httpHeaderName, httpHeaderValue);
        verify(mockHttpPut).setEntity(argThat(new StringEntityMatcher(new StringEntity("\"false\""))));
        verify(mockHttpClient).execute(mockHttpPut);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testRemoveObjectPermissionSet() throws Exception {
        final String objectName = "ORDER_STATUSES";
        String expectedUrl = BuildExpectedUrl("Objects/" + objectName, "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.RemoveObjectPermissionSet(objectName, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpDelete).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpDelete);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testgetObjectTypes() throws Exception {
        String expectedUrl = BuildExpectedUrl("ObjectTypes", "");
        String expectedStringResponse = "[\"View\",\"Stored Procedure\",\"Table\"]";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.getObjectTypes(testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeStringArrayList(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testAddObjectPermissionSet() throws Exception {
        final String testObjectName = "SP_ORDER_SUMMARY";
        final String testObjectType = "Stored Procedure";
        final String testObjectOwner = "XYZON";
        final boolean testAddFlag = true;
        final boolean testRemoveFlag = false;
        ArrayList<RoleToPermissionMap> testObjectPermissions = new ArrayList<RoleToPermissionMap>();
        String expectedUrl = BuildExpectedUrl("Objects", "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.AddObjectPermissionSet(testObjectName, testObjectType, testObjectOwner, testAddFlag, testRemoveFlag, testObjectPermissions, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpPost).setURI(new URI(expectedUrl));
        verify(mockHttpPost).setHeader(httpHeaderName, httpHeaderValue);
        verify(mockHttpPost).setEntity(argThat(new StringEntityMatcher(new StringEntity("{\"objectPermissions\":[],\"removeFlag\":false,\"objectOwner\":\"" + testObjectOwner + "\",\"addFlag\":true,\"objectType\":\"" + testObjectType + "\",\"objectName\":\"" + testObjectName + "\"}"))));
        verify(mockHttpClient).execute(mockHttpPost);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testgetRoles() throws Exception {
        String expectedUrl = BuildExpectedUrl("Roles", "");
        String expectedStringResponse = "[\"XYZON_READ_ROLE\",\"XYZON_POWER_ROLE\",\"XYZON_APP_ROLE\"]";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.getRoles(testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeStringArrayList(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testgetPermissionsForObjectType() throws Exception {
        final String testObjectType = "Stored Procedure";
        String expectedUrl = BuildExpectedUrl("Permissions/" + Uri.encode(testObjectType), "entity=objectType&");
        String expectedStringResponse = "[\"EXECUTE\"]";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.getPermissions(testObjectType, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeStringArrayList(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testgetPermissionsForObjectAndRole() throws Exception {
        final String testObject = "ORDER_STATUSES";
        final String testRole = "XYZON_READ_ROLE";
        String expectedUrl = BuildExpectedUrl("Permissions/" + Uri.encode(testObject) + "/" + Uri.encode(testRole), "entity=" + Uri.encode("object,role") + "&");
        String expectedStringResponse = "[\"SELECT\"]";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.getPermissions(testObject, testRole, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verify(mockJsonSerializer).DeserializeStringArrayList(expectedStringResponse);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testAddPermission() throws Exception {
        final String testObjectName = "APPLICATION_STATS";
        final String testRole = "XYZON_POWER_ROLE";
        final String testPermission = "INSERT";
        String expectedUrl = BuildExpectedUrl("Permissions", "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.AddPermission(testObjectName, testRole, testPermission, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpPost).setURI(new URI(expectedUrl));
        verify(mockHttpPost).setHeader(httpHeaderName, httpHeaderValue);
        verify(mockHttpPost).setEntity(argThat(new StringEntityMatcher(new StringEntity("{\"role\":\"" + testRole + "\",\"objectName\":\"" + testObjectName + "\",\"permission\":\"" + testPermission + "\"}"))));
        verify(mockHttpClient).execute(mockHttpPost);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testRemovePermission() throws Exception {
        final String testObjectName = "ORDER_STATUSES";
        final String testRole = "XYZON_READ_ROLE";
        final String testPermission = "SELECT";
        String expectedUrl = BuildExpectedUrl("Permissions/" + testObjectName + "/" + testRole + "/" + testPermission, "");
        
        SetCreateRestUrlWhenStatements();
        
        testRestRemoteDataModelProxy.RemovePermission(testObjectName, testRole, testPermission, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpDelete).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpDelete);
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testCreatePrivilegeScript() throws Exception {
        final ScriptType testScriptType = ScriptType.Rollout;
        final boolean testGenerateRevokeStatementsFlag = true;
        String expectedUrl = BuildExpectedUrl("PrivilegeScripts/" + Uri.encode(testScriptType.name()), "generateRevokeStatements=" + Uri.encode(String.valueOf(testGenerateRevokeStatementsFlag)) + "&");
        String expectedStringResponse = "\"GRANT SELECT ON VW_ORDER_STATUSES TO XYZON_APP_ROLE;\nGRANT EXECUTE ON SP_ORDER_STATUSES_INS TO XYZON_APP_ROLE;\nGRANT EXECUTE ON SP_ORDER_STATUSES_UPD TO XYZON_APP_ROLE;\nGRANT SELECT ON ORDER_STATUSES TO XYZON_POWER_ROLE;\nGRANT SELECT ON VW_ORDER_STATUSES TO XYZON_POWER_ROLE;\nGRANT EXECUTE ON SP_ORDER_STATUSES_INS TO XYZON_POWER_ROLE;\nGRANT EXECUTE ON SP_ORDER_STATUSES_UPD TO XYZON_POWER_ROLE;\nGRANT SELECT ON ORDER_STATUSES TO XYZON_READ_ROLE;\nGRANT SELECT ON VW_ORDER_STATUSES TO XYZON_READ_ROLE;\n\"";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.CreatePrivilegeScript(testScriptType, testGenerateRevokeStatementsFlag, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    public void testCreateSynonymScript() throws Exception {
        final ScriptType testScriptType = ScriptType.Rollback;
        String expectedUrl = BuildExpectedUrl("SynonymScripts/" + Uri.encode(testScriptType.name()), "");
        String expectedStringResponse = "\"CREATE SYNONYM XYZON_APP_USER.APPLICATION_STATS FOR XYZON.APPLICATION_STATS;\nDROP SYNONYM XYZON_APP_USER.VW_ORDER_STATUSES;\nDROP SYNONYM XYZON_APP_USER.SP_ORDER_STATUSES_INS;\nDROP SYNONYM XYZON_APP_USER.SP_ORDER_STATUSES_UPD;\nCREATE SYNONYM XYZON_POWER_USER.APPLICATION_STATS FOR XYZON.APPLICATION_STATS;\nDROP SYNONYM XYZON_POWER_USER.ORDER_STATUSES;\nDROP SYNONYM XYZON_POWER_USER.VW_ORDER_STATUSES;\nDROP SYNONYM XYZON_POWER_USER.SP_ORDER_STATUSES_INS;\nDROP SYNONYM XYZON_POWER_USER.SP_ORDER_STATUSES_UPD;\nCREATE SYNONYM JONES_SAM.APPLICATION_STATS FOR XYZON.APPLICATION_STATS;\nCREATE SYNONYM SMITH_JOHN.APPLICATION_STATS FOR XYZON.APPLICATION_STATS;\nDROP SYNONYM JONES_SAM.ORDER_STATUSES;\nDROP SYNONYM SMITH_JOHN.ORDER_STATUSES;\nDROP SYNONYM JONES_SAM.VW_ORDER_STATUSES;\nDROP SYNONYM SMITH_JOHN.VW_ORDER_STATUSES;\n\"";
        byte[] byteResponse = expectedStringResponse.getBytes("UTF-8");

        SetCreateRestUrlWhenStatements();
        when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        SetConvertHttpResponseToStringWhenStatements(byteResponse);

        testRestRemoteDataModelProxy.CreateSynonymScript(testScriptType, testAuthenticationContext, testTrackingData);
        
        SetCreateRestUrlVerifyStatements();
        verify(mockHttpGet).setURI(new URI(expectedUrl));
        verify(mockHttpClient).execute(mockHttpGet);
        SetConvertHttpResponseToStringVerifyStatements();
        verifyNoMoreInteractions(mockJsonSerializer,  mockHttpClient,  mockHttpGet,  mockHttpPut,  mockHttpPost,  mockHttpDelete,  mockHttpResponse,  mockHttpEntity,  mockInputStream);
    }
    
    /**
     * Calls when() methods for mock interactions in RestRemoteDataModelProxy private method CreateRestUrl().
     */
    private void SetCreateRestUrlWhenStatements() throws JSONException {
        when(mockJsonSerializer.SerializeAuthenticationContext(testAuthenticationContext)).thenReturn(serializedAuthenticationContext);
        when(mockJsonSerializer.SerializeTrackingData(testTrackingData)).thenReturn(serializedTrackingData);
    }
    
    /**
     * Calls verify() methods for mock interactions in RestRemoteDataModelProxy private method CreateRestUrl().
     */
    private void SetCreateRestUrlVerifyStatements() throws JSONException {
        verify(mockJsonSerializer).SerializeAuthenticationContext(testAuthenticationContext);
        verify(mockJsonSerializer).SerializeTrackingData(testTrackingData);
    }
    
    /**
     * Calls when() methods for mock interactions in RestRemoteDataModelProxy private method ConvertHttpResponseToString().
     * @param  characterBuffer  The buffer of characters to return when the BufferedReader.read() method is called.
     */
    private void SetConvertHttpResponseToStringWhenStatements(byte[] characterBuffer) throws IOException {
        when(mockHttpResponse.getEntity()).thenReturn(mockHttpEntity);
        when(mockHttpEntity.getContentLength()).thenReturn((long)characterBuffer.length);
        when(mockHttpEntity.getContent()).thenReturn(mockInputStream);
        when(mockInputStream.available()).thenReturn(characterBuffer.length);
        when(mockInputStream.read(any(byte[].class), eq(0), eq(8192))).thenAnswer(new ReadMethodAnswer(characterBuffer, characterBuffer.length));
    }
    
    /**
     * Calls verify() methods for mock interactions in RestRemoteDataModelProxy private method ConvertHttpResponseToString().
     */
    private void SetConvertHttpResponseToStringVerifyStatements() throws IOException {
        verify(mockHttpResponse, times(4)).getEntity();
        verify(mockHttpEntity, times(3)).getContentLength();
        verify(mockHttpEntity).getContent();
        verify(mockInputStream).available();
        verify(mockInputStream).read(any(byte[].class), eq(0), eq(8192));
        verify(mockInputStream).close();
    }
    
    /**
     * Builds a URL to set as the parameter to a verify() method on HttpGet, HttpPut, etc... classes.
     * @param   pathSegment   The configurable path segment of the URL e.g. "DefaultObjectOwner" in the URL "http://192.168.0.1:5001/OraclePermissionGeneratorWebServiceAPI/REST/DefaultObjectOwner?authenticationContext={"UserIdentifier":"tutorial_user@tempuri.org"}&trackingData={"IpV4Address":[192,168,2,101],"Location":{"Latitude":35.6895,"Longitude":139.6917,"SecondsSinceUpdate":23}}".
     * @param   querySegment  The configurable part of the query segment of the URL e.g. "NEW_OWNER" in the URL "http://192.168.0.1:5001/OraclePermissionGeneratorWebServiceAPI/REST/DefaultObjectOwner?defaultObjectOwner=NEW_OWNER&authenticationContext={%22UserIdentifier%22:%22tutorial_user@tempuri.org%22}&trackingData={%22IpV4Address%22:[192,168,2,101],%22Location%22:{%22Latitude%22:35.6895,%22Longitude%22:139.6917,%22SecondsSinceUpdate%22:23}}".
     * @return                The complete URL.
     */
    private String BuildExpectedUrl(String pathSegment, String querySegment) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://");
        urlBuilder.append(testDataServiceLocation);
        urlBuilder.append("/OraclePermissionGeneratorWebServiceAPI/REST/");
        urlBuilder.append(pathSegment);
        urlBuilder.append("?");
        urlBuilder.append(querySegment);
        urlBuilder.append("authenticationContext=");
        urlBuilder.append(Uri.encode(serializedAuthenticationContext));
        urlBuilder.append("&");
        urlBuilder.append("trackingData=");
        urlBuilder.append(Uri.encode(serializedTrackingData));
        return urlBuilder.toString();
    }
    
    /**
     * Extension of the mockito ArgumentMatcher class which allows org.apache.http.entity.StringEntity classes to be compared when used as parameters in mockito verify() and when() method calls.
     */
    private class StringEntityMatcher extends ArgumentMatcher<StringEntity> {

        private StringEntity stringEntityToMatch;
        
        /**
         * Initialises a new instance of the StringEntityMatcher class.
         * @param  stringEntityToMatch  The StringEntity class that should match the parameter passed to the mockito verify() or when() method call.
         */
        public StringEntityMatcher(StringEntity stringEntityToMatch) {
            this.stringEntityToMatch = stringEntityToMatch;
        }
        
        @Override
        public boolean matches(Object argument) {
            if (argument.getClass() != stringEntityToMatch.getClass()) {
                return false;
            }
            
            StringEntity comparisonStringEntity = (StringEntity)argument;
            // Convert both StringEntity object to Strings on order to compare them
            String stringToMatch = null;
            String comparisonString = null;
            try {
                stringToMatch = ConvertInputStreamToString(stringEntityToMatch.getContent(), (int)stringEntityToMatch.getContentLength(), "UTF-8");
                comparisonString = ConvertInputStreamToString(comparisonStringEntity.getContent(), (int)comparisonStringEntity.getContentLength(), "UTF-8");
            }
            catch (Exception e) {
                return false;
            }
            if (stringToMatch.equals(comparisonString) == false ) {
                return false;
            }
            
            return true;
        }
        
        @Override
        public void describeTo(Description description) {
            // This method is typically used in mockito when a test fails, to report the value that was wanted but not matched.  E.g. this implementation will return...
            //   "RestRemoteDataModelProxy([dataModelLocation])"
            // ... which will typically be written to the mockito failure trace.
            
            String stringToMatch;
            try {
                stringToMatch = ConvertInputStreamToString(stringEntityToMatch.getContent(), (int)stringEntityToMatch.getContentLength(), "UTF-8");
            }
            catch (Exception e) {
                stringToMatch = "WARNING: Exception occured coverting StringEntity object to a String.";
            }
            
            description.appendText(stringEntityToMatch.getClass().getSimpleName() + "(" + stringToMatch + ")");
        }
        
        /**
         * Converts the inputted InputStream into a String.
         * @param   inputStream                   The InputStream to convert.
         * @param   inputStreamLength             The length of the input stream.
         * @param   encoding                      The encoding scheme to use when converting the stream (e.g. 'UTF-8').
         * @return  The InputStream               converted into a String.
         * @throws  UnsupportedEncodingException  if the specified encoding scheme is invalid.
         * @throws  IOException                   if an error occurs when reading form the InputStream
         */
        private String ConvertInputStreamToString(InputStream inputStream, Integer inputStreamLength, String encoding) throws UnsupportedEncodingException, IOException {
            char[] characterBuffer = new char[inputStreamLength];
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            try {
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader, inputStreamLength);
                try {
                    bufferedReader.read(characterBuffer, 0, inputStreamLength);
                }
                finally {
                    bufferedReader.close();
                }
            }
            finally {
                inputStreamReader.close();
            }
            return new String(characterBuffer);
        }
    }
    
    /**
     * Mock answer for the BufferedReader.Read() method.
     */
    private class ReadMethodAnswer implements Answer<Integer> {

        private byte[] bytesToWrite;
        private Integer returnValue;

        /**
         * Initialises a new instance of the ReadMethodAnswer class.
         * @param  bytesToWrite  The byte array to write to the 'buffer' parameter when the Read() method is called.
         * @param  returnValue        The value to return.
         */
        public ReadMethodAnswer(byte[] bytesToWrite, Integer returnValue) {
            this.bytesToWrite = bytesToWrite;
            this.returnValue = returnValue;
        }
        
        @Override
        public Integer answer(InvocationOnMock invocation) throws Throwable {
            byte[] bufferParameter = (byte[])invocation.getArguments()[0];
            System.arraycopy(bytesToWrite, 0, bufferParameter, 0, bytesToWrite.length);
            return returnValue;
        }
    }
}
