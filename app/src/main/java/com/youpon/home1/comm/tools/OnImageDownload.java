package com.youpon.home1.comm.tools;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface OnImageDownload {
	void onDownloadSucc(Bitmap bitmap, String c_url, ImageView imageView);
}
