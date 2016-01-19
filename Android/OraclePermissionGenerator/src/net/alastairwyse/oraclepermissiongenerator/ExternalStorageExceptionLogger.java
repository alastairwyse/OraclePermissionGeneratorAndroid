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

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.os.Environment;

import net.alastairwyse.oraclepermissiongenerator.frameworkabstraction.*;

/**
 * Logs exceptions to a file in the external storage of the Android device.
 * @author Alastair Wyse
 */
public class ExternalStorageExceptionLogger implements IExceptionLogger {

    private final String fileNameDateFormat = "yyyy-MM-dd_HH-mm-ss";
    private final String fileNamePostFix = "Exception.log";
    private Context context;
    private IEnvironment environment;
    private FileWriter exceptionLogFileWriter;
    private IDateProvider dateProvider;
    private boolean testConstructor = false;
    
    /**
     * Initialises a new instance of the ExternalStorageExceptionLogger class.
     * @param  context  The context under which the exception logger is being initialized.
     */
    public ExternalStorageExceptionLogger(Context context) {
        this.context = context;
        environment = new net.alastairwyse.oraclepermissiongenerator.frameworkabstraction.Environment();
        dateProvider = new DateProvider();
    }
    
    /**
     * Initialises a new instance of the ExternalStorageExceptionLogger class.
     * <b>Note</b> this is an additional constructor to facilitate unit tests, and should not be used to instantiate the class under normal conditions.
     * @param  context                 A test (mock) Context object 
     * @param  environment             A test (mock) IEnvironment object 
     * @param  exceptionLogFileWriter  A test (mock) IFileWriter object
     * @param  dateProvider            A test (mock) IDateProvider object 
     */
    public ExternalStorageExceptionLogger(Context context, IEnvironment environment, FileWriter exceptionLogFileWriter, IDateProvider dateProvider) {
        this(context);
        this.exceptionLogFileWriter = exceptionLogFileWriter;
        this.environment = environment;
        this.dateProvider = dateProvider;
        testConstructor = true;
    }
    
    @Override
    public boolean LogException(Exception exception) {
        // Check whether the device's external storage is available
        if (environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) == false) {
            return false;
        }
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fileNameDateFormat);
            if(testConstructor == false) {
                File exceptionLogFile = new File(context.getExternalFilesDir(null), dateFormat.format(dateProvider.getDate()) + "_" + fileNamePostFix);
                exceptionLogFile.createNewFile();
                exceptionLogFileWriter = new FileWriter(exceptionLogFile, false);
            }
            StringWriter tempStringWriter = new StringWriter();
            PrintWriter tempPrintWriter = new PrintWriter(tempStringWriter);
            // TODO: If project compliance is changed to Java 1.7 or higher, change below to try with resources statement
            try {
                exception.printStackTrace(tempPrintWriter);
                exceptionLogFileWriter.write(tempStringWriter.toString());
                exceptionLogFileWriter.flush();
            }
            catch(Exception e) {
                throw e;
            }
            finally {
                tempPrintWriter.close();
                tempStringWriter.close();
                exceptionLogFileWriter.close();
            }
        }
        catch (Exception e) {
            // If any problem is encountered logging the exception, just return false.  Let client then decide how to deal with the exception.
            return false;
        }
        
        return true;
    }
}
