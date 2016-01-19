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

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * Defines methods which are proxied to and called on a remote instance of the Oracle Permission Generator data model.
 * @author Alastair Wyse
 */
public interface IRemoteDataModelProxy {

    /**
     * Adds an Oracle object permission set to the data layer.
     * @param   objectName             The name of the object.
     * @param   objectType             The type of the object.
     * @param   objectOwner            The user who owns the object.
     * @param   addFlag                Specifies whether statements should be generated to add this object when generating Oracle scripts.
     * @param   removeFlag             Specifies whether statements should be generated to remove this object when generating Oracle scripts.
     * @param   objectPermissions      The set of permissions for the object.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, boolean addFlag, boolean removeFlag, ArrayList<RoleToPermissionMap> objectPermissions, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Removes an Oracle object permission set from the data layer.
     * @param   objectName             The name of the object.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public void RemoveObjectPermissionSet(String objectName, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Checks whether the inputted object name is valid in the data model.
     * @param   objectName             The object name.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         The result of the validation.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ValidationResult ObjectNameValidate(String objectName, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Checks whether the inputted object type is valid in the data model.
     * @param   objectType             The object type.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         The result of the validation.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ValidationResult ObjectTypeValidate(String objectType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Checks whether the inputted object owner is valid in the data model.
     * @param   objectOwner            The object owner.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         The result of the validation.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ValidationResult ObjectOwnerValidate(String objectOwner, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         The default object owner for object permission sets.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public String getDefaultObjectOwner(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * @param   defaultObjectOwner     The default object owner for object permission sets.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public void setDefaultObjectOwner(String defaultObjectOwner, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         A list of all object types in the data layer.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ArrayList<String> getObjectTypes(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         A list of all Oracle objects in the data layer.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ArrayList<OracleObjectPermissionSet> getObjects(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Sets the add flag of the specified Oracle object.
     * @param   objectName             The name of the object to set the add flag for.
     * @param   addFlagValue           The value of the add flag.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public void SetAddFlag(String objectName, boolean addFlagValue, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Sets the remove flag of the specified Oracle object.
     * @param   objectName             The name of the object to set the remove flag for.
     * @param   removeFlagValue        The value of the add flag.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */ 
    public void SetRemoveFlag(String objectName, boolean removeFlagValue, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Adds a new permission to a specified Oracle object and role.
     * @param   objectName             The name of the object to add the permission for.
     * @param   role                   The role to apply the permission to.
     * @param   permission             The permission to add.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public void AddPermission(String objectName, String role, String permission, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Removes a permission from a specified Oracle object and role.
     * @param   objectName             The name of the object to remove the permission from.
     * @param   role                   The role to remove the permission from.
     * @param   permission             The permission to remove.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public void RemovePermission(String objectName, String role, String permission, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         A list of all roles in the data layer.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ArrayList<String> getRoles(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * @param   objectType             The object type to return the permission for.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         A list of all available permissions for the specified object type in the data layer.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ArrayList<String> getPermissions(String objectType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * @param   objectName             The object to return the permission for.
     * @param   role                   The role to return the permissions for.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         A list of all permissions set on the specified object assigned to the specified role in the data layer.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ArrayList<String> getPermissions(String objectName, String role, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         The master list of role to user mappings in the data layer.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ArrayList<RoleToUserMap> getMasterRoleToUserMapCollection(AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Returns a list of names of objects which reference the inputted role.
     * @param   role                   The name of the role.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         A list of objects which reference the specified role.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ArrayList<String> RoleGetReferencingObjects(String role, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Adds a role to user mapping to the master lookup list.
     * @param                          role  The name of the role.
     * @param                          user  The name of the user.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public void AddRoleToUserMap(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Removes a role to user mapping from the master lookup list.
     * @param   role                   The name of the role.
     * @param   user                   The name of the user.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public void RemoveRoleToUserMap(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;

    /**
     * Checks whether the inputted role to user map is valid in the data model.
     * @param   role                   The name of the role.
     * @param   user                   The name of the user.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         The result of the validation.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public ValidationResult RoleToUserMapValidate(String role, String user, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
        
    /**
     * Generates an Oracle privilege script.
     * @param   scriptType                The type of script to generate.
     * @param   generateRevokeStatements  Specifies whether revoke statements should be included in the script.
     * @param   authenticationContext     Authentication information to pass to the remote data model.
     * @param   trackingData              Tracking information to pass to the remote data model.
     * @return                            The Oracle permission script.
     * @throws  Exception                 if an error occurs when calling the remote data model.
     */
    public String CreatePrivilegeScript(ScriptType scriptType, boolean generateRevokeStatements, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
    
    /**
     * Generates an Oracle synonym script.
     * @param   scriptType             The type of script to generate.
     * @param   authenticationContext  Authentication information to pass to the remote data model.
     * @param   trackingData           Tracking information to pass to the remote data model.
     * @return                         The Oracle synonym script.
     * @throws  Exception              if an error occurs when calling the remote data model.
     */
    public String CreateSynonymScript(ScriptType scriptType, AuthenticationContext authenticationContext, TrackingData trackingData) throws Exception;
}
