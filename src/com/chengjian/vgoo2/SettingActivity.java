package com.chengjian.vgoo2;

import java.text.SimpleDateFormat;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {
	private Button okButton = null;
	private EditText serialNumberStart = null;
	private int serialNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      //�����ޱ���
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //����ȫ��
		setContentView(R.layout.setting_activity);
		okButton = (Button)findViewById(R.id.ok);
		serialNumberStart = (EditText)findViewById(R.id.serialNumberStart);
		
		SharedPreferences mySharedPreferences = getSharedPreferences("myPreference",MODE_PRIVATE);
		final SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String serialNumberStartStr = serialNumberStart.getText().toString().trim();
				if(!serialNumberStartStr.equals("") && isNumeric(serialNumberStartStr)) {
					try {
						final int serialNumberStartInt = Integer.parseInt(serialNumberStartStr);
					    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");   //��ȡ��ǰʱ��
					    String inStoreBillNoPre = simpleDateFormat.format(new java.util.Date());
					    String inStoreBillNoStart = "";
					    inStoreBillNoStart = inStoreBillNoPre + "-" + serialNumberStartInt;
						new AlertDialog.Builder(SettingActivity.this)
										.setTitle("����")
										.setMessage("�����ʼ��Ž�������Ϊ��\n" + inStoreBillNoStart)
										.setNegativeButton("ȡ��", null)
										.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
									    		editor.putInt("serial_number_start", serialNumberStartInt);
									    		editor.commit();
												Toast.makeText(SettingActivity.this, "���óɹ���", Toast.LENGTH_SHORT).show();
												finish();
											}
										})
										.show();
					} catch (Exception e) {  
						new AlertDialog.Builder(SettingActivity.this)
										.setTitle("�����ˣ�")
										.setMessage("�����ʼ����ѳ��������������ʾ��Χ�����������ã�")
										.setNeutralButton("�ر�", new DialogInterface.OnClickListener(){
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												serialNumberStart.setText("");
											}
										})
										.show();
			        }
				}
				else {
					new AlertDialog.Builder(SettingActivity.this)
								.setTitle("�����ˣ�")
								.setMessage("��������Ч�Ŀ����ʼ��ţ�")
								.setNeutralButton("�ر�", new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										serialNumberStart.setText("");
									}
								})
								.show();
				}
			}
		});
	}
	
	private static boolean isNumeric(String str) {
		for(int i = str.length();--i>=0;) {
			if(!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
