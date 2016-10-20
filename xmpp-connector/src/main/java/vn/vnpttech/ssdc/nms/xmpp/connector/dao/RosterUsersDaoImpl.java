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

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import vn.vnpttech.ssdc.nms.xmpp.connector.HibernateUtil;
import vn.vnpttech.ssdc.nms.xmpp.model.Rosterusers;

/**
 *
 * @author longdq
 */
public class RosterUsersDaoImpl extends XmppGenericDaoImpl<Rosterusers, Long> implements RosterUsersDao {

    public SessionFactory sf = HibernateUtil.getSessionFactory();

    public RosterUsersDaoImpl(Class<Rosterusers> persistentClass) {
        super(persistentClass);
    }

    public Session getSession() throws HibernateException {
        Session sess = sf.openSession();

        return sess;
    }

//    @Override
//    public Rosterusers saveRosterUser(Rosterusers r) {
//
//        Transaction tx = null;
//        Session session = getSession();
//        try {
//
//            tx = session.beginTransaction();
//            session.saveOrUpdate(r);
//            tx.commit();
//            return r;
//
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//            return null;
//        } finally {
//            session.close();
//        }
//        
//
//    }
//    @Override
//    public Long remove(Rosterusers r) {
//        Transaction tx = null;
//        Session session = getSession();
//        try {
//
//            tx = session.beginTransaction();
//            session.delete(r);
//            tx.commit();
//            return r.getId();
//
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//             e.printStackTrace();
//            return null;
//           
//        } finally {
//            session.close();
//        }
//    }
//
//    @Override
//    public Long remove(Long id) {
//
//        Session session = getSession();
//        Transaction tx = null;
//        try {
//
//            tx = session.beginTransaction();
//            Rosterusers r = (Rosterusers) session.load(Rosterusers.class, id);
//            session.delete(r);
//            tx.commit();
//            return id;
//
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//               
//            }
//            e.printStackTrace();
//             return null;
//        } finally {
//            session.close();
//        }
//    }
    @Override
    public Rosterusers getRosterUser(String userName, String jid) {
        try {
            Session session = getSession();
            Criteria c = session.createCriteria(Rosterusers.class);
            c.add(Restrictions.eq("username", userName));
            c.add(Restrictions.eq("jid", jid));
            
            List<Rosterusers> list = null;
            list = c.list();
            if (!list.isEmpty()) {
                return list.get(0);
            } else {
                return null;
            }
        } catch (HibernateException hibernateException) {
            throw hibernateException;
        }
    }

}
