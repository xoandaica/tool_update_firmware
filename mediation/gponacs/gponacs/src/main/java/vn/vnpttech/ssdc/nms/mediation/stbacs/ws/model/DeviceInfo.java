/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model;

/**
 * Device Information
 *
 * @author Vunb
 * @date Jun 29, 2015
 * @update Jun 29, 2015
 */
public class DeviceInfo {

    private String serialNumber;
    private String productClass;
    private String oid;
    private String manufacturer;

    private String hardwareVersion;
    private String softwareVersion;
    private String provisioningCode;

    private String modelName;
    private String mac;
    private String cpu;
    private String ram;
    private String rom;
    
    // DB properties
    private long id;
    private String policyName;
    private String description;
    private String cpeUsername;
    private String cpePassword;
    private int cpeStatus;
    private int firmwareStatus;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getProvisioningCode() {
        return provisioningCode;
    }

    public void setProvisioningCode(String provisioningCode) {
        this.provisioningCode = provisioningCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirmwareVersion() {
        return softwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.softwareVersion = firmwareVersion;
    }

    public String getCpeUsername() {
        return cpeUsername;
    }

    public void setCpeUsername(String cpeUsername) {
        this.cpeUsername = cpeUsername;
    }

    public String getCpePassword() {
        return cpePassword;
    }

    public void setCpePassword(String cpePassword) {
        this.cpePassword = cpePassword;
    }

    public int getCpeStatus() {
        return cpeStatus;
    }

    public void setCpeStatus(int cpeStatus) {
        this.cpeStatus = cpeStatus;
    }

    public int getFirmwareStatus() {
        return firmwareStatus;
    }

    public void setFirmwareStatus(int firmwareStatus) {
        this.firmwareStatus = firmwareStatus;
    }

    
}
