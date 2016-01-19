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
using OraclePermissionGeneratorDataModel;
using OraclePermissionGeneratorWebServiceAPI.Containers;
using OraclePermissionGeneratorWebServiceAPI.Containers.Converters;

namespace OraclePermissionGeneratorWebServiceAPI.Containers.Converters.UnitTests
{
    /// <summary>
    /// Unit tests for class OraclePermissionGeneratorWebServiceAPI.Containers.Converters.ContainerObjectConverter.
    /// </summary>
    [TestFixture]
    public class ContainerObjectConverterTests
    {
        ContainerObjectConverter testContainerObjectConverter;

        [SetUp]
        protected void SetUp()
        {
            testContainerObjectConverter = new ContainerObjectConverter();
        }

        [Test]
        public void IRoleToPermissionMapCollectionConvertSuccessTests()
        {
            // Test converting an empty RoleToPermissionMapCollection
            RoleToPermissionMapCollection emptyRoleToPermissionMapCollection = new RoleToPermissionMapCollection();
            List<RoleToPermissionMap> returnedRoleToPermissionMapList = testContainerObjectConverter.Convert(emptyRoleToPermissionMapCollection);
            Assert.AreEqual(0, returnedRoleToPermissionMapList.Count);

            // Test converting a RoleToPermissionMapCollection with elements
            RoleToPermissionMapCollection roleToPermissionMapCollection = new RoleToPermissionMapCollection();
            roleToPermissionMapCollection.Add(new StringMapper("A", "012345678901234567890123456789"));
            roleToPermissionMapCollection.Add(new StringMapper("987654321098765432109876543210", "C"));
            returnedRoleToPermissionMapList = testContainerObjectConverter.Convert(roleToPermissionMapCollection);
            Assert.AreEqual("A", returnedRoleToPermissionMapList[0].Role);
            Assert.AreEqual("012345678901234567890123456789", returnedRoleToPermissionMapList[0].Permission);
            Assert.AreEqual("987654321098765432109876543210", returnedRoleToPermissionMapList[1].Role);
            Assert.AreEqual("C", returnedRoleToPermissionMapList[1].Permission);
        }

        [Test]
        public void RoleToPermissionMapListConvertSuccessTests()
        {
            // Test converting an empty List
            List<RoleToPermissionMap> emptyList = new List<RoleToPermissionMap>();
            IRoleToPermissionMapCollection returnedRoleToPermissionMapCollection = testContainerObjectConverter.Convert(emptyList);
            Assert.AreEqual(0, returnedRoleToPermissionMapCollection.Count);

            // Test converting a list with elements
            List<RoleToPermissionMap> roleToPermissionMapList = new List<RoleToPermissionMap>();
            roleToPermissionMapList.Add(new RoleToPermissionMap { Role = "A", Permission = "012345678901234567890123456789" });
            roleToPermissionMapList.Add(new RoleToPermissionMap { Role = "987654321098765432109876543210", Permission = "C" });
            returnedRoleToPermissionMapCollection = testContainerObjectConverter.Convert(roleToPermissionMapList);
            Assert.IsTrue(returnedRoleToPermissionMapCollection.CheckMapping("A", "012345678901234567890123456789"));
            Assert.IsTrue(returnedRoleToPermissionMapCollection.CheckMapping("987654321098765432109876543210", "C"));
            Assert.AreEqual(2, returnedRoleToPermissionMapCollection.Count);
        }

        [Test]
        public void ListIGenerateableOracleObjectPermissionSetConvertSuccessTests()
        {
            // Test converting an empty list of IGenerateableOracleObjectPermissionSet objects
            List<IGenerateableOracleObjectPermissionSet> emptyList = new List<IGenerateableOracleObjectPermissionSet>();
            List<Containers.OracleObjectPermissionSet> returnedList = testContainerObjectConverter.Convert(emptyList);
            Assert.AreEqual(0, returnedList.Count);

            // Test converting a real list of IGenerateableOracleObjectPermissionSet objects
            List<IGenerateableOracleObjectPermissionSet> testList = new List<IGenerateableOracleObjectPermissionSet>();
            GenerateableOracleObjectPermissionSet firstObjectPermissionSet = new GenerateableOracleObjectPermissionSet("SP_CUSTOMERS_INS");
            firstObjectPermissionSet.ObjectType = "Stored Procedure";
            firstObjectPermissionSet.ObjectOwner = "XYZON";
            firstObjectPermissionSet.AddFlag = true;
            firstObjectPermissionSet.RemoveFlag = false;
            firstObjectPermissionSet.ObjectPermissions.Add(new StringMapper("XYZON_POWER_ROLE", "EXECUTE"));
            firstObjectPermissionSet.ObjectPermissions.Add(new StringMapper("XYZON_APP_ROLE", "EXECUTE"));
            testList.Add(firstObjectPermissionSet);
            GenerateableOracleObjectPermissionSet secondObjectPermissionSet = new GenerateableOracleObjectPermissionSet("APPLICATION_STATS");
            secondObjectPermissionSet.ObjectType = "Table";
            secondObjectPermissionSet.ObjectOwner = "OTHER_OWNER";
            secondObjectPermissionSet.AddFlag = false;
            secondObjectPermissionSet.RemoveFlag = true;
            secondObjectPermissionSet.ObjectPermissions.Add(new StringMapper("XYZON_POWER_ROLE", "SELECT"));
            secondObjectPermissionSet.ObjectPermissions.Add(new StringMapper("XYZON_APP_ROLE", "SELECT"));
            secondObjectPermissionSet.ObjectPermissions.Add(new StringMapper("XYZON_APP_ROLE", "INSERT"));
            secondObjectPermissionSet.ObjectPermissions.Add(new StringMapper("XYZON_APP_ROLE", "DELETE"));
            secondObjectPermissionSet.ObjectPermissions.Add(new StringMapper("XYZON_APP_ROLE", "UPDATE"));
            testList.Add(secondObjectPermissionSet);

            returnedList = testContainerObjectConverter.Convert(testList);

            Assert.AreEqual(2, returnedList.Count);
            Assert.AreEqual("SP_CUSTOMERS_INS", returnedList[0].ObjectName);
            Assert.AreEqual("Stored Procedure", returnedList[0].ObjectType);
            Assert.AreEqual("XYZON", returnedList[0].ObjectOwner);
            Assert.AreEqual(true, returnedList[0].AddFlag);
            Assert.AreEqual(false, returnedList[0].RemoveFlag);
            Assert.AreEqual(2, returnedList[0].ObjectPermissions.Count);
            Assert.AreEqual("XYZON_POWER_ROLE", returnedList[0].ObjectPermissions[0].Role);
            Assert.AreEqual("EXECUTE", returnedList[0].ObjectPermissions[0].Permission);
            Assert.AreEqual("XYZON_APP_ROLE", returnedList[0].ObjectPermissions[1].Role);
            Assert.AreEqual("EXECUTE", returnedList[0].ObjectPermissions[1].Permission);
            Assert.AreEqual("APPLICATION_STATS", returnedList[1].ObjectName);
            Assert.AreEqual("Table", returnedList[1].ObjectType);
            Assert.AreEqual("OTHER_OWNER", returnedList[1].ObjectOwner);
            Assert.AreEqual(false, returnedList[1].AddFlag);
            Assert.AreEqual(true, returnedList[1].RemoveFlag);
            Assert.AreEqual(5, returnedList[1].ObjectPermissions.Count);
            Assert.AreEqual("XYZON_POWER_ROLE", returnedList[1].ObjectPermissions[0].Role);
            Assert.AreEqual("SELECT", returnedList[1].ObjectPermissions[0].Permission);
            Assert.AreEqual("XYZON_APP_ROLE", returnedList[1].ObjectPermissions[1].Role);
            Assert.AreEqual("SELECT", returnedList[1].ObjectPermissions[1].Permission);
            Assert.AreEqual("XYZON_APP_ROLE", returnedList[1].ObjectPermissions[2].Role);
            Assert.AreEqual("INSERT", returnedList[1].ObjectPermissions[2].Permission);
            Assert.AreEqual("XYZON_APP_ROLE", returnedList[1].ObjectPermissions[3].Role);
            Assert.AreEqual("DELETE", returnedList[1].ObjectPermissions[3].Permission);
            Assert.AreEqual("XYZON_APP_ROLE", returnedList[1].ObjectPermissions[4].Role);
            Assert.AreEqual("UPDATE", returnedList[1].ObjectPermissions[4].Permission);
        }

        [Test]
        public void ValidationResultConvertSuccessTests()
        {
            string testValidationError = "The object name must be between 1 and 30 characters in length.";

            OraclePermissionGeneratorDataModel.ValidationResult testValidationResult = new OraclePermissionGeneratorDataModel.ValidationResult();
            testValidationResult.IsValid = false;
            testValidationResult.ValidationError = testValidationError;

            Containers.ValidationResult returnedValidationResult = testContainerObjectConverter.Convert(testValidationResult);
            Assert.AreEqual(false, returnedValidationResult.IsValid);
            Assert.AreEqual(testValidationError, returnedValidationResult.ValidationError);
        }

        [Test]
        public void IRoleToUserMapCollectionConvertSuccessTests()
        {
            // Test converting an empty RoleToUserMapCollection
            RoleToUserMapCollection emptyRoleToUserMapCollection = new RoleToUserMapCollection();
            List<RoleToUserMap> returnedRoleToUserMapList = testContainerObjectConverter.Convert(emptyRoleToUserMapCollection);
            Assert.AreEqual(0, returnedRoleToUserMapList.Count);

            // Test converting a RoleToUserMapCollection with elements
            RoleToUserMapCollection roleToUserMapCollection = new RoleToUserMapCollection();
            roleToUserMapCollection.Add(new StringMapper("A", "012345678901234567890123456789"));
            roleToUserMapCollection.Add(new StringMapper("987654321098765432109876543210", "C"));
            returnedRoleToUserMapList = testContainerObjectConverter.Convert(roleToUserMapCollection);
            Assert.AreEqual("A", returnedRoleToUserMapList[0].Role);
            Assert.AreEqual("012345678901234567890123456789", returnedRoleToUserMapList[0].User);
            Assert.AreEqual("987654321098765432109876543210", returnedRoleToUserMapList[1].Role);
            Assert.AreEqual("C", returnedRoleToUserMapList[1].User);
        }
    }
}
