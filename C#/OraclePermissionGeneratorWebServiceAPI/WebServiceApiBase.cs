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
using OraclePermissionGeneratorDataModel;
using OraclePermissionGeneratorDataInterfaceModel;
using OraclePermissionGeneratorPersistenceModel;
using OraclePermissionGeneratorWebServiceAPI.Containers;
using OraclePermissionGeneratorWebServiceAPI.Containers.Converters;

namespace OraclePermissionGeneratorWebServiceAPI
{
    /// <summary>
    /// Base class containing common functionality for classes which expose the data interface layer of the Oracle Permission Generator application as a web service.
    /// </summary>
    public abstract class WebServiceApiBase
    {
        /// <summary>Holds a copy of the Oracle Permission Generator data model for all users of the web service API.</summary>
        protected Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer> userDataRepository;
        /// <summary>Used to convert objects in the OraclePermissionGeneratorDataModel namespace to and from equivalent objects in the OraclePermissionGeneratorWebServiceAPI.Containers namespace.</summary>
        protected ContainerObjectConverter containerObjectConverter;
        /// <summary>Used to log tracking data passed to the methods exposed by the web service.</summary>
        protected ITrackingDataLogger trackingDataLogger;
        /// <summary>Used to serialize and deserialize container objects to and from JSON.</summary>
        protected ContainerObjectJsonSerializer jsonSerializer;

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.WebServiceApiBase class.
        /// </summary>
        /// <param name="userDataRepository">Dictionary object mapping unique user identifiers to OraclePermissionGeneratorDataInterfaceLayer objects.  Stores a copy of the data model for all users of the web service API.</param>
        protected WebServiceApiBase(Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer> userDataRepository)
        {
            this.userDataRepository = userDataRepository;
            containerObjectConverter = new ContainerObjectConverter();
            trackingDataLogger = new NullTrackingDataLogger();
            jsonSerializer = new ContainerObjectJsonSerializer();
        }

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.WebServiceApiBase class.
        /// </summary>
        /// <param name="userDataRepository">Dictionary object mapping unique user identifiers to OraclePermissionGeneratorDataInterfaceLayer objects.  Stores a copy of the data model for all users of the web service API.</param>
        /// <param name="trackingDataLogger">The logger used to log tracking data.</param>
        protected WebServiceApiBase(Dictionary<string, OraclePermissionGeneratorDataInterfaceLayer> userDataRepository, ITrackingDataLogger trackingDataLogger)
            : this(userDataRepository)
        {
            this.trackingDataLogger = trackingDataLogger;
        }

        /// <summary>
        /// Adds a user into the data layer.
        /// </summary>
        /// <param name="userIdentifier">A unique identifier for the user.</param>
        public void AddUser(String userIdentifier)
        {
            // Throw an exception is the user identifier is blank
            if (userIdentifier.Trim() == "")
            {
                throw new ArgumentException("Parameter 'userIdentifier' cannot be blank.", "userIdentifier");
            }

            // Throw an exception if the user identifier already exists in the data repository
            if (userDataRepository.ContainsKey(userIdentifier) == true)
            {
                throw new ArgumentException("User identifier '" + userIdentifier + "' already exists.", "userIdentifier");
            }

            OraclePermissionGeneratorDataInterfaceLayer userDataInterfaceLayer = new OraclePermissionGeneratorDataInterfaceLayer();
            // Setup default object type to permission maps
            // TODO: Eventually the ability to modify the object type to permission maps should be exposed through the web service API
            userDataInterfaceLayer.AddObjectTypeToPermissionMap("Table", "SELECT");
            userDataInterfaceLayer.AddObjectTypeToPermissionMap("Table", "INSERT");
            userDataInterfaceLayer.AddObjectTypeToPermissionMap("Table", "UPDATE");
            userDataInterfaceLayer.AddObjectTypeToPermissionMap("Table", "DELETE");
            userDataInterfaceLayer.AddObjectTypeToPermissionMap("View", "SELECT");
            userDataInterfaceLayer.AddObjectTypeToPermissionMap("Stored Procedure", "EXECUTE");

            userDataRepository.Add(userIdentifier, userDataInterfaceLayer);
        }

        /// <summary>
        /// Loads an Oracle Permission Generator data model for the specified user.
        /// </summary>
        /// <param name="userIdentifier">The unique identifier of the user to load the data model for.</param>
        /// <param name="dataFilePath">The full path to the file containing the data model.</param>
        /// <param name="xmlSchemaFilePath">The full path to the XML schema file to use to validate the data model file.</param>
        public void LoadDataModelFromFile(String userIdentifier, String dataFilePath, String xmlSchemaFilePath)
        {
            // Throw an exception if the user identifier does not in the data repository
            if (userDataRepository.ContainsKey(userIdentifier) == false)
            {
                throw new ArgumentException("User identifier '" + userIdentifier + "' does not exist.", "userIdentifier");
            }

            userDataRepository[userIdentifier].XmlSchemaFileName = xmlSchemaFilePath;
            try
            {
                userDataRepository[userIdentifier].Load(dataFilePath);
            }
            catch (Exception e)
            {
                throw new Exception("Failed to open data model data file at path '" + dataFilePath + "'.", e);
            }
        }

        /// <summary>
        /// Gets the default object owner for new object permission sets.
        /// </summary>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The default object owner.</returns>
        public virtual String GetDefaultObjectOwner(String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            String defaultObjectOwner = userDataRepository[deserializedAuthenticationContext.UserIdentifier].DefaultObjectOwner;

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "GetDefaultObjectOwner()", trackingData);

            return defaultObjectOwner;
        }

        /// <summary>
        /// Sets the default object owner for new object permission sets.
        /// </summary>
        /// <param name="defaultObjectOwner">The default object owner.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void SetDefaultObjectOwner(String defaultObjectOwner, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].DefaultObjectOwner = defaultObjectOwner;

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "SetDefaultObjectOwner(defaultObjectOwner)", trackingData);
        }

        /// <summary>
        /// Returns a list of all object types in the data layer.
        /// </summary>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns></returns>
        public virtual List<String> GetObjectTypes(String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            List<String> objectTypes = userDataRepository[deserializedAuthenticationContext.UserIdentifier].GetObjectTypes();

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "GetObjectTypes()", trackingData);

            return objectTypes;
        }

        /// <summary>
        /// Returns a list of all roles in the data layer.
        /// </summary>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The roles in the data layer.</returns>
        public virtual List<String> GetRoles(String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            List<String> roles = userDataRepository[deserializedAuthenticationContext.UserIdentifier].GetRoles();

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "GetRoles()", trackingData);

            return roles;
        }

        /// <summary>
        /// Returns a list of all available permissions for the specified object in the data layer.
        /// </summary>
        /// <param name="objectType">The object to return the permission for.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The available permissions in the data layer.</returns>
        public virtual List<String> GetPermissions(String objectType, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            List<String> permissions = userDataRepository[deserializedAuthenticationContext.UserIdentifier].GetPermissions(objectType);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "GetPermissions(objectType)", trackingData);

            return permissions;
        }

        /// <summary>
        /// Returns a list of all permissions on the specified object assigned to the specified role in the data layer.
        /// Note this method had to be renamed from the GetPermissions() method in the OraclePermissionGeneratorDataInterfaceLayer class, as WCF does not allow method overloading.
        /// </summary>
        /// <param name="objectName">The object to return the permissions for.</param>
        /// <param name="role">The role to return the permissions for.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The permissions for the object and role in the data layer.</returns>
        public virtual List<String> GetPermissionsForObject(String objectName, String role, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            List<String> permissions = userDataRepository[deserializedAuthenticationContext.UserIdentifier].GetPermissions(objectName, role);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "GetPermissionsForObject(objectName, role)", trackingData);

            return permissions;
        }

        /// <summary>
        /// Returns the master list of role to user mappings in the data layer.
        /// </summary>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The master list of role to user mappings.</returns>
        public virtual List<RoleToUserMap> GetMasterRoleToUserMapCollection(String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            IRoleToUserMapCollection roleToUserMapCollection = userDataRepository[deserializedAuthenticationContext.UserIdentifier].GetMasterRoleToUserMapCollection();
            List<RoleToUserMap> roleToUserMapList = containerObjectConverter.Convert(roleToUserMapCollection);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "GetMasterRoleToUserMapCollection()", trackingData);

            return roleToUserMapList;
        }

        /// <summary>
        /// Adds an Oracle object permission set to the data layer.
        /// </summary>
        /// <param name="objectName">The name of the object.</param>
        /// <param name="objectType">The type of the object.</param>
        /// <param name="objectOwner">The user who owns the object.</param>
        /// <param name="addFlag">Specifies whether statements should be generated to add this object when generating Oracle scripts.</param>
        /// <param name="removeFlag">Specifies whether statements should be generated to remove this object when generating Oracle scripts.</param>
        /// <param name="objectPermissions">The set of permissions for the object.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void AddObjectPermissionSet(String objectName, String objectType, String objectOwner, bool addFlag, bool removeFlag, List<RoleToPermissionMap> objectPermissions, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            IRoleToPermissionMapCollection convertedObjectPermissions = containerObjectConverter.Convert(objectPermissions);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, convertedObjectPermissions);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "AddObjectPermissionSet(objectName, objectType, objectOwner, addFlag, removeFlag, objectPermissions)", trackingData);
        }

        /// <summary>
        /// Removes an Oracle object permission set from the data layer.
        /// </summary>
        /// <param name="objectName">The name of the object.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void RemoveObjectPermissionSet(String objectName, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].RemoveObjectPermissionSet(objectName);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "RemoveObjectPermissionSet(objectName)", trackingData);
        }

        /// <summary>
        /// Checks whether the inputted object name is valid in the data model.
        /// </summary>
        /// <param name="objectName">The object name.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The result of the validation.</returns>
        public virtual Containers.ValidationResult ObjectNameValidate(String objectName, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            OraclePermissionGeneratorDataModel.ValidationResult validationResult = userDataRepository[deserializedAuthenticationContext.UserIdentifier].ObjectNameValidate(objectName);
            Containers.ValidationResult returnValidationResult = containerObjectConverter.Convert(validationResult);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "ObjectNameValidate(objectName)", trackingData);

            return returnValidationResult;
        }

        /// <summary>
        /// Checks whether the inputted object type is valid in the data model.
        /// </summary>
        /// <param name="objectType">The object type.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The result of the validation.</returns>
        public virtual Containers.ValidationResult ObjectTypeValidate(String objectType, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            OraclePermissionGeneratorDataModel.ValidationResult validationResult = userDataRepository[deserializedAuthenticationContext.UserIdentifier].ObjectTypeValidate(objectType);
            Containers.ValidationResult returnValidationResult = containerObjectConverter.Convert(validationResult);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "ObjectTypeValidate(objectType)", trackingData);

            return returnValidationResult;
        }

        /// <summary>
        /// Checks whether the inputted object owner is valid in the data model.
        /// </summary>
        /// <param name="objectOwner">The object owner.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The result of the validation.</returns>
        public virtual Containers.ValidationResult ObjectOwnerValidate(String objectOwner, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            OraclePermissionGeneratorDataModel.ValidationResult validationResult = userDataRepository[deserializedAuthenticationContext.UserIdentifier].ObjectOwnerValidate(objectOwner);
            Containers.ValidationResult returnValidationResult = containerObjectConverter.Convert(validationResult);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "ObjectOwnerValidate(objectOwner)", trackingData);

            return returnValidationResult;
        }

        /// <summary>
        /// Returns a list of all defined Oracle objects.
        /// </summary>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>A list of all defined Oracle objects.</returns>
        public virtual List<Containers.OracleObjectPermissionSet> GetObjects(String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            List<IGenerateableOracleObjectPermissionSet> permissionSetList = userDataRepository[deserializedAuthenticationContext.UserIdentifier].GetObjects();
            List<Containers.OracleObjectPermissionSet> returnObjects = containerObjectConverter.Convert(permissionSetList);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "GetObjects()", trackingData);

            return returnObjects;
        }

        /// <summary>
        /// Sets the add flag of the specified Oracle object.
        /// </summary>
        /// <param name="objectName">The name of the object to set the add flag for.</param>
        /// <param name="addFlagValue">The value of the add flag.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void SetAddFlag(String objectName, bool addFlagValue, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].SetAddFlag(objectName, addFlagValue);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "SetAddFlag(objectName, addFlagValue)", trackingData);
        }

        /// <summary>
        /// Sets the remove flag of the specified Oracle object.
        /// </summary>
        /// <param name="objectName">The name of the object to set the remove flag for.</param>
        /// <param name="removeFlagValue">The value of the remove flag.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void SetRemoveFlag(String objectName, bool removeFlagValue, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].SetRemoveFlag(objectName, removeFlagValue);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "SetRemoveFlag(objectName, removeFlagValue)", trackingData);
        }

        /// <summary>
        /// Adds a new permission to a specified Oracle object and role.
        /// </summary>
        /// <param name="objectName">The name of the object to add the permission for.</param>
        /// <param name="role">The role to apply the permission to.</param>
        /// <param name="permission">The permission to add.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void AddPermission(String objectName, String role, String permission, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].AddPermission(objectName, role, permission);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "AddPermission(objectName, role, permission)", trackingData);
        }

        /// <summary>
        /// Removes a permission from a specified Oracle object and role.
        /// </summary>
        /// <param name="objectName">The name of the object to remove the permission from.</param>
        /// <param name="role">The role to remove the permission from.</param>
        /// <param name="permission">The permission to remove.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void RemovePermission(String objectName, String role, String permission, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].RemovePermission(objectName, role, permission);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "RemovePermission(objectName, role, permission)", trackingData);
        }

        /// <summary>
        /// Returns a list of names of objects which reference the inputted role.
        /// </summary>
        /// <param name="role">The name of the role.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>A list of objects which reference the specified role.</returns>
        public virtual List<String> RoleGetReferencingObjects(String role, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            List<String> referencingObjects = userDataRepository[deserializedAuthenticationContext.UserIdentifier].RoleGetReferencingObjects(role);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "RoleGetReferencingObjects(role)", trackingData);

            return referencingObjects;
        }

        /// <summary>
        /// Adds a role to user mapping to the master lookup list.
        /// </summary>
        /// <param name="role">The name of the role.</param>
        /// <param name="user">The name of the user.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void AddRoleToUserMap(String role, String user, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].AddRoleToUserMap(role, user);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "AddRoleToUserMap(role, user)", trackingData);
        }

        /// <summary>
        /// Removes a role to user mapping from the master lookup list.
        /// </summary>
        /// <param name="role">The name of the role.</param>
        /// <param name="user">The name of the user.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        public virtual void RemoveRoleToUserMap(String role, String user, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            userDataRepository[deserializedAuthenticationContext.UserIdentifier].RemoveRoleToUserMap(role, user);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "RemoveRoleToUserMap(role, user)", trackingData);
        }

        /// <summary>
        /// Checks whether the inputted role to user map is valid in the data model.
        /// </summary>
        /// <param name="role">The name of the role.</param>
        /// <param name="user">The name of the user.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The result of the validation.</returns>
        public virtual Containers.ValidationResult RoleToUserMapValidate(String role, String user, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            OraclePermissionGeneratorDataModel.ValidationResult validationResult = userDataRepository[deserializedAuthenticationContext.UserIdentifier].RoleToUserMapValidate(role, user);
            Containers.ValidationResult returnValidationResult = containerObjectConverter.Convert(validationResult);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "RoleToUserMapValidate(role, user)", trackingData);

            return returnValidationResult;
        }

        /// <summary>
        /// Generates an Oracle privilege script.
        /// </summary>
        /// <param name="scriptType">The type of script to generate.</param>
        /// <param name="generateRevokeStatements">Whether revoke statments should be included in the script.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns></returns>
        public virtual String CreatePrivilegeScript(String scriptType, bool generateRevokeStatements, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);
            ScriptType deserializedScriptType = DeserializeScriptType(scriptType);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            PermissionScriptGenerator permissionScriptGenerator = BuildPermissionScriptGenerator(userDataRepository[deserializedAuthenticationContext.UserIdentifier]);
            String privilegeScript = permissionScriptGenerator.CreatePrivilegeScript(deserializedScriptType, generateRevokeStatements);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "CreatePrivilegeScript(scriptType, generateRevokeStatements)", trackingData);

            return privilegeScript;
        }

        /// <summary>
        /// Generates an Oracle synonym script.
        /// </summary>
        /// <param name="scriptType">The type of script to generate.</param>
        /// <param name="authenticationContext">The authentication context of the web service consumer or user.</param>
        /// <param name="trackingData">Tracking information of the web service consumer or user.</param>
        /// <returns>The Oracle synonym script.</returns>
        public virtual String CreateSynonymScript(String scriptType, String authenticationContext, String trackingData)
        {
            // Deserialize parameters
            AuthenticationContext deserializedAuthenticationContext = jsonSerializer.DeserializeAuthenticationContext(authenticationContext);
            ScriptType deserializedScriptType = DeserializeScriptType(scriptType);

            // Call data layer method
            ValidateUser(deserializedAuthenticationContext.UserIdentifier);
            PermissionScriptGenerator permissionScriptGenerator = BuildPermissionScriptGenerator(userDataRepository[deserializedAuthenticationContext.UserIdentifier]);
            String synonymScript = permissionScriptGenerator.CreateSynonymScript(deserializedScriptType);

            LogTrackingData(deserializedAuthenticationContext.UserIdentifier, "CreateSynonymScript(scriptType)", trackingData);

            return synonymScript;
        }

        /// <summary>
        /// Throws an exception if the inputted user cannot be validated.
        /// </summary>
        /// <param name="userIdentifier">The unique identifier of the user to validate.</param>
        protected void ValidateUser(string userIdentifier)
        {
            if (userDataRepository.ContainsKey(userIdentifier) == false)
            {
                throw new Exception("User '" + userIdentifier + "' not recognized.");
            }
        }

        /// <summary>
        /// If the provided serialized tracking data parameter is not null, deserializes the tracking data and logs it.
        /// </summary>
        /// <param name="userIdentifier">The identifier of the consumer/user of the web service.</param>
        /// <param name="methodName">The name of the web service API method that was called.</param>
        /// <param name="serializedTrackingData">The serialized tracking data of the consumer/user of the web service.</param>
        protected void LogTrackingData(string userIdentifier, string methodName, String serializedTrackingData)
        {
            try
            {
                Location location = null;
                byte[] ipAddress = null;

                if (serializedTrackingData != "")
                {
                    TrackingData deserializedTrackingData = jsonSerializer.DeserializeTrackingData(serializedTrackingData);
                    location = deserializedTrackingData.Location;
                    ipAddress = deserializedTrackingData.IpV4Address;
                }

                trackingDataLogger.Log(DateTime.UtcNow, userIdentifier, methodName, location, ipAddress);
            }
            catch (Exception e)
            {
                throw new Exception("Failed to deserialize and log tracking data.", e);
            }
        }

        /// <summary>
        /// Creates PermissionScriptGenerator object from the inputted OraclePermissionGeneratorDataInterfaceLayer.
        /// </summary>
        /// <param name="dataInterfaceLayer">The OraclePermissionGeneratorDataInterfaceLayer to use to build the PermissionScriptGenerator</param>
        /// <returns>A PermissionScriptGenerator object.</returns>
        protected PermissionScriptGenerator BuildPermissionScriptGenerator(OraclePermissionGeneratorDataInterfaceLayer dataInterfaceLayer)
        {
            // Constructor for PermissionScriptGenerator requires GenerateableOracleObjectPermissionSetCollection, but this is not directly available from the inputted data interface layer, hence need to create it
            GenerateableOracleObjectPermissionSetCollection collection = new GenerateableOracleObjectPermissionSetCollection();
            foreach (IGenerateableOracleObjectPermissionSet currentPermissionSet in dataInterfaceLayer.GetObjects())
            {
                collection.Add(currentPermissionSet);
            }
            PermissionScriptGenerator permissionScriptGenerator = new PermissionScriptGenerator(dataInterfaceLayer.GetMasterRoleToUserMapCollection(), collection);
            return permissionScriptGenerator;
        }

        /// <summary>
        /// Attempts to deserialize the inputted string into a ScriptType enum.
        /// </summary>
        /// <param name="serializedScriptType">The serialized ScriptType.</param>
        /// <returns>The deserialized ScriptType.</returns>
        protected ScriptType DeserializeScriptType(String serializedScriptType)
        {
            ScriptType returnScriptType;
            bool result = Enum.TryParse<ScriptType>(serializedScriptType, out returnScriptType);
            if (result == false)
            {
                throw new ArgumentException("The string '" + serializedScriptType + "' could not be converted to a valid script type.");
            }
            else
            {
                return returnScriptType;
            }
        }
    }
}
