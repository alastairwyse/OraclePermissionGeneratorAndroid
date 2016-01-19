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
using NMock2;
using OraclePermissionGeneratorWebServiceAPI.Containers;

namespace OraclePermissionGeneratorWebServiceAPI.UnitTests
{
    /// <summary>
    /// Derivation of the NMock2.Matcher class which allows Location classes to be compared when used as parameters in NMock2 Expect() method calls.
    /// </summary>
    class LocationMatcher: Matcher
    {
        private Location locationToMatch;

        /// <summary>
        /// Initialises a new instance of the OraclePermissionGeneratorWebServiceAPI.UnitTests.LocationMatcher class.
        /// </summary>
        /// <param name="locationToMatch">The 'expected' LocationMatcher (as opposed to the 'actual' one provided in the method call.</param>
        public LocationMatcher(Location locationToMatch)
        {
            this.locationToMatch = locationToMatch;
        }

        public override bool Matches(object o)
        {
            if (locationToMatch.GetType() != o.GetType())
            {
                return false;
            }

            Location comparisonLocation = (Location)o;
            if ((locationToMatch.Latitude != comparisonLocation.Latitude) || 
                (locationToMatch.Longitude != comparisonLocation.Longitude) ||
                (locationToMatch.SecondsSinceUpdate != comparisonLocation.SecondsSinceUpdate))
            {
                return false;
            }

            return true;
        }

        public override void DescribeTo(System.IO.TextWriter writer)
        {
            writer.Write(locationToMatch.GetType().Name + "(" + locationToMatch.Latitude + ", " + locationToMatch.Longitude + ", " + locationToMatch.SecondsSinceUpdate + ")");
        }
    }
}
