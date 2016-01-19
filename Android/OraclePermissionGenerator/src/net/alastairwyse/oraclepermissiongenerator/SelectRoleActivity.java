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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * An activity which displays a list of roles and allows the user to select one. 
 * @author Alastair Wyse
 */
public class SelectRoleActivity extends BaseActivity implements ISelectRoleView {

    private ListView roleList;
    private ArrayList<String> roleListData;
    private StringArrayAdapter roleListAdapter;
    // The object for which the role is being selected
    private String objectName;
    // The object type for which the role is being selected
    private String objectType;
    
    @Override
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    
    @Override
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_role_activity);  

        // Setup views within the activity
        roleList = (ListView)findViewById(R.id.select_role_list);
        roleListData = new ArrayList<String>();
        roleListAdapter = new StringArrayAdapter(this, R.layout.select_role_row, R.id.select_role_row_role_name_field, roleListData);
        roleList.setAdapter(roleListAdapter);
        roleList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView roleNameField = (TextView)view.findViewById(R.id.select_role_row_role_name_field);
                presenter.ShowSetPermissionsView(objectName, objectType, roleNameField.getText().toString());
            }
        });
        
        presenter = PresenterGlobalStorer.getPresenter();
        presenter.setSelectRoleView(this);
    }
    
    @Override
    public void onBackPressed() {
        presenter.setSelectRoleView(null);
        super.onBackPressed();
    }
    
    @Override
    public void onDestroy() {
        presenter.setSelectRoleView(null);
        super.onDestroy();
    }

    @Override
    public void PopulateRoles(ArrayList<String> roles) {
        for (String currentRole : roles) {
            roleListAdapter.add(currentRole);
        }
        roleListAdapter.notifyDataSetChanged();
    }
}
