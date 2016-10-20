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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vn.vnpttech.ssdc.nms.dao.PolicyDao;
import vn.vnpttech.ssdc.nms.model.Policy;
import vn.vnpttech.ssdc.nms.util.Constant;

/**
 *
 * @author Dell
 */
public class PolicyDaoHibernate extends GenericDaoHibernate<Policy, Long> implements PolicyDao {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public PolicyDaoHibernate() {
        super(Policy.class);
    }

    @Override
    public Map searchPolicy(String policyName, String policyStatus, String enable, String startTime, String endTime, Long start, Long limit) {
        try {

            int total = 0;
            Map pagingMap = new HashMap();

            List<Policy> listPolicy = new ArrayList<Policy>();
            Criteria criteria = getSession().createCriteria(Policy.class);
            //Name
            if (StringUtils.isNotBlank(policyName)) {
                criteria.add(Restrictions.like("name", "%" + policyName.trim() + "%").ignoreCase());
            }
            //Status
            if (StringUtils.isNotBlank(policyStatus)) {
                criteria.add(Restrictions.eq("policyStatus", Integer.parseInt(policyStatus.trim())));
            }
            //Enable
            if (StringUtils.isNotBlank(enable)) {
                criteria.add(Restrictions.eq("enable", Integer.parseInt(enable.trim())));
            }
            //Start Time & End Time
            if (StringUtils.isNotBlank(startTime)) {
                Date startTimeInDate = sdf.parse(startTime);
                criteria.add(Restrictions.ge("startTime", startTimeInDate));

            }
            if (StringUtils.isNotBlank(endTime)) {
                Date endTimeInDate = sdf.parse(endTime);
                criteria.add(Restrictions.le("endTime", endTimeInDate));
            }
            criteria.addOrder(Order.desc("id"));
            //Paging
            if (limit != null && limit > 0) {
                // get the count
                ScrollableResults results = criteria.scroll();
                results.last();
                total = results.getRowNumber() + 1;
                results.close();
                //End get count
                criteria.setFirstResult(start.intValue());
                criteria.setMaxResults(limit.intValue());
            }

            listPolicy = criteria.list();
            pagingMap.put("list", listPolicy);
            pagingMap.put("totalCount", Long.parseLong(String.valueOf(total)));
            return pagingMap;
        } catch (Exception ex) {
            log.error("ERROR searchPolicy: ", ex);
            return null;
        }
    }

    @Override
    public List<Policy> getPolicyStart(String currentTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(currentTime + "===============================");
        List<Policy> listPolicy = new ArrayList<Policy>();
        try {
            Criteria criteria = getSession().createCriteria(Policy.class);
            //Start Time & End Time
            // sdf.parse(currentTime);
            Date currentTimeInDate = new Date();//
            // System.out.println(currentTimeInDate.toString() + "++++++++++++++++++++++++");

            criteria.add(Restrictions.le("startTime", currentTimeInDate));
            criteria.add(Restrictions.ge("endTime", currentTimeInDate));

            //policy status is enable
            criteria.add(Restrictions.eq("enable", Constant.POLICY_ENABLE));

            // policy_status_not-run and running.
            List<Integer> status = new ArrayList<Integer>();
            status.add(Constant.POLICY_STATUS_DONE);
            status.add(Constant.POLICY_STATUS_RUNNING);
            Criterion policyNotRun = Restrictions.eq("policyStatus", Constant.POLICY_STATUS_NOT_RUN);

            Criterion policyDoneRunning = Restrictions.in("policyStatus", status);
            Criterion policyDeviceFailed = Restrictions.gt("deviceFailedNo", 0);

            Criterion andCri = Restrictions.and(policyDoneRunning, policyDeviceFailed);
            
            criteria.add(Restrictions.or(policyNotRun, andCri));

            //criteria.add(Restrictions.eq("policyStatus", Constant.POLICY_STATUS_NOT_RUN));
            listPolicy = criteria.list();

        } catch (HibernateException hibernateException) {
            log.error(hibernateException.getMessage());
            throw hibernateException;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }

        return listPolicy;

    }

}
