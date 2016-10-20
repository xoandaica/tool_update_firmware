package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;


import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.TR069StaticParameter;

public class WirelessObject {
	private ArrayList<SimpleObject> enableWireless = new ArrayList<SimpleObject>();
	private ArrayList<SimpleObject> ssidName = new ArrayList<SimpleObject>();
	private SimpleObject isolateClient = new SimpleObject();
	private ArrayList<SimpleObject> hidden = new ArrayList<SimpleObject>();
	private SimpleObject disableWMM = new SimpleObject();
	private SimpleObject enableWMF = new SimpleObject();
	private SimpleObject maxClient = new SimpleObject();
	private ArrayList<SimpleObject> security = new ArrayList<SimpleObject>();
	
//	public ArrayList<SimpleObject> getAllChangeObject(){
//		ArrayList<SimpleObject> returnValue = new ArrayList<SimpleObject>();
//		for(SimpleObject eW : enableWireless){
//			if(eW.hasNewValue){
//				returnValue.add(eW);
//				eW.hasNewValue =false;
//			}
//		}
//		
//		for(SimpleObject sN : ssidName){
//			if(sN.hasNewValue){
//				returnValue.add(sN);
//				sN.hasNewValue = false;
//			}
//		}
//		
//		for(SimpleObject hd : hidden){
//			if(hd.hasNewValue){
//				returnValue.add(hd);
//				hd.hasNewValue = false;
//			}
//		}
//		
//		return returnValue;
//	}	
	public ArrayList<SimpleObject> getEnableWireless() {
		return enableWireless;
	}
	public void setEnableWireless(ArrayList<SimpleObject> enableWireless) {
		this.enableWireless = enableWireless;
	}
	public boolean getEnable(){		
		return Boolean.parseBoolean(enableWireless.get(0).getValue());
	}
//	public void setEnable(boolean is){
//		if(is){
//			for(SimpleObject obj : enableWireless){
//				obj.setNewParameterValue("1");
//			}
//		}else{
//			for(SimpleObject obj : enableWireless){
//				obj.setNewParameterValue("0");
//			}
//		}
//	}
	
	public ArrayList<SimpleObject> getSsidName() {
		return ssidName;
	}
	public void setSsidName(ArrayList<SimpleObject> ssidName) {
		this.ssidName = ssidName;
	}	
	public String getSSID(){
		return ssidName.get(0).getValue();
	}
//	public void setSSID(String newValue){		
//		for(SimpleObject ob : ssidName){
//			ob.setNewParameterValue(newValue);
//		}
//	}
		
	public SimpleObject getIsolateClient() {
		return isolateClient;
	}
	public void setIsolateClient(SimpleObject isolateClient) {
		this.isolateClient = isolateClient;
	}
		
	public ArrayList<SimpleObject> getHidden() {
		return hidden;
	}
	public void setHidden(ArrayList<SimpleObject> hidden) {
		this.hidden = hidden;
	}
	public boolean getHide(){
		return Boolean.parseBoolean(hidden.get(0).getValue());
	}
//	public void setHide(boolean is){
//		if(is){
//			for(SimpleObject obj : hidden){
//				obj.setNewParameterValue("1");
//			}
//		}else{
//			for(SimpleObject obj : hidden){
//				obj.setNewParameterValue("0");
//			}
//		}
//	}
		
	public SimpleObject getDisableWMM() {
		return disableWMM;
	}
	public void setDisableWMM(SimpleObject disableWMM) {
		this.disableWMM = disableWMM;
	}
	public SimpleObject getEnableWMF() {
		return enableWMF;
	}
	public void setEnableWMF(SimpleObject enableWMF) {
		this.enableWMF = enableWMF;
	}
	public SimpleObject getMaxClient() {
		return maxClient;
	}
	public void setMaxClient(SimpleObject maxClient) {
		this.maxClient = maxClient;
	}
	public ArrayList<SimpleObject> getSecurity() {
		return security;
	}
	public void setSecurity(ArrayList<SimpleObject> security) {
		this.security = security;
	}
	
//	public void setWlAuthMode(String mode){
//		if(mode.equals(TR069StaticParameter.WlAuthMode_Open)){
//			security.get(0).setValue("open");
//			security.get(2).setValue("Basic");
//		}else if(mode.equals(TR069StaticParameter.WlAuthMode_WPA_PSK)){
//			security.get(0).setValue("psk");
//			security.get(2).setValue("WPA");
//		}else if(mode.equals(TR069StaticParameter.WlAuthMode_WPA2_PSK)){
//			security.get(0).setValue("psk2");
//			security.get(2).setValue("WPA");
//		}
//	}
}
