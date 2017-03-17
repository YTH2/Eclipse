/**
 * @author 韩百硕
 * 判断传感器激发功能
 */
package com.h2.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class Event
{

	/**
	 * 在这10s内激发的传感器
	 * 
	 * @param sensors
	 *            所有的传感器
	 * @return
	 */
	public static Sensor[] motivate(Sensor[] sensors)
	{
		for (int i = 0; i < Parameters.WINDOWNUMBER; i++)
		{
			for (int j = 0; j < sensors.length; j++)
			{
				if (!sensors[j].isSign())
				{
					boolean inter = Tools.getToken(sensors[j], i);
					if (inter)
					{
						sensors[j].setSign(inter);
						sensors[j].setTime(getTime(sensors[j].getDataFile(), i));
					}
				}
			}
		}
		return sensors;
	}

	/**
	 * 计算传感器的激发时间
	 * 
	 * @param dataFileName
	 *            文件名中有时间
	 * @param i
	 *            第几个60ms
	 * @return
	 */
	private static String getTime(String dataFileName, int i)
	{

		int begin = dataFileName.lastIndexOf("/");
		int end = dataFileName.lastIndexOf(".");
		dataFileName = dataFileName.substring(begin + 1, end);

		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyMMddhhmmss");
		try
		{
			cal.setTime(df.parse(dataFileName));
		} catch (ParseException e)
		{
			System.out.println("激发时间转换错误！");
			e.printStackTrace();
		}
		cal.add(Calendar.SECOND, 60 * i / 1000);

		return handleTime(cal.get(Calendar.YEAR) % 100) + handleTime(cal.get(Calendar.MONTH) + 1)
				+ handleTime(cal.get(Calendar.DAY_OF_MONTH)) + handleTime(cal.get(Calendar.HOUR_OF_DAY))
				+ handleTime(cal.get(Calendar.MINUTE)) + handleTime(cal.get(Calendar.SECOND));

	}

	/**
	 * 确保数据是两位长
	 * 
	 * @param time
	 * @return
	 */
	private static String handleTime(int time)
	{
		if ((time / 10) == 0)
		{
			return "0" + time;
		}
		return String.valueOf(time);
	}
}
