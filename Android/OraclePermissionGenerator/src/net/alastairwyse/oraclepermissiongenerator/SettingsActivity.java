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

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * An activity which displays and allows modifying of the general settings of the application. 
 * @author Alastair Wyse
 */
public class SettingsActivity extends BaseActivity implements ISettingsView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);  
        
        presenter = PresenterGlobalStorer.getPresenter();
        presenter.setSettingsView(this);
    }

    @Override
    public void onBackPressed() {
        presenter.setSettingsView(null);
        super.onBackPressed();
    }
    
    @Override
    public void onDestroy() {
        presenter.setSettingsView(null);
        super.onDestroy();
    }
    
    @Override
    public void PopulateDefaultObjectOwner(String defaultObjectOwner) {
        EditText defaultObjectOwnerField = (EditText)findViewById(R.id.settings_default_object_owner_field);
        defaultObjectOwnerField.setText(defaultObjectOwner);
    }

    /**
     * Method called when the 'Save' button is clicked.
     * @param  view  The widget that was clicked.
     */
    public void SaveButtonOnClickListener(View view) {
        String defaultObjectOwner = ((EditText)findViewById(R.id.settings_default_object_owner_field)).getText().toString().trim();
        presenter.SaveSettings(defaultObjectOwner);
    }
}
