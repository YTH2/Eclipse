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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class Operation
{
	/**
	 * 获取传感器的GPS
	 * 
	 * @param path
	 *            GPS文件的完整路径
	 * @return
	 * @throws IOException
	 *             读取文件失败
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
	 * 输出最终结果数据，输出数据之后本次的计算基本完成
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
		FileWriter writer1 = null;// 清空文件
		
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
				//确保文件现在没有数据
				writer1 = new FileWriter(path);
				writer1.write(new String(""));
				writer1.close();
				
				writer = new FileWriter(path, true);
				for (int i = 0; i < Parameters.RECORDNUM - 1; i++)
				{
					writer.write(record.get(i)+"\n");
				}
				writer.write(sensor.getTime() + " " + earthquake + " " + sensor.getLongtitude() + " "
						+ sensor.getLatitude() + " " + sensor.getAltitude() + "\n");
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

	/**
	 * 将激发传感器的数据进行存储,激发时间前后5秒的数据
	 * 
	 * @param sensors
	 *            所有的传感器
	 */
	public static void saveData(Sensor[] sensors)
	{
		for (int i = 0; i < sensors.length; i++)
		{
			if (sensors[i].isSign())
			{
				outputEarthData(sensors[i], i + 1);//备份数据
			}
		}
	}

	/**
	 * 将前后5秒的数据输出
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

			// 1：获取文件的时间
			int begin = sensor.getDataFile().lastIndexOf("/");
			int end = sensor.getDataFile().lastIndexOf(".");
			String dataFileName = sensor.getDataFile().substring(begin + 1, end);

			Calendar calFile = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyMMddhhmmss");
			try
			{
				calFile.setTime(df.parse(dataFileName));
			} catch (ParseException e)
			{
				System.out.println("数据文件时间转换错误！");
				e.printStackTrace();
			}
			// 2：获取激发时间
			Calendar calSign = Calendar.getInstance();
			try
			{
				calSign.setTime(df.parse(sensor.getTime()));
			} catch (ParseException e)
			{
				System.out.println("激发时间转换错误！");
				e.printStackTrace();
			}
			// 3：计算两者的差值
			int diff = (calSign.get(Calendar.MINUTE) - calFile.get(Calendar.MINUTE)) * 60
					+ (calSign.get(Calendar.SECOND) - calFile.get(Calendar.SECOND));
			// 4：存储数据
			if (diff < 5)// 需要从虚窗口中读取数据
			{
				// 跳过虚数据文件的diff秒的数据
				reader = new BufferedReader(new FileReader(new File(Parameters.VIRTUALWINDOW[i - 1])));
				int count2 = diff * Parameters.FREQUENCY;
				while (count2 > 0 && (s = reader.readLine()) != null)
				{
					count2--;
				}
				// 保存虚数据文件中的后(5-diff)秒的数据
				while ((s = reader.readLine()) != null)
				{
					writer.write(s + "\n");
				}
				// 保存前(diff+5)秒内的数据
				reader = new BufferedReader(new FileReader(new File(sensor.getDataFile())));
				int count1 = (diff + 5) * Parameters.FREQUENCY;
				while (count1 > 0 && (s = reader.readLine()) != null)
				{
					writer.write(s + "\n");
					count1--;
				}
				
			} else
			{
				// 跳过前边(diff-5)秒的数据
				int count = (diff - 5) * Parameters.FREQUENCY;
				while (count > 0 && (s = reader.readLine()) != null)
				{
					count--;
				}
				// 后边的全部保存
				while ((s = reader.readLine()) != null)
				{
					writer.write(s + "\n");
				}
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
