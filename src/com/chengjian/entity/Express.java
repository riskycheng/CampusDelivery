package com.chengjian.entity;

import android.widget.ImageView;

/**
 * function:��ʾһ����ݵ�ʵ������:������ݹ�˾�����ʱ�䡢�ռ��ˡ����״̬
 * 
 * @author chengjian
 * 
 */
public class Express {
	private ImageView expressImageView;
	private String ExpressName;
	private String FetchTime;
	private String RecName;
	private String ExpressStatus;

	public ImageView getExpressImageView() {
		return expressImageView;
	}

	public void setExpressImageView(ImageView expressImageView) {
		this.expressImageView = expressImageView;
	}

	public void setExpressStatus(String expressStatus) {
		ExpressStatus = expressStatus;
	}

	public String getExpressName() {
		return ExpressName;
	}

	public void setExpressName(String expressName) {

		ExpressName = expressName;
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
