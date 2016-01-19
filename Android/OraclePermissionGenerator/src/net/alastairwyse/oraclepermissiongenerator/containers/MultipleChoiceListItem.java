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

package net.alastairwyse.oraclepermissiongenerator.containers;

/**
 * Container object used to store a string a boolean which represents whether the value described by the string is selected.  Used to store data backing a ListView which is designed to replicate the 'Multiple Choice List' in the Android API Demos (API Demos &gt; Views &gt; Lists &gt; 11. Multiple Choice List).
 * @author Alastair Wyse
 */
public class MultipleChoiceListItem {

    private String listItemLabel;
    private boolean itemSelected;
    
    /**
     * @param  value  The label of the item in the list.
     */
    public void setListItemLabel(String value) {
        listItemLabel = value;
    }
    
    /**
     * @return  The label of the item in the list.
     */
    public String getListItemLabel() {
        return listItemLabel;
    }
    
    /**
     * @param  value  Whether the item in the list is selected.
     */
    public void setItemSelected(boolean value) {
        itemSelected = value;
    }
    
    /**
     * @return  Whether the item in the list is selected.
     */
    public boolean getItemSelected() {
        return itemSelected;
    }
    
    /**
     * Initialises a new instance of the MultipleChoiceListItem class.
     * @param  listItemLabel  The label of the item in the list.
     * @param  itemSelected   Whether the item in the list is selected.
     */
    public MultipleChoiceListItem(String listItemLabel, boolean itemSelected) {
        this.listItemLabel = listItemLabel;
        this.itemSelected = itemSelected;
    }
}
