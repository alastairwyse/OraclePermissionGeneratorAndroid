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

import net.alastairwyse.oraclepermissiongenerator.containers.*;

/**
 * Defines the methods of an MVP pattern view, which displays a list of Oracle database role to permission mappings, and allows the user to modify them.
 * @author Alastair Wyse
 */
public interface IRoleToUserMapView extends IMvpView {

    /**
     * Populates the view with a list of role to user mappings.
     * @param  mappings  The role to user mappings.
     */
    void PopulateRoleToUserMappings(ArrayList<RoleToUserMap> mappings);
    
    /**
     * Adds a role to user mapping to the view.
     * @param  mapping  The role to user mapping to add.
     */
    void AddRoleToUserMap(RoleToUserMap mapping);
    
    /**
     * Removes a role to user mapping from the view.
     * @param  mapping  The role to user mapping to remove.
     */
    void RemoveRoleToUserMap(RoleToUserMap mapping);
}
