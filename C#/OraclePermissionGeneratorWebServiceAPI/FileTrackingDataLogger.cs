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
using System.IO;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Temporary implementation of the ITrackingDataLogger to log tracking data to files (for testing during development).
    /// </summary>
    class FileTrackingDataLogger : ITrackingDataLogger, IDisposable
    {
        const String fileNameDateFormat = "yyyy-MM-dd HH-mm-ss";
        const String logEntryDateFormat = "yyyy-MM-dd HH:mm:ss.fff";

        /// <summary>Indicates whether the object has been disposed.</summary>
        protected bool disposed = false;
        private StreamWriter streamWriter;
        private Encoding fileEncoding = Encoding.UTF8;

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.FileTrackingDataLogger class.
        /// </summary>
        /// <param name="filePath">The path to write the file to (including trailing '\' character).</param>
        public FileTrackingDataLogger(String filePath)
        {
            String fullFilePath = filePath + "OPGWebServiceAPI Tracking Data " + DateTime.Now.ToString(fileNameDateFormat) + ".log";
            streamWriter = new StreamWriter(fullFilePath, false, fileEncoding);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ITrackingDataLogger.Log(System.DateTime,System.String,System.String,OraclePermissionGeneratorWebServiceAPI.Location,System.Byte[])"]/*'/>
        public void Log(DateTime timeStamp, string userIdentifier, string methodName, Containers.Location location, byte[] ipV4Address)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.Append(timeStamp.ToString(logEntryDateFormat) + " | ");
            stringBuilder.Append(userIdentifier + " | ");
            stringBuilder.Append(methodName + " | ");
            if (location != null)
            {
                stringBuilder.Append(location.Latitude + ", " + location.Longitude + ", " + location.SecondsSinceUpdate + " | ");
            }
            else
            {
                stringBuilder.Append(" | ");
            }
            if (ipV4Address != null)
            {
                for (int i = 0; i <= 3; i++)
                {
                    stringBuilder.Append(ipV4Address[i]);
                    if (i != 3)
                    {
                        stringBuilder.Append(".");
                    }
                }
            }

            streamWriter.WriteLine(stringBuilder.ToString());
            streamWriter.Flush();
        }

        #region Finalize / Dispose Methods

        /// <summary>
        /// Releases the unmanaged resources used by the FileTrackingDataLogger.
        /// </summary>
        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        #pragma warning disable 1591
        ~FileTrackingDataLogger()
        {
            Dispose(false);
        }
        #pragma warning restore 1591

        //------------------------------------------------------------------------------
        //
        // Method: Dispose
        //
        //------------------------------------------------------------------------------
        /// <summary>
        /// Provides a method to free unmanaged resources used by this class.
        /// </summary>
        /// <param name="disposing">Whether the method is being called as part of an explicit Dispose routine, and hence whether managed resources should also be freed.</param>
        protected virtual void Dispose(bool disposing)
        {
            if (!disposed)
            {
                if (disposing)
                {
                    // Free other state (managed objects).
                    if (streamWriter != null)
                    {
                        streamWriter.Dispose();
                        streamWriter = null;
                    }
                }
                // Free your own state (unmanaged objects).
                
                // Set large fields to null.

                disposed = true;
            }
        }

        #endregion
    }
}
