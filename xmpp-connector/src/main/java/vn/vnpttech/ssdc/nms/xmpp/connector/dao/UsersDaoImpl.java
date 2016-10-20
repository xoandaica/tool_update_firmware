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
package vn.vnpttech.ssdc.nms.xmpp.connector.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import vn.vnpttech.ssdc.nms.xmpp.model.Users;

/**
 *
 * @author longdq
 */
public class UsersDaoImpl extends XmppGenericDaoImpl<Users, Long> implements UsersDao {

    public UsersDaoImpl(Class<Users> persistentClass) {
        super(persistentClass);
    }

    @Override
    public Users getUserByUserName(String username) {
        String hql = "from Users u where u.username like ? ";
        Session session = getSession();
        Transaction tx = null;
        try {
            Query q = session.createQuery(hql);
            q.setParameter(0, username);
            List<Users> us = new ArrayList<Users>();
            us = q.list();
            if (us.size() > 0) {
                return us.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

}
