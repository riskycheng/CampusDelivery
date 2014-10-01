package com.chengjian.vgoo2;

import java.util.HashMap;
import java.util.Map;
import com.chengjian.vgoo2.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

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

	private TabHost mTabHost;
	private TabManager mTabManager;
	private LinearLayout mSettingLinearLayout;
	private LinearLayout mMainLinearLayout;
	// ��Ļ���
	private int mWidth;
	private float mPositionX;
	// ����״̬
	private boolean mSlided = false;

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

		RelativeLayout message = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.home_tab_bottom_layout, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("Message").setIndicator(message),
				FirstFragment.class, null);
		
		RelativeLayout app = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.single_tab_bottom_layout, null);
		mTabManager.addTab(mTabHost.newTabSpec("Apps").setIndicator(app),
				QueryAFragment.class, null);

		RelativeLayout contacts = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.all_tab_bottom_layout, null);
		mTabManager.addTab(mTabHost.newTabSpec("Contact")
				.setIndicator(contacts), QueryBFragment.class, null);

		

		mSettingLinearLayout = (LinearLayout) findViewById(R.id.setting);
		mMainLinearLayout = (LinearLayout) findViewById(R.id.main);
//		mMainLinearLayout.setOnTouchListener(mOnTouchListener);
//		slideIn();

		ListView listView = (ListView) findViewById(R.id.list);
//		listView.setOnTouchListener(mOnTouchListener);
//		findViewById(R.id.btn_settings).setOnClickListener(mOnClickListener);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tag"));
		}

		// ��ʼ��listview
		final Resources res = getResources();
		String[] mTitles = res.getStringArray(R.array.setting_items);
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
				R.layout.fragment_setting_item, R.id.item, mTitles);
		listView.setAdapter(mAdapter);
	}

//	// �����ť
//	private OnClickListener mOnClickListener = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.btn_settings:
//				if (mSlided) {
//					slideIn();
//				} else {
//					slideOut();
//				}
//				break;
//			}
//		}
//	};

//	// ����
//	private OnTouchListener mOnTouchListener = new OnTouchListener() {
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			if (v.getId() == R.id.main) {
//				int action = event.getAction();
//				switch (action) {
//				case MotionEvent.ACTION_DOWN:
//					mPositionX = event.getX();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					final float currentX = event.getX();
//					// ����߻���
//					if (currentX - mPositionX <= -MOVE_DISTANCE && !mSlided) {
//						slideOut();
//					} else if (currentX - mPositionX >= MOVE_DISTANCE
//							&& mSlided) {
//						slideIn();
//					}
//					break;
//				default:
//					slideIn();
//					break;
//				}
//				return true;
//			}
//			return false;
//		}
//	};
//
//	/**
//	 * ���������
//	 */
//	private void slideOut() {
//		TranslateAnimation translate = new TranslateAnimation(mWidth,
//				TRANSLATE_ANIMATION_WIDTH, 0, 0);
//		translate.setDuration(ANIMATION_DURATION_SLOW);
//		translate.setFillAfter(true);
//		mSettingLinearLayout.startAnimation(translate);
//		mSettingLinearLayout.getAnimation().setAnimationListener(
//				new Animation.AnimationListener() {
//
//					@Override
//					public void onAnimationStart(Animation anim) {
//
//					}
//
//					@Override
//					public void onAnimationRepeat(Animation animation) {
//
//					}
//
//					@Override
//					public void onAnimationEnd(Animation anima) {
//						TranslateAnimation animation = new TranslateAnimation(
//								0, TRANSLATE_ANIMATION_WIDTH - mWidth, 0, 0);
//						animation.setDuration(ANIMATION_DURATION_FAST);
//						animation.setFillAfter(true);
//						mMainLinearLayout.startAnimation(animation);
//						mSlided = true;
//					}
//				});
//	}
//
//	/**
//	 * ���������
//	 */
//	private void slideIn() {
//		TranslateAnimation translate = new TranslateAnimation(
//				TRANSLATE_ANIMATION_WIDTH, mWidth, 0, 0);
//		translate.setDuration(ANIMATION_DURATION_FAST);
//		// �������ʱͣ�ڽ���λ��
//		translate.setFillAfter(true);
//		mSettingLinearLayout.startAnimation(translate);
//		mSettingLinearLayout.getAnimation().setAnimationListener(
//				new Animation.AnimationListener() {
//
//					@Override
//					public void onAnimationStart(Animation animation) {
//						TranslateAnimation mainAnimation = new TranslateAnimation(
//								-mWidth + TRANSLATE_ANIMATION_WIDTH, 0, 0, 0);
//						mainAnimation.setDuration(ANIMATION_DURATION_SLOW);
//						mainAnimation.setFillAfter(true);
//						mMainLinearLayout.startAnimation(mainAnimation);
//					}
//
//					@Override
//					public void onAnimationRepeat(Animation animation) {
//
//					}
//
//					@Override
//					public void onAnimationEnd(Animation animation) {
//						mSlided = false;
//					}
//				});
//
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		if (mSlided) {
//			slideIn();
//		} else {
//			slideOut();
//		}
//		return true;
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && mSlided) {
//			slideIn();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

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
}