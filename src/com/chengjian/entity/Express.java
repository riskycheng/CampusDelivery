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
	private int id;
	private String bill_number;
	private String bill_no;
	private String mobile;
	private String rec_name;
	private String bill_status;
	private String time;
	private String name;

	public ImageView getExpressImageView() {
		return expressImageView;
	}

	public void setExpressImageView(ImageView expressImageView) {
		this.expressImageView = expressImageView;
	}

	public int getId() {
		return  id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBill_number() {
		return  bill_number;
	}

	public void setBill_number(String bill_number) {
		this.bill_number = bill_number;
	}

	public String getBill_no() {
		 return  bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getMobile() {
		return  mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRec_name() {
		return rec_name;
	}

	public void setRec_name(String rec_name) {
		this.rec_name = rec_name;
	}

	public String getBill_status() {
		
		return bill_status;
		
	}

	public void setBill_status(int status) {
		if (status == 0)
			bill_status =  "待到站";
		else if (status == 1)
			bill_status =  "已到站";
		else if (status == 2)
			bill_status =  "已预约取件";
		else if (status == 3)
			bill_status =  "已预约上门取件";
		else if (status == 4)
			bill_status =  "已提货";
		else if (status == 5)
			bill_status =  "站点拒收";
		else if (status == 6)
			bill_status =  "客户拒收";
		else if (status == 7)
			bill_status =  "预约件已找到";
		else
			bill_status =  "未知";
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return  name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
