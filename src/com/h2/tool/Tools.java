/**
 * @author 韩百硕
 * 确定传感器激发以及组合的工具类
 */
package com.h2.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class Tools
{
	/**
	 * 确定一个传感器在60ms内是否被激发 思想：将一个时窗内的数据(600条数据)全部读取到vector中，
	 * 并在读取的时候判断是否有数据超出阈值，跟据读取的结果给channel赋值。
	 * 
	 * @param sensor
	 *            传感器
	 * @param number
	 *            时间窗标识
	 * @return 是否被激发
	 */
	public static boolean getToken(Sensor sensor, int number)
	{
		File file = new File(sensor.getDataFile());// 定义数据文件的位置

		String s = null;// 存储中间临时值,读取数据文件中的一条数据并保存在s中。
		int[] iStr = new int[6]; // 存储记录切割值，因为后边用到分割s的值
		boolean channel = false;// 确定读取那三列数据，f表示读取345列，如果为t则读取123列
		int inte = -1;// 存储平均值

		Vector<String> container = new Vector<String>();// 存储当前时窗的数据

		int count = Parameters.N;

		long sumLong = 0;
		long sumShort = 0;

		double aveLong = 0;
		double aveShort = 0;

		long lineNumber = number * Parameters.N;// 确定跳过的记录数，记录数取决于时窗

		try
		{
			// 跳过前边时窗内的数据
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (lineNumber > 0 && ((s = br.readLine()) != null))
			{
				lineNumber--;
			}

			container.clear();
			// 读取本时窗的数据
			while (count > 0 && ((s = br.readLine()) != null))
			{
				String[] str = StringUtils.split(s, " ");
				for (int i = 3; i < 6; i++)// 共7列数据，最后一列为时间，程序中用不到，只用前边的6列
				{
					iStr[i] = Integer.parseInt(str[i]);
				}

				if (!TestThreshold(iStr[3]) && !TestThreshold(iStr[4]) && !TestThreshold(iStr[5]))
				{
					container.add(s);
				} else
				{
					channel = true;
					container.add(s);
				}
				count--;
			}
			if (channel)// 读取前三列
			{
				for (int i = 0; i < Parameters.N1; i++)
				{
					String[] str = StringUtils.split(container.get(i), " ");
					inte = (Math.abs(Integer.parseInt(str[0])) + Math.abs(Integer.parseInt(str[1]))
							+ Math.abs(Integer.parseInt(str[2]))) / 3;
					sumLong += inte;
				}
			} else
			{
				for (int i = 0; i < Parameters.N1; i++)
				{
					String[] str = StringUtils.split(container.get(i), " ");
					inte = (Math.abs(Integer.parseInt(str[3])) + Math.abs(Integer.parseInt(str[4]))
							+ Math.abs(Integer.parseInt(str[5]))) / 3;
					sumLong += inte;
				}
			}
			aveLong = (double) sumLong / Parameters.N1;

			if (channel)// 读取前三列
			{
				for (int i = Parameters.N1; i < Parameters.N; i++)
				{
					String[] str = StringUtils.split(container.get(i), " ");
					inte = (Math.abs(Integer.parseInt(str[0])) + Math.abs(Integer.parseInt(str[1]))
							+ Math.abs(Integer.parseInt(str[2]))) / 3;
					sumShort += inte;
				}
			} else
			{
				for (int i = Parameters.N1; i < Parameters.N; i++)
				{
					String[] str = StringUtils.split(container.get(i), " ");
					inte = (Math.abs(Integer.parseInt(str[3])) + Math.abs(Integer.parseInt(str[4]))
							+ Math.abs(Integer.parseInt(str[5]))) / 3;
					sumShort += inte;
				}
			}
			aveShort = (double) sumShort / Parameters.N2;

			br.close();
		} catch (Exception e)
		{
			System.out.println("读取数据文件" + sensor.getDataFile() + "失败！");
		}
		return (aveShort / aveLong) >= 1.4 ? true : false;
	}

	/**
	 * 组合C
	 * 
	 * @param a
	 *            所有的激活传感器 M
	 * @param num
	 *            选取的传感器 N
	 * @return 组合，用string表示
	 */
	public static List<String> combine(String[] a, int num)
	{
		List<String> list = new ArrayList<String>();

		StringBuffer sb = new StringBuffer();

		String[] b = new String[a.length];
		for (int i = 0; i < b.length; i++)
		{
			if (i < num)
			{
				b[i] = "1";
			} else
				b[i] = "0";
		}

		int point = 0;
		int nextPoint = 0;
		int count = 0;
		int sum = 0;
		String temp = "1";

		while (true)
		{
			// 判断是否全部移位完毕
			for (int i = b.length - 1; i >= b.length - num; i--)
			{
				if (b[i].equals("1"))
					sum += 1;
			}
			// 根据移位生成数据
			for (int i = 0; i < b.length; i++)
			{
				if (b[i].equals("1"))
				{
					point = i;
					sb.append(a[point]);
					sb.append(" ");
					count++;
					if (count == num)
						break;
				}
			}
			// 往返回值列表添加数据
			list.add(sb.toString());

			// 当数组的最后num位全部为1 退出
			if (sum == num)
			{
				break;
			}
			sum = 0;

			// 修改从左往右第一个10变成01
			for (int i = 0; i < b.length - 1; i++)
			{
				if (b[i].equals("1") && b[i + 1].equals("0"))
				{
					point = i;
					nextPoint = i + 1;
					b[point] = "0";
					b[nextPoint] = "1";
					break;
				}
			}
			// 将 i-point个元素的1往前移动 0往后移动
			for (int i = 0; i < point - 1; i++)
				for (int j = i; j < point - 1; j++)
				{
					if (b[i].equals("0"))
					{
						temp = b[i];
						b[i] = b[j + 1];
						b[j + 1] = temp;
					}
				}
			// 清空 StringBuffer
			sb.setLength(0);
			count = 0;
		}
		return list;
	}

	/**
	 * 计算通道转换
	 * 
	 * @param num
	 *            传进来的值
	 * @return 通道是否转换
	 */
	private static boolean TestThreshold(int num)
	{
		if (num > Parameters.HEAD || num < Parameters.TAIL)
		{
			return true;
		} else
		{
			return false;
		}

	}
}
