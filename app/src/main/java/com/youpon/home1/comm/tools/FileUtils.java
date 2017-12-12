/*
 * File name	: FileUtils.java
 * Copyright 	: (Shenzhen) I-WIT Digital Co. .All rights reserved
 * Project		: i43 Pet game
 * JDK			: JDK 1.6
 * SDK			: Android 2.3.3
 * Author		: csk
 * Date			: 2012-6-11
 * Comments		:
 * Mod date		:
 * Mod by		:
 * Mod reason	:
 */
package com.youpon.home1.comm.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;

import com.orhanobut.logger.Logger;

/**
 * Description :
 * 
 * @author :
 * @date :
 * @version :
 * @see :[class/class#method]
 * @since :[product/model]
 */
public class FileUtils {
	private String sdCardPath;
	private static final String TAG = "FileUtils";
	private static final String IMAGE_JPG = ".jpg";

	private static final String IMAGE_PNG = ".png";

	private static final String IMAGE_GIF = ".gif";

	private static final String[] IMAGE_TYPES = new String[] { IMAGE_JPG,
			IMAGE_PNG, IMAGE_GIF };

	private static FileUtils fileUtils = null;

	public FileUtils() {
		// 得到当前外部存储设备的目录( /SDCARD )
		sdCardPath = getSDPath();
	}
	
	/** 取SD卡路径 **/  
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); //获取根目录  
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }
	public static synchronized FileUtils getInstance() {

		if (null == fileUtils) {
			fileUtils = new FileUtils();
		}
		return fileUtils;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(sdCardPath + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {
		File dir = new File(sdCardPath + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(sdCardPath + fileName);
		return file.exists();

	}
	
	/**
	 * Description	: 获取Assets指定目录下面的图片名称
	 * @param context
	 * @param path
	 * @return
	 */
    public static List<String> getListString(Context context, String path){
        List<String> list = new ArrayList<String>();
        try {
            String[] str = context.getResources().getAssets().list(path);
            list = Arrays.asList(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

	/**
	 * 得到当前目录的所有图片的名字
	 * 
	 * @param path
	 * @return
	 */
	public List<String> getPathPicName(String path) {
		List<String> picPathList = new ArrayList<String>();
		File file = new File(path);
		File[] files = file.listFiles();
		if (null != files) {
			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName().toLowerCase();
				for (String type : IMAGE_TYPES) {
					if (name.endsWith(type)) {
						picPathList.add(files[i].getAbsolutePath());
					}
				}
			}
		}
		return picPathList;
	}
	

	/**
	 * 解压已下载的压缩包
	 */

	/**
	 * Description :
	 * 
	 * @param fileName
	 *            zip文件名字
	 * @param
	 *
	 * @return
	 */
	public boolean unZipFile(String fileName, String basePath, String unZipPath) {
		Logger.v("upZipFile", "sdCardPath + basePath + fileName ::" +sdCardPath + basePath + fileName);
		// download 下的zip包路径
		File file = new File(sdCardPath + basePath + fileName);
		// 创建根文件夹
		File m = new File(sdCardPath + unZipPath);
		m.mkdir();
		Logger.v("upZipFile", "sdCardPath + unZipPath ::" +sdCardPath + unZipPath);
		if(IOToolkit.upZipFile(file, sdCardPath + unZipPath)){
			if (file.exists())
				file.delete();
		}
//		try {
//			if(IOToolkit.unzip(sdCardPath + basePath + fileName, sdCardPath + unZipPath)){
//				if (file.exists())
//					file.delete();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		};
		return true;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 */
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 递归删除文件夹下面的所有的内容
	 * 
	 * @param path
	 * @return
	 */
	public boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	/**
     * 删除单个文件
     */
    public static boolean deleteFile(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 判断是否是文件
            if (file.isFile()) { 
                file.delete(); 
                return true;
            }
        }
        return false;
        
    }
	/**
	 * 检查SD卡是否插入
	 * 
	 * @return
	 * 
	 */
	public boolean checkSDCARD() {

		String status = Environment.getExternalStorageState();
		MyLog.e("MMMMMMMMMMMMMMM",status);
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
	//	"mnt/sdcard/"
		return false;
	}

	/**
	 * 判断文件是否存在在SDCard卡上
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public boolean checkFileExist(String path, String fileName) {

		File file = new File(path + fileName);

		return file.exists();
	}

	/**
	 * 读取SD卡中文本文件
	 * 
	 * @param fileName
	 * @return
	 */

	public String readSDFile(String sd_path, String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(sd_path + fileName);

		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis, "UTF-8"));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				sb.append(s);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();

	}
	
	/**
	 * Description	:  保存图片
	 * @param d        文件目录
	 * @param fileName 文件名称
	 * @param photo    图片
	 * @return
	 */
    public static String savePictures(File d, String fileName, Bitmap photo) {
        String path = null;
        try {
            if (!d.exists()) {
                if (d.mkdirs()) {
                    final File noMediaFile = new File(d, MediaStore.MEDIA_IGNORE_FILENAME);
                    if (!noMediaFile.exists()) {
                        new FileOutputStream(noMediaFile).write('\n');
                    }
                } else {
                    throw new IOException("cannot create dirs: " + d);
                }
            }
            File file = new File(d, fileName);
            OutputStream os = new FileOutputStream(file);
            photo.compress(Bitmap.CompressFormat.PNG, 0, os);
            os.flush();
            os.close();
            path = file.toString();
            Logger.e("=========", "=====" + path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
    
    /**
     * 获取系统图片存放的路径
     */
    @TargetApi(8)
    public static File getPicturesDirectory() {
        final File d;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            d = new File("/sdcard/Pictures");
        }
        return d;
    }
    /**
	 * 
	 * Description :读取Sdcard指定目录下的文件
	 * 
	 * @return
	 * @see :[class/class#field/class#method]
	 */
	public List<String> getSDFile(String dirPath) {
		dirPath = sdCardPath+dirPath;
		File mFile = new File(dirPath);
		File[] allList = mFile.listFiles();
		List<String> tempFile = new ArrayList<String>();
		if (allList != null) {
			for (int i = 0; i < allList.length; i++) {
				File lists = allList[i];
				String allDir = lists + "".trim();
				String has = FileUtils.getFilename(allDir);
				tempFile.add(has);
			}
		}
		return tempFile;
	}
	public boolean isExsitDir(String dirPath){
		dirPath = sdCardPath+dirPath;
		File file = new File(dirPath);
		if(file.exists()){
			return true;
		}
		return false;
	}
    /**
     * 涂鸦我的作品 获取所有作品
     */
    public static List<String> getDrawingFileList() {
        List<String> fileList = new ArrayList<String>();
        File f = getPicturesDirectory();
        if (!f.exists()) {
        	Logger.e("fileList", "======文件夹不存在=====" + f.toString() + ">>>" +  f.toURI());
        }
        File[] files = f.listFiles();
        for(int i = 0; i< files.length; i++){
            if(!files[i].toString().contains("art_")){
                continue;
            }
            fileList.add(files[i].toString());
        }
        Logger.e("fileList", "=====fileList.size======:" + fileList.size());
        return fileList;
    }
    
    
    
    /**
	 * 读取书籍名称
	 * 
	 * @return
	 */
	public static String getFilename(String dirPath) {

		int lastIndex = dirPath.lastIndexOf('/');
		String filename = dirPath.substring(lastIndex + 1);
		return filename;
	}
}
