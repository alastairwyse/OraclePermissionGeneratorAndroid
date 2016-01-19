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
using OraclePermissionGeneratorWebServiceAPI.Containers;
using OraclePermissionGeneratorWebServiceAPI.Containers.Converters;

namespace OraclePermissionGeneratorWebServiceAPI.Containers.Converters.UnitTests
{
    /// <summary>
    /// Unit tests for class OraclePermissionGeneratorWebServiceAPI.Containers.Converters.ContainerObjectJsonSerializer.
    /// </summary>
    [TestFixture]
    public class ContainerObjectJsonSerializerTests
    {
        ContainerObjectJsonSerializer testContainerObjectJsonSerializer;

        [SetUp]
        protected void SetUp()
        {
            testContainerObjectJsonSerializer = new ContainerObjectJsonSerializer();
        }

        [Test]
        public void SerializeListOfOracleObjectPermissionSetSuccessTests()
        {
            const String expectedSerializedList = @"[{""AddFlag"":true,""ObjectName"":""SP_INFORCE_INS"",""ObjectOwner"":""TEST_OWNER"",""ObjectPermissions"":[{""Permission"":""EXECUTE"",""Role"":""APP_ROLE""},{""Permission"":""EXECUTE"",""Role"":""GUI_ROLE""}],""ObjectType"":""Stored Procedure"",""RemoveFlag"":false},{""AddFlag"":true,""ObjectName"":""SP_INFORCE_DEL"",""ObjectOwner"":""TEST_OWNER"",""ObjectPermissions"":[{""Permission"":""EXECUTE"",""Role"":""APP_ROLE""},{""Permission"":""EXECUTE"",""Role"":""GUI_ROLE""}],""ObjectType"":""Stored Procedure"",""RemoveFlag"":false},{""AddFlag"":false,""ObjectName"":""VW_INFORCE"",""ObjectOwner"":""TEST_OWNER"",""ObjectPermissions"":[{""Permission"":""SELECT"",""Role"":""APP_ROLE""},{""Permission"":""SELECT"",""Role"":""READ_ROLE""}],""ObjectType"":""View"",""RemoveFlag"":true}]";
            
            List<OracleObjectPermissionSet> testList = new List<OracleObjectPermissionSet>();

            OracleObjectPermissionSet firstObjectPermissionSet = new OracleObjectPermissionSet();
            firstObjectPermissionSet.ObjectName = "SP_INFORCE_INS";
            firstObjectPermissionSet.ObjectType = "Stored Procedure";
            firstObjectPermissionSet.ObjectOwner = "TEST_OWNER";
            firstObjectPermissionSet.AddFlag = true;
            firstObjectPermissionSet.RemoveFlag = false;
            List<RoleToPermissionMap> firstRoleToPermissionMapList = new List<RoleToPermissionMap>();
            RoleToPermissionMap firstRoleToPermissionMapListFirstItem = new RoleToPermissionMap();
            firstRoleToPermissionMapListFirstItem.Role = "APP_ROLE";
            firstRoleToPermissionMapListFirstItem.Permission = "EXECUTE";
            RoleToPermissionMap firstRoleToPermissionMapListSecondItem = new RoleToPermissionMap();
            firstRoleToPermissionMapListSecondItem.Role = "GUI_ROLE";
            firstRoleToPermissionMapListSecondItem.Permission = "EXECUTE";
            firstRoleToPermissionMapList.Add(firstRoleToPermissionMapListFirstItem);
            firstRoleToPermissionMapList.Add(firstRoleToPermissionMapListSecondItem);
            firstObjectPermissionSet.ObjectPermissions = firstRoleToPermissionMapList;

            OracleObjectPermissionSet secondObjectPermissionSet = new OracleObjectPermissionSet();
            secondObjectPermissionSet.ObjectName = "SP_INFORCE_DEL";
            secondObjectPermissionSet.ObjectType = "Stored Procedure";
            secondObjectPermissionSet.ObjectOwner = "TEST_OWNER";
            secondObjectPermissionSet.AddFlag = true;
            secondObjectPermissionSet.RemoveFlag = false;
            List<RoleToPermissionMap> secondRoleToPermissionMapList = new List<RoleToPermissionMap>();
            RoleToPermissionMap secondRoleToPermissionMapListFirstItem = new RoleToPermissionMap();
            secondRoleToPermissionMapListFirstItem.Role = "APP_ROLE";
            secondRoleToPermissionMapListFirstItem.Permission = "EXECUTE";
            RoleToPermissionMap secondRoleToPermissionMapListSecondItem = new RoleToPermissionMap();
            secondRoleToPermissionMapListSecondItem.Role = "GUI_ROLE";
            secondRoleToPermissionMapListSecondItem.Permission = "EXECUTE";
            secondRoleToPermissionMapList.Add(secondRoleToPermissionMapListFirstItem);
            secondRoleToPermissionMapList.Add(secondRoleToPermissionMapListSecondItem);
            secondObjectPermissionSet.ObjectPermissions = secondRoleToPermissionMapList;

            OracleObjectPermissionSet thirdObjectPermissionSet = new OracleObjectPermissionSet();
            thirdObjectPermissionSet.ObjectName = "VW_INFORCE";
            thirdObjectPermissionSet.ObjectType = "View";
            thirdObjectPermissionSet.ObjectOwner = "TEST_OWNER";
            thirdObjectPermissionSet.AddFlag = false;
            thirdObjectPermissionSet.RemoveFlag = true;
            List<RoleToPermissionMap> thirdRoleToPermissionMapList = new List<RoleToPermissionMap>();
            RoleToPermissionMap thirdRoleToPermissionMapListFirstItem = new RoleToPermissionMap();
            thirdRoleToPermissionMapListFirstItem.Role = "APP_ROLE";
            thirdRoleToPermissionMapListFirstItem.Permission = "SELECT";
            RoleToPermissionMap thirdRoleToPermissionMapListSecondItem = new RoleToPermissionMap();
            thirdRoleToPermissionMapListSecondItem.Role = "READ_ROLE";
            thirdRoleToPermissionMapListSecondItem.Permission = "SELECT";
            thirdRoleToPermissionMapList.Add(thirdRoleToPermissionMapListFirstItem);
            thirdRoleToPermissionMapList.Add(thirdRoleToPermissionMapListSecondItem);
            thirdObjectPermissionSet.ObjectPermissions = thirdRoleToPermissionMapList;

            testList.Add(firstObjectPermissionSet);
            testList.Add(secondObjectPermissionSet);
            testList.Add(thirdObjectPermissionSet);

            String serializedList = testContainerObjectJsonSerializer.Serialize(testList);

            Assert.AreEqual(expectedSerializedList, serializedList);
        }

        [Test]
        public void SerializeListOfStringSuccessTests()
        {
            String expectedSerializedListOfStrings = @"[""View"",""Stored Procedure"",""Table""]";

            List<String> testListOfStrings = new List<String> { "View", "Stored Procedure", "Table" };

            String serializedListOfStrings = testContainerObjectJsonSerializer.Serialize(testListOfStrings);

            Assert.AreEqual(expectedSerializedListOfStrings, serializedListOfStrings);

            expectedSerializedListOfStrings = "[]";

            testListOfStrings.Clear();

            serializedListOfStrings = testContainerObjectJsonSerializer.Serialize(testListOfStrings);

            Assert.AreEqual(expectedSerializedListOfStrings, serializedListOfStrings);
        }

        [Test]
        public void SerializeValidationResultSuccessTests()
        {
            String expectedSerializedValidationResult = @"{""IsValid"":false,""ValidationError"":""The object 'SP_INFORCE_INS' already exists""}";

            ValidationResult testValidationResult = new ValidationResult() { IsValid = false, ValidationError = "The object 'SP_INFORCE_INS' already exists" };

            String serializedValidationResult = testContainerObjectJsonSerializer.Serialize(testValidationResult);

            Assert.AreEqual(expectedSerializedValidationResult, serializedValidationResult);

            expectedSerializedValidationResult = @"{""IsValid"":true,""ValidationError"":""""}";

            testValidationResult = new ValidationResult() { IsValid = true, ValidationError = "" };

            serializedValidationResult = testContainerObjectJsonSerializer.Serialize(testValidationResult);

            Assert.AreEqual(expectedSerializedValidationResult, serializedValidationResult);
        }

        [Test]
        public void SerializeListOfRoleToUserMapSuccessTests()
        {
            String expectedSerializedListOfRoleToUserMaps = @"[{""Role"":""XYZON_APP_ROLE"",""User"":""XYZON_APP_USER""},{""Role"":""XYZON_POWER_ROLE"",""User"":""XYZON_POWER_USER""},{""Role"":""XYZON_READ_ROLE"",""User"":""JONES_SAM""},{""Role"":""XYZON_READ_ROLE"",""User"":""SMITH_JOHN""}]";

            List<RoleToUserMap> testListOfRoleToUserMaps = new List<RoleToUserMap>();
            testListOfRoleToUserMaps.Add(new RoleToUserMap() { Role = "XYZON_APP_ROLE", User = "XYZON_APP_USER" });
            testListOfRoleToUserMaps.Add(new RoleToUserMap() { Role = "XYZON_POWER_ROLE", User = "XYZON_POWER_USER" });
            testListOfRoleToUserMaps.Add(new RoleToUserMap() { Role = "XYZON_READ_ROLE", User = "JONES_SAM" });
            testListOfRoleToUserMaps.Add(new RoleToUserMap() { Role = "XYZON_READ_ROLE", User = "SMITH_JOHN" });

            String serializedListOfRoleToUserMaps = testContainerObjectJsonSerializer.Serialize(testListOfRoleToUserMaps);

            Assert.AreEqual(expectedSerializedListOfRoleToUserMaps, serializedListOfRoleToUserMaps);
        }

        [Test]
        public void DeserializeAuthenticationContextSuccessTests()
        {
            const String serializedAuthenticationContext = @"{""UserIdentifier"":""test@tempuri.org""}";

            AuthenticationContext testAuthenticationContext = testContainerObjectJsonSerializer.DeserializeAuthenticationContext(serializedAuthenticationContext);

            Assert.AreEqual("test@tempuri.org", testAuthenticationContext.UserIdentifier);
        }

        [Test]
        public void DeserializeTrackingDataBothMembersNotNullSuccessTests()
        {
            const String serializedTrackingData = @"{""IpV4Address"":[192,168,2,101],""Location"":{""Latitude"":35.6895,""SecondsSinceUpdate"":23,""Longitude"":139.6917}}";

            TrackingData testTrackingData = testContainerObjectJsonSerializer.DeserializeTrackingData(serializedTrackingData);

            Assert.AreEqual(35.6895, testTrackingData.Location.Latitude);
            Assert.AreEqual(139.6917, testTrackingData.Location.Longitude);
            Assert.AreEqual(23, testTrackingData.Location.SecondsSinceUpdate);
            Assert.AreEqual(192, testTrackingData.IpV4Address[0]);
            Assert.AreEqual(168, testTrackingData.IpV4Address[1]);
            Assert.AreEqual(2, testTrackingData.IpV4Address[2]);
            Assert.AreEqual(101, testTrackingData.IpV4Address[3]);
        }

        [Test]
        public void DeserializeTrackingDataLocationNullSuccessTests()
        {
            const String serializedTrackingData = @"{""IpV4Address"":[192,168,2,101]}";

            TrackingData testTrackingData = testContainerObjectJsonSerializer.DeserializeTrackingData(serializedTrackingData);

            Assert.IsNull(testTrackingData.Location);
            Assert.AreEqual(192, testTrackingData.IpV4Address[0]);
            Assert.AreEqual(168, testTrackingData.IpV4Address[1]);
            Assert.AreEqual(2, testTrackingData.IpV4Address[2]);
            Assert.AreEqual(101, testTrackingData.IpV4Address[3]);
        }

        [Test]
        public void DeserializeTrackingDataIpAddressNullSuccessTests()
        {
            const String serializedTrackingData = @"{""Location"":{""Latitude"":35.6895,""SecondsSinceUpdate"":17,""Longitude"":139.6917}}";

            TrackingData testTrackingData = testContainerObjectJsonSerializer.DeserializeTrackingData(serializedTrackingData);

            Assert.AreEqual(35.6895, testTrackingData.Location.Latitude);
            Assert.AreEqual(139.6917, testTrackingData.Location.Longitude);
            Assert.AreEqual(17, testTrackingData.Location.SecondsSinceUpdate);
            Assert.IsNull(testTrackingData.IpV4Address);
        }

        [Test]
        public void DeserializeTrackingDataBothMembersNullSuccessTests()
        {
            const String serializedTrackingData = @"{}";

            TrackingData testTrackingData = testContainerObjectJsonSerializer.DeserializeTrackingData(serializedTrackingData);

            Assert.IsNull(testTrackingData.Location);
            Assert.IsNull(testTrackingData.IpV4Address);
        }

        [Test]
        public void DeserializeRoleToPermissionMapListSuccessTests()
        {
            String serializedListOfRoleToPermissionMaps = "[]";

            List<RoleToPermissionMap> testListOfRoleToPermissionMaps = testContainerObjectJsonSerializer.DeserializeRoleToPermissionMapList(serializedListOfRoleToPermissionMaps);

            Assert.AreEqual(0, testListOfRoleToPermissionMaps.Count);

            serializedListOfRoleToPermissionMaps = @"[{""Role"":""READ_ROLE"",""Permission"":""SELECT""},{""Role"":""APP_ROLE"",""Permission"":""INSERT""},{""Role"":""APP_ROLE"",""Permission"":""UPDATE""}]";

            testListOfRoleToPermissionMaps = testContainerObjectJsonSerializer.DeserializeRoleToPermissionMapList(serializedListOfRoleToPermissionMaps);

            Assert.AreEqual("READ_ROLE", testListOfRoleToPermissionMaps[0].Role);
            Assert.AreEqual("SELECT", testListOfRoleToPermissionMaps[0].Permission);
            Assert.AreEqual("APP_ROLE", testListOfRoleToPermissionMaps[1].Role);
            Assert.AreEqual("INSERT", testListOfRoleToPermissionMaps[1].Permission);
            Assert.AreEqual("APP_ROLE", testListOfRoleToPermissionMaps[2].Role);
            Assert.AreEqual("UPDATE", testListOfRoleToPermissionMaps[2].Permission);
        }
    }
}
