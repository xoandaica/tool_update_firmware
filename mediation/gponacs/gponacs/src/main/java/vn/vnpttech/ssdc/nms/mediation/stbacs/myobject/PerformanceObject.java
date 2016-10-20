package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PerformanceObject {

    private ArrayList<PerformanceJAXBWrapper> lanPerformance = new ArrayList<PerformanceJAXBWrapper>();
    private ArrayList<SimpleObject> wlanPerformance = new ArrayList<SimpleObject>();
    private ArrayList<PerformanceJAXBWrapper> wanPerformance = new ArrayList<PerformanceJAXBWrapper>();
    private ArrayList<SimpleObject> dslPerformance = new ArrayList<SimpleObject>();
    private ArrayList<SimpleObject> opticalInfo = new ArrayList<SimpleObject>();

    public ArrayList<PerformanceJAXBWrapper> getLanPerformance() {
        return lanPerformance;
    }

    public void setLanPerformance(ArrayList<PerformanceJAXBWrapper> lanPerformance) {
        this.lanPerformance = lanPerformance;
    }

    public ArrayList<PerformanceJAXBWrapper> getWanPerformance() {
        return wanPerformance;
    }

    public void setWanPerformance(ArrayList<PerformanceJAXBWrapper> wanPerformance) {
        this.wanPerformance = wanPerformance;
    }

    public ArrayList<SimpleObject> getWlanPerformance() {
        return wlanPerformance;
    }

    public void setWlanPerformance(ArrayList<SimpleObject> wlanPerformance) {
        this.wlanPerformance = wlanPerformance;
    }

    public ArrayList<SimpleObject> getDslPerformance() {
        return dslPerformance;
    }

    public void setDslPerformance(ArrayList<SimpleObject> dslPerformance) {
        this.dslPerformance = dslPerformance;
    }

    public ArrayList<SimpleObject> getOpticalInfo() {
        return opticalInfo;
    }

    public void setOpticalInfo(ArrayList<SimpleObject> opticalInfo) {
        this.opticalInfo = opticalInfo;
    }

}
