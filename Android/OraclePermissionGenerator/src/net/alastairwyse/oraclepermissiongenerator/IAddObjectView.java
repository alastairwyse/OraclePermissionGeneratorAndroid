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
 * Defines the methods of an MVP pattern view, which allows adding a new Oracle object to the application. 
 * @author Alastair Wyse
 */
public interface IAddObjectView extends IMvpView {

    /**
     * Populates the view with a list of available object types.
     * @param  objectTypes  A list of all available object types.
     */
    void PopulateObjectTypes(ArrayList<String> objectTypes);

    /**
     * Populates the view with the object owner.
     * @param  objectOwner  The object owner.
     */
    void PopulateObjectOwner(String objectOwner);
}
