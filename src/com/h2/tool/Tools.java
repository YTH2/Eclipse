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
	 * 确定一个传感器在60ms内是否被激发
	 * 
	 * @param sensor
	 *            传感器
	 * @param number
	 *            时间窗标识
	 * @return
	 */
	public static boolean getToken(Sensor sensor, int number)
	{
		File file = new File(sensor.getDataFile());// 定义文件的位置
		String s = null;// 存储中间值
		int max = -1;// 存储最大振幅
		Vector<Integer> container = null;// 存储当前时窗的数据

		int countLong = Parameters.N1;
		int countShort = Parameters.N2;

		long sumLong = 0;
		long sumShort = 0;

		double aveLong = 0;
		double aveShort = 0;

		long lineNumber = number * Parameters.N;

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (lineNumber > 0 && ((s = br.readLine()) != null))
			{
				lineNumber--;
			}

			while (countLong > 0 && ((s = br.readLine()) != null))
			{
				String[] str = StringUtils.split(s, " ");
				// TODO 需要确定应该读取哪列信息
				if (Integer.parseInt(str[0]) > max)
				{
					max = Integer.parseInt(str[0]);
				}
				sumLong += Integer.parseInt(str[0]);
				countLong--;
			}
			aveLong = (double) sumLong / Parameters.N1;

			while (countShort > 0 && ((s = br.readLine()) != null))
			{
				String[] str = StringUtils.split(s, " ");
				if (Integer.parseInt(str[0]) > max)
				{
					max = Integer.parseInt(str[0]);
				}
				sumShort += Integer.parseInt(str[0]);
				countShort--;
			}
			aveShort = (double) sumShort / Parameters.N2;

			br.close();
		} catch (Exception e)
		{
			System.out.println("读取数据文件" + sensor.getDataFile() + "失败！");
		}
		sensor.setFudu(max);
		return (aveShort / aveLong) >= 1.4 ? true : false;
	}

	/**
	 * 组合C
	 * 
	 * @param a
	 *            所有的激活传感器 M
	 * @param num
	 *            选取的传感器 N
	 * @return
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
}
