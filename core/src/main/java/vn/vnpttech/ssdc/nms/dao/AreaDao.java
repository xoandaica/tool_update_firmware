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
import vn.vnpttech.ssdc.nms.model.Area;

/**
 *
 * @author Dell
 */
public interface AreaDao extends GenericDao<Area, Long> {

    public List<Area> searchArea(String areaName_Search, String parentAreaName_Search);

    public List<Area> getAllProvince();

    public List<Area> getDistrictByProvinceId(String provinceId);

    public List<Area> getDistrictByProvinceListId(String provinceListId);

    public Area getAreaByName(String name);

}
