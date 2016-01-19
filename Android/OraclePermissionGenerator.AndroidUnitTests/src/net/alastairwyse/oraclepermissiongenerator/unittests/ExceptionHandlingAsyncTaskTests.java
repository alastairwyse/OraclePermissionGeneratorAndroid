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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import android.content.DialogInterface.OnClickListener;
import android.test.*;

import net.alastairwyse.oraclepermissiongenerator.*;
import net.alastairwyse.oraclepermissiongenerator.containers.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for class oraclepermissiongenerator.ExceptionHandlingAsyncTask.
 * Note this class is a private inner class inside the Presenter class.  Hence the Presenter Initialise() method is used as a proxy to test the underlying ExceptionHandlingAsyncTask class. 
 * @author Alastair Wyse
 */
public class ExceptionHandlingAsyncTaskTests extends InstrumentationTestCase {
    
    private Presenter testPresenter;
    private IDataInterface mockDataInterface;
    private IExceptionLogger mockExceptionLogger;
    private CountDownLatch backgroundThreadCompleteSignal;
    private IObjectListView mockObjectListView;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        mockDataInterface = mock(IDataInterface.class);
        mockExceptionLogger = mock(IExceptionLogger.class);
        backgroundThreadCompleteSignal = new CountDownLatch(1);
        mockObjectListView = mock(IObjectListView.class);
        testPresenter = new Presenter(mockDataInterface, mockExceptionLogger, backgroundThreadCompleteSignal);
        testPresenter.setObjectListView(mockObjectListView);
    }
    
    public void testHandleBackgroundExceptionSuccessTest() throws Throwable {
        ArrayList<OracleObjectPermissionSet> testData = CreateTestData();
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
        verifyNoMoreInteractions(mockDataInterface, mockExceptionLogger, mockObjectListView);
    }
    
    public void testHandleBackgroundExceptionIOException() throws Throwable {
        when(mockDataInterface.getObjects()).thenThrow(new IOException("Mock IOException"));
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.Initialise();
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getObjects();
        verify(mockObjectListView).ShowOkDialog(eq("Connection Error"), eq("Unable to connect to a network.  Please try again when a network is available."), any(OnClickListener.class));
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(mockDataInterface, mockExceptionLogger, mockObjectListView);
    }
    
    public void testHandleBackgroundExceptionOtherExceptionLoggingSucessful() throws Throwable {
        Exception mockException = new Exception("Mock Fatal Exception");
        when(mockDataInterface.getObjects()).thenThrow(mockException);
        when(mockExceptionLogger.LogException(mockException)).thenReturn(true);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.Initialise();
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getObjects();
        verify(mockExceptionLogger).LogException(mockException);

        verify(mockObjectListView).ShowOkDialog(eq("Error"), eq("A critical error occurred.  The application will now close."), any(OnClickListener.class));
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(mockDataInterface, mockExceptionLogger, mockObjectListView);
    }
    
    public void testHandleBackgroundExceptionOtherExceptionLoggingFailed() throws Throwable {
        final String expectedErrorMessage = "A critical error occurred.  The application will now close.\n\nMock Fatal Exception"; 
        Exception mockException = new Exception("Mock Fatal Exception");
        when(mockDataInterface.getObjects()).thenThrow(mockException);
        when(mockExceptionLogger.LogException(mockException)).thenReturn(false);
        
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testPresenter.Initialise();
            }
        });
        backgroundThreadCompleteSignal.await();
        
        verify(mockObjectListView).ShowWaitDialog("Please Wait", "Retrieving data...");
        verify(mockDataInterface).getObjects();
        verify(mockExceptionLogger).LogException(mockException);

        verify(mockObjectListView).ShowOkDialog(eq("Error"), eq(expectedErrorMessage), any(OnClickListener.class));
        verify(mockObjectListView).CloseWaitDialog();
        verifyNoMoreInteractions(mockDataInterface, mockExceptionLogger, mockObjectListView);
    }
    
    private ArrayList<OracleObjectPermissionSet> CreateTestData() {
        ArrayList<OracleObjectPermissionSet> returnData = new ArrayList<OracleObjectPermissionSet>();
        OracleObjectPermissionSet firstPermissionSet = new OracleObjectPermissionSet("SP_INFORCE_INS");
        firstPermissionSet.setObjectType("STORED_PROCEDURE");
        firstPermissionSet.setObjectOwner("DEFAULT_OWNER");
        firstPermissionSet.setAddFlag(true);
        firstPermissionSet.setAddFlag(false);
        ArrayList<RoleToPermissionMap> firstPermissionSetPermissions = new ArrayList<RoleToPermissionMap>();
        firstPermissionSetPermissions.add(new RoleToPermissionMap("POWER_ROLE", "EXECUTE"));
        firstPermissionSetPermissions.add(new RoleToPermissionMap("APP_ROLE", "EXECUTE"));
        firstPermissionSet.setObjectPermissions(firstPermissionSetPermissions);
        
        return returnData;
    }
}
