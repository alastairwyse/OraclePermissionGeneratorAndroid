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

import android.content.Context;

import net.alastairwyse.oraclepermissiongenerator.containers.LocalSettings;
import net.alastairwyse.oraclepermissiongenerator.containers.RemoteDataModelProxyType;

/**
 * Stores local settings for the application using the android SharedPreferences class.
 * @author Alastair Wyse
 */
public class SharedPreferencesLocalSettingsPersister implements ILocalSettingsPersister {

    private Context context;
    private final String settingsName = "OraclePermissionGeneratorLocalSettings";
    private final String userIdentifierKey = "userIdentifier";
    private final String remoteDataModelProxyTypeKey = "remoteDataModelProxyTypeKey";
    private final String soapDataServiceLocationKey = "soapDataServiceLocation";
    private final String restDataServiceLocationKey = "restDataServiceLocation";
    
    /**
     * Initialises a new instance of the SharedPreferencesLocalSettingsPersister class.
     * @param  context  The context under which the SharedPreferencesLocalSettingsPersister is being initialized.
     */
    public SharedPreferencesLocalSettingsPersister(Context context) {
        this.context = context;
    }
    
    @Override
    public void Write(LocalSettings localSettings) {
        android.content.SharedPreferences sharedPreferencesLocalSettings = context.getSharedPreferences(settingsName, 0);
        android.content.SharedPreferences.Editor editor = sharedPreferencesLocalSettings.edit();
        editor.putString(userIdentifierKey, localSettings.getUserIdentifier());
        editor.putString(remoteDataModelProxyTypeKey, localSettings.getRemoteDataModelProxyType().name());
        editor.putString(soapDataServiceLocationKey, localSettings.getSoapDataServiceLocation());
        editor.putString(restDataServiceLocationKey, localSettings.getRestDataServiceLocation());
        editor.commit();
    }

    @Override
    public LocalSettings Read() {
        android.content.SharedPreferences sharedPreferencesLocalSettings = context.getSharedPreferences(settingsName, 0);
        LocalSettings returnLocalSettings = new LocalSettings();
        returnLocalSettings.setUserIdentifier(sharedPreferencesLocalSettings.getString(userIdentifierKey, ""));
        returnLocalSettings.setRemoteDataModelProxyType(RemoteDataModelProxyType.valueOf(sharedPreferencesLocalSettings.getString(remoteDataModelProxyTypeKey, RemoteDataModelProxyType.SOAP.name())));
        returnLocalSettings.setSoapDataServiceLocation(sharedPreferencesLocalSettings.getString(soapDataServiceLocationKey, ""));
        returnLocalSettings.setRestDataServiceLocation(sharedPreferencesLocalSettings.getString(restDataServiceLocationKey, ""));
        return returnLocalSettings;
    }
}
