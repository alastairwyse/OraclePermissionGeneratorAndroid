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

import java.util.*;

/**
 * A set of permissions relating to an object in an Oracle database.
 * @author Alastair Wyse
 */
public class OracleObjectPermissionSet {

    private String objectName;
    private String objectType;
    private String objectOwner;
    private ArrayList<RoleToPermissionMap> objectPermissions;
    private boolean addFlag;
    private boolean removeFlag;
    
    /**
     * @return  The name of the Oracle object that the set of permissions apply to.
     */
    public String getObjectName() {
        return objectName;
    }
    
    /**
     * @param objectName  The name of the Oracle object that the set of permissions apply to.
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName.toUpperCase(Locale.US);
    }
    
    /**
     * @return  The type of the Oracle object.
     */
    public String getObjectType() {
        return objectType;
    }
    
    /**
     * @param objectType  The type of the Oracle object.
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    
    /**
     * @return  The user which owns the Oracle object.
     */
    public String getObjectOwner() {
        return objectOwner;
    }
    
    /**
     * @param objectOwner  The user which owns the Oracle object.
     */
    public void setObjectOwner(String objectOwner) {
        this.objectOwner = objectOwner.toUpperCase(Locale.US);
    }
    
    /**
     * @return  A collection of permissions related to the object.
     */
    public ArrayList<RoleToPermissionMap> getObjectPermissions() {
        return objectPermissions;
    }

    /**
     * @param objectPermissions  A collection of permissions related to the object.
     */
    public void setObjectPermissions(ArrayList<RoleToPermissionMap> objectPermissions) {
        this.objectPermissions = objectPermissions;
    }

    /**
     * @return  Whether add statements are to be included when generating the set of permissions.
     */
    public boolean getAddFlag() {
        return addFlag;
    }

    /**
     * @param  addFlag  Whether add statements are to be included when generating the set of permissions.
     */
    public void setAddFlag(boolean addFlag) {
        this.addFlag = addFlag;
    }

    /**
     * @return  Whether remove statements are to be included when generating the set of permissions.
     */
    public boolean getRemoveFlag() {
        return removeFlag;
    }

    /**
     * @param removeFlag  Whether remove statements are to be included when generating the set of permissions.
     */
    public void setRemoveFlag(boolean removeFlag) {
        this.removeFlag = removeFlag;
    }

    /**
     * Initialises a new instance of the OracleObjectPermissionSet class.
     */
    public OracleObjectPermissionSet() {
        objectPermissions = new ArrayList<RoleToPermissionMap>();
    }

    /**
     * Initialises a new instance of the OracleObjectPermissionSet class.
     * @param objectName  The name of the Oracle object that the set of permissions apply to.
     */
    public OracleObjectPermissionSet(String objectName) {
        this();
        setObjectName(objectName);
    }
}
