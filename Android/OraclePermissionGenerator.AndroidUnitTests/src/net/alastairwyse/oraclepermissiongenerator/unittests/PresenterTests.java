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

package net.alastairwyse.oraclepermissiongenerator.unittests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import android.content.DialogInterface.OnClickListener;
import android.test.InstrumentationTestCase;

import net.alastairwyse.oraclepermissiongenerator.*;
import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;
import net.alastairwyse.oraclepermissiongenerator.containers.unittests.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.unittests.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;

/**
 * Unit tests for class oraclepermissiongenerator.Presenter.
 * @author Alastair Wyse
 */
public class PresenterTests extends InstrumentationTestCase {
    
    private Presenter testPresenter;
    private IDataInterface mockDataInterface;
    private IExceptionLogger mockExceptionLogger;
    private CountDownLatch backgroundThreadCompleteSignal;
    private IObjectListView mockObjectListView;
    private IAddObjectView mockAddObjectView;
    private ISelectRoleView mockSelectRoleView;
    private ISetPermissionsView mockSetPermissionsView;
    private IRoleToUserMapView mockRoleToUserMapView;
    private IAddRoleToUserMapView mockAddRoleToUserMapView;
    private ISettingsView mockSettingsView;
    private IConnectionSettingsView mockConnectionSettingsView;
    private ISelectScriptView mockSelectScriptView;
    private Object[] allMocks;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        mockDataInterface = mock(IDataInterface.class);
        mockExceptionLogger = mock(IExceptionLogger.class);
        backgroundThreadCompleteSignal = new CountDownLatch(1);
        mockObjectListView = mock(IObjectListView.class);
        mockAddObjectView = mock(IAddObjectView.class);
        mockSelectRoleView = mock(ISelectRoleView.class);
        mockSetPermissionsView = mock(ISetPermissionsView.class);
        mockRoleToUserMapView = mock(IRoleToUserMapView.class);
        mockAddRoleToUserMapView = mock(IAddRoleToUserMapView.class);
        mockSettingsView = mock(ISettingsView.class);
        mockConnectionSettingsView = mock(IConnectionSettingsView.class);
        mockSelectScriptView = mock(ISelectScriptView.class);
        allMocks = new Object[] { mockDataInterface, mockExceptionLogger, mockObjectListView, mockAddObjectView, mockSelectRoleView, mockSetPermissionsView, mockRoleToUserMapView, mockAddRoleToUserMapView, mockSettingsView, mockConnectionSettingsView, mockSelectScriptView };
        testPresenter = new Presenter(mockDataInterface, mockExceptionLogger, backgroundThreadCompleteSignal);
        testPresenter.setObjectListView(mockObjectListView);
        testPresenter.setAddObjectView(mockAddObjectView);
        testPresenter.setSelectRoleView(mockSelectRoleView);
        testPresenter.setSetPermissionsView(mockSetPermissionsView);
        testPresenter.setRoleToUserMapView(mockRoleToUserMapView);
        testPresenter.setAddRoleToUserMapView(mockAddRoleToUserMapView);
        testPresenter.setSettingsView(mockSettingsView);
        testPresenter.setConnectionSettingsView(mockConnectionSettingsView);
        testPresenter.setSelectScriptView(mockSelectScriptView);
    }
    
    public void testInitialiseSuccessTest() throws Throwable {
        ArrayList<OracleObjectPermissionSet> testData = new ArrayList<OracleObjectPermissionSet>();
        
        when(mockDataInterface.getObjects()).thenReturn(testData);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.Initialise();
            }
        });
        backgroundThreadCompleteSignal.await();

        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getObjects();
        verify(mockObjectListView).ClearObjects();
        verify(mockObjectListView).PopulateObjects(testData);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testAddObjectSuccessTest() throws Throwable {
        final String testObjectName = "SP_INFORCE_INS";
        final String testObjectType = "Stored Procedure";
        final String testObjectOwner = "DEFAULT_OWNER";

        when(mockDataInterface.ObjectNameValidate(testObjectName)).thenReturn(new ValidationResult(true, ""));
        when(mockDataInterface.ObjectTypeValidate(testObjectType)).thenReturn(new ValidationResult(true, ""));
        when(mockDataInterface.ObjectOwnerValidate(testObjectOwner)).thenReturn(new ValidationResult(true, ""));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.AddObject(testObjectName, testObjectType, testObjectOwner, true, false);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockAddObjectView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).ObjectNameValidate(testObjectName);
        verify(mockDataInterface).ObjectTypeValidate(testObjectType);
        verify(mockDataInterface).ObjectOwnerValidate(testObjectOwner);
        verify(mockDataInterface).AddObjectPermissionSet(eq(testObjectName), eq(testObjectType), eq(testObjectOwner), eq(true), eq(false), any(ArrayList.class));
        verify(mockObjectListView).AddObject(any(OracleObjectPermissionSet.class));
        verify(mockAddObjectView).Close();
        verify(mockAddObjectView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testAddObjectObjectNameInvalid() throws Throwable {
        final String testObjectName = "";
        final String testObjectType = "Stored Procedure";
        final String testObjectOwner = "DEFAULT_OWNER";
        final String validationError = "The object name must be between 1 and 30 characters in length";
        
        when(mockDataInterface.ObjectNameValidate(testObjectName)).thenReturn(new ValidationResult(false, validationError));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.AddObject(testObjectName, testObjectType, testObjectOwner, true, false);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockAddObjectView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).ObjectNameValidate(testObjectName);
        verify(mockAddObjectView).ShowOkDialog(eq("Error"), eq(validationError), any(OnClickListener.class));
        verify(mockAddObjectView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testAddObjectObjectTypeInvalid() throws Throwable {
        final String testObjectName = "SP_INFORCE_INS";
        final String testObjectType = "Stored Procedure";
        final String testObjectOwner = "";
        final String validationError = "The object owner must be between 1 and 30 characters in length";
        
        when(mockDataInterface.ObjectNameValidate(testObjectName)).thenReturn(new ValidationResult(true, ""));
        when(mockDataInterface.ObjectTypeValidate(testObjectType)).thenReturn(new ValidationResult(true, ""));
        when(mockDataInterface.ObjectOwnerValidate(testObjectOwner)).thenReturn(new ValidationResult(false, validationError));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.AddObject(testObjectName, testObjectType, testObjectOwner, true, false);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockAddObjectView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).ObjectNameValidate(testObjectName);
        verify(mockDataInterface).ObjectTypeValidate(testObjectType);
        verify(mockDataInterface).ObjectOwnerValidate(testObjectOwner);
        verify(mockAddObjectView).ShowOkDialog(eq("Error"), eq(validationError), any(OnClickListener.class));
        verify(mockAddObjectView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testAddObjectObjectOwnerInvalid() throws Throwable {
        final String testObjectName = "SP_INFORCE_INS";
        final String testObjectType = "";
        final String testObjectOwner = "DEFAULT_OWNER";
        final String validationError = "The object owner must be between 1 and 30 characters in length";
        
        when(mockDataInterface.ObjectNameValidate(testObjectName)).thenReturn(new ValidationResult(true, ""));
        when(mockDataInterface.ObjectTypeValidate(testObjectType)).thenReturn(new ValidationResult(false, validationError));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.AddObject(testObjectName, testObjectType, testObjectOwner, true, false);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockAddObjectView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).ObjectNameValidate(testObjectName);
        verify(mockDataInterface).ObjectTypeValidate(testObjectType);
        verify(mockAddObjectView).ShowOkDialog(eq("Error"), eq(validationError), any(OnClickListener.class));
        verify(mockAddObjectView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testRemoveObjectPermissionSetSuccessTest() throws Throwable {
        final String testOracleObjectName = "SP_INFORCE_INS";
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.RemoveObject(testOracleObjectName);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).RemoveObjectPermissionSet(testOracleObjectName);
        verify(mockObjectListView).RemoveObject(testOracleObjectName);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testSetAddFlagSuccessTest() throws Throwable {
        final String testOracleObjectName = "SP_INFORCE_INS";
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SetAddFlag(testOracleObjectName, true);;
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).SetAddFlag(testOracleObjectName, true);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testSetRemoveFlagSuccessTest() throws Throwable {
        final String testOracleObjectName = "SP_INFORCE_INS";
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SetRemoveFlag(testOracleObjectName, true);;
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).SetRemoveFlag(testOracleObjectName, true);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testShowAddObjectViewSuccessTest() throws Throwable {
        ArrayList<String> testObjectTypes = new ArrayList<String>(Arrays.asList("Table", "View", "Stored Procedure"));
        String testDefaultObjectOwner = "DEFAULT_OWNER";

        when(mockDataInterface.getObjectTypes()).thenReturn(testObjectTypes);
        when(mockDataInterface.getDefaultObjectOwner()).thenReturn(testDefaultObjectOwner);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.ShowAddObjectView();
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getObjectTypes();
        verify(mockDataInterface).getDefaultObjectOwner();
        verify(mockAddObjectView).PopulateObjectTypes(testObjectTypes);
        verify(mockAddObjectView).PopulateObjectOwner(testDefaultObjectOwner);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testShowSelectRoleViewSuccessTest() throws Throwable {
        final String testOracleObjectName = "SP_INFORCE_INS";
        final String testObjectType = "Stored Procedure";
        ArrayList<String> testRoles = new ArrayList<String>(Arrays.asList("APP_ROLE", "GUI_ROLE", "READ_ROLE"));
        
        when(mockDataInterface.getRoles()).thenReturn(testRoles);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.ShowSelectRoleView(testOracleObjectName, testObjectType);;
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getRoles();
        verify(mockSelectRoleView).setObjectName(testOracleObjectName);
        verify(mockSelectRoleView).setObjectType(testObjectType);
        verify(mockSelectRoleView).PopulateRoles(testRoles);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testShowSetPermissionsViewSuccessTest() throws Throwable {
        final String testObjectName = "INFORCE";
        final String testObjectType = "Table";
        final String testRole = "APP_ROLE";
        ArrayList<String> testAllPermissions = new ArrayList<String>(Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE"));
        ArrayList<String> testObjectPermissions = new ArrayList<String>(Arrays.asList("INSERT"));
        
        when(mockDataInterface.getPermissions(testObjectType)).thenReturn(testAllPermissions);
        when(mockDataInterface.getPermissions(testObjectName, testRole)).thenReturn(testObjectPermissions);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.ShowSetPermissionsView(testObjectName, testObjectType, testRole);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockSelectRoleView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getPermissions(testObjectType);
        verify(mockDataInterface).getPermissions(testObjectName, testRole);
        verify(mockSetPermissionsView).setObjectName(testObjectName);
        verify(mockSetPermissionsView).setRole(testRole);
        verify(mockSetPermissionsView).PopulatePermissions(testObjectPermissions, testAllPermissions);
        verify(mockSelectRoleView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testSetPermssionTrueSuccessTest() throws Throwable {
        final String testObjectName = "INFORCE";
        final String testRole = "APP_ROLE";
        final String testPermission = "INSERT";
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SetPermssion(testObjectName, testRole, testPermission, true);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockSetPermissionsView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).AddPermission(testObjectName, testRole, testPermission);
        verify(mockSetPermissionsView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testSetPermssionFalseSuccessTest() throws Throwable {
        final String testObjectName = "INFORCE";
        final String testRole = "APP_ROLE";
        final String testPermission = "INSERT";
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SetPermssion(testObjectName, testRole, testPermission, false);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockSetPermissionsView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).RemovePermission(testObjectName, testRole, testPermission);
        verify(mockSetPermissionsView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testShowRoleToUserMapViewSuccessTest() throws Throwable {
        ArrayList<RoleToUserMap> testRoleToUserMappings = new ArrayList<RoleToUserMap>();
        testRoleToUserMappings.add(new RoleToUserMap("XYZON_APP_ROLE", "XYZON_APP_USER"));
        testRoleToUserMappings.add(new RoleToUserMap("XYZON_POWER_ROLE", "XYZON_POWER_USER"));
        testRoleToUserMappings.add(new RoleToUserMap("XYZON_READ_ROLE", "JONES_SAM"));
        testRoleToUserMappings.add(new RoleToUserMap("XYZON_READ_ROLE", "SMITH_JOHN"));
        
        when(mockDataInterface.getMasterRoleToUserMapCollection()).thenReturn(testRoleToUserMappings);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.ShowRoleToUserMapView();
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getMasterRoleToUserMapCollection();
        verify(mockRoleToUserMapView).PopulateRoleToUserMappings(testRoleToUserMappings);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testAddRoleToUserMapSuccessTest() throws Throwable {
        final String testRole = "XYZON_POWER_ROLE";
        final String testUser = "XYZON_POWER_USER";
        
        when(mockDataInterface.RoleToUserMapValidate(testRole, testUser)).thenReturn(new ValidationResult(true, ""));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.AddRoleToUserMap(testRole, testUser);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockAddRoleToUserMapView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).RoleToUserMapValidate(testRole, testUser);
        verify(mockDataInterface).AddRoleToUserMap(testRole, testUser);
        verify(mockRoleToUserMapView).AddRoleToUserMap(argThat(new RoleToUserMapMatcher(new RoleToUserMap(testRole, testUser))));
        verify(mockAddRoleToUserMapView).Close();
        verify(mockAddRoleToUserMapView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testAddRoleToUserMapBlankRole() throws Throwable {
        final String testRole = "";
        final String testUser = "XYZON_POWER_USER";
        final String validationError = "The role must be between 1 and 30 characters in length";
        
        when(mockDataInterface.RoleToUserMapValidate(testRole, testUser)).thenReturn(new ValidationResult(false, validationError));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.AddRoleToUserMap(testRole, testUser);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockAddRoleToUserMapView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).RoleToUserMapValidate(testRole, testUser);
        verify(mockAddRoleToUserMapView).ShowOkDialog(eq("Error"), eq(validationError), any(OnClickListener.class));
        verify(mockAddRoleToUserMapView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testRemoveRoleToUserMapReferencingObjectsDontExistSuccessTest() throws Throwable {
        final String testRole = "XYZON_POWER_ROLE";
        final String testUser = "XYZON_POWER_USER";
        
        when(mockDataInterface.RoleGetReferencingObjects(testRole)).thenReturn(new ArrayList<String>());
        when(mockDataInterface.getMasterRoleToUserMapCollection()).thenReturn(new ArrayList<RoleToUserMap>(Arrays.asList(new RoleToUserMap(testRole, testUser))));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.RemoveRoleToUserMap(testRole, testUser);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockRoleToUserMapView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).RoleGetReferencingObjects(testRole);
        verify(mockDataInterface).getMasterRoleToUserMapCollection();
        verify(mockDataInterface).RemoveRoleToUserMap(testRole, testUser);
        verify(mockRoleToUserMapView).RemoveRoleToUserMap(argThat(new RoleToUserMapMatcher(new RoleToUserMap(testRole, testUser))));
        verify(mockRoleToUserMapView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testRemoveRoleToUserMapReferencingObjectsExistSuccessTest() throws Throwable {
        final String testRole = "XYZON_POWER_ROLE";
        final String testUser = "XYZON_POWER_USER";
        final String referencingObjectName = "VW_CUSTOMERS";
        
        when(mockDataInterface.RoleGetReferencingObjects(testRole)).thenReturn(new ArrayList<String>(Arrays.asList(referencingObjectName)));
        when(mockDataInterface.getMasterRoleToUserMapCollection()).thenReturn(new ArrayList<RoleToUserMap>(Arrays.asList(new RoleToUserMap(testRole, testUser), new RoleToUserMap(testRole, "XYZON_POWER_USER2"))));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.RemoveRoleToUserMap(testRole, testUser);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockRoleToUserMapView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).RoleGetReferencingObjects(testRole);
        verify(mockDataInterface).getMasterRoleToUserMapCollection();
        verify(mockDataInterface).RemoveRoleToUserMap(testRole, testUser);
        verify(mockRoleToUserMapView).RemoveRoleToUserMap(argThat(new RoleToUserMapMatcher(new RoleToUserMap(testRole, testUser))));
        verify(mockRoleToUserMapView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testRemoveRoleToUserMapReferencingObjectsExist() throws Throwable {
        final String testRole = "XYZON_POWER_ROLE";
        final String testUser = "XYZON_POWER_USER";
        final String referencingObjectName = "VW_CUSTOMERS";
        final String validationError = "The role to user mapping is referenced by the following objects.  Please remove the references or objects and try again.\n  " + referencingObjectName + "\n";
        
        when(mockDataInterface.RoleGetReferencingObjects(testRole)).thenReturn(new ArrayList<String>(Arrays.asList(referencingObjectName)));
        when(mockDataInterface.getMasterRoleToUserMapCollection()).thenReturn(new ArrayList<RoleToUserMap>(Arrays.asList(new RoleToUserMap(testRole, testUser))));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.RemoveRoleToUserMap(testRole, testUser);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockRoleToUserMapView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).RoleGetReferencingObjects(testRole);
        verify(mockDataInterface).getMasterRoleToUserMapCollection();
        verify(mockRoleToUserMapView).ShowOkDialog(eq("Error"), eq(validationError), any(OnClickListener.class));
        verify(mockRoleToUserMapView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testSaveSettingsSuccessTest() throws Throwable {
        final String testDefaultObjectOwner = "XYZON";
        
        when(mockDataInterface.ObjectOwnerValidate(testDefaultObjectOwner)).thenReturn(new ValidationResult(true, ""));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SaveSettings(testDefaultObjectOwner);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockSettingsView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).ObjectOwnerValidate(testDefaultObjectOwner);
        verify(mockDataInterface).setDefaultObjectOwner(testDefaultObjectOwner);
        verify(mockSettingsView).Close();
        verify(mockSettingsView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testSaveSettingsDefaultObjectOwnerInvalid() throws Throwable {
        final String testDefaultObjectOwner = "XYZON";
        final String validationError = "The object owner must be between 1 and 30 characters in length";
        
        when(mockDataInterface.ObjectOwnerValidate(testDefaultObjectOwner)).thenReturn(new ValidationResult(false, validationError));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SaveSettings(testDefaultObjectOwner);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockSettingsView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).ObjectOwnerValidate(testDefaultObjectOwner);
        verify(mockSettingsView).ShowOkDialog(eq("Error"), eq(validationError), any(OnClickListener.class));
        verify(mockSettingsView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }

    public void testShowSettingsViewSuccessTest() throws Throwable {
        final String testDefaultObjectOwner = "XYZON";
        
        when(mockDataInterface.getDefaultObjectOwner()).thenReturn(testDefaultObjectOwner);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.ShowSettingsView();
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getDefaultObjectOwner();
        verify(mockSettingsView).PopulateDefaultObjectOwner(testDefaultObjectOwner);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }

    public void testShowConnectionSettingsViewSuccessTest() throws Throwable {
        final String testUserIdentifier = "user@tempuri.org";
        final RemoteDataModelProxyType testRemoteDataModelProxyType = RemoteDataModelProxyType.SOAP;
        final String testSoapDataServiceLocation = "192.168.1.101:5000";
        final String testRestDataServiceLocation = "192.168.1.101:5001";
        LocalSettings testLocalSettings = new LocalSettings();
        testLocalSettings.setUserIdentifier(testUserIdentifier);
        testLocalSettings.setRemoteDataModelProxyType(testRemoteDataModelProxyType);
        testLocalSettings.setSoapDataServiceLocation(testSoapDataServiceLocation);
        testLocalSettings.setRestDataServiceLocation(testRestDataServiceLocation);

        when(mockDataInterface.getLocalSettings()).thenReturn(testLocalSettings);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.ShowConnectionSettingsView();
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getLocalSettings();
        verify(mockConnectionSettingsView).PopulateUserIdentifier(testUserIdentifier);
        verify(mockConnectionSettingsView).PopulateRemoteDataModelProxyType(testRemoteDataModelProxyType);
        verify(mockConnectionSettingsView).PopulateSoapDataServiceLocation(testSoapDataServiceLocation);
        verify(mockConnectionSettingsView).PopulateRestDataServiceLocation(testRestDataServiceLocation);
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testSaveConnectionSettingsNoSettingsChangedSuccessTest() throws Throwable {
        // Reset the 'backgroundThreadCompleteSignal' to have a count of 2, as 2 worker threads are created using AsyncTask objects in the SaveConnectionSettings() method. 
        backgroundThreadCompleteSignal = new CountDownLatch(2);
        testPresenter = new Presenter(mockDataInterface, mockExceptionLogger, backgroundThreadCompleteSignal);
        testPresenter.setObjectListView(mockObjectListView);
        testPresenter.setAddObjectView(mockAddObjectView);
        testPresenter.setSelectRoleView(mockSelectRoleView);
        testPresenter.setSetPermissionsView(mockSetPermissionsView);
        testPresenter.setRoleToUserMapView(mockRoleToUserMapView);
        testPresenter.setAddRoleToUserMapView(mockAddRoleToUserMapView);
        testPresenter.setSettingsView(mockSettingsView);
        testPresenter.setConnectionSettingsView(mockConnectionSettingsView);
        testPresenter.setSelectScriptView(mockSelectScriptView);
        
        final String testUserIdentifier = "user@tempuri.org";
        final RemoteDataModelProxyType testRemoteDataModelProxyType = RemoteDataModelProxyType.SOAP;
        final String testSoapDataServiceLocation = "192.168.1.101:5000";
        final String testRestDataServiceLocation = "192.168.1.101:5001";
        LocalSettings testLocalSettings = new LocalSettings();
        testLocalSettings.setUserIdentifier(testUserIdentifier);
        testLocalSettings.setRemoteDataModelProxyType(testRemoteDataModelProxyType);
        testLocalSettings.setSoapDataServiceLocation(testSoapDataServiceLocation);
        testLocalSettings.setRestDataServiceLocation(testRestDataServiceLocation);
        LocalSettings currentLocalSettings = new LocalSettings();
        currentLocalSettings.setUserIdentifier("user@tempuri.org");
        currentLocalSettings.setRemoteDataModelProxyType(RemoteDataModelProxyType.SOAP);
        currentLocalSettings.setSoapDataServiceLocation("192.168.1.101:5000");
        currentLocalSettings.setRestDataServiceLocation("192.168.1.101:5001");
        ArrayList<OracleObjectPermissionSet> testData = new ArrayList<OracleObjectPermissionSet>();
        
        when(mockDataInterface.getLocalSettings()).thenReturn(currentLocalSettings);
        when(mockDataInterface.getObjects()).thenReturn(testData);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SaveConnectionSettings(testUserIdentifier, testRemoteDataModelProxyType, testSoapDataServiceLocation, testRestDataServiceLocation);
            }
        });
        backgroundThreadCompleteSignal.await();

        verify(mockConnectionSettingsView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getLocalSettings();
        verify(mockDataInterface).setLocalSettings(argThat(new LocalSettingsMatcher(currentLocalSettings)));
        verify(mockConnectionSettingsView).Close();
        verify(mockDataInterface).getObjects();
        verify(mockConnectionSettingsView).CloseWaitDialog();
        verifyNoMoreInteractions(mockDataInterface, mockConnectionSettingsView);
    }

    public void testSaveConnectionSettingsRemoteDataModelProxyTypeChangedToRestSuccessTest() throws Throwable {
        // Tests that a new IRemoteDataModelProxy is created and set on the data layer when the RemoteDataModelProxyType is changed from 'REST' to 'SOAP'
        
        // Reset the 'backgroundThreadCompleteSignal' to have a count of 2, as 2 worker threads are created using AsyncTask objects in the SaveConnectionSettings() method (due to nested call to Presenter.Initialise() method). 
        backgroundThreadCompleteSignal = new CountDownLatch(2);
        // Mock the IDataInterface parameter set on the Presenter as a DataInterfaceService class rather than IDataInterface interface, so that the setRemoteDataModelProxy() method is available to the test code. 
        DataInterfaceService mockDataInterfaceService = mock(DataInterfaceService.class);
        testPresenter = new Presenter(mockDataInterfaceService, mockExceptionLogger, backgroundThreadCompleteSignal);
        testPresenter.setObjectListView(mockObjectListView);
        testPresenter.setAddObjectView(mockAddObjectView);
        testPresenter.setSelectRoleView(mockSelectRoleView);
        testPresenter.setSetPermissionsView(mockSetPermissionsView);
        testPresenter.setRoleToUserMapView(mockRoleToUserMapView);
        testPresenter.setAddRoleToUserMapView(mockAddRoleToUserMapView);
        testPresenter.setSettingsView(mockSettingsView);
        testPresenter.setConnectionSettingsView(mockConnectionSettingsView);
        testPresenter.setSelectScriptView(mockSelectScriptView);
        
        final String testUserIdentifier = "user@tempuri.org";
        final RemoteDataModelProxyType testRemoteDataModelProxyType = RemoteDataModelProxyType.SOAP;
        final String testSoapDataServiceLocation = "192.168.1.101:5000";
        final String testRestDataServiceLocation = "192.168.1.101:5001";

        LocalSettings testLocalSettings = new LocalSettings();
        testLocalSettings.setUserIdentifier(testUserIdentifier);
        testLocalSettings.setRemoteDataModelProxyType(testRemoteDataModelProxyType);
        testLocalSettings.setSoapDataServiceLocation(testSoapDataServiceLocation);
        testLocalSettings.setRestDataServiceLocation(testRestDataServiceLocation);
        LocalSettings currentLocalSettings = new LocalSettings();
        currentLocalSettings.setUserIdentifier("user@tempuri.org");
        currentLocalSettings.setRemoteDataModelProxyType(RemoteDataModelProxyType.REST);
        currentLocalSettings.setSoapDataServiceLocation("192.168.1.101:5000");
        currentLocalSettings.setRestDataServiceLocation("192.168.1.101:5001");
        SoapRemoteDataModelProxy testRemoteDataModelProxy = new SoapRemoteDataModelProxy();
        testRemoteDataModelProxy.setDataModelLocation(testSoapDataServiceLocation);
        
        ArrayList<OracleObjectPermissionSet> testData = new ArrayList<OracleObjectPermissionSet>();
        
        when(mockDataInterfaceService.getLocalSettings()).thenReturn(currentLocalSettings);
        when(mockDataInterfaceService.getObjects()).thenReturn(testData);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SaveConnectionSettings(testUserIdentifier, testRemoteDataModelProxyType, testSoapDataServiceLocation, testRestDataServiceLocation);
            }
        });
        backgroundThreadCompleteSignal.await();

        verify(mockConnectionSettingsView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterfaceService).getLocalSettings();
        verify(mockDataInterfaceService).setLocalSettings(argThat(new LocalSettingsMatcher(testLocalSettings)));
        verify(mockDataInterfaceService).setRemoteDataModelProxy(argThat(new SoapRemoteDataModelProxyMatcher(testRemoteDataModelProxy)));
        verify(mockConnectionSettingsView).Close();
        verify(mockDataInterfaceService).getObjects();
        verify(mockConnectionSettingsView).CloseWaitDialog();
        verifyNoMoreInteractions(mockDataInterfaceService, mockConnectionSettingsView);
    }

    public void testSaveConnectionSettingsSoapDataServiceLocationChangedSuccessTest() throws Throwable {
        // Tests that a new IRemoteDataModelProxy is created and set on the data layer when the SoapDataServiceLocation is changed from '192.168.1.101:5002' to '192.168.1.101:5000'
        
        // Reset the 'backgroundThreadCompleteSignal' to have a count of 2, as 2 worker threads are created using AsyncTask objects in the SaveConnectionSettings() method (due to nested call to Presenter.Initialise() method). 
        backgroundThreadCompleteSignal = new CountDownLatch(2);
        // Mock the IDataInterface parameter set on the Presenter as a DataInterfaceService class rather than IDataInterface interface, so that the setRemoteDataModelProxy() method is available to the test code. 
        DataInterfaceService mockDataInterfaceService = mock(DataInterfaceService.class);
        testPresenter = new Presenter(mockDataInterfaceService, mockExceptionLogger, backgroundThreadCompleteSignal);
        testPresenter.setObjectListView(mockObjectListView);
        testPresenter.setAddObjectView(mockAddObjectView);
        testPresenter.setSelectRoleView(mockSelectRoleView);
        testPresenter.setSetPermissionsView(mockSetPermissionsView);
        testPresenter.setRoleToUserMapView(mockRoleToUserMapView);
        testPresenter.setAddRoleToUserMapView(mockAddRoleToUserMapView);
        testPresenter.setSettingsView(mockSettingsView);
        testPresenter.setConnectionSettingsView(mockConnectionSettingsView);
        testPresenter.setSelectScriptView(mockSelectScriptView);
        
        final String testUserIdentifier = "user@tempuri.org";
        final RemoteDataModelProxyType testRemoteDataModelProxyType = RemoteDataModelProxyType.SOAP;
        final String testSoapDataServiceLocation = "192.168.1.101:5000";
        final String testRestDataServiceLocation = "192.168.1.101:5001";

        LocalSettings testLocalSettings = new LocalSettings();
        testLocalSettings.setUserIdentifier(testUserIdentifier);
        testLocalSettings.setRemoteDataModelProxyType(testRemoteDataModelProxyType);
        testLocalSettings.setSoapDataServiceLocation(testSoapDataServiceLocation);
        testLocalSettings.setRestDataServiceLocation(testRestDataServiceLocation);
        LocalSettings currentLocalSettings = new LocalSettings();
        currentLocalSettings.setUserIdentifier("user@tempuri.org");
        currentLocalSettings.setRemoteDataModelProxyType(RemoteDataModelProxyType.SOAP);
        currentLocalSettings.setSoapDataServiceLocation("192.168.1.101:5002");
        currentLocalSettings.setRestDataServiceLocation("192.168.1.101:5001");
        SoapRemoteDataModelProxy testRemoteDataModelProxy = new SoapRemoteDataModelProxy();
        testRemoteDataModelProxy.setDataModelLocation(testSoapDataServiceLocation);
        
        ArrayList<OracleObjectPermissionSet> testData = new ArrayList<OracleObjectPermissionSet>();
        
        when(mockDataInterfaceService.getLocalSettings()).thenReturn(currentLocalSettings);
        when(mockDataInterfaceService.getObjects()).thenReturn(testData);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SaveConnectionSettings(testUserIdentifier, testRemoteDataModelProxyType, testSoapDataServiceLocation, testRestDataServiceLocation);
            }
        });
        backgroundThreadCompleteSignal.await();

        verify(mockConnectionSettingsView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterfaceService).getLocalSettings();
        verify(mockDataInterfaceService).setLocalSettings(argThat(new LocalSettingsMatcher(testLocalSettings)));
        verify(mockDataInterfaceService).setRemoteDataModelProxy(argThat(new SoapRemoteDataModelProxyMatcher(testRemoteDataModelProxy)));
        verify(mockConnectionSettingsView).Close();
        verify(mockDataInterfaceService).getObjects();
        verify(mockConnectionSettingsView).CloseWaitDialog();
        verifyNoMoreInteractions(mockDataInterfaceService, mockConnectionSettingsView);
    }

    public void testSaveConnectionSettingsRestDataServiceLocationChangedSuccessTest() throws Throwable {
        // Tests that a new IRemoteDataModelProxy is created and set on the data layer when the RestDataServiceLocation is changed from '192.168.1.101:5001' to '192.168.1.101:5002'
        
        // Reset the 'backgroundThreadCompleteSignal' to have a count of 2, as 2 worker threads are created using AsyncTask objects in the SaveConnectionSettings() method (due to nested call to Presenter.Initialise() method). 
        backgroundThreadCompleteSignal = new CountDownLatch(2);
        // Mock the IDataInterface parameter set on the Presenter as a DataInterfaceService class rather than IDataInterface interface, so that the setRemoteDataModelProxy() method is available to the test code. 
        DataInterfaceService mockDataInterfaceService = mock(DataInterfaceService.class);
        testPresenter = new Presenter(mockDataInterfaceService, mockExceptionLogger, backgroundThreadCompleteSignal);
        testPresenter.setObjectListView(mockObjectListView);
        testPresenter.setAddObjectView(mockAddObjectView);
        testPresenter.setSelectRoleView(mockSelectRoleView);
        testPresenter.setSetPermissionsView(mockSetPermissionsView);
        testPresenter.setRoleToUserMapView(mockRoleToUserMapView);
        testPresenter.setAddRoleToUserMapView(mockAddRoleToUserMapView);
        testPresenter.setSettingsView(mockSettingsView);
        testPresenter.setConnectionSettingsView(mockConnectionSettingsView);
        testPresenter.setSelectScriptView(mockSelectScriptView);
        
        final String testUserIdentifier = "user@tempuri.org";
        final RemoteDataModelProxyType testRemoteDataModelProxyType = RemoteDataModelProxyType.REST;
        final String testSoapDataServiceLocation = "192.168.1.101:5000";
        final String testRestDataServiceLocation = "192.168.1.101:5002";

        LocalSettings testLocalSettings = new LocalSettings();
        testLocalSettings.setUserIdentifier(testUserIdentifier);
        testLocalSettings.setRemoteDataModelProxyType(testRemoteDataModelProxyType);
        testLocalSettings.setSoapDataServiceLocation(testSoapDataServiceLocation);
        testLocalSettings.setRestDataServiceLocation(testRestDataServiceLocation);
        LocalSettings currentLocalSettings = new LocalSettings();
        currentLocalSettings.setUserIdentifier("user@tempuri.org");
        currentLocalSettings.setRemoteDataModelProxyType(RemoteDataModelProxyType.REST);
        currentLocalSettings.setSoapDataServiceLocation("192.168.1.101:5000");
        currentLocalSettings.setRestDataServiceLocation("192.168.1.101:5001");
        RestRemoteDataModelProxy testRemoteDataModelProxy = new RestRemoteDataModelProxy();
        testRemoteDataModelProxy.setDataModelLocation(testRestDataServiceLocation);
        
        ArrayList<OracleObjectPermissionSet> testData = new ArrayList<OracleObjectPermissionSet>();
        
        when(mockDataInterfaceService.getLocalSettings()).thenReturn(currentLocalSettings);
        when(mockDataInterfaceService.getObjects()).thenReturn(testData);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.SaveConnectionSettings(testUserIdentifier, testRemoteDataModelProxyType, testSoapDataServiceLocation, testRestDataServiceLocation);
            }
        });
        backgroundThreadCompleteSignal.await();

        verify(mockConnectionSettingsView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterfaceService).getLocalSettings();
        verify(mockDataInterfaceService).setLocalSettings(argThat(new LocalSettingsMatcher(testLocalSettings)));
        verify(mockDataInterfaceService).setRemoteDataModelProxy(argThat(new RestRemoteDataModelProxyMatcher(testRemoteDataModelProxy)));
        verify(mockConnectionSettingsView).Close();
        verify(mockDataInterfaceService).getObjects();
        verify(mockConnectionSettingsView).CloseWaitDialog();
        verifyNoMoreInteractions(mockDataInterfaceService, mockConnectionSettingsView);
    }
    
    public void testGeneratePrivilegeScriptSuccessTest() throws Throwable {
        final String testScriptText = "GRANT SELECT ON CUSTOMERS TO XYZON_POWER_ROLE;\nGRANT SELECT ON ITEMS TO XYZON_POWER_ROLE;";
        
        when(mockDataInterface.CreatePrivilegeScript(ScriptType.Rollout, true)).thenReturn(testScriptText);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.GeneratePrivilegeScript(ScriptType.Rollout, true);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockSelectScriptView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).CreatePrivilegeScript(ScriptType.Rollout, true);
        verify(mockDataInterface).WriteScript(testScriptText);
        verify(mockSelectScriptView).Close();
        verify(mockSelectScriptView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    public void testGenerateSynonymScriptSuccessTest() throws Throwable {
        final String testScriptText = "CREATE SYNONYM XYZON_POWER_USER.CUSTOMERS FOR XYZON.CUSTOMERS;\nCREATE SYNONYM XYZON_POWER_USER.ITEMS FOR XYZON.ITEMS;";
        
        when(mockDataInterface.CreateSynonymScript(ScriptType.Rollout)).thenReturn(testScriptText);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.GenerateSynonymScript(ScriptType.Rollout);
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockSelectScriptView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).CreateSynonymScript(ScriptType.Rollout);
        verify(mockDataInterface).WriteScript(testScriptText);
        verify(mockSelectScriptView).Close();
        verify(mockSelectScriptView).CloseWaitDialog();
        verifyNoMoreInteractions(allMocks);
    }
    
    /**
     * Extension of the mockito ArgumentMatcher class which allows RoleToUserMap classes to be compared when used as parameters in mockito verify() and when() method calls.
     */
    private class RoleToUserMapMatcher extends ArgumentMatcher<RoleToUserMap> {

        private RoleToUserMap roleToUserMapToMatch;

        /**
         * Initialises a new instance of the RoleToUserMapMatcher class.
         * @param  roleToUserMapToMatch  The RoleToUserMap class that should match the parameter passed to the mockito verify() or when() method call.
         */
        public RoleToUserMapMatcher(RoleToUserMap roleToUserMapToMatch) {
            this.roleToUserMapToMatch = roleToUserMapToMatch;
        }
        
        @Override
        public boolean matches(Object argument) {
            if (argument.getClass() != roleToUserMapToMatch.getClass()) {
                return false;
            }
            
            RoleToUserMap comparisonRoleToUserMap = (RoleToUserMap)argument;
            if ( roleToUserMapToMatch.getRole().equals(comparisonRoleToUserMap.getRole()) == false || 
                 roleToUserMapToMatch.getUser().equals(comparisonRoleToUserMap.getUser()) == false ) {
                return false;
            }
            
            return true;
        }
        
        @Override
        public void describeTo(Description description) {
            // This method is typically used in mockito when a test fails, to report the value that was wanted but not matched.  E.g. this implementation will return...
            //   "RoleToUserMap([role], [user])"
            // ... which will typically be written to the mockito failure trace.
            
            description.appendText(roleToUserMapToMatch.getClass().getSimpleName() + "(" + roleToUserMapToMatch.getRole().toString() + ", " + roleToUserMapToMatch.getUser().toString() + ")");
        }
    }
}
