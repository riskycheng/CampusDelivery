package com.chengjian.vgoo2;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.chengjian.utils.ConstantParams;
import com.chengjian.utils.LoadingActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText usernameET, passwordET = null;
	private Button login = null;
	String usernameUserInput = null;
	String passwordUserInput = null;
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
		setContentView(R.layout.login);
		login = (Button) findViewById(R.id.login);
		usernameET = (EditText) findViewById(R.id.username);
		passwordET = (EditText) findViewById(R.id.password);

		TextView copyright = (TextView) findViewById(R.id.copyright);
		int i = Calendar.getInstance().get(1);
		copyright.setText("Copyright ©南京宇牧信息科技有限公司 2008-" + i
				+ " All Rights Reserved.");

		mySharedPreferences = getSharedPreferences("myPreference", MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		// editor.putString("login_time", "2014-01-01 00:00:00");
		// editor.commit();

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable()) {
			new AlertDialog.Builder(LoginActivity.this)
					.setTitle("提示")
					.setMessage("当前网络不可用，请检查您的网络设置。您也可以使用一个有效的Wi-Fi网络。")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startActivity(new Intent(
											android.provider.Settings.ACTION_SETTINGS));
								}
							}).show();
		}

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				login(); // 登录
			}
		});

		// V2.1.1 修改：登录界面，“登录”按钮与Enter键绑定，输完密码后按Enter键直接登录
		passwordET.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					login(); // 登录
					return true;
				}
				return false;
			}
		});
	}

	private void login() {
		usernameUserInput = usernameET.getText().toString();
		passwordUserInput = passwordET.getText().toString();
		if (usernameUserInput.equals("") && passwordUserInput.equals("")) {
			Toast.makeText(LoginActivity.this, "请输入用户名和密码！", Toast.LENGTH_SHORT)
					.show();
		} else if (usernameUserInput.equals("")
				&& !passwordUserInput.equals("")) {
			Toast.makeText(LoginActivity.this, "请输入用户名！", Toast.LENGTH_SHORT)
					.show();
		} else if (!usernameUserInput.equals("")
				&& passwordUserInput.equals("")) {
			Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT)
					.show();
		} else {
			Intent intent = new Intent(LoginActivity.this,
					LoadingActivity.class);
			intent.putExtra("loadingType", "login");
			intent.putExtra("methodName", "validate");
			// 查询管理员id、密码
			// intent.putExtra("SQL",
			// "select id, adm_password from 020_admin where adm_name=\"" +
			// usernameUserInput + "\"");
			// 传递 用户名 用户密码
			intent.putExtra("name", usernameUserInput);
			intent.putExtra("password", passwordUserInput);
			startActivityForResult(intent, 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != 0)
			return;
		if (resultCode == RESULT_OK) {
			String password = "";
			int adminId = 0;
			String result = data.getStringExtra("Result"); // 接收Intent传递过来的查询结果
			Log.e("LoginTest:", result);
			if (result.equals("查询失败")) {
				new AlertDialog.Builder(LoginActivity.this)
						.setTitle("登录失败")
						.setMessage("当前网络不给力，登录失败，请稍后重试。")
						.setNeutralButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										passwordET.setText("");
										usernameET.setText("");
									}
								}).show();
			} else if (result.equals("登录失败")) {
				new AlertDialog.Builder(LoginActivity.this)
						.setTitle("登录失败")
						.setMessage("用户名不存在，请重新登录。")
						.setNeutralButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										passwordET.setText("");
										usernameET.setText("");
									}
								}).show();
			} else if (result.equals("登录成功")) {
				// String jsonString = result.substring(1, result.length()-1);
				// //去掉两头的中括号
				// try {
				// JSONObject jsonObject = new JSONObject(jsonString);
				// //转换为JSONObject
				// adminId = jsonObject.getInt("id"); //管理员ID
				// password = jsonObject.getString("adm_password"); //密码
				// } catch (JSONException e1) {
				// e1.printStackTrace();
				// }
				// passwordUserInput = password; //测试用
				// String password2MD5 = string2MD5(passwordUserInput);
				// if(passwordUserInput.equals(password)) {
				// if(password2MD5.equals(password)) {
				Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
						.show();
				String loginTimeStore = mySharedPreferences.getString(
						"login_time", "2014-01-01 00:00:00");
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss"); // 获取当前时间
				String loginTime = simpleDateFormat
						.format(new java.util.Date());
				if (!loginTime.substring(0, 10).equals(
						loginTimeStore.substring(0, 10))) {
					editor.putString("login_time", loginTime); // 如果不等，则认为是当天第一次登录，保存新的登录时间，计数清0
					editor.putInt("instore_bills", 0);
					editor.putInt("serial_number", 1);
					String currentInStoreBillNo = new SimpleDateFormat(
							"yyyyMMdd").format(new java.util.Date())
							+ "-"
							+ "1";
					editor.putString("current_instore_bill_no",
							currentInStoreBillNo);
				}
				editor.putInt("admin_id", adminId); // 保存
				editor.commit();

				Intent intent = new Intent(LoginActivity.this,
						FragmentTabActivity.class);
				ConstantParams.isFromLoginActivity = true;
				startActivity(intent);
				finish();
			}  
			// }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// private String string2MD5(String string) {
	// char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	// 'a', 'b', 'c', 'd', 'e', 'f' };
	// try {
	// byte[] btInput = string.getBytes();
	// // 获得MD5摘要算法的 MessageDigest 对象
	// MessageDigest mdInst = MessageDigest.getInstance("MD5");
	// // 使用指定的字节更新摘要
	// mdInst.update(btInput);
	// // 获得密文
	// byte[] md = mdInst.digest();
	// // 把密文转换成十六进制的字符串形式
	// int j = md.length;
	// char str[] = new char[j * 2];
	// int k = 0;
	// for (int i = 0; i < j; i++) {
	// byte byte0 = md[i];
	// str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	// str[k++] = hexDigits[byte0 & 0xf];
	// }
	// return new String(str);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
}
