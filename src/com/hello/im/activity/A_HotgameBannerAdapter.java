package com.hello.im.activity;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class A_HotgameBannerAdapter extends PagerAdapter implements
		OnTouchListener {
	public static final int DEFAULTTIMEOUT = 10000;

//	private ArrayList<BannerBean> data;
	private Context context;
	private ImageView[] views;
	private int width, height;
	private Handler handler;

	public A_HotgameBannerAdapter(ImageView[] views, Context context, Handler handler) {
//		this.data = data;
		this.context = context;
		this.handler = handler;
		this.views = views;
		int[] size = Utils.getScreenSize((Activity) context);
		width = size[0];
		height = (int) ((float) width
				* (float) ChatActivity.CONSTANTS_HEIGHT / (float) ChatActivity.CONSTANTS_WIDTH);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void destroyItem(View view, int position, Object object) {
		// View itemView = views[position % views.length];
		// ((ViewPager) view).removeView(itemView);
	}

	@Override
	public Object instantiateItem(View view, int position) {

		if (views[position % views.length].getParent() != null) {
			((ViewPager) views[position % views.length].getParent())
					.removeView(views[position % views.length]);
		}

		((ViewPager) view).addView(views[position % views.length]);
//		if (data.size() != 0) {
//			String path = data.get(position % views.length).pic;
//			MyThread task = new MyThread(views[position % views.length], path);
//			task.start();
//			views[position % views.length]
//					.setOnClickListener(new BannerOnClickListener(data
//							.get(position % views.length)));
			views[position % views.length].setOnTouchListener(this);
			view.setOnTouchListener(this);
//		}

		return views[position % views.length];
	}

	/**
	 * banner����¼�������
	 */
	public class BannerOnClickListener implements
			android.view.View.OnClickListener {
		private BannerBean bean;

		public BannerOnClickListener(BannerBean bean) {
			this.bean = bean;
		}

		@Override
		public void onClick(View v) {
//			((ChatActivity) context).circleBanner();
		}
	}

	/**
	 * �첽���񣬼���bannerͼƬ
	 * 
	 */
	class MyThread extends Thread {
		public static final int DIS_BANNER_DEFAULT = 1;// ����Ĭ��ͼƬ
		public static final int DIS_BANNER_RESULT = 2;// �������ص�ͼƬ���

		private ImageView image;
		private String path;
		private Bitmap result;

		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DIS_BANNER_DEFAULT:
					image.setImageResource(R.drawable.banner_default);
					break;
				case DIS_BANNER_RESULT:
					if (result != null) {
						Bitmap bmp = (Bitmap) result;
						image.setImageBitmap(bmp);
					}
					break;
				}
			}
		};

		public MyThread(ImageView image, String path) {
			this.image = image;
			this.path = path;
		}

		@Override
		public void run() {
			Bitmap bmp = BannerCacheUtils.getInstance().get((Activity) context,
					path, width, height);
			if (bmp != null && !bmp.isRecycled()) {
				result = bmp;
				android.os.Message msg = handler.obtainMessage();
				msg.what = MyThread.DIS_BANNER_RESULT;
				msg.sendToTarget();

				return;
			} else {
				android.os.Message msg = handler.obtainMessage();
				msg.what = MyThread.DIS_BANNER_DEFAULT;
				msg.sendToTarget();
			}

			HttpClient httpClient = null;
			HttpGet httpGet = null;
			byte[] result = null;
			try {
				httpGet = new HttpGet(path);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						DEFAULTTIMEOUT);
				HttpConnectionParams.setSoTimeout(httpParams, DEFAULTTIMEOUT);
				httpClient = new DefaultHttpClient(httpParams);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toByteArray(httpResponse.getEntity());
					bmp = BitmapFactory.decodeByteArray(result, 0,
							result.length);
					BufferedOutputStream bos = null;
					try {
						bos = new BufferedOutputStream(new FileOutputStream(
								Constants.getDatabasePath()
										+ Constants.IMAGE_DIRECTORY
										+ path.hashCode()));
						bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
						bos.flush();
						BannerCacheUtils.getInstance().put(
								path,
								ImageUtils.getBannerBySize((Activity) context,
										path, width, height));
						bmp = BannerCacheUtils.getInstance().get(
								(Activity) context, path, width, height);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.result = bmp;
			android.os.Message msg = handler.obtainMessage();
			msg.what = MyThread.DIS_BANNER_RESULT;
			msg.sendToTarget();
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			handler.removeMessages(0);
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			((ChatActivity) context).circleBanner();
		}

		return false;
	}
}
