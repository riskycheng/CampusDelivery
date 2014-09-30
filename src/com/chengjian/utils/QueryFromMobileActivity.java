package com.chengjian.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.chengjian.vgoo2.R;

public class QueryFromMobileActivity extends Activity{

	private TextView textView = null;
	String httpResult = "";
	BufferedReader bufferedReader = null;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case 2:		//处理查询操作结果
					Intent intent = QueryFromMobileActivity.this.getIntent();
					intent.putExtra("Result", httpResult);
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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//设置竖屏
		setContentView(R.layout.loading);
		textView = (TextView)findViewById(R.id.textView);
		
		Intent intent = this.getIntent();
		final String mailNoStr = intent.getStringExtra("mailNo");
		textView.setText("快件查询...");

		new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL("http://www.v-goo.com/syn_fwz.php?act=get_bill&mobile=\"" + mailNoStr + "\"&fwz_id=1");
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestProperty("accept", "*/*");
					conn.setRequestProperty("connection", "Kepp-Alive");
					conn.setRequestProperty("user-agent", "Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1)");
					conn.connect();
						
					bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
					String line = "";
					while((line = bufferedReader.readLine()) != null) {
						httpResult += line;
					}
					handler.sendEmptyMessage(ConstantParams.REQUEST_QUERYBILL_CODE);
				} catch (Exception e) {
					Log.e("connection:", "failed");
				}
				finally {
					try {
						bufferedReader.close();
						Log.e("httpResult:", httpResult);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
