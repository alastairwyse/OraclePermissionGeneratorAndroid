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

package net.alastairwyse.oraclepermissiongenerator.containers.unittests;

import net.alastairwyse.oraclepermissiongenerator.containers.LocalSettings;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

/**
 * Extension of the mockito ArgumentMatcher class which allows LocalSettings classes to be compared when used as parameters in mockito verify() and when() method calls.
 * @author Alastair Wyse
 */
public class LocalSettingsMatcher extends ArgumentMatcher<LocalSettings> {
    
    private LocalSettings localSettingsToMatch;

    /**
     * Initialises a new instance of the LocalSettingsMatcher class.
     * @param  localSettingsToMatch  The LocalSettings class that should match the parameter passed to the mockito verify() or when() method call.
     */
    public LocalSettingsMatcher(LocalSettings localSettingsToMatch) {
        this.localSettingsToMatch = localSettingsToMatch;
    }
    
    @Override
    public boolean matches(Object argument) {
        if (argument.getClass() != localSettingsToMatch.getClass()) {
            return false;
        }
        
        LocalSettings comparisonLocalSettings = (LocalSettings)argument;
        if ( localSettingsToMatch.getUserIdentifier().equals(comparisonLocalSettings.getUserIdentifier()) == false ||
             localSettingsToMatch.getRemoteDataModelProxyType() != comparisonLocalSettings.getRemoteDataModelProxyType() ||
             localSettingsToMatch.getSoapDataServiceLocation().equals(comparisonLocalSettings.getSoapDataServiceLocation()) == false ||
             localSettingsToMatch.getRestDataServiceLocation().equals(comparisonLocalSettings.getRestDataServiceLocation()) == false) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void describeTo(Description description) {
        // This method is typically used in mockito when a test fails, to report the value that was wanted but not matched.  E.g. this implementation will return...
        //   "LocalSettings([userIdentifier], [remoteDataModelProxyType], [soapDataServiceLocation], [restDataServiceLocation])"
        // ... which will typically be written to the mockito failure trace.
        
        description.appendText(localSettingsToMatch.getClass().getSimpleName() + "(" + localSettingsToMatch.getUserIdentifier().toString() + ", " + localSettingsToMatch.getRemoteDataModelProxyType().name() + ", " + localSettingsToMatch.getSoapDataServiceLocation().toString() + ", " + localSettingsToMatch.getRestDataServiceLocation().toString() + ")");
    }
}