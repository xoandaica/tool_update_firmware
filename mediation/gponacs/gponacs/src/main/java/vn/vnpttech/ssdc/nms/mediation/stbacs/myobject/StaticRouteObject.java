package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;


public class StaticRouteObject {
	private String rootName = "InternetGatewayDevice.Layer3Forwarding.Forwarding";
	private int instance;
	private ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
	
	public String getRootName() {
		return rootName;
	}
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}
	public int getInstance() {
		return instance;
	}
	public void setInstance(int instance) {
		this.instance = instance;
	}
	public ArrayList<SimpleObject> getListObj() {
		return listObj;
	}
	public void setListObj(ArrayList<SimpleObject> listObj) {
		this.listObj = listObj;
	}
	
	
}
