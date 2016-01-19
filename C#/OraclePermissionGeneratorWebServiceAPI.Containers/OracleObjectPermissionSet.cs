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
using System.Runtime.Serialization;

namespace OraclePermissionGeneratorWebServiceAPI.Containers
{
    /// <summary>
    /// Container class equivalent to OraclePermissionGeneratorDataModel.OracleObjectPermissionSet to allow serialization using the DataContract attribute.
    /// </summary>
    /// <remarks>Many classes in the OraclePermissionGeneratorDataModel namespace cannot be directly serialized using the DataContract attribute, as they either implement IXmlSerializable or do not define set properties on all data members.</remarks>
    [DataContract]
    public class OracleObjectPermissionSet
    {
        private bool addFlag;
        private bool removeFlag;
        private string objectName;
        private string objectType;
        private string objectOwner;
        private List<RoleToPermissionMap> objectPermissions;

        [DataMember]
        public bool AddFlag
        {
            set
            {
                addFlag = value;
            }
            get
            {
                return addFlag;
            }
        }

        [DataMember]
        public bool RemoveFlag
        {
            set
            {
                removeFlag = value;
            }
            get
            {
                return removeFlag;
            }
        }

        [DataMember]
        public string ObjectName
        {
            set
            {
                objectName = value;
            }
            get
            {
                return objectName;
            }
        }

        [DataMember]
        public string ObjectType
        {
            set
            {
                objectType = value;
            }
            get
            {
                return objectType;
            }
        }

        [DataMember]
        public string ObjectOwner
        {
            set
            {
                objectOwner = value;
            }
            get
            {
                return objectOwner;
            }
        }

        [DataMember]
        public List<RoleToPermissionMap> ObjectPermissions
        {
            set
            {
                objectPermissions = value;
            }
            get
            {
                return objectPermissions;
            }
        }

        public OracleObjectPermissionSet()
        {
        }
    }
}
