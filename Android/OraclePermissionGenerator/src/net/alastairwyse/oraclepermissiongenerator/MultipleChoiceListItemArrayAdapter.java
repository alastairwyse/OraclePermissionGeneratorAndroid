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
import android.widget.CheckBox;
import android.widget.TextView;

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * Provides a link/binding between a list of MultipleChoiceListItem objects and a ListView.  This class is designed to be the array adapter for a generic Android ListView resembling the 'Multiple Choice List' ListView in the Android API Demos (API Demos &gt; Views &gt; Lists &gt; 11. Multiple Choice List).
 * @author Alastair Wyse
 */
public class MultipleChoiceListItemArrayAdapter extends ArrayAdapter<MultipleChoiceListItem> {

    private Context context;
    private int rowLayoutResourceId;
    private int itemNameFieldId;
    private int itemSelectedCheckBoxViewId;
 
    /**
     * Initialises a new instance of the MultipleChoiceListItemArrayAdapter class.
     * @param  context                     The current context.
     * @param  rowLayoutResourceId         The resource ID for the layout for each row in the list view. 
     * @param  itemNameFieldId             The view id for the TextView which displays the text of the row.
     * @param  itemSelectedCheckBoxViewId  The view id for the CheckBox in each row.
     * @param  listData                    The list of MultipleChoiceListItem objects to display in the ListView.
     */
    public MultipleChoiceListItemArrayAdapter(Context context, int rowLayoutResourceId, int itemNameFieldId, int itemSelectedCheckBoxViewId, ArrayList<MultipleChoiceListItem> listData) {
        super(context, rowLayoutResourceId, listData);
        this.context = context;
        this.rowLayoutResourceId = rowLayoutResourceId;
        this.itemNameFieldId = itemNameFieldId;
        this.itemSelectedCheckBoxViewId = itemSelectedCheckBoxViewId;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout of the row, and get references to the views inside the row
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View returnRowView = inflater.inflate(rowLayoutResourceId, parent, false);
        TextView itemNameFieldView = (TextView)returnRowView.findViewById(itemNameFieldId);
        CheckBox itemSelectedCheckBox = (CheckBox)returnRowView.findViewById(itemSelectedCheckBoxViewId);
        
        // Set the properties and values of the views based on the relevant MultipleChoiceListItem object
        MultipleChoiceListItem currentItem = getItem(position);
        itemNameFieldView.setText(currentItem.getListItemLabel());
        itemSelectedCheckBox.setChecked(currentItem.getItemSelected());
        
        return returnRowView;
    }
}
