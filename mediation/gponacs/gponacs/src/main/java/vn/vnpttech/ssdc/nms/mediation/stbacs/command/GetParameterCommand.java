package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.util.ArrayList;

public class GetParameterCommand extends Command{
	
public GetParameterCommand(String serialNumber, String connectionRequestURL, String usernameCPE, String passwordCPE){
	
	this.serialNumberCPE = serialNumber;
	this.connectionRequestURL = connectionRequestURL;
	this.usernameCPE = usernameCPE;
	this.passwordCPE = passwordCPE;
	this.type = "GetParameter";
}

}
