package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.util.ArrayList;

public class GetParameterCommand_3 extends Command{
	
	private boolean nextCm = false;
	
	private String rootLayer2_Name;
	private ArrayList<String> rootLayer2;
	
	private String rootLayer2_PPP_Name;
	private ArrayList<ParameterObject_2> rootLayer2_PPP;
	private ArrayList<ParameterObject> rootLayer2_PPP_Name_listParams;
	
	
	private String rootLayer2_IP_Name;
	private ArrayList<ParameterObject_2> rootLayer2_IP;
	private ArrayList<ParameterObject> rootLayer2_IP_Name_listParams;
	
	public GetParameterCommand_3(String serialNumber, String connectionRequestURL, String usernameCPE, String passwordCPE){		
		this.serialNumberCPE = serialNumber;
		this.connectionRequestURL = connectionRequestURL;
		this.usernameCPE = usernameCPE;
		this.passwordCPE = passwordCPE;
		this.type = "GetParameter_3";
	}

	
	
	public boolean isNextCm() {
		return nextCm;
	}

	public void setNextCm(boolean nextCm) {
		this.nextCm = nextCm;
	}

	public String getRootLayer2_Name() {
		return rootLayer2_Name;
	}

	public void setRootLayer2_Name(String rootLayer2_Name) {
		this.rootLayer2_Name = rootLayer2_Name;
	}

	public ArrayList<String> getRootLayer2() {
		return rootLayer2;
	}

	public void setRootLayer2(ArrayList<String> rootLayer2) {
		this.rootLayer2 = rootLayer2;
	}

	public String getRootLayer2_PPP_Name() {
		return rootLayer2_PPP_Name;
	}

	public void setRootLayer2_PPP_Name(String rootLayer2_PPP_Name) {
		this.rootLayer2_PPP_Name = rootLayer2_PPP_Name;
	}

	public ArrayList<ParameterObject_2> getRootLayer2_PPP() {
		return rootLayer2_PPP;
	}

	public void setRootLayer2_PPP(ArrayList<ParameterObject_2> rootLayer2_PPP) {
		this.rootLayer2_PPP = rootLayer2_PPP;
	}

	public String getRootLayer2_IP_Name() {
		return rootLayer2_IP_Name;
	}

	public void setRootLayer2_IP_Name(String rootLayer2_IP_Name) {
		this.rootLayer2_IP_Name = rootLayer2_IP_Name;
	}

	public ArrayList<ParameterObject_2> getRootLayer2_IP() {
		return rootLayer2_IP;
	}

	public void setRootLayer2_IP(ArrayList<ParameterObject_2> rootLayer2_IP) {
		this.rootLayer2_IP = rootLayer2_IP;
	}



	public ArrayList<ParameterObject> getRootLayer2_PPP_Name_listParams() {
		return rootLayer2_PPP_Name_listParams;
	}



	public void setRootLayer2_PPP_Name_listParams(
			ArrayList<ParameterObject> rootLayer2_PPP_Name_listParams) {
		this.rootLayer2_PPP_Name_listParams = rootLayer2_PPP_Name_listParams;
	}



	public ArrayList<ParameterObject> getRootLayer2_IP_Name_listParams() {
		return rootLayer2_IP_Name_listParams;
	}



	public void setRootLayer2_IP_Name_listParams(
			ArrayList<ParameterObject> rootLayer2_IP_Name_listParams) {
		this.rootLayer2_IP_Name_listParams = rootLayer2_IP_Name_listParams;
	}
		
	
}
