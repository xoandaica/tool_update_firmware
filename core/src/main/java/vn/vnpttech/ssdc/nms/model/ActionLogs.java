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
package vn.vnpttech.ssdc.nms.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.search.annotations.Indexed;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author longdq
 */
@Entity
@Table(name = "action_logs")
@Indexed

public class ActionLogs extends BaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private Integer actionType;
    private String actionObject;
    private Timestamp actionTime;
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    public Long getId() {
        return id;
    }

    @Column(name = "username", length = 45)
    public String getUsername() {
        return username;
    }

    @Column(name = "action_type")
    public Integer getActionType() {
        return actionType;
    }

    @Column(name = "action_object", length = 45)
    public String getActionObject() {
        return actionObject;
    }

    @Column(name = "action_time")
    public Timestamp getActionTime() {
        return actionTime;
    }

    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    // setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public void setActionObject(String actionObject) {
        this.actionObject = actionObject;
    }

    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "id: " + id;
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
