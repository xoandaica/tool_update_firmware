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
import vn.vnpttech.ssdc.nms.dao.AreaDao;
import vn.vnpttech.ssdc.nms.model.Area;
import vn.vnpttech.ssdc.nms.service.AreaManager;

/**
 *
 * @author Dell
 */
public class AreaManagerImpl extends GenericManagerImpl<Area, Long> implements AreaManager{
    @Autowired
    AreaDao areaDao;

    public AreaManagerImpl() {
    }

    public AreaManagerImpl( AreaDao areaDao) {
        super(areaDao);
    }
    
    public AreaDao getAreaDao() {
        return areaDao;
    }

    public void setAreaDao(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    @Override
    public List<Area> searchArea(String areaName_Search, String parentAreaName_Search) {
        return areaDao.searchArea(areaName_Search, parentAreaName_Search);
    }

    @Override
    public List<Area> getAllProvince() {
        return areaDao.getAllProvince();
    }

    @Override
    public List<Area> getDistrictByProvinceId(String provinceId) {
        return areaDao.getDistrictByProvinceId(provinceId);
    }

    @Override
    public List<Area> getDistrictByProvinceListId(String provinceListId) {
        return areaDao.getDistrictByProvinceListId(provinceListId);
    }

    @Override
    public Area getAreaByName(String name) {
        return areaDao.getAreaByName(name);
    }
    
    
}
