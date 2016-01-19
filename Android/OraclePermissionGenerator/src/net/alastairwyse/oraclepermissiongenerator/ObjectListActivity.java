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

import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * The main activity in the Oracle Permission Generator application which shows a list of all Oracle objects.
 * @author Alastair Wyse
 */
public class ObjectListActivity extends BaseActivity implements IObjectListView {

    // TODO: Code to get the object name from the relevant textview after click is repeated in at least 3 methods (onObjectListRowAddCheckBoxClicked(), onObjectListRowRemoveCheckBoxClicked(), and Definition of OnClickListener)
    //       Consider putting into a private method

    private ListView oracleObjectList;
    private ArrayList<OracleObjectPermissionSet> oracleObjectListData;
    private OracleObjectArrayAdapter oracleObjectListAdapter;
    private OnClickListener oracleObjectListClickListener;
    private boolean showRoleToUserMapTestActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list_activity);   
        
        // Create a click handler for the Oracle object list ListView
        oracleObjectListClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the value in the object name TextView of the object list row this view belongs to (i.e. the name of the Oracle object)
                RelativeLayout parentLayout = (RelativeLayout)view.getParent();
                TextView objectListRowObjectNameField = (TextView)parentLayout.findViewById(R.id.object_list_row_object_name_field);
                TextView objectListRowObjectTypeField = (TextView)parentLayout.findViewById(R.id.object_list_row_object_type_field);
                presenter.ShowSelectRoleView(objectListRowObjectNameField.getText().toString(), objectListRowObjectTypeField.getText().toString());
            }
        };

        // Setup views within the activity
        oracleObjectList = (ListView)findViewById(R.id.oracle_object_list);
        oracleObjectListData = new ArrayList<OracleObjectPermissionSet>();
        oracleObjectListAdapter = new OracleObjectArrayAdapter(this, R.layout.object_list_row, R.id.object_list_row_object_name_field, R.id.object_list_row_object_type_field, R.id.object_list_row_add_checkbox, R.id.object_list_row_remove_checkbox, oracleObjectListData, oracleObjectListClickListener);
        oracleObjectList.setAdapter(oracleObjectListAdapter);
        registerForContextMenu(findViewById(R.id.oracle_object_list_fill_space));
        registerForContextMenu(oracleObjectList);

        // If the PresenterGlobalStorer already contains the presenter then set this presenter on this activity (e.g. if the activity is being recreated due to an orientation change)
        if (PresenterGlobalStorer.getPresenter() != null) {
            presenter = PresenterGlobalStorer.getPresenter();
            presenter.setObjectListView(this);
            presenter.Initialise();
        }
        // Otherwise setup the presenter component and put in the global storer
        else {
            presenter = new Presenter();
            presenter.setObjectListView(this);
            presenter.setExceptionLogger(new ExternalStorageExceptionLogger(this));
            presenter.Initialise();
            PresenterGlobalStorer.setPresenter(presenter);
        }
    }

    @Override
    public void onDestroy() {
        presenter.setObjectListView(null);
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu... this adds items to the action bar if it is present. 
        if (showRoleToUserMapTestActivity == true) {
            getMenuInflater().inflate(R.menu.object_list_activity_test_menu, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.object_list_activity_menu, menu);
        }
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.object_list_activity_menu_settings:
                presenter.ShowSettingsView();
                return true;
            case R.id.object_list_activity_menu_generate_script:
                presenter.ShowSelectScriptView();
                return true;
            case R.id.object_list_activity_menu_refresh:
                presenter.Initialise();
                return true;
            case R.id.object_list_activity_menu_role_to_user_mappings:
                presenter.ShowRoleToUserMapView();
                return true;
            case R.id.object_list_activity_menu_connection_settings:
                presenter.ShowConnectionSettingsView();
                return true;
            case R.id.object_list_activity_menu_role_to_user_mappings_test:
                Intent showRoleToUserMapTestActivityIntent = new Intent(this, net.alastairwyse.oraclepermissiongenerator.activitytest.RoleToUserMapActivity.class);
                startActivity(showRoleToUserMapTestActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()) {
            case R.id.oracle_object_list:
                getMenuInflater().inflate(R.menu.object_list_activity_context_menu_add_remove, menu);
                return;
            // Default case should be triggered if view is R.id.oracle_object_list_fill_space
            default:
                getMenuInflater().inflate(R.menu.object_list_activity_context_menu_add, menu);
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.object_list_activity_context_menu_add_object:
                presenter.ShowAddObjectView();
                return true;
            case R.id.object_list_activity_context_menu_remove_object:
                // Get the text of the object name field of the object row that was selected, and pass it to the presenter's RemoveObject() method
                android.widget.AdapterView.AdapterContextMenuInfo menuInfo = (android.widget.AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                RelativeLayout selectedRowLayout = (RelativeLayout)menuInfo.targetView;
                TextView objectNameField = (TextView) selectedRowLayout.findViewById(R.id.object_list_row_object_name_field);
                presenter.RemoveObject(objectNameField.getText().toString());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    
    @Override
    public void PopulateObjects(ArrayList<OracleObjectPermissionSet> oracleObjects) {
        oracleObjectListAdapter.clear();
        for (OracleObjectPermissionSet currentObjectPermissionSet : oracleObjects) {
            oracleObjectListAdapter.add(currentObjectPermissionSet);
        }
        oracleObjectListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void AddObject(OracleObjectPermissionSet oracleObject) {
        oracleObjectListAdapter.add(oracleObject);
        oracleObjectListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void RemoveObject(String objectName) {
        OracleObjectPermissionSet objectToRemove = null;
        for (int i = 0; i < oracleObjectListAdapter.getCount(); i++) {
            if (oracleObjectListAdapter.getItem(i).getObjectName().equals(objectName) == true) {
                objectToRemove = oracleObjectListAdapter.getItem(i);
            }
        }
        oracleObjectListAdapter.remove(objectToRemove);
        oracleObjectListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void ClearObjects() {
        oracleObjectListAdapter.clear();
        oracleObjectListAdapter.notifyDataSetChanged();
    }
    
    /**
     * Event handler for when the 'Add' CheckBox in a row of the Oracle object list is clicked.
     * @param  view  The CheckBox.
     */
    public void onObjectListRowAddCheckBoxClicked(View view) {
        boolean addFlagValue = ((CheckBox) view).isChecked();
        // Get the value in the TextView of the object list row this CheckBox belongs to (i.e. the name of the Oracle object)
        RelativeLayout parentLayout = (RelativeLayout)view.getParent();
        TextView objectListRowObjectNameField = (TextView)parentLayout.findViewById(R.id.object_list_row_object_name_field);
        presenter.SetAddFlag(objectListRowObjectNameField.getText().toString(), addFlagValue);
    }
    
    /**
     * Event handler for when the 'Remove' CheckBox in a row of the Oracle object list is clicked.
     * @param  view  The CheckBox.
     */
    public void onObjectListRowRemoveCheckBoxClicked(View view) {
        boolean removeFlagValue = ((CheckBox) view).isChecked();
        // Get the value in the TextView of the object list row this CheckBox belongs to (i.e. the name of the Oracle object)
        RelativeLayout parentLayout = (RelativeLayout)view.getParent();
        TextView objectListRowObjectNameField = (TextView)parentLayout.findViewById(R.id.object_list_row_object_name_field);
        presenter.SetRemoveFlag(objectListRowObjectNameField.getText().toString(), removeFlagValue);
    }
}
