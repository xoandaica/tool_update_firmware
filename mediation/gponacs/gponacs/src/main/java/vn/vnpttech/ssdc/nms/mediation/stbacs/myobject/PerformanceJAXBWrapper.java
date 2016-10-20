package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class PerformanceJAXBWrapper {
	private ArrayList<SimpleObject> performanceWrapper = new ArrayList<SimpleObject>();

	public ArrayList<SimpleObject> getPerformanceWrapper() {
		return performanceWrapper;
	}

	public void setPerformanceWrapper(ArrayList<SimpleObject> performanceWrapper) {
		this.performanceWrapper = performanceWrapper;
	}
	
	
}
