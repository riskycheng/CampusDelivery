package com.chengjian.vgoo2;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.chengjian.entity.Express;
import com.chengjian.utils.ConstantParams;
import com.chengjian.utils.LoadingActivity;
import com.chengjian.utils.ParsingTool;
import com.chengjian.vgoo2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * class:余件查询页面：完成对指定运单号进行查询，并根据查询结果做相应反应
 * 
 * @author chengjian
 * 
 */
public class QueryAFragment extends Fragment {

	private Activity mActivity;
	private EditText EditText_yundanhao;
	private TextView TextView_DeliveryName;
	private TextView TextView_RecName;
	private TextView TextView_Status;
	private TextView TextView_Mobile;
	private Button Button_submit;
	private Button Button_query;
	private Button Button_clear;
	public String mailNoStr;
	public String QueryResult;
	public static int cur_id = 0;
	public static String cur_bill_no = "";
	public static boolean isExist = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		System.out.println("AppsFragment :: onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("AppsFragment :: onCreateView...");

		View view = inflater.inflate(R.layout.querytab_a_layout, container,
				false);
		initUI(view);
		return view;
	}

	/**
	 * function:初始化控件声明
	 * 
	 * @author chengjian
	 */
	public void initUI(View view) {
		EditText_yundanhao = (EditText) view
				.findViewById(R.id.EditText_yundanhao);
		EditText_yundanhao.requestFocus();
		TextView_DeliveryName = (TextView) view
				.findViewById(R.id.tab_a_TextView_deliveryName);
		TextView_RecName = (TextView) view
				.findViewById(R.id.tab_a_TextView_RecName);
		TextView_Status = (TextView) view
				.findViewById(R.id.tab_a_TextView_Status);
		TextView_Mobile = (TextView) view
				.findViewById(R.id.tab_a_TextView_mobile);

		Button_submit = (Button) view.findViewById(R.id.Button_submit);
		Button_submit.setOnClickListener(new myClickListener());
		QueryResult = "";

		Button_query = (Button) view.findViewById(R.id.Btn_queryTabA);
		Button_query.setOnClickListener(new myClickListener());

		Button_clear = (Button) view.findViewById(R.id.Btn_clear);
		Button_clear.setOnClickListener(new myClickListener());
	}

	/**
	 * class:内部类，声明不同控件的触发回调函数
	 * 
	 * @author chengjian
	 */
	public class myClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/*
			 * case R.id.EditText_yundanhao: LayoutInflater inflater =
			 * LayoutInflater.from(mActivity); final View view =
			 * inflater.inflate(R.layout.edittext, null); new
			 * AlertDialog.Builder(mActivity) .setTitle("手动输入运单号：")
			 * .setView(view) .setNegativeButton("取消", null)
			 * .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { EditText mailNoEditText = (EditText) view
			 * .findViewById(R.id.mail_no_edittext); mailNoStr =
			 * mailNoEditText.getText() .toString().trim(); if
			 * (!mailNoStr.equals("")) { if (isLetterOrDigit(mailNoStr)) {
			 * EditText_yundanhao .setText(mailNoStr); // 手动输入完毕直接查询快件 Intent
			 * intent = new Intent( mActivity, QueryFromWaycodeActivity.class);
			 * intent.putExtra("mailNo", mailNoStr);
			 * startActivityForResult(intent, 2);
			 * 
			 * } else { Toast.makeText(mActivity, "运单号只能包含字母或数字，请重新输入！",
			 * Toast.LENGTH_SHORT) .show(); } } else { EditText_yundanhao
			 * .setText(mailNoStr); } } }).show();
			 * 
			 * break;
			 */
			case R.id.Button_submit:
				if(!isExist){
					Toast.makeText(mActivity, "无法重发：当前信息不存在，请先入库!", Toast.LENGTH_SHORT).show();
					break;
				}
				// 获得当前快递的id
				Log.e("cur_express_id:", cur_id + "");

				// 调用URL 重发短信
				// 入库成功后调用URL
				new Thread() {
					@Override
					public void run() {
						try {
							String urlTemp = "http://e-zhaosheng.com/vgoo/syn_fwz.php?act=syn_send&bill_id="
									+ cur_id + "&is_forced=1";
							URL url = new URL(urlTemp);
							HttpURLConnection conn = (HttpURLConnection) url
									.openConnection();
							conn.setRequestProperty("accept", "*/*");
							conn.setRequestProperty("connection", "Kepp-Alive");
							conn.setRequestProperty("user-agent",
									"Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1)");
							conn.connect();
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(mActivity, "HTTP访问异常！",
									Toast.LENGTH_SHORT).show();
						}
						// finally {
						// try {
						// bufferedReader.close();
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						// }
					}
				}.start();
				Toast.makeText(mActivity, "bill_no:" + cur_bill_no + " 短信重发成功!" , Toast.LENGTH_SHORT).show();
				break;

			case R.id.Btn_queryTabA:

				mailNoStr = EditText_yundanhao.getText().toString().trim();
				if (!mailNoStr.equals("")) {
					if (isLetterOrDigit(mailNoStr)) {
						// 手动输入完毕直接查询快件
						Intent intent = new Intent(mActivity,
								LoadingActivity.class);
						intent.putExtra("loadingType", "query_via_billno");
						intent.putExtra("methodName", "querybill");
						intent.putExtra("billNo", mailNoStr);
						//intent.putExtra("account", admin)
						startActivityForResult(intent,
								ConstantParams.QUERY_VIA_BILLNO);

					} else {
						Toast.makeText(mActivity, "运单号只能包含字母或数字，请重新输入！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mActivity, "输入不能为空!", Toast.LENGTH_SHORT)
							.show();
				}
				break;

			case R.id.Btn_clear:
				// 重置输入框
				EditText_yundanhao.setText("");
				isExist = false;
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

		// if(requestCode == 2 && resultCode == Activity.RESULT_OK)
		// Log.e("result:", data.getStringExtra("Result"));

		// 如果请求类型是查询快递单号信息
		if (requestCode == ConstantParams.QUERY_VIA_BILLNO) {
			if (resultCode == Activity.RESULT_OK) {
				String httpResult = data
						.getStringExtra(ConstantParams.EXTRA_QUERYBILL_RESULT);
				Log.e("QueryA_Result:", httpResult);
				if (httpResult.equals("[]")) {
					Toast.makeText(mActivity, "不存在,请先入库!", Toast.LENGTH_SHORT)
							.show();
					TextView_DeliveryName.setText("无");
					TextView_RecName.setText("无");
					TextView_Status.setText("无");
					TextView_Mobile.setText("无");
					//并将重发短信按钮设置为disable
					isExist = false;
					return;
				}
				//有结果则将按钮置为enable
				isExist = true;
				// 转成Json
				try {
					ArrayList<Express> expresses = ParsingTool
							.Json2Expresses(httpResult);
					if (expresses.size() == 0) {
						Toast.makeText(mActivity, "不存在，请先入库!",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Express express = expresses.get(0);
					// 解析状态
					cur_id = express.getId();
					cur_bill_no = express.getBill_no();
					TextView_Status.setText(express.getBill_status());
					TextView_Mobile.setText(express.getMobile());
					TextView_DeliveryName.setText(express.getName());
					TextView_RecName.setText(express.getRec_name());
				} catch (Exception e) {
					Log.e("ParsingResult2Json_error:", e.getMessage());
					Toast.makeText(mActivity, "查询出错，重试!", Toast.LENGTH_LONG)
							.show();
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
