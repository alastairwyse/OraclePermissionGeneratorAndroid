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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * Provides a link/binding between a list of RoleToPermissionMap objects and a ListView.
 * @author Alastair Wyse
 */
public class RoleToUserMapArrayAdapter extends ArrayAdapter<RoleToUserMap> {

    private Context context;
    private int rowLayoutResourceId;
    private int roleFieldId;
    private int userFieldId;
    
    /**
     * Initialises a new instance of the RoleToUserMapArrayAdapter class.
     * @param  context              The current context.
     * @param  rowLayoutResourceId  The resource ID for the layout for each row in the list view. 
     * @param  roleFieldId          The view id for the TextView which displays the role.
     * @param  userFieldId          The view id for the TextView which displays the user.
     * @param  listData             The list of RoleToPermissionMap objects to display in the ListView.
     */
    public RoleToUserMapArrayAdapter(Context context, int rowLayoutResourceId, int roleFieldId, int userFieldId, ArrayList<RoleToUserMap> listData) {
        super(context, rowLayoutResourceId, listData);
        this.context = context;
        this.rowLayoutResourceId = rowLayoutResourceId;
        this.roleFieldId = roleFieldId;
        this.userFieldId = userFieldId;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout of the row, and get references to the views inside the row
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View returnRowView = inflater.inflate(rowLayoutResourceId, parent, false);
        TextView roleView = (TextView)returnRowView.findViewById(roleFieldId);
        TextView userView = (TextView)returnRowView.findViewById(userFieldId);

        // Set the properties and values of the views based on the relevant RoleToUserMap object
        RoleToUserMap currentObject = getItem(position);
        roleView.setText(currentObject.getRole());
        userView.setText(currentObject.getUser());

        // Set the text views as long-clickable (this appears to pass the long click to the parent to handle... i.e. to the role to user map ListView)
        roleView.setLongClickable(true);
        userView.setLongClickable(true);
        
        return returnRowView;
    }
}
