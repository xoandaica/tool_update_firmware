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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author longdq
 */
@Entity
@Table(name = "policy")
@Indexed
public class Policy extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Date startTime;
    private Date endTime;
    private int policyStatus;// 0:CHƯA THỰC HIỆN; 1: ĐANG THỰC HIỆN; 2: ĐÃ THỰC HIỆN
    private int enable;
    private int deviceSuccessNo;
    private int deviceFailedNo;

    private Set<Area> areas = new HashSet<Area>();

    private Set<PolicyHistory> policiesHistory;

    private DeviceModel deviceModel;

    private Firmware firmware;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    @Column(name = "policy_status")
    public int getPolicyStatus() {
        return policyStatus;
    }

    @Column(name = "enable")
    public int getEnable() {
        return enable;
    }

    @Column(name = "device_success_no")
    public int getDeviceSuccessNo() {
        return deviceSuccessNo;
    }

    @Column(name = "device_failed_no")
    public int getDeviceFailedNo() {
        return deviceFailedNo;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(
            name = "policy_area",
            joinColumns = {
                @JoinColumn(name = "policy_id")},
            inverseJoinColumns = @JoinColumn(name = "area_id")
    )
    public Set<Area> getAreas() {
        return areas;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    public DeviceModel getDeviceModel() {
        return deviceModel;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "policy")
    public Set<PolicyHistory> getPoliciesHistory() {
        return policiesHistory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firmware_id", nullable = false)
    public Firmware getFirmware() {
        return firmware;
    }

    //setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setPolicyStatus(int policyStatus) {
        this.policyStatus = policyStatus;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public void setDeviceSuccessNo(int deviceSuccessNo) {
        this.deviceSuccessNo = deviceSuccessNo;
    }

    public void setDeviceFailedNo(int deviceFailedNo) {
        this.deviceFailedNo = deviceFailedNo;
    }

    public void setDeviceModel(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    public void setPoliciesHistory(Set<PolicyHistory> policiesHistory) {
        this.policiesHistory = policiesHistory;
    }

    public void setFirmware(Firmware firmware) {
        this.firmware = firmware;
    }
    @Override
    public String toString() {
        return "id: " + id + ", name: " + name;
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

 

}
