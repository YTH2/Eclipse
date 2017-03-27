/**
 * @author 韩百硕
 * 
 * 给激发的传感器计算最大振幅
 */
package com.h2.magnitude;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class MaxFudu
{
	/**
	 * 给激发的传感器计算最大的振幅
	 * 
	 * @param Token
	 *            所有的传感器
	 */
	public static Sensor[] getMaxFudu(Sensor[] Token)
	{
		for (Sensor sensor : Token)
		{
			if (sensor.isSign())
			{
				SensorMaxFudu(sensor);
			}
		}
		return Token;
	}

	/**
	 * 计算激发传感器的最大振幅
	 * 
	 * @param sen
	 *            激发的传感器
	 */
	private static void SensorMaxFudu(Sensor sen)
	{
		// 1：先扫一遍10秒的数据，确定用哪一个通道,顺便确定通道的最大值
		boolean flag = getFlag(sen);
		// 2：获取b的时间
		Sensor token = getBTime(sen, flag);
		// 3：计算最大振幅,并将结果存入传感器
		sen.setFudu(getMaxA(token, flag));
	}

	/**
	 * 确定读取哪一个通道. 先扫一遍10秒的数据，确定用哪一个通道,顺便确定通道的最大值
	 * 
	 * @param sen
	 *            激发的传感器
	 * @return 标志，f表示4 5通道，t表示1 2通道
	 */
	private static boolean getFlag(Sensor sen)
	{
		boolean flag = false;// 标识
		String s = null;// 存储中间值
		String[] inte = new String[7];
		// 存储1245通道的最大值
		double max1 = 0;
		double max2 = 0;
		double max4 = 0;
		double max5 = 0;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(sen.getDataFile())));
			while ((s = reader.readLine()) != null)
			{
				inte = s.split(" ");
				if (!flag)
				{
					if (testValue(inte[4]) || testValue(inte[3]))
					{
						flag = true;
					}
				}
				max1 = (Math.abs(max1) > Math.abs(Integer.parseInt(inte[0]))) ? max1 : Integer.parseInt(inte[0]);
				max2 = (Math.abs(max2) > Math.abs(Integer.parseInt(inte[1]))) ? max2 : Integer.parseInt(inte[1]);
				max4 = (Math.abs(max4) > Math.abs(Integer.parseInt(inte[3]))) ? max4 : Integer.parseInt(inte[3]);
				max5 = (Math.abs(max5) > Math.abs(Integer.parseInt(inte[4]))) ? max5 : Integer.parseInt(inte[4]);
			}
			reader.close();
			sen.setMax1(max1);
			sen.setMax2(max2);
			sen.setMax4(max4);
			sen.setMax5(max5);
		} catch (Exception e)
		{
			System.out.println("MaxFudu类读取数据文件失败！");
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 计算最大值后面第一个数值为0的点的时间
	 * 
	 * @param sen
	 *            激发的传感器
	 * @param flag
	 *            标识选择的通道是45还是12
	 * @return 记录数
	 */
	private static Sensor getBTime(Sensor sen, boolean flag)
	{
		if (!flag)// 选择45通道
		{
			double An = getRecordCount(sen, 5) / (double) Parameters.FREQUENCY;
			sen.setBn(An);
			double Ae = getRecordCount(sen, 4) / (double) Parameters.FREQUENCY;
			sen.setBe(Ae);
		} else// 选择12通道
		{
			double An = getRecordCount(sen, 2) / (double) Parameters.FREQUENCY;
			sen.setBn(An);
			double Ae = getRecordCount(sen, 1) / (double) Parameters.FREQUENCY;
			sen.setBe(Ae);
		}
		return sen;
	}

	/**
	 * 计算传感器的最大振幅
	 * 
	 * @param sen
	 *            激发的传感器
	 * @param flag
	 *            标志是否换通道
	 * @param btime
	 *            最大值到值为0的记录之间的记录数
	 * @return 激发传感器的最大振幅
	 */
	private static double getMaxA(Sensor sen, boolean flag)
	{
		int num = 0;// 除数
		double An = 0;
		double Ae = 0;
		if (flag)// 激发，12通道，除数是16
		{
			num = 16;
			An = (sen.getMax2() * 0.0000238 * Math.sin(2 * Math.PI * sen.getBn() / (4 * sen.getBn()) + Math.PI / 2)
					+ sen.getMax2() * 0.0000238 * Math.sin(Math.PI / 2)) * sen.getBn() / 2;
			Ae = (sen.getMax1() * 0.0000238 * Math.sin(2 * Math.PI * sen.getBe() / (4 * sen.getBe()) + Math.PI / 2)
					+ sen.getMax1() * 0.0000238 * Math.sin(Math.PI / 2)) * sen.getBe() / 2;

		} else// 45通道，除数是64
		{
			num = 64;
			An = (sen.getMax5() * 0.0000238 * Math.sin(2 * Math.PI * sen.getBn() / (4 * sen.getBn()) + Math.PI / 2)
					+ sen.getMax5() * 0.0000238 * Math.sin(Math.PI / 2)) * sen.getBn() / 2;
			Ae = (sen.getMax4() * 0.0000238 * Math.sin(2 * Math.PI * sen.getBe() / (4 * sen.getBe()) + Math.PI / 2)
					+ sen.getMax4() * 0.0000238 * Math.sin(Math.PI / 2)) * sen.getBe() / 2;
		}
		return (Ae + An) / (num * 2);
	}

	/**
	 * 返回最大值到0值之间的记录数
	 * 
	 * @param sen
	 *            激发传感器
	 * @param channel
	 *            第几通道
	 * @return 记录数
	 */
	private static double getRecordCount(Sensor sen, int channel)
	{
		int recordNumber = 0;
		String s = null;
		String[] inte = new String[7];
		// 1：先取指定channel的最大值
		double Max = 0;
		switch (channel)
		{
		case 1:
			Max = sen.getMax1();
			break;
		case 2:
			Max = sen.getMax2();
			break;
		case 4:
			Max = sen.getMax4();
			break;
		case 5:
			Max = sen.getMax5();
			break;
		}
		// 2：定位到最大值的那条记录
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(sen.getDataFile())));
			while ((s = reader.readLine()) != null)
			{
				inte = s.split(" ");
				if ((int) Max == Integer.parseInt(inte[channel - 1]))
				{
					while ((s = reader.readLine()) != null)
					{
						inte = s.split(" ");
						if (Integer.parseInt(inte[channel - 1]) == 0)
						{
							break;
						}
						recordNumber++;
					}
				}
			}
			reader.close();
		} catch (Exception e)
		{
			System.out.println("震级计算中getRecordCount函数失败！");
		}
		return recordNumber;
	}

	/**
	 * 计算是否超出界限
	 * 
	 * @param s
	 *            可以转换为int的string
	 * @return 是否越界
	 */
	private static boolean testValue(String s)
	{
		int a = Integer.parseInt(s);
		if (a > Parameters.HEAD || a < Parameters.TAIL)
		{
			return true;
		} else
		{
			return false;
		}
	}
}
