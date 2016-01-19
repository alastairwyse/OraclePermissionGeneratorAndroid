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

/**
 * Defines the methods of an MVP pattern view, which displays a list of permissions associated with an Oracle object and role, and allows the user to set them.
 * @author Alastair Wyse
 */
public interface ISetPermissionsView extends IMvpView {

    /**
     * @param  objectName  The name of the object to set the permissions for.
     */
    void setObjectName(String objectName);

    /**
     * @param  role  The role to set the permissions for.
     */
    void setRole(String role);
    
    /**
     * Populates the view with a list of permissions
     * @param   objectPermissions  A list of the permissions set for the Oracle object.
     * @param   allPermissions     A list of all the available permissions for the Oracle object.
     */
    void PopulatePermissions(ArrayList<String> objectPermissions, ArrayList<String> allPermissions);
}
