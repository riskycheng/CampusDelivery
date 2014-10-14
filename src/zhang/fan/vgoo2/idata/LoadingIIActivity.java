package zhang.fan.vgoo2.idata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.chengjian.vgoo2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingIIActivity extends Activity {
	private TextView textView = null;
	String httpResult = "";
	BufferedReader bufferedReader = null;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case 2:		//处理查询操作结果
					Intent intent = LoadingIIActivity.this.getIntent();
					intent.putExtra("Result", httpResult);
					setResult(RESULT_OK, intent);
					finish();
					break;
				default:
					break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);	//设置竖屏
		setContentView(R.layout.loading);
		textView = (TextView)findViewById(R.id.textView);
		
		Intent intent = this.getIntent();
		final String mailNoStr = intent.getStringExtra("mailNo");
		textView.setText("快件查询...");

		new Thread() {
			@Override
			public void run() {
				try {
//					URL url = new URL("http://e-zhaosheng.com/kd/syn_fwz.php?act=get_taobao_bill&bill_number=" + mailNoStr);
//					URL url = new URL("http://www.v-goo.com/syn_fwz.php?act=get_taobao_bill&bill_number=" + mailNoStr + "&fwz_id=1");
					URL url = new URL("http://www.v-goo.com/syn_fwz.php?act=get_bill&bill_number=" + mailNoStr + "&fwz_id=1");
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
					handler.sendEmptyMessage(2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
