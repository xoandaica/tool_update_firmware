package vn.vnpttech.ssdc.nms.service;


import java.util.Date;
import java.util.List;

import vn.vnpttech.ssdc.nms.criteria.ActionLogsCriteria;
import vn.vnpttech.ssdc.nms.model.ActionLogs;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.service.GenericManager;

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
/**
 *
 * @author longdq
 */
public interface ActionLogsManager extends GenericManager<ActionLogs, Long> {

    public List<ActionLogs> getActionLogsByUsername(String username);
    public List<ActionLogs> getActionLogs(String username, Date from, Date to, int offset, int limit);
    //public void saveActionLog(String username, Object obj, ActionTypeEnum actionType, String desctiption);
    public void saveActionLog(String username, String obj, ActionTypeEnum actionType, String desctiption);

    List<ActionLogs> searchCriteria(ActionLogsCriteria searchCriteria);

	int countActionLogs(ActionLogsCriteria searchCriteria);
}
