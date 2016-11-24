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
	 * @return
	 */
	public static Sensor getXYZ(List<Sensor> sensors)
	{
		Sensor sensor = new Sensor();

		DoubleMatrix B = DoubleMatrix.zeros(4, 1);

		DoubleMatrix A = getA(sensors);

		DoubleMatrix C = getC(sensors);
		B = Solve.pinv(A).mmul(C);
		// 通过B给sensor赋值
		sensor.setTime((long) B.get(0));
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
		return Math.pow(Parameters.C, 2) * Math.pow(sensor.getTime(), 2) - (Math.pow(sensor.getAltitude(), 2)
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

	private static DoubleMatrix getRow(int i, List<Sensor> sensors)
	{
		DoubleMatrix v = DoubleMatrix.zeros(1, 4);
		v.put(0, Math.pow(Parameters.C, 2) * getTimeDiff(sensors.get(i).getTime(), sensors.get(0).getTime()));
		v.put(1, sensors.get(0).getLatitude() - sensors.get(i).getLatitude());
		v.put(2, sensors.get(0).getLongtitude() - sensors.get(i).getLongtitude());
		v.put(3, sensors.get(0).getAltitude() - sensors.get(i).getAltitude());
		return v;
	}

	private static double getTimeDiff(long t1, long t2)
	{
		int[] h1 = Tools.getTime(t1);
		int[] h2 = Tools.getTime(t2);
		String str = (h2[0] - h1[0]) * 3600 + "" + (h2[1] - h1[1]) * 60 + "" + (h2[2] - h1[2]);
		return Double.parseDouble(str);
	}
}
