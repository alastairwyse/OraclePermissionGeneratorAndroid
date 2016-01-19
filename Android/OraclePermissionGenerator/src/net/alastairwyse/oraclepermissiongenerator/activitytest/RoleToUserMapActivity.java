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

package net.alastairwyse.oraclepermissiongenerator.activitytest;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import net.alastairwyse.oraclepermissiongenerator.R;
import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * A second version of the RoleToUserMapActivity which does not use the Presenter class, and instead connects directly to the DataInterfaceService class.  Also performs control flow and exception handling within the Activity code.
 * @author Alastair Wyse
 */
public class RoleToUserMapActivity extends BaseActivity {

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
        
        // Connect to the data service and populate the role to user map list
        ExceptionHandlingAsyncTask<Void, Void, ArrayList<RoleToUserMap>> showRoleToUserMapViewTask = new ExceptionHandlingAsyncTask<Void, Void, ArrayList<RoleToUserMap>>(this) {
            @Override
            protected ArrayList<RoleToUserMap> doInBackground(Void... arg0) {
                ArrayList<RoleToUserMap> mappings = null;
                
                try {
                    CheckDataInterfaceServiceConnection();
                    mappings = dataInterface.getMasterRoleToUserMapCollection();
                }
                catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return mappings;
            }
            
            @Override
            protected void onPostExecute(ArrayList<RoleToUserMap> mappings) {
                if (HandleBackgroundException() == false) {
                    roleToUserMapAdapter.clear();
                    for (RoleToUserMap currentRoleToUserMap : mappings) {
                        roleToUserMapAdapter.add(currentRoleToUserMap);
                    }
                    roleToUserMapAdapter.notifyDataSetChanged();
                }
                
                super.onPostExecute(mappings);
            }
        };
        showRoleToUserMapViewTask.execute();
    }

    @Override
    public void onDestroy() {
        if (dataInterfaceServiceConnected == true) {
            unbindService(dataInterfaceServiceConnection);
        }
        super.onDestroy();
    }
    
    public void AddRoleToUserMap(RoleToUserMap mapping) {
        roleToUserMapAdapter.add(mapping);
        roleToUserMapAdapter.notifyDataSetChanged();
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
                // TODO: Need to implement this
                
                // Create an Intent to start the add role to user mapping Activity
                //Intent showAddRoleToUserMapViewIntent = new Intent(this, AddRoleToUserMapActivity.class);
                //startActivity(showAddRoleToUserMapViewIntent);
                return true;
            case R.id.role_to_user_map_activity_context_menu_remove_mapping:
                // Get the role and user that was selected, and pass it to the presenter's RemoveRoleToUserMap() method
                android.widget.AdapterView.AdapterContextMenuInfo menuInfo = (android.widget.AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                LinearLayout selectedRowLayout = (LinearLayout)menuInfo.targetView;
                String role = ((TextView)selectedRowLayout.findViewById(R.id.role_to_user_map_list_row_role_field)).getText().toString();
                String user = ((TextView)selectedRowLayout.findViewById(R.id.role_to_user_map_list_row_user_field)).getText().toString();
                RemoveRoleToUserMap(role, user);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    /**
     * Removes a role to user mapping from the data interface layer and this Activity
     * @param  role  The name of the role to remove the mapping for.
     * @param  user  The name of the user to remove the mapping for.
     */
    private void RemoveRoleToUserMap(String role, String user) {
        ExceptionHandlingAsyncTask<String, Void, ValidationResultContainer<String[]>> removeRoleToUserMapTask = new ExceptionHandlingAsyncTask<String, Void, ValidationResultContainer<String[]>>(this) {
            @Override
            protected ValidationResultContainer<String[]> doInBackground(String... parameters) {
                ValidationResult validationResult = null;
                try {
                    CheckDataInterfaceServiceConnection();
                    ArrayList<String> referencingObjects = dataInterface.RoleGetReferencingObjects(parameters[0]);
                    ArrayList<RoleToUserMap> masterRoleToUserMappings = dataInterface.getMasterRoleToUserMapCollection();
                    // Find the number of role to user mappings for the specified role
                    int numMappings = 0;
                    for (RoleToUserMap currentRoleToUserMap : masterRoleToUserMappings) {
                        if (currentRoleToUserMap.getRole().equals(parameters[0])) {
                            numMappings++;
                        }
                    }
                    
                    // If there are objects referencing the role to user map, and it is the last map for that role, then show an error
                    if ((referencingObjects.size() != 0) && (numMappings == 1)) {
                        // Build an error message to display, listing all the objects referencing the role to user mapping
                        StringBuilder errorMessage = new StringBuilder();
                        errorMessage.append("The role to user mapping is referenced by the following objects.  Please remove the references or objects and try again.\n");
                        for (String currentReferencingObject : referencingObjects) {
                            errorMessage.append("  " + currentReferencingObject + "\n");
                        }

                        validationResult = new ValidationResult(false, errorMessage.toString());
                    }
                    else {
                        validationResult = new ValidationResult(true, "");
                        dataInterface.RemoveRoleToUserMap(parameters[0], parameters[1]);
                    }
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return new ValidationResultContainer<String[]>(validationResult, new String[] { parameters[0], parameters[1] });
            }
            
            @Override
            protected void onPostExecute(ValidationResultContainer<String[]> validationResultContainer) {
                if (HandleBackgroundException() == false) {
                    if (validationResultContainer.ValidationResult.getIsValid() == true) {
                        // Remove the mapping from the role to user map view
                        for (int i = 0; i < roleToUserMapData.size(); i++) {
                            if (validationResultContainer.PostExecuteParameter[0].equals(roleToUserMapData.get(i).getRole()) && validationResultContainer.PostExecuteParameter[1].equals(roleToUserMapData.get(i).getUser())) {
                                roleToUserMapAdapter.remove(roleToUserMapData.get(i));
                                roleToUserMapAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                    else {
                        // Show an error dialog with the results of the validation
                        
                        // Create a class to define the action to take when the user clicks the 'OK' button on the alert dialog
                        OnClickListener alertDialogConfirmationAction = new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Close the alert dialog
                                notificationDialogDisplayView.CloseOkDialog();
                            }
                        };
                        notificationDialogDisplayView.ShowOkDialog("Error", validationResultContainer.ValidationResult.getValidationError(), alertDialogConfirmationAction);
                    }
                }
                
                super.onPostExecute(validationResultContainer);
            }
        };
        removeRoleToUserMapTask.execute(role, user);
    }
    
    /**
     * Container object which holds a validation result, and a parameter which should be used after evaluating the validation result.  Designed to be passed between the doInBackground() and onPostExecute() methods of the ExceptionHandlingAsyncTask class, to allow the onPostExecute() method to use the parameter or not depending on whether the validation was successful.
     *
     * @param <T> The type of the parameter to use after evaluating the validation result.
     */
    private class ValidationResultContainer<T> {
        
        public ValidationResult ValidationResult;
        public T PostExecuteParameter;
        
        public ValidationResultContainer(ValidationResult validationResult, T postExecuteParameter) {
            ValidationResult = validationResult;
            PostExecuteParameter = postExecuteParameter;
        }
    }
}
