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
    /// Container class holding the physical location (in terms of latitude and longitude) of a consumer/user of the web service.
    /// </summary>
    [DataContract]
    public class Location
    {
        private double latitude;
        private double longitude;
        private int secondsSinceUpdate;

        /// <summary>
        /// The latitude of the location.
        /// </summary>
        [DataMember]
        public double Latitude
        {
            get
            {
                return latitude;
            }
            set
            {
                latitude = value;
            }
        }

        /// <summary>
        /// The longitude of the location.
        /// </summary>
        [DataMember]
        public double Longitude
        {
            get
            {
                return longitude;
            }
            set
            {
                longitude = value;
            }
        }

        /// <summary>
        /// The number of seconds that have elapsed since the location was recorded.
        /// </summary>
        [DataMember]
        public int SecondsSinceUpdate
        {
            get
            {
                return secondsSinceUpdate;
            }
            set
            {
                secondsSinceUpdate = value;
            }
        }
    }
}
