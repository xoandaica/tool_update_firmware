package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;

public class LanSetupObject {
	private ArrayList<SimpleObject> lanCommon = new ArrayList<SimpleObject>();
	private ArrayList<LanSetup_IPLease> lanIPLease = new ArrayList<LanSetup_IPLease>();
	
	
	public ArrayList<SimpleObject> getLanCommon() {
		return lanCommon;
	}
	public void setLanCommon(ArrayList<SimpleObject> lanCommon) {
		this.lanCommon = lanCommon;
	}
	public ArrayList<LanSetup_IPLease> getLanIPLease() {
		return lanIPLease;
	}
	public void setLanIPLease(ArrayList<LanSetup_IPLease> lanIPLease) {
		this.lanIPLease = lanIPLease;
	}
	
}
