package com.youpon.home1.comm.tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class WidgetParam {

	public static float getWidthPHeightTimes(Context ctx) {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		return (float) dm.widthPixels / (float) dm.heightPixels;
	}

	/**
	 * 
	 * @param
	 * @param view
	 *            控件
	 * @param w
	 *            view与屏幕宽度之比
	 * @param
	 */

	public static void setParam(Context ctx, View view, double w, double times) {
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		lp.width = (int) (dm.widthPixels * w);
		lp.height = (int) (lp.width * times);

		view.setLayoutParams(lp);
	}

	/**
	 * 
	 * @param ctx
	 * @param view
	 * @param w
	 *            view与屏幕宽度之比,长度与宽度相等。
	 */
	public static void setParam(Context ctx, View view, double w) {
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		lp.width = (int) (dm.widthPixels * w);
		lp.height = lp.width;

		view.setLayoutParams(lp);
	}

	/**
	 * 
	 * @param ctx
	 * @param view
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 */
	public static void setRelativeLayoutParam(Context ctx, View view, int left,
			int right, int top, int bottom) {
		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		// DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		rl.setMargins(left, top, left + view.getWidth(), top + view.getHeight());
		view.setLayoutParams(rl);
	}

	public static void setRelativeLayoutParam(Context ctx, View view,
			float lefttimes, float toptimes, float w, float h) {
		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		int left = (int) (lefttimes * dm.widthPixels);
		int right = (int) (dm.widthPixels - left - (int) (dm.widthPixels * w));
		int top = (int) (toptimes * dm.heightPixels);
		int bottom = (int) (dm.heightPixels - top - (int) (dm.widthPixels * h));

		rl.setMargins(left, top, right, bottom);
		view.setLayoutParams(rl);
	}

	// 设置图片大小 位置
	public static void setImageLocSize(View mView, int width, int height,
			int leftMargin, int topMargin) {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams = new RelativeLayout.LayoutParams(width, height);
		layoutParams.leftMargin = leftMargin;
		layoutParams.topMargin = topMargin;
		mView.setLayoutParams(layoutParams);
	}
}
