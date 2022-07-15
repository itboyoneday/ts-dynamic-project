package com.tsingsoft.common.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用于生成MD5加密码（32位）的工具类
 *
 * @author wangrui
 * @version 1.0 Apr 25th, 2011
 * @copyright 北京清软创新科技有限公司
 */
@Slf4j
public class StringUtil {
	public static SimpleDateFormat SDF_YMD_HMS = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss");
	/**
	 * 返回 calendar 的 string(yyyyMMdd HH:mm:ss) 表示
	 *
	 * @param calendar
	 * @return calendar 的 string 表示
	 */
	public static String printCaldendar(Calendar calendar) {
		return SDF_YMD_HMS.format(calendar.getTime());
	}

	/**
	 * 转换加密的方法
	 *
	 * @param inStr
	 * @return 转换后的32位MD5加密码
	 */
	public static String toMD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			log.error("StringUtil中toMD5方法出错！", e);
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	static Map dotTimeMap = new HashMap();

	public static String getDotTimeString(int dotNum) {
		if (dotTimeMap.get("" + dotNum) != null) {
			return (String) dotTimeMap.get("" + dotNum);
		}
		StringBuffer sb = new StringBuffer();
		String iStr = "";
		String jStr = "";
		int k = 0;
		int td = 288 * 5 / dotNum;
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				iStr = "0" + i;
			} else {
				iStr = Integer.toString(i);
			}
			for (int j = 0; j < 60; j += td) {

				if (j < 10) {
					jStr = "0" + j;
				}else {
					jStr = Integer.toString(j);
				}
				sb.append("T" + iStr + jStr + ",");
				k++;
			}
		}
		String value = sb.substring(0, sb.length() - 1);
		dotTimeMap.put("" + dotNum, value);
		return value;
	}

	@SuppressWarnings("unchecked")
	public static String getDotTimeString(int dotNum, int fcstimeno) {
		// System.out.println("fcstimeno==="+fcstimeno);
		if (dotTimeMap.get("" + dotNum + "" + fcstimeno) != null) {
			return (String) dotTimeMap.get("" + dotNum + "" + fcstimeno);
		}
		StringBuffer sb = new StringBuffer();
		String iStr = "";
		String jStr = "";
		int k = 0;
		int td = 288 * 5 / dotNum;
		// 得到a.*
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				iStr = "0" + i;
			}else {
				iStr = Integer.toString(i);
			}
			for (int j = 0; j < 60; j += td) {
				k += (288 / dotNum);
				if (k <= fcstimeno) {
					continue;
				}
				if (j < 10) {
					jStr = "0" + j;
				}else {
					jStr = Integer.toString(j);
				}
				sb.append("A.T" + iStr + jStr + ",");
			}
		}
		// 得到b.*
		k = 0;
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				iStr = "0" + i;
			}else {
				iStr = Integer.toString(i);
			}
			for (int j = 0; j < 60; j += td) {
				k += (288 / dotNum);
				if (k > fcstimeno) {
					continue;
				}
				if (j < 10) {
					jStr = "0" + j;
				} else {
					jStr = Integer.toString(j);
				}
				sb.append("B.T" + iStr + jStr + ",");
			}
		}
		String value = sb.substring(0, sb.length() - 1);
		dotTimeMap.put("" + dotNum + "" + fcstimeno, value);
		return value;
	}

	/**
	 * 在展示的时候，可能需要去掉字符串中的html标签，只展示无格式的字符，并且在字符的长度超一定值时使用......代替超出部分
	 *
	 * @param input
	 *            输入需要展示的字符串
	 * @param length
	 *            该字符串的最大长度，如果超过长度时，添加.....
	 * @return
	 * @author zhuge
	 * @create 2011-7-12
	 */
	public static String splitAndFilterString(String input, int length) {
		if (input == null || "".equals(input.trim())) {
			return "";
		}
		String str = input.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "")
				.replaceAll("</[a-zA-Z]+[1-9]?>", "");
		str = str.replaceAll("[(/>)<]", "");
		int len = str.length();
		if (len <= length) {
			return str;
		} else {
			str = str.substring(0, length);
			str += "......";
		}
		return str;
	}

	/**
	 * 若str为""、NULL、"null"，返回""；其他返回自身
	 *
	 * @param str
	 * @return
	 */
	public static String killNull(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str;
	}

	/**
	 * 字符串是否为""、NULL、"null"
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim()) ||"undefined".equals(str)
				|| "null".equalsIgnoreCase(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 获取findStr在src中出现的次数
	 *
	 * @param src
	 * @param findStr
	 * @return
	 */
	public static int getTimes(String src, String findStr) {
		int times = 0;
		if (isEmpty(src) || isEmpty(findStr)) {
			return times;
		}
		while (src.indexOf(findStr) >= 0) {
			int findStrLength = findStr.length();
			int index = src.indexOf(findStr);
			src = src.substring(index + findStrLength);
			times++;
		}
		return times;
	}

	public static String UUID(){
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	/**
	 * 将字符串转成byte数字串
	 * 使用gbk编码，byte数字串中去掉"-"
	 * @param str
	 * @return
	 */
	public static String strToByteStr(String str) {
		String byteStr = null;
		try {
			byte[] bs = str.getBytes("gbk");
			byteStr = new String();
			for (byte b : bs) {
				byteStr += String.valueOf(b).replace("-", "");
			}
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return byteStr;
	}
}
