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
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import vn.vnpttech.ssdc.nms.dao.DeviceModelDao;
import vn.vnpttech.ssdc.nms.model.DeviceModel;

/**
 *
 * @author longdq
 */
public class DeviceModelDaoHibernate extends GenericDaoHibernate<DeviceModel, Long> implements DeviceModelDao {

    public DeviceModelDaoHibernate(Class<DeviceModel> persistentClass) {
        super(persistentClass);
    }

    public DeviceModelDaoHibernate() {
        super(DeviceModel.class);
    }

    @Override
    public DeviceModel getModelByName(String modelName) {
        Criteria c = getSession().createCriteria(DeviceModel.class);
        c.add(Restrictions.like("name", modelName, MatchMode.EXACT));
        return (DeviceModel) c.uniqueResult();
//        List<DeviceModel> ml = new ArrayList<DeviceModel>();
//        ml = c.list();
//        if (ml != null && ml.size() > 0) {
//            return ml.get(0);
//        } else {
//            return null;
//        }
    }

}
