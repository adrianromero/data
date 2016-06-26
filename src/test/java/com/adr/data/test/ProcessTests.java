//     Data Access is a Java library to store data
//     Copyright (C) 2016 Adrián Romero Corchado.
//
//     This file is part of Data Access
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//     
//         http://www.apache.org/licenses/LICENSE-2.0
//     
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.
package com.adr.data.test;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.DataQueryLink;
import com.adr.data.RecordMap;
import com.adr.data.ValuesMap;
import com.adr.data.ValuesEntry;
import com.adr.data.security.SecureFacade;
import com.adr.data.var.VariantBoolean;
import com.adr.data.var.VariantString;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adrian
 */
public class ProcessTests {

    public ProcessTests() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void hello() throws DataException {

        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);
            secfac.login("admin", "admin");

            link.execute(
                new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", new VariantString("001"))),
                    new ValuesMap(
                        new ValuesEntry("name", "Spain"),
                        new ValuesEntry("hasRegion", VariantBoolean.TRUE),
                        new ValuesEntry("countrycode", "ES"))),
                new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "003")),
                    new ValuesMap(
                        new ValuesEntry("name", "Germany"),
                        new ValuesEntry("hasRegion", VariantBoolean.TRUE),
                        new ValuesEntry("countrycode", "DE"))),
                new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "004")),
                    new ValuesMap(
                        new ValuesEntry("name", "Italy"),
                        new ValuesEntry("hasRegion", VariantBoolean.TRUE),
                        new ValuesEntry("countrycode", "IT"))),
                new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "c_country"),
                        new ValuesEntry("c_country_id", "005")),
                    new ValuesMap(
                        new ValuesEntry("name", "Portugal"),
                        new ValuesEntry("hasRegion", true),
                        new ValuesEntry("countrycode", "PT"))));
            
            secfac.logout();
        }
    }
}
