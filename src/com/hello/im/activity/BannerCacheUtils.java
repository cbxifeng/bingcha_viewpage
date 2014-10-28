package com.hello.im.activity;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * ��Ϸ��ҳ��BannerͼƬ����
 */
public class BannerCacheUtils {
	private static final BannerCacheUtils instance = new BannerCacheUtils();
	private int hardCachedSize = (int) (Runtime.getRuntime().maxMemory()) / 16;

	private BannerCacheUtils() {
	}

	public static BannerCacheUtils getInstance() {
		return instance;
	}

	private LruCache<String, Bitmap> sHardBitmapCache = new LruCache<String, Bitmap>(
			hardCachedSize) {
		@Override
		public int sizeOf(String key, Bitmap value) {
			return value.getWidth() * value.getHeight();
		}
	};

	public boolean put(String key, Bitmap value) {
		if (value != null && !value.isRecycled()) {
			sHardBitmapCache.put(key, value);

			return true;
		}

		return false;
	}

	public Bitmap get(Activity context, String key, int width, int height) {
		if (key == null || key.equals("")) {
			return null;
		}
		Bitmap bmp = sHardBitmapCache.get(key);
		if (bmp != null && !bmp.isRecycled()) {
			return bmp;
		} else {
			String picName = Constants.getDatabasePath()
					+ Constants.IMAGE_DIRECTORY + key.hashCode();
			File nativeFile = new File(picName);
			if (nativeFile.exists()) {
				bmp = ImageUtils.getBannerBySize(context, picName, width,
						height);
				if (bmp != null && !bmp.isRecycled()) {
					sHardBitmapCache.put(key, bmp);

					return bmp;
				}
			}
		}

		return null;
	}

	public void clear() {
		sHardBitmapCache.evictAll();
	}
}
