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
import java.util.concurrent.CountDownLatch;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.IBinder;

import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.DataInterfaceService.DataInterfaceServiceBinder;

/**
 * The presenter component in the application (following the MVP pattern).  Handles all interaction between the data layer and the views, plus managing tasks on background worker threads, and exception handling.
 * @author Alastair Wyse
 */
public class Presenter implements IPresenter {

    // The time in milliseconds to wait between iterations of spinning loops which wait for activities to open, services to start, etc...
    private final Integer spinInterval = 50;
    
    private IObjectListView objectListView;
    private IAddObjectView addObjectView;
    private ISelectRoleView selectRoleView;
    private ISetPermissionsView setPermissionsView;
    private IRoleToUserMapView roleToUserMapView;
    private IAddRoleToUserMapView addRoleToUserMapView;
    private ISettingsView settingsView;
    private IConnectionSettingsView connectionSettingsView;
    private ISelectScriptView selectScriptView;
    private IDataInterface dataInterface;
    private DataInterfaceServiceConnection dataInterfaceServiceConnection;
    private volatile boolean dataInterfaceServiceConnected = false;
    private IExceptionLogger exceptionLogger;
    private boolean instantiatedWithTestConstructor = false;
    private CountDownLatch backgroundThreadCompleteSignal;
    
    /**
     * Initialises a new instance of the Presenter class.
     */
    public Presenter() {
        dataInterfaceServiceConnection = new DataInterfaceServiceConnection();
    }
    
    /**
     * Initialises a new instance of the Presenter class.
     * <b>Note</b> this is an additional constructor to facilitate unit tests, and should not be used to instantiate the class under normal conditions.
     * @param  dataInterface                   A test (mock) data interface.
     * @param  exceptionLogger                 A test (mock) exception logger.
     * @param  backgroundThreadCompleteSignal  Notifies test code that a background worker thread process has completed (so that assertions can subsequently be made in units tests).
     */
    public Presenter(IDataInterface dataInterface, IExceptionLogger exceptionLogger, CountDownLatch backgroundThreadCompleteSignal) {
        this.dataInterface = dataInterface;
        this.exceptionLogger = exceptionLogger;
        this.backgroundThreadCompleteSignal = backgroundThreadCompleteSignal;
        instantiatedWithTestConstructor = true;
        dataInterfaceServiceConnected = true;
    }
    
    @Override
    public void setObjectListView(IObjectListView objectListView) {
        this.objectListView = objectListView;
    }
    
    @Override
    public void setAddObjectView(IAddObjectView addObjectView) {
        this.addObjectView = addObjectView;
    }
    
    @Override
    public void setSelectRoleView(ISelectRoleView selectRoleView) {
        this.selectRoleView = selectRoleView;
    }
    
    @Override
    public void setSetPermissionsView(ISetPermissionsView setPermissionsView) {
        this.setPermissionsView = setPermissionsView;
    }
    
    @Override
    public void setRoleToUserMapView(IRoleToUserMapView roleToUserMapView) {
        this.roleToUserMapView = roleToUserMapView;
    }
    
    @Override
    public void setAddRoleToUserMapView(IAddRoleToUserMapView addRoleToUserMapView) {
        this.addRoleToUserMapView = addRoleToUserMapView;
    }
    
    @Override
    public void setSettingsView(ISettingsView settingsView) {
        this.settingsView = settingsView;
    }
    
    @Override
    public void setConnectionSettingsView(IConnectionSettingsView connectionSettingsView) {
        this.connectionSettingsView = connectionSettingsView;
    }
    
    @Override
    public void setSelectScriptView(ISelectScriptView selectScriptView) {
        this.selectScriptView = selectScriptView;
    }
    
    @Override
    public void setExceptionLogger(IExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    @Override
    public void Initialise() {
        ExceptionHandlingAsyncTask<Void, Void, ArrayList<OracleObjectPermissionSet>> initialiseTask = new ExceptionHandlingAsyncTask<Void, Void, ArrayList<OracleObjectPermissionSet>>(objectListView) {
            @Override
            protected ArrayList<OracleObjectPermissionSet> doInBackground(Void... arg0) {
                // Get all objects from the data interface
                ArrayList<OracleObjectPermissionSet> returnedObjects = new ArrayList<OracleObjectPermissionSet>();

                try {
                    CheckDataInterfaceServiceConnection();
                    returnedObjects = dataInterface.getObjects();
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                    
                return returnedObjects;
            }
            
            @Override
            protected void onPostExecute(ArrayList<OracleObjectPermissionSet> objectPermissionSets) {
                if (HandleBackgroundException() == false) {
                    // Populate all objects into the object list view
                    objectListView.ClearObjects();
                    objectListView.PopulateObjects(objectPermissionSets);
                }
                
                super.onPostExecute(objectPermissionSets);
            }
        };
        initialiseTask.execute();
    }
    
    @Override
    public void AddObject(String objectName, String objectType, String objectOwner, boolean addFlag, boolean removeFlag) {
        OracleObjectPermissionSet newObjectPermissionSet = new OracleObjectPermissionSet(objectName);
        newObjectPermissionSet.setObjectType(objectType);
        newObjectPermissionSet.setObjectOwner(objectOwner);
        newObjectPermissionSet.setAddFlag(addFlag);
        newObjectPermissionSet.setRemoveFlag(removeFlag);
        
        ExceptionHandlingAsyncTask<OracleObjectPermissionSet, Void, ValidationResultContainer<OracleObjectPermissionSet>> addObjectTask = new ExceptionHandlingAsyncTask<OracleObjectPermissionSet, Void, ValidationResultContainer<OracleObjectPermissionSet>>(addObjectView) {
            @Override
            protected ValidationResultContainer<OracleObjectPermissionSet> doInBackground(OracleObjectPermissionSet... parameters) {
                ValidationResult validationResult = null;
                try {
                    CheckDataInterfaceServiceConnection();
                    validationResult = dataInterface.ObjectNameValidate(parameters[0].getObjectName());
                    if (validationResult.getIsValid() == false) {
                        return new ValidationResultContainer<OracleObjectPermissionSet>(validationResult, parameters[0]);
                    }
                    validationResult = dataInterface.ObjectTypeValidate(parameters[0].getObjectType());
                    if (validationResult.getIsValid() == false) {
                        return new ValidationResultContainer<OracleObjectPermissionSet>(validationResult, parameters[0]);
                    }
                    validationResult = dataInterface.ObjectOwnerValidate(parameters[0].getObjectOwner());
                    if (validationResult.getIsValid() == false) {
                        return new ValidationResultContainer<OracleObjectPermissionSet>(validationResult, parameters[0]);
                    }
                    
                    dataInterface.AddObjectPermissionSet(parameters[0].getObjectName(), parameters[0].getObjectType(), parameters[0].getObjectOwner(), parameters[0].getAddFlag(), parameters[0].getRemoveFlag(), parameters[0].getObjectPermissions());
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return new ValidationResultContainer<OracleObjectPermissionSet>(validationResult, parameters[0]);
            }

            @Override
            protected void onPostExecute(ValidationResultContainer<OracleObjectPermissionSet> validationResultContainer) {
                if (HandleBackgroundException() == false) {
                    
                    if (validationResultContainer.ValidationResult.getIsValid() == true) {
                        // Add the object to the object list view
                        objectListView.AddObject(validationResultContainer.PostExecuteParameter);
                        notificationDialogDisplayView.Close();
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
        addObjectTask.execute(newObjectPermissionSet);
    }
    
    @Override
    public void RemoveObject(String objectName) {
        ExceptionHandlingAsyncTask<String, Void, String> removeObjectTask = new ExceptionHandlingAsyncTask<String, Void, String>(objectListView) {
            @Override
            protected String doInBackground(String... parameters) {
                try {
                    CheckDataInterfaceServiceConnection();
                    dataInterface.RemoveObjectPermissionSet(parameters[0]);
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return parameters[0];
            }

            @Override
            protected void onPostExecute(String objectName) {
                if (HandleBackgroundException() == false) {
                    // Remove the object from the object list view
                    objectListView.RemoveObject(objectName);
                }
                
                super.onPostExecute(objectName);
            }
        };
        removeObjectTask.execute(objectName);
    }
    
    @Override
    public void SetAddFlag(String objectName, boolean addFlagValue) {
        ExceptionHandlingAsyncTask<OracleObjectFlagParameters, Void, Void> setAddFlagTask = new ExceptionHandlingAsyncTask<OracleObjectFlagParameters, Void, Void>(objectListView) {
            @Override
            protected Void doInBackground(OracleObjectFlagParameters... parameters) {
                try {
                    CheckDataInterfaceServiceConnection();
                    dataInterface.SetAddFlag(parameters[0].ObjectName, parameters[0].FlagValue);
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                HandleBackgroundException();
                super.onPostExecute(result);
            }
        };
        setAddFlagTask.execute(new OracleObjectFlagParameters(objectName, addFlagValue));
    }
    
    @Override
    public void SetRemoveFlag(String objectName, boolean removeFlagValue) {
        ExceptionHandlingAsyncTask<OracleObjectFlagParameters, Void, Void> setRemoveFlagTask = new ExceptionHandlingAsyncTask<OracleObjectFlagParameters, Void, Void>(objectListView) {
            @Override
            protected Void doInBackground(OracleObjectFlagParameters... parameters) {
                try {
                    CheckDataInterfaceServiceConnection();
                    dataInterface.SetRemoveFlag(parameters[0].ObjectName, parameters[0].FlagValue);
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                HandleBackgroundException();
                super.onPostExecute(result);
            }
        };
        setRemoveFlagTask.execute(new OracleObjectFlagParameters(objectName, removeFlagValue));
    }
    
    @Override
    public void SetPermssion(String objectName, String role, String permission, boolean value) {
        ExceptionHandlingAsyncTask<SetPermissionParameters, Void, Void> setPermssionTask = new ExceptionHandlingAsyncTask<SetPermissionParameters, Void, Void>(setPermissionsView) {
            @Override
            protected Void doInBackground(SetPermissionParameters... parameters) {
                try {
                    CheckDataInterfaceServiceConnection();
                    if (parameters[0].Value == true) {
                        dataInterface.AddPermission(parameters[0].ObjectName, parameters[0].Role, parameters[0].Permission);
                    }
                    else {
                        dataInterface.RemovePermission(parameters[0].ObjectName, parameters[0].Role, parameters[0].Permission);
                    }
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                HandleBackgroundException();
                super.onPostExecute(result);
            }
        };
        setPermssionTask.execute(new SetPermissionParameters(objectName, role, permission, value));
    }
    
    @Override
    public void AddRoleToUserMap(String role, String user) {
        ExceptionHandlingAsyncTask<String, Void, ValidationResultContainer<String[]>> addRoleToUserMapTask = new ExceptionHandlingAsyncTask<String, Void, ValidationResultContainer<String[]>>(addRoleToUserMapView) {
            @Override
            protected ValidationResultContainer<String[]> doInBackground(String... parameters) {
                ValidationResult validationResult = null;
                try {
                    CheckDataInterfaceServiceConnection();
                    validationResult = dataInterface.RoleToUserMapValidate(parameters[0], parameters[1]);
                    if (validationResult.getIsValid() == false) {
                        return new ValidationResultContainer<String[]>(validationResult, new String[] { parameters[0], parameters[1] });
                    }

                    dataInterface.AddRoleToUserMap(parameters[0], parameters[1]);
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return new ValidationResultContainer<String[]>(validationResult, new String[] { parameters[0], parameters[1] });
            }
            
            @Override
            protected void onPostExecute(ValidationResultContainer<String[]> validationResultContainer) {
                if (HandleBackgroundException() == false) {
                    
                    if (validationResultContainer.ValidationResult.getIsValid() == true) {
                        // Add the mapping to the role to user map view
                        RoleToUserMap newRoleToUserMap = new RoleToUserMap(validationResultContainer.PostExecuteParameter[0], validationResultContainer.PostExecuteParameter[1]);
                        roleToUserMapView.AddRoleToUserMap(newRoleToUserMap);
                        notificationDialogDisplayView.Close();
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
        addRoleToUserMapTask.execute(role, user);
    }
    
    @Override
    public void RemoveRoleToUserMap(String role, String user) {
        ExceptionHandlingAsyncTask<String, Void, ValidationResultContainer<String[]>> removeRoleToUserMapTask = new ExceptionHandlingAsyncTask<String, Void, ValidationResultContainer<String[]>>(roleToUserMapView) {
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
                        RoleToUserMap roleToUserMap = new RoleToUserMap(validationResultContainer.PostExecuteParameter[0], validationResultContainer.PostExecuteParameter[1]);
                        roleToUserMapView.RemoveRoleToUserMap(roleToUserMap);
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
    
    @Override
    public void SaveSettings(String defaultObjectOwner) {
        ExceptionHandlingAsyncTask<String, Void, ValidationResultContainer<String>> saveSettingsTask = new ExceptionHandlingAsyncTask<String, Void, ValidationResultContainer<String>>(settingsView) {
            @Override
            protected ValidationResultContainer<String> doInBackground(String... parameters) {
                ValidationResult validationResult = null;
                try {
                    CheckDataInterfaceServiceConnection();
                    validationResult = dataInterface.ObjectOwnerValidate(parameters[0]);
                    if (validationResult.getIsValid() == false) {
                        return new ValidationResultContainer<String>(validationResult, parameters[0]);
                    }

                    dataInterface.setDefaultObjectOwner(parameters[0]);
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return new ValidationResultContainer<String>(validationResult, parameters[0]);
            }
            
            @Override
            protected void onPostExecute(ValidationResultContainer<String> validationResultContainer) {
                if (HandleBackgroundException() == false) {
                    
                    if (validationResultContainer.ValidationResult.getIsValid() == true) {
                        notificationDialogDisplayView.Close();
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
        saveSettingsTask.execute(defaultObjectOwner);
    }
    
    @Override
    public void SaveConnectionSettings(String userIdentifier, RemoteDataModelProxyType remoteDataModelProxyType, String soapDataServiceLocation, String restDataServiceLocation) {
        ExceptionHandlingAsyncTask<LocalSettings, Void, Void> saveConnectionSettingsTask = new ExceptionHandlingAsyncTask<LocalSettings, Void, Void>(connectionSettingsView) {
            @Override
            protected Void doInBackground(LocalSettings... parameters) {
                try {
                    CheckDataInterfaceServiceConnection();
                    LocalSettings currentLocalSettings = dataInterface.getLocalSettings();
                    // If any of the connection settings have changed, then recreate the RemoteDataModelProxy on the data interface
                    if (parameters[0].getRemoteDataModelProxyType() != currentLocalSettings.getRemoteDataModelProxyType() ||
                        parameters[0].getSoapDataServiceLocation().equals(currentLocalSettings.getSoapDataServiceLocation()) == false ||
                        parameters[0].getRestDataServiceLocation().equals(currentLocalSettings.getRestDataServiceLocation()) == false) {
                        switch (parameters[0].getRemoteDataModelProxyType()) {
                        case SOAP:
                            SetDataServiceRemoteDataModelProxy(RemoteDataModelProxyType.SOAP, parameters[0].getSoapDataServiceLocation());
                            break;
                        case REST:
                            SetDataServiceRemoteDataModelProxy(RemoteDataModelProxyType.REST, parameters[0].getRestDataServiceLocation());
                            break;
                        default:
                            throw new Exception("Unhandled " + RemoteDataModelProxyType.class.getName() + " field '" + parameters[0].getRemoteDataModelProxyType().name() + "' encountered.");
                        }
                    }
                    dataInterface.setLocalSettings(parameters[0]);
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                if (HandleBackgroundException() == false) {
                    notificationDialogDisplayView.Close();
                    // Reload the object list view
                    Initialise();
                }
                super.onPostExecute(result);
            }
        };
        LocalSettings newLocalSettings = new LocalSettings();
        newLocalSettings.setUserIdentifier(userIdentifier);
        newLocalSettings.setRemoteDataModelProxyType(remoteDataModelProxyType);
        newLocalSettings.setSoapDataServiceLocation(soapDataServiceLocation);
        newLocalSettings.setRestDataServiceLocation(restDataServiceLocation);
        saveConnectionSettingsTask.execute(newLocalSettings);
    }
    
    @Override
    public void GeneratePrivilegeScript(ScriptType scriptType, boolean generateRevokeStatements) {
        ExceptionHandlingAsyncTask<GeneratePrivilegeScriptParameters, Void, Void> generatePrivilegeScriptTask = new ExceptionHandlingAsyncTask<GeneratePrivilegeScriptParameters, Void, Void>(selectScriptView) {
            @Override
            protected Void doInBackground(GeneratePrivilegeScriptParameters... parameters) {
                try {
                    CheckDataInterfaceServiceConnection();
                    String scriptText = dataInterface.CreatePrivilegeScript(parameters[0].ScriptType, parameters[0].GenerateRevokeStatements);
                    dataInterface.WriteScript(scriptText);
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                if (HandleBackgroundException() == false) {
                    notificationDialogDisplayView.Close();
                }
                
                super.onPostExecute(result);
            }
        };
        generatePrivilegeScriptTask.execute(new GeneratePrivilegeScriptParameters(scriptType, generateRevokeStatements));
    }
    
    @Override
    public void GenerateSynonymScript(ScriptType scriptType) {
        ExceptionHandlingAsyncTask<ScriptType, Void, Void> generateSynonymScriptTask = new ExceptionHandlingAsyncTask<ScriptType, Void, Void>(selectScriptView) {
            @Override
            protected Void doInBackground(ScriptType... parameters) {
                try {
                    CheckDataInterfaceServiceConnection();
                    String scriptText = dataInterface.CreateSynonymScript(parameters[0]);
                    dataInterface.WriteScript(scriptText);
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                if (HandleBackgroundException() == false) {
                    notificationDialogDisplayView.Close();
                }
                
                super.onPostExecute(result);
            }
        };
        generateSynonymScriptTask.execute(scriptType);
    }
    
    @Override
    public void ShowAddObjectView() {
        ExceptionHandlingAsyncTask<Void, Void, ShowAddObjectViewResults> showAddObjectViewTask = new ExceptionHandlingAsyncTask<Void, Void, ShowAddObjectViewResults>(objectListView) {
            @Override
            protected ShowAddObjectViewResults doInBackground(Void... arg0) {
                ArrayList<String> objectTypes = null;
                String objectOwner = "";
                
                try {
                    CheckDataInterfaceServiceConnection();
                    objectTypes = dataInterface.getObjectTypes();
                    objectOwner = dataInterface.getDefaultObjectOwner();

                    // If code execution is in a unit test do not actually create the intent
                    if (instantiatedWithTestConstructor == false) {
                        Context notificationDialogDisplayViewContext = (Context)notificationDialogDisplayView;
                        Intent showAddObjectViewIntent = new Intent(notificationDialogDisplayViewContext, AddObjectActivity.class);
                        notificationDialogDisplayViewContext.startActivity(showAddObjectViewIntent);
                    }
                    
                    // Wait until the activity is opened.
                    while (addObjectView == null) {
                        Thread.sleep(spinInterval);
                    }
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return new ShowAddObjectViewResults(objectTypes, objectOwner);
            }

            @Override
            protected void onPostExecute(ShowAddObjectViewResults results) {
                if (HandleBackgroundException() == false) {
                    addObjectView.PopulateObjectOwner(results.ObjectOwner);
                    addObjectView.PopulateObjectTypes(results.ObjectTypes);
                }
                
                super.onPostExecute(results);
            }
        };
        showAddObjectViewTask.execute();
    }
    
    @Override
    public void ShowSelectRoleView(String objectName, String objectType) {
        ExceptionHandlingAsyncTask<ShowSelectRoleViewParameters, Void, ArrayList<String>> showSelectRoleViewTask = new ExceptionHandlingAsyncTask<ShowSelectRoleViewParameters, Void, ArrayList<String>>(objectListView) {
            @Override
            protected ArrayList<String> doInBackground(ShowSelectRoleViewParameters... parameters) {
                ArrayList<String> roles = null;
                
                try {
                    CheckDataInterfaceServiceConnection();
                    roles = dataInterface.getRoles();

                    // If code execution is in a unit test do not actually create the intent
                    if (instantiatedWithTestConstructor == false) {
                        Context notificationDialogDisplayViewContext = (Context)notificationDialogDisplayView;
                        Intent showSelectRoleViewIntent = new Intent(notificationDialogDisplayViewContext, SelectRoleActivity.class);
                        notificationDialogDisplayViewContext.startActivity(showSelectRoleViewIntent);
                    }
                    
                    // Wait until the activity is opened.
                    while (selectRoleView == null) {
                        Thread.sleep(spinInterval);
                    }
                    selectRoleView.setObjectName(parameters[0].ObjectName);
                    selectRoleView.setObjectType(parameters[0].ObjectType);
                    
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return roles;
            }
            
            @Override
            protected void onPostExecute(ArrayList<String> roles) {
                if (HandleBackgroundException() == false) {
                    selectRoleView.PopulateRoles(roles);
                }
                
                super.onPostExecute(roles);
            }
        };
        showSelectRoleViewTask.execute(new ShowSelectRoleViewParameters(objectName, objectType));
    }
    
    @Override
    public void ShowSetPermissionsView(String objectName, String objectType, String role) {
        ExceptionHandlingAsyncTask<ShowSetPermissionsViewParameters, Void, ShowSetPermissionsViewResults> showSetPermissionsViewTask = new ExceptionHandlingAsyncTask<ShowSetPermissionsViewParameters, Void, ShowSetPermissionsViewResults>(selectRoleView) {
            @Override
            protected ShowSetPermissionsViewResults doInBackground(ShowSetPermissionsViewParameters... parameters) {
                ArrayList<String> allPermissions = null;
                ArrayList<String> objectPermissions = null;
                
                try {
                    CheckDataInterfaceServiceConnection();
                    allPermissions = dataInterface.getPermissions(parameters[0].ObjectType);
                    objectPermissions = dataInterface.getPermissions(parameters[0].ObjectName, parameters[0].Role);

                    // If code execution is in a unit test do not actually create the intent
                    if (instantiatedWithTestConstructor == false) {
                        Context notificationDialogDisplayViewContext = (Context)notificationDialogDisplayView;
                        Intent showSetPermissionsViewIntent = new Intent(notificationDialogDisplayViewContext, SetPermissionsActivity.class);
                        notificationDialogDisplayViewContext.startActivity(showSetPermissionsViewIntent);
                    }
                    
                    // Wait until the activity is opened.
                    while (setPermissionsView == null) {
                        Thread.sleep(spinInterval);
                    }
                    setPermissionsView.setObjectName(parameters[0].ObjectName);
                    setPermissionsView.setRole(parameters[0].Role);

                } catch (Exception e) {
                    doInBackgroundException = e;
                }

                return new ShowSetPermissionsViewResults(allPermissions, objectPermissions);
            }
            
            @Override
            protected void onPostExecute(ShowSetPermissionsViewResults results) {
                if (HandleBackgroundException() == false) {
                    setPermissionsView.PopulatePermissions(results.ObjectPermissions, results.AllPermissions);
                }
                
                super.onPostExecute(results);
            }
        };
        showSetPermissionsViewTask.execute(new ShowSetPermissionsViewParameters(objectName, objectType, role));
    }
    
    @Override
    public void ShowRoleToUserMapView() {
        ExceptionHandlingAsyncTask<Void, Void, ArrayList<RoleToUserMap>> showRoleToUserMapViewTask = new ExceptionHandlingAsyncTask<Void, Void, ArrayList<RoleToUserMap>>(objectListView) {
            @Override
            protected ArrayList<RoleToUserMap> doInBackground(Void... arg0) {
                ArrayList<RoleToUserMap> mappings = null;
                
                try {
                    CheckDataInterfaceServiceConnection();
                    mappings = dataInterface.getMasterRoleToUserMapCollection();

                    // If code execution is in a unit test do not actually create the intent
                    if (instantiatedWithTestConstructor == false) {
                        Context notificationDialogDisplayViewContext = (Context)notificationDialogDisplayView;
                        Intent showRoleToUserMapViewIntent = new Intent(notificationDialogDisplayViewContext, RoleToUserMapActivity.class);
                        notificationDialogDisplayViewContext.startActivity(showRoleToUserMapViewIntent);
                    }
                    
                    // Wait until the activity is opened.
                    while (roleToUserMapView == null) {
                        Thread.sleep(spinInterval);
                    }
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return mappings;
            }
            
            @Override
            protected void onPostExecute(ArrayList<RoleToUserMap> mappings) {
                if (HandleBackgroundException() == false) {
                    roleToUserMapView.PopulateRoleToUserMappings(mappings);
                }
                
                super.onPostExecute(mappings);
            }
        };
        showRoleToUserMapViewTask.execute();
    }
    
    @Override
    public void ShowAddRoleToUserMapView() {
        Context roleToUserMapViewContext = (Context)roleToUserMapView;
        Intent showAddRoleToUserMapViewIntent = new Intent(roleToUserMapViewContext, AddRoleToUserMapActivity.class);
        roleToUserMapViewContext.startActivity(showAddRoleToUserMapViewIntent);
    }
    
    @Override
    public void ShowSettingsView() {
        ExceptionHandlingAsyncTask<Void, Void, String> showSettingsViewTask = new ExceptionHandlingAsyncTask<Void, Void, String>(objectListView) {
            @Override
            protected String doInBackground(Void... arg0) {
                String defaultObjectOwner = null;
                
                try {
                    CheckDataInterfaceServiceConnection();
                    defaultObjectOwner = dataInterface.getDefaultObjectOwner();

                    // If code execution is in a unit test do not actually create the intent
                    if (instantiatedWithTestConstructor == false) {
                        Context notificationDialogDisplayViewContext = (Context)notificationDialogDisplayView;
                        Intent showSettingsViewIntent = new Intent(notificationDialogDisplayViewContext, SettingsActivity.class);
                        notificationDialogDisplayViewContext.startActivity(showSettingsViewIntent);
                    }
                    
                    // Wait until the activity is opened.
                    while (settingsView == null) {
                        Thread.sleep(spinInterval);
                    }
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return defaultObjectOwner;
            }
            
            @Override
            protected void onPostExecute(String defaultObjectOwner) {
                if (HandleBackgroundException() == false) {
                    settingsView.PopulateDefaultObjectOwner(defaultObjectOwner);
                }
                
                super.onPostExecute(defaultObjectOwner);
            }
        };
        showSettingsViewTask.execute();
    }
    
    @Override
    public void ShowConnectionSettingsView() {
        ExceptionHandlingAsyncTask<Void, Void, LocalSettings> showConnectionSettingsViewTask = new ExceptionHandlingAsyncTask<Void, Void, LocalSettings>(objectListView) {
            @Override
            protected LocalSettings doInBackground(Void... arg0) {
                LocalSettings returnLocalSettings = null;
                
                try {
                    CheckDataInterfaceServiceConnection();
                    returnLocalSettings = dataInterface.getLocalSettings();

                    // If code execution is in a unit test do not actually create the intent
                    if (instantiatedWithTestConstructor == false) {
                        Context notificationDialogDisplayViewContext = (Context)notificationDialogDisplayView;
                        Intent showConnectionSettingsViewIntent = new Intent(notificationDialogDisplayViewContext, ConnectionSettingsActivity.class);
                        notificationDialogDisplayViewContext.startActivity(showConnectionSettingsViewIntent);
                    }
                    
                    // Wait until the activity is opened.
                    while (connectionSettingsView == null) {
                        Thread.sleep(spinInterval);
                    }
                } catch (Exception e) {
                    doInBackgroundException = e;
                }
                
                return returnLocalSettings;
            }
            
            @Override
            protected void onPostExecute(LocalSettings localSettings) {
                if (HandleBackgroundException() == false) {
                    connectionSettingsView.PopulateUserIdentifier(localSettings.getUserIdentifier());
                    connectionSettingsView.PopulateRemoteDataModelProxyType(localSettings.getRemoteDataModelProxyType());
                    connectionSettingsView.PopulateSoapDataServiceLocation(localSettings.getSoapDataServiceLocation());
                    connectionSettingsView.PopulateRestDataServiceLocation(localSettings.getRestDataServiceLocation());
                }
                
                super.onPostExecute(localSettings);
            }
        };
        showConnectionSettingsViewTask.execute();
    }
    
    @Override
    public void ShowSelectScriptView() {
        Context objectListViewContext = (Context)objectListView;
        Intent showSelectScriptViewIntent = new Intent(objectListViewContext, SelectScriptActivity.class);
        objectListViewContext.startActivity(showSelectScriptViewIntent);
    }
    
    @Override
    public void Exit(int exitStatus) {
        if (selectScriptView != null) {
            selectScriptView.Close();
        }
        if (connectionSettingsView != null) {
            connectionSettingsView.Close();
        }
        if (settingsView != null) {
            settingsView.Close();
        }
        if (addRoleToUserMapView != null) {
            addRoleToUserMapView.Close();
        }
        if (roleToUserMapView != null) {
            roleToUserMapView.Close();
        }
        if (setPermissionsView != null) {
            setPermissionsView.Close();
        }
        if (selectRoleView != null) {
            selectRoleView.Close();
        }
        if (addObjectView != null) {
            addObjectView.Close();
        }
        if (objectListView != null) {
            objectListView.Close();
        }
        System.exit(exitStatus);
    }
    
    /**
     * Checks that the data interface service is connected, and if not connects.
     * <b>Note</b> - This method attempts to cast private member objectListView to a Context, and hence will likely break if used in unit tests where the object list view is mocked.
     */
    private void CheckDataInterfaceServiceConnection() throws Exception, InterruptedException {
        if (dataInterfaceServiceConnected == false) {
            Context context = (Context)objectListView;

            // Create an intent to bind to the data interface service 
            Intent dataInterfaceServiceBindIntent = new Intent(context, DataInterfaceService.class);
            context.bindService(dataInterfaceServiceBindIntent, dataInterfaceServiceConnection, Context.BIND_AUTO_CREATE);

            // Wait until the dataInterface is set by the onServiceConnected() method.
            while (dataInterfaceServiceConnected == false) {
                Thread.sleep(spinInterval);
            }
            
            // Set the remote data model proxy on the data service
            switch (dataInterface.getLocalSettings().getRemoteDataModelProxyType()) {
            case SOAP:
                SetDataServiceRemoteDataModelProxy(dataInterface.getLocalSettings().getRemoteDataModelProxyType(), dataInterface.getLocalSettings().getSoapDataServiceLocation());
                break;
            case REST:
                SetDataServiceRemoteDataModelProxy(dataInterface.getLocalSettings().getRemoteDataModelProxyType(), dataInterface.getLocalSettings().getRestDataServiceLocation());
                break;
            default:
                throw new Exception("Unhandled " + RemoteDataModelProxyType.class.getName() + " field '" + dataInterface.getLocalSettings().getRemoteDataModelProxyType().name() + "' encountered.");
            }
            
            // Unbind the context of the object list activity from the service.
            //   According to the Android documentation this should not be done, as the service could be stopped by the OS at any time after unbinding the activity from it
            //   However if the service is actually stopped, this method will detect so and reconnect
            //   Unbinding the activity from the service avoids problems with 'Activity has leaked ServiceConnection' exceptions when the activity is destroyed on orientation change or similar.
            context.unbindService(dataInterfaceServiceConnection);
        }
    }
    
    /**
     * Sets a remote data model proxy object and associated location on the data layer.
     * @param  remoteDataModelProxyType  The type of remote data model proxy to set on the data layer.
     * @param  dataServiceLocation       The location (IP address or hostname and port) of the remote data model proxy, for example '192.168.0.101:5000'.
     */
    private void SetDataServiceRemoteDataModelProxy(RemoteDataModelProxyType remoteDataModelProxyType, String dataServiceLocation) throws Exception {
        switch (remoteDataModelProxyType) {
        case SOAP:
            SoapRemoteDataModelProxy soapRemoteDataModelProxy = new SoapRemoteDataModelProxy();
            soapRemoteDataModelProxy.setDataModelLocation(dataServiceLocation);
            ((DataInterfaceService)dataInterface).setRemoteDataModelProxy(soapRemoteDataModelProxy);
            break;
        case REST:
            RestRemoteDataModelProxy restRemoteDataModelProxy = new RestRemoteDataModelProxy();
            restRemoteDataModelProxy.setDataModelLocation(dataServiceLocation);
            ((DataInterfaceService)dataInterface).setRemoteDataModelProxy(restRemoteDataModelProxy);
            break;
        default:
            throw new Exception("Unhandled " + RemoteDataModelProxyType.class.getName() + " field '" + remoteDataModelProxyType.name() + "' encountered.");
        }
    }
    
    /**
     * Implementation of the ServiceConnection class to facilitate connecting to the android service which provides the data interface for the application.
     * According to Android documentation (http://developer.android.com/reference/android/content/ServiceConnection.html), the callback methods defined in this interface are called from the main thread of the process, hence locks are not placed around access to the 'dataInterface' member.
     */
    private class DataInterfaceServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Cast the binder to a DataInterfaceServiceBinder, and get the DataInterfaceService from it.
            DataInterfaceServiceBinder binder = (DataInterfaceServiceBinder)service;
            DataInterfaceService dataInterface = binder.getService();
            dataInterface.Initialize();
            Presenter.this.dataInterface = dataInterface;
            dataInterfaceServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataInterfaceServiceConnected = false;
        }
    }
    
    /**
     * An extension of the AsyncTask class, which provides functionality to display a 'wait' message to the user while background tasks are running, and to catch and handle exceptions which occur in the doInBackground() method.  
     *
     * @param  <Params>    the type of the parameters sent to the task upon execution.
     * @param  <Progress>  the type of the progress units published during the background computation.
     * @param  <Result>    the type of the result of the background computation.
     */
    private abstract class ExceptionHandlingAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

        /** The title of the dialog which is displayed to ask the user to wait for the task to complete. */
        protected final String waitDialogTitle = "Please Wait";
        /** The message which is displayed to ask the user to wait for the task to complete. */
        protected final String waitDialogMessage = "Retrieving data...";
        /** The title of the alert dialog which is displayed if an error occurs in the doInBackground() method. */
        protected final String exceptionDialogTitle = "Error";
        /** The error message to display if an error occurs in the doInBackground() method. */
        protected final String exceptionDialogBaseMessage = "A critical error occurred.  The application will now close.";
        /** The title of the alert dialog which is displayed if a connection error occurs in the doInBackground() method. */
        protected final String connectExceptionDialogTitle = "Connection Error";
        /** The error message to display if a connection error occurs in the doInBackground() method. */
        protected final String connectExceptionDialogBaseMessage = "Unable to connect to a network.  Please try again when a network is available.";
        /** Member which should be assigned an exception, if one occurs in the doInBackground() method. */
        protected Exception doInBackgroundException;
        /** The view to use to display notification to the user (i.e. to wait for the task to complete, or to alert an exception occurs when executing the doInBackground() method). */
        protected IMvpView notificationDialogDisplayView;
        
        /**
         * Initialises a new instance of the ExceptionHandlingAsyncTask class.
         * @param  exceptionDialogDisplayView  The view to use to display notification to the user (i.e. to wait for the task to complete, or to alert an exception occurs when executing the doInBackground() method)..
         */
        public ExceptionHandlingAsyncTask(IMvpView notificationDialogDisplayView) {
            super();
            doInBackgroundException = null;
            this.notificationDialogDisplayView = notificationDialogDisplayView;
        }

        @Override
        protected void onPreExecute() {
            notificationDialogDisplayView.ShowWaitDialog(waitDialogTitle, waitDialogMessage);
        }
        
        @Override
        protected void onPostExecute(Result result) {
            notificationDialogDisplayView.CloseWaitDialog();

            // Set a signal to notify that the worker thread has completed
            if (instantiatedWithTestConstructor == true) {
                backgroundThreadCompleteSignal.countDown();
            }
        }
        
        /**
         * Handles any exception that occurred when calling the doInBackground() method.  Deriving classes should call this method in their implementation of the onPostExecute() method.  If the exception which occurred is an instance of java.io.IOException, this method will display an alert dialog to ask the user to retry the operation when the network becomes available.  If the exception which occurred is any other type of exception, this method will attempt to log the exception using the presenter's 'exceptionLogger' member, before displaying an alert dialog to inform the user of a critical error, and closing the application. 
         * @return  Whether an exception occurred.
         */
        protected boolean HandleBackgroundException() {
            // Check if an exception occurred during execution of the doInBackground() method 
            if (doInBackgroundException != null) {
                
                // If the exception was an IOException show an alert dialog to inform the user
                if (doInBackgroundException instanceof java.io.IOException) {
                    // Create a class to define the action to take when the user clicks the 'OK' button on the alert dialog
                    OnClickListener alertDialogConfirmationAction = new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // Close the alert dialog
                            notificationDialogDisplayView.CloseOkDialog();
                        }
                    };
                    notificationDialogDisplayView.ShowOkDialog(connectExceptionDialogTitle, connectExceptionDialogBaseMessage, alertDialogConfirmationAction);
                }
                
                // Otherwise attempt to log the exception and close the application
                else {
                    OnClickListener alertDialogConfirmationAction = new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // Close the alert dialog, the view (activity), and then exit the application
                            notificationDialogDisplayView.CloseOkDialog();
                            notificationDialogDisplayView.Close();
                            Exit(1);
                        }
                    };
                    
                    // Attempt to log the exception
                    boolean exceptionLogged = exceptionLogger.LogException(doInBackgroundException);
                    String exceptionDialogMessage;
                    if(exceptionLogged == true) {
                        exceptionDialogMessage = exceptionDialogBaseMessage;
                    }
                    else {
                        exceptionDialogMessage = exceptionDialogBaseMessage + "\n\n" + doInBackgroundException.getMessage();
                    }
                    notificationDialogDisplayView.ShowOkDialog(exceptionDialogTitle, exceptionDialogMessage, alertDialogConfirmationAction);
                }

                return true;
            }
            else {
                return false;
            }
        }
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
    
    /**
     * Container object which holds parameters for methods which set add or remove flags on an Oracle object (e.g. method SetAddFlag()).
     */
    private class OracleObjectFlagParameters {
        
        public String ObjectName;
        public boolean FlagValue;
        
        public OracleObjectFlagParameters(String objectName, boolean flagValue) {
            ObjectName = objectName;
            FlagValue = flagValue;
        }
    }
    
    /**
     * Container object which holds results of worker thread for method ShowAddObjectView().
     */
    private class ShowAddObjectViewResults {
        
        public ArrayList<String> ObjectTypes;
        public String ObjectOwner;
        
        public ShowAddObjectViewResults(ArrayList<String> objectTypes, String objectOwner) {
            ObjectTypes = objectTypes;
            ObjectOwner = objectOwner;
        }
    }
    
    /**
     * Container object which holds parameters for method ShowSelectRoleView().
     */
    private class ShowSelectRoleViewParameters {
        
        public String ObjectName;
        public String ObjectType;
        
        public ShowSelectRoleViewParameters(String objectName, String objectType) {
            ObjectName = objectName;
            ObjectType = objectType;
        }
    }
    
    /**
     * Container object which holds parameters for method ShowSetPermissionsView().
     */
    private class ShowSetPermissionsViewParameters extends ShowSelectRoleViewParameters {

        public String Role;
        
        public ShowSetPermissionsViewParameters(String objectName, String objectType, String role) {
            super(objectName, objectType);
            Role = role;
        }
    }
    
    /**
     * Container object which holds results of worker thread for method ShowSetPermissionsView().
     */
    private class ShowSetPermissionsViewResults {
        
        public ArrayList<String> AllPermissions;
        public ArrayList<String> ObjectPermissions;
        
        public ShowSetPermissionsViewResults(ArrayList<String> allPermissions, ArrayList<String> objectPermissions) {
            AllPermissions = allPermissions;
            ObjectPermissions = objectPermissions;
        }
    }
    
    /**
     * Container object which holds parameters for method SetPermssion().
     */
    private class SetPermissionParameters {
        
        public String ObjectName; 
        public String Role;
        public String Permission;
        public boolean Value;
        
        public SetPermissionParameters(String objectName, String role, String permission, boolean value) {
            ObjectName = objectName; 
            Role = role;
            Permission = permission;
            Value = value;
        }
    }
    
    /**
     * Container object which holds parameters for method GeneratePrivilegeScript().
     */
    private class GeneratePrivilegeScriptParameters {
        
        public ScriptType ScriptType;
        public boolean GenerateRevokeStatements;
        
        public GeneratePrivilegeScriptParameters(ScriptType scriptType, boolean generateRevokeStatements) {
            ScriptType = scriptType;
            GenerateRevokeStatements = generateRevokeStatements;
        }
    }
}