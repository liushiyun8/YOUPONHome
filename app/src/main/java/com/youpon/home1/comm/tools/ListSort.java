package com.youpon.home1.comm.tools;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * List对象排序的通用方法
 * 
 * @author
 */
public class ListSort {
	
	
	/**
	 * 
	 * @param list
	 *            要排序的集合
	 * @param field
	 *            要排序的实体的属性所对应的get方法
	 * @param sort
	 *            desc 为正序
	 */
	public static <T> void listSort(List<T> list, final String field, final String sort) {
		final Comparator cptor = new Comparator<T>() {

			public int compare(T a, T b) {
				int ret = 0;
				
				try {
					String methodName = genGetterMethodName(field);
					// 获取m1的方法名
					Method m1 = a.getClass().getDeclaredMethod(methodName);
					// 获取m2的方法名
					Method m2 = b.getClass().getDeclaredMethod(methodName);

					if (sort != null && "desc".equals(sort)) {

						ret = m2.invoke(b).toString()
								.compareTo(m1.invoke(a).toString());

					} else {
						// 正序排序
						ret = m1.invoke(a).toString()
								.compareTo(m2.invoke(b).toString());
					}
				} catch (Exception e) {
					Logger.e("ListSort", "LISTSORT异常:" + e.getMessage());
				}
				return ret;
			}
		};
		// 用内部类实现排序
		Collections.sort(list, cptor);
	}
	
	private static String genGetterMethodName(String field) {
		return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
}
