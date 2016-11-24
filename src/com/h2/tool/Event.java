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
	public static Sensor[] motivate(Sensor[] sensors, long count)
	{
		for (int i = 0; i < Parameters.WINDOWNUMBER; i++)
		{
			for (int j = 0; j < sensors.length; j++)
			{
				if (!sensors[j].isSign())
				{
					boolean inter = Tools.getToken(sensors[j].getDataFile(), i, count,sensors[j]);
					if (inter)
					{
						sensors[j].setSign(inter);
						sensors[j].setTime(getTime(sensors[j].getDataFile(), i, count));
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
	 *            窗口数 单位时60ms
	 * @param count
	 *            ，表示第一次循环，单位是10s
	 * @return
	 * @throws ParseException
	 */
	private static long getTime(String dataFileName, int i, long count)
	{
		int second = (int) (i * 60 / 1000 + count * 10);
		
		int begin = dataFileName.lastIndexOf("/");
		int end = dataFileName.lastIndexOf(".");
		dataFileName = dataFileName.substring(begin + 1, end);

		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("hhmmss");
		try
		{
			cal.setTime(df.parse(dataFileName));
		} catch (ParseException e)
		{
			System.out.println("激发时间转换错误！");
			e.printStackTrace();
		}
		cal.add(Calendar.SECOND, second);
		return Long.parseLong(
				cal.get(Calendar.HOUR_OF_DAY) + "" + cal.get(Calendar.MINUTE) + "" + cal.get(Calendar.SECOND));

	}
}
