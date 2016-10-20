package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;

public class WANGUIObjectEthernet {
	private ArrayList<Layer2InterfaceObject> layer2LanInterface = new ArrayList<Layer2InterfaceObject>();
	private ArrayList<WanServiceObject> wanService = new ArrayList<WanServiceObject>();
	
	public ArrayList<Layer2InterfaceObject> getLayer2LanInterface() {
		return layer2LanInterface;
	}
	public void setLayer2LanInterface(
			ArrayList<Layer2InterfaceObject> layer2LanInterface) {
		this.layer2LanInterface = layer2LanInterface;
	}
	public ArrayList<WanServiceObject> getWanService() {
		return wanService;
	}
	public void setWanService(ArrayList<WanServiceObject> wanService) {
		this.wanService = wanService;
	}
	

	
	
}
