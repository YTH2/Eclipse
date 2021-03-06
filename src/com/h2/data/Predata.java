/**
 * @author 韩百硕
 */
package com.h2.data;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;
import com.h2.file.Operation;
import com.h2.tool.FindFile;

public class Predata
{
	/**
	 * 得到激发传感器的序号，并让其变为数组，方便进行后边组合处理
	 * 
	 * @param num
	 *            激发的传感器的数量
	 * @param arr
	 *            总的传感器的数组
	 * @return 激发传感器的编号string数组
	 */
	public static String[] toArray(int num, Sensor[] arr)
	{
		String[] arrs = new String[num];
		int j = 0;
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].isSign())
			{
				arrs[j] = String.valueOf(i);
				j++;
			}
		}
		return arrs;
	}

	/**
	 * 确定激发的传感器个数
	 * 
	 * @param Token
	 *            总的传感器的数组
	 * @return 激发传感器的数量
	 */
	public static int getCount(Sensor[] Token)
	{
		int count = 0;
		for (Sensor b : Token)
		{
			if (b.isSign())
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * 前边组合生成的字符串，一个组合是一条string，比如“1 2 3”，用于组合计算后的使用
	 * 
	 * @param str
	 *            一条组合的信息，比如“1 2 3”
	 * @return int数组 [1,2,3]
	 */
	public static int[] getSensorsID(String str)
	{
		String[] strs = str.split(" ");
		int[] arr = new int[strs.length];
		for (int i = 0; i < strs.length; i++)
		{
			arr[i] = Integer.parseInt(strs[i]);
		}
		return arr;
	}

	/**
	 * 加载全部传感器的信息
	 * 
	 * @param count
	 *            传感器的总数量
	 * @return 初始化好的传感器，每个传感器都关联了数据文件，gps文件
	 */
	public static Sensor[] loadSensorInfo(int count)
	{
		Sensor[] sensors = new Sensor[count];
		for (int i = 0; i < count; i++)
		{
			sensors[i] = initSensor(i);
		}

		return sensors;
	}

	/**
	 * 加载一个传感器的信息
	 * 
	 * @param i
	 *            传感器的编号
	 * @return 一个初始化好的传感器
	 */
	private static Sensor initSensor(int i)
	{
		Sensor sensor = new Sensor();

		// 确定传感器两种文件位置
		sensor.setDataFile(FindFile.getFileName(Parameters.DATAPATH[i]));
		sensor.setGPSFile(Parameters.GPSPATH[i]);
		sensor.setOutPutfile(Parameters.EARTHDATAFILE[i]);

		// 需要通过读取gps文件获取
		String[] strs = Operation.getGPS(sensor.getGPSFile()).split(" ");
		sensor.setLatitude(Double.parseDouble(strs[1]));
		sensor.setLongtitude(Double.parseDouble(strs[0]));
		sensor.setAltitude(Double.parseDouble(strs[2]));

		return sensor;
	}
}
