package vn.vnpttech.ssdc.nms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "area_device_mapping")
@Indexed
public class AreaDeviceMapping extends BaseObject implements Serializable{
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "province")
	private String province;
	@Column(name = "district")
	private String district;
	@Column(name = "area_code")
	private String areaCode;
	@Column(name = "serial_number")
	private String serialNumber;
	@Column(name = "mac_address")
	private String macAddress;
	@Column(name = "ip")
	private String ip;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
    public String toString() {
        return "id: " + id
        		+ ", province: " + province
        		+ ", district: " + district
        		+ ", serial_numeber: " + serialNumber
        		+ ", ip: " + ip;
    }

    @Override
    public boolean equals(Object o) {
        return false; 
    }

    @Override
    public int hashCode() {
        return 1; 
    }
}
