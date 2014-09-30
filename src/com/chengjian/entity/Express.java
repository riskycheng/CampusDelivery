package com.chengjian.entity;

/**
 * function:表示一个快递的实体类型:包含快递公司、提货时间、收件人、快递状态
 * 
 * @author chengjian
 * 
 */
public class Express {
	private String ExpressName;
	private String FetchTime;
	private String RecName;
	private String ExpressStatus;

	
	
	
	public String getExpressName() {
		return ExpressName;
	}

	public void setExpressName(int expressName) {
		ExpressName = String.valueOf(expressName);
	}

	public String getFetchTime() {
		return FetchTime;
	}

	public void setFetchTime(String fetchTime) {
		FetchTime = fetchTime;
	}

	public String getRecName() {
		return RecName;
	}

	public void setRecName(String recName) {
		RecName = recName;
	}

	public String getExpressStatus() {
		return ExpressStatus;
	}

	public void setExpressStatus(boolean expressStatus) {
		if (expressStatus)
			ExpressStatus = "已签收";
		else
			ExpressStatus = "未签收";
	}

}
