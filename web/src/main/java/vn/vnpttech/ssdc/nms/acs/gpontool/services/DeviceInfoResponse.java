
package vn.vnpttech.ssdc.nms.acs.gpontool.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deviceInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deviceInfoResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.stbacs.mediation.nms.ssdc.vnpttech.vn/}basicResponse">
 *       &lt;sequence>
 *         &lt;element name="deviceInfo" type="{http://services.stbacs.mediation.nms.ssdc.vnpttech.vn/}deviceInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deviceInfoResponse", propOrder = {
    "deviceInfo"
})
public class DeviceInfoResponse
    extends BasicResponse
{

    protected DeviceInfo deviceInfo;

    /**
     * Gets the value of the deviceInfo property.
     * 
     * @return
     *     possible object is
     *     {@link DeviceInfo }
     *     
     */
    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * Sets the value of the deviceInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeviceInfo }
     *     
     */
    public void setDeviceInfo(DeviceInfo value) {
        this.deviceInfo = value;
    }

}
