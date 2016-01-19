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
using OraclePermissionGeneratorDataInterfaceModel;
using OraclePermissionGeneratorWebServiceAPI.Containers;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Exposes the data interface layer of the Oracle Permission Generator application as a REST web service.
    /// </summary>
    public class RestWebServiceApi : WebServiceApiBase, IRestWebServiceApi
    {
        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.RestWebServiceApi class.
        /// </summary>
        /// <param name="userDataRepository">Dictionary object mapping unique user identifiers to OraclePermissionGeneratorDataInterfaceLayer objects.  Stores a copy of the data model for all users of the web service API.</param>
        public RestWebServiceApi(Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer> userDataRepository)
            : base(userDataRepository)
        {
        }

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.RestWebServiceApi class.
        /// </summary>
        /// <param name="userDataRepository">Dictionary object mapping unique user identifiers to OraclePermissionGeneratorDataInterfaceLayer objects.  Stores a copy of the data model for all users of the web service API.</param>
        /// <param name="trackingDataLogger">The logger used to log tracking data.</param>
        public RestWebServiceApi(Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer> userDataRepository, ITrackingDataLogger trackingDataLogger)
            : base(userDataRepository, trackingDataLogger)
        {
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetDefaultObjectOwner(System.String,System.String)"]/*'/>
        public override String GetDefaultObjectOwner(String authenticationContext, String trackingData)
        {
            return base.GetDefaultObjectOwner(authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.SetDefaultObjectOwner(System.String,System.String,System.String)"]/*'/>
        public override void SetDefaultObjectOwner(String authenticationContext, String trackingData, String defaultObjectOwner)
        {
            base.SetDefaultObjectOwner(defaultObjectOwner, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetObjectTypes(System.String,System.String)"]/*'/>
        public override List<String> GetObjectTypes(String authenticationContext, String trackingData)
        {
            return base.GetObjectTypes(authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetRoles(System.String,System.String)"]/*'/>
        public override List<String> GetRoles(String authenticationContext, String trackingData)
        {
            return base.GetRoles(authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetPermissions(System.String,System.String,System.String)"]/*'/>
        public override List<String> GetPermissions(String objectType, String authenticationContext, String trackingData)
        {
            return base.GetPermissions(objectType, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetPermissionsForObject(System.String,System.String,System.String,System.String)"]/*'/>
        public override List<String> GetPermissionsForObject(String objectName, String role, String authenticationContext, String trackingData)
        {
            return base.GetPermissionsForObject(objectName, role, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetMasterRoleToUserMapCollection(System.String,System.String)"]/*'/>
        public override List<RoleToUserMap> GetMasterRoleToUserMapCollection(String authenticationContext, String trackingData)
        {
            return base.GetMasterRoleToUserMapCollection(authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.AddObjectPermissionSet(System.String,System.String,System.String,System.Boolean,System.Boolean,System.Collections.Generic.List{OraclePermissionGeneratorWebServiceAPI.Containers.RoleToPermissionMap},System.String,System.String)"]/*'/>
        public override void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, bool addFlag, bool removeFlag, List<RoleToPermissionMap> objectPermissions, String authenticationContext, String trackingData)
        {
            base.AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RemoveObjectPermissionSet(System.String,System.String,System.String)"]/*'/>
        public override void RemoveObjectPermissionSet(String objectName, String authenticationContext, String trackingData)
        {
            base.RemoveObjectPermissionSet(objectName, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.ObjectNameValidate(System.String,System.String,System.String)"]/*'/>
        public override Containers.ValidationResult ObjectNameValidate(String objectName, String authenticationContext, String trackingData)
        {
            return base.ObjectNameValidate(objectName, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.ObjectTypeValidate(System.String,System.String,System.String)"]/*'/>
        public override Containers.ValidationResult ObjectTypeValidate(String objectType, String authenticationContext, String trackingData)
        {
            return base.ObjectTypeValidate(objectType, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.ObjectOwnerValidate(System.String,System.String,System.String)"]/*'/>
        public override Containers.ValidationResult ObjectOwnerValidate(String objectOwner, String authenticationContext, String trackingData)
        {
            return base.ObjectOwnerValidate(objectOwner, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetObjects(System.String,System.String)"]/*'/>
        public override List<Containers.OracleObjectPermissionSet> GetObjects(String authenticationContext, String trackingData)
        {
            return base.GetObjects(authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.SetAddFlag(System.String,System.Boolean,System.String,System.String)"]/*'/>
        public override void SetAddFlag(String objectName, bool addFlagValue, String authenticationContext, String trackingData)
        {
            base.SetAddFlag(objectName, addFlagValue, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.SetRemoveFlag(System.String,System.Boolean,System.String,System.String)"]/*'/>
        public override void SetRemoveFlag(String objectName, bool removeFlagValue, String authenticationContext, String trackingData)
        {
            base.SetRemoveFlag(objectName, removeFlagValue, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.AddPermission(System.String,System.String,System.String,System.String,System.String)"]/*'/>
        public override void AddPermission(String objectName, String role, String permission, String authenticationContext, String trackingData)
        {
            base.AddPermission(objectName, role, permission, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RemovePermission(System.String,System.String,System.String,System.String,System.String)"]/*'/>
        public override void RemovePermission(String objectName, String role, String permission, String authenticationContext, String trackingData)
        {
            base.RemovePermission(objectName, role, permission, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RoleGetReferencingObjects(System.String,System.String,System.String)"]/*'/>
        public override List<String> RoleGetReferencingObjects(String role, String authenticationContext, String trackingData)
        {
            return base.RoleGetReferencingObjects(role, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.AddRoleToUserMap(System.String,System.String,System.String,System.String)"]/*'/>
        public override void AddRoleToUserMap(String role, String user, String authenticationContext, String trackingData)
        {
            base.AddRoleToUserMap(role, user, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RemoveRoleToUserMap(System.String,System.String,System.String,System.String)"]/*'/>
        public override void RemoveRoleToUserMap(String role, String user, String authenticationContext, String trackingData)
        {
            base.RemoveRoleToUserMap(role, user, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RoleToUserMapValidate(System.String,System.String,System.String,System.String)"]/*'/>
        public override Containers.ValidationResult RoleToUserMapValidate(String role, String user, String authenticationContext, String trackingData)
        {
            return base.RoleToUserMapValidate(role, user, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.CreatePrivilegeScript(System.String,System.Boolean,System.String,System.String)"]/*'/>
        public override String CreatePrivilegeScript(String scriptType, bool generateRevokeStatements, String authenticationContext, String trackingData)
        {
            return base.CreatePrivilegeScript(scriptType, generateRevokeStatements, authenticationContext, trackingData);
        }

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.CreateSynonymScript(System.String,System.String,System.String)"]/*'/>
        public override String CreateSynonymScript(String scriptType, String authenticationContext, String trackingData)
        {
            return base.CreateSynonymScript(scriptType, authenticationContext, trackingData);
        }
    }
}
