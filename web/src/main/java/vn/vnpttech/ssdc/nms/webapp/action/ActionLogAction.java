package vn.vnpttech.ssdc.nms.webapp.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import vn.vnpttech.ssdc.nms.criteria.ActionLogsCriteria;
import vn.vnpttech.ssdc.nms.model.ActionLogs;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.util.DateUtil;


/**
 * Action for facilitating User Management feature.
 */
public class ActionLogAction extends BaseAction {
    private static final long serialVersionUID = 6776558938712115191L;
    
    private ActionLogsCriteria searchCriteria;
    
    public ActionLogsCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(ActionLogsCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	
	public String index() {
        return SUCCESS;
    }

	/**
     * Get user list
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public InputStream getActionLogList() throws JSONException, UnsupportedEncodingException, Exception {
    	JSONObject result = new JSONObject();
		JSONArray array  = new JSONArray();
		
		int totalCount = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			if(searchCriteria == null){
				searchCriteria = new ActionLogsCriteria();
			}
			
			/* Get time params */
			try {
				String from = getRequest().getParameter("startTime");
				if(from != null && !from.equals("")){
					from = from.replace("T", " ");
					searchCriteria.setStartTime(formatter.parse(from));
				}
				
				String to = getRequest().getParameter("endTime");
				if(to != null && !to.equals("")){
					to = to.replace("T", " ");
					searchCriteria.setEndTime(formatter.parse(to));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			//Get total Count
			totalCount = logManager.countActionLogs(searchCriteria);
			log.info(totalCount + " actions");
			
			List<ActionLogs> list = logManager.searchCriteria(searchCriteria);
			
			/* Put data to json object */
			if(list != null && list.size() > 0){
				for(ActionLogs item : list ){
					JSONObject obj = new JSONObject();
					obj.put("id", item.getId());
					obj.put("username", item.getUsername());
					obj.put("actionTime", DateUtil.convertDateToDateTimeString(item.getActionTime()));
					obj.put("actionObject", item.getActionObject());
					obj.put("description", item.getDescription());
					
					for(ActionTypeEnum type : ActionTypeEnum.values()){
						if(type.getType().equals(item.getActionType())){
							obj.put("actionType", type.getName());
						}
					}
					
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
}
