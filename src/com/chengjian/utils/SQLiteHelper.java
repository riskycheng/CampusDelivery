package com.chengjian.utils;

import java.util.ArrayList;

import com.chengjian.entity.SavedBill;

import android.database.SQLException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SQLiteHelper extends SQLiteOpenHelper {
	public static String DB_NAME = "bill.db";
	public static String TB_NAME = "billRecords";
	private static SQLiteHelper instance = null;
	private final static String TB_MAILNO = "mailno";
	private final static String TB_RECEIVERNAME = "receivername";
	private final static String TB_COMPANYID = "companyid";
	private final static String TB_RECEIVERMOBILE = "receivermobile";
	private final static String TB_ADMINID = "adminid";
	private final static String TB_CURRENTINSTOREBILLNO = "currentinstorebillno";

	public static SQLiteHelper getInstance(Context context) {
		if (instance == null) {
			instance = new SQLiteHelper(context);
		}
		return instance;
	}

	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建billRecords的表
		String CREATE_TABLE = "create table " + TB_NAME + " (" + TB_MAILNO
				+ " nchar primary key," + TB_RECEIVERNAME + " nchar,"
				+ TB_COMPANYID + " nchar," + TB_RECEIVERMOBILE + " nchar,"
				+ TB_ADMINID + " nchar," + TB_CURRENTINSTOREBILLNO + " nchar)";
		db.execSQL(CREATE_TABLE);
		Log.e("mySqliteHelper", "created table");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	// 插入数据
	public long insertBillData(String s1, String s2, String s3, String s4,
			String s5, String s6) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(TB_MAILNO, s1);
		contentValues.put(TB_RECEIVERNAME, s2);
		contentValues.put(TB_COMPANYID, s3);
		contentValues.put(TB_RECEIVERMOBILE, s4);
		contentValues.put(TB_ADMINID, s5);
		contentValues.put(TB_CURRENTINSTOREBILLNO, s6);
		return db.insert(TB_NAME, null, contentValues);
	}

	// 插入数据
	public long insertBillData(SavedBill savedBill) {
		long result = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(TB_MAILNO, savedBill.getMailNoStr());
		contentValues.put(TB_RECEIVERNAME, savedBill.getReceiverNameStr());
		contentValues.put(TB_COMPANYID, savedBill.getCompanyId());
		contentValues.put(TB_RECEIVERMOBILE,
				savedBill.getReceiverMobilephoneStr());
		contentValues.put(TB_ADMINID, savedBill.getAdminId());
		contentValues.put(TB_CURRENTINSTOREBILLNO,
				savedBill.getCurrentInStoreBillNo());
		try {
			result = db.insert(TB_NAME, null, contentValues);
		} catch (Exception e) {
			Log.e("insert one bill:", "failed");
		}
		return result;
	}

	/**
	 * function:根据运单号删除单条记录
	 * 
	 * @return 返回值： -1 表示失败：（重复） 1：成功插入
	 */
	public int delete_one_bill(String billNo) {
		int result = -1;
		String SqlStr = "delete from " + TB_NAME + " where " + TB_MAILNO + "="
				+ billNo;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.execSQL(SqlStr);
			result = 1;
			Log.e("delete one bill:", "sucess");
		} catch (Exception e) {
			Log.e("delete one bill:", "failed");
		}
		return result;
	}

	/**
	 * function:清空所有的bill记录
	 */
	public void clearAllBills() {
		String SqlStr = "delete * from " + TB_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.execSQL(SqlStr);
			Log.e("delete all bills:", "sucess");
		} catch (SQLException e) {
			Log.e("delete all bills:", "failed");
		}
	}

	/**
	 * function:查询当前所有记录
	 * 
	 */
	public ArrayList<SavedBill> queryAllBills() {
		ArrayList<SavedBill> arrayRes = new ArrayList<SavedBill>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TB_NAME, new String[] { TB_MAILNO,
				TB_RECEIVERNAME, TB_COMPANYID, TB_RECEIVERMOBILE, TB_ADMINID,
				TB_CURRENTINSTOREBILLNO }, null, null, null, null, TB_MAILNO);
		while (cursor.moveToNext()) {
			String s[] = new String[6];
			for (int i = 0; i < 6; i++)
				s[i] = cursor.getString(i);
			SavedBill savedBill = ParsingTool.Parsing2SavedBill(s[0], s[1],
					s[2], s[3], s[4], s[5]);
			arrayRes.add(savedBill);
		}
		Log.e("arrResLen:", arrayRes.size() + "");
		return arrayRes;
	}
}
