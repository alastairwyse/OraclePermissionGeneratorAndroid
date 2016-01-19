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
using System.Xml;
using OraclePermissionGeneratorDataModel;
using OraclePermissionGeneratorDataInterfaceModel;
using OraclePermissionGeneratorPersistenceModel;
using OraclePermissionGeneratorWebServiceAPI.Containers;
using OraclePermissionGeneratorWebServiceAPI.Containers.Converters;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Exposes the data interface layer of the Oracle Permission Generator application as a SOAP web service.
    /// </summary>
    public class SoapWebServiceApi : WebServiceApiBase, ISoapWebServiceApi
    {
        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.SoapWebServiceApi class.
        /// </summary>
        /// <param name="userDataRepository">Dictionary object mapping unique user identifiers to OraclePermissionGeneratorDataInterfaceLayer objects.  Stores a copy of the data model for all users of the web service API.</param>
        public SoapWebServiceApi(Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer> userDataRepository)
            : base(userDataRepository)
        {
        }

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.SoapWebServiceApi class.
        /// </summary>
        /// <param name="userDataRepository">Dictionary object mapping unique user identifiers to OraclePermissionGeneratorDataInterfaceLayer objects.  Stores a copy of the data model for all users of the web service API.</param>
        /// <param name="trackingDataLogger">The logger used to log tracking data.</param>
        public SoapWebServiceApi(Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer> userDataRepository, ITrackingDataLogger trackingDataLogger)
            : base(userDataRepository, trackingDataLogger)
        {
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetDefaultObjectOwner(System.String,System.String)"]/*'/>
        public override String GetDefaultObjectOwner(String authenticationContext, String trackingData)
        {
            return base.GetDefaultObjectOwner(authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.SetDefaultObjectOwner(System.String,System.String,System.String)"]/*'/>
        public override void SetDefaultObjectOwner(String defaultObjectOwner, String authenticationContext, String trackingData)
        {
            base.SetDefaultObjectOwner(defaultObjectOwner, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetObjectTypes(System.String,System.String)"]/*'/>
        public String GetObjectTypes(String authenticationContext, String trackingData)
        {
            List<String> objectTypes = base.GetObjectTypes(authenticationContext, trackingData);
            return jsonSerializer.Serialize(objectTypes);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetRoles(System.String,System.String)"]/*'/>
        public String GetRoles(String authenticationContext, String trackingData)
        {
            List<String> roles = base.GetRoles(authenticationContext, trackingData);
            return jsonSerializer.Serialize(roles);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetPermissions(System.String,System.String,System.String)"]/*'/>
        public String GetPermissions(String objectType, String authenticationContext, String trackingData)
        {
            List<String> permissions = base.GetPermissions(objectType, authenticationContext, trackingData);
            return jsonSerializer.Serialize(permissions);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetPermissionsForObject(System.String,System.String,System.String,System.String)"]/*'/>
        public String GetPermissionsForObject(String objectName, String role, String authenticationContext, String trackingData)
        {
            List<String> permissions = base.GetPermissionsForObject(objectName, role, authenticationContext, trackingData);
            return jsonSerializer.Serialize(permissions);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetMasterRoleToUserMapCollection(System.String,System.String)"]/*'/>
        public String GetMasterRoleToUserMapCollection(String authenticationContext, String trackingData)
        {
            List<RoleToUserMap> roleToUserMapList = base.GetMasterRoleToUserMapCollection(authenticationContext, trackingData);
            return jsonSerializer.Serialize(roleToUserMapList);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.AddObjectPermissionSet(System.String,System.String,System.String,System.Boolean,System.Boolean,System.String,System.String,System.String)"]/*'/>
        public void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, bool addFlag, bool removeFlag, String objectPermissions, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            List<RoleToPermissionMap> deserializedObjectPermissions = jsonSerializer.DeserializeRoleToPermissionMapList(objectPermissions);

            base.AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, deserializedObjectPermissions, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RemoveObjectPermissionSet(System.String,System.String,System.String)"]/*'/>
        public override void RemoveObjectPermissionSet(String objectName, String authenticationContext, String trackingData)
        {
            base.RemoveObjectPermissionSet(objectName, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.ObjectNameValidate(System.String,System.String,System.String)"]/*'/>
        public String ObjectNameValidate(String objectName, String authenticationContext, String trackingData)
        {
            Containers.ValidationResult returnValidationResult = base.ObjectNameValidate(objectName, authenticationContext, trackingData);
            return jsonSerializer.Serialize(returnValidationResult);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.ObjectTypeValidate(System.String,System.String,System.String)"]/*'/>
        public String ObjectTypeValidate(String objectType, String authenticationContext, String trackingData)
        {
            Containers.ValidationResult returnValidationResult = base.ObjectTypeValidate(objectType, authenticationContext, trackingData);
            return jsonSerializer.Serialize(returnValidationResult);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.ObjectOwnerValidate(System.String,System.String,System.String)"]/*'/>
        public String ObjectOwnerValidate(String objectOwner, String authenticationContext, String trackingData)
        {
            Containers.ValidationResult returnValidationResult = base.ObjectOwnerValidate(objectOwner, authenticationContext, trackingData);
            return jsonSerializer.Serialize(returnValidationResult);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetObjects(System.String,System.String)"]/*'/>
        public String GetObjects(String authenticationContext, String trackingData)
        {
            List<Containers.OracleObjectPermissionSet> returnObjects = base.GetObjects(authenticationContext, trackingData);
            return jsonSerializer.Serialize(returnObjects);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.SetAddFlag(System.String,System.Boolean,System.String,System.String)"]/*'/>
        public override void SetAddFlag(String objectName, bool addFlagValue, String authenticationContext, String trackingData)
        {
            base.SetAddFlag(objectName, addFlagValue, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.SetRemoveFlag(System.String,System.Boolean,System.String,System.String)"]/*'/>
        public override void SetRemoveFlag(String objectName, bool removeFlagValue, String authenticationContext, String trackingData)
        {
            base.SetRemoveFlag(objectName, removeFlagValue, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.AddPermission(System.String,System.String,System.String,System.String,System.String)"]/*'/>
        public override void AddPermission(String objectName, String role, String permission, String authenticationContext, String trackingData)
        {
            base.AddPermission(objectName, role, permission, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RemovePermission(System.String,System.String,System.String,System.String,System.String)"]/*'/>
        public override void RemovePermission(String objectName, String role, String permission, String authenticationContext, String trackingData)
        {
            base.RemovePermission(objectName, role, permission, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RoleGetReferencingObjects(System.String,System.String,System.String)"]/*'/>
        public String RoleGetReferencingObjects(String role, String authenticationContext, String trackingData)
        {
            List<String> referencingObjects = base.RoleGetReferencingObjects(role, authenticationContext, trackingData);
            return jsonSerializer.Serialize(referencingObjects);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.AddRoleToUserMap(System.String,System.String,System.String,System.String)"]/*'/>
        public override void AddRoleToUserMap(String role, String user, String authenticationContext, String trackingData)
        {
            base.AddRoleToUserMap(role, user, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RemoveRoleToUserMap(System.String,System.String,System.String,System.String)"]/*'/>
        public override void RemoveRoleToUserMap(String role, String user, String authenticationContext, String trackingData)
        {
            base.RemoveRoleToUserMap(role, user, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RoleToUserMapValidate(System.String,System.String,System.String,System.String)"]/*'/>
        public String RoleToUserMapValidate(String role, String user, String authenticationContext, String trackingData)
        {
            Containers.ValidationResult returnValidationResult = base.RoleToUserMapValidate(role, user, authenticationContext, trackingData);
            return jsonSerializer.Serialize(returnValidationResult);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.CreatePrivilegeScript(System.String,System.Boolean,System.String,System.String)"]/*'/>
        public override String CreatePrivilegeScript(String scriptType, bool generateRevokeStatements, String authenticationContext, String trackingData)
        {
            return base.CreatePrivilegeScript(scriptType, generateRevokeStatements, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.CreateSynonymScript(System.String,System.String,System.String)"]/*'/>
        public override String CreateSynonymScript(String scriptType, String authenticationContext, String trackingData)
        {
            return base.CreateSynonymScript(scriptType, authenticationContext, trackingData);
        }
    }
}
