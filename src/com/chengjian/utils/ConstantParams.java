package com.chengjian.utils;

import android.os.Environment;

public class ConstantParams {
	public static boolean isFromLoginActivity = false;
	public static boolean isFromSplashActivity = false;
	public final static int REQUEST_LOGIN_CODE = 1;
	public final static int REQUEST_QUERYBILL_CODE = 2;
	public final static String EXTRA_QUERYBILL_RESULT = "Result";
	public final static String EXTRA_INPUT_BILL_NO = "billNo";
	public final static int QUERY_LOGIN = 0;
	public final static int QUERY_VIA_MOBILE = 1;
	public final static int QUERY_VIA_BILLNO = 2;
	public final static int INSTORE = 3;
	public final static String GET_XML_PATH = "http://www.v-goo.com/public/attachment/android_client_update/androidVer.xml";
	public final static String GET_APK_PATH = "http://www.v-goo.com/public/attachment/android_client_update/CampusDelivery.apk";
	public final static String SAVE_APK_OR_XML_PATH = Environment.getExternalStorageDirectory().toString() + "/vgoo2/";
	public final static float CUR_VERIOSN = 3.2f;


}
