/**
 * @author 韩百硕
 */
package com.h2.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.h2.constant.Parameters;
import com.h2.constant.Sensor;
import com.h2.data.Predata;

public class Location
{
	/**
	 * 获取震源
	 * 
	 * @param count
	 *            激活的传感器数量
	 * @param Token
	 *            传感器集合
	 * @return
	 */
	public static Sensor getLocation(int count, Sensor[] Token)
	{
		String[] arr = Predata.toArray(count, Token);// 从这里边挑选传感器组,arr里边是激发的传感器编号
		List<String> arrList = Tools.combine(arr, Parameters.RULENum);// arr是激发的传感器组,5是一个固定值

		Sensor[] sensors = new Sensor[arrList.size()];// 每一个组合都用一个sensor对象来存储,组合数与sensor数相同

		for (int i = 0; i < sensors.length; i++)
		{
			sensors[i] = getOneSensor(arrList.get(i), Token);
		}
		return getAveSensor(sensors);

	}

	/**
	 * 得到一个组合中震源的位置
	 * 
	 * @param str
	 *            组合
	 * @param Token
	 *            总的传感器
	 * @return
	 */
	private static Sensor getOneSensor(String str, Sensor[] Token)
	{
		// 得到组合中传感器的编号
		int[] arr = Predata.getSensorsID(str);
		// 从Token里取出传感器计算震源
		List<Sensor> sensors = new ArrayList<Sensor>();
		for (int id : arr)
		{
			sensors.add(Token[id]);
		}
		return LinearAlgebra.getXYZ(sensors);
	}

	/**
	 * 计算震源的平均位置
	 * 
	 * @param sensors
	 *            各个组合计算出来的震源坐标
	 * @return 平均震源坐标
	 */
	private static Sensor getAveSensor(Sensor[] sensors)
	{
		Sensor sensor = new Sensor();
		int sensorCount = sensors.length;

		if (sensorCount != 1)
		{
			Calendar cal = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyMMddhhmmss");

			double Altitude = 0;
			double Longtitude = 0;
			double Latitude = 0;
			long time = 0;

			for (Sensor sen : sensors)
			{
				Altitude += sen.getAltitude();
				Longtitude += sen.getLongtitude();
				Latitude += sen.getLatitude();

				try
				{
					cal.setTime(df.parse(sen.getTime()));
				} catch (ParseException e)
				{
					System.out.println("Location类中" + "getAveSensor函数日期转化失败！");
				}
				time += cal.getTimeInMillis();
			}
			sensor.setAltitude((double) Math.round((Altitude / sensorCount) * 100) / 100);
			sensor.setLongtitude((double) Math.round((Longtitude / sensorCount) * 10000) / 10000);
			sensor.setLatitude((double) Math.round((Latitude / sensorCount) * 10000) / 10000);
			cal.setTimeInMillis(time / sensorCount);
			sensor.setTime(timeformat(cal));

		} else
		{
			sensor = sensors[0];
		}
		return sensor;
	}

	private static String timeformat(Calendar cal)
	{
		int y = cal.get(Calendar.YEAR) % 100;
		int M = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		return handleTime(y) + handleTime(M) + handleTime(d) + handleTime(h) + handleTime(m) + handleTime(s);
	}

	/**
	 * 确保数据是两位长
	 * 
	 * @param time
	 * @return 两位表示的时间字段
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
