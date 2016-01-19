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

import net.alastairwyse.oraclepermissiongenerator.containers.OracleObjectPermissionSet;

/**
 * Defines the methods of an MVP pattern view, which displays a list of Oracle objects. 
 * @author Alastair Wyse
 */
public interface IObjectListView extends IMvpView {

    /**
     * Adds an Oracle database object to the list.
     * @param  oracleObject  The Oracle database object to add.
     */
    public void AddObject(OracleObjectPermissionSet oracleObject);
    
    /**
     * Populates the view with a set of Oracle database object.
     * @param  oracleObjects  The Oracle database objects to populate.
     */
    public void PopulateObjects(ArrayList<OracleObjectPermissionSet> oracleObjects);
    
    /**
     * Removes an Oracle database object from the list.
     * @param  objectName  The name of the Oracle database object to remove.
     */
    public void RemoveObject(String objectName);
    
    /**
     * Clears all Oracle database objects from the list.
     */
    public void ClearObjects();
}
