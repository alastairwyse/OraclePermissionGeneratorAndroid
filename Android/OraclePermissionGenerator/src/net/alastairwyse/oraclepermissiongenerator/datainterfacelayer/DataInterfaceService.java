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

import java.net.InetAddress;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * Android service which implements the application data interface layer.
 * @author Alastair Wyse
 */
public class DataInterfaceService extends Service implements IDataInterface {

    private final DataInterfaceServiceBinder binder = new DataInterfaceServiceBinder();
    private IRemoteDataModelProxy remoteDataModelProxy;
    private ILocationProvider locationProvider;
    private INetworkInfoProvider networkInfoProvider;
    private ILocalSettingsPersister localSettingsPersister;
    private IScriptPersister scriptPersister;
    private LocalSettings localSettings;

    /**
     * Initialises a new instance of the DataInterfaceService class.
     */
    public DataInterfaceService() {
        super();

        localSettingsPersister = new SharedPreferencesLocalSettingsPersister(this);
        scriptPersister = new AndroidIntentScriptPersister(this);
        localSettings = new LocalSettings();
    }

    /**
     * Initialises a new instance of the DataInterfaceService class.
     * <b>Note</b> this is an additional constructor to facilitate unit tests, and should not be used to instantiate the class under normal conditions.
     * @param  locationProvider        A test (mock) location provider.
     * @param  networkInfoProvider     A test (mock) network info provider.
     * @param  localSettingsPersister  A test (mock) local settings persister.
     * @param  scriptPersister         A test (mock) script persister.
     */
    public DataInterfaceService(ILocationProvider locationProvider, INetworkInfoProvider networkInfoProvider, ILocalSettingsPersister localSettingsPersister, IScriptPersister scriptPersister) {
        super();
        
        this.locationProvider = locationProvider;
        this.networkInfoProvider = networkInfoProvider;
        this.localSettingsPersister = localSettingsPersister;
        this.scriptPersister = scriptPersister;
        localSettings = new LocalSettings();
    }
    
    /**
     * @param  remoteDataModelProxy  The IRemoteDataModelProxy instance to use to call data model methods remotely.
     */
    public void setRemoteDataModelProxy(IRemoteDataModelProxy remoteDataModelProxy) {
        this.remoteDataModelProxy = remoteDataModelProxy;
    }
    
    /**
     * Initializes local members and connections to android system-level services.  Should be called only after the instance of this class has been started as an Android service.
     */
    public void Initialize() {
        // Setup location provider
        LocationProvider locationProvider = new LocationProvider(this);
        locationProvider.Connect();
        this.locationProvider = locationProvider;

        // Setup network info provider 
        NetworkInfoProvider networkInfoProvider = new NetworkInfoProvider(this);
        networkInfoProvider.Connect();
        this.networkInfoProvider = networkInfoProvider;
        
        // Call getLocalSettings() to pull the local settings from persistent storage into the local member
        getLocalSettings();
    }
    
    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, boolean addFlag, boolean removeFlag, ArrayList<RoleToPermissionMap> objectPermissions) throws Exception {
        remoteDataModelProxy.AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void RemoveObjectPermissionSet(String objectName) throws Exception {
        remoteDataModelProxy.RemoveObjectPermissionSet(objectName, getAuthenticationContext(), getTrackingData()); 
    }
    
    @Override
    public ValidationResult ObjectNameValidate(String objectName) throws Exception {
        return remoteDataModelProxy.ObjectNameValidate(objectName, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ValidationResult ObjectTypeValidate(String objectType) throws Exception {
        return remoteDataModelProxy.ObjectTypeValidate(objectType, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ValidationResult ObjectOwnerValidate(String objectOwner) throws Exception {
        return remoteDataModelProxy.ObjectOwnerValidate(objectOwner, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public String getDefaultObjectOwner() throws Exception {
        return remoteDataModelProxy.getDefaultObjectOwner(getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void setDefaultObjectOwner(String defaultObjectOwner) throws Exception {
        remoteDataModelProxy.setDefaultObjectOwner(defaultObjectOwner, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ArrayList<String> getObjectTypes() throws Exception {
        return remoteDataModelProxy.getObjectTypes(getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ArrayList<OracleObjectPermissionSet> getObjects() throws Exception {
        return remoteDataModelProxy.getObjects(getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void SetAddFlag(String objectName, boolean addFlagValue) throws Exception {
        remoteDataModelProxy.SetAddFlag(objectName, addFlagValue, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void SetRemoveFlag(String objectName, boolean removeFlagValue) throws Exception {
        remoteDataModelProxy.SetRemoveFlag(objectName, removeFlagValue, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void AddPermission(String objectName, String role, String permission) throws Exception {
        remoteDataModelProxy.AddPermission(objectName, role, permission, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void RemovePermission(String objectName, String role, String permission) throws Exception {
        remoteDataModelProxy.RemovePermission(objectName, role, permission, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ArrayList<String> getRoles() throws Exception {
        return remoteDataModelProxy.getRoles(getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ArrayList<String> getPermissions(String objectType) throws Exception {
        return remoteDataModelProxy.getPermissions(objectType, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ArrayList<String> getPermissions(String objectName, String role) throws Exception {
        return remoteDataModelProxy.getPermissions(objectName, role, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ArrayList<RoleToUserMap> getMasterRoleToUserMapCollection() throws Exception {
        return remoteDataModelProxy.getMasterRoleToUserMapCollection(getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ArrayList<String> RoleGetReferencingObjects(String role) throws Exception {
        return remoteDataModelProxy.RoleGetReferencingObjects(role, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void AddRoleToUserMap(String role, String user) throws Exception {
        remoteDataModelProxy.AddRoleToUserMap(role, user, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void RemoveRoleToUserMap(String role, String user) throws Exception {
        remoteDataModelProxy.RemoveRoleToUserMap(role, user, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public ValidationResult RoleToUserMapValidate(String role, String user) throws Exception {
        return remoteDataModelProxy.RoleToUserMapValidate(role, user, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void setLocalSettings(LocalSettings localSettings) {
        this.localSettings = localSettings;
        localSettingsPersister.Write(this.localSettings);
    }
    
    @Override
    public LocalSettings getLocalSettings() {
        localSettings = localSettingsPersister.Read();
        return localSettings;
    }
    
    @Override
    public String CreatePrivilegeScript(ScriptType scriptType, boolean generateRevokeStatements) throws Exception {
        return remoteDataModelProxy.CreatePrivilegeScript(scriptType, generateRevokeStatements, getAuthenticationContext(), getTrackingData());
    }

    @Override
    public String CreateSynonymScript(ScriptType scriptType) throws Exception {
        return remoteDataModelProxy.CreateSynonymScript(scriptType, getAuthenticationContext(), getTrackingData());
    }
    
    @Override
    public void WriteScript(String scriptText) {
        scriptPersister.Write(scriptText);
    }
    
    /**
     * @return  The authentication context based on the current configured user.
     */
    private AuthenticationContext getAuthenticationContext() {
        return new AuthenticationContext(localSettings.getUserIdentifier());
    }
    
    /**
     * @return  The current tracking data.
     */
    private TrackingData getTrackingData() {
        TrackingData returnTrackingData = new TrackingData();
        // TODO: locationProvider seems to always be null the first time the code is run on the emulator after booting.  Need to understand why.
        returnTrackingData.setLocation(locationProvider.getLastKnownLocation());
        InetAddress ipAddress = networkInfoProvider.getMobileIpAddress();
        if (ipAddress == null) {
            returnTrackingData.setIpV4Address(null);
        }
        else {
            returnTrackingData.setIpV4Address(ipAddress.getAddress());
        }
        return returnTrackingData;
    }
    
    /**
     * Binder class that is returned by the DataInterfaceService class
     * @author Alastair Wyse
     */
    public class DataInterfaceServiceBinder extends Binder {
        
        /**
         * Returns the outer DataInterfaceService class.
         * @return The DataInterfaceService.
         */
        public DataInterfaceService getService() {
            return DataInterfaceService.this;
        }
    }
}
