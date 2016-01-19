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
 * Defines a mapping between two string values.
 * @author Alastair Wyse
 */
public abstract class StringMap {

    protected int stringMinLength;
    protected int stringMaxLength;
    
    /** The name of the type of entity of the 'mapped from' value */
    protected String mappedFromEntityName;
    /** The name of the type of entity of the 'mapped to' value */
    protected String mappedToEntityName;
    
    protected String mappedFrom;
    protected String mappedTo;
    
    /**
     * Initialises a new instance of the StringMap class.
     * @param stringMinLength       The minimum allowed string length .
     * @param stringMaxLength       The maximum allowed string length.
     * @param mappedFromEntityName  The name of the type of entity of the 'mapped from' value.
     * @param mappedToEntityName    The name of the type of entity of the 'mapped to' value.
     */
    protected StringMap(int stringMinLength, int stringMaxLength, String mappedFromEntityName, String mappedToEntityName) {
        this.stringMinLength = stringMinLength;
        this.stringMaxLength = stringMaxLength;
        this.mappedFromEntityName = mappedFromEntityName;
        this.mappedToEntityName = mappedToEntityName;
    }
    
    /**
     * Sets the 'from' value in the mapping.
     * @param mappedFrom  The 'from' value.
     */
    protected void setMappedFrom(String mappedFrom) {
        if (mappedFrom.length() < stringMinLength || mappedFrom.length() > stringMaxLength){
            throw new IllegalArgumentException("Argument '" + mappedFromEntityName + "' must be between " + stringMinLength + " and " + stringMaxLength + " characters in length.");
        }
        else{
            this.mappedFrom = mappedFrom;
        }
    }
    
    /**
     * Sets the 'to' value in the mapping.
     * @param mappedTo  The 'to' value.
     */
    protected void setMappedTo(String mappedTo) {
        if (mappedTo.length() < stringMinLength || mappedTo.length() > stringMaxLength){
            throw new IllegalArgumentException("Argument '" + mappedToEntityName + "' must be between " + stringMinLength + " and " + stringMaxLength + " characters in length.");
        }
        else{
            this.mappedTo = mappedTo;
        }
    }
}
