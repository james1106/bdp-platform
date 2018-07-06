package com.bdp.schedule.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String format(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null) {
			return null;
		}
		return format.format(date);
	}
}
