package com.chengjian.vgoo2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chengjian.entity.Express;
import com.chengjian.utils.ConstantParams;
import com.chengjian.utils.LoadingActivity;
import com.chengjian.utils.ParsingTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class QueryBFragment extends Fragment {
	private Activity mActivity;
	private Spinner Spinner_QueryType;
	private EditText EditText_QueryNumber;
	private ListView ListView_Query_AllResults;
	private Button BtnQuery;
	private Button BtnClear;
	public static String inputValue;
	public static SimpleAdapter simpleAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 加载布局
		View view = inflater.inflate(R.layout.querytab_b_layout, container,
				false);
		initUI(view);
		return view;
	}

	// 初始化界面控件声明
	public void initUI(View view) {
		Spinner_QueryType = (Spinner) view.findViewById(R.id.Spinner_queryType);
		EditText_QueryNumber = (EditText) view
				.findViewById(R.id.Edittext_queryNumber);
		EditText_QueryNumber.requestFocus();
		ListView_Query_AllResults = (ListView) view
				.findViewById(R.id.ListView_all_Results);
		BtnQuery = (Button) view.findViewById(R.id.Btn_queryTabB);
		BtnQuery.setOnClickListener(new MyClickListener());
		BtnClear = (Button) view.findViewById(R.id.Btn_TabB_clear);
		BtnClear.setOnClickListener(new MyClickListener());
		// 建立查询方式的数据源
		String items[] = getResources().getStringArray(R.array.QueryType);
		// 创建简单adpater
		ArrayAdapter<String> QueryAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_spinner_item, items);
		QueryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定数据源
		Spinner_QueryType.setAdapter(QueryAdapter);
		Spinner_QueryType
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (position == 0)
							EditText_QueryNumber.setHint("输入手机号查询");
						else
							EditText_QueryNumber.setHint("输入运单号查询");
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						EditText_QueryNumber.setHint("输入手机号查询");
					}
				});
	}

	// 内部类：定义点击输入框动作
	public class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/*
			 * case R.id.Edittext_queryNumber: LayoutInflater inflater =
			 * LayoutInflater.from(mActivity); final View view =
			 * inflater.inflate(R.layout.edittext, null); new
			 * AlertDialog.Builder(mActivity) .setTitle("手动输入：") .setView(view)
			 * .setNegativeButton("取消", null) .setPositiveButton("确定", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { EditText EditText_temp = (EditText) view
			 * .findViewById(R.id.mail_no_edittext); inputValue =
			 * EditText_temp.getText() .toString().trim(); if
			 * (!inputValue.equals("")) { if (isLetterOrDigit(inputValue)) {
			 * EditText_QueryNumber .setText(inputValue); // 手动输入完毕直接查询快件 Intent
			 * intent = new Intent( mActivity, LoadingActivity.class);
			 * intent.putExtra("loadingType", "query");
			 * intent.putExtra("methodName", "ExecuteJson"); // 判断当前选择 int
			 * selection = Spinner_QueryType .getSelectedItemPosition(); if
			 * (selection == 0) // 手机号查询 intent.putExtra( "SQL",
			 * "select a.name,b.tihuo_time,b.rec_name,b.bill_status from 020_delivery a,020_way_bill b where a.fwz_id=1 and b.delivery_id=a.id and b.mobile=\""
			 * + inputValue + "\""); else // 运单号查询 intent.putExtra( "SQL",
			 * "select a.name,b.tihuo_time,b.rec_name,b.bill_status from 020_delivery a,020_way_bill b where a.fwz_id=1 and b.delivery_id=a.id and b.bill_number="
			 * + inputValue); startActivityForResult(intent, 1);
			 * 
			 * } else { Toast.makeText(mActivity, "运单号只能包含字母或数字，请重新输入！",
			 * Toast.LENGTH_SHORT) .show(); } } else { EditText_QueryNumber
			 * .setText(inputValue); } } }).show();
			 * 
			 * break;
			 */
			case R.id.Btn_queryTabB:

				inputValue = EditText_QueryNumber.getText().toString().trim();
				if (isLetterOrDigit(inputValue)) {
					// 手动输入完毕直接查询快件
					Intent intent = new Intent(mActivity, LoadingActivity.class);
					intent.putExtra("methodName", "querybill");
					// 判断当前选择
					int selection = Spinner_QueryType.getSelectedItemPosition();
					if (selection == 0) {
						// 手机号查询
						intent.putExtra("loadingType", "query_via_mobile");
						intent.putExtra("mobile", inputValue);
						startActivityForResult(intent,
								ConstantParams.QUERY_VIA_MOBILE);
					} else {
						// 运单号查询
						intent.putExtra("loadingType", "query_via_billno");
						intent.putExtra("billNo", inputValue);
						startActivityForResult(intent,
								ConstantParams.QUERY_VIA_BILLNO);
					}

				} else {
					Toast.makeText(mActivity, "运单号只能包含字母或数字，请重新输入！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.Btn_TabB_clear:
				EditText_QueryNumber.setText("");
				break;
			}
		}
	}

	private boolean isLetterOrDigit(String str) {
		int j = 0;
		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) >= '0' && str.charAt(i) <= '9')
					|| (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
					|| (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')) {
				j++;
			}
			if (j == str.length()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 如果请求类型是查询快递单号信息
		if (requestCode == ConstantParams.QUERY_VIA_BILLNO
				|| requestCode == ConstantParams.QUERY_VIA_MOBILE) {
			if (resultCode == Activity.RESULT_OK) {
				String httpResult = data
						.getStringExtra(ConstantParams.EXTRA_QUERYBILL_RESULT);

				Log.e("httpResult:", httpResult);
				if (httpResult.equals("")) {
					Toast.makeText(mActivity, "不存在!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				// 转成Json
				try {
					// 将json结果转化为ArrayList
					ArrayList<Express> expresses = ParsingTool
							.Json2Expresses(httpResult);
					// 此时express内的快递公司名称还未查询成功
					// 根据expresses的结果设计adpater
					simpleAdapter = new SimpleAdapter(mActivity,
							convert2List(expresses),
							R.layout.listview_result_layout, new String[] {
									"deliveryImg", "deliveryID", "billNo",
									"time", "receiver", "status" }, new int[] {
									R.id.listview_image,
									R.id.listview_Delivery,
									R.id.listview_BillNo, R.id.listview_Time,
									R.id.listview_RecName,
									R.id.listview_BillStatus });
					ListView_Query_AllResults.setAdapter(simpleAdapter);

				} catch (Exception e) {
					Log.e("ParsingResult2Json_error:", e.getMessage());
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * function:将Express的对象集合转化为List<Map>
	 * 
	 * @author chengjian
	 */
	public List<Map<String, Object>> convert2List(ArrayList<Express> expresses) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Express express : expresses) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deliveryID", express.getName());
			// 根据名称确定图片
			String name = express.getName().trim();
			if (name.startsWith("圆通"))
				map.put("deliveryImg", R.drawable.yuantong);
			else if (name.startsWith("天天"))
				map.put("deliveryImg", R.drawable.tiantain);
			else if (name.startsWith("顺丰") || name.startsWith("顺风"))
				map.put("deliveryImg", R.drawable.shunfeng);
			else if (name.startsWith("EMS"))
				map.put("deliveryImg", R.drawable.ems);
			else if (name.startsWith("韵达"))
				map.put("deliveryImg", R.drawable.yunda);
			else if (name.startsWith("联邦"))
				map.put("deliveryImg", R.drawable.lianbang);
			else if (name.startsWith("申通"))
				map.put("deliveryImg", R.drawable.shentong);
			else if (name.startsWith("中通"))
				map.put("deliveryImg", R.drawable.zhongtong);
			else if (name.startsWith("宅急送"))
				map.put("deliveryImg", R.drawable.zhaijisong);
			else
				map.put("deliveryImg", R.drawable.other);

			map.put("billNo", express.getBill_no());
			map.put("time", express.getTime());
			map.put("receiver", express.getRec_name());
			map.put("status", express.getBill_status());
			result.add(map);
		}
		return result;
	}

}
