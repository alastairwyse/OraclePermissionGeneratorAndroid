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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import net.alastairwyse.oraclepermissiongenerator.containers.RemoteDataModelProxyType;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.DataInterfaceService.DataInterfaceServiceBinder;

/**
 * Base Activity class for alternate versions of activities which do not use the Presenter class nor MVP pattern.  Provides functionality to connect to the DataInterfaceService class, execute long running processes on worker threads, handle exceptions, and to display wait dialog windows and confirmation dialog windows.
 * @author Alastair Wyse
 */
public class BaseActivity extends Activity {

    /** The interface to the data layer of the application. */
    protected IDataInterface dataInterface;
    /** The Android service connection to the data interface. */
    protected DataInterfaceServiceConnection dataInterfaceServiceConnection;
    /** Indicates whether this Activity is currently bound to the data interface. */
    protected volatile boolean dataInterfaceServiceConnected = false;
    /** Object used to log any exceptions which occur. */
    protected static IExceptionLogger exceptionLogger = new ExternalStorageExceptionLogger();
    /** Dialog which requests the user to wait. */
    protected ProgressDialog waitDialog;
    /** Dialog which contains an 'OK' button the user must click to confirm */
    protected AlertDialog okDialog;
    /** The time in milliseconds to wait between iterations of spinning loops which wait for activities to open, services to start, etc...  */
    protected final Integer spinInterval = 50;
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataInterfaceServiceConnection = new DataInterfaceServiceConnection();
    }
    
    /**
     * Shows a dialog requesting the user to wait.
     * @param  title    The title displayed in the dialog.
     * @param  message  The message displayed in the dialog.
     */
    public void ShowWaitDialog(String title, String message) {
        runOnUiThread(new ShowWaitDialogThread(title, message));
    }
    
    /**
     * Closes the dialog previously opened with the ShowWaitDialog() method.
     */
    public void CloseWaitDialog() {
        runOnUiThread(new CloseWaitDialogThread());
    }
    
    /**
     * Shows a dialog reporting a message to the user, and asking for them to confirm by clicking an 'OK' button.
     * @param  title     The title displayed in the dialog.
     * @param  message   The message displayed in the dialog.
     * @param  listener  Callback to the code to execute when the 'OK' button is clicked.
     */
    public void ShowOkDialog(String title, String message, OnClickListener listener) {
        runOnUiThread(new ShowOkDialogThread(title, message, listener));
    }
    
    /**
     * Closes the dialog previously opened with the ShowOkDialog() method.
     */
    public void CloseOkDialog() {
        runOnUiThread(new CloseOKDialogThread());
    }

    /**
     * Closes the Activity.
     */
    public void Close() {
        finish();
    }
    
    /**
     * Checks that the data interface service is connected, and if not connects.
     * @throws  InterruptedException  if this thread is interrupted while waiting for the data interface service to connect.
     */
    protected void CheckDataInterfaceServiceConnection() throws InterruptedException, Exception {
        if (dataInterfaceServiceConnected == false) {
            // Create an intent to bind to the data interface service 
            Intent dataInterfaceServiceBindIntent = new Intent(this, DataInterfaceService.class);
            bindService(dataInterfaceServiceBindIntent, dataInterfaceServiceConnection, Context.BIND_AUTO_CREATE);

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
            this.unbindService(dataInterfaceServiceConnection);
        }
    }
    
    /**
     * Sets a remote data model proxy object and associated location on the data layer.
     * @param  remoteDataModelProxyType  The type of remote data model proxy to set on the data layer.
     * @param  dataServiceLocation       The location (IP address or hostname and port) of the remote data model proxy, for example '192.168.0.101:5000'.
     */
    protected void SetDataServiceRemoteDataModelProxy(RemoteDataModelProxyType remoteDataModelProxyType, String dataServiceLocation) throws Exception {
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
     * Thread which performs the function of the ShowWaitDialog() method.
     */
    private class ShowWaitDialogThread implements Runnable {

        String title;
        String message;
        
        /**
         * Initialises a new instance of the ShowWaitDialogThread class.
         * @param  title    The title to display in the dialog.
         * @param  message  The message to display in the dialog.
         */
        public ShowWaitDialogThread(String title, String message) {
            this.title = title;
            this.message = message;
        }
        
        @Override
        public void run() {
            waitDialog = ProgressDialog.show(BaseActivity.this, title, message);
        }
    }
    
    /**
     * Thread which performs the function of the CloseWaitDialog() method.
     */
    private class CloseWaitDialogThread implements Runnable {
        
        @Override
        public void run() {
            waitDialog.dismiss();
        }
    }
    
    /**
     * Thread which performs the function of the ShowOkDialog() method.
     */
    private class ShowOkDialogThread implements Runnable {
        
        String title;
        String message;
        OnClickListener listener;
        
        /**
         * Initialises a new instance of the ShowOkDialogThread class.
         * @param  title     The title to display in the dialog.
         * @param  message   The message to display in the dialog.
         * @param  listener  Callback to the code to execute when the 'OK' button is clicked.
         */
        public ShowOkDialogThread(String title, String message, OnClickListener listener) {
            this.title = title;
            this.message = message;
            this.listener = listener;
        }
        
        @Override
        public void run() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseActivity.this);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("OK", listener);
            okDialog = alertDialogBuilder.show();
        }
    }
    
    /**
     * Thread which performs the function of the CloseWaitDialog() method.
     */
    private class CloseOKDialogThread implements Runnable {
        
        @Override
        public void run() {
            okDialog.dismiss();
        }
    }
    
    /**
     * Implementation of the ServiceConnection class to facilitate connecting to the android service which provides the data interface for the application.
     */
    private class DataInterfaceServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Cast the binder to a DataInterfaceServiceBinder, and get the DataInterfaceService from it.
            DataInterfaceServiceBinder binder = (DataInterfaceServiceBinder)service;
            DataInterfaceService dataInterface = binder.getService();
            dataInterface.Initialize();
            BaseActivity.this.dataInterface = dataInterface;
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
    protected abstract class ExceptionHandlingAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

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
        protected BaseActivity notificationDialogDisplayView;
        
        /**
         * Initialises a new instance of the ExceptionHandlingAsyncTask class.
         * @param  exceptionDialogDisplayView  The view to use to display notification to the user (i.e. to wait for the task to complete, or to alert an exception occurs when executing the doInBackground() method)..
         */
        public ExceptionHandlingAsyncTask(BaseActivity notificationDialogDisplayView) {
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
                            System.exit(1);
                        }
                    };
                    
                    // Attempt to log the exception
                    boolean exceptionLogged = exceptionLogger.LogException(notificationDialogDisplayView, doInBackgroundException);
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
}
