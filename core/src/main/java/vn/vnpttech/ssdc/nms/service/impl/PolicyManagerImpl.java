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
package vn.vnpttech.ssdc.nms.service.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vnpttech.ssdc.nms.dao.PolicyDao;
import vn.vnpttech.ssdc.nms.model.Policy;
import vn.vnpttech.ssdc.nms.service.PolicyManager;

/**
 *
 * @author Dell
 */
public class PolicyManagerImpl extends GenericManagerImpl<Policy, Long> implements PolicyManager {

    @Autowired
    PolicyDao policyDao;

    public PolicyDao getPolicyDao() {
        return policyDao;
    }

    public void setPolicyDao(PolicyDao policyDao) {
        this.policyDao = policyDao;
    }

    public PolicyManagerImpl() {
    }

    public PolicyManagerImpl(PolicyDao policyDao) {
        super(policyDao);
    }

    @Override
    public Map searchPolicy(String policyName, String policyStatus, String enable, String startTime, String endTime, Long start, Long limit) {
        return policyDao.searchPolicy(policyName, policyStatus, enable, startTime, endTime, start, limit);
    }

    @Override
    public List<Policy> getPolicyStart(String currentTime)  {
        return policyDao.getPolicyStart(currentTime);
    }
}
