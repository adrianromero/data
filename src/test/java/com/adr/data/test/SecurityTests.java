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
import com.adr.data.DataQueryLink;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.data.security.SecureFacade;
import com.adr.data.security.SecurityDataException;
import com.adr.data.var.VariantString;
import java.util.List;
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
public class SecurityTests {

    public SecurityTests() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLoginLogout() throws DataException {

        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);

            Record login = secfac.login("admin", "admin");

            // Assert loging success
            Assert.assertEquals("Administrator", login.getString("DISPLAYNAME"));
            Assert.assertEquals("System", login.getString("ROLE"));

            // this query succeds because admin has permissions to all resources
            List<Record> result1 = link.query(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "USERNAME"),
                    new ValuesEntry("ID", new VariantString("admin"))),
                new ValuesMap(
                    new ValuesEntry("NAME", VariantString.NULL),
                    new ValuesEntry("CODECARD", VariantString.NULL))));
            Assert.assertEquals(1, result1.size());

            secfac.logout();

            try {
                // This query fails because not logged users 
                link.query(new RecordMap(
                    new ValuesMap(
                        new ValuesEntry("_ENTITY", "USERNAME"),
                        new ValuesEntry("ID", new VariantString("admin"))),
                    new ValuesMap(
                        new ValuesEntry("NAME", VariantString.NULL),
                        new ValuesEntry("CODECARD", VariantString.NULL))));
                Assert.fail();
            } catch (SecurityDataException ex) {
                Assert.assertEquals("No authorization to query resource: USERNAME", ex.getMessage());
            }
        }
    }
    
    @Test
    public void testLoginManager() throws DataException {
        
        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);

            Record login = secfac.login("manager", null);       
            Assert.assertEquals("Manager", login.getString("DISPLAYNAME"));

            login = secfac.current();        
            Assert.assertEquals("Manager", login.getString("DISPLAYNAME"));

            secfac.logout();
            login = secfac.current();
            Assert.assertNull(login);
        }
    }
    
    @Test
    public void testAnonymous() throws DataException {
        
        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);      

            Record login = secfac.current();
            Assert.assertNull(login);  

            // this query succeds because anonymous has permissions to all resources
            List<Record> result1 = link.query(new RecordMap(
                new ValuesMap(
                    new ValuesEntry("_ENTITY", "USERNAME_VISIBLE"),
                    new ValuesEntry("ID", VariantString.NULL)),
                new ValuesMap(
                    new ValuesEntry("NAME", VariantString.NULL),
                    new ValuesEntry("DISPLAYNAME", VariantString.NULL))));
            Assert.assertEquals(3, result1.size());
        }
    }
    
    @Test
    public void testSave() throws DataException {
        
        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);

            Record login = secfac.login("manager", null);       
            Assert.assertEquals("Manager", login.getString("DISPLAYNAME"));

            login.getValue().set("DISPLAYNAME", new VariantString("ManagerUpdated"));

            Record login2 = secfac.saveCurrent(login);
            Assert.assertEquals("ManagerUpdated", login2.getString("DISPLAYNAME"));

            secfac.logout();

            login = secfac.login("manager", null);       
            Assert.assertEquals("ManagerUpdated", login.getString("DISPLAYNAME"));    

            login.getValue().set("DISPLAYNAME", new VariantString("Manager")); // restore value

            login2 = secfac.saveCurrent(login);
            Assert.assertEquals("Manager", login2.getString("DISPLAYNAME"));        

            secfac.logout();
        }
    }   
    
    @Test
    public void testSaveFails() throws DataException {
        
        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            
            SecureFacade secfac = new SecureFacade(link);

            Record loginmanager = secfac.login("manager", null);       
            Assert.assertEquals("Manager", loginmanager.getString("DISPLAYNAME"));
            secfac.logout();

            Record loginuser = secfac.login("user", null);       
            Assert.assertEquals("User", loginuser.getString("DISPLAYNAME"));

            try {
                Record loginsaved = secfac.saveCurrent(loginmanager);
                Assert.fail(); // Cannot save a differentuser
            } catch (SecurityDataException ex) {
                Assert.assertEquals("Trying to save a user different than authenticated user.", ex.getMessage());    
            }

            secfac.logout();  
        }
               System.out.println("2");
    }      
    
    @Test
    public void testAuthorizations() throws DataException {
        
        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);

            Assert.assertTrue(secfac.hasAuthorization("USERNAME_VISIBLE"));
            Assert.assertFalse(secfac.hasAuthorization("authenticatedres"));
            Assert.assertFalse(secfac.hasAuthorization("com/adr/hellocore/fxml/datalist?datatable=com/adr/hellocore/security/role"));        
            Assert.assertFalse(secfac.hasAuthorization("anyotherresource"));

            secfac.login("manager", null);  

            Assert.assertTrue(secfac.hasAuthorization("USERNAME_VISIBLE"));
            Assert.assertTrue(secfac.hasAuthorization("authenticatedres"));
            Assert.assertTrue(secfac.hasAuthorization("com/adr/hellocore/fxml/datalist?datatable=com/adr/hellocore/security/role"));
            Assert.assertFalse(secfac.hasAuthorization("anyotherresource"));

            secfac.login("user", null);  

            Assert.assertTrue(secfac.hasAuthorization("USERNAME_VISIBLE"));
            Assert.assertTrue(secfac.hasAuthorization("authenticatedres"));
            Assert.assertFalse(secfac.hasAuthorization("com/adr/hellocore/fxml/datalist?datatable=com/adr/hellocore/security/role"));
            Assert.assertFalse(secfac.hasAuthorization("anyotherresource"));

            secfac.login("admin", "admin");  

            Assert.assertTrue(secfac.hasAuthorization("USERNAME_VISIBLE"));
            Assert.assertTrue(secfac.hasAuthorization("authenticatedres"));
            Assert.assertTrue(secfac.hasAuthorization("com/adr/hellocore/fxml/datalist?datatable=com/adr/hellocore/security/role"));
            Assert.assertTrue(secfac.hasAuthorization("anyotherresource"));

            secfac.logout();
        }
               System.out.println("2");
    }
    
    @Test
    public void testQueryAuthorizations() throws DataException {
       System.out.println("2");
        
        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);       

            List<Record> auth1 = secfac.getCurrentRoleAuthorizations();
            Assert.assertEquals(0, auth1.size());

            secfac.login("manager", null);  
            auth1 = secfac.getCurrentRoleAuthorizations();
            Assert.assertEquals(3, auth1.size());

            secfac.login("user", null);  
            auth1 = secfac.getCurrentRoleAuthorizations();
            Assert.assertEquals(0, auth1.size());

            secfac.logout();  
            auth1 = secfac.getCurrentRoleAuthorizations();
            Assert.assertEquals(0, auth1.size());
        }
    }
    
    
    @Test
    public void testChangePasswords() throws DataException {
        try (DataQueryLink link = SourceLink.createDataQueryLink()) {
            SecureFacade secfac = new SecureFacade(link);       

            Record loginuser = secfac.login("user", null);  
            Assert.assertEquals("User", loginuser.getString("DISPLAYNAME"));
            
            secfac.savePassword("user", null, "pepeluis");
            
            try {
                secfac.savePassword("manager", "nope", "foo");
                Assert.fail(); // Cannot save a differentuser
            } catch (SecurityDataException ex) {
                Assert.assertEquals("Trying to save a user different than authenticated user.", ex.getMessage());    
            }           
            
            try {
                secfac.savePassword("user", "idontremember", "other");
                Assert.fail();
            } catch (SecurityDataException ex) {
                Assert.assertEquals("Invalid password.", ex.getMessage());    
            }           
            
            
            secfac.savePassword("user", "pepeluis", "joselito");

            secfac.savePassword("user", "joselito", null);

            secfac.logout();  
        }        
    }
    
}
