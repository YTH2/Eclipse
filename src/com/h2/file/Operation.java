/**
 * @author 韩百硕
 */
package com.h2.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class Operation
{
	/**
	 * 获取探测器的gps
	 * 
	 * @param path
	 *            GPS文件的完整路径
	 * @return
	 * @throws IOException
	 */
	public static String getGPS(String path)
	{
		String inteString = null;// 存储GPS信息
		String result = null;// 最终的结果
		try
		{
			BufferedReader read = new BufferedReader(new FileReader(new File(path)));
			while ((inteString = read.readLine()) != null)
			{
				String[] str = StringUtils.split(inteString, " ");
				if (str.length == 8)
				{
					result = str[5] + " " + str[6] + " " + str[7];// 经度维度海拔
				}
			}
			read.close();
		} catch (Exception e)
		{
			System.out.println(path + "----GPS文件数据读取失败！");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 输出数据，以追加的方式输出震源信息
	 * 
	 * @param path
	 *            震源数据输出路径
	 * @param sensor
	 *            震源数据存储单元
	 * @param earthquake
	 *            震级
	 */
	public static void outputData(String path, Sensor sensor, double earthquake)
	{
		// 查看输出文件是否存在
		File fi = new File(path);
		BufferedReader reader = null;// 先读出数据，然后写入数据，保证有十条数据
		FileWriter writer = null;
		Vector<String> record = new Vector<String>();

		try
		{
			if (!fi.exists())
			{
				fi.createNewFile();
				writer = new FileWriter(path, true);
				for (int i = 0; i < Parameters.RECORDNUM; i++)
				{
					writer.write("000000000000 0.0 00000.0000 0000.0000 000.0\n");
				}
				writer.close();
			}
		} catch (Exception e)
		{
			System.out.println("震源位置文件创建失败！");
		}

		try
		{
			reader = new BufferedReader(new FileReader(fi));

			if (reader.readLine() != null)
			{
				// 删除第一条数据然后更新十条数据
				for (int i = 1; i < Parameters.RECORDNUM; i++)
				{
					record.add(reader.readLine());
				}
				writer = new FileWriter(path, true);
				for (int i = 0; i < Parameters.RECORDNUM - 1; i++)
				{
					writer.write(record.get(i));
				}
				writer.write(sensor.getTime() + " " + earthquake + " " + sensor.getLongtitude() + " "
						+ sensor.getLatitude() + " " + sensor.getAltitude());
			}

		} catch (IOException e)
		{
			System.out.println("输出震源数据失败！");
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

	/**
	 * 将激发传感器的数据进行存储
	 * 
	 * @param sensors
	 */
	public static void saveData(Sensor[] sensors)
	{
		for (int i = 0; i < sensors.length; i++)
		{
			if (sensors[i].isSign())
			{
				outputEarthData(sensors[i], i + 1);
			}
		}
	}

	/**
	 * 将本块数据输出
	 * 
	 * @param sensor
	 *            传感器的信息
	 * @param i
	 *            用于说明传感器错误提示
	 */
	private static void outputEarthData(Sensor sensor, int i)
	{
		BufferedWriter writer = null;
		BufferedReader reader = null;

		String s;// 存储中间结果，作为缓存
		File fi = new File(sensor.getOutPutfile());
		try
		{
			if (!fi.exists())
			{
				fi.createNewFile();
			}
		} catch (Exception e)
		{
			System.out.println("传感器-" + i + "-激发数据存储失败！");
		}
		try
		{
			reader = new BufferedReader(new FileReader(new File(sensor.getDataFile())));
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new BufferedWriter(new FileWriter(fi, true));

			writer.write("---------------------------------------------------------\n");
			writer.write("传感器" + i + "的激发时间:    " + sensor.getTime() + "\n");// 传感器的激发时间
			writer.write("传感器" + i + "的最大振幅:    " + sensor.getFudu() + "\n");// 最大振幅
			writer.write("---------------------------------------------------------\n");
			while ((s = reader.readLine()) != null)
			{
				writer.write(s + "\n");
			}

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
				if (reader != null)
				{
					reader.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
