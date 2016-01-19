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
using NUnit.Framework;
using OraclePermissionGeneratorWebServiceAPI;

namespace OraclePermissionGeneratorWebServiceAPI.UnitTests
{
    /// <summary>
    /// Unit tests for class OraclePermissionGeneratorWebServiceAPI.RestWebServiceApi.
    /// </summary>
    [TestFixture]
    public class RestWebServiceApiTests
    {
        // NOTE: RestWebServiceApi really just serves to proxy method calls to class WebServiceApiBase.  The reason it exists is so that WCF REST method attributes can be set on its interface IRestWebServiceApi.
        //         Hence no tests are defined for this class.

        [SetUp]
        protected void SetUp()
        {
        }
    }
}
