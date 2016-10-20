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

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author longdq
 */
@Entity
@Table(name = "update_firmware_logs")
@Indexed
public class UpdateFirmwareLogs extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private int newFirmwareId;
    private Date updateTime;
    private int deviceId;
    private int status;
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Column(name = "new_firmware_id")
    public int getNewFirmwareId() {
        return newFirmwareId;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Column(name = "device_id")
    public int getDeviceId() {
        return deviceId;
    }

    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "id: " + id;
    }

    @Override
    public boolean equals(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNewFirmwareId(int newFirmwareId) {
        this.newFirmwareId = newFirmwareId;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
