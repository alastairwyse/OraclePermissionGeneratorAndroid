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
using OraclePermissionGeneratorWebServiceAPI.Containers;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Defines methods which expose the data interface layer of the Oracle Permission Generator application over a REST web service.
    /// </summary>
    [ServiceContract]
    public interface IRestWebServiceApi
    {
        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.AddUser(System.String)"]/*'/>
        void AddUser(String userIdentifier);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetDefaultObjectOwner(System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "DefaultObjectOwner?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        String GetDefaultObjectOwner(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.SetDefaultObjectOwner(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "DefaultObjectOwner?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "PUT")]
        void SetDefaultObjectOwner(String authenticationContext, String trackingData, String defaultObjectOwner);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetObjectTypes(System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "ObjectTypes?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<String> GetObjectTypes(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetRoles(System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "Roles?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<String> GetRoles(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetPermissions(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "Permissions/{objectType}?entity=objectType&authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<String> GetPermissions(String objectType, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetPermissionsForObject(System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "Permissions/{objectName}/{role}?entity=object,role&authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<String> GetPermissionsForObject(String objectName, String role, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetMasterRoleToUserMapCollection(System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "RoleToUserMappings?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<RoleToUserMap> GetMasterRoleToUserMapCollection(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.AddObjectPermissionSet(System.String,System.String,System.String,System.Boolean,System.Boolean,System.Collections.Generic.List{OraclePermissionGeneratorWebServiceAPI.Containers.RoleToPermissionMap},System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "Objects?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, bool addFlag, bool removeFlag, List<RoleToPermissionMap> objectPermissions, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RemoveObjectPermissionSet(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "Objects/{objectName}?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "DELETE")]
        void RemoveObjectPermissionSet(String objectName, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.ObjectNameValidate(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "/Validations/ObjectName/{objectName}?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Containers.ValidationResult ObjectNameValidate(String objectName, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.ObjectTypeValidate(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "/Validations/ObjectType/{objectType}?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Containers.ValidationResult ObjectTypeValidate(String objectType, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.ObjectOwnerValidate(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "/Validations/ObjectOwner/{objectOwner}?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Containers.ValidationResult ObjectOwnerValidate(String objectOwner, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.GetObjects(System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "Objects?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<Containers.OracleObjectPermissionSet> GetObjects(String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.SetAddFlag(System.String,System.Boolean,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "Objects/{objectName}/AddFlag?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "PUT")]
        void SetAddFlag(String objectName, bool addFlagValue, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.SetRemoveFlag(System.String,System.Boolean,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "Objects/{objectName}/RemoveFlag?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "PUT")]
        void SetRemoveFlag(String objectName, bool removeFlagValue, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.AddPermission(System.String,System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "Permissions?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        void AddPermission(String objectName, String role, String permission, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RemovePermission(System.String,System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "Permissions/{objectName}/{role}/{permission}?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "DELETE")]
        void RemovePermission(String objectName, String role, String permission, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RoleGetReferencingObjects(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "ReferencingObjects/{role}?entity=role&authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<String> RoleGetReferencingObjects(String role, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.AddRoleToUserMap(System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "RoleToUserMappings?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest)]
        void AddRoleToUserMap(String role, String user, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RemoveRoleToUserMap(System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebInvoke(UriTemplate = "RoleToUserMappings/{role}/{user}?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, Method = "DELETE")]
        void RemoveRoleToUserMap(String role, String user, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.RoleToUserMapValidate(System.String,System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "/Validations/RoleToUserMap/{role}/{user}?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Containers.ValidationResult RoleToUserMapValidate(String role, String user, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.CreatePrivilegeScript(System.String,System.Boolean,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "/PrivilegeScripts/{scriptType}?generateRevokeStatements={generateRevokeStatements}&authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        String CreatePrivilegeScript(String scriptType, bool generateRevokeStatements, String authenticationContext, String trackingData);

        /// <include file='InterfaceDocumentationComments.xml' path='doc/members/member[@name="M:OraclePermissionGeneratorWebServiceAPI.IRestWebServiceApi.CreateSynonymScript(System.String,System.String,System.String)"]/*'/>
        [OperationContract]
        [WebGet(UriTemplate = "/SynonymScripts/{scriptType}?authenticationContext={authenticationContext}&trackingData={trackingData}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        String CreateSynonymScript(String scriptType, String authenticationContext, String trackingData);
    }
}
