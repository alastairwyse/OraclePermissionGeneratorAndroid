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

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.*;
import android.net.Uri;

import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.serialization.ContainerObjectJsonSerializer;

/**
 * Proxies method calls to a remote instance of the Oracle Permission Generator data model running as a REST/JSON web service.
 * @author Alastair Wyse
 */
public class RestRemoteDataModelProxy implements IRemoteDataModelProxy {

    private final String httpUrlPrefix = "http://";
    private final String urlBasePath = "OraclePermissionGeneratorWebServiceAPI/REST";
    private final String httpHeaderName = "Content-Type";
    private final String httpHeaderValue = "text/json; charset=UTF-8";
    private char urlPathDelimiter = '/';
    private char urlQueryDelimiter = '?';
    private char urlQueryParameterDelimiter = '&';
    
    private String dataModelLocation;
    private ContainerObjectJsonSerializer jsonSerializer;
    private HttpClient httpClient;
    private HttpGet httpGet;
    private HttpPut httpPut;
    private HttpPost httpPost;
    private HttpDelete httpDelete;
    private boolean instantiatedWithTestConstructor = false;
    
    /**
     * Initialises a new instance of the RestRemoteDataModelProxy class.
     */
    public RestRemoteDataModelProxy() {
        jsonSerializer = new ContainerObjectJsonSerializer();
    }
    
    /**
     * Initialises a new instance of the RestRemoteDataModelProxy class.
     * <b>Note</b> this is an additional constructor to facilitate unit tests, and should not be used to instantiate the class under normal conditions.
     * @param  jsonSerializer  A test (mock) JSON serializer.
     * @param  httpClient      A test (mock) HttpGet object.
     * @param  httpGet         A test (mock) HttpGet object.
     * @param  httpPut         A test (mock) HttpPut object.
     * @param  httpPost        A test (mock) HttpPost object.
     * @param  httpDelete      A test (mock) HttpDelete object.
     */
    public RestRemoteDataModelProxy(ContainerObjectJsonSerializer jsonSerializer, HttpClient httpClient, HttpGet httpGet, HttpPut httpPut, HttpPost httpPost, HttpDelete httpDelete) {
        this();
        
        this.jsonSerializer = jsonSerializer;
        this.httpClient = httpClient;
        this.httpGet = httpGet;
        this.httpPut = httpPut;
        this.httpPost = httpPost;
        this.httpDelete = httpDelete;
        instantiatedWithTestConstructor = true;
    }
    
    /**
     * @param  dataModelLocation  The network location (IP address or hostname and port) of the data model web service, for example '192.168.0.101:5000'.
     */
    public void setDataModelLocation(String dataModelLocation) {
        this.dataModelLocation = dataModelLocation;
    }
    
    /**
     * @return  The network location (IP address or hostname and port) of the data model web service.
     */
    public String getDataModelLocation() {
        return dataModelLocation;
    }

    @Override
    public void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, boolean addFlag, boolean removeFlag, ArrayList<RoleToPermissionMap> objectPermissions, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        JSONArray permissions = new JSONArray();
        for(RoleToPermissionMap currentRoleToPermissionMap : objectPermissions) {
            JSONObject currentRoleToPermissionMapJsonObject = new JSONObject();
            currentRoleToPermissionMapJsonObject.put("Role", currentRoleToPermissionMap.getRole());
            currentRoleToPermissionMapJsonObject.put("Permission", currentRoleToPermissionMap.getPermission());
            permissions.put(currentRoleToPermissionMapJsonObject);
        }
        JSONObject parameters = new JSONObject();
        parameters.put("objectName", objectName);
        parameters.put("objectType", objectType);
        parameters.put("objectOwner", objectOwner);
        parameters.put("addFlag", addFlag);
        parameters.put("removeFlag", removeFlag);
        parameters.put("objectPermissions", permissions);
        MakePostRequest(parameters.toString(), "Objects", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public void RemoveObjectPermissionSet(String objectName, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Objects");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(objectName));
        MakeDeleteRequest(urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public ValidationResult ObjectNameValidate(String objectName, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Validations");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append("ObjectName");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(objectName));
        String response = MakeGetRequest(urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return jsonSerializer.DeserializeValidationResult(response);
    }

    @Override
    public ValidationResult ObjectTypeValidate(String objectType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Validations");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append("ObjectType");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(objectType));
        String response = MakeGetRequest(urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return jsonSerializer.DeserializeValidationResult(response);
    }

    @Override
    public ValidationResult ObjectOwnerValidate(String objectOwner, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Validations");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append("ObjectOwner");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(objectOwner));
        String response = MakeGetRequest(urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return jsonSerializer.DeserializeValidationResult(response);
    }

    @Override
    public String getDefaultObjectOwner(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        String response = MakeGetRequest("DefaultObjectOwner", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return DeserializeBareJsonString(response);
    }

    @Override
    public void setDefaultObjectOwner(String defaultObjectOwner, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        MakePutRequest("\"" + defaultObjectOwner + "\"", "DefaultObjectOwner", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public ArrayList<String> getObjectTypes(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        String response = MakeGetRequest("ObjectTypes", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return jsonSerializer.DeserializeStringArrayList(response);
    }

    @Override
    public ArrayList<OracleObjectPermissionSet> getObjects(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        String response = MakeGetRequest("Objects", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return jsonSerializer.DeserializeOracleObjectPermissionSetArrayList(response);
    }

    @Override
    public void SetAddFlag(String objectName, boolean addFlagValue,  AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Objects");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(objectName));
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append("AddFlag");
        MakePutRequest("\"" + String.valueOf(addFlagValue) + "\"", urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public void SetRemoveFlag(String objectName, boolean removeFlagValue, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Objects");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(objectName));
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append("RemoveFlag");
        MakePutRequest("\"" + String.valueOf(removeFlagValue) + "\"", urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public void AddPermission(String objectName, String role, String permission, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        JSONObject parameters = new JSONObject();
        parameters.put("objectName", objectName);
        parameters.put("role", role);
        parameters.put("permission", permission);
        MakePostRequest(parameters.toString(), "Permissions", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public void RemovePermission(String objectName, String role, String permission, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Permissions");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(objectName));
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(role));
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(permission));
        MakeDeleteRequest(urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public ArrayList<String> getRoles(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        String response = MakeGetRequest("Roles", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return jsonSerializer.DeserializeStringArrayList(response);
    }

    @Override
    public ArrayList<String> getPermissions(String objectType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        String urlPathSegment = "Permissions" + urlPathDelimiter + Uri.encode(objectType);
        ArrayList<RestQueryParameter> queryParameters = new ArrayList<RestQueryParameter>();
        queryParameters.add(new RestQueryParameter("entity", "objectType"));
        String response = MakeGetRequest(urlPathSegment, queryParameters, authenticationContext, trackingData);
        return jsonSerializer.DeserializeStringArrayList(response);
    }

    @Override
    public ArrayList<String> getPermissions(String objectName, String role, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Permissions");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(objectName));
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(role));
        ArrayList<RestQueryParameter> queryParameters = new ArrayList<RestQueryParameter>();
        queryParameters.add(new RestQueryParameter("entity", "object,role"));
        String response = MakeGetRequest(urlPathSegment.toString(), queryParameters, authenticationContext, trackingData);
        return jsonSerializer.DeserializeStringArrayList(response);
    }

    @Override
    public ArrayList<RoleToUserMap> getMasterRoleToUserMapCollection(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        String response = MakeGetRequest("RoleToUserMappings", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return jsonSerializer.DeserializeRoleToUserMapArrayList(response);
    }

    @Override
    public ArrayList<String> RoleGetReferencingObjects(String role, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        String urlPathSegment = "ReferencingObjects" + urlPathDelimiter + Uri.encode(role);
        ArrayList<RestQueryParameter> queryParameters = new ArrayList<RestQueryParameter>();
        queryParameters.add(new RestQueryParameter("entity", "role"));
        String response = MakeGetRequest(urlPathSegment, queryParameters, authenticationContext, trackingData);
        return jsonSerializer.DeserializeStringArrayList(response);
    }

    @Override
    public void AddRoleToUserMap(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        JSONObject parameters = new JSONObject();
        parameters.put("role", role);
        parameters.put("user", user);
        MakePostRequest(parameters.toString(), "RoleToUserMappings", new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public void RemoveRoleToUserMap(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("RoleToUserMappings");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(role));
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(user));
        MakeDeleteRequest(urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
    }

    @Override
    public ValidationResult RoleToUserMapValidate(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("Validations");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append("RoleToUserMap");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(role));
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(user));
        String response = MakeGetRequest(urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return jsonSerializer.DeserializeValidationResult(response);
    }

    @Override
    public String CreatePrivilegeScript(ScriptType scriptType, boolean generateRevokeStatements, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("PrivilegeScripts");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(scriptType.name()));
        ArrayList<RestQueryParameter> queryParameters = new ArrayList<RestQueryParameter>();
        queryParameters.add(new RestQueryParameter("generateRevokeStatements", String.valueOf(generateRevokeStatements)));
        String response = MakeGetRequest(urlPathSegment.toString(), queryParameters, authenticationContext, trackingData);
        return DeserializeBareJsonString(response);
    }

    @Override
    public String CreateSynonymScript(ScriptType scriptType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        StringBuilder urlPathSegment = new StringBuilder();
        urlPathSegment.append("SynonymScripts");
        urlPathSegment.append(urlPathDelimiter);
        urlPathSegment.append(Uri.encode(scriptType.name()));
        String response = MakeGetRequest(urlPathSegment.toString(), new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);
        return DeserializeBareJsonString(response);
    }
    
    /**
     * Creates and sends a REST GET request.
     * @param   urlPathSegment           The path to use in the URL of the request (e.g. "Objects" in the URL "http://192.68.0.1:5000/OraclePermissionGeneratorWebServiceAPI/REST/Objects?").
     * @param   queryParameters          A set of parameters to appear in the query portion of the URL of the request.
     * @param   authenticationContext    Authentication information to include in the URL of the request (as part of the query portion).
     * @param   trackingData             Tracking information to include in the URL of the request (as part of the query portion).
     * @return                           The response from REST request returned as a string.
     * @throws  JSONException            if an error occurs whilst serializing the 'authenticationContext' and 'trackingData' parameters.
     * @throws  URISyntaxException       if an error occurs when creating a URI.
     * @throws  ClientProtocolException  if a HTTP protocol error occurs when making the REST request.
     * @throws  IOException              if an error occurs or the connection was aborted when making the REST request.
     */
    private String MakeGetRequest(String urlPathSegment, ArrayList<RestQueryParameter> queryParameters, AuthenticationContext authenticationContext, TrackingData trackingData) throws JSONException, URISyntaxException, ClientProtocolException, IOException {
        String url = CreateRestUrl(urlPathSegment, queryParameters, authenticationContext, trackingData);

        // Setup objects to create the GET request
        if (instantiatedWithTestConstructor == false) {
            // TODO: According to Apache documentation (http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/client/DefaultHttpClient.html), the DefaultHttpClient class is supposed to implement the Closeable interface.
            //         However, the close() method doesn't appear to be available in Android's implementation of this.
            //         Was concerned that not calling close() here (and for other Make*Request() methods) might cause memory leaks, but since close() is not available, I have to assume below implementation is OK. 
            httpClient = new DefaultHttpClient();
            httpGet = new HttpGet();
        }
        httpGet.setURI(new URI(url));
        HttpResponse httpResponse = httpClient.execute(httpGet);

        return ConvertHttpResponseToString(httpResponse);
    }
    
    /**
     * Creates and sends a REST PUT request.
     * @param   requestBody                   The body of the HTTP request.
     * @param   urlPathSegment                The path to use in the URL of the request (e.g. "Objects" in the URL "http://192.68.0.1:5000/OraclePermissionGeneratorWebServiceAPI/REST/Objects?").
     * @param   queryParameters               A set of parameters to appear in the query portion of the URL of the request.
     * @param   authenticationContext         Authentication information to include in the URL of the request (as part of the query portion).
     * @param   trackingData                  Tracking information to include in the URL of the request (as part of the query portion).
     * @throws  JSONException                 if an error occurs whilst serializing the 'authenticationContext' and 'trackingData' parameters.
     * @throws  URISyntaxException            if an error occurs when creating a URI.
     * @throws  UnsupportedEncodingException  if an error occurs whilst attempting to set the HTTP header on the request.
     * @throws  ClientProtocolException       if a HTTP protocol error occurs when making the REST request.
     * @throws  IOException                   if an error occurs or the connection was aborted when making the REST request, or an error occurs when reading and converting the response to a string.
     */
    private void MakePutRequest(String requestBody, String urlPathSegment, ArrayList<RestQueryParameter> queryParameters, AuthenticationContext authenticationContext, TrackingData trackingData) throws JSONException, URISyntaxException, UnsupportedEncodingException, ClientProtocolException, IOException {
        String url = CreateRestUrl(urlPathSegment, new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);

        // Setup objects to create the PUT request
        if (instantiatedWithTestConstructor == false) {
            httpClient = new DefaultHttpClient();
            httpPut = new HttpPut();
        }
        httpPut.setURI(new URI(url));
        httpPut.setHeader(httpHeaderName, httpHeaderValue);
        httpPut.setEntity(new StringEntity(requestBody));
        
        httpClient.execute(httpPut);
    }
    
    /**
     * Creates and sends a REST POST request.
     * @param   requestBody                   The body of the HTTP request.
     * @param   urlPathSegment                The path to use in the URL of the request (e.g. "Objects" in the URL "http://192.68.0.1:5000/OraclePermissionGeneratorWebServiceAPI/REST/Objects?").
     * @param   queryParameters               A set of parameters to appear in the query portion of the URL of the request.
     * @param   authenticationContext         Authentication information to include in the URL of the request (as part of the query portion).
     * @param   trackingData                  Tracking information to include in the URL of the request (as part of the query portion).
     * @throws  JSONException                 if an error occurs whilst serializing the 'authenticationContext' and 'trackingData' parameters.
     * @throws  URISyntaxException            if an error occurs when creating a URI.
     * @throws  UnsupportedEncodingException  if an error occurs whilst attempting to set the HTTP header on the request.
     * @throws  ClientProtocolException       if a HTTP protocol error occurs when making the REST request.
     * @throws  IOException                   if an error occurs or the connection was aborted when making the REST request, or an error occurs when reading and converting the response to a string.
     */
    private void MakePostRequest(String requestBody, String urlPathSegment, ArrayList<RestQueryParameter> queryParameters, AuthenticationContext authenticationContext, TrackingData trackingData) throws JSONException, URISyntaxException, UnsupportedEncodingException, ClientProtocolException, IOException {
        String url = CreateRestUrl(urlPathSegment, new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);

        // Setup objects to create the POST request
        if (instantiatedWithTestConstructor == false) {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost();
        }
        httpPost.setURI(new URI(url));
        httpPost.setHeader(httpHeaderName, httpHeaderValue);
        httpPost.setEntity(new StringEntity(requestBody));

        httpClient.execute(httpPost);
    }
    
    /**
     * Creates and sends a REST DELETE request.
     * @param   urlPathSegment           The path to use in the URL of the request (e.g. "Objects" in the URL "http://192.68.0.1:5000/OraclePermissionGeneratorWebServiceAPI/REST/Objects?").
     * @param   queryParameters          A set of parameters to appear in the query portion of the URL of the request.
     * @param   authenticationContext    Authentication information to include in the URL of the request (as part of the query portion).
     * @param   trackingData             Tracking information to include in the URL of the request (as part of the query portion).
     * @throws  JSONException            if an error occurs whilst serializing the 'authenticationContext' and 'trackingData' parameters.
     * @throws  URISyntaxException       if an error occurs when creating a URI.
     * @throws  ClientProtocolException  if a HTTP protocol error occurs when making the REST request.
     * @throws  IOException              if an error occurs or the connection was aborted when making the REST request.
     */
    private void MakeDeleteRequest(String urlPathSegment, ArrayList<RestQueryParameter> queryParameters, AuthenticationContext authenticationContext, TrackingData trackingData) throws JSONException, URISyntaxException, ClientProtocolException, IOException {
        String url = CreateRestUrl(urlPathSegment, new ArrayList<RestQueryParameter>(), authenticationContext, trackingData);

        // Setup objects to create the DELETE request
        if (instantiatedWithTestConstructor == false) {
            httpClient = new DefaultHttpClient();
            httpDelete = new HttpDelete();
        }
        httpDelete.setURI(new URI(url));
        httpClient.execute(httpDelete);
    }
    
    /**
     * Creates a valid REST URL using the given path segment, and including the specified query parameters in addition to the supplied AuthenticationContext and TrackingData as query parameters.
     * @param   urlPathSegment         The path to use in the URL (e.g. "Objects" in the URL "http://192.168.0.1:5000/OraclePermissionGeneratorWebServiceAPI/REST/Objects?").
     * @param   queryParameters        A set of parameters to appear in the query portion of the URL.
     * @param   authenticationContext  Authentication information to include in the URL (as part of the query portion).
     * @param   trackingData           Tracking information to include in the URL (as part of the query portion).
     * @return                         The REST URL.
     * @throws  JSONException          if an error occurs whilst serializing the 'authenticationContext' and 'trackingData' parameters.
     */
    private String CreateRestUrl(String urlPathSegment, ArrayList<RestQueryParameter> queryParameters, AuthenticationContext authenticationContext, TrackingData trackingData) throws JSONException {
        // Serialize parameters
        String serializedAuthenticationContext = jsonSerializer.SerializeAuthenticationContext(authenticationContext);
        String serializedTrackingData = jsonSerializer.SerializeTrackingData(trackingData);
        
        // Create the URL
        StringBuilder urlStringBuilder = new StringBuilder(512);
        urlStringBuilder.append(httpUrlPrefix);
        urlStringBuilder.append(dataModelLocation);
        urlStringBuilder.append(urlPathDelimiter);
        urlStringBuilder.append(urlBasePath);
        urlStringBuilder.append(urlPathDelimiter);
        urlStringBuilder.append(urlPathSegment);
        urlStringBuilder.append(urlQueryDelimiter);
        if (queryParameters.size() > 0) {
            for (RestQueryParameter currentParameter : queryParameters) {
                urlStringBuilder.append(currentParameter.Name);
                urlStringBuilder.append("=");
                urlStringBuilder.append(Uri.encode(currentParameter.Value));
                urlStringBuilder.append(urlQueryParameterDelimiter);
            }
        }
        urlStringBuilder.append("authenticationContext=");
        urlStringBuilder.append(Uri.encode(serializedAuthenticationContext));
        urlStringBuilder.append(urlQueryParameterDelimiter);
        urlStringBuilder.append("trackingData=");
        urlStringBuilder.append(Uri.encode(serializedTrackingData));
        return urlStringBuilder.toString();
    }
    
    /**
     * Converts an HttpResponse object to a string.
     * @param   httpResponse  The HttpResponse to convert.
     * @return                The HttpResponse converted to a string.
     * @throws  IOException   if an error occurs when converting the HTTP response to a string.
     */
    private String ConvertHttpResponseToString(HttpResponse httpResponse) throws IOException {
        char[] characterBuffer = new char[(int)httpResponse.getEntity().getContentLength()];
        // TODO: If project compliance is changed to Java 1.7 or higher, change below to try with resources statement  
        InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent());
        try {
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader, (int)httpResponse.getEntity().getContentLength());
            try {
                bufferedReader.read(characterBuffer, 0, (int)httpResponse.getEntity().getContentLength());
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
    
    /**
     * Deserializes a bare encoded JSON string, i.e. a string of the form "value" rather than {"name":"value"}.  .NET WCF gives the option to return JSON responses in bare format, and this method is used to deserialize strings returned in that format.
     * @param   serializedString  The JSON string to deserialize.
     * @return                    The deserialized string.
     */
    private String DeserializeBareJsonString(String serializedString) throws JSONException {
        final String jsonName = "stringValue";
        
        String jsonString = "{\"" + jsonName + "\":" + serializedString + "}";
        JSONObject stringJsonObject = new JSONObject(jsonString);
        return stringJsonObject.getString(jsonName);
    }
    
    /**
     * Container object which holds the name and value of a query parameter to send as part of a REST request.
     */
    private class RestQueryParameter {
        
        public String Name;
        public String Value;
        
        public RestQueryParameter(String name, String value) {
            Name = name;
            Value = value;
        }
    }
}
