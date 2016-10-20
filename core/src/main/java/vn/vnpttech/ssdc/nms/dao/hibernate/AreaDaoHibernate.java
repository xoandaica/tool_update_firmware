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
package vn.vnpttech.ssdc.nms.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import vn.vnpttech.ssdc.nms.dao.AreaDao;
import vn.vnpttech.ssdc.nms.model.Area;

/**
 *
 * @author Dell
 */
public class AreaDaoHibernate extends GenericDaoHibernate<Area, Long> implements AreaDao {

    
    
    public AreaDaoHibernate() {
        super(Area.class);
    }

    @Override
    public List<Area> searchArea(String areaName_Search, String parentAreaId_Search) {
        try {
            log.debug("start AreaDaoHibernate: searchArea");
            List<Area> result = new ArrayList<Area>();
            Criteria criteria = getSession().createCriteria(Area.class);
            if (StringUtils.isNotBlank(areaName_Search)) {
                criteria.add(Restrictions.like("name", "%" + areaName_Search + "%").ignoreCase());
            }
            if (StringUtils.isNotBlank(parentAreaId_Search)) {
                criteria.add(Restrictions.eq("area.id", Long.parseLong(parentAreaId_Search)));
            }
            result = criteria.list();
            return result;
        } catch (Exception ex) {
            log.error("ERROR searchArea: ", ex);
            return null;
        }
    }

    @Override
    public List<Area> getAllProvince() {
        try {
            List<Area> result = new ArrayList<Area>();
            Criteria criteria = getSession().createCriteria(Area.class);
            criteria.add(Restrictions.eq("areaType", 1));//province
            result = criteria.list();
            return result;
        } catch (Exception ex) {
            log.error("ERROR getAllProvince: ", ex);
            return null;
        }
    }

    @Override
    public List<Area> getDistrictByProvinceId(String provinceId) {
        try {
            List<Area> result = new ArrayList<Area>();
            if (StringUtils.isNotEmpty(provinceId)) {
                Criteria criteria = getSession().createCriteria(Area.class);
                criteria.add(Restrictions.eq("areaType", 2));//district
                criteria.add(Restrictions.eq("area.id", Long.parseLong(provinceId.trim())));
                result = criteria.list();
            }
            return result;
        } catch (Exception ex) {
            log.error("ERROR getDistrictByProvinceId: ", ex);
            return null;
        }
    }

    @Override
    public List<Area> getDistrictByProvinceListId(String provinceListId) {
        try {
            List<Area> result = new ArrayList<Area>();
            if (StringUtils.isNotBlank(provinceListId)) {
                Criteria criteria = getSession().createCriteria(Area.class);
                criteria.add(Restrictions.eq("areaType", 2));//district\
                if (provinceListId.contains(",")) {
                    String[] provincePieces = provinceListId.split(",");
                    List<Long> lstPro = new ArrayList<Long>();
                    for (String tmp : provincePieces) {
                        lstPro.add(Long.parseLong(tmp));
                    }
                    criteria.add(Restrictions.in("area.id", lstPro));
                } else {
                    criteria.add(Restrictions.eq("area.id", Long.parseLong(provinceListId.trim())));
                }
                result = criteria.list();
            }
            return result;
        } catch (Exception ex) {
            log.error("ERROR getDistrictByProvinceId: ", ex);
            return null;
        }
    }

    @Override
    public Area getAreaByName(String name) {
        Criteria criteria = getSession().createCriteria(Area.class);
        if (StringUtils.isNotBlank(name)) {
            criteria.add(Restrictions.like("name", name));
            List<Area> areas = null;
            areas = criteria.list();
            if (areas != null && areas.size() > 0) {
                return areas.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    

}
