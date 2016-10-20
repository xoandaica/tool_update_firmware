package vn.vnpttech.ssdc.nms.criteria;

import java.util.Date;

public class ActionLogsCriteria extends BaseCriterialObj {
	private String username;
	private Date startTime;
	private Date endTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
