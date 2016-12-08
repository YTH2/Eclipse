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
		 * 第一步：确定传感器数量
		 */
		Sensor[] Token = Predata.loadSensorInfo(Parameters.SensorNum);

		long i = 0;// 这是第几次进行循环 用于激发时间的计算
		
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
				Operation.saveData(Token);
				Sensor location = Location.getLocation(count, Token);
				Operation.outputLocation(Parameters.EARTHQUAKEFILE, location);
				Earth.outputEarthClass(location, Token, count);
			}

			i++;
			// 重置token
			for (Sensor sensor : Token)
			{
				sensor.setFudu(0);
				sensor.setSign(false);
				sensor.setTime("000000");
			}
		}
	}
}
