package com.cmdt.carrental.common.util;

public class SqlUtil {
	
	public static String processLikeInjectionStatement(String keyWord){
		if(keyWord.contains("%") || keyWord.contains("_")){
			keyWord = keyWord.replace("%", "/%").replace("_", "/_");
            return "'%"+keyWord+"%' ESCAPE '/'";
        }
        else{
        	return "'%"+keyWord+"%'";
        }
	}
	
}
