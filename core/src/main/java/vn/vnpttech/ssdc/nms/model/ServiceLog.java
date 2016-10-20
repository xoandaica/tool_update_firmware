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
package vn.vnpttech.ssdc.nms.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SSDC
 */
@Entity
@Table(name = "service_log", catalog = "gponnms", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceLog.findAll", query = "SELECT s FROM ServiceLog s")})
public class ServiceLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "action_id", nullable = false)
    private Long actionId;
    @Basic(optional = false)
    @Column(name = "action_name", nullable = false, length = 50)
    private String actionName;
    @Column(name = "action_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionStartTime;
    @Column(name = "action_end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionEndTime;
    @Basic(optional = false)
    @Column(name = "serial_number", nullable = false, length = 50)
    private String serialNumber;
    @Column(name = "result", length = 50)
    private String result;
    @Column(name = "error", length = 255)
    private String error;
    @Basic(optional = false)
    @Column(name = "ip_device", nullable = false, length = 50)
    private String ipDevice;

    public ServiceLog() {
    }

    public ServiceLog(Long actionId) {
        this.actionId = actionId;
    }

    public ServiceLog(Long actionId, String actionName, String serialNumber, String ipDevice) {
        this.actionId = actionId;
        this.actionName = actionName;
        this.serialNumber = serialNumber;
        this.ipDevice = ipDevice;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Date getActionStartTime() {
        return actionStartTime;
    }

    public void setActionStartTime(Date actionStartTime) {
        this.actionStartTime = actionStartTime;
    }

    public Date getActionEndTime() {
        return actionEndTime;
    }

    public void setActionEndTime(Date actionEndTime) {
        this.actionEndTime = actionEndTime;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIpDevice() {
        return ipDevice;
    }

    public void setIpDevice(String ipDevice) {
        this.ipDevice = ipDevice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionId != null ? actionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceLog)) {
            return false;
        }
        ServiceLog other = (ServiceLog) object;
        if ((this.actionId == null && other.actionId != null) || (this.actionId != null && !this.actionId.equals(other.actionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vn.vnpttech.ssdc.nms.model.ServiceLog[ actionId=" + actionId + " ]";
    }
    
}
