package zhang.fan.vgoo2.idata;

import com.iData.barcodecontroll.BarcodeControll;

import android.os.Handler;
import android.os.Message;

public class HadwareControll {
	private static BarcodeControll barcodeControll = new BarcodeControll();
	static public Handler m_handler = null;
	static public final int BARCODE_READ = 10;
	private static boolean m_stop = false;
	private static boolean start_Scan=false;
	static boolean read_ok = false;
	
	public static void Open() {
		barcodeControll.Barcode_open();
		m_stop = false;
		start_Scan = false;
//		new BarcodeReadThread().start();
	}

	public static void Close() {
		m_stop = true;
		start_Scan = false;
		barcodeControll.Barcode_Close();
	}

	public static void scan_start() {
		barcodeControll.Barcode_StartScan();
		start_Scan = true;
		m_stop = false;
		new BarcodeReadThread().start();
		
	}

	public static void scan_stop() {
		barcodeControll.Barcode_StopScan();
		m_stop = true;
	}

	static class BarcodeReadThread extends Thread {
		public void run() {
			while (!m_stop) {
//				try {
//					Thread.sleep(50);
//					Thread.sleep(5);
					ReadBarcodeID();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
		};
	}

	public static void ReadBarcodeID() {
		String info = null;
		byte[] buffer = null;
		buffer = barcodeControll.Barcode_Read();
		try {
			info = new String(buffer, "utf-8");
		}catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		System.out.println("333333333======"+info.length()+m_handler+start_Scan);
		if (info.length() > 0 && m_handler != null && start_Scan) {
//			System.out.println("44444444======"+info.length()+m_handler+start_Scan);
			Message msg = new Message();
			msg.what = BARCODE_READ;
			msg.obj = info;
			System.out.println("msg.obj=" + msg.obj);
			read_ok = true;
			m_handler.sendMessage(msg);
		}
	}
}
