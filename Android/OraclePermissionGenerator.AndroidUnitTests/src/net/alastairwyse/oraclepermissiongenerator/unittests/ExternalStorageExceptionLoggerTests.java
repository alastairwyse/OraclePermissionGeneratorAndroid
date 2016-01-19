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

package net.alastairwyse.oraclepermissiongenerator.unittests;

import android.content.Context;
import android.os.Environment;
import android.test.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import net.alastairwyse.oraclepermissiongenerator.*;
import net.alastairwyse.oraclepermissiongenerator.frameworkabstraction.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for class oraclepermissiongenerator.ExternalStorageExceptionLogger.
 * @author Alastair Wyse
 */
public class ExternalStorageExceptionLoggerTests extends AndroidTestCase {
    
    private ExternalStorageExceptionLogger testExternalStorageExceptionLogger;
    private Context mockContext;
    private IEnvironment mockEnvironment;
    private FileWriter mockFileWriter;
    private IDateProvider mockDateProvider;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        mockContext = mock(Context.class);
        mockEnvironment = mock(IEnvironment.class);
        mockFileWriter = mock(FileWriter.class);
        mockDateProvider = mock(IDateProvider.class);
        testExternalStorageExceptionLogger = new ExternalStorageExceptionLogger(mockContext, mockEnvironment, mockFileWriter, mockDateProvider);
    }
    
    public void testLogExceptionSuccessTest() throws Exception {
        Date testDate = new Date(2015, 7, 10);
        testDate.setHours(18);
        testDate.setMinutes(24);
        testDate.setSeconds(26);
        Exception testException = new Exception("Test Exception");
        StringWriter tempStringWriter = new StringWriter();
        PrintWriter tempPrintWriter = new PrintWriter(tempStringWriter);
        testException.printStackTrace(tempPrintWriter);
        
        when(mockEnvironment.getExternalStorageState()).thenReturn(Environment.MEDIA_MOUNTED);
        when(mockContext.getExternalFilesDir(null)).thenReturn(new File("/mnt/sdcard/Android/data/net.alastairwyse.oraclepermissiongenerator/files"));
        when(mockDateProvider.getDate()).thenReturn(testDate);
        
        boolean result = testExternalStorageExceptionLogger.LogException(testException);

        verify(mockEnvironment).getExternalStorageState();
        verify(mockFileWriter).write(tempStringWriter.toString());
        verify(mockFileWriter).flush();
        verify(mockFileWriter).close();
        assertEquals(true, result);
        verifyNoMoreInteractions(mockEnvironment, mockContext, mockDateProvider, mockFileWriter);
    }
    
    public void testLogExceptionMediaUnmounted() throws Exception {
        Exception testException = new Exception("Test Exception");
        
        when(mockEnvironment.getExternalStorageState()).thenReturn(Environment.MEDIA_UNMOUNTED);
        
        boolean result = testExternalStorageExceptionLogger.LogException(testException);
        
        assertEquals(false, result);
    }
    
    public void testLogExceptionExceptionDuringWrite() throws Exception {
        Date testDate = new Date(2015, 7, 10);
        testDate.setHours(18);
        testDate.setMinutes(24);
        testDate.setSeconds(26);
        Exception testException = new Exception("Test Exception");
        StringWriter tempStringWriter = new StringWriter();
        PrintWriter tempPrintWriter = new PrintWriter(tempStringWriter);
        testException.printStackTrace(tempPrintWriter);
        
        when(mockEnvironment.getExternalStorageState()).thenReturn(Environment.MEDIA_MOUNTED);
        when(mockContext.getExternalFilesDir(null)).thenReturn(new File("/mnt/sdcard/Android/data/net.alastairwyse.oraclepermissiongenerator/files"));
        when(mockDateProvider.getDate()).thenReturn(testDate);

        doThrow(new IOException("Mock IOException.")).when(mockFileWriter).write(tempStringWriter.toString());
        boolean result = testExternalStorageExceptionLogger.LogException(testException);
        
        assertEquals(false, result);
    }
}
