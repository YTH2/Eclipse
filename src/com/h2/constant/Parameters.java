/**
 * @author 韩百硕
 * 本程序用到的常量
 */
package com.h2.constant;

public class Parameters
{
	private static final int LONGTIMEWINDOW = 50;// 单位是毫秒
	private static final int SHORTTIMEWINDOW = 10;// 单位是毫秒
	private static final int FREQUENCY = 10000;// 单位hz/s
	private static final int TEMP = 1000;// 单位转换
	// head和tail是转换通道时的阈值
	public static final int HEAD = 32767;
	public static final int TAIL = -32768;
	/**
	 * 结果数据文件中的记录数
	 */
	public static final int RECORDNUM = 10;
	/**
	 * 长時窗采样点个数
	 */
	public static final int N1 = LONGTIMEWINDOW * FREQUENCY / TEMP;
	/**
	 * 短時窗采样点个数
	 */
	public static final int N2 = SHORTTIMEWINDOW * FREQUENCY / TEMP;
	/**
	 * (一个长時窗+一个短时窗)時窗采样点总个数
	 */
	public static final int N = (LONGTIMEWINDOW + SHORTTIMEWINDOW) * FREQUENCY / TEMP;
	/**
	 * 10s内時窗的个数
	 */
	public static final int WINDOWNUMBER = 167;
	/**
	 * 第六步中常量
	 */
	public static final int C = 5500;
	/**
	 * 传感器的总数量
	 */
	public static final int SensorNum = 5;
	public static final int RULENum = 5;// 确定的读取数量 Cmn中的n 至少有五个传感器的数据才能进行定位
	/**
	 * 读取数据文件的路径，这里只是目录
	 */
	public static final String[] DATAPATH = { "D://Data//Sensor1//", "D://Data//Sensor2//", "D://Data//Sensor3//",
			"D://Data//Sensor4//", "D://Data//Sensor5//" };// 每个传感器的探测数据来自不同的文件
	/**
	 * 读取GPS文件的路径
	 */
	public static final String[] GPSPATH = { "D://Data//Sensor1//GPS.txt", "D://Data//Sensor2//GPS.txt",
			"D://Data//Sensor3//GPS.txt", "D://Data//Sensor4//GPS.txt", "D://Data//Sensor5//GPS.txt" };// 每个传感器的位置信息来自不同的文件
	/**
	 * 地震备份数据输出路径
	 */
	public static final String[] EARTHDATAFILE = { "D://Data//Sensor1//Backup.txt", "D://Data//Sensor2//Backup.txt",
			"D://Data//Sensor3//Backup.txt", "D://Data//Sensor4//Backup.txt", "D://Data//Sensor5//Backup.txt" };// 不同的文件存储不同传感器的信息
	/**
	 * 输出计算结果 时间-震级-定位
	 */
	public static final String MINEEARTHQUAKEFILE = "D://Data//Mine.txt";
}
