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

/**
 * Provides a link/binding between a list of strings and a ListView.
 * @author Alastair Wyse
 */
public class StringArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private int rowLayoutResourceId;
    private int stringFieldId;
    
    /**
     * Initialises a new instance of the StringArrayAdapter class.
     * @param  context              The current context.
     * @param  rowLayoutResourceId  The resource id for the layout for each row in the list view. 
     * @param  stringFieldId        The view id for the TextView which displays the string value.
     * @param  listData             The list of strings to display in the ListView.
     */
    public StringArrayAdapter(Context context, int rowLayoutResourceId, int stringFieldId, ArrayList<String> listData) {
        super(context, rowLayoutResourceId, listData);
        this.context = context;
        this.rowLayoutResourceId = rowLayoutResourceId;
        this.stringFieldId = stringFieldId;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout of the row, and get references to the views inside the row
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View returnRowView = inflater.inflate(rowLayoutResourceId, parent, false);
        TextView stringFieldView = (TextView)returnRowView.findViewById(stringFieldId);
        
        // Set the TextView's value based on the current item in the list
        String currentFieldValue = getItem(position);
        stringFieldView.setText(currentFieldValue);

        return returnRowView;
    }
}
