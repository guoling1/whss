package com.jkm.base.common.util;

import java.lang.reflect.Field;

public class ReflectExUtil {
	public static Object getFieldValue(Object obj, String fieldName){
		Object result = null;
	    Field field = ReflectExUtil.getField(obj,fieldName);
	    if (field != null){
	    	field.setAccessible(true);
	    	try {
	    		result = field.get(obj);
			}catch (IllegalArgumentException e){
				e.printStackTrace();
	        } catch (IllegalAccessException e){
	        	e.printStackTrace();
	        }
		}
	    return result;
	}
	
	public static Field getField(Object obj,String fieldName){
		Field field = null;
	    for (Class<?> clazz=obj.getClass(); clazz != Object.class;clazz=clazz.getSuperclass()){
	    	try {
	    		field=clazz.getDeclaredField(fieldName);
	    		break;
	    	} catch (NoSuchFieldException e) {
	       }
	    }
	    return field;
	}
	
	public static void setFieldValue(Object obj,String fieldName,String fieldValue){
		Field field = ReflectExUtil.getField(obj,fieldName);
    	if (field != null){
    		field.setAccessible(true);
    		try {
				field.set(obj, fieldValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
}
