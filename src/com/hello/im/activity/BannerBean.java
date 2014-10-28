package com.hello.im.activity;

import java.io.Serializable;

/**
 *	������Ϸ���棬banner����
 */
public class BannerBean implements Serializable, Comparable<BannerBean>{
	public static final long serialVersionUID = 1L;
	public static final int NEWS_TYPE = 1;
	public static final int GAME_TYPE = 2;
	
	public String pic;
	public String newsid;
	public String gameid;
	public int position;
	public int type;
	public String title;
	
	@Override
	public int compareTo(BannerBean another) {
		if (another != null && another instanceof BannerBean) {
			if (this.position > another.position) {
				return 1;
			} else {
				return -1;
			}
		}
		
		return -1;
	}
}
