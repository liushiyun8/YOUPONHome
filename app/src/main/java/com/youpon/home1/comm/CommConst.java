/**
 * copyright @ iwit,. android team. 2014-2015
 */
package com.youpon.home1.comm;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Environment;


/**
 * 
 * 存放公共的常量
 * 
 * @author tao
 * 
 */
public class CommConst {

	public static final String DB_NAME = "magicpen.db";
	public static final int DB_VER = 1;
	// 测试版
	// public static final String SIT_DOMAIN_URL = "http://192.168.16.71";
	// 正式版
	public static final String SIT_DOMAIN_URL = "http://iosee.weeocean.com";

	// 杭州服务器
	public static final String SIT_DOMAIN_FILE_URL = "http://hvknukmpgxuixedriosee.oss-cn-hangzhou.aliyuncs.com/";
	// 测试书籍包
	// public static final String SIT_DOMAIN_FILE_URL =
	// "http://192.168.16.71/file/iosee/book/";
	// 书籍包单独列出来
	public static String SITE_FILE_BOOK_FILE_URL = SIT_DOMAIN_FILE_URL;

	// 书籍包、书籍相关的封面图、介绍图访问路径，后面要再加上文件名
	public static String SITE_FILE_BOOK_URL = SIT_DOMAIN_URL
			+ "/file/iosee/book/";
	public static String SITE_ROOT_URL = SIT_DOMAIN_URL + "/action/client/";
	// 服务器音乐路径
	public static String SITE_FILE_MUSIC_URL = SIT_DOMAIN_URL
			+ "/file/iosee/music/";

	public static final String PATH_PROGRAM_ROOT = "/iwit_pen/";
	// 书籍相关内容，zip包放在这里解压，解压放到以bid命名的子文件下后，解压后删除zip包
	public static String PATH_BOOK = PATH_PROGRAM_ROOT + "book/";
	public static String PATH_BOOK_PIC = PATH_BOOK + "pic/";// 书籍相关的图片

	public static final String CONTROL_SP_FILE = "control";
	public static final String CONTROL_SP_BOX = "box";
	public static final String CONTROL_SP_TIME = "time";
	public static final String JSON_KEY_RESULT = "result";
	public static final String JSON_KEY_MESSAGE = "message";
	public static final String POST_PARAM_KEY_USER_NAME = "name";
	public static final String POST_PARAM_KEY_USER_EMAIL = "email";
	public static final String POST_PARAM_KEY_PWD = "pwd";
	public static final String POST_PARAM_KEY_NPWD = "npwd";

	public static final String VERSION_NUM = "1.0.6";

	public static final String TYPEINFO = "istype";
	public static final String SP_REGION_ISTABLETACTIVED = "isactived";
	public static final String SWITCH_STATE_FLAG = "switchstate";
	public static final String JSON_FIELD_NAME = "restypes";
	public static final String SP_FILE = "com.iwit.pen.sp";
	public static final String SP_KEY_CURRENT_USER = "sp_key_current_user";
	public static final String SP_KEY_ACTIVE_CODE = "giftcard";
	public static final String FIELD_NAME_UID = "uid";
	public static final String RESTYPE_FREE = "a0";// 免费资源类型
	public static final String SP_KEY_CURRENT_RESTYPES = "sp_key_current_restypes";

	// message标记
	public static final int HANDLER_MESSAGE_WHAT_FAULT = -1; // 失败
	public static final short HANDLER_MESSAGE_WHAT_SUCCESS = 2002; // 成功

	public static final String POST_URL_METHOD_LOGIN = "login";
	public static final String POST_URL_METHOD_FIND = "findpassword";
	public static final String POST_URL_METHOD_REGISTER = "register";
	public static final String POST_URL_METHOD_CHANGE_PWD = "changepassword";
	public static final String POST_URL_METHOD_ACTIVE = "bundlegift";
	public static final String POST_URL_METHOD_ACTIVETABLET = "activetablet";
	public static final String FLAG_FALSE = "0";

	public static final String FLAG_HAS_ACTIVETED_SUCCESS = "1";

	public final static String DRAWING_BG_SOUND = "darwing_bg_sound";
	// 书籍请求方法
	public final static String REQUEST_METHOD = "listbook";
	// 音乐请求方法
	public final static String REQUEST_MUSIC_METHOD = "listmusic";

	// 本地音乐存放路径
	public static String SAVE_PATH_MUSIC = PATH_PROGRAM_ROOT + "music/";
	// 本地音乐图片路径
	// 本地音乐播放路径
	public static String SAVE_PATH_MUSIC_PIC = PATH_PROGRAM_ROOT + "musicpic";
	public static String PALY_PATH_MUSIC = Environment
			.getExternalStorageDirectory().getAbsolutePath() + SAVE_PATH_MUSIC;

	// 书籍资源状态
	// status 192 正在下载 （193） 194 195 196 下载暂停 通过自己的程序 200 下载成功 190挂载

	// STATUS_DOWNLOAD 192 （未下载完，默认）正在下载
	public static final int STATUS_DOWNLOADING = 192;
	// STATUS_PAUSE 193 （下载暂停，通过自己的程序主动暂停）暂停
	public static final int STATUS_PAUSE = 193;
	// STATUS_READ 200 （下载完，解压完成）阅读
	public static final int STATUS_READY = 200;
	// STATUS_MOUNT_BEGIN 190 （下载中，网络原因暂停）开始挂载
	// public static final int STATUS_MOUNT_BEGIN = 190; 190先不考虑
	// STATUS_MOUNT_BEGIN 194 （下载中，网络原因暂停）开始挂载
	public static final int STATUS_MOUNT_BEGIN = 194;
	// STATUS_MOUNT_BEGIN 190 （下载中，网络原因暂停）挂载中
	public static final int STATUS_MOUNT_ING = 195;
	// STATUS_MOUNT_BEGIN 190 （下载中，网络原因暂停）结束挂载
	public static final int STATUS_MOUNT_END = 196;

	public static final int STATUS_READ_BOOK = 2 << 2;
	// 下载状态数据
	public static final int DOWNLOAD_FINISH = 200;
	public static final int OVER_DOWNLOAD = 300;

	// 进度状态数据信息
	public static final int PROGRESS_FULL = 100;
	public static final String KEY_RESLUT = "result";
	public static final String SP_REGION_SYNCTIME = "sp_region_synctime";

	// 激活流程相关状态，用于界面进一步处理
	public static final int STATUS_WAITING_ACTIVE = 20;
	public static final int STATUS_WAITING_ADD_TIMES = 21;
	public static final int STATUS_TABLET_HASACTIVED = 10;
	public static final int STATUS_NOT_LOGIN = -1;
	public static final String PACKAGE_NAME = "com.iwit.pen";

	// 书籍下载标识
	public static final int FLAG_DOWNLOAD_NOT_BEGIN = 0;// 未下载，删除要将其置0
	public static final int FLAG_DOWNLOADING = 1;// 正在下载
	public static final int FLAG_DOWNLOAD_PAUSE = 2;// 暂停
	public static final int FLAG_DOWNLOAD_FINISH = 3;// 下载完成
	public static final int FLAG_DOWNLOAD_UNZIPED = 4;// 解压完成

	// 书籍类别
	public static final int FLAG_BOOK_TYPE_LEARN = 1;
	public static final int FLAG_BOOK_TYPE_ANIMAL = 4;
	public static final int FLAG_BOOK_TYPE_ENGLISH = 2;
	public static final int FLAG_BOOK_TYPE_STORY = 3;

	// 学写字大bean
	/** 本地存放书籍的目录 */
	public static String writeBookSourceDir = null;
	public static String mBookBaseFile = null;
	public static int curBookBid = 0;
	/**
	 * 存放透明图片
	 */
	public final static Bitmap starBitmap = Bitmap.createBitmap(40, 40,
			Bitmap.Config.ARGB_8888);

	// 两个长度的list的index
	public static final int LIST_INDEX_1 = 1;
	public static final int LIST_INDEX_0 = 0;

	public static final String BG_MUSIC_SERVICE = "com.iwit.pen.comm.tools.MusicBackGroundMusic";
	/**
	 * gallery 所用的bitmap
	 */
	public static HashMap<String, Bitmap> dataCache = null;
}
