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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class ContactsFragment extends Fragment {
	private Activity mActivity;
	private Spinner Spinner_QueryType;
	private EditText EditText_QueryNumber;
	private ListView ListView_Query_AllResults;
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
		ListView_Query_AllResults = (ListView) view
				.findViewById(R.id.ListView_all_Results);

		// 建立查询方式的数据源
		String items[] = getResources().getStringArray(R.array.QueryType);
		// 创建简单adpater
		ArrayAdapter<String> QueryAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_spinner_item, items);
		// 绑定数据源
		Spinner_QueryType.setAdapter(QueryAdapter);

		// 定义输入框的点击动作
		EditText_QueryNumber.setOnClickListener(new MyClickListener());
	}

	// 内部类：定义点击输入框动作
	public class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.Edittext_queryNumber:
				LayoutInflater inflater = LayoutInflater.from(mActivity);
				final View view = inflater.inflate(R.layout.edittext, null);
				new AlertDialog.Builder(mActivity)
						.setTitle("手动输入：")
						.setView(view)
						.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										EditText EditText_temp = (EditText) view
												.findViewById(R.id.mail_no_edittext);
										inputValue = EditText_temp.getText()
												.toString().trim();
										if (!inputValue.equals("")) {
											if (isLetterOrDigit(inputValue)) {
												EditText_QueryNumber
														.setText(inputValue);
												// 手动输入完毕直接查询快件
												Intent intent = new Intent(
														mActivity,
														LoadingActivity.class);
												intent.putExtra("loadingType",
														"query");
												intent.putExtra("methodName",
														"ExecuteJson");
												// 查询管理员id、密码
												intent.putExtra("SQL",
														"select * from 020_way_bill where mobile=\""
																+ inputValue
																+ "\"");
												startActivityForResult(intent,
														1);
												
											} else {
												Toast.makeText(mActivity,
														"运单号只能包含字母或数字，请重新输入！",
														Toast.LENGTH_SHORT)
														.show();
											}
										} else {
											EditText_QueryNumber
													.setText(inputValue);
										}
									}
								}).show();

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
		if (requestCode == 1) {
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
							R.layout.listview_result_layout,
							new String[] { "deliveryID", "time", "receiver",
									"status" }, new int[] {
									R.id.listview_Delivery, R.id.listview_Time,
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
			map.put("deliveryID", express.getExpressName());
			map.put("time", express.getFetchTime());
			map.put("receiver", express.getRecName());
			map.put("status", express.getExpressStatus());
			result.add(map);
		}
		return result;
	}

}
