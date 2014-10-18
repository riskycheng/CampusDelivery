package com.chengjian.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import com.chengjian.vgoo2.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class LoadingActivity extends Activity {
	private TextView textView = null;
	// String endPoint = "";
	String result = null;
	String password = null;
	String s1, s2, s3, s4, s5, s6 = null;
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	public static final int LOGIN_MSG = 0;
	public static final int QUERY_VIA_SQL_MSG = 1;
	public static final int QUERY_VIA_BILLNO_MSG = 2;
	public static final int QUERY_VIA_MOBILE_MSG = 3;
	public static final int INSTORE_MSG = 4;
	public static final int SETSMS_MSG = 5;
    public static int admin_id = 0;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent intent = LoadingActivity.this.getIntent();
			switch (msg.what) {
			case QUERY_VIA_MOBILE_MSG:
			case QUERY_VIA_BILLNO_MSG:
			case QUERY_VIA_SQL_MSG:
			case INSTORE_MSG:
			case LOGIN_MSG: // ���� ��¼ ��ѯ�������
				intent.putExtra("Result", result);
				setResult(RESULT_OK, intent);
				finish();
				break;
			case SETSMS_MSG://���� ���ö���ģ�幦��
				intent.putExtra("Result", result);
				setResult(RESULT_OK, intent);
				finish();
				break;
			default:
				break;
			}
		}
	};

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT); // ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading);
		textView = (TextView) findViewById(R.id.textView);

		final Intent intent = this.getIntent();
		final String loadingType = intent.getStringExtra("loadingType");
		final String methodName = intent.getStringExtra("methodName");

		// Log.e("sql:", SQL);
		// check
		// Log.e("UserName:", UserName);
		// Log.e("Password:", UserPassword);
		if (loadingType.equals("login")) {
			textView.setText("���ڵ�¼...");
		} else if (loadingType.equals("query")
				|| loadingType.equals("query_via_billno")
				|| loadingType.equals("query_via_mobile")) {
			textView.setText("���ڲ�ѯ...");
		} else if (loadingType.equals("refresh")) {
			textView.setText("����ˢ��...");
		} else if (loadingType.equals("modify")) {
			textView.setText("���ڸ�������...");
		} else if (loadingType.equals("refresh_spinner")) {
			textView.setText("��ȡ������˾...");
		}  else if (loadingType.equals("add_sms")) {
			textView.setText("���Ӷ���ģ��...");
		}else if (loadingType.equals("instore")) {
			textView.setText("������...");
			s1 = intent.getStringExtra("s1");
			s2 = intent.getStringExtra("s2");
			s3 = intent.getStringExtra("s3");
			s4 = intent.getStringExtra("s4");
			s5 = intent.getStringExtra("s5");
			s6 = intent.getStringExtra("s6");
		}
		mySharedPreferences = getSharedPreferences("myPreference",
				Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		admin_id = mySharedPreferences.getInt("admin_id", 0);
		Log.e("loadingActivity:admin_id:", admin_id+"");
		new Thread() {
			@Override
			public void run() {
				if (loadingType.equals("login")) {
					// ���LoginActivity�������Ĳ���
					final String UserName = intent.getStringExtra("name");
					final String UserPassword = intent
							.getStringExtra("password");

					result = callWebService_Login(methodName, UserName,
							UserPassword); // ����webservice��ѯ
					handler.sendEmptyMessage(LOGIN_MSG);
				} else if (loadingType.equals("instore")) {
					result = callWebService2(methodName);
					handler.sendEmptyMessage(INSTORE_MSG);
				} else if (loadingType.equals("query")) {
					// ��ȡ��ѯ��SQL���
					final String SQL = intent.getStringExtra("SQL");
					result = callWebService(methodName, SQL);
					handler.sendEmptyMessage(QUERY_VIA_SQL_MSG);
				} else if (loadingType.equals("query_via_billno")) {
					// ��ȡ��ѯ��SQL���
					final String BillNo = intent.getStringExtra("billNo");
					// result = callWebService(methodName, SQL);
					// handler.sendEmptyMessage(QUERY_VIA_SQL_MSG);
					result = callWebService_QueryBillViaBillNo(methodName,
							BillNo);
					handler.sendEmptyMessage(QUERY_VIA_BILLNO_MSG);
				} else if (loadingType.equals("query_via_mobile")) {
					// ��ȡ��ѯ��SQL���
					final String Mobile = intent.getStringExtra("mobile");
					result = callWebService_QueryBillViaMobile(methodName,
							Mobile);
					handler.sendEmptyMessage(QUERY_VIA_MOBILE_MSG);
				}else if (loadingType.equals("add_sms")) {
					// ��ȡ��ѯ��SQL���
					final String smsContent = intent.getStringExtra("smsContent");
					final int account = intent.getIntExtra("account",0);
					result = String.valueOf(callWebService_SetSMS(methodName,
							smsContent,String.valueOf(account)));
					handler.sendEmptyMessage(SETSMS_MSG);
				}

			}
		}.start();
	}

	public String callWebService_Test_HelloWorld(String methodName) {
		String nameSpace = "http://tempuri.org/"; // �����ռ�
		String endPoint = "http://203.195.188.172:8091/PostService.asmx"; // EndPoint
		String soapAction = nameSpace + methodName; // SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName); // ָ��WebService�������ռ�͵��õķ�����
		// soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10); // ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc; // �ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true; // �����Ƿ���õ���dotNet������WebService

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope); // ����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		if (object != null) {
			String result = object.getProperty(0).toString();
			if (result == null) {
				result = "��ѯΪ��"; // result���Ϊ�գ���ѯΪ��
			}
			return result;
		} else {
			return "��ѯʧ��"; // object����Ϊ�գ���������ʧ��
		}
	}

	/**
	 * function:ͨ��WebService���õ�¼����
	 * 
	 * @param methodName
	 *            ���󷽷�����
	 * @param UserName
	 *            ������û���
	 * @param UserPassword
	 *            ������û�����
	 * @return �������� -1 ʧ�� 0�������� 1 or others���ɹ�
	 */
	public String callWebService_Login(String methodName, String UserName,
			String UserPassword) {
		String result = null;
		String nameSpace = "http://tempuri.org/"; // �����ռ�
		String endPoint = "http://118.123.22.62:8090/PostService.asmx"; // EndPoint
		String soapAction = nameSpace + methodName; // SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName); // ָ��WebService�������ռ�͵��õķ�����
		if (UserName != null && UserPassword != null) {
			// WebService�ӿڵĲ���
			rpc.addProperty("name", UserName);
			// ת����MD5����
			rpc.addProperty("pwd", ParsingTool.Convert2MD5Str(UserPassword));
		}

		// soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10); // ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc; // �ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true; // �����Ƿ���õ���dotNet������WebService

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope); // ����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ȡ����ֵ
		SoapObject object = (SoapObject) envelope.bodyIn;
		if (object != null) {
			result = object.getProperty(0).toString();
			return result;
		} else {
			return "-1";
		}
	}

	public String callWebService(String methodName, String SQL) {
		String nameSpace = "http://tempuri.org/"; // �����ռ�
		String endPoint = "http://203.195.188.172:8091/PostService.asmx"; // EndPoint
		String soapAction = nameSpace + methodName; // SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName); // ָ��WebService�������ռ�͵��õķ�����
		if (SQL != null) {
			rpc.addProperty("Sqlstr", SQL); // WebService�ӿڵĲ���
		}

		// soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10); // ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc; // �ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true; // �����Ƿ���õ���dotNet������WebService

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope); // ����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		if (object != null) {
			String result = object.getProperty(0).toString();
			if (result == null) {
				result = "��ѯΪ��"; // result���Ϊ�գ���ѯΪ��
			}
			return result;
		} else {
			return "��ѯʧ��"; // object����Ϊ�գ���������ʧ��
		}
	}

	/**
	 * function:�����˵��Ų�ѯ
	 * 
	 * @param methodName
	 * @param BillNo
	 * @return
	 */
	public String callWebService_QueryBillViaBillNo(String methodName,
			String BillNo) {
		String result = null;
		String nameSpace = "http://tempuri.org/"; // �����ռ�
		String endPoint = "http://118.123.22.62:8090/PostService.asmx"; // EndPoint
		String soapAction = nameSpace + methodName; // SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName); // ָ��WebService�������ռ�͵��õķ�����
		if (BillNo != null) {
			// WebService�ӿڵĲ���
			rpc.addProperty("bill_no", BillNo);
			rpc.addProperty("mobile", "");
			rpc.addProperty("account", admin_id);
		}

		// soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10); // ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc; // �ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true; // �����Ƿ���õ���dotNet������WebService

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope); // ����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ȡ����ֵ
		SoapObject object = (SoapObject) envelope.bodyIn;
		if (object != null) {
			result = object.getProperty(0).toString();
			return result;
		} else {
			return "-1";
		}
	}

	/**
	 * function:�����ֻ��Ų�ѯ
	 * 
	 * @param methodName ������
	 * @param Mobile �ֻ���
	 * @return ����json���
	 * @author chengjian
	 */
	public String callWebService_QueryBillViaMobile(String methodName,
			String Mobile) {
		String result = null;
		String nameSpace = "http://tempuri.org/"; // �����ռ�
		String endPoint = "http://118.123.22.62:8090/PostService.asmx"; // EndPoint
		String soapAction = nameSpace + methodName; // SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName); // ָ��WebService�������ռ�͵��õķ�����
		if (Mobile != null) {
			// WebService�ӿڵĲ���
			rpc.addProperty("bill_no", "");
			rpc.addProperty("mobile", Mobile);
			rpc.addProperty("account",admin_id);
		}

		// soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10); // ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc; // �ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true; // �����Ƿ���õ���dotNet������WebService

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope); // ����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ȡ����ֵ
		SoapObject object = (SoapObject) envelope.bodyIn;
		if (object != null) {
			result = object.getProperty(0).toString();
			return result;
		} else {
			return "-1";
		}
	}

	public String callWebService2(String methodName) {
		String nameSpace = "http://tempuri.org/"; // �����ռ�
		String endPoint = "http://118.123.22.62:8090/PostService.asmx"; // EndPoint
		String soapAction = nameSpace + methodName; // SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName); // ָ��WebService�������ռ�͵��õķ�����

		rpc.addProperty("s1", s1);
		rpc.addProperty("s2", s2);
		rpc.addProperty("s3", s3);
		rpc.addProperty("s4", s4);
		rpc.addProperty("s5", s5);
		rpc.addProperty("s6", s6);

		// soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10); // ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc; // �ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true; // �����Ƿ���õ���dotNet������WebService

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope); // ����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ȡ���ص�����
		// System.out.println("111112222==envelope.bodyIn=="+envelope.bodyIn);
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
			return "���ʧ��";
		}
		// SoapObject object = null;
		// try {
		// object = (SoapObject) envelope.getResponse();
		// } catch (SoapFault e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		if (object != null) {
			String result = object.getProperty(0).toString();
			if (result == null) {
				result = "��ѯΪ��"; // result���Ϊ�գ���ѯΪ��
			}
			return result;
		} else {
			return "��ѯʧ��"; // object����Ϊ�գ���������ʧ��
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	//
	// }
	// return false;
	// }

	/**
	 * function:���ö���ģ��
	 * @param methodName ������
	 * @param account ��ǰ�ʺ�
	 * @author chengjian
	 * @return ����ֵ 0������ʧ��  >0 ���ӳɹ�
	 */
	public int callWebService_SetSMS(String methodName, String smsContent,
			String account) {

		int result = 0;
		String nameSpace = "http://tempuri.org/"; // �����ռ�
		String endPoint = "http://118.123.22.62:8090/PostService.asmx"; // EndPoint
		String soapAction = nameSpace + methodName; // SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName); // ָ��WebService�������ռ�͵��õķ�����
		if (smsContent != null) {
			// WebService�ӿڵĲ���
			rpc.addProperty("content", smsContent);
			rpc.addProperty("account", account);
		}

		// soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10); // ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc; // �ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true; // �����Ƿ���õ���dotNet������WebService

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope); // ����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ȡ����ֵ
		SoapObject object = (SoapObject) envelope.bodyIn;
		if (object != null)
			result = Integer.parseInt(object.getProperty(0).toString().trim());
		return result;
	}

}
