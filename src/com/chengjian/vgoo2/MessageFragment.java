package com.chengjian.vgoo2;

import com.chengjian.vgoo2.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/***
 * @author huangsm
 * @date 2013-1-4
 * @email huangsanm@gmail.com
 * @desc 所有信息列表
 */
public class MessageFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		System.out.println("Fragment :: onCreate");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("MessageFragment :: onCreateView");
		return inflater.inflate(R.layout.query_left_layout, container, false);
	}
}
