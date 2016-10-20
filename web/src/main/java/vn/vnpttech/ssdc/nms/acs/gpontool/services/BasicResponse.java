
package vn.vnpttech.ssdc.nms.acs.gpontool.services;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for basicResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="basicResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.stbacs.mediation.nms.ssdc.vnpttech.vn/}templResponse">
 *       &lt;sequence>
 *         &lt;element name="Results" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Item" type="{http://services.stbacs.mediation.nms.ssdc.vnpttech.vn/}namedKeyValue" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "basicResponse", propOrder = {
    "results"
})
@XmlSeeAlso({
    DeviceInfoResponse.class
})
public class BasicResponse
    extends TemplResponse
{

    @XmlElement(name = "Results")
    protected BasicResponse.Results results;

    /**
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link BasicResponse.Results }
     *     
     */
    public BasicResponse.Results getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicResponse.Results }
     *     
     */
    public void setResults(BasicResponse.Results value) {
        this.results = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Item" type="{http://services.stbacs.mediation.nms.ssdc.vnpttech.vn/}namedKeyValue" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "item"
    })
    public static class Results {

        @XmlElement(name = "Item")
        protected List<NamedKeyValue> item;

        /**
         * Gets the value of the item property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the item property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NamedKeyValue }
         * 
         * 
         */
        public List<NamedKeyValue> getItem() {
            if (item == null) {
                item = new ArrayList<NamedKeyValue>();
            }
            return this.item;
        }

    }

}
