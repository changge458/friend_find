/**
 * 
 */
package com.fz.model;

import java.util.Map;

/**
 */
public interface ObjectInterface {
	/**
	 * 不用每个表都建立一个方法，这里根据表名自动装配
	 * @param map
	 * @return
	 */
	public  Object setObjectByMap(Map<String,Object> map);
}
