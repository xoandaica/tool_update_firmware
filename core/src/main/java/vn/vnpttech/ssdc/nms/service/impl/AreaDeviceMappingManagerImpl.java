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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import vn.vnpttech.ssdc.nms.criteria.AreaDeviceMappingCriteria;
import vn.vnpttech.ssdc.nms.dao.AreaDeviceMappingDao;
import vn.vnpttech.ssdc.nms.model.AreaDeviceMapping;
import vn.vnpttech.ssdc.nms.service.AreaDeviceMappingManager;

/**
 *
 * @author Dell
 */
public class AreaDeviceMappingManagerImpl extends GenericManagerImpl<AreaDeviceMapping, Long> implements AreaDeviceMappingManager {

	@Autowired
	AreaDeviceMappingDao areaDeviceMappingDao;

	public AreaDeviceMappingDao getAreaDeviceMappingDao() {
        return areaDeviceMappingDao;
    }

    public void setAreaDeviceMappingDao(AreaDeviceMappingDao areaDeviceMappingDao) {
        this.areaDeviceMappingDao = areaDeviceMappingDao;
    }

    public AreaDeviceMappingManagerImpl() {
    }

    public AreaDeviceMappingManagerImpl(AreaDeviceMappingDao areaDeviceMappingDao) {
        super(areaDeviceMappingDao);
    }
    
    @Override
    public int count(AreaDeviceMappingCriteria searchCriteria){
    	return areaDeviceMappingDao.count(searchCriteria);
    }

    @Override
	public List<AreaDeviceMapping> searchCriteria(AreaDeviceMappingCriteria searchCriteria){
		return areaDeviceMappingDao.searchCriteria(searchCriteria);
	}
    
    @Override
    public boolean saveList(List<AreaDeviceMapping> list, int mapping){
    	return areaDeviceMappingDao.saveList(list, mapping);
    }

    @Override
    public List<AreaDeviceMapping> getBy(String serialNumber, String macAddress) {
       return areaDeviceMappingDao.getBy(serialNumber, macAddress);
    }
}
