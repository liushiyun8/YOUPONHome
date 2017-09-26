package com.youpon.home1.comm.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.youpon.home1.R;


/**
 * 图片处理公共类 Description :
 * 
 * @author :
 * @date : 2014-7-1
 * @version :
 * @see :[class/class#method]
 * @since :[product/model]
 */
public class BitmapUtil {
	/**
	 * 复制图片
	 * 
	 * @param bgmap
	 * @return
	 */
	public static Bitmap copyBitmap(Bitmap bgmap) {
		Bitmap newb = Bitmap.createBitmap(bgmap.getWidth(), bgmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		if (newb != null) {
			Canvas newc = new Canvas(newb);
			newc.drawBitmap(bgmap, 0, 0, null);
		}
		return newb;
	}

	/**
	 * 缩放图片 宽高比不同
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * 缩放图片 宽高比相同的缩放
	 * 
	 * @param bm
	 * @param scale
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, float scale) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * 从sd卡获取压缩较多的图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getImageBitmap(String path, int size) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inSampleSize = size;
		bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_4444;
		bmpFactoryOptions.inPurgeable = true;
		bmpFactoryOptions.inInputShareable = true;
		return BitmapFactory.decodeFile(path, bmpFactoryOptions);
	}

	/**
	 * 从sd卡获取压缩较多的图片565
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap get565ImageBitmap(String path, int size) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inSampleSize = size;
		bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ALPHA_8;
		bmpFactoryOptions.inPurgeable = true;
		bmpFactoryOptions.inInputShareable = true;
		return BitmapFactory.decodeFile(path, bmpFactoryOptions);
	}

	/**
	 * 质量上压缩图片
	 * 
	 * @param
	 * @return
	 */
	public static Bitmap compressImage(String path) {
		Bitmap image = BitmapFactory.decodeFile(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于20kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 从sd卡获取压缩比较少的图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getImageBitmap(String path) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bmpFactoryOptions.inPurgeable = true;
		bmpFactoryOptions.inInputShareable = true;
		return BitmapFactory.decodeFile(path, bmpFactoryOptions);
	}

	/**
	 * Description : 根据路径把图片缩成已知的大小
	 * 
	 * @param srcPath
	 *            路径
	 * @param w
	 *            宽
	 * @param h
	 *            高
	 * @return
	 */
	public static Bitmap zoomPathToImg(String srcPath, int w, int h) {
		Bitmap newbmp = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		try {
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inSampleSize = 1;
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 创建操作图片用的Matrix对象
			Matrix matrix = new Matrix();
			// 计算缩放比例
			float scaleWidth = ((float) w / width);
			float scaleHeight = ((float) h / height);
			// 设置缩放比例
			matrix.postScale(scaleWidth, scaleHeight);
			// 建立新的bitmap，其内容是对原bitmap的缩放后的图
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
			// 把bitmap转换成drawable并返回
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newbmp;
	}

	/**
	 * Description : 根据路径把图片缩成已知的大小
	 * 
	 * @param srcPath
	 *            路径
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @return
	 */
	public static Bitmap getBitmapToPahtOrWOrH(Context con, Bitmap bitmap,
			String srcPath, int width, int height) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		InputStream is = null;
		try {
			is = con.getAssets().open(srcPath);
			// 开始读入图片，只返回图片的宽高，不返回图片
			newOpts.inJustDecodeBounds = true;
			// 此时返回bm为空
			BitmapFactory.decodeStream(is, null, newOpts);
			int outWidth = newOpts.outWidth;
			int outHeight = newOpts.outHeight;
			is.close();
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			newOpts.inSampleSize = 1;
			if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
				int sampleSize = (outWidth / width + outHeight / height) / 2;
				newOpts.inSampleSize = sampleSize;
			}
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			newOpts.inJustDecodeBounds = false;
			newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			is = con.getAssets().open(srcPath);
			bitmap = BitmapFactory.decodeStream(is, null, newOpts);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * Description : 创建一张背景图片
	 * 
	 * @param con
	 * @param bitmap
	 * @param str
	 * @return
	 */
	public static Bitmap getBgBitmapByPath(Context con, Bitmap bitmap,
			String str) {
		Bitmap bm = null;
		bm = getBitmapToPahtOrWOrH(con, bitmap, str, 0, 0);
		bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				Bitmap.Config.ARGB_8888);
		bitmap.eraseColor(Color.WHITE);
		Canvas can = new Canvas(bitmap);
		can.drawBitmap(bm, 0, 0, null);
		if (null != bm) {
			bm.recycle();
			bm = null;
		}
		return bitmap;
	}

	/**
	 * 设置调色板的颜色
	 */
	public static void setPaintColorPalette(Context con, ImageView iv,
			int colorId, Bitmap newBitMap) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 不加载bitmap到内存中
		options.inJustDecodeBounds = true;
		// 获取资源图片
		InputStream is = con.getResources().openRawResource(
				R.raw.ic_launcher);
		BitmapFactory.decodeStream(is, null, options);
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		newBitMap = Bitmap.createBitmap(outWidth, outHeight,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(newBitMap);
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(colorId);
		int r = outWidth / 2;
		c.drawCircle(r, r, r - 10, p);
		iv.setImageBitmap(newBitMap);
	}

	/**
	 * 将Drawable转化为Bitmap Description :
	 * 
	 * @param drawable
	 * @return
	 * @see :[class/class#field/class#method]
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 将Bitmap转换成Drawable对象 Description :
	 * 
	 * @param b
	 * @return
	 * @see :[class/class#field/class#method]
	 */
	public static Drawable bitmapToDrawable(Bitmap b) {
		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
		BitmapDrawable bmd = new BitmapDrawable(b);
		return bmd;
	}

	public static Bitmap getBgBitmapByPath(Bitmap tBitmap, int w, int h) {
		tBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		tBitmap.eraseColor(Color.WHITE);
		return tBitmap;
	}
}
