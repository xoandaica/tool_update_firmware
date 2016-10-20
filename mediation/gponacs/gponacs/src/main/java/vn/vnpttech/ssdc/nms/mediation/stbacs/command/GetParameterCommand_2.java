package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.util.ArrayList;

public class GetParameterCommand_2 extends Command{
	
//	private ArrayList<ParameterObject_2> listParams = new ArrayList<ParameterObject_2>();	
	private String rootName;
	private ArrayList<String> listRootParams = new ArrayList<String>();
	
	public GetParameterCommand_2(String serialNumber, String connectionRequestURL, String usernameCPE, String passwordCPE){		
		this.serialNumberCPE = serialNumber;
		this.connectionRequestURL = connectionRequestURL;
		this.usernameCPE = usernameCPE;
		this.passwordCPE = passwordCPE;
		this.type = "GetParameter_2";
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	
	public ArrayList<String> getListRootParams() {
		return listRootParams;
	}

	public void setListRootParams(ArrayList<String> listRootParams) {
		this.listRootParams = listRootParams;
	}

	
	
}
