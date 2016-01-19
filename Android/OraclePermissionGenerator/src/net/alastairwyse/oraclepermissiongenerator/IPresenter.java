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

package net.alastairwyse.oraclepermissiongenerator;

import net.alastairwyse.oraclepermissiongenerator.containers.RemoteDataModelProxyType;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.ScriptType;

/**
 * Defines the methods of the MVP pattern presenter component within the application. 
 * @author Alastair Wyse
 */
public interface IPresenter {

    /**
     * @param  objectListView  The object list view associated with the presenter.
     */
    void setObjectListView(IObjectListView objectListView);

    /**
     * @param  addObjectView  The add object view associated with the presenter.
     */
    void setAddObjectView(IAddObjectView addObjectView);
    
    /**
     * @param  selectRoleView  The select role view associated with the presenter.
     */
    void setSelectRoleView(ISelectRoleView selectRoleView);
    
    /**
     * @param  setPermissionsView  The set permissions view associated with the presenter.
     */
    void setSetPermissionsView(ISetPermissionsView setPermissionsView);
    
    /**
     * @param  roleToUserMapView  The role to user map view associated with the presenter.
     */
    void setRoleToUserMapView(IRoleToUserMapView roleToUserMapView);
    
    /**
     * @param  addRoleToUserMapView  The add role to user map view associated with the presenter.
     */
    void setAddRoleToUserMapView(IAddRoleToUserMapView addRoleToUserMapView);
    
    /**
     * @param  settingsView  The settings view associated with the presenter.
     */
    void setSettingsView(ISettingsView settingsView);
    
    /**
     * @param  connectionSettingsView  The connection settings view associated with the presenter.
     */
    void setConnectionSettingsView(IConnectionSettingsView connectionSettingsView);
    
    /**
     * @param  selectScriptView  The select script view associated with the presenter.
     */
    void setSelectScriptView(ISelectScriptView selectScriptView);
    
    /**
     * @param  exceptionLogger  The logger that the presenter should use for logging exceptions.
     */
    void setExceptionLogger(IExceptionLogger exceptionLogger);
    
    /**
     * Initialises the presenter.
     */
    void Initialise();
    
    /**
     * Adds an Oracle object to the data interface layer and object list view.
     * @param  objectName   The name of the object.
     * @param  objectType   The type of the object.
     * @param  objectOwner  The user who owns the object.
     * @param  addFlag      Specifies whether statements should be generated to add this object when generating Oracle scripts.
     * @param  removeFlag   Specifies whether statements should be generated to remove this object when generating Oracle scripts.
     */
    void AddObject(String objectName, String objectType, String objectOwner, boolean addFlag, boolean removeFlag);
    
    /**
     * Removes the object with the specified name from the data interface layer and object list view.
     * @param  objectName  The name of the object to remove.
     */
    void RemoveObject(String objectName);
    
    /**
     * Sets the 'add' flag on the specified object.
     * @param  objectName    The name of the object to set the add flag for.
     * @param  addFlagValue  The value of the add flag.
     */
    void SetAddFlag(String objectName, boolean addFlagValue);
    
    /**
     * Sets the 'remove' flag on the specified object.
     * @param  objectName       The name of the object to set the remove flag for.
     * @param  removeFlagValue  The value of the remove flag.
     */
    void SetRemoveFlag(String objectName, boolean removeFlagValue);
    
    /**
     * Sets a permission for the specified object and role.
     * @param  objectName  The name of the object to set the permission for.
     * @param  role        The role to set the permission for.
     * @param  permission  The permission to set.
     * @param  value       Whether the permission should be set or not (false implies remove the permission).
     */
    void SetPermssion(String objectName, String role, String permission, boolean value);
    
    /**
     * Adds a role to user mapping to the data interface layer and role to user map view.
     * @param  role  The name of the role to add the mapping for.
     * @param  user  The name of the user to add the mapping for.
     */
    void AddRoleToUserMap(String role, String user);
    
    /**
     * Removes a role to user mapping from the data interface layer and role to user map view.
     * @param  role  The name of the role to remove the mapping for.
     * @param  user  The name of the user to remove the mapping for.
     */
    void RemoveRoleToUserMap(String role, String user);
    
    /**
     * Saves the application settings to the data interface layer.
     * @param  defaultObjectOwner  The default object owner.
     */
    void SaveSettings(String defaultObjectOwner);
    
    /**
     * Saves the connection settings to the data interface layer.
     * @param  userIdentifier            The unique identifier for the user.
     * @param  remoteDataModelProxyType  The mechanism of proxy to the remote data model instance
     * @param  soapDataServiceLocation   The network location of the SOAP data service.
     * @param  restDataServiceLocation   The network location of the REST data service.
     */
    void SaveConnectionSettings(String userIdentifier, RemoteDataModelProxyType remoteDataModelProxyType, String soapDataServiceLocation, String restDataServiceLocation);
    
    /**
     * Creates and persists an Oracle privilege script.
     * @param  scriptType                The type of script to generate.
     * @param  generateRevokeStatements  Specifies whether revoke statements should be included in the script.
     */
    void GeneratePrivilegeScript(ScriptType scriptType, boolean generateRevokeStatements);
    
    /**
     * Creates and persists an Oracle synonym script.
     * @param  scriptType  The type of script to generate.
     */
    void GenerateSynonymScript(ScriptType scriptType);
    
    /**
     * Displays the view to add objects to the application.
     */
    void ShowAddObjectView();
    
    /**
     * Display the view to select a role to allow modification of object permissions.
     * @param  objectName  The name of the object the select role view is being shown for.
     * @param  objectType  The type of the object the select role view is being shown for.
     */
    void ShowSelectRoleView(String objectName, String objectType);
    
    /**
     * Displays the view to set object permissions.
     * @param  objectName  The name of the object to set the permissions for.
     * @param  objectType  The type of the object to set the permissions for.
     * @param  role        The role to set the permissions for.
     */
    void ShowSetPermissionsView(String objectName, String objectType, String role);
    
    /**
     * Displays the view to display and modify role to user mappings.
     */
    void ShowRoleToUserMapView();
    
    /**
     * Displays the view to add role to user mappings.
     */
    void ShowAddRoleToUserMapView();
    
    /**
     * Displays the view to display and modify application settings.
     */
    void ShowSettingsView();
    
    /**
     * Displays the view to display and modify connection settings.
     */
    void ShowConnectionSettingsView();
    
    /**
     * Displays the view to select and generate an Oracle permission script.
     */
    void ShowSelectScriptView();
    
    /**
     * Exits from the application.
     * @param  exitStatus  Indicates the status of the exit.  Typically this would be 0 for a normal, graceful exit, and 1 for an abnormal exit, e.g. due to an exception.
     */
    void Exit(int exitStatus);
}
