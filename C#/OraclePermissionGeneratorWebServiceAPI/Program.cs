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
using System.ServiceModel;
using System.ServiceModel.Description;
using OraclePermissionGeneratorDataInterfaceModel;

namespace OraclePermissionGeneratorWebServiceAPI
{
    class Program
    {
        static void Main(string[] args)
        {
            const String soapServiceEndpointAddress = "SOAP";
            const String restServiceEndpointAddress = "REST";

            Uri soapBaseAddress = new Uri("http://localhost:5000/OraclePermissionGeneratorWebServiceAPI");
            Uri restBaseAddress = new Uri("http://localhost:5001/OraclePermissionGeneratorWebServiceAPI");

            // Create and start the service host
            using (FileTrackingDataLogger trackingDataLogger = new FileTrackingDataLogger(@"C:\Temp\"))
            //using (AwsDynamoDbTrackingDataLogger trackingDataLogger = new AwsDynamoDbTrackingDataLogger("[access key id]", "[secret access key]"))
            //using (AwsRedshiftTrackingDataLogger trackingDataLogger = new AwsRedshiftTrackingDataLogger("[redshift server location]", 5439, "[redshift server instance name]", "[user name]", "[password]"))
            {
                //trackingDataLogger.Connect();
                Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer> userDataRespository = new Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer>();
                SoapWebServiceApi soapApiInstance = new SoapWebServiceApi(userDataRespository, trackingDataLogger);
                RestWebServiceApi restApiInstance = new RestWebServiceApi(userDataRespository, trackingDataLogger);
                soapApiInstance.AddUser("tutorial_user@tempuri.org");
                soapApiInstance.LoadDataModelFromFile("tutorial_user@tempuri.org", @"..\..\..\Resources\Tutorial 2.xml", @"..\..\..\Resources\ORACLE_PERMISSION_GENERATOR_CONFIG.xsd");

                using (ServiceHost soapServiceHost = new ServiceHost(soapApiInstance, soapBaseAddress))
                using (ServiceHost restServiceHost = new ServiceHost(restApiInstance, restBaseAddress))
                {
                    // Set SOAP service host settings
                    ServiceMetadataBehavior soapServiceMetadataBehavior = new ServiceMetadataBehavior();
                    soapServiceMetadataBehavior.HttpGetEnabled = true;
                    soapServiceMetadataBehavior.MetadataExporter.PolicyVersion = PolicyVersion.Policy15;
                    soapServiceHost.Description.Behaviors.Add(soapServiceMetadataBehavior);
                    BasicHttpBinding soapBinding = new BasicHttpBinding();
                    ServiceEndpoint soapEndpoint = soapServiceHost.AddServiceEndpoint(typeof(ISoapWebServiceApi), soapBinding, soapServiceEndpointAddress);
                    // Set single instance
                    soapServiceHost.Description.Behaviors.Find<ServiceBehaviorAttribute>().InstanceContextMode = InstanceContextMode.Single;
                    // Turn on option to include exception in any web service interface errors
                    soapServiceHost.Description.Behaviors.Find<ServiceDebugBehavior>().IncludeExceptionDetailInFaults = true;

                    // Set REST service host settings
                    ServiceMetadataBehavior restServiceMetadataBehavior = new ServiceMetadataBehavior();
                    restServiceMetadataBehavior.HttpGetEnabled = true;
                    restServiceMetadataBehavior.MetadataExporter.PolicyVersion = PolicyVersion.Policy15;
                    restServiceHost.Description.Behaviors.Add(restServiceMetadataBehavior);
                    WebHttpBinding restBinding = new WebHttpBinding();
                    ServiceEndpoint restEndpoint = restServiceHost.AddServiceEndpoint(typeof(IRestWebServiceApi), restBinding, restServiceEndpointAddress);
                    WebHttpBehavior restBehavior = new WebHttpBehavior();
                    restEndpoint.Behaviors.Add(restBehavior);
                    restServiceHost.Description.Behaviors.Find<ServiceBehaviorAttribute>().InstanceContextMode = InstanceContextMode.Single;
                    restServiceHost.Description.Behaviors.Find<ServiceDebugBehavior>().IncludeExceptionDetailInFaults = true;

                    // Start the services
                    soapServiceHost.Open();
                    restServiceHost.Open();
                    Console.WriteLine("Press [Enter] to stop the service.");
                    Console.ReadLine();
                    restServiceHost.Close();
                    soapServiceHost.Close();
                }

                //trackingDataLogger.Disconnect();
            }
        }
    }
}
