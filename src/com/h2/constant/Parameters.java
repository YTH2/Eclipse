/**
 * @author 韩百硕
 * 本程序用到的常量
 */
package com.h2.constant;

public class Parameters
{
	/**
	 * 长时窗的时长，单位是毫秒
	 */
	private static final int LONGTIMEWINDOW = 50;// 单位是毫秒
	/**
	 * 短时窗的时长，单位是毫秒
	 */
	private static final int SHORTTIMEWINDOW = 10;// 单位是毫秒
	/**
	 * 传感器的采样频率，单位是hz/s，文档中是10k，表示每秒有10000条数据
	 */
	public static final int FREQUENCY = 10000;// 单位hz/s
	/**
	 * 用于单位转换，采样频率是秒，长短时窗的单位是毫秒
	 */
	private static final int TEMP = 1000;// 单位转换
	/**
	 * 用于通道转换中使用的阈值，HEAD表示上限
	 */
	public static final int HEAD = 32767;
	/**
	 * 用于通道转换中使用的阈值，TAIL表示下限
	 */
	public static final int TAIL = -32768;
	/**
	 * 存储虚窗口的数据，虚窗口保存上一个10秒数据中的后5秒数据，这个数用于跳过前边的数据
	 */
	public static final int COUNT5sRECORD = 5 * FREQUENCY;
	/**
	 * D://Data//Mine.txt文件中保存的记录数
	 */
	public static final int RECORDNUM = 10;
	/**
	 * 长时窗采样点个数
	 */
	public static final int N1 = LONGTIMEWINDOW * FREQUENCY / TEMP;
	/**
	 * 短时窗采样点个数
	 */
	public static final int N2 = SHORTTIMEWINDOW * FREQUENCY / TEMP;
	/**
	 * (一个长时窗+一个短时窗)时窗采样点总个数
	 */
	public static final int N = (LONGTIMEWINDOW + SHORTTIMEWINDOW) * FREQUENCY / TEMP;
	/**
	 * 10s内时窗的个数，因为每次计算的数据都是10秒内的数据
	 */
	public static final int WINDOWNUMBER = 167;
	/**
	 * 震源计算中使用的常量
	 */
	public static final int C = 5500;
	/**
	 * 传感器的总数量
	 */
	public static final int SensorNum = 5;
	/**
	 * 激发传感器中选择RULENum个传感器，用于震级公式的计算，这个常量后期不用变，因为只有是这个值才能进行计算
	 */
	public static final int RULENum = 5;// 确定的读取数量 Cmn中的n 至少有五个传感器的数据才能进行定位
	/**
	 * 数据文件的路径，这里只是目录
	 */
	public static final String[] DATAPATH = { "D://Data//Sensor1//", "D://Data//Sensor2//", "D://Data//Sensor3//",
			"D://Data//Sensor4//", "D://Data//Sensor5//" };// 每个传感器的探测数据来自不同的文件
	/**
	 * GPS文件的路径
	 */
	public static final String[] GPSPATH = { "D://Data//Sensor1//GPS.txt", "D://Data//Sensor2//GPS.txt",
			"D://Data//Sensor3//GPS.txt", "D://Data//Sensor4//GPS.txt", "D://Data//Sensor5//GPS.txt" };// 每个传感器的位置信息来自不同的文件
	/**
	 * 地震备份数据输出路径
	 */
	public static final String[] EARTHDATAFILE = { "D://Data//Sensor1//Backup.txt", "D://Data//Sensor2//Backup.txt",
			"D://Data//Sensor3//Backup.txt", "D://Data//Sensor4//Backup.txt", "D://Data//Sensor5//Backup.txt" };// 不同的文件存储不同传感器的信息
	/**
	 * 虚窗口的数据保存路径，5s内的数据
	 */
	public static final String[] VIRTUALWINDOW = { "D://Data//Sensor1//virtual.txt", "D://Data//Sensor2//virtual.txt",
			"D://Data//Sensor3//virtual.txt", "D://Data//Sensor4//virtual.txt", "D://Data//Sensor5//virtual.txt" };
	/**
	 * 程序结果的输出路径
	 */
	public static final String MINEEARTHQUAKEFILE = "D://Data//Mine.txt";
}
