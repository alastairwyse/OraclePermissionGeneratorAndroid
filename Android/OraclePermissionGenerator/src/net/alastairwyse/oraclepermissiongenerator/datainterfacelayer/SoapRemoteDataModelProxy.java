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

import java.util.ArrayList;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;
import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.serialization.*;

/**
 * Proxies method calls to a remote instance of the Oracle Permission Generator data model running as a SOAP web service.
 * @author Alastair Wyse
 */
public class SoapRemoteDataModelProxy implements IRemoteDataModelProxy {

    private final String xmlVersionTag = "<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->";
    private final String webServiceNamespace = "http://tempuri.org/";
    private final String soapActionPrefix = "http://tempuri.org/ISoapWebServiceApi/";
    private final String httpTransportUrlPrefix = "http://";
    private final String httpTransportUrlPostfix = "/OraclePermissionGeneratorWebServiceAPI/SOAP";
    
    private String dataModelLocation;
    private ContainerObjectJsonSerializer jsonSerializer;
    private SoapObject soapObject;
    private SoapSerializationEnvelope envelope;
    private HttpTransportSE transport;
    private boolean instantiatedWithTestConstructor = false;
    
    /**
     * Initialises a new instance of the SoapRemoteDataModelProxy class.
     */
    public SoapRemoteDataModelProxy() {
        jsonSerializer = new ContainerObjectJsonSerializer();
    }
    
    /**
     * Initialises a new instance of the SoapRemoteDataModelProxy class.
     * <b>Note</b> this is an additional constructor to facilitate unit tests, and should not be used to instantiate the class under normal conditions.
     * @param  jsonSerializer  A test (mock) JSON serializer.
     * @param  soapObject      A test (mock) SOAP object.
     * @param  envelope        A test (mock) SOAP serialization envelope.
     * @param  transport       A test (mock) HTTP transport provider.
     */
    public SoapRemoteDataModelProxy(ContainerObjectJsonSerializer jsonSerializer, SoapObject soapObject, SoapSerializationEnvelope envelope, HttpTransportSE transport) {
        super();
        
        this.jsonSerializer = jsonSerializer;
        this.soapObject = soapObject;
        this.envelope = envelope;
        this.transport = transport;
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
        String serializedObjectPermissions = jsonSerializer.SerializeRoleToPermissionMapArrayList(objectPermissions);
        
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectName", objectName));
        parameters.add(new SoapProperty("objectType", objectType));
        parameters.add(new SoapProperty("objectOwner", objectOwner));
        parameters.add(new SoapProperty("addFlag", addFlag));
        parameters.add(new SoapProperty("removeFlag", removeFlag));
        parameters.add(new SoapProperty("objectPermissions", serializedObjectPermissions));
        MakeSoapRequest("AddObjectPermissionSet", parameters, authenticationContext, trackingData);
    }

    @Override
    public void RemoveObjectPermissionSet(String objectName, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectName", objectName));
        MakeSoapRequest("RemoveObjectPermissionSet", parameters, authenticationContext, trackingData);
    }

    @Override
    public ValidationResult ObjectNameValidate(String objectName, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectName", objectName));
        Object result = MakeSoapRequest("ObjectNameValidate", parameters, authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ValidationResult validationResult = jsonSerializer.DeserializeValidationResult(result.toString());
        return validationResult;
    }

    @Override
    public ValidationResult ObjectTypeValidate(String objectType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectType", objectType));
        Object result = MakeSoapRequest("ObjectTypeValidate", parameters, authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ValidationResult validationResult = jsonSerializer.DeserializeValidationResult(result.toString());
        return validationResult;
    }

    @Override
    public ValidationResult ObjectOwnerValidate(String objectOwner, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectOwner", objectOwner));
        Object result = MakeSoapRequest("ObjectOwnerValidate", parameters, authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ValidationResult validationResult = jsonSerializer.DeserializeValidationResult(result.toString());
        return validationResult;
    }

    @Override
    public String getDefaultObjectOwner(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        Object result = MakeSoapRequest("GetDefaultObjectOwner", new ArrayList<SoapProperty>(), authenticationContext, trackingData);

        return DeserializeSoapString(result.toString());
    }

    @Override
    public void setDefaultObjectOwner(String defaultObjectOwner, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("defaultObjectOwner", defaultObjectOwner));
        MakeSoapRequest("SetDefaultObjectOwner", parameters, authenticationContext, trackingData);
    }

    @Override
    public ArrayList<String> getObjectTypes(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        Object result = MakeSoapRequest("GetObjectTypes", new ArrayList<SoapProperty>(), authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ArrayList<String> objectTypes = jsonSerializer.DeserializeStringArrayList(result.toString());
        return objectTypes;
    }

    @Override
    public ArrayList<OracleObjectPermissionSet> getObjects(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        Object result = MakeSoapRequest("GetObjects", new ArrayList<SoapProperty>(), authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ArrayList<OracleObjectPermissionSet> permissionSet = jsonSerializer.DeserializeOracleObjectPermissionSetArrayList(result.toString());
        return permissionSet;
    }

    @Override
    public void SetAddFlag(String objectName, boolean addFlagValue, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectName", objectName));
        parameters.add(new SoapProperty("addFlagValue", addFlagValue));
        MakeSoapRequest("SetAddFlag", parameters, authenticationContext, trackingData);
    }

    @Override
    public void SetRemoveFlag(String objectName, boolean removeFlagValue, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectName", objectName));
        parameters.add(new SoapProperty("removeFlagValue", removeFlagValue));
        MakeSoapRequest("SetRemoveFlag", parameters, authenticationContext, trackingData);
    }

    @Override
    public void AddPermission(String objectName, String role, String permission, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectName", objectName));
        parameters.add(new SoapProperty("role", role));
        parameters.add(new SoapProperty("permission", permission));
        MakeSoapRequest("AddPermission", parameters, authenticationContext, trackingData);
    }

    @Override
    public void RemovePermission(String objectName, String role, String permission, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectName", objectName));
        parameters.add(new SoapProperty("role", role));
        parameters.add(new SoapProperty("permission", permission));
        MakeSoapRequest("RemovePermission", parameters, authenticationContext, trackingData);
    }

    @Override
    public ArrayList<String> getRoles(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        Object result = MakeSoapRequest("GetRoles", new ArrayList<SoapProperty>(), authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ArrayList<String> roles = jsonSerializer.DeserializeStringArrayList(result.toString());
        return roles;
    }

    @Override
    public ArrayList<String> getPermissions(String objectType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectType", objectType));
        Object result = MakeSoapRequest("GetPermissions", parameters, authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ArrayList<String> permissions = jsonSerializer.DeserializeStringArrayList(result.toString());
        return permissions;
    }

    @Override
    public ArrayList<String> getPermissions(String objectName, String role, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("objectName", objectName));
        parameters.add(new SoapProperty("role", role));
        Object result = MakeSoapRequest("GetPermissionsForObject", parameters, authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ArrayList<String> permissions = jsonSerializer.DeserializeStringArrayList(result.toString());
        return permissions;
    }

    @Override
    public ArrayList<RoleToUserMap> getMasterRoleToUserMapCollection(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        Object result = MakeSoapRequest("GetMasterRoleToUserMapCollection", new ArrayList<SoapProperty>(), authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ArrayList<RoleToUserMap> roleToUserMaps = jsonSerializer.DeserializeRoleToUserMapArrayList(result.toString());
        return roleToUserMaps;
    }

    @Override
    public ArrayList<String> RoleGetReferencingObjects(String role, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("role", role));
        Object result = MakeSoapRequest("RoleGetReferencingObjects", parameters, authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ArrayList<String> referencingObjects = jsonSerializer.DeserializeStringArrayList(result.toString());
        return referencingObjects;
    }

    @Override
    public void AddRoleToUserMap(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("role", role));
        parameters.add(new SoapProperty("user", user));
        MakeSoapRequest("AddRoleToUserMap", parameters, authenticationContext, trackingData);
    }

    @Override
    public void RemoveRoleToUserMap(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("role", role));
        parameters.add(new SoapProperty("user", user));
        MakeSoapRequest("RemoveRoleToUserMap", parameters, authenticationContext, trackingData);
    }

    @Override
    public ValidationResult RoleToUserMapValidate(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("role", role));
        parameters.add(new SoapProperty("user", user));
        Object result = MakeSoapRequest("RoleToUserMapValidate", parameters, authenticationContext, trackingData);
        
        // Deserialize SOAP response
        ValidationResult validationResult = jsonSerializer.DeserializeValidationResult(result.toString());
        return validationResult;
    }

    @Override
    public String CreatePrivilegeScript(ScriptType scriptType, boolean generateRevokeStatements, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("scriptType", scriptType.name()));
        parameters.add(new SoapProperty("generateRevokeStatements", generateRevokeStatements));
        Object result = MakeSoapRequest("CreatePrivilegeScript", parameters, authenticationContext, trackingData);

        return DeserializeSoapString(result.toString());
    }

    @Override
    public String CreateSynonymScript(ScriptType scriptType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception {
        ArrayList<SoapProperty> parameters = new ArrayList<SoapProperty>();
        parameters.add(new SoapProperty("scriptType", scriptType.name()));
        Object result = MakeSoapRequest("CreateSynonymScript", parameters, authenticationContext, trackingData);

        return DeserializeSoapString(result.toString());
    }
    
    /**
     * Makes a SOAP web service request based on the supplied operation name and soap property parameters.
     * @param   operationName                          The SOAP operation name (i.e. remote method to call).
     * @param   parameters                             The parameters to pass to the method.
     * @param   authenticationContext                  Authentication information to pass to the remote method.
     * @param   trackingData                           Tracking information to pass to the remote method.
     * @return                                         Object containing the response to the SOAP request
     * @throws  IOException                            if an error occurs whilst making HTTP transport call as part of the SOAP request.
     * @throws  org.xmlpull.v1.XmlPullParserException  if an error occurs whilst making HTTP transport call as part of the SOAP request.
     * @throws  org.json.JSONException                 if an error occurs whilst serializing the SOAP request parameters to JSON strings.
     */
    private Object MakeSoapRequest(String operationName, ArrayList<SoapProperty> parameters, AuthenticationContext authenticationContext, TrackingData trackingData) throws IOException, XmlPullParserException,  JSONException {
        // Create the ksoap2 utility objects
        if (instantiatedWithTestConstructor == false) {
            soapObject = new SoapObject(webServiceNamespace, operationName);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            String httpTransportUrl = httpTransportUrlPrefix + dataModelLocation + httpTransportUrlPostfix;
            transport = new HttpTransportSE(httpTransportUrl);
        }

        // Add the parameters to the SoapObject 
        for (SoapProperty currentSoapProperty : parameters) {
            soapObject.addProperty(currentSoapProperty.Name, currentSoapProperty.Value);
        }
        soapObject.addProperty("authenticationContext", jsonSerializer.SerializeAuthenticationContext(authenticationContext));
        soapObject.addProperty("trackingData", jsonSerializer.SerializeTrackingData(trackingData));

        // Create the SOAP envelope and add the parameters to it
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setOutputSoapObject(soapObject);

        // Execute the SOAP request
        transport.debug = true;
        transport.setXmlVersionTag(xmlVersionTag);
        transport.call(soapActionPrefix + operationName, envelope);
        return envelope.getResponse();
    }
    
    /**
     * Deserializes a raw encoded SOAP string.  This method is required because kSOAP doesn't seem to be able to handle deserializing a blank string (SoapSerializationEnvelope.getResponse().toString() returns "anyType{}"
     * @param   serializedString  The SOAP string to deserialize.
     * @return                    The deserialized string.
     */
    private String DeserializeSoapString(String serializedString) {
        final String nullRepresentation = "anyType{}";
        
        if (serializedString.equals(nullRepresentation)) {
            return "";
        }
        else {
            return serializedString;
        }
    }
    
    /**
     * Container object which holds the name and value of a soap property to send to the web service as part of a method call.
     */
    private class SoapProperty {
        
        public String Name;
        public Object Value;

        public SoapProperty(String name, Object value) {
            Name = name;
            Value = value;
        }
    }
}
