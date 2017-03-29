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
	/**
	 * 保存传感器前一个10秒数据中的后5秒数据作为虚窗口数据
	 * 
	 * @param token
	 *            所有的传感器
	 */
	public static void saveData(Sensor[] token)
	{
		String s = null;//中间缓存
		int count;
		BufferedReader reader;
		BufferedWriter writer;
		for (int i = 0; i < token.length; i++)
		{
			count = Parameters.COUNT5sRECORD;
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
				reader = new BufferedReader(new FileReader(new File(token[i].getDataFile())));
				//确保虚窗口中没有数据
				FileWriter writer1 = new FileWriter(fi);
				writer1.write("");
				writer1.close();
				
				writer = new BufferedWriter(new FileWriter(fi, true));
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
				// 虚窗口文件写入失败
				System.out.println("虚窗口文件写入失败!");
			}

		}
	}
}
