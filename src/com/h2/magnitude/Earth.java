/**
 * @author 韩百硕
 */
package com.h2.magnitude;

import com.h2.constant.Sensor;

public class Earth
{
	/**
	 * 计算得到震级
	 * 
	 * @param sensor
	 *            上一步中求得的震源的坐标
	 * @param sensors
	 *            部署的传感器的数组
	 * @param count
	 *            激发的传感器的数量
	 * @return 最终的震级
	 */
	public static double earthClass(Sensor sensor, Sensor[] sensors, int count)
	{
		double earthclass = 0;// 震级变量
		double[] values = new double[count];// 存储两个传感器计算出来的震级

		int j = 0;// 表示多少个震级，然后求平均值
		for (int i = 0; i < sensors.length; i++)
		{
			if (sensors[i].isSign())
			{
				values[j] = getOneEarthClass(sensor, sensors[i]);
				j++;
			}
		}
		for (double d : values)
		{
			earthclass += d;
		}
		return (double) Math.round((earthclass / count) * 100) / 100.0;// 震级保留两位小数
	}

	/**
	 * 通过两个传感器计算震级
	 * 
	 * @param s
	 *            震源传感器
	 * @param s2
	 *            部署的并被激发的传感器
	 * @return 震级
	 */
	private static double getOneEarthClass(Sensor s, Sensor s2)
	{
		double distance = getDistance(s, s2);
		if (distance < 0.0 || distance > 5.0)
		{
			return 0;
		}
		return Math.log(s2.getFudu()) + getR(distance);
	}

	/**
	 * 计算两个坐标之间的距离，通过经纬度计算直线距离
	 * 
	 * @param s1
	 *            震源传感器
	 * @param s2
	 *            部署的并被激发的传感器
	 * @return 距离
	 */
	private static double getDistance(Sensor s1, Sensor s2)
	{
		// http://blog.163.com/yuck_deng/blog/static/19501514720118132513641/

		double radLat1 = rad(s1.getLatitude() / 100);
		double radLat2 = rad(s2.getLatitude() / 100);
		double a = radLat1 - radLat2;
		double b = rad(s1.getLongtitude() / 100) - rad(s2.getLongtitude() / 100);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s / 1000;
	}

	private static double rad(double d)
	{
		return d * Math.PI / 180.0;
	}

	private static double getR(double d)
	{
		if (d >= 0 && d < 0.5)
		{
			return 0.48;
		}
		if (d >= 0.5 && d < 1)
		{
			return 0.78;
		}
		if (d >= 1 && d < 1.5)
		{
			return 1.05;
		}
		if (d >= 1.5 && d < 2)
		{
			return 1.21;
		}
		if (d >= 2 && d < 2.5)
		{
			return 1.36;
		}
		if (d >= 2.5 && d < 3)
		{
			return 1.47;
		}
		if (d >= 3 && d < 3.5)
		{
			return 1.57;
		}
		if (d >= 3.5 && d < 4)
		{
			return 1.66;
		}
		if (d >= 4 && d < 4.5)
		{
			return 1.73;
		}
		if (d >= 4.5 && d <= 5.0)
		{
			return 1.8;
		}
		return 0;
	}

	private static final double EARTH_RADIUS = 6378137;
}
