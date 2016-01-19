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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;

/**
 * An extension of the Activity class containing common methods used in the application.
 * @author Alastair Wyse
 */
public class BaseActivity extends Activity implements IMvpView {

    /** The presenter associated with this activity. */
    protected IPresenter presenter;
    /** Dialog which requests the user to wait. */
    protected ProgressDialog waitDialog;
    /** Dialog which contains an 'OK' button the user must click to confirm */
    protected AlertDialog okDialog;

    @Override
    public void ShowWaitDialog(String title, String message) {
        runOnUiThread(new ShowWaitDialogThread(title, message));
    }
    
    @Override
    public void CloseWaitDialog() {
        runOnUiThread(new CloseWaitDialogThread());
    }
    
    @Override
    public void ShowOkDialog(String title, String message, OnClickListener listener) {
        runOnUiThread(new ShowOkDialogThread(title, message, listener));
    }
    
    @Override
    public void CloseOkDialog() {
        runOnUiThread(new CloseOKDialogThread());
    }
    
    @Override
    public void Close() {
        finish();
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
}
