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

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * An activity which allows adding a new Oracle object to the application.
 * @author Alastair Wyse
 */
public class AddObjectActivity extends BaseActivity implements IAddObjectView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_object_activity);  
        
        presenter = PresenterGlobalStorer.getPresenter();
        presenter.setAddObjectView(this);
    }
    
    @Override
    public void onBackPressed() {
        presenter.setAddObjectView(null);
        super.onBackPressed();
    }
    
    @Override
    public void onDestroy() {
        presenter.setAddObjectView(null);
        super.onDestroy();
    }
    
    @Override
    public void PopulateObjectTypes(ArrayList<String> objectTypes) {
        Spinner objectTypeField = (Spinner)findViewById(R.id.add_object_object_type_field);
        ArrayAdapter<CharSequence> objectTypesFieldArrayAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        for (String currentObjectType : objectTypes) {
            objectTypesFieldArrayAdapter.add(currentObjectType);
        }
        objectTypesFieldArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectTypeField.setAdapter(objectTypesFieldArrayAdapter);
    }
    
    @Override
    public void PopulateObjectOwner(String objectOwner) {
        ((TextView)findViewById(R.id.add_object_object_owner_field)).setText(objectOwner);
    }

    /**
     * Method called when the 'Add Object' button is clicked.
     * @param  view  The widget that was clicked.
     */
    public void AddObjectButtonOnClickListener(View view) {
        // Get the contents of the fields in the activity
        String objectName = ((EditText)findViewById(R.id.add_object_object_name_field)).getText().toString().trim();
        String objectType = ((Spinner)findViewById(R.id.add_object_object_type_field)).getSelectedItem().toString();
        String objectOwner = ((EditText)findViewById(R.id.add_object_object_owner_field)).getText().toString().trim();
        
        presenter.AddObject(objectName, objectType, objectOwner, true, false);
    }
}
