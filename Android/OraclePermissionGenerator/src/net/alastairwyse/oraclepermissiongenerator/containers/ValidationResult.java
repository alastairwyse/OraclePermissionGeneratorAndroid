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
 * Contains the results of validating a property against the data interface layer.
 * @author Alastair Wyse
 */
public class ValidationResult {

    private boolean isValid;
    private String validationError;
    
    /**
     * @return  Whether the validation was successful.
     */
    public boolean getIsValid() {
        return isValid;
    }
    
    /**
     * @return  The error message generated in the case that the validation failed.
     */
    public String getValidationError() {
        return validationError;
    }
    
    /**
     * Initialises a new instance of the ValidationResult class.
     * @param  isValid          Whether the validation was successful.
     * @param  validationError  The error message generated in the case that the validation failed.
     */
    public ValidationResult (boolean isValid, String validationError) {
        this.isValid = isValid;
        this.validationError = validationError;
    }
}
