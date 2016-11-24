package com.h2.constant;

public class Sensor
{
	public Sensor()
	{
		this.sign = false;
		this.time = "000000";
		this.fudu = 0;

		this.Altitude = 0;
		this.Longtitude = 0;
		this.Latitude = 0;

		this.DataFile = "";
		this.GPSFile = "";
		this.OutPutfile = "";
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

	public void setTime(String time)
	{
		this.time = time;
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

	public int getFudu()
	{
		return fudu;
	}

	public void setFudu(int fudu)
	{
		this.fudu = fudu;
	}

	private boolean sign;// 标识是否被激发
	private String time;// 激发的时间
	private int fudu;// 最大振幅

	private double Longtitude;// 经线
	private double Latitude;// 纬线
	private double Altitude;// 海拔

	private String GPSFile;// GPS文件位置
	private String DataFile;// 数据文件位置
	private String OutPutfile;// 数据输出位置

}
