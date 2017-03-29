/**
 * @author 韩百硕
 * 计算第六步的数据
 */
package com.h2.tool;

import java.util.List;

import org.jblas.DoubleMatrix;
import org.jblas.Solve;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class LinearAlgebra
{
	/**
	 * 通过五个被激发传感器计算震源坐标
	 * 
	 * @param sensors
	 * @return 拥有震源的传感器变量
	 */
	public static Sensor getXYZ(List<Sensor> sensors)
	{
		Sensor sensor = new Sensor();

		DoubleMatrix B = DoubleMatrix.zeros(4, 1);

		DoubleMatrix A = getA(sensors);

		DoubleMatrix C = getC(sensors);

		B = Solve.pinv(A).mmul(C);
		// 通过B给sensor赋值
		sensor.setTime(getSetTime(sensors.get(0), B.get(1)));// (B.get(1));
		sensor.setLatitude(B.get(1));
		sensor.setLongtitude(B.get(2));
		sensor.setAltitude(B.get(3));
		return sensor;
	}

	private static DoubleMatrix getC(List<Sensor> sensors)
	{
		DoubleMatrix C = DoubleMatrix.zeros(4, 1);
		C.put(0, getLinearAlgebraT(sensors.get(1), sensors.get(0)));
		C.put(1, getLinearAlgebraT(sensors.get(2), sensors.get(0)));
		C.put(2, getLinearAlgebraT(sensors.get(3), sensors.get(0)));
		C.put(3, getLinearAlgebraT(sensors.get(4), sensors.get(0)));
		return C;
	}

	/**
	 * 计算C中的值
	 * 
	 * @param sensor1
	 * @param sensor2
	 * @return
	 */
	private static double getLinearAlgebraT(Sensor sensor1, Sensor sensor2)
	{
		return (getT(sensor1) - getT(sensor2)) / 2;
	}

	/**
	 * 通过传感器信息计算c2t2-(x2+y2+z2) 2为幂
	 * 
	 * @param sensor
	 * @return
	 */
	private static double getT(Sensor sensor)
	{
		return Math.pow(Parameters.C, 2) * Math.pow(getTime(sensor), 2) - (Math.pow(sensor.getAltitude(), 2)
				+ Math.pow(sensor.getLatitude(), 2) + Math.pow(sensor.getLongtitude(), 2));

	}

	/**
	 * 得到A矩阵
	 * 
	 * @return
	 */
	private static DoubleMatrix getA(List<Sensor> sensors)
	{
		DoubleMatrix A = DoubleMatrix.zeros(4, 4);
		for (int i = 0; i < 4; i++)
		{
			A.putRow(i, getRow(i + 1, sensors));
		}
		return A;
	}

	/**
	 * 
	 * @param i
	 * @param sensors
	 * @return
	 */
	private static DoubleMatrix getRow(int i, List<Sensor> sensors)
	{
		DoubleMatrix v = DoubleMatrix.zeros(1, 4);
		v.put(0, Math.pow(Parameters.C, 2) * (getTime(sensors.get(i)) - getTime(sensors.get(0))));
		v.put(1, sensors.get(0).getLatitude() - sensors.get(i).getLatitude());
		v.put(2, sensors.get(0).getLongtitude() - sensors.get(i).getLongtitude());
		v.put(3, sensors.get(0).getAltitude() - sensors.get(i).getAltitude());
		return v;
	}

	/**
	 * 计算时间
	 * 
	 * @param sensor
	 *            传递进来的传感器
	 * @return
	 */
	private static int getTime(Sensor sensor)
	{
		// 时间的格式yyMMddhhmmss
		String time = sensor.getTime();
		int hour = Integer.parseInt(time.substring(6, 8));
		int min = Integer.parseInt(time.substring(8, 10));
		int second = Integer.parseInt(time.substring(10, 12));
		return hour * 3600 + min * 60 + second;
	}

	/**
	 * 得到传感器的激发时间
	 * 
	 * @param sensor
	 *            随便一个传感器，目的为了获得年月日
	 * @param time
	 *            单位是秒数
	 * @return
	 */
	private static String getSetTime(Sensor sensor, double inte)
	{
		int time = (int) inte;
		String st1 = sensor.getTime().substring(0, 6);// 年月日
		// 计算时间
		int hour = time / 3600;
		String str2 = (hour / 10) > 0 ? hour + "" : "0" + hour;

		int min = (time % 3600) / 60;
		String str3 = (min / 10) > 0 ? min + "" : "0" + min;

		int sec = time - hour * 3600 - min * 60;
		String str4 = (sec / 10) > 0 ? sec + "" : "0" + hour;

		return st1 + str2 + str3 + str4;
	}

}
