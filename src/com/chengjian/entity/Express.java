package com.chengjian.entity;

/**
 * function:��ʾһ����ݵ�ʵ������:������ݹ�˾�����ʱ�䡢�ռ��ˡ����״̬
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
			ExpressStatus = "��ǩ��";
		else
			ExpressStatus = "δǩ��";
	}

}
