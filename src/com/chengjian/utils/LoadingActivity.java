package com.chengjian.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import com.chengjian.vgoo2.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class LoadingActivity extends Activity {
	private TextView textView = null;
//	String endPoint = "";
	String result = null;
	String password = null;
	String s1, s2, s3, s4, s5, s6 = null;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case 1:		//�����ѯ�������
					Intent intent = LoadingActivity.this.getIntent();
					intent.putExtra("Result", result);
					setResult(RESULT_OK, intent);
					finish();
					break;
				default:
					break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);	//��������
		setContentView(R.layout.loading);
		textView = (TextView)findViewById(R.id.textView);
		
		Intent intent = this.getIntent();
		final String loadingType = intent.getStringExtra("loadingType");
		final String methodName = intent.getStringExtra("methodName");
		final String SQL = intent.getStringExtra("SQL");
		Log.e("sql:", SQL);
		if(loadingType.equals("login")) {
			textView.setText("���ڵ�¼...");
		}
		else if(loadingType.equals("query")){
			textView.setText("���ڲ�ѯ...");
		}
		else if(loadingType.equals("refresh")){
			textView.setText("����ˢ��...");
		}
		else if(loadingType.equals("modify")){
			textView.setText("���ڸ�������...");
		}
		else if(loadingType.equals("refresh_spinner")) {
			textView.setText("��ȡ������˾...");
		}
		else if(loadingType.equals("instore")){
			textView.setText("������...");
			s1 = intent.getStringExtra("s1");
			s2 = intent.getStringExtra("s2");
			s3 = intent.getStringExtra("s3");
			s4 = intent.getStringExtra("s4");
			s5 = intent.getStringExtra("s5");
			s6 = intent.getStringExtra("s6");
		}
		
		new Thread() {
			@Override
			public void run() {
				if(loadingType.equals("query") || loadingType.equals("login")) {
					result = callWebService(methodName, SQL);	//����webservice��ѯ
				}
				handler.sendEmptyMessage(1);
			}
		}.start();
	}

	public String callWebService(String methodName, String SQL) {
		String nameSpace = "http://tempuri.org/";		//�����ռ�
		String endPoint = "http://203.195.188.172:8091/PostService.asmx";		//EndPoint
		String soapAction = nameSpace + methodName;		//SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName);		//ָ��WebService�������ռ�͵��õķ�����
		if(SQL != null) {
			rpc.addProperty("Sqlstr", SQL);			//WebService�ӿڵĲ���
		}
		
		//soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);	//���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc;		//�ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true;		//�����Ƿ���õ���dotNet������WebService
		
		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope);		//����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		if(object != null) {
			String result = object.getProperty(0).toString();
			if(result == null) {
				result = "��ѯΪ��";		//result���Ϊ�գ���ѯΪ��
			}
			return result;
		}
		else {
			return "��ѯʧ��";	//object����Ϊ�գ���������ʧ��
		}
	}

	public String callWebService2(String methodName) {
		String nameSpace = "http://tempuri.org/";		//�����ռ�
		String endPoint = "http://203.195.188.172:8091/PostService.asmx";	//EndPoint
		String soapAction = nameSpace + methodName;		//SOAP Action
		SoapObject rpc = new SoapObject(nameSpace, methodName);		//ָ��WebService�������ռ�͵��õķ�����

		rpc.addProperty("s1", s1);
		rpc.addProperty("s2", s2);
		rpc.addProperty("s3", s3);
		rpc.addProperty("s4", s4);
		rpc.addProperty("s5", s5);
		rpc.addProperty("s6", s6);
		
		//soap Header
		Element[] header = new Element[1];
		header[0] = new Element().createElement(nameSpace, "SecurityContext");
		Element username = new Element().createElement(nameSpace, "UserId");
		username.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(nameSpace, "UserPW");
		pass.addChild(Node.TEXT, "admin");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);	//���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		envelope.headerOut = header;
		envelope.bodyOut = rpc;		//�ȼ��� envelope.setOutputSoapObject(rpc);
		envelope.dotNet = true;		//�����Ƿ���õ���dotNet������WebService
		
		HttpTransportSE transport = new HttpTransportSE(endPoint);
		transport.debug = true;
		try {
			transport.call(soapAction, envelope);		//����WebService
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ȡ���ص�����
//		System.out.println("111112222==envelope.bodyIn=="+envelope.bodyIn);
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
			return "���ʧ��";
		}
//		SoapObject object = null;
//		try {
//			object = (SoapObject) envelope.getResponse();
//		} catch (SoapFault e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if(object != null) {
			String result = object.getProperty(0).toString();
			if(result == null) {
				result = "��ѯΪ��";		//result���Ϊ�գ���ѯΪ��
			}
			return result;
		}
		else {
			return "��ѯʧ��";	//object����Ϊ�գ���������ʧ��
		}
	}
	
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		 if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			 
//		 }
//		 return false;
//	}

}
