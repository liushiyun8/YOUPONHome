package com.youpon.home1.comm;

/**
 * 
 * Description	: 通用公共类
 * @author		:
 * @date		: 2014-7-22
 * @version		:
 * @see  		:[class/class#method]
 * @since		:[product/model]
 */
public class Utils {
        private static long lastClickTime;  
        /**
         * Description	: 防止按纽连续点击
         * @return
         * @see 		:[class/class#field/class#method]
         */
        public static boolean isFastDoubleClick() {  
            long time = System.currentTimeMillis();  
            long timeD = time - lastClickTime;  
            if ( 0 < timeD && timeD < 500) {     
                return true;     
            }     
            lastClickTime = time;     
            return false;     
        }  
}
