/**
 * @author 韩百硕
 */
package com.h2.main;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;
import com.h2.data.Predata;
import com.h2.file.Operation;
import com.h2.file.Save5Data;
import com.h2.magnitude.Earth;
import com.h2.magnitude.MaxFudu;
import com.h2.tool.Event;
import com.h2.tool.Location;

public class EarthQuake
{
	public static void main(String[] args)
	{
		// 用于时间的延迟，若是进行了震级震源的计算，后边延迟时间就少点，若是没有计算则延迟时间多点
		boolean flag;

		while (true)
		{
			long startTime = System.currentTimeMillis();
			// 加载各个传感器的信息
			Sensor[] Token = Predata.loadSensorInfo(Parameters.SensorNum);
			flag = false;
			// 第二步：读取文件中的数据 第三步：计算平均振幅
			Token = Event.motivate(Token);// 确定哪些传感器被激发
			// 第四步：激发出现在四个以上的传感器中
			int count = Predata.getCount(Token);// 在这10s内激发的传感器数
			// 第六步：定位震源,sensor里边存储有震源信息
			if (count > 4)
			{
				flag = true;
				Token = MaxFudu.getMaxFudu(Token);
				// 备份传感器数据
				Operation.saveData(Token);
				// 计算震源的位置
				Sensor location = Location.getLocation(count, Token);
				// 计算激发传感器的最大振幅 MaxFudu.getMaxFudu(Token);
				// 计算震级
				double earthquake = Earth.earthClass(location, Token, count);
				// 输出震源的位置
				Operation.outputData(Parameters.MINEEARTHQUAKEFILE, location, earthquake);
			}
			// 保存5秒的数据
			Save5Data.saveData(Token);
			long endTime = System.currentTimeMillis();
			System.out.println((endTime - startTime) / 1000);
			if (flag)// 延迟时间要少
			{
				try
				{
					Thread.sleep(5000);// 每隔10s计算一次，此处需要修改，因为程序处理时间不知道
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			} else// 没有进行震源震级的计算延迟时间要大点
			{
				try
				{
					Thread.sleep(5000);// 每隔10s计算一次，此处需要修改，因为程序处理时间不知道
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
