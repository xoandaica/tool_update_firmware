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
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author longdq
 */
@Entity
@Table(name = "area")
@Indexed
public class Area extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer areaType;
    private String description;
    private Set<Area> areas;
    private Area area;
    private Set<Device> devices;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Column(name = "name", length = 45)
    public String getName() {
        return name;
    }

    @Column(name = "area_type")
    public Integer getAreaType() {
        return areaType;
    }

    @Column(name = "description", length = 45)
    public String getDescription() {
        return description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "area")
    public Set<Area> getAreas() {
        return areas;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    public Area getArea() {
        return area;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "area")
    public Set<Device> getDevices() {
        return devices;
    }

    //setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAreaType(Integer areaType) {
        this.areaType = areaType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name;
    }

    @Override
    public boolean equals(Object o) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return 1; //To change body of generated methods, choose Tools | Templates.
    }

}
