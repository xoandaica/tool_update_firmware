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
import vn.vnpttech.ssdc.nms.xmpp.model.Rosterusers;

/**
 *
 * @author longdq
 */
public interface XmppGenericDao<T, PK extends Serializable> {

    List<T> getAll();

    public T save(T object);

    public T remove(T object);

    public PK remove(PK id);
    
    public T get(PK id);
}
