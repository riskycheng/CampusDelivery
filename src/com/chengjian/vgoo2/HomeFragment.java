package com.chengjian.vgoo2;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zhang.fan.vgoo2.idata.CornerListView;
import zhang.fan.vgoo2.idata.HadwareControll;
import zhang.fan.vgoo2.idata.LoadingActivity;
import zhang.fan.vgoo2.idata.LoadingIIActivity;

import com.chengjian.utils.ConstantParams;
import com.chengjian.vgoo2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/***
 * @author huangsm
 * @date 2013-1-4
 * @email huangsanm@gmail.com
 * @desc ������Ϣ�б�
 */
public class HomeFragment extends Fragment {
	private CornerListView mListView = null;
	private EditText mailNo, receiverName, receiverMobilephone = null;
	private Spinner companySpinner = null;
	private Button manualInput, replace, inStore = null;
	private String mailNoStr = null;
	private Activity mActivity;
	int adminId = 0;		//����ԱID
//	int serialNumber = 0;	//�����ţ����ͣ�
	String login_time = "";	//�����¼ʱ��
	int instoreBills = 0;	//��������
//	String inStoreBillNo = "";	//��������
	
	public Handler mHandler = new MainHandler();
	protected MediaPlayer mediaPlayer = null;
	boolean press = true;
	
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	
	ArrayList<HashMap<String, String>> arrayList = null;
	HashMap<String, String> map1, map2, map3, map4 = null;
	private SimpleAdapter adapter = null;
	String[] companyStr = null;
	ArrayAdapter<String> companyAdapter = null;
	int length = 0;
	int companyId = -1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);      //�����ޱ���
//		getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //����ȫ��
		HadwareControll.Open();		//���豸
		HadwareControll.m_handler = mHandler;
		mActivity = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("MessageFragment :: onCreateView");
		View view = inflater.inflate(R.layout.instore_activity, container,
				false);
		init(view);
		return view;
	}
	
	
	public void init(View view){
		
		mailNo = (EditText)view.findViewById(R.id.mail_no);
		companySpinner = (Spinner)view.findViewById(R.id.company_spinner);
		receiverName = (EditText)view.findViewById(R.id.receiver_name);
		receiverMobilephone = (EditText)view.findViewById(R.id.receiver_mobilephone);
		manualInput = (Button)view.findViewById(R.id.manual_input);
		replace = (Button)view.findViewById(R.id.replace);
		inStore = (Button)view.findViewById(R.id.in_store);
		
		mySharedPreferences = mActivity.getSharedPreferences("myPreference",Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		
		adminId = mySharedPreferences.getInt("admin_id",0);
		login_time = mySharedPreferences.getString("login_time","");
		instoreBills = mySharedPreferences.getInt("instore_bills",0);
		String prevInStoreBillNo = mySharedPreferences.getString("prev_instore_bill_no","");
		String currentInStoreBillNo = mySharedPreferences.getString("current_instore_bill_no","");

		arrayList = new ArrayList<HashMap<String, String>>();
		map1 = new HashMap<String, String>();
		map2 = new HashMap<String, String>();
		map3 = new HashMap<String, String>();
		map4 = new HashMap<String, String>();
		
		map1.put("item", "��¼ʱ��");
		map1.put("item_content", login_time);
		map2.put("item", "�����");
		map2.put("item_content", instoreBills + " ��");
		map3.put("item", "��һ�������");
		map3.put("item_content", prevInStoreBillNo);
		map4.put("item", "��ǰ�����");
		map4.put("item_content", currentInStoreBillNo);

		arrayList.add(map1);
		arrayList.add(map2);
		arrayList.add(map3);
		arrayList.add(map4);
		
		adapter = new SimpleAdapter(mActivity, arrayList,
				R.layout.simple_list_item_1, new String[] {"item", "item_content"},
				new int[] { R.id.item_title, R.id.item_content });
		mListView = (CornerListView)view.findViewById(R.id.list1);
		mListView.setAdapter(adapter);
	/*	
		if(ConstantParams.isFromLoginActivity || ConstantParams.isFromSplashActivity) {	//�ӵ�¼���棬���ߴ�SplashActivity��ת������
			ConstantParams.isFromLoginActivity = false;
			ConstantParams.isFromSplashActivity = false;
			Intent intent2 = new Intent(mActivity, LoadingActivity.class);
			intent2.putExtra("loadingType", "refresh_spinner");
			intent2.putExtra("methodName", "ExecuteJson");
			//���ݹ���ԱID��ȡ������˾�б�
			intent2.putExtra("SQL", "SELECT name, fwz_delivery_id FROM 020_delivery a, 020_admin b WHERE a.fwz_id=b.fwz_id and b.id=" + adminId);
			startActivityForResult(intent2, 5);
		}
	*/	
		companyStr = getResources().getStringArray(R.array.deliveryCompanies);
		companyAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, companyStr);
    	companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	companySpinner.setAdapter(companyAdapter);
    	companySpinner.setSelection(0);
		
		companySpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String companyManuSelect = companyAdapter.getItem(arg2).trim();
				if(companyManuSelect.equals("δ֪")) {
					companyId = -1;
				}
				else if(companyManuSelect.equals("У԰�ȿ��")) {
					companyId = 0;
				}
				else if(companyManuSelect.equals("EMS")) {
					companyId = 2;
				}
				else if(companyManuSelect.equals("��ͨ���")) {
					companyId = 100;
				}
				else if(companyManuSelect.equals("Բͨ���")) {
					companyId = 101;
				}
				else if(companyManuSelect.equals("�ϴ����")) {
					companyId = 102;
				}
				else if(companyManuSelect.equals("լ����")) {
					companyId = 103;
				}
				else if(companyManuSelect.equals("������")) {
					companyId = 106;
				}
				else if(companyManuSelect.equals("��ͨ�ٵ�")) {
					companyId = 500;
				}
				else if(companyManuSelect.equals("������")) {
					companyId = 504;
				}
				else if(companyManuSelect.equals("˳����")) {
					companyId = 505;
				}
	            if(companyId != -1) {
					editor.putInt("prev_company_id", companyId);
					editor.commit();
	            }
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		//V2.1�������ܣ�����enter���İ󶨣��ֶ��������ֻ��ź󣬰�Enter��ֱ�����
		receiverMobilephone.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {
					billInStore();	//������
					return true;
				}
				return false;
			}
		});
		
		manualInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);   
			    final View view = inflater.inflate(R.layout.edittext, null);
				new AlertDialog.Builder(mActivity)
								.setTitle("�ֶ������˵��ţ�")
								.setView(view)
								.setNegativeButton("ȡ��", null)
								.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog, int which) {
										EditText mailNoEditText = (EditText) view.findViewById(R.id.mail_no_edittext);
										mailNoStr = mailNoEditText.getText().toString().trim();
										if(!mailNoStr.equals("")) {
											if(isLetterOrDigit(mailNoStr)) {
												mailNo.setText(mailNoStr);
												//�ֶ��������ֱ�Ӳ�ѯ���
												Intent intent = new Intent(mActivity, LoadingIIActivity.class);
												intent.putExtra("mailNo", mailNoStr);
												startActivityForResult(intent, 2);
											}
											else {
												Toast.makeText(mActivity,"�˵���ֻ�ܰ�����ĸ�����֣����������룡",Toast.LENGTH_SHORT).show();
											}
										}
										else {
											mailNo.setText(mailNoStr);
										}
									}
								})
								.show();
			}
		});
		
		replace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mailNo.setText("");
				companySpinner.setSelection(length);
				companyId = -1;
				receiverName.setText("");
				receiverMobilephone.setText("");
			}
		});
		
		inStore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				billInStore();	//������
			}
		});
	}
		
	
	

private void billInStore() {
	String receiverNameStr = receiverName.getText().toString().trim();
	String receiverMobilephoneStr = receiverMobilephone.getText().toString().trim();
	if(mailNo.equals("") || receiverNameStr.equals("") || receiverMobilephoneStr.equals("")) {
		Toast.makeText(mActivity, "����д�������˵���Ϣ��", Toast.LENGTH_SHORT).show();
	}
	else if(companyId == -1) {
		Toast.makeText(mActivity, "��Ч������˾�����ֶ�ѡ��", Toast.LENGTH_SHORT).show();
	}
	else if(!receiverMobilephoneStr.substring(0, 1).equals("1") || receiverMobilephoneStr.length() != 11) {
		Toast.makeText(mActivity, "��������ȷ���ֻ����룡", Toast.LENGTH_SHORT).show();
	}
	else {					
		//��ȡ��ǰ�����
		String currentInStoreBillNo = mySharedPreferences.getString("current_instore_bill_no","");
		
		Intent intent = new Intent(mActivity, LoadingActivity.class);
		intent.putExtra("loadingType", "instore");
		intent.putExtra("methodName", "savebill");
		intent.putExtra("s1", mailNoStr);
		intent.putExtra("s2", receiverNameStr);
		intent.putExtra("s3", companyId + "");
		intent.putExtra("s4", receiverMobilephoneStr);
		intent.putExtra("s5", adminId + "");
		intent.putExtra("s6", currentInStoreBillNo);
		startActivityForResult(intent, 1);
	}
}


@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if(requestCode == 1) {	//������
		if(resultCode == Activity.RESULT_OK) {
			final String result = data.getStringExtra("Result");	//����Intent���ݹ����Ĳ�ѯ���
			if(result.equals("��ѯʧ��") || result.equals("��ѯΪ��")) {
				new AlertDialog.Builder(mActivity)
								.setTitle("������ʧ��")
								.setMessage("��ǰ���粻������������ʧ�ܣ����Ժ����ԡ�")
								.setNeutralButton("ȷ��", null)
								.show();
			}
			else if(result.equals("���ʧ��")) {
				new AlertDialog.Builder(mActivity)
								.setTitle("���ʧ��")
								.setMessage("���ʧ�ܣ���ǰ�����Ѿ���⣬��˶Ե��ź����ԡ�")
								.setNeutralButton("ȷ��", null)
								.show();
			}
			else {
				Toast.makeText(mActivity, mailNoStr + " ���ɹ���", Toast.LENGTH_SHORT).show();
				new Thread() {
					@Override
					public void run() {
						try {
							URL url = new URL("http://www.v-goo.com/syn_fwz.php?act=syn_send&bill_id=" + result);
							HttpURLConnection conn = (HttpURLConnection)url.openConnection();
							conn.setRequestProperty("accept", "*/*");
							conn.setRequestProperty("connection", "Kepp-Alive");
							conn.setRequestProperty("user-agent", "Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1)");
							conn.connect();
//							BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//							String line = "";
//							String httpResult = "";
//							while((line = bufferedReader.readLine()) != null) {
//								httpResult += line;
//							}
//							handler.sendEmptyMessage(2);
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(mActivity, "HTTP�����쳣��", Toast.LENGTH_SHORT).show();
						}
//						finally {
//							try {
//								bufferedReader.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
					}
				}.start();
				
				String prevInstoreBillNo = mySharedPreferences.getString("current_instore_bill_no","");
				int serialNumber = mySharedPreferences.getInt("serial_number", 1);
				
				arrayList.clear();
				map1.put("item", "��¼ʱ��");
				map1.put("item_content", login_time);
				instoreBills++;
				map2.put("item", "�����");
				map2.put("item_content", instoreBills + " ��");
				map3.put("item", "��һ�������");
				map3.put("item_content", prevInstoreBillNo);
				
				serialNumber++;
				String currentInStoreBillNo = "";
				currentInStoreBillNo = prevInstoreBillNo.substring(0, 9) + serialNumber;
				map4.put("item", "��ǰ�����");
				map4.put("item_content", currentInStoreBillNo);

				arrayList.add(map1);
				arrayList.add(map2);
				arrayList.add(map3);
				arrayList.add(map4);
				adapter.notifyDataSetChanged();
				
				editor.putInt("serial_number", serialNumber);	//����
				editor.putInt("instore_bills", instoreBills);
				editor.putString("prev_instore_bill_no", prevInstoreBillNo);
				editor.putString("current_instore_bill_no", currentInStoreBillNo);
				editor.commit();
				
				mailNo.setText("");
				companySpinner.setSelection(length);
				companyId = -1;
				receiverName.setText("");
				receiverMobilephone.setText("");
			}
		}
	}
	else if(requestCode == 2) {	//�����ѯ
		if(resultCode == Activity.RESULT_OK) {
			mailNo.setText(mailNoStr);
			String httpResult = data.getStringExtra("Result");	//����Intent���ݹ����Ĳ�ѯ���
		    try {
		        JSONObject result = new JSONObject(httpResult);		//ת��ΪJSONObject
		        companyId = result.getInt("companyId");
		        String receiverNameStr = result.getString("recName");
		        String receiverMobilephoneStr = result.getString("mobile");
		            
		        if(companyId == -1) {	//��ȡ��һ�εĿ�ݹ�˾
		          	companyId = mySharedPreferences.getInt("prev_company_id", -1);
		        }
		        else {
					editor.putInt("prev_company_id", companyId);
					editor.commit();
		        }
		        receiverName.setText(receiverNameStr);
		        receiverMobilephone.setText(receiverMobilephoneStr);
		        Toast.makeText(mActivity, "�����ѯ�ɹ�", Toast.LENGTH_SHORT).show();
		    } catch (JSONException e) {	//�����ѯ�쳣
		        Toast.makeText(mActivity, "���������", Toast.LENGTH_SHORT).show();
				companyId = mySharedPreferences.getInt("prev_company_id", -1);	//��ȡ��һ�εĿ�ݹ�˾
		        receiverName.setText("0");
		        receiverMobilephone.setText("");
		    }
		    
			String companyStr2 = "";
            switch(companyId) {
            	case -1: companyStr2 = "δ֪"; break;
            	case 0: companyStr2 = "У԰�ȿ��"; break;
            	case 2: companyStr2 = "EMS"; break;
            	case 100: companyStr2 = "��ͨ���"; break;
            	case 101: companyStr2 = "Բͨ���"; break;
            	case 102: companyStr2 = "�ϴ����"; break;
            	case 103: companyStr2 = "լ����"; break;
            	case 106: companyStr2 = "������"; break;
            	case 500: companyStr2 = "��ͨ�ٵ�"; break;
            	case 504: companyStr2 = "������"; break;
            	case 505: companyStr2 = "˳����"; break;
            	default: break;
            }
            for(int i=0; i<length; i++) {
            	if(companyStr[i].equals(companyStr2)) {
            		companySpinner.setSelection(i);
            	}
            }
            receiverMobilephone.requestFocus();		//��ȡ����
            receiverMobilephone.setSelection(receiverMobilephone.getText().toString().length());	//���ù��λ��
		}
	}
	else if(requestCode == 3) {	//������ʼ���
		int serialNumber = mySharedPreferences.getInt("serial_number_start", 0);
		arrayList.clear();
		map1.put("item", "��¼ʱ��");
		map1.put("item_content", login_time);
		map2.put("item", "�����");
		map2.put("item_content", instoreBills + " ��");
		String prevInstoreBillNo = mySharedPreferences.getString("prev_instore_bill_no","");
		map3.put("item", "��һ�������");
		map3.put("item_content", prevInstoreBillNo);
		String currentInStoreBillNo = "";
		//��������ţ�20140601-001���ų��ԡ�xyg����ͷ�ı��
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");   //��ȡ��ǰʱ��
        String datePre = simpleDateFormat.format(new java.util.Date());
        currentInStoreBillNo = datePre + "-" + serialNumber;
		map4.put("item", "��ǰ�����");
		map4.put("item_content", currentInStoreBillNo);

		arrayList.add(map1);
		arrayList.add(map2);
		arrayList.add(map3);
		arrayList.add(map4);
		adapter.notifyDataSetChanged();
		
		editor.putInt("serial_number", serialNumber);	//����
		editor.putString("current_instore_bill_no", currentInStoreBillNo);
		editor.commit();
	}
	else if(requestCode == 5) {	//��ѯ������˾�б�
		if(resultCode == Activity.RESULT_OK) {
			String result = data.getStringExtra("Result");	//����Intent���ݹ����Ĳ�ѯ���
			if(result.equals("��ѯʧ��") || result.equals("��ѯΪ��")) {
				new AlertDialog.Builder(mActivity)
								.setTitle("ʧ��")
								.setMessage("��ǰ���粻��������ѯ������˾�б�ʧ�ܣ����Ժ����ԡ�")
								.setNeutralButton("ȷ��", null)
								.show();
			}
			else {
				Toast.makeText(mActivity, "��ѯ������˾�б�ɹ���", Toast.LENGTH_SHORT).show();
				try {
		            JSONArray jsonArray = new JSONArray(result);
		            length = jsonArray.length();
		            if(length > 0) {
		            	companyStr = new String[length+1];
		            	for(int i=0; i<length; i++) {
			            	JSONObject jsonObj = (JSONObject) jsonArray.opt(i);
			            	companyStr[i] = jsonObj.getString("name");
		            	}
		            	companyStr[length] = "δ֪";
		            	companyAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, companyStr);
		            	companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		            	companySpinner.setAdapter(companyAdapter);
		            	companySpinner.setSelection(length);
		            }
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	super.onActivityResult(requestCode, resultCode, data);
}

private boolean isLetterOrDigit(String str) {
	int j = 0;
	for(int i=0; i<str.length(); i++) {
		if((str.charAt(i) >= '0' && str.charAt(i) <= '9')
		|| (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
		|| (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')) {
			j++;
		}
		if(j == str.length()) {
			return true;
		}
	}
	return false;
}

private class MainHandler extends Handler {
	public void handleMessage(Message msg) {
		switch(msg.what) {
		case HadwareControll.BARCODE_READ:
			String scanResult0 = msg.obj + "";		//������ɨ����
			//V2.1�������ܣ�ɨ�����ʱ��ǰ��ġ�*���Ź��˵�
			String scanResult = "";
			if(scanResult0.charAt(0) == '*') {	//�������ĸΪ��*���ţ���ȥ��ǰ��ġ�*����
				scanResult = scanResult0.substring(1, scanResult0.length()-1);
			}
			else {
				scanResult = scanResult0;
			}
			mailNo.setText(scanResult.replaceAll(" ", ""));	//ȥ�����пո�
			play_sound();
			//ɨ�����ֱ�Ӳ�ѯ���
			mailNoStr = mailNo.getText().toString().trim();
			if(!mailNoStr.equals("") && isLetterOrDigit(mailNoStr)) {
				Intent intent = new Intent(mActivity, LoadingIIActivity.class);
				intent.putExtra("mailNo", mailNoStr);
				startActivityForResult(intent, 2);
			}
			else {
				Toast.makeText(mActivity,"�˵�������������ɨ�裡",Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
};

public boolean onKeyDown(int keyCode, KeyEvent event) {
	if(keyCode == KeyEvent.KEYCODE_F9 || keyCode == KeyEvent.KEYCODE_F10 || keyCode == KeyEvent.KEYCODE_F11) {
		//V2.1.1 �޸ģ���ɫ�������ΰ���ʱ�����ֱ����⣨�����ٵ����Ļ�ϵġ������⡱��ť��,
		// �ж����ݣ������˵��š���Ϊ�գ��������⺯��
		if(!mailNo.getText().toString().equals("")) {
			billInStore();	//������
		}
		else {
			if(press) {
				HadwareControll.scan_start();
				press = false;
				return true;
			}
		}
	}
	if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){
		new AlertDialog.Builder(mActivity)
						.setTitle("�˳�")
						.setMessage("ȷ��Ҫ�˳�У԰����")
						.setNegativeButton("ȡ��", null)
						.setPositiveButton("�˳�", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mActivity.finish();
							}
						})
						.show();
	}
	return true;
}

public boolean onKeyUp(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_F9 || keyCode == KeyEvent.KEYCODE_F10 || keyCode == KeyEvent.KEYCODE_F11) {
		HadwareControll.scan_stop();
		press = true;
		return true;
	}
	return true;
}

public void play_sound() {
	try {
		if (mediaPlayer == null)
			mediaPlayer = MediaPlayer.create(mActivity, R.raw.beep);
		mediaPlayer.stop();
		mediaPlayer.prepare();
		mediaPlayer.start();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void onDestroy() {
	HadwareControll.Close();
	HadwareControll.m_handler = null;
	super.onDestroy();
}
	
}
