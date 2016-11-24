package com.h2.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class Tools
{
	/**
	 * 确定一个传感器在60ms内是否被激发
	 * 
	 * @param path
	 *            文件位置
	 * @param number
	 *            第几次读取文件，定位读取位置
	 * @param count
	 *            第几次循环，一次循环读取10s的数据
	 * @return
	 */
	public static boolean getToken(String path, int number, long count, Sensor sensor)
	{
		File file = new File(path);// 定义文件的位置
		String s = null;
		int max = -1;

		int countLong = Parameters.N1;
		int countShort = Parameters.N2;

		long sumLong = 0;
		long sumShort = 0;

		long aveLong = 0;
		long aveShort = 0;

		long lineNumber = number * Parameters.N + count * Parameters.WINDOWNUMBER * Parameters.N;

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
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
			aveLong = sumLong / Parameters.N1;

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
			aveShort = sumShort / Parameters.N2;

			br.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		sensor.setFudu(max);
		return (double) aveShort / (double) aveLong >= 1.4 ? true : false;
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

	/**
	 * 激发时间是long类型(hhmmss) 转换为时分秒 。LinearAlgebra类中使用
	 * 
	 * @param ti
	 * @return
	 */
	public static int[] getTime(long ti)
	{
		int[] time = { 0, 0, 0 };// 时分秒

		time[0] = (int) (ti / 10000);
		time[1] = (int) ((ti % 10000) / 100);
		time[2] = (int) (ti % 100);

		return time;
	}
}
