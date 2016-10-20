package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.util.ArrayList;

public class SetParameterCommand extends Command{
	private ArrayList<Object> listParams = new ArrayList<Object>();
	
	public ArrayList<Object> getListParam() {
		return listParams;
	}

	public void addParamObject(ParameterObject paraOb){
		this.listParams.add(paraOb);
	}

	public void setListParam(ArrayList<Object> listParam) {
		this.listParams = listParam;
	}

	public synchronized void receiveResult() {
		System.out.println("ID 22222 Thread: "+Thread.currentThread().getId() + "_NAME: "+Thread.currentThread().getName());
		//this.listParams = listParam;
		System.out.println("REceive result true");
		hasResult = true;
		errorCheck =false;
		FactoryCommand.removeCommand(this);
		notify();
	}	
}
