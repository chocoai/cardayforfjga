package com.cmdt.carrental.platform.service.util;

import java.util.Random;

public class WsUtil {

	public static int nextInt(final int min, final int max){
        Random rand= new Random();
        int tmp = Math.abs(rand.nextInt());
        return tmp % (max - min + 1) + min;
	}

}
