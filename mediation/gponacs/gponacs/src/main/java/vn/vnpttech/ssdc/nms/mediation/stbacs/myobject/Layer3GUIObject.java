package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;

public class Layer3GUIObject {
	private SimpleObject defaultGateway;
	private SimpleObject defaultDNS;
	
	private ArrayList<WanServiceObject> listWanService ;
	private ArrayList<StaticRouteObject> listStaticRoute;
			
	public SimpleObject getDefaultGateway() {
		return defaultGateway;
	}
	public void setDefaultGateway(SimpleObject defaultGateway) {
		this.defaultGateway = defaultGateway;
	}
	public SimpleObject getDefaultDNS() {
		return defaultDNS;
	}
	public void setDefaultDNS(SimpleObject defaultDNS) {
		this.defaultDNS = defaultDNS;
	}
	public ArrayList<WanServiceObject> getListWanService() {
		return listWanService;
	}
	public void setListWanService(ArrayList<WanServiceObject> listWanService) {
		this.listWanService = listWanService;
	}
	public ArrayList<StaticRouteObject> getListStaticRoute() {
		return listStaticRoute;
	}
	public void setListStaticRoute(ArrayList<StaticRouteObject> listStaticRoute) {
		this.listStaticRoute = listStaticRoute;
	}
	
	
}
