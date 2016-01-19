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

package net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.unittests;

import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.SoapRemoteDataModelProxy;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

/**
 * Extension of the mockito ArgumentMatcher class which allows SoapRemoteDataModelProxy classes to be compared when used as parameters in mockito verify() and when() method calls.
 * @author Alastair Wyse
 */
public class SoapRemoteDataModelProxyMatcher extends ArgumentMatcher<SoapRemoteDataModelProxy> {

    private SoapRemoteDataModelProxy soapRemoteDataModelProxyToMatch;
    
    /**
     * Initialises a new instance of the SoapRemoteDataModelProxyMatcher class.
     * @param  soapRemoteDataModelProxyToMatch  The SoapRemoteDataModelProxy class that should match the parameter passed to the mockito verify() or when() method call.
     */
    public SoapRemoteDataModelProxyMatcher(SoapRemoteDataModelProxy soapRemoteDataModelProxyToMatch) {
        this.soapRemoteDataModelProxyToMatch = soapRemoteDataModelProxyToMatch;
    }
    
    @Override
    public boolean matches(Object argument) {
        if (argument.getClass() != soapRemoteDataModelProxyToMatch.getClass()) {
            return false;
        }
        
        SoapRemoteDataModelProxy comparisonSoapRemoteDataModelProxy = (SoapRemoteDataModelProxy)argument;
        if (soapRemoteDataModelProxyToMatch.getDataModelLocation().equals(comparisonSoapRemoteDataModelProxy.getDataModelLocation()) == false) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void describeTo(Description description) {
        // This method is typically used in mockito when a test fails, to report the value that was wanted but not matched.  E.g. this implementation will return...
        //   "SoapRemoteDataModelProxy([dataModelLocation])"
        // ... which will typically be written to the mockito failure trace.
        
        description.appendText(soapRemoteDataModelProxyToMatch.getClass().getSimpleName() + "(" + soapRemoteDataModelProxyToMatch.getDataModelLocation().toString() + ")");
    }
}
