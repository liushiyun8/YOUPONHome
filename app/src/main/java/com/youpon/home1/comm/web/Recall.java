/**
 * copyright @ iwit,. android team. 2014-2015
 */
package com.youpon.home1.comm.web;

/**
 * 回调接口
 * @author tao
 *
 */
public interface Recall {
	/**
	 * 执行回调
	 * @param serverData 请求到的数据
	 */
	public void execute(String serverData);
}
