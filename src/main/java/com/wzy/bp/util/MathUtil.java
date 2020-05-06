package com.wzy.bp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.NumberFormat;

public class MathUtil {

	public static String percent(float a, float b){
		NumberFormat nt = NumberFormat.getPercentInstance();
		//设置百分数精确度2即保留两位小数
		nt.setMinimumFractionDigits(0);
		return nt.format(a/b);
	}



	public static void main(String[] args) throws Exception{
		System.out.println(percent(1,3));
		System.out.println(new BCryptPasswordEncoder().encode("wzy,1234"));
	}
}
