/**
 * @author 韩百硕
 * 
 * 获取传感器的数据文件地址
 */
package com.h2.tool;

import java.io.File;

public class FindFile
{
	public static String getFileName(String Path)
	{
		File file = new File(Path);
		if (file.isDirectory())
		{
			File[] paths = file.listFiles();
			for (File fi : paths)
			{
				int end = fi.getName().lastIndexOf(".");
				String name = fi.getName().substring(0, end);
				if (name.matches("[0-9]+"))
				{
					return Path + fi.getName();
				}
			}
		}
		return "";
	}
}
