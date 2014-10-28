package com.hello.im.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {
	public static int pixToDip(Context context, int value) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return (int) (value / dm.density + 0.5f);
	}

	public static float pixelToDp(Context context, float val) {
		float density = context.getResources().getDisplayMetrics().density;
		return val * density;
	}

	public static long getTimeStamp(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(time);

			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();

			return 0;
		}
	}

	public static int[] getScreenSize(Activity context) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		return new int[] { width, height };
	}
}
