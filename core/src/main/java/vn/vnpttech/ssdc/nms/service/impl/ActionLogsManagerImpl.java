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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import vn.vnpttech.ssdc.nms.criteria.ActionLogsCriteria;
import vn.vnpttech.ssdc.nms.dao.ActionLogsDao;
import vn.vnpttech.ssdc.nms.model.ActionLogs;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.service.ActionLogsManager;

/**
 *
 * @author longdq
 */
public class ActionLogsManagerImpl extends GenericManagerImpl<ActionLogs, Long> implements ActionLogsManager {
	@Autowired
	ActionLogsDao actionLogsDao;
	
	public ActionLogsManagerImpl() {
    }

    public ActionLogsManagerImpl( ActionLogsDao actionLogsDao) {
        super(actionLogsDao);
    }

    public ActionLogsDao getActionLogsDao() {
        return actionLogsDao;
    }

    public void setActionLogsDao(ActionLogsDao actionLogsDao) {
        this.actionLogsDao = actionLogsDao;
    }

    @Override
    public List<ActionLogs> getActionLogsByUsername(String username) {
        return actionLogsDao.getActionLogsByUsername(username);
    }
    
    @Override
    public List<ActionLogs> getActionLogs(String username, Date from, Date to, int offset, int limit){
    	return actionLogsDao.getActionLogs(username, from, to, offset, limit);
    }
    
    /*@Override
    public void saveActionLog(String username, Object obj, ActionTypeEnum actionType, String desctiption){
    	ActionLogs item = new ActionLogs();
    	item.setUsername(username);
    	item.setActionObject(obj.getClass().toString());
    	item.setActionType(actionType.getType());
    	item.setDescription(desctiption != null && !desctiption.equals("")? desctiption : obj.toString());
    	item.setActionTime(new Date());
    	
    	actionLogsDao.save(item);
    	
    }*/
    
    @Override
    public void saveActionLog(String username, String obj, ActionTypeEnum actionType, String desctiption){
    	ActionLogs item = new ActionLogs();
    	item.setUsername(username);
    	item.setActionObject(obj);
    	item.setActionType(actionType.getType());
    	item.setDescription(desctiption);
    	item.setActionTime(new Timestamp(System.currentTimeMillis()));
    	
    	actionLogsDao.save(item);
    }
    
    @Override
    public List<ActionLogs> searchCriteria(ActionLogsCriteria searchCriteria){
    	return actionLogsDao.searchCriteria(searchCriteria);
    }

    @Override
	public int countActionLogs(ActionLogsCriteria searchCriteria){
    	return actionLogsDao.countActionLogs(searchCriteria);
    }
}
