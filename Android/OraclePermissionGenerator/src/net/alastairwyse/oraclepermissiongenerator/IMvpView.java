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

import android.content.DialogInterface.OnClickListener;

/**
 * Defines common methods of MVP pattern views within the application. 
 * @author Alastair Wyse
 */
public interface IMvpView {
    
    /**
     * Shows a dialog requesting the user to wait.
     * @param  title    The title displayed in the dialog.
     * @param  message  The message displayed in the dialog.
     */
    void ShowWaitDialog(String title, String message);

    /**
     * Closes the dialog previously opened with the ShowWaitDialog() method.
     */
    void CloseWaitDialog();
    
    /**
     * Shows a dialog reporting a message to the user, and asking for them to confirm by clicking an 'OK' button.
     * @param  title     The title displayed in the dialog.
     * @param  message   The message displayed in the dialog.
     * @param  listener  Callback to the code to execute when the 'OK' button is clicked.
     */
    void ShowOkDialog(String title, String message, OnClickListener listener);
    
    /**
     * Closes the dialog previously opened with the ShowOkDialog() method.
     */
    void CloseOkDialog();
    
    /**
     * Closes the view.
     */
    void Close();
}
