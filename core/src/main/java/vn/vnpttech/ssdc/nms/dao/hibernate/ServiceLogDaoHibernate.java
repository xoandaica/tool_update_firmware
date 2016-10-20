/*
 * Copyright 2016 Pivotal Software, Inc..
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
import static javax.ws.rs.core.Response.status;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import vn.vnpttech.ssdc.nms.dao.ServiceLogDao;
import vn.vnpttech.ssdc.nms.model.PolicyHistory;
import vn.vnpttech.ssdc.nms.model.ServiceLog;

/**
 *
 * @author SSDC
 */
public class ServiceLogDaoHibernate extends GenericDaoHibernate<ServiceLog, Long> implements ServiceLogDao {

    public ServiceLogDaoHibernate() {
        super(ServiceLog.class);
    }

    @Override
    public List<ServiceLog> searchLogBySerial(String serialNumber) {
        List<ServiceLog> lph = new ArrayList<ServiceLog>();
        Criteria c = getSession().createCriteria(PolicyHistory.class);
        if (StringUtils.isNotBlank(serialNumber)) {
            c.add(Restrictions.like("serial_number", serialNumber));
        }
        lph = c.list();
        return lph;
    }

}
