/**
 * copyright @ iwit,. android team. 2014-2015
 */
package com.youpon.home1.comm;

/**
 * @author tao
 *
 */
public class MagicPenException extends Exception {
	private static final long serialVersionUID = 564196779958838644L;
	/**
	 * 捕获的异常，通常是在执行api相关处理时捕获的，统一归类为MagicPenException<br>
	 * 此异常供外部调用时再捕获
	 * @param desc
	 * @param e
	 */
	public MagicPenException(String desc, Exception e) {
		super(desc, e);
	}
	
	public MagicPenException(String desc) {
		super(desc);
	}
}
