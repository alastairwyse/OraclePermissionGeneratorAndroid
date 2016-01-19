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
using NUnit.Framework;
using NMock2;
using NMock2.Matchers;
using OraclePermissionGeneratorDataInterfaceModel;
using OraclePermissionGeneratorWebServiceAPI;
using OraclePermissionGeneratorWebServiceAPI.Containers;

namespace OraclePermissionGeneratorWebServiceAPI.UnitTests
{
    /// <summary>
    /// Unit tests for class OraclePermissionGeneratorWebServiceAPI.WebServiceApiBase.
    /// <remarks>Tests abstract base class WebServiceApiBase via derived class SoapWebServiceApi.</remarks>
    /// </summary>
    [TestFixture]
    public class WebServiceApiBaseTests
    {
        // NOTE: The WebServiceApiBase class performs mainly 3 functions.  These functions are largely unit tested in other test classes or projects.
        //       Hence this class tests the logging of tracking data via the ITrackingDataLogger member of the class.  The location of tests for these 3 functions are listed below...
        //         1) Serialization and deserialization - tested in unit tests for class ContainerObjectJsonSerializer.
        //         2) Proxying of methods to class OraclePermissionGeneratorDataInterfaceLayer - in unit tests in the OraclePermissionGenerator project.
        //         3) Logging of tracking data via ITrackingDataLogger - tested in this test class.

        private const String testUserIdentifier = "test_user@tempuri.org";
        private const String testSerializedAuthenticationContext = @"{""UserIdentifier"":""" + testUserIdentifier + @"""}";
        private const String testSerializedTrackingData = @"{""IpV4Address"":[192,168,2,101],""Location"":{""Latitude"":35.6895,""Longitude"":139.6917,""SecondsSinceUpdate"":23}}";
        private Byte[] IpV4Address;
        private Location testLocation;

        private Mockery mocks;
        private ITrackingDataLogger mockTrackingDataLogger;
        private SoapWebServiceApi testSoapWebServiceApi;

        [SetUp]
        protected void SetUp()
        {
            testLocation = new Location();
            testLocation.Latitude = 35.6895;
            testLocation.Longitude = 139.6917;
            testLocation.SecondsSinceUpdate = 23;
            IpV4Address = new Byte[] { 192, 168, 2, 101 };

            mocks = new Mockery();
            mockTrackingDataLogger = mocks.NewMock<ITrackingDataLogger>();
            testSoapWebServiceApi = new SoapWebServiceApi(new Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer>(), mockTrackingDataLogger);
            testSoapWebServiceApi.AddUser(testUserIdentifier);
        }

        [Test]
        public void AddUserBlankIdentifier()
        {
            ArgumentException e = Assert.Throws<ArgumentException>(delegate
            {
                testSoapWebServiceApi.AddUser(" ");
            });
            Assert.That(e.Message, NUnit.Framework.Is.StringStarting("Parameter 'userIdentifier' cannot be blank."));
            Assert.AreEqual("userIdentifier", e.ParamName);
        }

        [Test]
        public void AddUserIdentifierAlreadyExists()
        {
            ArgumentException e = Assert.Throws<ArgumentException>(delegate
            {
                testSoapWebServiceApi.AddUser("abc");
                testSoapWebServiceApi.AddUser("abc");
            });
            Assert.That(e.Message, NUnit.Framework.Is.StringStarting("User identifier 'abc' already exists."));
            Assert.AreEqual("userIdentifier", e.ParamName);
        }

        [Test]
        public void LoadDataModelFromFileInvalidUserIdentifier()
        {
            ArgumentException e = Assert.Throws<ArgumentException>(delegate
            {
                testSoapWebServiceApi.LoadDataModelFromFile("InvalidUserId", "", "");
            });
            Assert.That(e.Message, NUnit.Framework.Is.StringStarting("User identifier 'InvalidUserId' does not exist."));
            Assert.AreEqual("userIdentifier", e.ParamName);
        }

        [Test]
        public void LoadDataModelFromFileInvalidFilePath()
        {
            Exception e = Assert.Throws<Exception>(delegate
            {
                testSoapWebServiceApi.LoadDataModelFromFile(testUserIdentifier, "InvalidPath", "");
            });
            Assert.That(e.Message, NUnit.Framework.Is.StringStarting("Failed to open data model data file at path 'InvalidPath'."));
        }

        [Test]
        public void GetDefaultObjectOwnerLoggingTest()
        {
            SetDataLoggingExpectations("GetDefaultObjectOwner()");

            testSoapWebServiceApi.GetDefaultObjectOwner(testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void SetDefaultObjectOwnerLoggingTest()
        {
            SetDataLoggingExpectations("SetDefaultObjectOwner(defaultObjectOwner)");

            testSoapWebServiceApi.SetDefaultObjectOwner("XYZON", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void GetObjectTypesLoggingTest()
        {
            SetDataLoggingExpectations("GetObjectTypes()");

            testSoapWebServiceApi.GetObjectTypes(testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void GetRolesLoggingTest()
        {
            SetDataLoggingExpectations("GetRoles()");

            testSoapWebServiceApi.GetRoles(testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void GetPermissionsLoggingTest()
        {
            SetDataLoggingExpectations("GetPermissions(objectType)");

            testSoapWebServiceApi.GetPermissions("Stored Procedure", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void GetPermissionsForObjectLoggingTest()
        {
            SetDataLoggingExpectations("AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions)");
            SetDataLoggingExpectations("GetPermissionsForObject(objectName, role)");

            testSoapWebServiceApi.AddObjectPermissionSet("ORDERS", "Table", "XYZON", true, false, new List<RoleToPermissionMap>(), testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.GetPermissionsForObject("ORDERS", "XYZON_READ_ROLE", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void GetMasterRoleToUserMapCollectionLoggingTest()
        {
            SetDataLoggingExpectations("GetMasterRoleToUserMapCollection()");
            
            testSoapWebServiceApi.GetMasterRoleToUserMapCollection(testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void AddObjectPermissionSetLoggingTest()
        {
            SetDataLoggingExpectations("AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions)");

            testSoapWebServiceApi.AddObjectPermissionSet("ORDER_STATUSES", "Table", "XYZON", true, false, new List<RoleToPermissionMap>(), testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void RemoveObjectPermissionSetLoggingTest()
        {
            SetDataLoggingExpectations("AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions)");
            SetDataLoggingExpectations("RemoveObjectPermissionSet(objectName)");

            testSoapWebServiceApi.AddObjectPermissionSet("ORDER_STATUSES", "Table", "XYZON", true, false, new List<RoleToPermissionMap>(), testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.RemoveObjectPermissionSet("ORDER_STATUSES", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void ObjectNameValidateLoggingTest()
        {
            SetDataLoggingExpectations("ObjectNameValidate(objectName)");

            testSoapWebServiceApi.ObjectNameValidate("ORDER_STATUSES", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void ObjectTypeValidateLoggingTest()
        {
            SetDataLoggingExpectations("ObjectTypeValidate(objectType)");

            testSoapWebServiceApi.ObjectTypeValidate("Package", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void ObjectOwnerValidateLoggingTest()
        {
            SetDataLoggingExpectations("ObjectOwnerValidate(objectOwner)");

            testSoapWebServiceApi.ObjectOwnerValidate("XYZON", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void GetObjectsLoggingTest()
        {
            SetDataLoggingExpectations("GetObjects()");

            testSoapWebServiceApi.GetObjects(testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void SetAddFlagLoggingTest()
        {
            SetDataLoggingExpectations("AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions)");
            SetDataLoggingExpectations("SetAddFlag(objectName, addFlagValue)");

            testSoapWebServiceApi.AddObjectPermissionSet("ORDER_STATUSES", "Table", "XYZON", true, false, new List<RoleToPermissionMap>(), testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.SetAddFlag("ORDER_STATUSES", true, testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void SetRemoveFlagLoggingTest()
        {
            SetDataLoggingExpectations("AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions)");
            SetDataLoggingExpectations("SetRemoveFlag(objectName, removeFlagValue)");

            testSoapWebServiceApi.AddObjectPermissionSet("ORDER_STATUSES", "Table", "XYZON", true, false, new List<RoleToPermissionMap>(), testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.SetRemoveFlag("ORDER_STATUSES", false, testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void AddPermissionLoggingTest()
        {
            SetDataLoggingExpectations("AddRoleToUserMap(role, user)");
            SetDataLoggingExpectations("AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions)");
            SetDataLoggingExpectations("AddPermission(objectName, role, permission)");

            testSoapWebServiceApi.AddRoleToUserMap("XYZON_READ_ROLE", "XYZON_READ_USER", testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.AddObjectPermissionSet("ORDER_STATUSES", "Table", "XYZON", true, false, new List<RoleToPermissionMap>(), testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.AddPermission("ORDER_STATUSES", "XYZON_READ_ROLE", "SELECT", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void RemovePermissionLoggingTest()
        {
            SetDataLoggingExpectations("AddRoleToUserMap(role, user)");
            SetDataLoggingExpectations("AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions)");
            SetDataLoggingExpectations("AddPermission(objectName, role, permission)");
            SetDataLoggingExpectations("RemovePermission(objectName, role, permission)");

            testSoapWebServiceApi.AddRoleToUserMap("XYZON_READ_ROLE", "XYZON_READ_USER", testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.AddObjectPermissionSet("ORDER_STATUSES", "Table", "XYZON", true, false, new List<RoleToPermissionMap>(), testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.AddPermission("ORDER_STATUSES", "XYZON_READ_ROLE", "SELECT", testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.RemovePermission("ORDER_STATUSES", "XYZON_READ_ROLE", "SELECT", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void RoleGetReferencingObjectsLoggingTest()
        {
            SetDataLoggingExpectations("RoleGetReferencingObjects(role)");

            testSoapWebServiceApi.RoleGetReferencingObjects("XYZON_READ_ROLE", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void AddRoleToUserMapLoggingTest()
        {
            SetDataLoggingExpectations("AddRoleToUserMap(role, user)");

            testSoapWebServiceApi.AddRoleToUserMap("XYZON_READ_ROLE", "XYZON_READ_USER", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void RemoveRoleToUserMapLoggingTest()
        {
            SetDataLoggingExpectations("AddRoleToUserMap(role, user)");
            SetDataLoggingExpectations("RemoveRoleToUserMap(role, user)");

            testSoapWebServiceApi.AddRoleToUserMap("XYZON_READ_ROLE", "XYZON_READ_USER", testSerializedAuthenticationContext, testSerializedTrackingData);
            testSoapWebServiceApi.RemoveRoleToUserMap("XYZON_READ_ROLE", "XYZON_READ_USER", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void RoleToUserMapValidateLoggingTest()
        {
            SetDataLoggingExpectations("RoleToUserMapValidate(role, user)");

            testSoapWebServiceApi.RoleToUserMapValidate("XYZON_READ_ROLE", "XYZON_READ_USER", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void CreatePrivilegeScriptLoggingTest()
        {
            SetDataLoggingExpectations("CreatePrivilegeScript(scriptType, generateRevokeStatements)");

            testSoapWebServiceApi.CreatePrivilegeScript("Rollout", true, testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        [Test]
        public void CreateSynonymScriptLoggingTest()
        {
            SetDataLoggingExpectations("CreateSynonymScript(scriptType)");

            testSoapWebServiceApi.CreateSynonymScript("Rollout", testSerializedAuthenticationContext, testSerializedTrackingData);

            mocks.VerifyAllExpectationsHaveBeenMet();
        }

        /// <summary>
        /// Calls the NMock2.Expect() method to setup expectations for calls to the Log() method on member mockTrackingDataLogger.
        /// </summary>
        /// <param name="methodName">The descriptor of the method that is expected to be a parameter to the Log() call.</param>
        private void SetDataLoggingExpectations(String methodName)
        {
            Object[] expectedLogParameters = new Object[]
            {
                new TypeMatcher(typeof(DateTime)), 
                testUserIdentifier, 
                methodName, 
                new LocationMatcher(testLocation), 
                IpV4Address
            };
            Expect.Once.On(mockTrackingDataLogger).Method("Log").With(expectedLogParameters);
        }
    }
}
