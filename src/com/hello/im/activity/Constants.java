package com.hello.im.activity;

import java.io.File;

import android.os.Environment;

public class Constants {
	// ��Ŀ¼
	public static final String SD_DIRECTORY = "/yyjoy";
	public static final String APP_DIRECTORY = "/wanzhu";
	public static final String IMAGE_DIRECTORY = APP_DIRECTORY + "/image";
	public static final String LOG_DIRECTORY = APP_DIRECTORY + "/log";

	public static final String PHOTO_NAME = "photo.jpg";

	public static final int NORMAL_DYNAMIC_INVISIBLE_WIDTH = 150;
	public static final int RECRUIT_DYNAMIC_INVISIBLE_WIDTH = 150;
	public static final int EXPECT_RECRUIT_DYNAMIC_INVISIBLE_WIDTH = 180;

	/**
	 * @return path
	 */
	public static final String getDatabasePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ SD_DIRECTORY;
	}

	/**
	 */
	public static final void initDirectory() {
		(new File(Constants.getDatabasePath())).mkdirs();
		(new File(Constants.getDatabasePath() + Constants.APP_DIRECTORY))
				.mkdirs();
		(new File(Constants.getDatabasePath() + Constants.IMAGE_DIRECTORY))
				.mkdirs();
		(new File(Constants.getDatabasePath() + Constants.LOG_DIRECTORY))
				.mkdirs();
	}
}
