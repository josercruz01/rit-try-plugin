package com.plugin.tryplugin.core.app;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeService {
	public static String textTimeStamp(){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss_SSS");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
