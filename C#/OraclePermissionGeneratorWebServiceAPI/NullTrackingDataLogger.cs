/*
 * Copyright 2015 Alastair Wyse (http://www.oraclepermissiongenerator.net/methodinvocationremotingandroid/)
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

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OraclePermissionGeneratorWebServiceAPI.Containers;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Implementation of interface ITrackingDataLogger which does not perform any logging.
    /// </summary>
    class NullTrackingDataLogger : ITrackingDataLogger
    {
        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.NullTrackingDataLogger class.
        /// </summary>
        public NullTrackingDataLogger()
        {
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ITrackingDataLogger.Log(System.DateTime,System.String,System.String,OraclePermissionGeneratorWebServiceAPI.Location,System.Byte[])"]/*'/>
        public void Log(DateTime timeStamp, string userIdentifier, string methodName, Location location, byte[] ipV4Address)
        {
        }
    }
}
