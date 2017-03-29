/**
 * @author 韩百硕
 * 给传感器创建一个类
 */
package com.h2.constant;

public class Sensor
{
	public Sensor()
	{
		this.sign = false;
		this.time = "000000000000";
		this.fudu = 0;

		this.Altitude = 0;
		this.Longtitude = 0;
		this.Latitude = 0;

		this.DataFile = "";
		this.GPSFile = "";
		this.OutPutfile = "";
		//用于震级的计算
		Max1 = 0;
		Max2 = 0;
		Max4 = 0;
		Max5 = 0;
		Bn = 0;
		Be = 0;
	}

	@Override
	public String toString()
	{
		return Longtitude + " " + Latitude + " " + Altitude;
	}

	public boolean isSign()
	{
		return sign;
	}

	public void setSign(boolean sign)
	{
		this.sign = sign;
	}

	public String getTime()
	{
		return time;
	}

	/**
	 * 设置传感器的激发时间
	 * 
	 * @param time
	 *            激发的时间，必须是12位长
	 */
	public void setTime(String time)
	{
		if (time.length() == 12)
		{
			this.time = time;
		} else
		{
			System.out.println("激发时间赋值不正确！");
		}

	}

	public double getLongtitude()
	{
		return Longtitude;
	}

	public void setLongtitude(double longtitude)
	{
		Longtitude = longtitude;
	}

	public double getLatitude()
	{
		return Latitude;
	}

	public void setLatitude(double latitude)
	{
		Latitude = latitude;
	}

	public double getAltitude()
	{
		return Altitude;
	}

	public void setAltitude(double altitude)
	{
		Altitude = altitude;
	}

	public String getGPSFile()
	{
		return GPSFile;
	}

	public void setGPSFile(String gPSFile)
	{
		GPSFile = gPSFile;
	}

	public String getDataFile()
	{
		return DataFile;
	}

	public void setDataFile(String dataFile)
	{
		DataFile = dataFile;
	}

	public String getOutPutfile()
	{
		return OutPutfile;
	}

	public void setOutPutfile(String outPutfile)
	{
		OutPutfile = outPutfile;
	}

	public double getFudu()
	{
		return fudu;
	}

	public void setFudu(double fudu)
	{
		this.fudu = fudu;
	}

	public double getMax4()
	{
		return Max4;
	}

	public void setMax4(double max4)
	{
		Max4 = max4;
	}

	public double getMax5()
	{
		return Max5;
	}

	public void setMax5(double max5)
	{
		Max5 = max5;
	}

	public double getMax1()
	{
		return Max1;
	}

	public void setMax1(double max1)
	{
		Max1 = max1;
	}

	public double getMax2()
	{
		return Max2;
	}

	public void setMax2(double max2)
	{
		Max2 = max2;
	}

	public double getBn()
	{
		return Bn;
	}

	public void setBn(double bn)
	{
		Bn = bn;
	}

	public double getBe()
	{
		return Be;
	}

	public void setBe(double be)
	{
		Be = be;
	}

	/**
	 * 标识是否被激发
	 */
	private boolean sign;//
	/**
	 * 激发的时间，时间12位格式为yyMMddhhmmss
	 */
	private String time;//
	/**
	 * 传感器最大振幅，用于震级计算
	 */
	private double fudu;//
	/**
	 * 经线
	 */
	private double Longtitude;//
	/**
	 * 纬线
	 */
	private double Latitude;//
	/**
	 * 海拔
	 */
	private double Altitude;//
	/**
	 * GPS文件位置
	 */
	private String GPSFile;//
	/**
	 * 数据文件位置
	 */
	private String DataFile;//
	/**
	 * 数据输出位置
	 */
	private String OutPutfile;//

	// 最大震级公式修改后增加的字段
	/**
	 * 通道1的最大值
	 */
	private double Max1;
	/**
	 * 通道2的最大值
	 */
	private double Max2;
	/**
	 * 通道4的最大值
	 */
	private double Max4;
	/**
	 * 通道5的最大值
	 */
	private double Max5;
	/**
	 * An中的记录数，用于震级计算中获取时间
	 */
	private double Bn;
	/**
	 * Ae中的记录数，用于震级计算中获取时间
	 */
	private double Be;
}
