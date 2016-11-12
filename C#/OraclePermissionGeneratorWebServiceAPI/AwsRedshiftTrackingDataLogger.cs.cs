/*
 * Copyright 2016 Alastair Wyse (http://www.oraclepermissiongenerator.net/methodinvocationremotingandroid/)
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
using System.Data.Odbc;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Logs tracking data to an Amazon Web Services Redshift database.
    /// </summary>
    /// <remarks>Requires PostgreSQL ODBC driver.</remarks>
    class AwsRedshiftTrackingDataLogger : ITrackingDataLogger, IDisposable
    {
        private const String odbcDriverIdentifier = "{PostgreSQL Unicode}";
        private const String redshiftDateStringFormat = "yyyy-MM-dd HH:mm:ss.fff";
        private const String trackingDataTableName = "tracking_data";

        /// <summary>Indicates whether the object has been disposed.</summary>
        protected bool disposed = false;
        private String connectionString;
        private OdbcConnection connection;
        
        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.AwsRedshiftTrackingDataLogger class.
        /// </summary>
        /// <param name="server">The network location of the Redshift database instance.</param>
        /// <param name="port">The TCP port used to connect to the database instance.</param>
        /// <param name="database">The name of the database.</param>
        /// <param name="userId">The user identifier used to log in to the database.</param>
        /// <param name="password">The password.</param>
        public AwsRedshiftTrackingDataLogger(String server, UInt16 port, String database, String userId, String password)
        {
            // Validate the constructor parameters
            ValidateStringParameter(server);
            ValidateStringParameter(database);
            ValidateStringParameter(userId);

            // Create the ODBC connection string
            OdbcConnectionStringBuilder connectionStringBuilder = new OdbcConnectionStringBuilder();
            connectionStringBuilder.Driver = odbcDriverIdentifier;
            connectionStringBuilder.Add("Server", server);
            connectionStringBuilder.Add("Database", database);
            connectionStringBuilder.Add("UID", userId);
            connectionStringBuilder.Add("PWD", password);
            connectionStringBuilder.Add("Port", port);
            connectionString = connectionStringBuilder.ToString();
        }

        /// <summary>
        /// Connects to the Redshift database instance.
        /// </summary>
        public void Connect()
        {
            connection = new OdbcConnection(connectionString);
            try
            {
                connection.Open();
            }
            catch (Exception e)
            {
                throw new Exception("Failed to connect to Redshift database instance.", e);
            }
        }

        /// <summary>
        /// Disconnects from the Redshift database instance.
        /// </summary>
        public void Disconnect()
        {
            if (connection != null)
            {
                connection.Close();
            }
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ITrackingDataLogger.Log(System.DateTime,System.String,System.String,OraclePermissionGeneratorWebServiceAPI.Location,System.Byte[])"]/*'/>
        public void Log(DateTime timeStamp, string userIdentifier, string methodName, Containers.Location location, byte[] ipV4Address)
        {
            // Use a dictionary to store the column name / column value pairs to be inserted into the table
            Dictionary<String, String> columnValues = new Dictionary<String, String>();
            // PostgreSQL requires 'zulu' postfix to specify UTC
            columnValues.Add("time_stamp", WrapStringInSingleQuotes(timeStamp.ToString(redshiftDateStringFormat) + " zulu"));
            columnValues.Add("user_id", WrapStringInSingleQuotes(userIdentifier));
            columnValues.Add("method_name", WrapStringInSingleQuotes(methodName));
            if (location != null)
            {
                columnValues.Add("latitude", location.Latitude.ToString());
                columnValues.Add("longitude", location.Longitude.ToString());
                columnValues.Add("seconds_since_update", location.SecondsSinceUpdate.ToString());
            }
            if (ipV4Address != null)
            {
                columnValues.Add("ip_addess_octet_1", ipV4Address[0].ToString());
                columnValues.Add("ip_addess_octet_2", ipV4Address[1].ToString());
                columnValues.Add("ip_addess_octet_3", ipV4Address[2].ToString());
                columnValues.Add("ip_addess_octet_4", ipV4Address[3].ToString());
            }

            // Build the SQL insert statement
            StringBuilder insertStatementBuilder = new StringBuilder();
            insertStatementBuilder.Append("INSERT  INTO  ");
            insertStatementBuilder.Append(trackingDataTableName);
            insertStatementBuilder.Append(" ( ");
            Int32 currentColumnIndex = 1;
            foreach(String currentColumnName in columnValues.Keys)
            {
                insertStatementBuilder.Append(currentColumnName);
                if (currentColumnIndex < columnValues.Count)
                {
                    insertStatementBuilder.Append(", ");
                }
                currentColumnIndex++;
            }
            insertStatementBuilder.Append(")  VALUES  ( ");
            currentColumnIndex = 1;
            foreach(String currentColumnValue in columnValues.Values)
            {
                insertStatementBuilder.Append(currentColumnValue);
                if (currentColumnIndex < columnValues.Count)
                {
                    insertStatementBuilder.Append(", ");
                }
                currentColumnIndex++;
            }
            insertStatementBuilder.Append(");");
            
            // Execute the statement
            using (OdbcCommand command = connection.CreateCommand())
            {
                command.CommandText = insertStatementBuilder.ToString();
                try
                {
                    command.ExecuteNonQuery();
                }
                catch (Exception e)
                {
                    throw new Exception("Failed to execute SQL insert statement against Redshift databse instance.", e);
                }
            }
        }

        /// <summary>
        /// Throws an exception if the specified string parameter is null or blank.
        /// </summary>
        /// <param name="parameterName">The name of the parameter to validate.</param>
        private void ValidateStringParameter(String parameterName)
        {
            if (String.IsNullOrEmpty(parameterName) == true)
            {
                throw new ArgumentException("Parameter '" + parameterName + "' cannot be null or blank.", parameterName);
            }
        }

        /// <summary>
        /// Wraps the inputted string with single quotation (') characters.
        /// </summary>
        /// <param name="inputString">The string to wrap.</param>
        /// <returns>The string wrapped with single quotes.</returns>
        private String WrapStringInSingleQuotes(String inputString)
        {
            return "'" + inputString + "'";
        }

        #region Finalize / Dispose Methods

        /// <summary>
        /// Releases the unmanaged resources used by the AwsRedshiftTrackingDataLogger.
        /// </summary>
        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        #pragma warning disable 1591
        ~AwsRedshiftTrackingDataLogger()
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
                    if (connection != null)
                    {
                        connection.Close();
                        connection.Dispose();
                        connection = null;
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
