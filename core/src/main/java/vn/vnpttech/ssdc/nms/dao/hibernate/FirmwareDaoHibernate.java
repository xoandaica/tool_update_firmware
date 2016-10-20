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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import vn.vnpttech.ssdc.nms.dao.FirmwareDao;
import vn.vnpttech.ssdc.nms.model.Firmware;

/**
 *
 * @author Dell
 */
public class FirmwareDaoHibernate extends GenericDaoHibernate<Firmware, Long> implements FirmwareDao {

    public FirmwareDaoHibernate() {
        super(Firmware.class);
    }

    @Override
    public List<Firmware> searchFirmware(Long modelId, String version, Date startDate, Date endDate, Integer start, Integer limit) {
        List<Firmware> l = new ArrayList<Firmware>();
        try {
            Criteria c = getSession().createCriteria(Firmware.class);
            if (modelId != null) {
                c.add(Restrictions.eq("deviceModel.id", modelId));
            }
            if (StringUtils.isNotBlank(version)) {
                c.add(Restrictions.like("version", version, MatchMode.ANYWHERE));
            }

            if (startDate != null) {
                c.add(Restrictions.ge("releaseDate", startDate));
            }
            if (endDate != null) {
                c.add(Restrictions.le("releaseDate", endDate));
            }
            if (start != null && limit != null) {
                c.setFirstResult(start);
                c.setMaxResults(start + limit);
            }
            c.addOrder(Order.desc("id"));

            l = c.list();
            return l;

        } catch (Exception e) {
            log.error(e.getMessage());
//            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public Long countFirmware(Long modelId, String version, Date startDate, Date endDate) {

        Long ret = null;
        try {
            Criteria c = getSession().createCriteria(Firmware.class);
            if (modelId != null) {
                c.add(Restrictions.eq("deviceModel.id", modelId));
            }
            if (StringUtils.isNotBlank(version)) {
                c.add(Restrictions.like("version", version, MatchMode.ANYWHERE));
            }

            if (startDate != null) {
                c.add(Restrictions.ge("releaseDate", startDate));
            }
            if (endDate != null) {
                c.add(Restrictions.le("releaseDate", endDate));
            }
            c.setProjection(Projections.rowCount());
            List results = null;
            results = c.list();
            if (results != null) {
                ret = (Long) results.get(0);
            }
            return ret;
        } catch (Exception e) {
            log.error(e.getMessage());
//            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Integer deleteList(List<Long> ids) {

        String hql = "Delete from Firmware where id in ( :id )";
        Query q = getSession().createQuery(hql);
        q.setParameterList("id", ids);
        int result = -1;
        result = q.executeUpdate();
        return result;

    }

    @Override
    public boolean checkNewFirmware(Timestamp t, String modelName, String version) {
        String hql = "Select count(*) from Firmware where (releaseDate >= :releaseDate and deviceModel.name like :name) or version like :version ";
        Query q = getSession().createQuery(hql);
        q.setParameter("releaseDate", t);
        q.setParameter("name", modelName);
        q.setParameter("version", version);
        Long ret = (Long) q.uniqueResult();
        if (ret != null && ret > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Firmware getByModelVersion(String modelName, String version) {
        try {
            String hql = " From Firmware where  deviceModel.name like :name and version like :version";
            Query q = getSession().createQuery(hql);
            q.setParameter("name", modelName);
            q.setParameter("version", version);
            List<Firmware> fs = new ArrayList<Firmware>();
            fs = q.list();
            if (fs != null && fs.size() > 0) {
                return fs.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
//            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean checkFirmwareExist(Long modelId, String fwVersion) {
        String hql = "Select count(*) from Firmware where  deviceModel.id = :model_id and version like :version ";
        Query q = getSession().createQuery(hql);
        q.setParameter("model_id", modelId);
        q.setParameter("version", fwVersion);
        Long ret = (Long) q.uniqueResult();
        if (ret != null && ret > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateDefaultFw(Long id, Long modelId) {
        Query query = getSession().createQuery("update Firmware set fwDefault= 0 where id <> :id and deviceModel.id = :model_id ");
        query.setParameter("id", id);
        query.setParameter("model_id", modelId);
        System.out.println(query.getQueryString() + "-------------------");
        int result = query.executeUpdate();
    }

    @Override
    public List<Firmware> getFirmwareDefault() {
        Criteria c = getSession().createCriteria(Firmware.class);
        c.add(Restrictions.eq("fwDefault", 1));
        c.addOrder(Order.desc("id"));
        List<Firmware> l = c.list();
        return l;

    }

}
