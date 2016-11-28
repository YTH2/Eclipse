package com.h2.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class Operation
{
	/**
	 * 读取探测器的gps
	 * 
	 * @param path
	 *            文件的完整路径
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
					result = handle(str[5]) + " " + handle(str[6]) + " " + str[7];// 经度维度海拔
				}
			}
			read.close();
		} catch (Exception e)
		{
			System.out.println("GPS文件数据读取失败！");
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
	 */
	public static void outputLocation(String path, Sensor sensor)
	{
		// 查看输出文件是否存在
		File fi = new File(path);
		try
		{
			if (!fi.exists())
			{
				fi.createNewFile();
			}
		} catch (Exception e)
		{
			System.out.println("震源位置文件创建失败！");
		}

		FileWriter writer = null;
		try
		{
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(path, true);
			writer.write(sensor.toString() + "\n");
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
				outputEarthData(sensors[i]);
			}
		}
	}

	/**
	 * 以追加的方式输出激发传感器前后5s内的数据
	 * 
	 * @param path
	 */
	private static void outputEarthData(Sensor sensor)
	{
		BufferedWriter writer = null;
		BufferedReader reader = null;
		int count = Parameters.N * Parameters.WINDOWNUMBER;
		long reduCount = 0;
		String s;
		File fi = new File(sensor.getOutPutfile());
		try
		{
			if (!fi.exists())
			{
				fi.createNewFile();
			}
		} catch (Exception e)
		{
			System.out.println("激发数据存储失败！");
		}
		// 传感器激发时间与文件数据记录时间之差
		int dif = getTimeDif10s(sensor.getDataFile(), sensor.getTime());
		if (dif <= 5)
		{// 从文件的头开始读取数据
			try
			{
				reader = new BufferedReader(new FileReader(new File(sensor.getDataFile())));
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				writer = new BufferedWriter(new FileWriter(new File(sensor.getOutPutfile()), true));
				writer.write("---------------------------------------------------------\n");
				writer.write("传感器的激发时间:    " + sensor.getTime() + "\n");// 传感器的激发时间
				writer.write("---------------------------------------------------------\n");
				while (count > 0 && ((s = reader.readLine()) != null))
				{
					writer.write(s + "\n");
					count--;
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

		} else
		{// 跳过数据到指定位置进行数据读取
			reduCount = (dif - 5) * 10000;
			try
			{
				reader = new BufferedReader(new FileReader(new File(sensor.getDataFile())));
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				while (reduCount > 0 && ((s = reader.readLine()) != null))
				{
					reduCount--;
				}
				writer = new BufferedWriter(new FileWriter(new File(sensor.getOutPutfile()), true));
				writer.write("---------------------------------------------------------\n");
				writer.write("传感器的激发时间:    " + sensor.getTime() + "\n");// 传感器的激发时间
				writer.write("---------------------------------------------------------\n");
				while (count > 0 && ((s = reader.readLine()) != null))
				{
					writer.write(s + "\n");
					count--;
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

	/**
	 * 返回两个时间之间的差值 单位是1s
	 * 
	 * @param str1
	 *            数据文件名中的时间
	 * @param str2
	 *            激发时间
	 * @return
	 */
	private static int getTimeDif10s(String str1, String str2)
	{

		Calendar cal1 = GregorianCalendar.getInstance();// 数据文件的时间
		Calendar cal2 = GregorianCalendar.getInstance();// 激发时间

		DateFormat df = new SimpleDateFormat("hhmmss");// 格式化时间

		int begin = str1.lastIndexOf("/");
		int end = str1.lastIndexOf(".");
		str1 = str1.substring(begin + 7, end);

		try
		{
			cal1.setTime(df.parse(str1));
			cal2.setTime(df.parse(str2));
		} catch (ParseException e)
		{
			System.out.println("Operation类------------时间转换错误！");
			e.printStackTrace();
		}
		return (cal2.get(Calendar.HOUR_OF_DAY) - cal1.get(Calendar.HOUR_OF_DAY)) * 3600
				+ (cal2.get(Calendar.MINUTE) - cal1.get(Calendar.MINUTE)) * 60
				+ (cal2.get(Calendar.SECOND) - cal1.get(Calendar.SECOND));

	}

	/**
	 * 矫正经纬度
	 * 
	 * @param str
	 * @return
	 */
	private static String handle(String str)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(Double.parseDouble(str) / 100);
	}

}
