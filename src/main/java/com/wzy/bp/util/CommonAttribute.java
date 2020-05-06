/*
 * 
 * 
 * 
 */
package com.wzy.bp.util;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * 公共参数
 * 
 * 
 * 
 */
public final class CommonAttribute {
	
	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** UTF-8编码 */
	public static final String UTF_8 = "UTF-8";
	
	/** POST */
	public static final String POST="post";
	

    /**
     * 警报级别
     */
    public static final Integer ALERT_CRITICAL= 4;
    public static final Integer ALERT_WARN= 3;
    public static final Integer ALERT_NORMAL= 2;
    public static final Integer ALERT_TIPS= 1;


    /** 支付方式名称 */
    public static final Map alertLevel = new HashedMap(){{
        put(ALERT_CRITICAL, "严重");
        put(ALERT_WARN, "警告");
        put(ALERT_NORMAL, "一般");
        put(ALERT_TIPS, "提示");
    }};

	public static void main(String[] args) {
		System.out.println(CommonAttribute.alertLevel.get(1));
	}

}