package com.chengjian.entity;

import android.widget.ImageView;

/**
 * function:表示一个快递的实体类型:包含快递公司、提货时间、收件人、快递状态
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
			ExpressStatus = "已签收";
		else
			ExpressStatus = "未签收";
	}

}
