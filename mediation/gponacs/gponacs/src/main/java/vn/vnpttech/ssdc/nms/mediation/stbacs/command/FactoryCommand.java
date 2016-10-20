package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.util.ArrayList;

public class FactoryCommand {
	private static ArrayList<Command> listCommand = new ArrayList<Command>();
	
	public static ArrayList<Command> getInstance(){
		return listCommand;
	}

	public static boolean removeCommand(Command cm){
		for(int i=0;i<listCommand.size();i++){
			if(listCommand.get(i).getSerialNumberCPE().equals(cm.getSerialNumberCPE())){
				getInstance().remove(i);
				return true;
			}
		}
		return false;
	}
	
	public static boolean addCommand(Command cm){
		for(int i=0;i<listCommand.size();i++){
			if(listCommand.get(i).getSerialNumberCPE().equals(cm.getSerialNumberCPE())){
				return false;
			}
		}
		getInstance().add(cm);		
		return true;
	}
	
	public static Command getCommand(String serialNumberCPE){
		for(int i=0;i<listCommand.size();i++){
			if(listCommand.get(i).getSerialNumberCPE().equals(serialNumberCPE)){
				return listCommand.get(i);
			}
		}
		
		return null;
	}
}
