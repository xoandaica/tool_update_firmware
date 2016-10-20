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

import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import vn.vnpttech.ssdc.nms.xmpp.connector.HibernateUtil;

/**
 *
 * @author longdq
 */
public class XmppGenericDaoImpl<T, PK extends Serializable> implements XmppGenericDao<T, PK> {

    private Class<T> persistentClass;
    public SessionFactory sf = HibernateUtil.getSessionFactory();

    public Session getSession() throws HibernateException {
        Session sess = sf.openSession();
        return sess;
    }

    public XmppGenericDaoImpl(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;

    }

    @Override
    public List<T> getAll() {
        Session sess = getSession();
        return sess.createCriteria(persistentClass).list();
    }

    @Override
    public T save(T object) {
        Transaction tx = null;
        Session session = getSession();
        try {

            tx = session.beginTransaction();
            session.saveOrUpdate(object);
            tx.commit();
            return object;

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

    @Override
    public T remove(T object) {
        Transaction tx = null;
        Session session = getSession();
        try {

            tx = session.beginTransaction();
            session.delete(object);
            tx.commit();
            return object;

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

    @Override
    public PK remove(PK id) {
        Session session = getSession();
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            T r = (T) session.load(persistentClass, id);
            session.delete(r);
            tx.commit();
            return id;

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

    @Override
    public T get(PK id) {
        Session session = getSession();
        Transaction tx = null;
        try {
            IdentifierLoadAccess byId = session.byId(persistentClass);
            T entity = (T) byId.load(id);
            return entity;
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
