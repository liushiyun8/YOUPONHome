package com.youpon.home1.comm.view;

import android.app.ProgressDialog;
import android.content.Context;
/**
 * 耗时请求时用到的系统的缓冲对话框 主要用在网络加载
 * @author Administrator
 *
 */
public class BufferProgressDilog {
	private Context mContext;
	private String message;
	private String title;

	/**
	 * 不带tittle和message的dialog
	 * @param mContext
	 */
	public BufferProgressDilog(Context mContext) {
		this.mContext = mContext;
	}

	public BufferProgressDilog(Context mContext, String message) {
		this.mContext = mContext;
		this.message = message;
	}

	/**
	 * 带tittle和message的dialog
	 * 
	 * @param mContext
	 * @param message
	 */
	public BufferProgressDilog(Context mContext, String title, String message) {
		this.mContext = mContext;
		this.message = message;
		this.title = title;
	}

	/**
	 * 
	 * @param style 默认 ProgressDialog.STYLE_SPINNER
	 * @return
	 */
	public ProgressDialog showDialog(int style) {
		ProgressDialog progressDialog= new ProgressDialog(mContext);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(style);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(true);
		return progressDialog;
	}
}
