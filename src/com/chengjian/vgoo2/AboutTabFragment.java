package com.chengjian.vgoo2;

import com.chengjian.utils.ConstantParams;
import com.chengjian.utils.LoadingActivity;
import com.chengjian.utils.UpdateManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AboutTabFragment extends Fragment {
	private Activity mActivity;
	private FrameLayout setting_setNum_FrameLayout, setting_setSMS,
			setting_help_FrameLayout, setting_check_Update,
			setting_signOut_FrameLayout;
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	public static int adminId = 0;
	public static String local_sms_content = "";

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

		View view = inflater.inflate(R.layout.tab_about_layout, container,
				false);
		// Button btn = (Button) view.findViewById(R.id.btn_test);
		// btn.setOnClickListener(new Button.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
		// dialog.setTitle("ÌáÊ¾");
		// dialog.create().show();
		// }
		// });
		init(view);
		return view;
	}

	public void init(View view) {
		setting_setNum_FrameLayout = (FrameLayout) view
				.findViewById(R.id.setting_01_setStartNum);
		setting_setNum_FrameLayout.setOnClickListener(new myClickListener());

		setting_setSMS = (FrameLayout) view
				.findViewById(R.id.setting_02_setSMS);
		setting_setSMS.setOnClickListener(new myClickListener());

		setting_help_FrameLayout = (FrameLayout) view
				.findViewById(R.id.setting_03_help);
		setting_help_FrameLayout.setOnClickListener(new myClickListener());

		setting_check_Update = (FrameLayout) view
				.findViewById(R.id.setting_04_update);
		setting_check_Update.setOnClickListener(new myClickListener());

		setting_signOut_FrameLayout = (FrameLayout) view
				.findViewById(R.id.setting_05_signOut);
		setting_signOut_FrameLayout.setOnClickListener(new myClickListener());

		mySharedPreferences = mActivity.getSharedPreferences("myPreference",
				Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
	}

	public class myClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.setting_01_setStartNum:
				startActivityForResult(new Intent(mActivity,
						SettingActivity.class), 3);
				break;
			case R.id.setting_02_setSMS:
				// Toast.makeText(mActivity, "功能升级中...", Toast.LENGTH_SHORT)
				// .show();
				// 设置短信模板功能
				// 获取当前用户id
				adminId = mySharedPreferences.getInt("admin_id", 0);
				LayoutInflater inflater = (LayoutInflater) mActivity
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				final View view = inflater.inflate(R.layout.sms_layout, null);
				final EditText smsContentEditext = (EditText) view
						.findViewById(R.id.Editetext_sms_content);
				// 如果当前有值，加载
				local_sms_content = mySharedPreferences.getString("local_sms",
						"");
				smsContentEditext.setText(local_sms_content);
				smsContentEditext.setSelection(local_sms_content.length());
				
				new AlertDialog.Builder(mActivity)
						.setTitle("添加短信模板：")
						.setView(view)
						.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										String smsContent = smsContentEditext
												.getText().toString().trim();
										if (!smsContent.equals("")) {
											// 将结果写入本地
											editor.putString("local_sms",
													smsContent);
											editor.commit();
											// 调用WebService方法 写入结果
											Intent intent = new Intent(
													mActivity,
													LoadingActivity.class);
											intent.putExtra("loadingType",
													"add_sms");
											intent.putExtra("methodName",
													"setsms");
											intent.putExtra("smsContent",
													smsContent);
											intent.putExtra("account", adminId);
											startActivityForResult(intent, 2);

										} else {
											Toast.makeText(mActivity,
													"输入为空，添加短信模板失败!",
													Toast.LENGTH_SHORT).show();
											Log.e("add sms:", "failed!");
										}
									}
								}).show();

				break;
			case R.id.setting_03_help:
				startActivityForResult(
						new Intent(mActivity, HelpActivity.class), 4);
				break;

			case R.id.setting_04_update:

				// Toast.makeText(mActivity, "检查更新功能升级中...", Toast.LENGTH_SHORT)
				// .show();

				Intent intent = new Intent(mActivity, LoadingActivity.class);
				intent.putExtra("loadingType", "update");
				startActivityForResult(intent, 3);

				break;
			case R.id.setting_05_signOut:
				new AlertDialog.Builder(mActivity)
						.setTitle("退出当前帐号")
						.setMessage("退出后将删除当前帐号信息，下次登录需要重新输入帐号。确定要退出吗？")
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.cancel();
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										editor.putInt("admin_id", 0);
										editor.commit();
										// V2.1.1 修改：退出当前帐号时，返回到登录界面
										startActivity(new Intent(mActivity,
												LoginActivity.class));
										mActivity.finish();
									}
								}).show();
				break;
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 如果请求类型是 增加 短信模板
		if (requestCode == 2) {
			if (resultCode == Activity.RESULT_OK) {
				String httpResult = data
						.getStringExtra(ConstantParams.EXTRA_QUERYBILL_RESULT);

				Log.e("httpResult:", httpResult);
				if (httpResult.equals("0")) {
					Toast.makeText(mActivity, "增加短信模板失败!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				// 增加成功
				Toast.makeText(mActivity, "增加短信模板成功!", Toast.LENGTH_SHORT)
						.show();
				Log.e("set_sms:", "success!");

			}
			// 如果请求类型是 更新版本
		} else if (requestCode == 3) {
			if (resultCode == Activity.RESULT_OK) {
				String httpResult = data
						.getStringExtra(ConstantParams.EXTRA_QUERYBILL_RESULT);
				Log.e("httpResult:", httpResult);
				// 判断版本号
				float serverVersion = Float.parseFloat(httpResult);
				UpdateManager updateManager = new UpdateManager(mActivity);
				updateManager
						.checkUpdateInfo(serverVersion > ConstantParams.CUR_VERIOSN);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
