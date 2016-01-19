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

import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * Provides a link/binding between a list of OracleObjectPermissionSet objects and a ListView.
 * <b>Note</b> the constructor of this class requires an android.view.View.OnClickListener object, which is the click handler for the object name and type text views.  Am using this method for intercepting clicks on rows of Oracle object ListView, as I couldn't get it to respond to clicks by setting an OnItemClickListener class on the ListView itself.
 * @author Alastair Wyse
 */
public class OracleObjectArrayAdapter extends ArrayAdapter<OracleObjectPermissionSet> {

    private Context context;
    private int rowLayoutResourceId;
    private int objectNameFieldId;
    private int objectTypeFieldId;
    private int addCheckBoxViewId;
    private int removeCheckBoxViewId;
    private OnClickListener clickHandler;
    
    /**
     * Initialises a new instance of the OracleObjectArrayAdapter class.
     * @param  context               The current context.
     * @param  rowLayoutResourceId   The resource ID for the layout for each row in the list view. 
     * @param  objectNameFieldId     The view id for the TextView which displays the object name.
     * @param  objectTypeFieldId     The view id for the TextView which displays the object type.
     * @param  addCheckBoxViewId     The view id for the CheckBox which shows whether the object should be added.
     * @param  removeCheckBoxViewId  The view id for the CheckBox which shows whether the object should be removed.
     * @param  listData              The list of OracleObjectPermissionSet objects to display in the ListView.
     * @param  clickHandler          The class which should handle clicks on the object name or type TextViews
     */
    public OracleObjectArrayAdapter(Context context, int rowLayoutResourceId, int objectNameFieldId, int objectTypeFieldId, int addCheckBoxViewId, int removeCheckBoxViewId, ArrayList<OracleObjectPermissionSet> listData, OnClickListener clickHandler) {
        super(context, rowLayoutResourceId, listData);
        this.context = context;
        this.rowLayoutResourceId = rowLayoutResourceId;
        this.objectNameFieldId = objectNameFieldId;
        this.objectTypeFieldId = objectTypeFieldId;
        this.addCheckBoxViewId = addCheckBoxViewId;
        this.removeCheckBoxViewId = removeCheckBoxViewId;
        this.clickHandler = clickHandler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout of the row, and get references to the views inside the row
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View returnRowView = inflater.inflate(rowLayoutResourceId, parent, false);
        TextView objectNameView = (TextView)returnRowView.findViewById(objectNameFieldId);
        TextView objectTypeView = (TextView)returnRowView.findViewById(objectTypeFieldId);
        CheckBox addCheckBox = (CheckBox)returnRowView.findViewById(addCheckBoxViewId);
        CheckBox removeCheckBox = (CheckBox)returnRowView.findViewById(removeCheckBoxViewId);

        // Set the properties and values of the views based on the relevant OracleObjectPermissionSet object
        OracleObjectPermissionSet currentObject = getItem(position);
        objectNameView.setText(currentObject.getObjectName());
        objectTypeView.setText(currentObject.getObjectType());
        addCheckBox.setChecked(currentObject.getAddFlag());
        removeCheckBox.setChecked(currentObject.getRemoveFlag());

        // Set click handlers on the relevant views
        objectNameView.setOnClickListener(clickHandler);
        objectTypeView.setOnClickListener(clickHandler);
        // Set the text views as long-clickable (this appears to pass the long click to the parent to handle... i.e. to the Oracle object ListView)
        objectNameView.setLongClickable(true);
        objectTypeView.setLongClickable(true);
        
        return returnRowView;
    }
}
