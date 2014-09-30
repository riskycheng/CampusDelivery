package com.chengjian.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.chengjian.entity.Express;

public class ParsingTool {

	
	
	/**
	 * function:将查询的Json结果解析成Express对象
	 * @author chengjian
	 */
	public static ArrayList<Express> Json2Expresses(String jsonStr){
		ArrayList<Express> result = new ArrayList<Express>();
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			int length = jsonArray.length();
			for(int i = 0; i < length; i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Express express = new Express();
				express.setExpressName(jsonObject.getInt("delivery_id"));
				express.setFetchTime(jsonObject.getString("tihuo_time"));
				express.setRecName(jsonObject.getString("rec_name"));
				express.setExpressStatus(jsonObject.getBoolean("bill_status"));
				result.add(express);
			}
			Log.e("array's length:", result.size()+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
