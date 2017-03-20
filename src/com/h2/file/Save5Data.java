/**
 * @author 韩百硕
 * 保存各个传感器5秒的数据
 */
package com.h2.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.h2.constant.Parameters;
import com.h2.constant.Sensor;

public class Save5Data
{
	public static void saveData(Sensor[] token)
	{
		String s = null;
		int count = Parameters.COUNT5sRECORD;
		for (int i = 0; i < token.length; i++)
		{
			// 跳过前五秒的数据，只存储后五秒的数据
			File fi = new File(Parameters.VIRTUALWINDOW[i]);
			if (!fi.exists())
			{
				try
				{
					fi.createNewFile();
				} catch (IOException e)
				{
					System.out.println("虚窗口文件创建失败！");
				}
			}
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(new File(token[i].getDataFile())));
				BufferedWriter writer = new BufferedWriter(new FileWriter(fi, false));
				while ((count > 0) && (s = reader.readLine()) != null)
				{
					count--;
				}
				while ((s = reader.readLine()) != null)
				{
					writer.write(s + "\n");
				}
				reader.close();
				writer.close();
			} catch (Exception e)
			{
				// 欻窗口文件写入失败
				System.out.println("虚窗口文件写入失败!");
			}

		}
	}
}
