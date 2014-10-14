package com.chengjian.vgoo2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chengjian.utils.ConstantParams;
import com.chengjian.utils.LoadingActivity;
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
public class QueryAFragment extends Fragment {

	private Activity mActivity;
	private EditText EditText_yundanhao;
	private TextView TextView_DeliveryName;
	private TextView TextView_RecName;
	private TextView TextView_Status;
	private TextView TextView_Time;
	private Button Button_submit;
	private Button Button_query;
	private Button Button_clear;
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

		View view = inflater.inflate(R.layout.querytab_a_layout, container,
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
		EditText_yundanhao.requestFocus();
		TextView_DeliveryName = (TextView) view
				.findViewById(R.id.tab_a_TextView_deliveryName);
		TextView_RecName = (TextView) view
				.findViewById(R.id.tab_a_TextView_RecName);
		TextView_Status = (TextView) view
				.findViewById(R.id.tab_a_TextView_Status);
		TextView_Time = (TextView) view.findViewById(R.id.tab_a_TextView_Time);

		Button_submit = (Button) view.findViewById(R.id.Button_submit);
		Button_submit.setOnClickListener(new myClickListener());
		QueryResult = "";

		Button_query = (Button) view.findViewById(R.id.Btn_queryTabA);
		Button_query.setOnClickListener(new myClickListener());
		
		Button_clear = (Button) view.findViewById(R.id.Btn_clear);
		Button_clear.setOnClickListener(new myClickListener());
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
			/*
			 * case R.id.EditText_yundanhao: LayoutInflater inflater =
			 * LayoutInflater.from(mActivity); final View view =
			 * inflater.inflate(R.layout.edittext, null); new
			 * AlertDialog.Builder(mActivity) .setTitle("�ֶ������˵��ţ�")
			 * .setView(view) .setNegativeButton("ȡ��", null)
			 * .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { EditText mailNoEditText = (EditText) view
			 * .findViewById(R.id.mail_no_edittext); mailNoStr =
			 * mailNoEditText.getText() .toString().trim(); if
			 * (!mailNoStr.equals("")) { if (isLetterOrDigit(mailNoStr)) {
			 * EditText_yundanhao .setText(mailNoStr); // �ֶ��������ֱ�Ӳ�ѯ��� Intent
			 * intent = new Intent( mActivity, QueryFromWaycodeActivity.class);
			 * intent.putExtra("mailNo", mailNoStr);
			 * startActivityForResult(intent, 2);
			 * 
			 * } else { Toast.makeText(mActivity, "�˵���ֻ�ܰ�����ĸ�����֣����������룡",
			 * Toast.LENGTH_SHORT) .show(); } } else { EditText_yundanhao
			 * .setText(mailNoStr); } } }).show();
			 * 
			 * break;
			 */
			case R.id.Button_submit:
				Toast.makeText(mActivity, "submit", Toast.LENGTH_SHORT).show();
				break;

			case R.id.Btn_queryTabA:

				mailNoStr = EditText_yundanhao.getText().toString().trim();
				if (!mailNoStr.equals("")) {
					if (isLetterOrDigit(mailNoStr)) {
						// �ֶ��������ֱ�Ӳ�ѯ���
						Intent intent = new Intent(mActivity,
								QueryFromWaycodeActivity.class);
						intent.putExtra("mailNo", mailNoStr);
						startActivityForResult(intent, 2);

					} else {
						Toast.makeText(mActivity, "�˵���ֻ�ܰ�����ĸ�����֣����������룡",
								Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(mActivity, "���벻��Ϊ��!", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case R.id.Btn_clear:
				//���������
				EditText_yundanhao.setText("");
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
		if (requestCode == 2) {
			if (resultCode == Activity.RESULT_OK) {
				String httpResult = data
						.getStringExtra(ConstantParams.EXTRA_QUERYBILL_RESULT);
				Log.e("QueryA:httpResult:", httpResult);
				if (httpResult.equals("")) {
					Toast.makeText(mActivity, "������,�������!", Toast.LENGTH_SHORT)
							.show();
					TextView_DeliveryName.setText("��");
					TextView_RecName.setText("��");
					TextView_Status.setText("��");
					TextView_Time.setText("��");
					return;
				}
				// ת��Json
				try {

					JSONObject jsonObject = new JSONObject(httpResult);
					TextView_DeliveryName.setText(jsonObject
							.getString("companyName"));
					TextView_RecName.setText(jsonObject.getString("recName"));
					// ����״̬
					int status = jsonObject.getInt("bill_status");
					if (status == 0)
						TextView_Status.setText("����վ");
					else if (status == 1)
						TextView_Status.setText("�ѵ�վ");
					else if (status == 2)
						TextView_Status.setText("��ԤԼȡ��");
					else if (status == 3)
						TextView_Status.setText("��ԤԼ����ȡ��");
					else if (status == 4)
						TextView_Status.setText("�����");
					else if (status == 5)
						TextView_Status.setText("վ�����");
					else if (status == 6)
						TextView_Status.setText("�ͻ�����");
					else if (status == 7)
						TextView_Status.setText("ԤԼ�����ҵ�");
					else
						TextView_Status.setText("δ֪");

					TextView_Time.setText(jsonObject.getString("mobile"));
				} catch (JSONException e) {
					Log.e("ParsingResult2Json_error:", e.getMessage());
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
