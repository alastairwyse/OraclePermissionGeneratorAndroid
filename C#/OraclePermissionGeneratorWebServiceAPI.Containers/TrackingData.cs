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
using System.ComponentModel;
using System.ServiceModel.Dispatcher;

namespace OraclePermissionGeneratorWebServiceAPI.Containers
{
    /// <summary>
    /// Container class storing tracking information about a consumer/user of the web service.
    /// </summary>
    [DataContract]
    public class TrackingData
    {
        private Location location;
        private byte[] ipV4Address;

        /// <summary>
        /// The physical location of the user.
        /// </summary>
        [DataMember]
        public Location Location
        {
            get
            {
                return location;
            }
            set
            {
                location = value;
            }
        }

        /// <summary>
        /// The IPV4 address of the device consuming the web service.
        /// </summary>
        [DataMember]
        public byte[] IpV4Address
        {
            get
            {
                return ipV4Address;
            }
            set
            {
                ipV4Address = value;
            }
        }
    }
}
