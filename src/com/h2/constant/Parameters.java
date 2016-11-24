package com.h2.constant;

public class Parameters
{
	private static final int LONGTIMEWINDOW = 50;// 单位是毫秒
	private static final int SHORTTIMEWINDOW = 10;// 单位是毫秒
	private static final int FREQUENCY = 10000;// 单位hz/s
	private static final int TEMP = 1000;// 单位转换
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

	public static final int CHANNEL = 4;// 通道数
	/**
	 * 10s内時窗的个数
	 */
	public static final int WINDOWNUMBER = 167;

	public static final int C = 5500;// 第六步中常量

	public static final int SensorNum = 5;// 传感器总数量
	public static final int RULENum = 5;// 确定的读取数量 Cmn中的n 至少有五个传感器的数据才能进行定位
	/**
	 * 读取数据文件的路径，这里只是目录
	 */
	public static final String[] DATAPATH = { "D://Data//Sensor1//", "D://Data//Sensor2//", "D://Data//Sensor3//",
			"D://Data//Sensor4//", "D://Data//Sensor5//" };// 每个传感器的探测数据来自不同的文件
	/**
	 * 读取GPS文件的路径
	 */
	public static final String[] GPSPATH = { "D://Data//Sensor1//Gps.txt", "D://Data//Sensor2//Gps.txt",
			"D://Data//Sensor3//Gps.txt", "D://Data//Sensor4//Gps.txt", "D://Data//Sensor5//Gps.txt" };// 每个传感器的位置信息来自不同的文件
	/**
	 * 地震数据输出路径
	 */
	public static final String[] EARTHDATAFILE = { "D://Data//Sensor1//Backup.txt", "D://Data//Sensor2//Backup.txt",
			"D://Data//Sensor3//Backup.txt", "D://Data//Sensor4//Backup.txt", "D://Data//Sensor5//Backup.txt" };// 不同的文件存储不同传感器的信息
	/**
	 * 震源信息输出路径
	 */
	public static final String EARTHQUAKEFILE = "D://Data//Location.txt";// 显示数据时要显示位置信息，此文件是固定的
	/**
	 * 震级数据输出路径
	 */
	public static final String EARTHClassFILE = "D://Data//Earth.txt";
}
