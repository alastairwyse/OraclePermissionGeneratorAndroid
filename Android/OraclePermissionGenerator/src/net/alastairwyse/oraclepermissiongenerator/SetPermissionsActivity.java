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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * An activity which displays a list of permissions for an object and role, and allows the user to set the permissions.
 * @author Alastair Wyse
 */
public class SetPermissionsActivity extends BaseActivity implements ISetPermissionsView {

    private ListView permissionList;
    private ArrayList<MultipleChoiceListItem> permissionListData;
    private MultipleChoiceListItemArrayAdapter permissionListAdapter;
    // The object for which the permissions are being set
    private String objectName;
    // The role for which the permissions are being set
    private String role;

    @Override
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_permissions_activity);  

        // Setup views within the activity
        permissionList = (ListView)findViewById(R.id.set_permissions_list);
        permissionListData = new ArrayList<MultipleChoiceListItem>();
        permissionListAdapter = new MultipleChoiceListItemArrayAdapter(this, R.layout.set_permissions_row, R.id.set_permissions_row_permission_name_field, R.id.set_permissions_row_set_checkbox, permissionListData);
        permissionList.setAdapter(permissionListAdapter);
        
        presenter = PresenterGlobalStorer.getPresenter();
        presenter.setSetPermissionsView(this);
    }

    @Override
    public void onBackPressed() {
        presenter.setSetPermissionsView(null);
        super.onBackPressed();
    }
    
    @Override
    public void onDestroy() {
        presenter.setSetPermissionsView(null);
        super.onDestroy();
    }
    
    @Override
    public void PopulatePermissions(ArrayList<String> objectPermission, ArrayList<String> allPermissions) {
        for (String currentPermssion : allPermissions) {
            if (objectPermission.contains(currentPermssion)) {
                permissionListData.add(new MultipleChoiceListItem(currentPermssion, true));
            }
            else {
                permissionListData.add(new MultipleChoiceListItem(currentPermssion, false));
            }
        }
        permissionListAdapter.notifyDataSetChanged();
    }
    
    /**
     * Event handler for when the CheckBox in a row of the permission list is clicked.
     * @param  view  The CheckBox.
     */
    public void onPermissionListRowCheckBoxClicked(View view) {
        boolean setValue = ((CheckBox) view).isChecked();
        // Get the value in the TextView of the permission list row this checkbox belongs to (i.e. the name of the permission)
        RelativeLayout parentLayout = (RelativeLayout)view.getParent();
        TextView permissionListRowPermissionNameField = (TextView)parentLayout.findViewById(R.id.set_permissions_row_permission_name_field);
        presenter.SetPermssion(objectName, role, permissionListRowPermissionNameField.getText().toString(), setValue);
    }
}
