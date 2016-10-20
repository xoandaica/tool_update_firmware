package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.util.ArrayList;

public class AddObjectCommand extends Command{
	
	private String rootObject;
	private int instance;
	private ArrayList<Object> listParams = new ArrayList<Object>();
		
	
	public String getRootObject() {
		return rootObject;
	}

	public void setRootObject(String rootObject) {
		this.rootObject = rootObject;
	}

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

	public ArrayList<Object> getListParam() {
		return listParams;
	}

	public void addParamObject(ParameterObject paraOb){
		this.listParams.add(paraOb);
	}

	public void setListParam(ArrayList<Object> listParam) {
		this.listParams = listParam;
	}


	public boolean executeCommand() throws Exception{
		FactoryCommand.addCommand(this);
		ConnectionCPE.RequestConnectionHttp(getConnectionRequestURL(),"xuan","123");

		System.out.println("ID 1111 Thread: "+Thread.currentThread().getId() + "_NAME: "+Thread.currentThread().getName());
		synchronized (this) {
			while(!hasResult){
				wait(5000);
			}
		}
		
		return errorCheck;
	}

	public synchronized void receiveError(String errorString){
		hasResult =true;
		errorCheck =true;
		this.errorString = errorString;
		FactoryCommand.removeCommand(this);
		notify();
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
