package com.chengjian.vgoo2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chengjian.utils.ConstantParams;
import com.chengjian.utils.QueryFromWaycodeActivity;
import com.chengjian.vgoo2.R;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * class:�����ѯҳ�棺��ɶ�ָ���˵��Ž��в�ѯ�������ݲ�ѯ�������Ӧ��Ӧ
 * 
 * @author chengjian
 * 
 */
public class AppsFragment extends Fragment {

	private Activity mActivity;
	private EditText EditText_yundanhao;
	private TextView TextView_queryResult;
	private Button Button_submit;
	public String mailNoStr;
	public String QueryResult;

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

		View view = inflater.inflate(R.layout.query_left_layout, container,
				false);
		initUI(view);
		return view;
	}

	/**
	 * function:��ʼ���ؼ�����
	 * 
	 * @author chengjian
	 */
	public void initUI(View view) {
		EditText_yundanhao = (EditText) view
				.findViewById(R.id.EditText_yundanhao);
		EditText_yundanhao.setOnClickListener(new myClickListener());
		TextView_queryResult = (TextView) view
				.findViewById(R.id.TextView_queryResult);
		Button_submit = (Button) view.findViewById(R.id.Button_submit);
		Button_submit.setOnClickListener(new myClickListener());
		QueryResult = "";
	}

	/**
	 * class:�ڲ��࣬������ͬ�ؼ��Ĵ����ص�����
	 * 
	 * @author chengjian
	 */
	public class myClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.EditText_yundanhao:
				LayoutInflater inflater = LayoutInflater.from(mActivity);
				final View view = inflater.inflate(R.layout.edittext, null);
				new AlertDialog.Builder(mActivity)
						.setTitle("�ֶ������˵��ţ�")
						.setView(view)
						.setNegativeButton("ȡ��", null)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										EditText mailNoEditText = (EditText) view
												.findViewById(R.id.mail_no_edittext);
										mailNoStr = mailNoEditText.getText()
												.toString().trim();
										if (!mailNoStr.equals("")) {
											if (isLetterOrDigit(mailNoStr)) {
												EditText_yundanhao
														.setText(mailNoStr);
												// �ֶ��������ֱ�Ӳ�ѯ���
												Intent intent = new Intent(
														mActivity,
														QueryFromWaycodeActivity.class);
												intent.putExtra("mailNo",
														mailNoStr);
												startActivityForResult(intent,
														2);
											} else {
												Toast.makeText(mActivity,
														"�˵���ֻ�ܰ�����ĸ�����֣����������룡",
														Toast.LENGTH_SHORT)
														.show();
											}
										} else {
											EditText_yundanhao
													.setText(mailNoStr);
										}
									}
								}).show();

				break;
			case R.id.Button_submit:
				Toast.makeText(mActivity, "submit", Toast.LENGTH_SHORT).show();
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
		if (requestCode == ConstantParams.REQUEST_QUERYBILL_CODE) {
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
					JSONObject jsonObject = new JSONObject(httpResult);
					String receiverName = jsonObject.getString("recName");
					String receiverNumber = jsonObject.getString("mobile");
					QueryResult = "Name:" + receiverName + "\nPhone:"
							+ receiverNumber;
				} catch (JSONException e) {
					Log.e("ParsingResult2Json_error:", e.getMessage());
				}
			}
		}

		TextView_queryResult.setText(QueryResult);
		super.onActivityResult(requestCode, resultCode, data);
	}

}
