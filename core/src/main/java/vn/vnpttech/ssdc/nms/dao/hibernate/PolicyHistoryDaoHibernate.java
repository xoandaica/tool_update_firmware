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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vn.vnpttech.ssdc.nms.dao.PolicyHistoryDao;
import vn.vnpttech.ssdc.nms.model.PolicyHistory;

/**
 *
 * @author Dell
 */
public class PolicyHistoryDaoHibernate extends GenericDaoHibernate<PolicyHistory, Long> implements PolicyHistoryDao {

    public PolicyHistoryDaoHibernate() {
        super(PolicyHistory.class);
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public List<PolicyHistory> getPolicyHistory(String serialNo, Integer status) {
        List<PolicyHistory> lph = new ArrayList<PolicyHistory>();
        Criteria c = getSession().createCriteria(PolicyHistory.class);
        if (StringUtils.isNotBlank(serialNo)) {
            c.add(Restrictions.like("deviceSerialNumber", serialNo));
        }
        if (status != null) {
            c.add(Restrictions.eq("status", status));
        }

        c.add(Restrictions.isNull("endTime"));
        lph = c.list();
        return lph;
    }

    @Override
    public Map searchPolicyHistory(String policyId, String deviceId, Long start, Long limit) {
        try {
            int total = 0;
            Map pagingMap = new HashMap();

            List<PolicyHistory> listPolicyHistory = new ArrayList<>();
            Criteria criteria = getSession().createCriteria(PolicyHistory.class);
            //Policy
            if (StringUtils.isNotBlank(policyId)) {
                criteria.add(Restrictions.eq("policy.id", Long.parseLong(policyId.trim())));
            }
            //Device
            if (StringUtils.isNotBlank(deviceId)) {
                criteria.add(Restrictions.eq("device.id", Long.parseLong(deviceId.trim())));
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

            listPolicyHistory = criteria.list();
            pagingMap.put("list", listPolicyHistory);
            pagingMap.put("totalCount", Long.parseLong(String.valueOf(total)));
            return pagingMap;
        } catch (Exception ex) {
            log.error("ERROR searchPolicyHistory: ", ex);
            return null;
        }
    }

    @Override
    public void deletePolicyHistory(String deviceIdList) {
        try {
            if (StringUtils.isNotBlank(deviceIdList)) {
                String[] listId = deviceIdList.split(",");
                List<Long> ids = new ArrayList<Long>();
                for (String idTmp : listId) {
                    ids.add(Long.parseLong(idTmp));
                }
                //Delete
                Session session = getSession();
                String hql = "DELETE FROM PolicyHistory ph WHERE ph.device.id IN (:list)";
                Query query = session.createQuery(hql);
                query.setParameterList("list", ids);
                query.executeUpdate();
            }

        } catch (Exception ex) {
            log.error("ERROR deletePolicyHistory: ", ex);
        }
    }

}
