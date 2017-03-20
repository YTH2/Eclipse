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
import com.h2.tool.Event;
import com.h2.tool.Location;

public class EarthQuake
{
	public static void main(String[] args)
	{
		// 加载各个传感器的信息
		Sensor[] Token = Predata.loadSensorInfo(Parameters.SensorNum);
		while (true)
		{
			//  第二步：读取文件中的数据 第三步：计算平均振幅
			Token = Event.motivate(Token);// 确定哪些传感器被激发
			//  第四步：激发出现在四个以上的传感器中 
			int count = Predata.getCount(Token);// 在这10s内激发的传感器数
			// 第六步：定位震源,sensor里边存储有震源信息
			if (count > 4)
			{
				// 备份传感器数据
				Operation.saveData(Token);
				// 计算震源的位置
				Sensor location = Location.getLocation(count, Token);
				// 计算震级
				double earthquake = Earth.outputEarthClass(location, Token, count);
				// 输出震源的位置
				Operation.outputData(Parameters.MINEEARTHQUAKEFILE, location, earthquake);
			}
			//保存5秒的数据
			Save5Data.saveData(Token);
			// 重置token
			for (Sensor sensor : Token)
			{
				sensor.setFudu(-1);
				sensor.setSign(false);
				sensor.setTime("000000000000");
			}
			try// TODO 间隔时间需要修改
			{
				Thread.sleep(10000);//每隔10s计算一次，此处需要修改，因为程序处理时间不知道
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
