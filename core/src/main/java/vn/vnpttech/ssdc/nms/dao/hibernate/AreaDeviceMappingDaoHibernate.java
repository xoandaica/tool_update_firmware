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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import vn.vnpttech.ssdc.nms.criteria.AreaDeviceMappingCriteria;
import vn.vnpttech.ssdc.nms.dao.AreaDeviceMappingDao;
import vn.vnpttech.ssdc.nms.model.AreaDeviceMapping;

/**
 *
 * @author Cuongnm
 */
@Repository("areaDeviceMappingDao")
public class AreaDeviceMappingDaoHibernate extends GenericDaoHibernate<AreaDeviceMapping, Long> implements AreaDeviceMappingDao{

    public AreaDeviceMappingDaoHibernate() {
        super(AreaDeviceMapping.class);
    }

    @Override
    public int count(AreaDeviceMappingCriteria searchCriteria){
    	if(searchCriteria == null){
    		searchCriteria = new AreaDeviceMappingCriteria();
    	}
		
		Criteria cr = getSession().createCriteria(AreaDeviceMapping.class);
    	
		if(StringUtils.isNotBlank(searchCriteria.getSerialNumber())){
    		cr.add(Restrictions.like("serialNumber", "%" + searchCriteria.getSerialNumber() + "%"));
    	}
		
		if(StringUtils.isNotBlank(searchCriteria.getMacAddress())){
    		cr.add(Restrictions.like("macAddress", "%" + searchCriteria.getMacAddress() + "%"));
    	}
    	
    	if(StringUtils.isNotBlank(searchCriteria.getProvince())){
    		cr.add(Restrictions.like("province", "%" + searchCriteria.getProvince() + "%"));
    	}
    	
    	if(StringUtils.isNotBlank(searchCriteria.getDistrict())){
    		cr.add(Restrictions.like("district", "%" + searchCriteria.getDistrict() + "%"));
    	}
    	
    	if(StringUtils.isNotBlank(searchCriteria.getIp())){
    		cr.add(Restrictions.like("ip", "%" + searchCriteria.getIp() + "%"));
    	}
    	
    	return ((Number)cr.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    @SuppressWarnings("unchecked")
	@Override
	public List<AreaDeviceMapping> searchCriteria(AreaDeviceMappingCriteria searchCriteria){
    	if(searchCriteria == null){
    		searchCriteria = new AreaDeviceMappingCriteria();
    	}
    	
    	Criteria cr = getSession().createCriteria(AreaDeviceMapping.class);
    	
    	if(StringUtils.isNotBlank(searchCriteria.getSerialNumber())){
    		cr.add(Restrictions.like("serialNumber", "%" + searchCriteria.getSerialNumber() + "%"));
    	}
    	
    	if(StringUtils.isNotBlank(searchCriteria.getMacAddress())){
    		cr.add(Restrictions.like("macAddress", "%" + searchCriteria.getMacAddress() + "%"));
    	}
    	
    	if(StringUtils.isNotBlank(searchCriteria.getProvince())){
    		cr.add(Restrictions.like("province", "%" + searchCriteria.getProvince() + "%"));
    	}
    	
    	if(StringUtils.isNotBlank(searchCriteria.getDistrict())){
    		cr.add(Restrictions.like("district", "%" + searchCriteria.getDistrict() + "%"));
    	}
    	
    	if(StringUtils.isNotBlank(searchCriteria.getIp())){
    		cr.add(Restrictions.like("ip", "%" + searchCriteria.getIp() + "%"));
    	}
    	
    	cr.setMaxResults(searchCriteria.getLimit());
    	cr.setFirstResult(searchCriteria.getStart());
    	
    	
    	return cr.list();
	}
    
    @SuppressWarnings("unchecked")
	@Override
    public List<AreaDeviceMapping> getBy(String serialNumber, String macAddress){
    	Criteria cr = getSession().createCriteria(AreaDeviceMapping.class);
    	
    	if(StringUtils.isNotBlank(serialNumber)){
    		cr.add(Restrictions.like("serialNumber", "%" + serialNumber + "%"));
    	}
    	
    	else if(StringUtils.isNotBlank(macAddress)){
    		cr.add(Restrictions.like("macAddress", "%" + macAddress + "%"));
    	}
    	
    	else {
    		return null;
    	}
    	
    	return cr.list();
    }
    
    @Override
    public boolean saveList(List<AreaDeviceMapping> list, int mapping){
    	//Open a other session to comit, not get current session
    	Session session = getSessionFactory().openSession();
    	
    	try {
    		session.getTransaction().begin();
    		
    		if(list != null){
    			for(AreaDeviceMapping item : list){
    				List<AreaDeviceMapping> records = null;
    				switch (mapping) {
						case 1: //serial number
							records = this.getBy(item.getSerialNumber(), null);
							break;
							
						case 2: //mac address
							records = this.getBy(null, item.getMacAddress());
							break;
							
						case 3: //all
							records = this.getBy(item.getSerialNumber(), item.getMacAddress());
							break;
	
						default:
							break;
					}
    				
    				if(records != null && records.size() > 0){
    					item.setId(records.get(0).getId());
    				}
    				
    				this.save(item);
    			}
    		}
    		
    		session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();  
			session.getTransaction().rollback();
			return false;
		}finally {
			session.close();
		}  
    	
    	return true;
    	
    }
}
