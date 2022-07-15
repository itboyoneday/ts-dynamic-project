package com.tsingsoft.common.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author bask
 */
public class TimeConsuming {

	private long startTime;
	private long endTime;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void end() {
		endTime = System.currentTimeMillis();
	}

	/**
	 * 输出固定格式毫秒
	 *
	 * @return
	 */
	public double getConsumingMillis() {
		return (endTime - startTime);
	}

	/**
	 * 输出固定格式秒
	 *
	 * @return
	 */
	public double getConsumingSecond() {
		return (endTime - startTime) / Double.parseDouble(1000 + "");
	}

	/**
	 * 输出固定格式分钟
	 *
	 * @return
	 */
	public double getConsumingMinute() {
		return ((endTime - startTime) / Double.parseDouble(1000 + ""))/60;
	}

	/**
	 * 自定义输出格式
	 * 小于1秒，输出毫秒
	 * 大于1秒，小于60秒，输出秒
	 * 大于60秒，小于3600秒 输出分钟
	 * 大于3600秒以上，输出小时
	 * @return
	 */
	public String getConsuming() {
		NumberFormat numFormat = NumberFormat.getInstance();
		numFormat.setMaximumFractionDigits(2);
		numFormat.setMinimumFractionDigits(2);
		numFormat.setGroupingUsed(false);
		double f = (endTime - startTime);
		if(f < 1000){
			return (endTime - startTime) + "毫秒";
		}else if(f >=1000 && f <60000){
			return numFormat.format((endTime - startTime) / Double.parseDouble(1000 + "")) + "秒";
		}else if(f>=60000 && f < 3600000){
			return numFormat.format(((endTime - startTime) / Double.parseDouble(1000 + ""))/60) + "分钟";
		}else if(f >=3600000){
			return numFormat.format((((endTime - startTime) / Double.parseDouble(1000 + ""))/60)/60) + "小时";
		}else{
			return (endTime - startTime) + "毫秒";
		}
	}

	/**
	 * 获取截取的时间串
	 * @param time
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getTime(String time,int start,int end) {
		return time != null ? time.substring(start, end) : "";
	}

	/**
	 * 获取当前时间
	 * @param parren 格式化规则
	 * @return
	 */
	public static String getCurrentDay(String parren){
		if(parren == null){
			parren = "yyyyMMdd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(parren);
		Date date = new Date();
		String ymd = null;
		try {
			ymd = sdf.format(date);
		}catch (Exception e){}
		return ymd;
	}

	/**
	 * 获得指定日期的前n天
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String pattern,String specifiedDay,int dayNum){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat(pattern).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day-dayNum);

		String dayBefore=new SimpleDateFormat(pattern).format(c.getTime());
		return dayBefore;
	}



	/**
	 * 获得指定日期的后一天
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String pattern,String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat(pattern).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day+1);

		String dayAfter=new SimpleDateFormat(pattern).format(c.getTime());
		return dayAfter;
	}

	public static void main(String[] args) {
		System.out.println(getSpecifiedDayAfter("yyyyMMdd",getCurrentDay(null)));
	}

}
