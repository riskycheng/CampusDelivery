package com.chengjian.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.chengjian.entity.Express;

public class ParsingTool {

	/**
	 * function:将查询的Json结果解析成Express对象
	 * 
	 * @param jsonStr
	 *            输入查询返回的json字符串
	 * @author chengjian
	 */
	public static ArrayList<Express> Json2Expresses(String jsonStr) {
		ArrayList<Express> result = new ArrayList<Express>();
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Express express = new Express();
				express.setExpressName(jsonObject.getString("name"));
				express.setFetchTime(jsonObject.getString("tihuo_time"));
				express.setRecName(jsonObject.getString("rec_name"));
				express.setExpressStatus(jsonObject.getBoolean("bill_status"));
				result.add(express);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * function:将普通字符串转化为MD5加密方式
	 */
	public static String Convert2MD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

}
