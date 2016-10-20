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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import vn.vnpttech.ssdc.nms.criteria.ActionLogsCriteria;
import vn.vnpttech.ssdc.nms.dao.ActionLogsDao;
import vn.vnpttech.ssdc.nms.model.ActionLogs;

/**
 *
 * @author longdq
 */
public class ActionLogsDaoHibernate extends GenericDaoHibernate<ActionLogs, Long> implements ActionLogsDao{

    public ActionLogsDaoHibernate() {
        super(ActionLogs.class);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<ActionLogs> getActionLogsByUsername(String username) {
    	Query qry = getSession().createQuery("from ActionLogs where username like '%?%'").setParameter(0, username);
        return qry.list();
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<ActionLogs> getActionLogs(String username, Date from, Date to, int offset, int limit){
    	Criteria cr = getSession().createCriteria(ActionLogs.class);
    	
    	if(username != null && !username.equals("")){
    		cr.add(Restrictions.like("username", "%" + username + "%"));
    	}
    	
    	if(from != null){
    		cr.add(Restrictions.ge("actionTime", from));
    	}
    	
    	if(to != null){
    		cr.add(Restrictions.le("actionTime", to));
    	}
    	
    	cr.addOrder(Order.desc("actionTime"));
    	
    	cr.setMaxResults(limit);
    	cr.setFirstResult(offset);
    	
    	
    	return cr.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
	public List<ActionLogs> searchCriteria(ActionLogsCriteria searchCriteria){
    	if(searchCriteria == null){
    		searchCriteria = new ActionLogsCriteria();
    	}
    	
    	Criteria cr = getSession().createCriteria(ActionLogs.class);
    	
    	if(StringUtils.isNotBlank(searchCriteria.getUsername())){
    		cr.add(Restrictions.like("username", "%" + searchCriteria.getUsername() + "%"));
    	}
    	
    	if(searchCriteria.getStartTime() != null){
    		cr.add(Restrictions.ge("actionTime", searchCriteria.getStartTime()));
    	}
    	
    	if(searchCriteria.getEndTime() != null){
    		cr.add(Restrictions.le("actionTime", searchCriteria.getEndTime()));
    	}
    	
    	cr.addOrder(Order.desc("actionTime"));
    	
    	cr.setMaxResults(searchCriteria.getLimit());
    	cr.setFirstResult(searchCriteria.getStart());
    	
    	
    	return cr.list();
    }

    @Override
	public int countActionLogs(ActionLogsCriteria searchCriteria){
		if(searchCriteria == null){
    		searchCriteria = new ActionLogsCriteria();
    	}
		
		Criteria crit = getSession().createCriteria(ActionLogs.class);
    	
		if(StringUtils.isNotBlank(searchCriteria.getUsername())){
			crit.add(Restrictions.like("username", "%" + searchCriteria.getUsername() + "%"));
    	}
    	
    	if(searchCriteria.getStartTime() != null){
    		crit.add(Restrictions.ge("actionTime", searchCriteria.getStartTime()));
    	}
    	
    	if(searchCriteria.getEndTime() != null){
    		crit.add(Restrictions.le("actionTime", searchCriteria.getEndTime()));
    	}
    	
    	return ((Number)crit.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

}
