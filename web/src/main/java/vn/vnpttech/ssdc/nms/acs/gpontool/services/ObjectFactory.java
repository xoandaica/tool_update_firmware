
package vn.vnpttech.ssdc.nms.acs.gpontool.services;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the vn.vnpttech.ssdc.nms.acs.gpontool package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: vn.vnpttech.ssdc.nms.acs.gpontool
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BasicResponse }
     * 
     */
    public BasicResponse createBasicResponse() {
        return new BasicResponse();
    }

    /**
     * Create an instance of {@link DeviceInfoResponse }
     * 
     */
    public DeviceInfoResponse createDeviceInfoResponse() {
        return new DeviceInfoResponse();
    }

    /**
     * Create an instance of {@link ArrayList }
     * 
     */
    public ArrayList createArrayList() {
        return new ArrayList();
    }

    /**
     * Create an instance of {@link DeviceInfo }
     * 
     */
    public DeviceInfo createDeviceInfo() {
        return new DeviceInfo();
    }

    /**
     * Create an instance of {@link NamedKeyValue }
     * 
     */
    public NamedKeyValue createNamedKeyValue() {
        return new NamedKeyValue();
    }

    /**
     * Create an instance of {@link BasicResponse.Results }
     * 
     */
    public BasicResponse.Results createBasicResponseResults() {
        return new BasicResponse.Results();
    }

}
