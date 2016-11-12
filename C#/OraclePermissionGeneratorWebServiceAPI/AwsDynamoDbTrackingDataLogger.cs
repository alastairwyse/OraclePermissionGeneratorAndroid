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
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.Model;
using Amazon.DynamoDBv2.DocumentModel;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Logs tracking data to an Amazon Web Services DynamoDB instance.
    /// </summary>
    class AwsDynamoDbTrackingDataLogger : ITrackingDataLogger, IDisposable
    {
        private const String logEntryDateFormat = "yyyy-MM-dd HH:mm:ss.fff";

        /// <summary>Indicates whether the object has been disposed.</summary>
        protected bool disposed = false;
        private AmazonDynamoDBConfig dynamoDbConfig;
        private AmazonDynamoDBClient dynamoDbClient;

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.AwsDynamoDbTrackingDataLogger class.
        /// </summary>
        /// <param name="awsAccessKeyId">The Amazon Web Services IAM access key ID.</param>
        /// <param name="awsSecretAccessKey">The Amazon Web Services IAM secret access key.</param>
        public AwsDynamoDbTrackingDataLogger(String awsAccessKeyId, String awsSecretAccessKey)
        {
            dynamoDbConfig = new AmazonDynamoDBConfig();
            dynamoDbClient = new AmazonDynamoDBClient(awsAccessKeyId, awsSecretAccessKey, dynamoDbConfig);
        }

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.AwsDynamoDbTrackingDataLogger class.
        /// </summary>
        /// <param name="awsAccessKeyId">The Amazon Web Services IAM access key ID.</param>
        /// <param name="awsSecretAccessKey">The Amazon Web Services IAM secret access key.</param>
        /// <param name="serviceUrl">The endpoint URL of the DynamoDB instance.</param>
        public AwsDynamoDbTrackingDataLogger(String awsAccessKeyId, String awsSecretAccessKey, String serviceUrl)
            : this (awsAccessKeyId, awsSecretAccessKey)
        {
            dynamoDbConfig.ServiceURL = serviceUrl;
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ITrackingDataLogger.Log(System.DateTime,System.String,System.String,OraclePermissionGeneratorWebServiceAPI.Location,System.Byte[])"]/*'/>
        public void Log(DateTime timeStamp, string userIdentifier, string methodName, Containers.Location location, byte[] ipV4Address)
        {
            Document trackingDataDocument = new Document();
            trackingDataDocument.Add("UserIdentifier", new Primitive(userIdentifier));
            trackingDataDocument.Add("TimeStamp", new Primitive(timeStamp.ToString("yyyy-MM-dd HH:mm:ss.fff")));
            trackingDataDocument.Add("MethodName", new Primitive(methodName));

            if (location != null)
            {
                Document locationDocument = new Document();
                locationDocument.Add("Latitude", new Primitive(location.Latitude.ToString(), true));
                locationDocument.Add("Longitude", new Primitive(location.Longitude.ToString(), true));
                locationDocument.Add("SecondsSinceUpdate", new Primitive(location.SecondsSinceUpdate.ToString(), true));
                locationDocument.Add("Location", locationDocument);
            }

            if (ipV4Address != null)
            {
                DynamoDBList ipAddressDocument = new DynamoDBList();
                ipAddressDocument.Add(new Primitive(ipV4Address[0].ToString(), true));
                ipAddressDocument.Add(new Primitive(ipV4Address[1].ToString(), true));
                ipAddressDocument.Add(new Primitive(ipV4Address[2].ToString(), true));
                ipAddressDocument.Add(new Primitive(ipV4Address[3].ToString(), true));
                trackingDataDocument.Add("IpAddress", ipAddressDocument);
            }

            try
            {
                Table trackingDataTable = Table.LoadTable(dynamoDbClient, "TrackingData");
                trackingDataTable.PutItem(trackingDataDocument);
            }
            catch (Exception e)
            {
                throw new Exception("Failed to write tracking data to DynamoDB instance.", e);
            }
        }

        #region Finalize / Dispose Methods

        /// <summary>
        /// Releases the unmanaged resources used by the AwsDynamoDbTrackingDataLogger.
        /// </summary>
        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        #pragma warning disable 1591
        ~AwsDynamoDbTrackingDataLogger()
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
                    if (dynamoDbClient != null)
                    {
                        dynamoDbClient.Dispose();
                        dynamoDbClient = null;
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
