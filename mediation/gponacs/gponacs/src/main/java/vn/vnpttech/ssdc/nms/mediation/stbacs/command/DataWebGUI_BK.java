package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataWebGUI_BK {
	private String serialNumber;
	private String connectionRequestURL;
	
	public DataWebGUI_BK(String serialNumber, String connectionRequestURL){
		this.serialNumber = serialNumber;
		this.connectionRequestURL = connectionRequestURL;
	}
	
	private String getPathFile(String shortPath){
		return System.getProperty("user.dir") + File.separator + "datafile" + File.separator + shortPath;
	}
	
	private ArrayList<String> getLineFromFile(String shorPath) throws IOException{
		ArrayList<String> returnValue = new ArrayList<String>();
		
		DataInputStream in = new DataInputStream(new FileInputStream(getPathFile(shorPath)));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		
		while((strLine = br.readLine()) != null){
			if(!strLine.equals("")){
				returnValue.add(strLine);
			}
		}
		
		return returnValue;
	}
		
	private ArrayList<String> get4ElementFromLine(String line){
		ArrayList<String> returnValue = new ArrayList<String>();
		
		StringBuffer bst = new StringBuffer(line);						
		int start=0, end=0;
		for(int i=0;i<bst.length();i++){
			if(bst.charAt(i)=='-' || i==(bst.length()-1)){
				end=i;
				if(i==(bst.length()-1))end++;
				returnValue.add(bst.substring(start, end));				
				start=end+1;
			}
		}
		
		return returnValue;
	}
	
	public ArrayList<ParameterObject_3> getWANServiceByInterface() throws IOException{
		ArrayList<ParameterObject_3> returnValue = new ArrayList<ParameterObject_3>();
		
		GetParameterCommand_3 cm = new GetParameterCommand_3(serialNumber, connectionRequestURL, Command.USERNAME_DEFAULT, Command.PASSWORD_DEFAULT);		
		//getData from file
			//PPP file
		ArrayList<String> getParamPPP = getLineFromFile("Get_WANService_ATM_PPP");
		cm.setRootLayer2_Name(getParamPPP.get(0));
		cm.setRootLayer2_PPP_Name(getParamPPP.get(1));
		for(int i=2;i<getParamPPP.size();i++){
			ArrayList<String> element = get4ElementFromLine(getParamPPP.get(i));
			ParameterObject temp = new ParameterObject();
			temp.setId(Integer.parseInt(element.get(0)));
			temp.setName(element.get(1));
			temp.setParameter(element.get(2));
			temp.setType(element.get(3));
			
			cm.getRootLayer2_PPP_Name_listParams().add(temp);
		}
			//IP file
		ArrayList<String> getParamIP = getLineFromFile("Get_WANService_ATM_IP");
		//cm.setRootLayer2_Name(getParamPPP.get(0));
		cm.setRootLayer2_IP_Name(getParamIP.get(1));
		for(int i=2;i<getParamIP.size();i++){
			ArrayList<String> element = get4ElementFromLine(getParamIP.get(i));
			ParameterObject temp = new ParameterObject();
			temp.setId(Integer.parseInt(element.get(0)));
			temp.setName(element.get(1));
			temp.setParameter(element.get(2));
			temp.setType(element.get(3));
			
			cm.getRootLayer2_IP_Name_listParams().add(temp);
		}
		//end getData from file
		
		return returnValue;
	}
	
	public ArrayList<ParameterObject_2> getLayer2Interface(String layer2Interface) throws Exception{
		//ArrayList<ParameterObject_2> returnValue = new ArrayList<ParameterObject_2>();
		
		ArrayList<Object> dataRequest = new ArrayList<Object>();
	
		//init command
		GetParameterCommand_2 cm = new GetParameterCommand_2(serialNumber, connectionRequestURL, Command.USERNAME_DEFAULT, Command.PASSWORD_DEFAULT);
		
		//get data from file
		ArrayList<String> getParamsData = getLineFromFile(layer2Interface);	
		cm.setRootName(getParamsData.get(0));
		for(int i=1;i<getParamsData.size();i++){
			ArrayList<String> element = get4ElementFromLine(getParamsData.get(i));
			
			ParameterObject temp = new ParameterObject();
			temp.setId(Integer.parseInt(element.get(0)));
			temp.setName(element.get(1));
			temp.setParameter(element.get(2));
			temp.setType(element.get(3));
			
			dataRequest.add(temp);			
		}
		
		cm.setListParam(dataRequest);
		
		boolean ok = cm.executeCommand();
		
		if(!ok){
			//System.out.println("false");
			
			ArrayList<ParameterObject_2> returnValue = new ArrayList<ParameterObject_2>();
			
			for(int i=0;i<cm.getListParam().size();i++){
				returnValue.add((ParameterObject_2) cm.getListParam().get(i));
			}
			return returnValue;
		}else {
			//System.out.println("true");
			return null;
		}
		//return returnValue;
	}
	
	public ArrayList<ParameterObject> getInforManagerment() throws Exception{
		ArrayList<Object> dataRequest = new ArrayList<Object>();
		
		//init command
		GetParameterCommand cm = new GetParameterCommand(serialNumber, connectionRequestURL, Command.USERNAME_DEFAULT, Command.PASSWORD_DEFAULT);
		
		//get data from file
		ArrayList<String> getParamsData = getLineFromFile(FilePath.GET_MANAGEMENT);
		for(int k=0;k<getParamsData.size();k++){
			ParameterObject temp = new ParameterObject();
			
			ArrayList<String> dtaLine = get4ElementFromLine(getParamsData.get(k));
			temp.setId(Integer.parseInt(dtaLine.get(0)));
			temp.setName(dtaLine.get(1));
			temp.setParameter(dtaLine.get(2));
			temp.setType(dtaLine.get(3));
			//System.out.println(k+ ": "+temp.getId()+": "+temp.getName()+": "+temp.get);
			dataRequest.add(temp);
		}
		
		//execute command
		cm.setListParam(dataRequest);
		//System.out.println("11L :"+cm.getListParam().size());
		boolean ok = cm.executeCommand();
		if(!ok){
			//System.out.println("false");
			
			ArrayList<ParameterObject> returnValue = new ArrayList<ParameterObject>();
			
			for(int i=0;i<cm.getListParam().size();i++){
				returnValue.add((ParameterObject) cm.getListParam().get(i));
			}
			return returnValue;
		}else {
			//System.out.println("true");
			return null;
		}
//		for(int i=0;i<returnValue.size();i++){
//			System.out.println("hello: "+returnValue.get(i).getId()+"-"
//					+returnValue.get(i).getName()+"-"
//					+returnValue.get(i).getParameter()+"-"
//					+returnValue.get(i).getType());
//		}

	}

	
	
}
