package com.h2.magnitude;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class Earth
{

	public static void outputEarthClass(Sensor sensor, Sensor[] sensors, int count)
	{
		double earthclass = 0;// 震级变量
		FileWriter writer = null;
		double[] values = new double[count];

		int j = 0;// 表示多个少震级，然后求平均值
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
		try
		{
			writer = new FileWriter(new File(Parameters.EARTHClassFILE), true);
			writer.write(String.valueOf(earthclass / count));
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (writer != null)
				{
					writer.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static double getOneEarthClass(Sensor s, Sensor s2)
	{
		if (getDistance(s, s2) < 0.5 || getDistance(s, s2) > 5)
		{
			return 0;
		}
		return Math.log((double) s2.getFudu()) + getR(getDistance(s, s2));
	}

	private static double getDistance(Sensor s1, Sensor s2)
	{
		// http://blog.163.com/yuck_deng/blog/static/19501514720118132513641/

		double radLat1 = rad(s1.getLatitude());
		double radLat2 = rad(s2.getLatitude());
		double a = radLat1 - radLat2;
		double b = rad(s1.getLongtitude()) - rad(s2.getLongtitude());
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s / 1000;
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
		if (d >= 4.5 && d < 5)
		{
			return 1.8;
		}
		return 0;
	}

	private static double rad(double d)
	{
		return d * Math.PI / 180.0;
	}

	private static final double EARTH_RADIUS = 6378137;
}
