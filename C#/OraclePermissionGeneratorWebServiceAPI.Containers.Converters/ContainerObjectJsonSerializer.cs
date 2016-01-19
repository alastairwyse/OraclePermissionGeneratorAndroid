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
using System.IO;
using System.Runtime.Serialization.Json;
using OraclePermissionGeneratorWebServiceAPI.Containers;

namespace OraclePermissionGeneratorWebServiceAPI.Containers.Converters
{
    /// <summary>
    /// Provides methods to serialize and deserialize container objects in the OraclePermissionGeneratorWebServiceAPI.Containers namespace using JSON.
    /// </summary>
    public class ContainerObjectJsonSerializer
    {
        // TODO: Consider whether this should be moved to OraclePermissionGeneratorWebServiceAPI.Containers or OraclePermissionGeneratorWebServiceAPI.Containers.Serialization

        private Encoding characterEncoding;

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.ContainerObjectJsonSerializer class.
        /// </summary>
        public ContainerObjectJsonSerializer()
        {
            characterEncoding = new UTF8Encoding(false);
        }

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.ContainerObjectJsonSerializer class.
        /// </summary>
        /// <param name="characterEncoding">The character encoding to use when converting between underlying streams and strings.</param>
        public ContainerObjectJsonSerializer(Encoding characterEncoding)
        {
            this.characterEncoding = characterEncoding;
        }

        /// <summary>
        /// Serializes a list of OracleObjectPermissionSet objects to a JSON-encoded string.
        /// </summary>
        /// <param name="permissionSetArray">The list of OracleObjectPermissionSet objects to serialize.</param>
        /// <returns>The list serialized as a string.</returns>
        public String Serialize(List<Containers.OracleObjectPermissionSet> permissionSetArray)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(List<Containers.OracleObjectPermissionSet>));
            MemoryStream tempStream = new MemoryStream();
            serializer.WriteObject(tempStream, permissionSetArray);
            return ConvertMemoryStreamToString(tempStream);
        }

        /// <summary>
        /// Serializes a list of strings to a JSON-encoded string.
        /// </summary>
        /// <param name="strings">The list of strings to serialize.</param>
        /// <returns>The list serialized as a string.</returns>
        public String Serialize(List<String> strings)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(List<String>));
            MemoryStream tempStream = new MemoryStream();
            serializer.WriteObject(tempStream, strings);
            return ConvertMemoryStreamToString(tempStream);
        }

        /// <summary>
        /// Serializes a ValidationResult to a JSON-encoded string.
        /// </summary>
        /// <param name="validationResult">The ValidationResult to serialize.</param>
        /// <returns>The ValidationResult serialized as a string.</returns>
        public String Serialize(ValidationResult validationResult)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(ValidationResult));
            MemoryStream tempStream = new MemoryStream();
            serializer.WriteObject(tempStream, validationResult);
            return ConvertMemoryStreamToString(tempStream);
        }

        /// <summary>
        /// Serializes a list of RoleToUserMap objects to a JSON-encoded string.
        /// </summary>
        /// <param name="roleToUserMapList">The list of RoleToUserMap objects to serialize.</param>
        /// <returns>The list serialized as a string.</returns>
        public String Serialize(List<RoleToUserMap> roleToUserMapList)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(List<RoleToUserMap>));
            MemoryStream tempStream = new MemoryStream();
            serializer.WriteObject(tempStream, roleToUserMapList);
            return ConvertMemoryStreamToString(tempStream);
        }

        /// <summary>
        /// Deserializes an AuthenticationContext from a JSON-encoded string.
        /// </summary>
        /// <param name="serializedAuthenticationContext">A JSON-encoded string contaning the AuthenticationContext.</param>
        /// <returns>The AuthenticationContext.</returns>
        public AuthenticationContext DeserializeAuthenticationContext(String serializedAuthenticationContext)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(AuthenticationContext));
            using (MemoryStream tempStream = ConvertStringToMemoryStream(serializedAuthenticationContext))
            {
                return (AuthenticationContext)serializer.ReadObject(tempStream);
            }
        }

        /// <summary>
        /// Deserializes a TrackingData object from a JSON-encoded string.
        /// </summary>
        /// <param name="serializedTrackingData">A JSON-encoded string contaning the TrackingData object.</param>
        /// <returns>The TrackingData.</returns>
        public TrackingData DeserializeTrackingData(String serializedTrackingData)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(TrackingData));
            using (MemoryStream tempStream = ConvertStringToMemoryStream(serializedTrackingData))
            {
                return (TrackingData)serializer.ReadObject(tempStream);
            }
        }

        public List<RoleToPermissionMap> DeserializeRoleToPermissionMapList(String serializedRoleToPermissionMapList)
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(List<RoleToPermissionMap>));
            using (MemoryStream tempStream = ConvertStringToMemoryStream(serializedRoleToPermissionMapList))
            {
                return (List<RoleToPermissionMap>)serializer.ReadObject(tempStream);
            }
        }

        #region Private Methods

        /// <summary>
        /// Converts a string to a System.IO.MemoryStream object.
        /// </summary>
        /// <param name="inputString">The string to be converted.</param>
        /// <returns>The string converted to a MemoryStream.</returns>
        private MemoryStream ConvertStringToMemoryStream(string inputString)
        {
            MemoryStream targetStream = new MemoryStream();
            StreamWriter targetStreamWriter = new StreamWriter(targetStream, characterEncoding);

            targetStreamWriter.Write(inputString);
            targetStreamWriter.Flush();
            targetStream.Position = 0;

            return targetStream;
        }

        /// <summary>
        /// Converts the contents of a System.IO.MemoryStream object to a string.
        /// </summary>
        /// <param name="inputMemoryStream">The MemoryStream to be converted.</param>
        /// <returns>The contents of the MemoryStream.</returns>
        /// <remarks>Note that Position property of the MemoryStream will be altered.</remarks>
        private string ConvertMemoryStreamToString(MemoryStream inputMemoryStream)
        {
            using (StreamReader sourceStreamReader = new StreamReader(inputMemoryStream, characterEncoding))
            {
                inputMemoryStream.Position = 0;
                return sourceStreamReader.ReadToEnd();
            }
        }

        #endregion
    }
}
