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
using OraclePermissionGeneratorDataModel;

namespace OraclePermissionGeneratorWebServiceAPI.Containers.Converters
{
    /// <summary>
    /// Converts container objects in the OraclePermissionGeneratorDataModel namespace to equivalent objects in the OraclePermissionGeneratorWebServiceAPI.Containers namespace, so they can be serialized and exposed as a web service by Windows Communication Foundation.
    /// </summary>
    public class ContainerObjectConverter
    {
        /// <summary>
        /// Converts an OraclePermissionGeneratorDataModel.IRoleToPermissionMapCollection object to a List of OraclePermissionGeneratorWebServiceAPI.Containers.RoleToPermissionMap objects.
        /// </summary>
        /// <param name="roleToPermissionMapCollection">The IRoleToPermissionMapCollection to convert.</param>
        /// <returns>The converted object.</returns>
        public List<RoleToPermissionMap> Convert(IRoleToPermissionMapCollection roleToPermissionMapCollection)
        {
            List<RoleToPermissionMap> returnList = new List<RoleToPermissionMap>();

            foreach (IStringMapper currentStringMapper in roleToPermissionMapCollection)
            {
                RoleToPermissionMap roleToPermissionMap = new RoleToPermissionMap();
                roleToPermissionMap.Role = currentStringMapper.MappedFrom;
                roleToPermissionMap.Permission = currentStringMapper.MappedTo;
                returnList.Add(roleToPermissionMap);
            }

            return returnList;
        }

        /// <summary>
        /// Converts a List of OraclePermissionGeneratorWebServiceAPI.Containers.RoleToPermissionMap objects to an OraclePermissionGeneratorDataModel.IRoleToPermissionMapCollection object.
        /// </summary>
        /// <param name="roleToPermissionMapList">The list of RoleToPermissionMap objects to convert.</param>
        /// <returns>The converted object.</returns>
        public IRoleToPermissionMapCollection Convert(List<RoleToPermissionMap> roleToPermissionMapList)
        {
            RoleToPermissionMapCollection returnCollection = new RoleToPermissionMapCollection();

            foreach (RoleToPermissionMap currentRoleToPermissionMap in roleToPermissionMapList)
            {
                returnCollection.Add(currentRoleToPermissionMap.Role, currentRoleToPermissionMap.Permission);
            }

            return returnCollection;
        }

        /// <summary>
        /// Converts an OraclePermissionGeneratorDataModel.IRoleToUserMapCollection object to a List of OraclePermissionGeneratorWebServiceAPI.Containers.RoleToUserMap objects.
        /// </summary>
        /// <param name="roleToUserMapCollection"></param>
        /// <returns></returns>
        public List<RoleToUserMap> Convert(IRoleToUserMapCollection roleToUserMapCollection)
        {
            List<RoleToUserMap> returnList = new List<RoleToUserMap>();

            foreach (IStringMapper currentStringMapper in roleToUserMapCollection)
            {
                RoleToUserMap roleToUserMap = new RoleToUserMap();
                roleToUserMap.Role = currentStringMapper.MappedFrom;
                roleToUserMap.User = currentStringMapper.MappedTo;
                returnList.Add(roleToUserMap);
            }

            return returnList;
        }

        /// <summary>
        /// Converts a list of objects implementing interface OraclePermissionGeneratorDataModel.IGenerateableOracleObjectPermissionSet to a list of OraclePermissionGeneratorWebServiceAPI.Containers.OracleObjectPermissionSet objects.
        /// </summary>
        /// <param name="inputList">The list of IGenerateableOracleObjectPermissionSet objects to convert.</param>
        /// <returns>The converted list.</returns>
        public List<Containers.OracleObjectPermissionSet> Convert(List<IGenerateableOracleObjectPermissionSet> inputList)
        {
            List<Containers.OracleObjectPermissionSet> returnList = new List<Containers.OracleObjectPermissionSet>();

            foreach (IGenerateableOracleObjectPermissionSet currentGenerateableOracleObjectPermissionSet in inputList)
            {
                Containers.OracleObjectPermissionSet newOracleObjectPermissionSet = new Containers.OracleObjectPermissionSet();
                newOracleObjectPermissionSet.ObjectName = currentGenerateableOracleObjectPermissionSet.ObjectName;
                newOracleObjectPermissionSet.ObjectType = currentGenerateableOracleObjectPermissionSet.ObjectType;
                newOracleObjectPermissionSet.ObjectOwner = currentGenerateableOracleObjectPermissionSet.ObjectOwner;
                newOracleObjectPermissionSet.AddFlag = currentGenerateableOracleObjectPermissionSet.AddFlag;
                newOracleObjectPermissionSet.RemoveFlag = currentGenerateableOracleObjectPermissionSet.RemoveFlag;
                newOracleObjectPermissionSet.ObjectPermissions = Convert(currentGenerateableOracleObjectPermissionSet.ObjectPermissions);
                returnList.Add(newOracleObjectPermissionSet);
            }

            return returnList;
        }

        /// <summary>
        /// Converts an OraclePermissionGeneratorDataModel.ValidationResult into an OraclePermissionGeneratorWebServiceAPI.Containers.ValidationResult.
        /// </summary>
        /// <param name="inputValidationResult">The OraclePermissionGeneratorDataModel.ValidationResult to convert.</param>
        /// <returns>The converted ValidationResult.</returns>
        public OraclePermissionGeneratorWebServiceAPI.Containers.ValidationResult Convert(OraclePermissionGeneratorDataModel.ValidationResult inputValidationResult)
        {
            return new OraclePermissionGeneratorWebServiceAPI.Containers.ValidationResult() { IsValid = inputValidationResult.IsValid, ValidationError = inputValidationResult.ValidationError };
        }
    }
}
