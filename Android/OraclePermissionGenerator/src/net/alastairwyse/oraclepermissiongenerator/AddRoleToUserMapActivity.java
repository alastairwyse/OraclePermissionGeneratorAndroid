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
import android.widget.EditText;

/**
 * An activity which allows adding a mapping between an Oracle role and user to the application. 
 * @author Alastair Wyse
 */
public class AddRoleToUserMapActivity extends BaseActivity implements IAddRoleToUserMapView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_role_to_user_map_activity);  
        
        presenter = PresenterGlobalStorer.getPresenter();
        presenter.setAddRoleToUserMapView(this);
    }

    @Override
    public void onBackPressed() {
        presenter.setAddRoleToUserMapView(null);
        super.onBackPressed();
    }
    
    @Override
    public void onDestroy() {
        presenter.setAddRoleToUserMapView(null);
        super.onDestroy();
    }
    
    /**
     * Method called when the 'Add Mapping' button is clicked.
     * @param  view  The widget that was clicked.
     */
    public void AddMappingButtonOnClickListener(View view) {
        // Get the contents of the fields in the activity
        String role = ((EditText)findViewById(R.id.add_role_to_user_map_role_field)).getText().toString().trim();
        String user = ((EditText)findViewById(R.id.add_role_to_user_map_user_field)).getText().toString().trim();
        
        presenter.AddRoleToUserMap(role, user);
    }
}
