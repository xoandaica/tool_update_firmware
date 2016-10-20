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
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author longdq
 */
@Entity
@Table(name = "firmware")
@Indexed
public class Firmware extends BaseObject implements Serializable {

    private Long id;
    private String version;
    private DeviceModel deviceModel;
    private Timestamp releaseDate;
    private String releaseNote;
    private String firmwarePath;
    private int deviceUseageNumber;

    private int fwDefault;

    private Set<Policy> policies;

    private String modelName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Column(name = "version", length = 45)
    public String getVersion() {
        return version;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    public DeviceModel getDeviceModel() {
        return deviceModel;
    }

    @Column(name = "release_date")
    public Timestamp getReleaseDate() {
        return releaseDate;
    }

    @Column(name = "release_note")
    public String getReleaseNote() {
        return releaseNote;
    }

    @Column(name = "firmware_path", length = 255)
    public String getFirmwarePath() {
        return firmwarePath;
    }

    @Column(name = "device_usage_number")
    public int getDeviceUseageNumber() {
        return deviceUseageNumber;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "firmware")
    public Set<Policy> getPolicies() {
        return policies;
    }

    @Column(name = "fw_default")
    public int getFwDefault() {
        return fwDefault;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDeviceModel(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setReleaseDate(Timestamp releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public void setFirmwarePath(String firmwarePath) {
        this.firmwarePath = firmwarePath;
    }

    public void setDeviceUseageNumber(int deviceUseageNumber) {
        this.deviceUseageNumber = deviceUseageNumber;
    }

    public void setPolicies(Set<Policy> policies) {
        this.policies = policies;
    }

    public void setFwDefault(int fwDefault) {
        this.fwDefault = fwDefault;
    }

    @Override
    public String toString() {
        return "id: " + id + ", version: " + version;
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

    @Transient
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
