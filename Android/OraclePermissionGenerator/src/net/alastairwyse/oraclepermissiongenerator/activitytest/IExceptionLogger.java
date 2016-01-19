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

package net.alastairwyse.oraclepermissiongenerator.activitytest;

import android.content.Context;

/**
 * Defines methods to allow the storing / logging of exceptions which occur during the use of the application.
 * @author Alastair Wyse
 */
public interface IExceptionLogger {

    /**
     * Stores / logs details of the specified exception. 
     * @param   context    The context for which the exception is being logged.
     * @param   exception  The exception to log.
     * @return             Whether the logging was successful or not.
     */
    boolean LogException(Context context, Exception exception);
}
