package com.cmdt.carrental.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;

import com.cmdt.carrental.common.model.UsageReportPieItemModel;



public class BeanSortUtils {
	
	@SuppressWarnings("unchecked")
    public static <T> void sort(List<T> list, String fieldName, boolean asc) {
        Comparator<?> mycmp = ComparableComparator.getInstance();
        mycmp = ComparatorUtils.nullLowComparator(mycmp); // 允许null
        if (!asc) {
            mycmp = ComparatorUtils.reversedComparator(mycmp); // 逆序
        }
        Collections.sort(list, new BeanComparator(fieldName, mycmp));
    }
	
	@SuppressWarnings("unchecked")
    public static <T> List<T> sort(List<T> list, String fieldName, boolean asc,int topVal) {
		List<T> retList = new ArrayList<T>();
		List<T> tmpList = new ArrayList<T>();
		sort(list,fieldName,asc);
		if(list.size() > topVal){
			for(int i = 0 ; i < topVal ; i ++){
				tmpList.add(list.get(i));
			}
		}else{
			tmpList.addAll(list);
		}
		
		try {
			//只保留fieldName不为0的数据
			if(tmpList != null && tmpList.size() > 0){
				for(T tVal : tmpList){
					Object v = invokeMethod(tVal,fieldName,null);
					if(v != null){
						Double tmpVal = Double.valueOf(String.valueOf(v));
						if(tmpVal > 0.0){
							retList.add(tVal);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
    }
	
	@SuppressWarnings("unchecked")  
    private static Object invokeMethod(Object owner, String methodName,  
            Object[] args) throws Exception {  
        Class ownerClass = owner.getClass();  
        methodName = methodName.substring(0, 1).toUpperCase()  
                + methodName.substring(1);  
        Method method = null;  
        try {  
            method = ownerClass.getMethod("get" + methodName);  
        } catch (SecurityException e) {  
        } catch (NoSuchMethodException e) {  
            return " can't find 'get" + methodName + "' method";  
        }  
        return method.invoke(owner);  
    }  

}
