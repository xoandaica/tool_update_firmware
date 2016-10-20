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

import java.util.List;

import vn.vnpttech.ssdc.nms.criteria.AreaDeviceMappingCriteria;
import vn.vnpttech.ssdc.nms.model.AreaDeviceMapping;

/**
 *
 * @author Dell
 */
public interface AreaDeviceMappingDao extends GenericDao<AreaDeviceMapping, Long>{

	int count(AreaDeviceMappingCriteria searchCriteria);

	List<AreaDeviceMapping> searchCriteria(AreaDeviceMappingCriteria searchCriteria);

	boolean saveList(List<AreaDeviceMapping> list, int mapping);
    
	List<AreaDeviceMapping> getBy(String serialNumber, String macAddress);
}
