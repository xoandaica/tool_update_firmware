package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;
//package org.openacs.mycommand;
//
//import java.io.ByteArrayOutputStream;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import javax.servlet.http.HttpSession;
//
//import org.openacs.ACSServlet;
//import org.openacs.database.CPE_Inventory_Table;
//import org.openacs.database.DBAccess;
//import org.openacs.message.AddObject;
//import org.openacs.message.Download;
//import org.openacs.message.GetParameterValues;
//import org.openacs.message.Inform;
//import org.openacs.message.Reboot;
//import org.openacs.message.SetParameterValues;
//import org.openacs.myobject.CacheCommandObject;
//
//public class CommandCacheFactory {
//	private ArrayList<CacheCommandObject> listCommand = new ArrayList<CacheCommandObject>();
//			
//	public ArrayList<CacheCommandObject> getListCommand() {
//		return listCommand;
//	}
//
//	public void setListCommand(ArrayList<CacheCommandObject> listCommand) {
//		this.listCommand = listCommand;
//	}
//
//	public void addCommand(CacheCommandObject cmd){
//		this.listCommand.add(cmd);
//	}
//	
//	public CacheCommandObject getCacheCommand(){
//		if(listCommand.size()>0){
//			CacheCommandObject temp = listCommand.get(0);
//			//listCommand.remove(0);
//			return temp;
//		}
//		return null;
//	}
//	
//	public boolean removeCommand(String event){
//		for(CacheCommandObject items: listCommand){
//			if(items.getEventCode().equals("event")){
//				return listCommand.remove(items);
//			}
//		}
//		return false;
//	}
//	
//	public void processRequest(HttpSession session, Inform lastInform, ByteArrayOutputStream out, String event){
////		System.out.println("11111111111111111111: "+event);
////		if(event.equals("0 BOOTSTRAP")){
////			
////		}else if(event.equals("1 BOOT")){
////			
////		}else if(event.equals("2 PERIODIC")){
////			
////		}else if(event.equals("4 VALUE CHANGE")){
////			
////		}else 
//		if(event.equals(CacheCommandObject.EVENT_6)){
//			removeCommand(CacheCommandObject.EVENT_6);
//			Command cm = CommandRequestFactory.getCommand(lastInform.sn);
//    		if(cm!=null){
//    			if(cm.getType().equals(Command.TYPE_GETTREEVALUE)){
//    				GetValueCommand gtValCmd = (GetValueCommand) cm;
//    				
//    				GetParameterValues recm = new GetParameterValues(gtValCmd.getTreeNode());
//    				recm.writeTo(out);
//    			}else if(cm.getType().equals(Command.TYPE_SETVALUE)){
//    				SetValueCommand setCmd = (SetValueCommand) cm;
//    				
//    				SetParameterValues recm = new SetParameterValues();
//    				for(int i=0;i<setCmd.getListObj().size();i++){
//    					recm.AddValue(setCmd.getListObj().get(i).getParameter(), setCmd.getListObj().get(i).getValue(), setCmd.getListObj().get(i).getType());
//    				}
//    				recm.writeTo(out);
//    			}else if(cm.getType().equals(Command.TYPE_ADDOBJECT)){
//    				AddObjectCommand addCmd = (AddObjectCommand) cm;
//    				AddObject recm = new AddObject(addCmd.getRootName()+".", "");
//    				recm.writeTo(out);
//    			}else if(cm.getType().equals(Command.TYPE_REBOOT)){
//    				//RebootCommand rbCmd = (RebootCommand)cm;
//    				Reboot recm = new Reboot();
//    				recm.writeTo(out);
//    			}else if(cm.getType().equals(Command.TYPE_UPGRADE_FW)){
//    				UpdateFirmwareCommand upFWCmd = (UpdateFirmwareCommand) cm;
//    				Download recm = new Download("", upFWCmd.getUrlFileServer(), Download.FT_FIRMWARE);
//    				recm.UserName = upFWCmd.getUsernameFileServer();
//    				recm.Password = upFWCmd.getPasswordFileServer();
//    				recm.writeTo(out);
//    			}	
//    		}
//    		session.setAttribute(ACSServlet.ATTR_CACHECOMMAND, this);
//		}else if(event.equals(CacheCommandObject.EVENT_0)){
//			//insert device to database
//			CPE_Inventory_Table cpeInfor = new CPE_Inventory_Table();
//			cpeInfor.setConnectionRequestURL(lastInform.getConnectionRequestURL());
//			cpeInfor.setHardwareVersion(lastInform.getHardwareVersion());
//			cpeInfor.setManufacturer(lastInform.Manufacturer);
//			cpeInfor.setOui(lastInform.getOui());
//			cpeInfor.setProductClass(lastInform.ProductClass);
//			cpeInfor.setProvisioningCode(lastInform.getProvisiongCode());
//			cpeInfor.setSerialNumber(lastInform.sn);
//			cpeInfor.setSoftwareVersion(lastInform.getSoftwareVersion());
//			try {
//				DBAccess.insertRecordInventory(cpeInfor);
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			//end insert
//			//set enable inform and interval
//			SetParameterValues recm = new SetParameterValues();
//			recm.AddValue("InternetGatewayDevice.ManagementServer.PeriodicInformEnable", "1", "xsd:boolean");
//			recm.AddValue("InternetGatewayDevice.ManagementServer.PeriodicInformInterval", "299", "xsd:unsignedInt");
//			recm.writeTo(out);
//			
//		}else{			
//			removeCommand(event);
//			CacheCommandObject temp = getCacheCommand();
//			if(temp!=null){
//				processRequest(session, lastInform, out, temp.getEventCode());
//			}
//			session.setAttribute(ACSServlet.ATTR_CACHECOMMAND, this);
//		}
//		
//		
//	}
//	
//}
