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
using System.ServiceModel.Web;
using OraclePermissionGeneratorPersistenceModel;
using OraclePermissionGeneratorWebServiceAPI.Containers;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Defines methods which expose the data interface layer of the Oracle Permission Generator application over a SOAP web service.
    /// </summary>
    [ServiceContract]
    public interface ISoapWebServiceApi
    {
        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.AddUser(System.String)"]/*'/>
        void AddUser(String userIdentifier);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetDefaultObjectOwner(System.String,System.String)"]/*'/>
        [OperationContract]
        String GetDefaultObjectOwner(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.SetDefaultObjectOwner(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        void SetDefaultObjectOwner(String defaultObjectOwner, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetObjectTypes(System.String,System.String)"]/*'/>
        [OperationContract]
        String GetObjectTypes(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetRoles(System.String,System.String)"]/*'/>
        [OperationContract]
        String GetRoles(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetPermissions(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        String GetPermissions(String objectType, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetPermissionsForObject(System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        String GetPermissionsForObject(String objectName, String role, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetMasterRoleToUserMapCollection(System.String,System.String)"]/*'/>
        [OperationContract]
        String GetMasterRoleToUserMapCollection(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.AddObjectPermissionSet(System.String,System.String,System.String,System.Boolean,System.Boolean,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, bool addFlag, bool removeFlag, String objectPermissions, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RemoveObjectPermissionSet(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        void RemoveObjectPermissionSet(String objectName, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.ObjectNameValidate(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        String ObjectNameValidate(String objectName, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.ObjectTypeValidate(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        String ObjectTypeValidate(String objectType, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.ObjectOwnerValidate(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        String ObjectOwnerValidate(String objectOwner, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.GetObjects(System.String,System.String)"]/*'/>
        [OperationContract]
        String GetObjects(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.SetAddFlag(System.String,System.Boolean,System.String,System.String)"]/*'/>
        [OperationContract]
        void SetAddFlag(String objectName, bool addFlagValue, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.SetRemoveFlag(System.String,System.Boolean,System.String,System.String)"]/*'/>
        [OperationContract]
        void SetRemoveFlag(String objectName, bool removeFlagValue, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.AddPermission(System.String,System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        void AddPermission(String objectName, String role, String permission, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RemovePermission(System.String,System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        void RemovePermission(String objectName, String role, String permission, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RoleGetReferencingObjects(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        String RoleGetReferencingObjects(String role, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.AddRoleToUserMap(System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        void AddRoleToUserMap(String role, String user, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RemoveRoleToUserMap(System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        void RemoveRoleToUserMap(String role, String user, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.RoleToUserMapValidate(System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        String RoleToUserMapValidate(String role, String user, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.CreatePrivilegeScript(System.String,System.Boolean,System.String,System.String)"]/*'/>
        [OperationContract]
        String CreatePrivilegeScript(String scriptType, bool generateRevokeStatements, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.ISoapWebServiceApi.CreateSynonymScript(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        String CreateSynonymScript(String scriptType, String authenticationContext, String trackingData);
    }
}
