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
    /// Container class equivalent to OraclePermissionGeneratorDataModel.StringMapper to allow serialization using the DataContract attribute.
    /// </summary>
    /// <remarks>Many classes in the OraclePermissionGeneratorDataModel namespace cannot be directly serialized using the DataContract attribute, as they either implement IXmlSerializable or do not define set properties on all data members.</remarks>
    [DataContract]
    public class RoleToUserMap
    {
        private string role;
        private string user;

        [DataMember]
        public string Role
        {
            set
            {
                role = value;
            }
            get
            {
                return role;
            }
        }

        [DataMember]
        public string User
        {
            set
            {
                user = value;
            }
            get
            {
                return user;
            }
        }

        public RoleToUserMap()
        {
        }
    }
}
