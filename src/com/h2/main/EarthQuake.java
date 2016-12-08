package com.h2.main;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;
import com.h2.data.Predata;
import com.h2.file.Operation;
import com.h2.magnitude.Earth;
import com.h2.tool.Event;
import com.h2.tool.Location;

public class EarthQuake
{
	public static void main(String[] args)
	{
		/**
		 * 第一步：确定传感器数量，初始化传感器，每个传感器会分配 1：经度，纬度，海拔 2：数据文件位置，GPS文件位置，备份数据输出位置
		 * 3：最大幅度（0），激发时间（“000000”），激发标志位为默认（false）
		 */
		Sensor[] Token = Predata.loadSensorInfo(Parameters.SensorNum);

		long i = 0;// 这是第几次进行循环 用于激发时间的计算。 通俗的说就是第几个10s

		while (true)
		{
			/**
			 * 第二步：读取文件中的数据 第三步：计算平均振幅
			 */
			Token = Event.motivate(Token, i);// 确定哪些传感器被激发
			/**
			 * 第四步：激发出现在四个以上的传感器中
			 */
			int count = Predata.getCount(Token);// 在这10s内激发的传感器数
			/**
			 * 第六步：定位震源,sensor里边存储有震源信息
			 */
			if (count > 4)
			{
				// 备份传感器数据
				Operation.saveData(Token);
				// 计算震源的位置
				Sensor location = Location.getLocation(count, Token);
				// 输出震源的位置
				Operation.outputLocation(Parameters.EARTHQUAKEFILE, location);
				// 输出震级
				Earth.outputEarthClass(location, Token, count);
			}
			// 重置token
			for (Sensor sensor : Token)
			{
				sensor.setFudu(0);
				sensor.setSign(false);
				sensor.setTime("000000");
			}
			i++;
		}
	}
}
