package vn.vnpttech.ssdc.nms.webapp.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import vn.vnpttech.ssdc.nms.criteria.AreaDeviceMappingCriteria;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.model.AreaDeviceMapping;
import vn.vnpttech.ssdc.nms.service.AreaDeviceMappingManager;

public class AreaDeviceMappingAction extends BaseAction {
	private static final long serialVersionUID = -7798761036950965039L;

	private AreaDeviceMappingCriteria searchCriteria;
	private Long[] deleteIds;
	private File file;
	private Boolean mappingSerial;
	private Boolean mappingMacAddress;

	@Autowired
	private AreaDeviceMappingManager areaDeviceMappingManager;

	public AreaDeviceMappingCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(AreaDeviceMappingCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public AreaDeviceMappingManager getAreaDeviceMappingManager() {
		return areaDeviceMappingManager;
	}

	public void setAreaDeviceMappingManager(
			AreaDeviceMappingManager areaDeviceMappingManager) {
		this.areaDeviceMappingManager = areaDeviceMappingManager;
	}

	public void setDeleteIds(Long[] deleteIds) {
		this.deleteIds = deleteIds;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setMappingSerial(Boolean mappingSerial) {
		this.mappingSerial = mappingSerial;
	}

	public void setMappingMacAddress(Boolean mappingMacAddress) {
		this.mappingMacAddress = mappingMacAddress;
	}

	public String index() {
		return SUCCESS;
	}

	/**
	 * Get List of area mapping
	 * 
	 * @return
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public InputStream getAreaMappingList() throws JSONException,
			UnsupportedEncodingException, Exception {
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		int totalCount = 0;

		try {
			if (searchCriteria == null) {
				searchCriteria = new AreaDeviceMappingCriteria();
			}

			// Get total Count
			totalCount = areaDeviceMappingManager.count(searchCriteria);
			log.info(totalCount + " mappings");

			List<AreaDeviceMapping> list = areaDeviceMappingManager
					.searchCriteria(searchCriteria);

			/* Put data to json object */
			if (list != null && list.size() > 0) {
				for (AreaDeviceMapping item : list) {
					JSONObject obj = new JSONObject();
					obj.put("id", item.getId());
					obj.put("province", item.getProvince());
					obj.put("district", item.getDistrict());
					obj.put("serialNumber", item.getSerialNumber());
					obj.put("macAddress", item.getMacAddress());
					obj.put("ip", item.getIp());

					array.put(obj);
				}
			}

		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e);
		}

		JSONObject data = new JSONObject();
		data.put("totalCount", totalCount);
		data.put("list", array);
		result.put("result", data);

		return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
	}

	/**
	 * Delete area mapping
	 * 
	 * @return
	 */
	public InputStream getDeleteAreaMapping() {
		JSONObject result = new JSONObject();
		try {
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					areaDeviceMappingManager.remove(deleteIds[i]);
				}
			}

			saveActionLogs(AreaDeviceMapping.class.getSimpleName(),
					ActionTypeEnum.DELETE, Arrays.toString(deleteIds));
			log.info(ActionTypeEnum.DELETE.getName() + " " + Arrays.toString(deleteIds));

			result.put(SUCCESS, true);
			result.put("msg", "Delete successful!");

		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e);
		}
		
		return new ByteArrayInputStream(result.toString().getBytes());
	}

	public InputStream getUploadAreaMappingFile() {
		JSONObject result = new JSONObject();
		try {
			
			JSONArray array = new JSONArray();
			
			try {
				//Parse excel
				List<AreaDeviceMapping> list = processExcelFile(file);
				List<AreaDeviceMapping> listToBeSaved = new ArrayList<AreaDeviceMapping>();
				
				//iterate through items
				for(AreaDeviceMapping item : list){
					//System.out.println(item.toString());
					
					JSONObject obj = new JSONObject();
					
					obj.put("province", item.getProvince());
					obj.put("district", item.getDistrict());
					obj.put("serialNumber", item.getSerialNumber());
					obj.put("macAddress", item.getMacAddress());
					
					//Check valid province: not null and not empty
					if(StringUtils.isBlank(item.getProvince())){
						obj.put("note", getText("management.areaMapping.upload.emptyProvince"));
						obj.put("status", false);
					}
					
					//check serial number empty
					else if(StringUtils.isBlank(item.getSerialNumber()) && mappingSerial != null && mappingSerial){
						obj.put("note", getText("management.areaMapping.upload.emptySerial"));
						obj.put("status", false);
					}
					//check mac address empty
					else if(StringUtils.isBlank(item.getMacAddress()) && mappingMacAddress != null && mappingMacAddress){
						obj.put("note", getText("management.areaMapping.upload.emptyMac"));
						obj.put("status", false);
					}
					//check mapping both
					else if(mappingSerial && mappingMacAddress && 
							(StringUtils.isBlank(item.getMacAddress()) || StringUtils.isBlank(item.getSerialNumber()))){
						obj.put("note", getText("management.areaMapping.upload.emptyBoth"));
						obj.put("status", false);
					}else{
						obj.put("note", getText("message.success"));
						obj.put("status", true);
						listToBeSaved.add(item);
					}
					
					array.put(obj);
				}
				
				int mapping = mappingSerial && mappingMacAddress ? 3 : (mappingSerial ? 1: 2);
				
				areaDeviceMappingManager.saveList(listToBeSaved, mapping);
				
				saveActionLogs(AreaDeviceMapping.class.getSimpleName(), ActionTypeEnum.UPLOAD, listToBeSaved.size() + " items saved");
				log.info(ActionTypeEnum.UPLOAD.getName() + " " + listToBeSaved.size() + " items saved");

				result.put(SUCCESS, true);
	            result.put("msg", "successful!");
	            result.put("list", array);
				
			} catch (Exception e) {
				result.put(SUCCESS, false);
	            result.put("msg", getText("management.areaMapping.invalidFile"));
	            log.error(e);
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e);
			//return null;
		}
		
		return new ByteArrayInputStream(result.toString().getBytes());
	}

	private List<AreaDeviceMapping> processExcelFile(File file) throws IOException{
		List<AreaDeviceMapping> list = new ArrayList<AreaDeviceMapping>();
		
		//try {
			// Creating Input Stream
			FileInputStream myInput = new FileInputStream(file);
			
			// Create a workbook using the File System 
			XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
			
			// Get the first sheet from workbook 
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);
			
			AreaDeviceMapping item = new AreaDeviceMapping();
			
			/** We now need something to iterate through the cells.**/
			Iterator<Row> rowIter = mySheet.rowIterator();
			while(rowIter.hasNext()){
				XSSFRow myRow = (XSSFRow) rowIter.next();
				Iterator<Cell> cellIter = myRow.cellIterator();
				while(cellIter.hasNext()){
					XSSFCell myCell = (XSSFCell) cellIter.next();
					
					if(myCell.getCellType() != HSSFCell.CELL_TYPE_STRING){
						myCell.setCellType(Cell.CELL_TYPE_STRING);
					}
					String value = myCell.getStringCellValue().trim();
					
					switch (myCell.getColumnIndex()) {
						case 0: //province
							item.setProvince(value);
							break;
							
						case 1: //district
							item.setDistrict(value);
							break;
							
						case 2: //serial number
							item.setSerialNumber(value);
							break;
							
						case 3: //mac address
							item.setMacAddress(value);
							break;
							
						/*case 4: //ip range
							item.setIp(value);
							break;*/
	
						default:
							break;
					}
					
					
				}
				
				if(item.getProvince() != null || item.getDistrict() != null || item.getIp() != null || item.getSerialNumber() != null){
					if(item.getProvince().equals("Province")){
						item = new AreaDeviceMapping(); //for next row
						continue;
					}
					list.add(item);
					item = new AreaDeviceMapping(); //for next row
				}
				
			}	
			
		/*} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		return list;
	}
}
