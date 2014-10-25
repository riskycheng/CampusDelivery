package com.chengjian.vgoo2;

import java.util.HashMap;
import java.util.Map;

import zhang.fan.vgoo2.idata.HadwareControll;
import zhang.fan.vgoo2.idata.LoadingIIActivity;

import com.chengjian.utils.SQLiteHelper;
import com.chengjian.vgoo2.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

/***
 * @author huangsm
 * @date 2013-1-4
 * @email huangsanm@gmail.com
 * @desc fragment Activity ���õ�˳����onAttach-->onCreate-->...-->onResume
 *       ���л�����һ��fragment��ʱ�򣬻����onPause-->onStop-->onDestroyView
 *       �л�����ʱ��onCreateView-->onActivityCreated-->onStart-->onResume
 *       Ҳ����˵onAttach ��onCreateֻ������һ�Ρ������ڽ������ݳ�ʼ����ʱ��Ӧ�ðѹ����ŵ������������н��С�
 */
public class FragmentTabActivity extends FragmentActivity {

	private final static int TRANSLATE_ANIMATION_WIDTH = 150;
	private final static int ANIMATION_DURATION_FAST = 450;
	private final static int ANIMATION_DURATION_SLOW = 350;
	private final static int MOVE_DISTANCE = 50;
	boolean press = true;
	public Handler mHandler = new MainHandler();
	private TabHost mTabHost;
	private TabManager mTabManager;
	private LinearLayout mSettingLinearLayout;
	private LinearLayout mMainLinearLayout;
	protected MediaPlayer mediaPlayer = null;
	// ��Ļ���
	private int mWidth;
	private float mPositionX;
	// ����״̬
	private boolean mSlided = false;

	// �ؼ�
	public EditText mailNo = null;
	public EditText EditText_yundanhao = null;
	// ��ʼ��
	public static String cur_tab = "";
	public HomeFragment homeFragment = null;
	public QueryAFragment queryAFragment = null;

	public Button instoreAdd = null;
	public Button reSendMsgBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityUtils.requestNotTitleBar(this);

		setContentView(R.layout.fragment_tabs);

		mWidth = getResources().getDisplayMetrics().widthPixels;

		// �̳�tabactivity.getTabHost()����Ҫsetup()
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mTabManager = new TabManager(this, mTabHost, R.id.containertabcontent);

		// 1st Tab
		RelativeLayout HomeTab = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.home_tab_bottom_layout, null);
		mTabManager.addTab(mTabHost.newTabSpec("Home").setIndicator(HomeTab),
				HomeFragment.class, null);

		// 2nd Tab
		RelativeLayout QueryATab = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.single_tab_bottom_layout, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("QueryATab").setIndicator(QueryATab),
				QueryAFragment.class, null);

		// 3rd Tab
		RelativeLayout QueryBTab = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.all_tab_bottom_layout, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("QueryBTab").setIndicator(QueryBTab),
				QueryBFragment.class, null);

		// 4th Tab
		RelativeLayout AboutTab = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.setting_tab_bottom_layout, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("AboutTab").setIndicator(AboutTab),
				AboutTabFragment.class, null);

		// ��ʼ�� �������ݿ�
		SQLiteDatabase database = this.openOrCreateDatabase("bill.db",
				MODE_APPEND, null);// �������ݿ�
		final SQLiteHelper db = new SQLiteHelper(getApplicationContext());
		db.getWritableDatabase();

		HadwareControll.Open(); // ���豸
		HadwareControll.m_handler = mHandler;
		// ��ʼ��EditText
		mailNo = HomeFragment.mailNo;
		instoreAdd = HomeFragment.inStoreAdd;
		EditText_yundanhao = QueryAFragment.EditText_yundanhao;
		homeFragment = new HomeFragment();
		reSendMsgBtn = QueryAFragment.Button_submit;
		mSettingLinearLayout = (LinearLayout) findViewById(R.id.setting);
		mMainLinearLayout = (LinearLayout) findViewById(R.id.main);
		// mMainLinearLayout.setOnTouchListener(mOnTouchListener);
		// slideIn();

		ListView listView = (ListView) findViewById(R.id.list);
		// listView.setOnTouchListener(mOnTouchListener);
		// findViewById(R.id.btn_settings).setOnClickListener(mOnClickListener);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tag"));
		}

		// // ��ʼ��listview
		// final Resources res = getResources();
		// String[] mTitles = res.getStringArray(R.array.setting_items);
		// ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
		// R.layout.fragment_setting_item, R.id.item, mTitles);
		// listView.setAdapter(mAdapter);
	}

	// // �����ť
	// private OnClickListener mOnClickListener = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.btn_settings:
	// if (mSlided) {
	// slideIn();
	// } else {
	// slideOut();
	// }
	// break;
	// }
	// }
	// };

	// // ����
	// private OnTouchListener mOnTouchListener = new OnTouchListener() {
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (v.getId() == R.id.main) {
	// int action = event.getAction();
	// switch (action) {
	// case MotionEvent.ACTION_DOWN:
	// mPositionX = event.getX();
	// break;
	// case MotionEvent.ACTION_MOVE:
	// final float currentX = event.getX();
	// // ����߻���
	// if (currentX - mPositionX <= -MOVE_DISTANCE && !mSlided) {
	// slideOut();
	// } else if (currentX - mPositionX >= MOVE_DISTANCE
	// && mSlided) {
	// slideIn();
	// }
	// break;
	// default:
	// slideIn();
	// break;
	// }
	// return true;
	// }
	// return false;
	// }
	// };
	//
	// /**
	// * ���������
	// */
	// private void slideOut() {
	// TranslateAnimation translate = new TranslateAnimation(mWidth,
	// TRANSLATE_ANIMATION_WIDTH, 0, 0);
	// translate.setDuration(ANIMATION_DURATION_SLOW);
	// translate.setFillAfter(true);
	// mSettingLinearLayout.startAnimation(translate);
	// mSettingLinearLayout.getAnimation().setAnimationListener(
	// new Animation.AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation anim) {
	//
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation anima) {
	// TranslateAnimation animation = new TranslateAnimation(
	// 0, TRANSLATE_ANIMATION_WIDTH - mWidth, 0, 0);
	// animation.setDuration(ANIMATION_DURATION_FAST);
	// animation.setFillAfter(true);
	// mMainLinearLayout.startAnimation(animation);
	// mSlided = true;
	// }
	// });
	// }
	//
	// /**
	// * ���������
	// */
	// private void slideIn() {
	// TranslateAnimation translate = new TranslateAnimation(
	// TRANSLATE_ANIMATION_WIDTH, mWidth, 0, 0);
	// translate.setDuration(ANIMATION_DURATION_FAST);
	// // �������ʱͣ�ڽ���λ��
	// translate.setFillAfter(true);
	// mSettingLinearLayout.startAnimation(translate);
	// mSettingLinearLayout.getAnimation().setAnimationListener(
	// new Animation.AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	// TranslateAnimation mainAnimation = new TranslateAnimation(
	// -mWidth + TRANSLATE_ANIMATION_WIDTH, 0, 0, 0);
	// mainAnimation.setDuration(ANIMATION_DURATION_SLOW);
	// mainAnimation.setFillAfter(true);
	// mMainLinearLayout.startAnimation(mainAnimation);
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// mSlided = false;
	// }
	// });
	//
	// }
	//
	// @Override
	// public boolean onContextItemSelected(MenuItem item) {
	// if (mSlided) {
	// slideIn();
	// } else {
	// slideOut();
	// }
	// return true;
	// }
	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK && mSlided) {
	// slideIn();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	/**
	 * ����֮ǰ
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tag", mTabHost.getCurrentTabTag());
	}

	public static class TabManager implements TabHost.OnTabChangeListener {
		private final FragmentTabActivity mActivity;
		// ����tab
		private final Map<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
		private final TabHost mTabHost;
		private final int mContainerID;
		private TabInfo mLastTab;

		/**
		 * @param activity
		 *            context
		 * @param tabHost
		 *            tab
		 * @param containerID
		 *            fragment's parent note
		 */
		public TabManager(FragmentTabActivity activity, TabHost tabHost,
				int containerID) {
			mActivity = activity;
			mTabHost = tabHost;
			mContainerID = containerID;
			mTabHost.setOnTabChangedListener(this);
		}

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;
			private Fragment fragment;

			TabInfo(String _tag, Class<?> _clss, Bundle _args) {
				tag = _tag;
				clss = _clss;
				args = _args;
			}
		}

		static class TabFactory implements TabHost.TabContentFactory {
			private Context mContext;

			TabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumHeight(0);
				v.setMinimumWidth(0);
				return v;
			}
		}

		// ����tab
		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new TabFactory(mActivity));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			final FragmentManager fm = mActivity.getSupportFragmentManager();
			info.fragment = fm.findFragmentByTag(tag);
			// isDetached����״̬
			if (info.fragment != null && !info.fragment.isDetached()) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}
			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);
		}

		@Override
		public void onTabChanged(String tabId) {
			Log.e("tabIndex:", tabId + "");
			cur_tab = tabId;
			TabInfo newTab = mTabs.get(tabId);
			if (mLastTab != newTab) {
				FragmentManager fragmentManager = mActivity
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				// ����֮ǰ��tab
				if (mLastTab != null && mLastTab.fragment != null) {
					fragmentTransaction.detach(mLastTab.fragment);
				}
				if (newTab != null) {
					if (newTab.fragment == null) {
						newTab.fragment = Fragment.instantiate(mActivity,
								newTab.clss.getName(), newTab.args);
						fragmentTransaction.add(mContainerID, newTab.fragment,
								newTab.tag);
					} else {
						// ����
						fragmentTransaction.attach(newTab.fragment);
					}
				}
				mLastTab = newTab;
				fragmentTransaction.commit();
				// ���ڽ��̵����߳��У����첽�ķ�ʽ��ִ��,�����Ҫ����ִ������ȴ��еĲ�������Ҫ�����������
				// ���еĻص�����ص���Ϊ��������������б�ִ����ɣ����Ҫ��ϸȷ����������ĵ���λ�á�
				fragmentManager.executePendingTransactions();
			}
		}
	}

	// key Event
	private class MainHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HadwareControll.BARCODE_READ:
				String scanResult0 = msg.obj + ""; // ������ɨ����
				// V2.1�������ܣ�ɨ�����ʱ��ǰ��ġ�*���Ź��˵�
				String scanResult = "";
				if (scanResult0.charAt(0) == '*') { // �������ĸΪ��*���ţ���ȥ��ǰ��ġ�*����
					scanResult = scanResult0.substring(1,
							scanResult0.length() - 1);
				} else {
					scanResult = scanResult0;
				}
				mailNo = HomeFragment.mailNo;
				EditText_yundanhao = QueryAFragment.EditText_yundanhao;
				String temp = scanResult.replaceAll(" ", "");

				if (cur_tab.equals("Home")) {
					mailNo.setText(temp);
					HomeFragment.before_str_mailNo = temp;
				} else {
					EditText_yundanhao.setText(temp);
				}

				Log.e("ScanResult:", temp);
				play_sound();

				// ɨ�����ֱ�Ӳ�ѯ���
				// String mailNoStr = mailNo.getText().toString().trim();
				// if (!mailNoStr.equals("") && isLetterOrDigit(mailNoStr)) {
				// Intent intent = new Intent(FragmentTabActivity.this,
				// LoadingIIActivity.class);
				// intent.putExtra("mailNo", mailNoStr);
				// startActivityForResult(intent, 2);
				// } else {
				// Toast.makeText(FragmentTabActivity.this, "�˵�������������ɨ�裡",
				// Toast.LENGTH_SHORT).show();
				// }
				break;
			default:
				break;
			}
		}
	};

	public void play_sound() {
		try {
			if (mediaPlayer == null)
				mediaPlayer = MediaPlayer.create(FragmentTabActivity.this,
						R.raw.beep);
			mediaPlayer.stop();
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
			finish();
		if (keyCode == KeyEvent.KEYCODE_F9 || keyCode == KeyEvent.KEYCODE_F10
				|| keyCode == KeyEvent.KEYCODE_F11) {
			if (!homeFragment.mailNo.getText().toString().trim().equals("")
					&& cur_tab.equals("Home")) {
				Log.e("log:", "�������ݿ�"); // ������
				instoreAdd = HomeFragment.inStoreAdd;
				instoreAdd.performClick();
			} else if (cur_tab.equals("QueryATab")) {
				if (!queryAFragment.EditText_yundanhao.getText().toString().trim().equals("")) {
					Log.e("log:", "�ط�����");// �ط�
					reSendMsgBtn = QueryAFragment.Button_submit;
					reSendMsgBtn.performClick();
				}else{
					if (press) {
						HadwareControll.scan_start();
						press = false;
						return true;
					}
				}
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
			new AlertDialog.Builder(FragmentTabActivity.this)
					.setTitle("�˳�")
					.setMessage("ȷ��Ҫ�˳�У԰����_test��")
					.setNegativeButton("ȡ��", null)
					.setPositiveButton("�˳�",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).show();
		}
		return super.onKeyDown(keyCode, event);
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

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_F9 || keyCode == KeyEvent.KEYCODE_F10
				|| keyCode == KeyEvent.KEYCODE_F11) {
			HadwareControll.scan_stop();
			press = true;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	// public void play_sound() {
	// try {
	// if (mediaPlayer == null)
	// mediaPlayer = MediaPlayer.create(mActivity, R.raw.beep);
	// mediaPlayer.stop();
	// mediaPlayer.prepare();
	// mediaPlayer.start();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void onDestroy() {
		HadwareControll.Close();
		HadwareControll.m_handler = null;
		super.onDestroy();
	}

}