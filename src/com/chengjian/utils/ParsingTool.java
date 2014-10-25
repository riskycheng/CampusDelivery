package com.chengjian.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.chengjian.entity.Express;
import com.chengjian.entity.SavedBill;

public class ParsingTool {

	/**
	 * function:����ѯ��Json���������Express����
	 * 
	 * @param jsonStr
	 *            �����ѯ���ص�json�ַ���
	 * @author chengjian
	 */
	public static ArrayList<Express> Json2Expresses(String jsonStr) {
		ArrayList<Express> result = new ArrayList<Express>();
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Express express = new Express();
				express.setId(jsonObject.getInt("id"));
				express.setBill_number(jsonObject.getString("bill_number"));
				express.setBill_no(jsonObject.getString("bill_no"));
				express.setMobile(jsonObject.getString("mobile"));
				express.setRec_name(jsonObject.getString("rec_name"));
				express.setBill_status(jsonObject.getInt("bill_status"));
				express.setTime(jsonObject.getString("time"));
				express.setName(jsonObject.getString("name"));

				result.add(express);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * function:����ͨ�ַ���ת��ΪMD5���ܷ�ʽ
	 */
	public static String Convert2MD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	// parsing into savedBill
	public static SavedBill Parsing2SavedBill(String mailNoStr,
			String receiverNameStr, String companyId,
			String receiverMobilephoneStr, String adminId,
			String currentInStoreBillNo) {
		SavedBill savedBill = new SavedBill();
		savedBill.setMailNoStr(mailNoStr);
		savedBill.setReceiverNameStr(receiverNameStr);
		savedBill.setCompanyId(companyId);
		savedBill.setReceiverMobilephoneStr(receiverMobilephoneStr);
		savedBill.setAdminId(adminId);
		savedBill.setCurrentInStoreBillNo(currentInStoreBillNo);
		return savedBill;
	}

	/**
	 * function:��XML�ļ��н����� �汾��
	 * 
	 * @param path
	 *            ����XML�ļ�·��
	 * @return ����������İ汾�ţ� ���Ϊ0����ʾ��ȡʧ��
	 */
	public static String parsingVerFromXML(String path) {
		String version = "";
		File file = new File(path);
		if (!file.exists() || !file.isFile())
			return version;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(file); // ��ȡ��xml�ļ�
			// ���濪ʼ��ȡ
			Element root = doc.getDocumentElement(); // ��ȡ��Ԫ��
			NodeList versionRoot = root.getElementsByTagName("android_ver");
			version = versionRoot.item(0).getTextContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	
	/**
	 * function:ת��Ϊ�ֻ�������ʾ��ʽ���м���λΪ*
	 * @author chengjian
	 */
	public static String convert2PhoneDisplay(String phone){
		String tempA = phone.substring(0, 3);
		String tempB = "****";
		String tempC = phone.substring(7);
		return tempA + tempB + tempC;
	}
	
}
