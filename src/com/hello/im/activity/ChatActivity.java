package com.hello.im.activity;

import java.util.ArrayList;
import java.util.TreeSet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatActivity extends Activity {
	public static final int CONSTANTS_WIDTH = 720;// banner标准宽度
	public static final int CONSTANTS_HEIGHT = 360;// banner标准高度
	public static final int BANNER_CIRCLE_ITEM_TIME = 5000;// Banner
															// Item每隔5s转动一次
	public static final int BANNER_INIT_ITEM = 1000;// Banner初始位置
	private ArrayList<BannerBean> banners;
	private A_HotgameBannerAdapter bannerAdapter;
	private ViewPager bannerPager;
	private ImageView[] indicater = new ImageView[5];
	private ImageView[] bannerPics;
	private ImageView bannerDefault;
	private TextView bannerTitle;
	private Handler bannerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int index = bannerPager.getCurrentItem();
			
			bannerPager.setCurrentItem(index + 1);

			circleBanner();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		Constants.initDirectory();

		initView();
		
		/**
		 * 添加Banner数据
		 */
		ArrayList<BannerBean> loadData = new ArrayList<BannerBean>();
		for (int i=0;i<5;i++) {
			BannerBean bean = new BannerBean();
			bean.pic = "http://f.hiphotos.baidu.com/image/h%3D360/sign=6b79e3fdb4003af352bada66052bc619/b58f8c5494eef01f67831b31e2fe9925bd317daa.jpg";
			bean.position = i;
			loadData.add(bean);
		}
		//数据下载完成
		bannerDefault.setVisibility(View.GONE);
		addBannerData();
	}

	private void initView() {
		banners = new ArrayList<BannerBean>();
		bannerDefault = (ImageView) findViewById(R.id.a_hot_fragment_header_bannerDefault);
		bannerPager = (ViewPager) findViewById(R.id.a_hot_fragment_header_banner);
		bannerTitle = (TextView) findViewById(R.id.a_hot_fragment_header_banner_title);
		indicater[0] = (ImageView) findViewById(R.id.a_hot_fragment_header_dian1);
		indicater[1] = (ImageView) findViewById(R.id.a_hot_fragment_header_dian2);
		indicater[2] = (ImageView) findViewById(R.id.a_hot_fragment_header_dian3);
		indicater[3] = (ImageView) findViewById(R.id.a_hot_fragment_header_dian4);
		indicater[4] = (ImageView) findViewById(R.id.a_hot_fragment_header_dian5);
		int screenWidth = Utils.getScreenSize(this)[0];
		int height = (int) ((float) screenWidth * (float) CONSTANTS_HEIGHT / (float) CONSTANTS_WIDTH);
		/**
		 * 定义Banner容器的布局
		 */
		RelativeLayout.LayoutParams bannerParams = new RelativeLayout.LayoutParams(
				screenWidth, height);
		/*
		 * bannerParams.leftMargin = Utils.dipToPix(this, 5);
		 * bannerParams.rightMargin = Utils.dipToPix(this, 5);
		 */
		bannerPager.setLayoutParams(bannerParams);
		/**
		 * Banner默认图片的布局
		 */
		RelativeLayout.LayoutParams bannerDefaultParams = new RelativeLayout.LayoutParams(
				screenWidth, height);
		bannerDefault.setLayoutParams(bannerDefaultParams);
		bannerDefault.setScaleType(ScaleType.CENTER_CROP);
		bannerDefault.setImageResource(R.drawable.banner_default);

		bannerPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					circleBanner();
				}

				return false;
			}
		});
	}

	/**
	 * 第一次加载时，保存banner数据
	 * 
	 * @param data
	 * @param loadData
	 */
	private void addBannerData() {
		TreeSet<BannerBean> set = new TreeSet<BannerBean>();
//		for (BannerBean bean : loadData) {
//			set.add(bean);
//		}
//
//		for (BannerBean bean : set) {
//			banners.add(bean);
//		}

		bannerPics = new ImageView[indicater.length];
//		for (int i = 0; i < indicater.length; i++) {
//			indicater[i].setVisibility(View.GONE);
//		}
//		for (int i = 0; i < loadData.size(); i++) {
//			indicater[i].setVisibility(View.VISIBLE);
//		}
		bannerPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
//				bannerTitle.setText(banners.get(index % bannerPics.length).title);

				switch (index % bannerPics.length) {
				case 0:
					updateIndicter(indicater[0], indicater[1], indicater[2],
							indicater[3], indicater[4]);
					break;
				case 1:
					updateIndicter(indicater[1], indicater[0], indicater[2],
							indicater[3], indicater[4]);
					break;
				case 2:
					updateIndicter(indicater[2], indicater[1], indicater[0],
							indicater[3], indicater[4]);
					break;
				case 3:
					updateIndicter(indicater[3], indicater[1], indicater[2],
							indicater[0], indicater[4]);
					break;
				case 4:
					updateIndicter(indicater[4], indicater[1], indicater[2],
							indicater[0], indicater[3]);
					break;
				}
			}

			private void updateIndicter(ImageView image1, ImageView image2,
					ImageView image3, ImageView image4, ImageView image5) {
				image1.setImageResource(R.drawable.d2);
				image2.setImageResource(R.drawable.d1);
				image3.setImageResource(R.drawable.d1);
				image4.setImageResource(R.drawable.d1);
				image5.setImageResource(R.drawable.d1);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int index) {
			}
		});

		/**
		 * Banner图片的布局
		 */
		int screenWidth = Utils.getScreenSize(this)[0];
		int height = (int) ((float) screenWidth * (float) CONSTANTS_HEIGHT / (float) CONSTANTS_WIDTH);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				screenWidth, height);
		for (int i = 0; i < bannerPics.length; i++) {
			bannerPics[i] = new ImageView(this);
			bannerPics[i].setLayoutParams(params);
			bannerPics[i].setBackgroundResource(R.drawable.banner_default);
			bannerPics[i].setScaleType(ScaleType.CENTER_CROP);
		}

//		bannerTitle.setText(banners.get(0).title);
		bannerAdapter = new A_HotgameBannerAdapter(bannerPics, this,
				bannerHandler);
		bannerPager.setAdapter(bannerAdapter);
		bannerPager.setCurrentItem(bannerPics.length * BANNER_INIT_ITEM);

		circleBanner();
	}

	public void circleBanner() {
		Message msg = bannerHandler.obtainMessage();
		msg.what = 0;
		bannerHandler.sendMessageDelayed(msg, BANNER_CIRCLE_ITEM_TIME);
	}
}
