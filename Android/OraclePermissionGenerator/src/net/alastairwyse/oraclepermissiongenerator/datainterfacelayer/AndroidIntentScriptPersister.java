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

package net.alastairwyse.oraclepermissiongenerator.datainterfacelayer;

import android.content.Context;
import android.content.Intent;

/**
 * Sends an Oracle script to another application via an Android Intent.
 * @author Alastair Wyse
 */
public class AndroidIntentScriptPersister implements IScriptPersister {

    private final String intentMimeType = "text/plain";
    private Context context;
    
    /**
     * Initialises a new instance of the AndroidIntentScriptPersister class.
     * @param  context  The context under which the AndroidIntentScriptPersister is being initialized.
     */
    public AndroidIntentScriptPersister(Context context) {
        this.context = context;
    }
    
    @Override
    public void Write(String scriptText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, scriptText);
        sendIntent.setType(intentMimeType);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }

}
