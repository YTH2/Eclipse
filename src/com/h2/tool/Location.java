package com.h2.tool;

import java.util.ArrayList;
import java.util.List;
import com.h2.constant.Parameters;
import com.h2.constant.Sensor;
import com.h2.data.Predata;

public class Location
{
	/**
	 * 
	 * @param count
	 *            激活的传感器数量
	 * @param Token
	 *            传感器集合
	 * @return
	 */
	public static Sensor getLocation(int count, Sensor[] Token)
	{
		Sensor sensor = new Sensor();

		String[] arr = Predata.toArray(count, Token);// 从这里边挑选传感器组,arr里边是激发的传感器编号
		List<String> arrList = Tools.combine(arr, Parameters.RULENum);// arr是激发的传感器组,5是一个固定值

		Sensor[] sensors = new Sensor[arrList.size()];// 每一个组合都用一个sensor对象来存储,组合数与sensor数相同

		for (int i = 0; i < sensors.length; i++)
		{
			sensors[i] = getOneSensor(arrList.get(i), Token);
		}
		sensor = getAveSensor(sensors);
		return sensor;
	}

	/**
	 * 得到一个组合中震源的位置
	 * 
	 * @param str
	 * @param Token
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
	 * @return
	 */
	private static Sensor getAveSensor(Sensor[] sensors)
	{
		Sensor sensor = new Sensor();
		int sensorCount = sensors.length;

		if (sensorCount != 1)
		{
			double Altitude = 0;
			double Longtitude = 0;
			double Latitude = 0;

			for (Sensor sen : sensors)
			{
				Altitude += sen.getAltitude();
				Longtitude += sen.getLongtitude();
				Latitude += sen.getLatitude();

			}
			sensor.setAltitude(Altitude / sensorCount);
			sensor.setLongtitude(Longtitude / sensorCount);
			sensor.setLatitude(Latitude / sensorCount);

		} else
		{
			sensor = sensors[0];
		}
		return sensor;
	}
}
