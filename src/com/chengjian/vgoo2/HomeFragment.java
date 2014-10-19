package com.chengjian.vgoo2;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zhang.fan.vgoo2.idata.CornerListView;
import zhang.fan.vgoo2.idata.HadwareControll;
import zhang.fan.vgoo2.idata.LoadingActivity;
import zhang.fan.vgoo2.idata.LoadingIIActivity;

import com.chengjian.entity.SavedBill;
import com.chengjian.utils.ParsingTool;
import com.chengjian.utils.SQLiteHelper;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class HomeFragment extends Fragment {
	private CornerListView mListView = null;
	private EditText mailNo, receiverName, receiverMobilephone = null;
	private Spinner companySpinner = null;
	private Button manualInput, replace, inStore, inStoreAdd = null;
	private String mailNoStr = "";
	private Activity mActivity;
	int adminId = 0; // 管理员ID
	// int serialNumber = 0; //快件编号（整型）
	String login_time = ""; // 当天登录时间
	int instoreBills = 0; // 已入库计数
	// String inStoreBillNo = ""; //快件入库编号

	public Handler mHandler = new MainHandler();
	protected MediaPlayer mediaPlayer = null;
	boolean press = true;

	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;

	// 定义数据库读写对象
	SQLiteHelper mysqHelper;
	private final static int OFFSET = 100;
	ArrayList<HashMap<String, String>> arrayList = null;
	HashMap<String, String> map1, map2, map3, map4 = null;
	private SimpleAdapter adapter = null;
	String[] companyStr = null;
	ArrayAdapter<String> companyAdapter = null;
	int length = 11;
	int companyId = -1;
	// 用于保存已经添加到本地的数据结构
	public static ArrayList<SavedBill> savedBillsList = null;
	// 用于指示当前总记录数目 和当前位置
	public static int allBillNum = 0;
	public static int curBillNo = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
		// getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
		HadwareControll.Open(); // 打开设备
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

	public void init(View view) {
		mySharedPreferences = mActivity.getSharedPreferences("myPreference",
				Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		// 实例化SQliteHelper对象
		mysqHelper = new SQLiteHelper(mActivity);
		savedBillsList = new ArrayList<SavedBill>();
		mailNo = (EditText) view.findViewById(R.id.mail_no);
		companySpinner = (Spinner) view.findViewById(R.id.company_spinner);
		receiverName = (EditText) view.findViewById(R.id.receiver_name);
		receiverMobilephone = (EditText) view
				.findViewById(R.id.receiver_mobilephone);
		manualInput = (Button) view.findViewById(R.id.manual_input);
		replace = (Button) view.findViewById(R.id.replace);
		inStore = (Button) view.findViewById(R.id.in_store);

		inStoreAdd = (Button) view.findViewById(R.id.in_store_add);
		// 加入到本地
		inStoreAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取
				String s1 = mailNoStr;
				String s2 = receiverName.getText().toString().trim();
				String s3 = String.valueOf(companyId);
				String s4 = receiverMobilephone.getText().toString().trim();
				String s5 = String.valueOf(adminId);
				String s6 = mySharedPreferences.getString(
						"current_instore_bill_no", "");

				if (s1.equals("") || s2.equals("") || s4.equals("")) {
					Toast.makeText(mActivity, "请填写完整的运单信息！", Toast.LENGTH_SHORT)
							.show();
				} else if (companyId == -1) {
					Toast.makeText(mActivity, "无效物流公司，请手动选择！",
							Toast.LENGTH_SHORT).show();
				} else if (!s4.substring(0, 1).equals("1") || s4.length() != 11) {
					Toast.makeText(mActivity, "请输入正确的手机号码！", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 封装为SavedBill
					SavedBill savebill = ParsingTool.Parsing2SavedBill(s1, s2,
							s3, s4, s5, s6);
					// 加入向量
					// savedBillsList.add(savebill);
					// 加入到数据库
					long tempRes = mysqHelper.insertBillData(savebill);
					if (tempRes == -1) {
						Toast.makeText(mActivity, "保存失败：重复插入！",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(mActivity, "保存成功！", Toast.LENGTH_SHORT)
								.show();
					}
				}
				// Log.e("listsLength:", savedBillsList.size() + "");
			}
		});

		adminId = mySharedPreferences.getInt("admin_id", 0);
		login_time = mySharedPreferences.getString("login_time", "");
		instoreBills = mySharedPreferences.getInt("instore_bills", 0);
		String prevInStoreBillNo = mySharedPreferences.getString(
				"prev_instore_bill_no", "");
		String currentInStoreBillNo = mySharedPreferences.getString(
				"current_instore_bill_no", "");

		arrayList = new ArrayList<HashMap<String, String>>();
//		map1 = new HashMap<String, String>();
		map2 = new HashMap<String, String>();
		map3 = new HashMap<String, String>();
		map4 = new HashMap<String, String>();

//		map1.put("item", "登录时间");
//		map1.put("item_content", login_time);
		map2.put("item", "已入库");
		map2.put("item_content", instoreBills + " 件");
		map3.put("item", "上一次入库编号");
		map3.put("item_content", prevInStoreBillNo);
		map4.put("item", "当前入库编号");
		map4.put("item_content", currentInStoreBillNo);

//		arrayList.add(map1);
		arrayList.add(map2);
		arrayList.add(map3);
		arrayList.add(map4);

		adapter = new SimpleAdapter(mActivity, arrayList,
				R.layout.simple_list_item_1, new String[] { "item",
						"item_content" }, new int[] { R.id.item_title,
						R.id.item_content });
		mListView = (CornerListView) view.findViewById(R.id.list1);
		mListView.setAdapter(adapter);
		/*
		 * if(ConstantParams.isFromLoginActivity ||
		 * ConstantParams.isFromSplashActivity) { //从登录界面，或者从SplashActivity跳转过来的
		 * ConstantParams.isFromLoginActivity = false;
		 * ConstantParams.isFromSplashActivity = false; Intent intent2 = new
		 * Intent(mActivity, LoadingActivity.class);
		 * intent2.putExtra("loadingType", "refresh_spinner");
		 * intent2.putExtra("methodName", "ExecuteJson"); //根据管理员ID获取物流公司列表
		 * intent2.putExtra("SQL",
		 * "SELECT name, fwz_delivery_id FROM 020_delivery a, 020_admin b WHERE a.fwz_id=b.fwz_id and b.id="
		 * + adminId); startActivityForResult(intent2, 5); }
		 */
		companyStr = getResources().getStringArray(R.array.deliveryCompanies);
		companyAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_spinner_item, companyStr);
		companyAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		companySpinner.setAdapter(companyAdapter);
		companySpinner.setSelection(0);

		companySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String companyManuSelect = companyAdapter.getItem(arg2).trim();
				if (companyManuSelect.equals("未知")) {
					companyId = -1;
				} else if (companyManuSelect.equals("校园谷快递")) {
					companyId = 0;
				} else if (companyManuSelect.equals("申通快递")) {
					companyId = 100;
				} else if (companyManuSelect.equals("圆通速递")) {
					companyId = 101;
				} else if (companyManuSelect.equals("中通快递")) {
					companyId = 500;
				} else if (companyManuSelect.equals("韵达快递")) {
					companyId = 102;
				} else if (companyManuSelect.equals("百世汇通")) {
					companyId = 502;
				} else if (companyManuSelect.equals("天天快递")) {
					companyId = 504;
				} else if (companyManuSelect.equals("顺丰速运")) {
					companyId = 505;
				} else if (companyManuSelect.equals("中国邮政")) {
					companyId = 1;
				} else if (companyManuSelect.equals("全峰快递")) {
					companyId = 1216;
				} else if (companyManuSelect.equals("优速快递")) {
					companyId = 1207;
				} else if (companyManuSelect.equals("宅急送")) {
					companyId = 103;
				} else if (companyManuSelect.equals("联邦快递")) {
					companyId = 106;
				} else if (companyManuSelect.equals("华强物流")) {
					companyId = 108;
				} else if (companyManuSelect.equals("中铁快运")) {
					companyId = 1016;
				} else if (companyManuSelect.equals("国通快递")) {
					companyId = 200143;
				} else if (companyManuSelect.equals("德邦物流")) {
					companyId = 107;
				} else if (companyManuSelect.equals("EMS")) {
					companyId = 2;
				}
				if (companyId != -1) {
					editor.putInt("prev_company_id", companyId);
					editor.commit();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// V2.1新增功能：入库和enter键的绑定，手动输入完手机号后，按Enter键直接入库
		receiverMobilephone.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					billInStore(); // 快件入库
					return true;
				}
				return false;
			}
		});

		manualInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LayoutInflater inflater = (LayoutInflater) mActivity
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				final View view = inflater.inflate(R.layout.edittext, null);
				new AlertDialog.Builder(mActivity)
						.setTitle("手动输入运单号：")
						.setView(view)
						.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										EditText mailNoEditText = (EditText) view
												.findViewById(R.id.mail_no_edittext);
										mailNoStr = mailNoEditText.getText()
												.toString().trim();
										if (!mailNoStr.equals("")) {
											if (isLetterOrDigit(mailNoStr)) {
												mailNo.setText(mailNoStr);
												// 手动输入完毕直接查询快件
												Intent intent = new Intent(
														mActivity,
														LoadingIIActivity.class);
												intent.putExtra("mailNo",
														mailNoStr);
												startActivityForResult(intent,
														2);
											} else {
												Toast.makeText(mActivity,
														"运单号只能包含字母或数字，请重新输入！",
														Toast.LENGTH_SHORT)
														.show();
											}
										} else {
											mailNo.setText(mailNoStr);
										}
									}
								}).show();
			}
		});

		replace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mailNo.setText("");
				companySpinner.setSelection(0);
				companyId = -1;
				receiverName.setText("");
				receiverMobilephone.setText("");
			}
		});

		inStore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				savedBillsList = mysqHelper.queryAllBills();
				allBillNum = savedBillsList.size();
				curBillNo = 0;
				myBillInStore(); // 快件入库
			}
		});

		// 长按添加到本地按钮事件：显示当前本地数据库中所有bill
		inStoreAdd.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				savedBillsList = mysqHelper.queryAllBills();

				// 将当前数据库内的所有订单进行显示
				LayoutInflater inflater = (LayoutInflater) mActivity
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				final View view = inflater.inflate(
						R.layout.all_bills_checkout_layout, null);
				ListView allBillsListview = (ListView) view
						.findViewById(R.id.Listview_allBillsCheckout);

				// 将结果集转化为List<Map<String,Object>>
				List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
				for (SavedBill savedBill : savedBillsList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("mailNo", savedBill.getMailNoStr());
					map.put("receiver", savedBill.getReceiverNameStr());
					listData.add(map);
				}

				// 将其转化为adapter
				SimpleAdapter adapter = new SimpleAdapter(mActivity, listData,
						R.layout.all_bill_checkout_details_layout,
						new String[] { "mailNo", "receiver" }, new int[] {
								R.id.textView_mailNo,
								R.id.textView_receiverName });
				allBillsListview.setAdapter(adapter);
				// 显示总条数
				TextView allNum = (TextView) view
						.findViewById(R.id.TextView_allBillNum);
				allNum.setText(savedBillsList.size() + "");

				new AlertDialog.Builder(mActivity)
						.setTitle("当前数据库所有记录：")
						.setView(view)
						.setNegativeButton("清空所有",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 清空所有记录
										mysqHelper.clearAllBills();
										Toast.makeText(mActivity, "本地记录已清空!",
												Toast.LENGTH_SHORT).show();
									}
								}).setPositiveButton("知道了", null).show();

				return true;
			}
		});
	}

	private void billInStore() {
		String receiverNameStr = receiverName.getText().toString().trim();
		String receiverMobilephoneStr = receiverMobilephone.getText()
				.toString().trim();
		if (mailNo.equals("") || receiverNameStr.equals("")
				|| receiverMobilephoneStr.equals("")) {
			Toast.makeText(mActivity, "保存失败:请填写完整的运单信息！", Toast.LENGTH_SHORT)
					.show();
		} else if (companyId == -1) {
			Toast.makeText(mActivity, "保存失败:无效物流公司，请手动选择！", Toast.LENGTH_SHORT)
					.show();
		} else if (!receiverMobilephoneStr.substring(0, 1).equals("1")
				|| receiverMobilephoneStr.length() != 11) {
			Toast.makeText(mActivity, "保存失败:请输入正确的手机号码！", Toast.LENGTH_SHORT)
					.show();
		} else {
			// 获取当前入库编号
			String currentInStoreBillNo = mySharedPreferences.getString(
					"current_instore_bill_no", "");

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

	private void myBillInStore() {
		// int requestCode = curBillNo + OFFSET;
		if (allBillNum == 0) {
			Toast.makeText(mActivity, "当前本地为空，提交失败!", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		SavedBill savedBill = savedBillsList.get(curBillNo);
		Log.e("入库：", curBillNo + "");
		Intent intent = new Intent(mActivity, LoadingActivity.class);
		intent.putExtra("loadingType", "instore");
		intent.putExtra("methodName", "savebill");
		intent.putExtra("s1", savedBill.getMailNoStr());
		intent.putExtra("s2", savedBill.getReceiverNameStr());
		intent.putExtra("s3", savedBill.getCompanyId());
		intent.putExtra("s4", savedBill.getReceiverMobilephoneStr());
		intent.putExtra("s5", savedBill.getAdminId());
		intent.putExtra("s6", savedBill.getCurrentInStoreBillNo());
		curBillNo++;
		startActivityForResult(intent, 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) { // 入库操作
			if (resultCode == Activity.RESULT_OK) {
				final String result = data.getStringExtra("Result"); // 接收Intent传递过来的查询结果

				if (result.equals("查询失败") || result.equals("查询为空")) {
					// new AlertDialog.Builder(mActivity).setTitle("入库操作失败")
					// .setMessage("当前网络不给力，入库操作失败，请稍后重试。")
					// .setNeutralButton("确定", null).show();
					Toast.makeText(
							mActivity,
							"运单号:"
									+ savedBillsList.get(curBillNo - 1)
											.getMailNoStr() + " 入库失败，网络故障!",
							Toast.LENGTH_SHORT).show();
					// Log.e("入库result:"+savedBillsList.get(curBillNo).getMailNoStr(),
					// "查询失败");
				} else if (result.equals("入库失败")) {
					// new AlertDialog.Builder(mActivity).setTitle("入库失败")
					// .setMessage("入库失败，或当前单号已经入库，请核对单号后重试。")
					// .setNeutralButton("确定", null).show();
					Toast.makeText(
							mActivity,
							"运单号:"
									+ savedBillsList.get(curBillNo - 1)
											.getMailNoStr() + " 入库失败,已入库！",
							Toast.LENGTH_SHORT).show();
					// Log.e("入库result:"+savedBillsList.get(curBillNo).getMailNoStr(),
					// "入库失败，或当前单号已经入库，请核对单号后重试。");
				} else {
					// Log.e("入库result:"+savedBillsList.get(curBillNo).getMailNoStr(),
					// "入库成功");
					Toast.makeText(
							mActivity,
							"运单号:"
									+ savedBillsList.get(curBillNo - 1)
											.getMailNoStr() + " 入库成功！",
							Toast.LENGTH_SHORT).show();

					// 入库成功后调用URL
					new Thread() {
						@Override
						public void run() {
							try {
								URL url = new URL(
										"http://e-zhaosheng.com/vgoo/syn_fwz.php?act=syn_fwz&bill_id="
												+ result);
								HttpURLConnection conn = (HttpURLConnection) url
										.openConnection();
								conn.setRequestProperty("accept", "*/*");
								conn.setRequestProperty("connection",
										"Kepp-Alive");
								conn.setRequestProperty("user-agent",
										"Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1)");
								conn.connect();
								// BufferedReader bufferedReader = new
								// BufferedReader(new
								// InputStreamReader(conn.getInputStream()));
								// String line = "";
								// String httpResult = "";
								// while((line = bufferedReader.readLine()) !=
								// null) {
								// httpResult += line;
								// }
								// handler.sendEmptyMessage(2);

								// 入库成功并且调用URL无异常后会自动删除该条记录
								int deleteRes = mysqHelper
										.delete_one_bill(savedBillsList.get(
												curBillNo - 1).getMailNoStr());
								if (deleteRes == -1)
									Log.e("delete this record:", "failed");
								else
									Log.e("delete this record:", "success");
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(mActivity, "HTTP访问异常！",
										Toast.LENGTH_SHORT).show();
							}
						}
					}.start();

					String prevInstoreBillNo = mySharedPreferences.getString(
							"current_instore_bill_no", "");
					int serialNumber = mySharedPreferences.getInt(
							"serial_number", 1);

					arrayList.clear();
					map1.put("item", "登录时间");
					map1.put("item_content", login_time);
					instoreBills++;
					map2.put("item", "已入库");
					map2.put("item_content", instoreBills + " 件");
					map3.put("item", "上一次入库编号");
					map3.put("item_content", prevInstoreBillNo);

					serialNumber++;
					String currentInStoreBillNo = "";
					currentInStoreBillNo = prevInstoreBillNo.substring(0, 9)
							+ serialNumber;
					map4.put("item", "当前入库编号");
					map4.put("item_content", currentInStoreBillNo);

					arrayList.add(map1);
					arrayList.add(map2);
					arrayList.add(map3);
					arrayList.add(map4);
					adapter.notifyDataSetChanged();

					editor.putInt("serial_number", serialNumber); // 保存
					editor.putInt("instore_bills", instoreBills);
					editor.putString("prev_instore_bill_no", prevInstoreBillNo);
					editor.putString("current_instore_bill_no",
							currentInStoreBillNo);
					editor.commit();

					mailNo.setText("");
					companySpinner.setSelection(length);
					companyId = -1;
					receiverName.setText("");
					receiverMobilephone.setText("");
				}
				if (curBillNo < allBillNum)
					myBillInStore();
			}
		} else if (requestCode == 2) { // 快件查询
			if (resultCode == Activity.RESULT_OK) {
				mailNo.setText(mailNoStr);
				String httpResult = data.getStringExtra("Result"); // 接收Intent传递过来的查询结果
				Log.e("Home：快件查询：", httpResult);
				try {
					JSONObject result = new JSONObject(httpResult); // 转换为JSONObject
					companyId = result.getInt("companyId");
					String receiverNameStr = result.getString("recName");
					String receiverMobilephoneStr = result.getString("mobile");

					if (companyId == -1) { // 获取上一次的快递公司
						companyId = mySharedPreferences.getInt(
								"prev_company_id", -1);
					} else {
						editor.putInt("prev_company_id", companyId);
						editor.commit();
					}
					receiverName.setText(receiverNameStr);
					receiverMobilephone.setText(receiverMobilephoneStr);
					String companyStr2 = result.getString("companyName")
							.toString();
					Log.e("companyName:", companyStr2);
					for (int i = 0; i < length; i++) {
						if (companyStr[i].equals(companyStr2))
							companySpinner.setSelection(i);
					}
					Toast.makeText(mActivity, "快件查询成功", Toast.LENGTH_SHORT)
							.show();
				} catch (JSONException e) { // 快件查询异常
					Toast.makeText(mActivity, "快件不存在", Toast.LENGTH_SHORT)
							.show();
					companyId = mySharedPreferences.getInt("prev_company_id",
							-1); // 获取上一次的快递公司
					receiverName.setText("0");
					receiverMobilephone.setText("");
				}

				// String companyStr2 = "";
				// switch(companyId) {
				// case -1: companyStr2 = "未知"; break;
				// case 0: companyStr2 = "校园谷快递"; break;
				// case 2: companyStr2 = "EMS"; break;
				// case 100: companyStr2 = "申通快递"; break;
				// case 101: companyStr2 = "圆通快递"; break;
				// case 102: companyStr2 = "韵达快运"; break;
				// case 103: companyStr2 = "宅急送"; break;
				// case 106: companyStr2 = "联邦快递"; break;
				// case 500: companyStr2 = "中通速递"; break;
				// case 504: companyStr2 = "天天快递"; break;
				// case 505: companyStr2 = "顺丰快递"; break;
				// default: break;
				// }

				receiverMobilephone.requestFocus(); // 获取焦点
				receiverMobilephone.setSelection(receiverMobilephone.getText()
						.toString().length()); // 设置光标位置
			}
		} else if (requestCode == 3) { // 设置起始编号
			int serialNumber = mySharedPreferences.getInt(
					"serial_number_start", 0);
			arrayList.clear();
			map1.put("item", "登录时间");
			map1.put("item_content", login_time);
			map2.put("item", "已入库");
			map2.put("item_content", instoreBills + " 件");
			String prevInstoreBillNo = mySharedPreferences.getString(
					"prev_instore_bill_no", "");
			map3.put("item", "上一次入库编号");
			map3.put("item_content", prevInstoreBillNo);
			String currentInStoreBillNo = "";
			// 生成入库编号，20140601-001，排除以“xyg”开头的编号
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd"); // 获取当前时间
			String datePre = simpleDateFormat.format(new java.util.Date());
			currentInStoreBillNo = datePre + "-" + serialNumber;
			map4.put("item", "当前入库编号");
			map4.put("item_content", currentInStoreBillNo);

			arrayList.add(map1);
			arrayList.add(map2);
			arrayList.add(map3);
			arrayList.add(map4);
			adapter.notifyDataSetChanged();

			editor.putInt("serial_number", serialNumber); // 保存
			editor.putString("current_instore_bill_no", currentInStoreBillNo);
			editor.commit();
		} else if (requestCode == 5) { // 查询物流公司列表
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getStringExtra("Result"); // 接收Intent传递过来的查询结果
				if (result.equals("查询失败") || result.equals("查询为空")) {
					new AlertDialog.Builder(mActivity).setTitle("失败")
							.setMessage("当前网络不给力，查询物流公司列表失败，请稍后重试。")
							.setNeutralButton("确定", null).show();
				} else {
					Toast.makeText(mActivity, "查询物流公司列表成功！", Toast.LENGTH_SHORT)
							.show();
					try {
						JSONArray jsonArray = new JSONArray(result);
						length = jsonArray.length();
						if (length > 0) {
							companyStr = new String[length + 1];
							for (int i = 0; i < length; i++) {
								JSONObject jsonObj = (JSONObject) jsonArray
										.opt(i);
								companyStr[i] = jsonObj.getString("name");
							}
							companyStr[length] = "未知";
							companyAdapter = new ArrayAdapter<String>(
									mActivity,
									android.R.layout.simple_spinner_item,
									companyStr);
							companyAdapter
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) >= '0' && str.charAt(i) <= '9')
					|| (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
					|| (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')) {
				j++;
			}
			if (j == str.length()) {
				return true;
			}
		}
		return false;
	}

	private class MainHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HadwareControll.BARCODE_READ:
				String scanResult0 = msg.obj + ""; // 条形码扫描结果
				// V2.1新增功能：扫码入库时将前后的“*”号过滤掉
				String scanResult = "";
				if (scanResult0.charAt(0) == '*') { // 如果首字母为“*”号，则去掉前后的“*”号
					scanResult = scanResult0.substring(1,
							scanResult0.length() - 1);
				} else {
					scanResult = scanResult0;
				}
				mailNo.setText(scanResult.replaceAll(" ", "")); // 去掉所有空格
				play_sound();
				// 扫描完毕直接查询快件
				mailNoStr = mailNo.getText().toString().trim();
				if (!mailNoStr.equals("") && isLetterOrDigit(mailNoStr)) {
					Intent intent = new Intent(mActivity,
							LoadingIIActivity.class);
					intent.putExtra("mailNo", mailNoStr);
					startActivityForResult(intent, 2);
				} else {
					Toast.makeText(mActivity, "运单号有误，请重新扫描！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_F9 || keyCode == KeyEvent.KEYCODE_F10
				|| keyCode == KeyEvent.KEYCODE_F11) {
			// V2.1.1 修改：黄色按键二次按下时，快件直接入库（不用再点击屏幕上的“快件入库”按钮）,
			// 判断依据：若“运单号”不为空，则调用入库函数
			if (!mailNo.getText().toString().equals("")) {
				billInStore(); // 快件入库
			} else {
				if (press) {
					HadwareControll.scan_start();
					press = false;
					return true;
				}
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			new AlertDialog.Builder(mActivity)
					.setTitle("退出")
					.setMessage("确定要退出校园谷吗？")
					.setNegativeButton("取消", null)
					.setPositiveButton("退出",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mActivity.finish();
								}
							}).show();
		}
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_F9 || keyCode == KeyEvent.KEYCODE_F10
				|| keyCode == KeyEvent.KEYCODE_F11) {
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