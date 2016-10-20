package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.util.ArrayList;

public class ParameterObject_2 {
	
	private String rootName;
	//private int instance;
	private ArrayList<ParameterObject> listParams = new ArrayList<ParameterObject>();	
		
	public String getRootName() {
		return rootName;
	}
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}	
		
	public ArrayList<ParameterObject> getListParams() {
		return listParams;
	}
	public void setListParams(ArrayList<ParameterObject> listParams) {
		this.listParams = listParams;
	}
	
	
}
