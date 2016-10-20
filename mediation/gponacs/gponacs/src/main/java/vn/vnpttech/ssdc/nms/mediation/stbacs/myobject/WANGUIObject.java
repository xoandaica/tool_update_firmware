package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;

public class WANGUIObject {
	private ArrayList<Layer2InterfaceObject> atmInterface = new ArrayList<Layer2InterfaceObject>();
	private ArrayList<WanServiceObject> wanService = new ArrayList<WanServiceObject>();
	
	
	public ArrayList<Layer2InterfaceObject> getAtmInterface() {
		return atmInterface;
	}
	public void setAtmInterface(ArrayList<Layer2InterfaceObject> atmInterface) {
		this.atmInterface = atmInterface;
	}
	public ArrayList<WanServiceObject> getWanService() {
		return wanService;
	}
	public void setWanService(ArrayList<WanServiceObject> wanService) {
		this.wanService = wanService;
	}
	
	
}
