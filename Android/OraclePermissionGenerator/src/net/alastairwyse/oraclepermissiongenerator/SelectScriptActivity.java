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

import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.ScriptType;

/**
 * An activity which displays a list of script types the application can generate, and allows a user to select one in order to generate the script. 
 * @author Alastair Wyse
 */
public class SelectScriptActivity extends BaseActivity implements ISelectScriptView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_script_activity);  

        presenter = PresenterGlobalStorer.getPresenter();
        presenter.setSelectScriptView(this);
    }
    
    @Override
    public void onBackPressed() {
        presenter.setSelectScriptView(null);
        super.onBackPressed();
    }
    
    @Override
    public void onDestroy() {
        presenter.setSelectScriptView(null);
        super.onDestroy();
    }
    
    /**
     * Method called when the 'Privilege Rollout' field is clicked.
     * @param  view  The widget that was clicked.
     */
    public void PrivilegeRolloutFieldOnClickListener(View view) {
        presenter.GeneratePrivilegeScript(ScriptType.Rollout, true);
    }
    
    /**
     * Method called when the 'Synonym Rollout' field is clicked.
     * @param  view  The widget that was clicked.
     */
    public void SynonymRolloutFieldOnClickListener(View view) {
        presenter.GenerateSynonymScript(ScriptType.Rollout);
    }
    
    /**
     * Method called when the 'Privilege Rollback' field is clicked.
     * @param  view  The widget that was clicked.
     */
    public void PrivilegeRollbackFieldOnClickListener(View view) {
        presenter.GeneratePrivilegeScript(ScriptType.Rollback, true);
    }
    
    /**
     * Method called when the 'Synonym Rollback' field is clicked.
     * @param  view  The widget that was clicked.
     */
    public void SynonymRollbackFieldOnClickListener(View view) {
        presenter.GenerateSynonymScript(ScriptType.Rollback);
    }
    
    
}
