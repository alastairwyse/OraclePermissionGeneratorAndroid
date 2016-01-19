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

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.alastairwyse.oraclepermissiongenerator.containers.RemoteDataModelProxyType;

/**
 * An activity which displays and allows modifying of the connection settings of the application. 
 * @author Alastair Wyse
 */
public class ConnectionSettingsActivity extends BaseActivity implements IConnectionSettingsView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_settings_activity);  

        presenter = PresenterGlobalStorer.getPresenter();
        presenter.setConnectionSettingsView(this);
    }
    
    @Override
    public void onBackPressed() {
        presenter.setConnectionSettingsView(null);
        super.onBackPressed();
    }
    
    @Override
    public void onDestroy() {
        presenter.setConnectionSettingsView(null);
        super.onDestroy();
    }
    
    @Override
    public void PopulateUserIdentifier(String userIdentifier) {
        EditText userIdentifierField = (EditText)findViewById(R.id.connection_settings_user_identifier_field);
        userIdentifierField.setText(userIdentifier);
    }

    @Override
    public void PopulateRemoteDataModelProxyType(RemoteDataModelProxyType remoteDataModelProxyType) {
        int selectedItemIndex = 0;
        Spinner remoteDataModelProxyTypeField = (Spinner)findViewById(R.id.connection_settings_remote_data_model_type_field);
        ArrayAdapter<CharSequence> remoteDataModelProxyTypeFieldArrayAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        int count = 0;
        for (RemoteDataModelProxyType currentRemoteDataModelProxyType : RemoteDataModelProxyType.values()) {
            remoteDataModelProxyTypeFieldArrayAdapter.add(currentRemoteDataModelProxyType.name());
            if (remoteDataModelProxyType == currentRemoteDataModelProxyType) {
                selectedItemIndex = count;
            }
            count++;
        }
        remoteDataModelProxyTypeFieldArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remoteDataModelProxyTypeField.setAdapter(remoteDataModelProxyTypeFieldArrayAdapter);
        remoteDataModelProxyTypeField.setSelection(selectedItemIndex);
    }
    
    @Override
    public void PopulateSoapDataServiceLocation(String soapDataServiceLocation) {
        EditText soapDataServiceLocationField = (EditText)findViewById(R.id.connection_settings_soap_data_service_location_field);
        soapDataServiceLocationField.setText(soapDataServiceLocation);
    }
    
    @Override
    public void PopulateRestDataServiceLocation(String restDataServiceLocation) {
        EditText restDataServiceLocationField = (EditText)findViewById(R.id.connection_settings_rest_data_service_location_field);
        restDataServiceLocationField.setText(restDataServiceLocation);
    }

    /**
     * Method called when the 'Save' button is clicked.
     * @param  view  The widget that was clicked.
     */
    public void SaveButtonOnClickListener(View view) {
        String userIdentifier = ((EditText)findViewById(R.id.connection_settings_user_identifier_field)).getText().toString().trim();
        String remoteDataModelProxyTypeString = ((Spinner)findViewById(R.id.connection_settings_remote_data_model_type_field)).getSelectedItem().toString();
        RemoteDataModelProxyType remoteDataModelProxyType = RemoteDataModelProxyType.valueOf(remoteDataModelProxyTypeString);
        String soapDataServiceLocation = ((EditText)findViewById(R.id.connection_settings_soap_data_service_location_field)).getText().toString().trim();
        String restDataServiceLocation = ((EditText)findViewById(R.id.connection_settings_rest_data_service_location_field)).getText().toString().trim();
        presenter.SaveConnectionSettings(userIdentifier, remoteDataModelProxyType, soapDataServiceLocation, restDataServiceLocation);
    }
}
