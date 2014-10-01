package com.chengjian.vgoo2;

import java.util.Calendar;

import com.chengjian.utils.ConstantParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private static final int SHOW_TIME_MIN = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标�?
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
		setContentView(R.layout.splash_activity);

		TextView copyright = (TextView) findViewById(R.id.copyright);
		int i = Calendar.getInstance().get(1);
		copyright.setText("Copyright ©南京宇牧信息科技有限公司 2008-" + i
				+ " All Rights Reserved.");

		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {
				int result;
				long startTime = System.currentTimeMillis();
				result = prepareLoading();
				long loadingTime = System.currentTimeMillis() - startTime;
				if (loadingTime < SHOW_TIME_MIN) {
					try {
						Thread.sleep(SHOW_TIME_MIN - loadingTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return result;
			}

			@Override
			protected void onPostExecute(Integer result) {
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"myPreference", 0);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				int adminId = mySharedPreferences.getInt("admin_id", 0); // 根据管理员ID来区分用�?
				boolean isFirstTimeLaunch = mySharedPreferences.getBoolean(
						"first_time_launch", true);
				if (isFirstTimeLaunch) {
					editor.putBoolean("first_time_launch", false);
					editor.commit();
					startActivity(new Intent(SplashActivity.this,
							WelcomeActivity.class));
				} else if (adminId == 0) {
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
				} else {
					ConstantParams.isFromSplashActivity = true;
					startActivity(new Intent(SplashActivity.this,
							FragmentTabActivity.class));
				}
				finish();
				// 两个参数分别表示进入的动�?�?��的动�?
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			};
		}.execute(new Void[] {});
	}

	private int prepareLoading() {

		// 监测版本更新的功能，待开发�?。�?

		return 0;
	}
}
