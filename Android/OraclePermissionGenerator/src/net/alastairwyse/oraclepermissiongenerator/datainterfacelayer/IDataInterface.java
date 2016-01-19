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

import java.util.*;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * The data interface layer of the Oracle Permission Generator application.  Equivalent to the model in the MVP pattern.
 * @author Alastair Wyse
 */
public interface IDataInterface {

    /**
     * Adds an Oracle object permission set to the data layer.
     * @param  objectName         The name of the object.
     * @param  objectType         The type of the object.
     * @param  objectOwner        The user who owns the object.
     * @param  addFlag            Specifies whether statements should be generated to add this object when generating Oracle scripts.
     * @param  removeFlag         Specifies whether statements should be generated to remove this object when generating Oracle scripts.
     * @param  objectPermissions  The set of permissions for the object.
     * @throws Exception          if an error occurs when accessing the data layer.
     */
    public void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, boolean addFlag, boolean removeFlag, ArrayList<RoleToPermissionMap> objectPermissions) throws Exception;
    
    /**
     * Removes an Oracle object permission set from the data layer.
     * @param   objectName  The name of the object.
     * @throws  Exception   if an error occurs when accessing the data layer.
     */
    public void RemoveObjectPermissionSet(String objectName) throws Exception;
    
    /**
     * Checks whether the inputted object name is valid in the data model.
     * @param   objectName  The object name.
     * @return              The result of the validation.
     * @throws  Exception   if an error occurs when accessing the data layer.
     */
    public ValidationResult ObjectNameValidate(String objectName) throws Exception;
    
    /**
     * Checks whether the inputted object type is valid in the data model.
     * @param   objectType  The object type.
     * @return              The result of the validation.
     * @throws  Exception   if an error occurs when accessing the data layer.
     */
    public ValidationResult ObjectTypeValidate(String objectType) throws Exception;
    
    /**
     * Checks whether the inputted object owner is valid in the data model.
     * @param   objectOwner  The object owner.
     * @return               The result of the validation.
     * @throws  Exception    if an error occurs when accessing the data layer.
     */
    public ValidationResult ObjectOwnerValidate(String objectOwner) throws Exception;
    
    /**
     * @return             The default object owner for object permission sets.
     * @throws  Exception  if an error occurs when accessing the data layer.
     */
    public String getDefaultObjectOwner() throws Exception;
    
    /**
     * @param defaultObjectOwner  The default object owner for object permission sets.
     * @throws Exception          if an error occurs when accessing the data layer.
     */
    public void setDefaultObjectOwner(String defaultObjectOwner) throws Exception;
    
    /**
     * @return             A list of all object types in the data layer.
     * @throws  Exception  if an error occurs when accessing the data layer.
     */
    public ArrayList<String> getObjectTypes() throws Exception;
    
    /**
     * @return             A list of all Oracle objects in the data layer.
     * @throws  Exception  if an error occurs when accessing the data layer.
     */
    public ArrayList<OracleObjectPermissionSet> getObjects() throws Exception;
    
    /**
     * Sets the add flag of the specified Oracle object.
     * @param   objectName    The name of the object to set the add flag for.
     * @param   addFlagValue  The value of the add flag.
     * @throws  Exception     if an error occurs when accessing the data layer.
     */
    public void SetAddFlag(String objectName, boolean addFlagValue) throws Exception;
    
    /**
     * Sets the remove flag of the specified Oracle object.
     * @param   objectName       The name of the object to set the remove flag for.
     * @param   removeFlagValue  The value of the add flag.
     * @throws  Exception        if an error occurs when accessing the data layer.
     */
    public void SetRemoveFlag(String objectName, boolean removeFlagValue) throws Exception;
    
    /**
     * Adds a new permission to a specified Oracle object and role.
     * @param   objectName  The name of the object to add the permission for.
     * @param   role        The role to apply the permission to.
     * @param   permission  The permission to add.
     * @throws  Exception   if an error occurs when accessing the data layer.
     */
    public void AddPermission(String objectName, String role, String permission) throws Exception;
    
    /**
     * Removes a permission from a specified Oracle object and role.
     * @param   objectName  The name of the object to remove the permission from.
     * @param   role        The role to remove the permission from.
     * @param   permission  The permission to remove.
     * @throws  Exception   if an error occurs when accessing the data layer.
     */
    public void RemovePermission(String objectName, String role, String permission) throws Exception;
    
    /**
     * @return                   A list of all roles in the data layer.
     * @throws  Exception        if an error occurs when accessing the data layer.
     */
    public ArrayList<String> getRoles() throws Exception;
    
    /**
     * @param   objectType  The object type to return the permission for.
     * @return              A list of all available permissions for the specified object type in the data layer.
     * @throws  Exception   if an error occurs when accessing the data layer.
     */
    public ArrayList<String> getPermissions(String objectType) throws Exception;
    
    /**
     * @param   objectName  The object to return the permission for.
     * @param   role        The role to return the permissions for.
     * @return              A list of all permissions set on the specified object assigned to the specified role in the data layer.
     * @throws  Exception   if an error occurs when accessing the data layer.
     */
    public ArrayList<String> getPermissions(String objectName, String role) throws Exception;
    
    /**
     * @return  The master list of role to user mappings in the data layer.
     * @throws  Exception  if an error occurs when accessing the data layer.
     */
    public ArrayList<RoleToUserMap> getMasterRoleToUserMapCollection() throws Exception;
    
    /**
     * Returns a list of names of objects which reference the inputted role.
     * @param   role  The name of the role.
     * @return  A list of objects which reference the specified role.
     * @throws  Exception  if an error occurs when accessing the data layer.
     */
    public ArrayList<String> RoleGetReferencingObjects(String role) throws Exception;
    
    /**
     * Adds a role to user mapping to the master lookup list.
     * @param   role  The name of the role.
     * @param   user  The name of the user.
     * @throws  Exception  if an error occurs when accessing the data layer.
     */
    public void AddRoleToUserMap(String role, String user) throws Exception;
    
    /**
     * Removes a role to user mapping from the master lookup list.
     * @param   role                   The name of the role.
     * @param   user                   The name of the user.
     * @throws  Exception              if an error occurs when accessing the data layer.
     */
    public void RemoveRoleToUserMap(String role, String user) throws Exception;

    /**
     * Checks whether the inputted role to user map is valid in the data model.
     * @param    role       The name of the role.
     * @param    user       The name of the user.
     * @return              The result of the validation.
     * @throws   Exception  if an error occurs when accessing the data layer.
     */
    public ValidationResult RoleToUserMapValidate(String role, String user) throws Exception;
    
    /**
     * @param  localSettings  Persists the given local setting to the device.
     */
    public void setLocalSettings(LocalSettings localSettings);
    
    /**
     * Retrieves the local settings from the device storage, and caches them in the data layer.
     * @return  Local settings for the application.
     */
    public LocalSettings getLocalSettings();
    
    /**
     * Generates an Oracle privilege script.
     * @param   scriptType                The type of script to generate.
     * @param   generateRevokeStatements  Specifies whether revoke statements should be included in the script.
     * @return                            The Oracle permission script.
     * @throws  Exception                 if an error occurs when accessing the data layer.
     */
    public String CreatePrivilegeScript(ScriptType scriptType, boolean generateRevokeStatements) throws Exception;
    
    /**
     * Generates an Oracle synonym script.
     * @param   scriptType  The type of script to generate.
     * @return              The Oracle synonym script.
     * @throws  Exception   if an error occurs when accessing the data layer.
     */
    public String CreateSynonymScript(ScriptType scriptType) throws Exception;
    
    /**
     * Write the provided Oracle script text to persistent storage.
     * @param  scriptText  The text of the script.
     */
    public void WriteScript(String scriptText);
}
