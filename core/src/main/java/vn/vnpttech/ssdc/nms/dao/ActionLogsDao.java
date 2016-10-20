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

import java.util.Date;
import java.util.List;

import vn.vnpttech.ssdc.nms.criteria.ActionLogsCriteria;
import vn.vnpttech.ssdc.nms.model.ActionLogs;

/**
 *
 * @author longdq
 */

public interface ActionLogsDao extends GenericDao<ActionLogs, Long> {
    public List<ActionLogs> getActionLogsByUsername(String username);
    
    public List<ActionLogs> getActionLogs(String username, Date from, Date to, int offset, int limit);

	public List<ActionLogs> searchCriteria(ActionLogsCriteria searchCriteria);

	public int countActionLogs(ActionLogsCriteria searchCriteria);
}
