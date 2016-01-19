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
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * An activity which displays a list of Oracle database role to permission mappings, and allows the user to modify them.
 * @author Alastair Wyse
 */
public class RoleToUserMapActivity extends BaseActivity implements IRoleToUserMapView {

    private ListView roleToUserMapList;
    private ArrayList<RoleToUserMap> roleToUserMapData;
    private RoleToUserMapArrayAdapter roleToUserMapAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_to_user_map_activity);  

        roleToUserMapList = (ListView)findViewById(R.id.role_to_user_map_list);
        roleToUserMapData = new ArrayList<RoleToUserMap>();
        roleToUserMapAdapter = new RoleToUserMapArrayAdapter(this, R.layout.role_to_user_map_row, R.id.role_to_user_map_list_row_role_field, R.id.role_to_user_map_list_row_user_field, roleToUserMapData);
        roleToUserMapList.setAdapter(roleToUserMapAdapter);
        registerForContextMenu(findViewById(R.id.role_to_user_map_fill_space));
        registerForContextMenu(roleToUserMapList);
        
        presenter = PresenterGlobalStorer.getPresenter();
        presenter.setRoleToUserMapView(this);
    }

    @Override
    public void onBackPressed() {
        presenter.setRoleToUserMapView(null);
        super.onBackPressed();
    }
    
    @Override
    public void onDestroy() {
        presenter.setRoleToUserMapView(null);
        super.onDestroy();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()) {
            case R.id.role_to_user_map_list:
                getMenuInflater().inflate(R.menu.role_to_user_map_acitivity_context_menu_add_remove, menu);
                return;
            // Default case should be triggered if view is R.id.oracle_object_list_fill_space
            default:
                getMenuInflater().inflate(R.menu.role_to_user_map_acitivity_context_menu_add, menu);
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.role_to_user_map_activity_context_menu_add_mapping:
                presenter.ShowAddRoleToUserMapView();
                return true;
            case R.id.role_to_user_map_activity_context_menu_remove_mapping:
                // Get the role and user that was selected, and pass it to the presenter's RemoveRoleToUserMap() method
                android.widget.AdapterView.AdapterContextMenuInfo menuInfo = (android.widget.AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                LinearLayout selectedRowLayout = (LinearLayout)menuInfo.targetView;
                TextView roleField = (TextView) selectedRowLayout.findViewById(R.id.role_to_user_map_list_row_role_field);
                TextView userField = (TextView) selectedRowLayout.findViewById(R.id.role_to_user_map_list_row_user_field);
                presenter.RemoveRoleToUserMap(roleField.getText().toString(), userField.getText().toString());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    @Override
    public void PopulateRoleToUserMappings(ArrayList<RoleToUserMap> mappings) {
        roleToUserMapAdapter.clear();
        for (RoleToUserMap currentRoleToUserMap : mappings) {
            roleToUserMapAdapter.add(currentRoleToUserMap);
        }
        roleToUserMapAdapter.notifyDataSetChanged();
    }

    @Override
    public void AddRoleToUserMap(RoleToUserMap mapping) {
        roleToUserMapAdapter.add(mapping);
        roleToUserMapAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void RemoveRoleToUserMap(RoleToUserMap mapping) {
        RoleToUserMap itemToRemove = null;
        for (int i = 0; i < roleToUserMapData.size(); i++) {
            if (mapping.getRole().equals(roleToUserMapData.get(i).getRole()) && mapping.getUser().equals(roleToUserMapData.get(i).getUser())) {
                itemToRemove = roleToUserMapData.get(i);
            }
        }
        roleToUserMapAdapter.remove(itemToRemove);
        roleToUserMapAdapter.notifyDataSetChanged();
    }
}
