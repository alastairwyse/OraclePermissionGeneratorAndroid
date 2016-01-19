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

package net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.serialization;

import java.util.*;

import org.json.*;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * Provides methods to serialize and deserialize objects in the net.alastairwyse.oraclepermissiongenerator.containers package using JSON.
 * @author Alastair Wyse
 */
public class ContainerObjectJsonSerializer {

    /**
     * Initialises a new instance of the ContainerObjectJsonSerializer class.
     */
    public ContainerObjectJsonSerializer() {
    }
    
    /**
     * Serializes the inputted string to a JSON formatted string.
     * @param   jsonName       The name of the object in the JSON encoding.
     * @param   value          The string to serialize.
     * @return                 A string containing the JSON representation of the inputted string.
     * @throws  JSONException  if an error occurs while serializing.
     */
    public String SerializeString(String jsonName, String value) throws JSONException {
        JSONObject stringJsonObject = new JSONObject();
        stringJsonObject.put(jsonName, value);

        return stringJsonObject.toString();
    }
    
    /**
     * Serializes the inputted boolean to a JSON formatted string.
     * @param   jsonName       The name of the object in the JSON encoding.
     * @param   value          The string to serialize.
     * @return                 A string containing the JSON representation of the inputted string.
     * @throws  JSONException  if an error occurs while serializing.
     */
    public String SerializeBoolean(String jsonName, boolean value) throws JSONException {
        JSONObject booleanJsonObject = new JSONObject();
        booleanJsonObject.put(jsonName, value);

        return booleanJsonObject.toString();
    }
    
    /**
     * Serializes the inputted ArrayList of RoleToPermissionMap objects to a string using JSON formatting.
     * @param   roleToPermissionMapArrayList  The ArrayList of RoleToPermissionMap objects to serialize.
     * @return                                A string containing the JSON representation of the object.
     * @throws  JSONException                 if an error occurs while serializing.
     */
    public String SerializeRoleToPermissionMapArrayList(ArrayList<RoleToPermissionMap> roleToPermissionMapArrayList) throws JSONException {
        JSONArray roleToPermissionMapJsonArray = new JSONArray();
        for(RoleToPermissionMap currentRoleToPermissionMap : roleToPermissionMapArrayList) {
            JSONObject currentRoleToPermissionMapJsonObject = new JSONObject();
            currentRoleToPermissionMapJsonObject.put("Role", currentRoleToPermissionMap.getRole());
            currentRoleToPermissionMapJsonObject.put("Permission", currentRoleToPermissionMap.getPermission());
            roleToPermissionMapJsonArray.put(currentRoleToPermissionMapJsonObject);
        }

        return roleToPermissionMapJsonArray.toString();
    }
    
    /**
     * Serializes the inputted AuthenticationContext to a string using JSON formatting.
     * @param   authenticationContext  The AuthenticationContext to serialize.
     * @return                         A string containing the JSON representation of the object.
     * @throws  JSONException          if an error occurs while serializing.
     */
    public String SerializeAuthenticationContext(AuthenticationContext authenticationContext) throws JSONException {
        JSONObject authenticationContextJsonObject = new JSONObject();
        authenticationContextJsonObject.put("UserIdentifier", authenticationContext.getUserIdentifier());

        return authenticationContextJsonObject.toString();
    }
    
    /**
     * Serializes the inputted TrackingData to a string using JSON formatting.
     * @param   trackingData   The TrackingData to serialize.
     * @return                 A string containing the JSON representation of the object.
     * @throws  JSONException  if an error occurs while serializing.
     */
    public String SerializeTrackingData(TrackingData trackingData) throws JSONException {
        JSONObject trackingDataJsonObject = new JSONObject();

        if (trackingData.getLocation() == null) {
            trackingDataJsonObject.put("Location", null);
        }
        else {
            JSONObject locationJsonObject = new JSONObject();
            locationJsonObject.put("Latitude", trackingData.getLocation().getLatitude());
            locationJsonObject.put("Longitude", trackingData.getLocation().getLongitude());
            locationJsonObject.put("SecondsSinceUpdate", trackingData.getLocation().getSecondsSinceUpdate());
            trackingDataJsonObject.put("Location", locationJsonObject);
        }
        
        if (trackingData.getIpV4Address() == null) {
            trackingDataJsonObject.put("IpV4Address", null);
        }
        else {
            JSONArray ipV4AddressJsonArray = new JSONArray();
            for(byte currentByte : trackingData.getIpV4Address()) {
                // .NET byte data type on the web service side is unsigned, so need to convert signed bytes to unsigned integers
                ipV4AddressJsonArray.put((int)(currentByte & 0xFF));
            }
            trackingDataJsonObject.put("IpV4Address", ipV4AddressJsonArray);
        }

        return trackingDataJsonObject.toString();
    }

    /**
     * Deserializes the inputted JSON formatted string to an ArrayList of strings.
     * @param   serializedStringArrayList  A JSON formatted string containing a serialized ArrayList of strings.
     * @return                             The deserialized ArrayList of strings.
     * @throws  JSONException              if an error occurs while deserializing.
     */
    public ArrayList<String> DeserializeStringArrayList(String serializedStringArrayList) throws JSONException {
        ArrayList<String> returnArrayList = new ArrayList<String>();
        
        JSONArray stringArrayListJsonArray = new JSONArray(serializedStringArrayList);
        for (int i = 0; i < stringArrayListJsonArray.length(); i++) {
            returnArrayList.add(stringArrayListJsonArray.getString(i));
        }
        
        return returnArrayList;
    }
    
    /**
     * Deserializes the inputted JSON formatted string to an ArrayList of OracleObjectPermissionSet objects.
     * @param   serializedOracleObjectPermissionSetArrayList  A JSON formatted string containing a serialized ArrayList of OracleObjectPermissionSet objects.
     * @return                                                The deserialized ArrayList of OracleObjectPermissionSet objects.
     * @throws  JSONException                                 if an error occurs while deserializing.
     */
    public ArrayList<OracleObjectPermissionSet> DeserializeOracleObjectPermissionSetArrayList(String serializedOracleObjectPermissionSetArrayList) throws JSONException {
        ArrayList<OracleObjectPermissionSet> returnArrayList = new ArrayList<OracleObjectPermissionSet>();

        JSONArray oracleObjectPermissionSetJsonArray = new JSONArray(serializedOracleObjectPermissionSetArrayList);
        for (int i = 0; i < oracleObjectPermissionSetJsonArray.length(); i++) {
            JSONObject currentOracleObjectPermissionSetJsonObject = oracleObjectPermissionSetJsonArray.getJSONObject(i);
            OracleObjectPermissionSet currentOracleObjectPermissionSet = new OracleObjectPermissionSet();
            // Get all scalar properties from JSONObject, and set on the OracleObjectPermissionSet
            currentOracleObjectPermissionSet.setObjectName(currentOracleObjectPermissionSetJsonObject.getString("ObjectName"));
            currentOracleObjectPermissionSet.setObjectType(currentOracleObjectPermissionSetJsonObject.getString("ObjectType"));
            currentOracleObjectPermissionSet.setObjectOwner(currentOracleObjectPermissionSetJsonObject.getString("ObjectOwner"));
            currentOracleObjectPermissionSet.setAddFlag(currentOracleObjectPermissionSetJsonObject.getBoolean("AddFlag"));
            currentOracleObjectPermissionSet.setRemoveFlag(currentOracleObjectPermissionSetJsonObject.getBoolean("RemoveFlag"));
            // Get and iterate through the array of role to permission maps
            JSONArray roleToPermissionMapJsonArray = currentOracleObjectPermissionSetJsonObject.getJSONArray("ObjectPermissions");
            ArrayList<RoleToPermissionMap> currentRoleToPermissionMaps = new ArrayList<RoleToPermissionMap>();
            for(int j = 0; j < roleToPermissionMapJsonArray.length(); j++) {
                JSONObject currentroleToPermissionMapJsonObject = roleToPermissionMapJsonArray.getJSONObject(j);
                String role = currentroleToPermissionMapJsonObject.getString("Role");
                String permission = currentroleToPermissionMapJsonObject.getString("Permission");
                currentRoleToPermissionMaps.add(new RoleToPermissionMap(role, permission));
            }
            currentOracleObjectPermissionSet.setObjectPermissions(currentRoleToPermissionMaps);
            returnArrayList.add(currentOracleObjectPermissionSet);
        }
        
        return returnArrayList;
    }
    
    /**
     * Deserializes the inputted JSON formatted string to an ArrayList of RoleToPermissionMap objects.
     * @param   serializedRoleToPermissionMapArrayList  A JSON formatted string containing a serialized ArrayList of RoleToPermissionMap objects.
     * @return                                          The deserialized ArrayList of RoleToPermissionMap objects.
     * @throws  JSONException                           if an error occurs while deserializing.
     */
    public ArrayList<RoleToPermissionMap> DeserializeRoleToPermissionMapArrayList(String serializedRoleToPermissionMapArrayList) throws JSONException {
        ArrayList<RoleToPermissionMap> returnArray = new ArrayList<RoleToPermissionMap>();
        JSONArray roleToPermissionMapJsonArray = new JSONArray(serializedRoleToPermissionMapArrayList);
        for(int i = 0; i < roleToPermissionMapJsonArray.length(); i++) {
            JSONObject currentObject = roleToPermissionMapJsonArray.getJSONObject(i);
            String role = currentObject.get("Role").toString();
            String permission = currentObject.get("Permission").toString();
            returnArray.add(new RoleToPermissionMap(role, permission));
        }
        return returnArray;
    }
    
    public ArrayList<RoleToUserMap> DeserializeRoleToUserMapArrayList(String serializedRoleToUserMapArrayList) throws JSONException {
        ArrayList<RoleToUserMap> returnArray = new ArrayList<RoleToUserMap>();
        JSONArray roleToUserMapJsonArray = new JSONArray(serializedRoleToUserMapArrayList);
        for(int i = 0; i < roleToUserMapJsonArray.length(); i++) {
            JSONObject currentObject = roleToUserMapJsonArray.getJSONObject(i);
            String role = currentObject.get("Role").toString();
            String user = currentObject.get("User").toString();
            returnArray.add(new RoleToUserMap(role, user));
        }
        return returnArray;
    }
    
    /**
     * Deserializes the inputted JSON formatted string to a ValidationResult object.
     * @param   serializedValidationResult  A JSON formatted string containing a serialized ValidationResult object.
     * @return                              The deserialized ValidationResult object.
     * @throws  JSONException               if an error occurs while deserializing.
     */
    public ValidationResult DeserializeValidationResult(String serializedValidationResult) throws JSONException {
        JSONObject validationResultJsonObject = new JSONObject(serializedValidationResult);
        ValidationResult returnValidationResult = new ValidationResult(validationResultJsonObject.getBoolean("IsValid"), validationResultJsonObject.getString("ValidationError"));
        
        return returnValidationResult;
    }
}
