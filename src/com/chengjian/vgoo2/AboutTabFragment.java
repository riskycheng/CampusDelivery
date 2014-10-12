package com.chengjian.vgoo2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class AboutTabFragment extends Fragment {
	private Activity mActivity;
	private FrameLayout setting_setNum_FrameLayout, setting_help_FrameLayout,
			setting_signOut_FrameLayout;
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
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
		setting_help_FrameLayout = (FrameLayout) view
				.findViewById(R.id.setting_02_help);
		setting_help_FrameLayout.setOnClickListener(new myClickListener());
		setting_signOut_FrameLayout = (FrameLayout) view
				.findViewById(R.id.setting_03_signOut);
		setting_signOut_FrameLayout.setOnClickListener(new myClickListener());
		
		mySharedPreferences = mActivity.getSharedPreferences("myPreference",Activity.MODE_PRIVATE);
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

			case R.id.setting_02_help:
				startActivityForResult(
						new Intent(mActivity, HelpActivity.class), 4);
				break;

			case R.id.setting_03_signOut:
				new AlertDialog.Builder(mActivity)
				.setTitle("退出当前帐号")
				.setMessage("退出后将删除当前帐号信息，下次登录需要重新输入帐号。确定要退出吗？")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {						
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.cancel();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {						
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						editor.putInt("admin_id", 0);
						editor.commit();
						//V2.1.1 修改：退出当前帐号时，返回到登录界面
						startActivity(new Intent(mActivity, LoginActivity.class));
						mActivity.finish();
					}
				})
				.show();
				break;
			}
		}

	}

}
