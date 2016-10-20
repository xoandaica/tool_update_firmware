/*
 * Copyright 2015 Pivotal Software, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.vnpttech.ssdc.nms.dao;

import java.sql.Timestamp;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.model.Policy;
import vn.vnpttech.ssdc.nms.model.PolicyHistory;

/**
 *
 * @author longdq
 */
public class PolicyHistoryTest extends BaseDaoTestCase {

    @Autowired
    PolicyHistoryDao pld;

    public PolicyHistoryTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {
         PolicyHistory policyHis = new PolicyHistory();
                policyHis.setStartTime(new Timestamp(new Date().getTime()));
                policyHis.setDescription("Update firmware by Policy ");
                policyHis.setDeviceSerialNumber("123456");
                policyHis.setEndTime(null);
                
                Policy p = new Policy();
                p.setId(1L);
                
                Device d = new Device();
                d.setId(1L);
                
                policyHis.setDevice(d);
                policyHis.setPolicy(p);
                policyHis.setFirmwareOldVersion("v1bc");
                policyHis.setStatus(2);
                
                policyHis = pld.save(policyHis);
                
                System.out.println(policyHis.getPolicy().getName());
                
    }
}
