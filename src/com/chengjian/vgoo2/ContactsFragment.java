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
		// ���ز���
		View view = inflater.inflate(R.layout.querytab_b_layout, container,
				false);
		initUI(view);
		return view;
	}

	// ��ʼ������ؼ�����
	public void initUI(View view) {
		Spinner_QueryType = (Spinner) view.findViewById(R.id.Spinner_queryType);
		EditText_QueryNumber = (EditText) view
				.findViewById(R.id.Edittext_queryNumber);
		ListView_Query_AllResults = (ListView) view
				.findViewById(R.id.ListView_all_Results);

		// ������ѯ��ʽ������Դ
		String items[] = getResources().getStringArray(R.array.QueryType);
		// ������adpater
		ArrayAdapter<String> QueryAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_spinner_item, items);
		// ������Դ
		Spinner_QueryType.setAdapter(QueryAdapter);

		// ���������ĵ������
		EditText_QueryNumber.setOnClickListener(new MyClickListener());
	}

	// �ڲ��ࣺ�������������
	public class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.Edittext_queryNumber:
				LayoutInflater inflater = LayoutInflater.from(mActivity);
				final View view = inflater.inflate(R.layout.edittext, null);
				new AlertDialog.Builder(mActivity)
						.setTitle("�ֶ����룺")
						.setView(view)
						.setNegativeButton("ȡ��", null)
						.setPositiveButton("ȷ��",
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
												// �ֶ��������ֱ�Ӳ�ѯ���
												Intent intent = new Intent(
														mActivity,
														LoadingActivity.class);
												intent.putExtra("loadingType",
														"query");
												intent.putExtra("methodName",
														"ExecuteJson");
												// ��ѯ����Աid������
												intent.putExtra("SQL",
														"select * from 020_way_bill where mobile=\""
																+ inputValue
																+ "\"");
												startActivityForResult(intent,
														1);
												
											} else {
												Toast.makeText(mActivity,
														"�˵���ֻ�ܰ�����ĸ�����֣����������룡",
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

		// ������������ǲ�ѯ��ݵ�����Ϣ
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				String httpResult = data
						.getStringExtra(ConstantParams.EXTRA_QUERYBILL_RESULT);

				Log.e("httpResult:", httpResult);
				if (httpResult.equals("")) {
					Toast.makeText(mActivity, "������!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				// ת��Json
				try {
					// ��json���ת��ΪArrayList
					ArrayList<Express> expresses = ParsingTool
							.Json2Expresses(httpResult);
					// ��ʱexpress�ڵĿ�ݹ�˾���ƻ�δ��ѯ�ɹ�
					// ����expresses�Ľ�����adpater
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
	 * function:��Express�Ķ��󼯺�ת��ΪList<Map>
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
