package com.mmc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @Author: mmc
 * @Date: 2018/9/11 21:37
 * @Version 1.0
 */
public class RecordException extends RuntimeException {

    Logger logger=LoggerFactory.getLogger(RecordException.class);

    //异常类出现的class名
    private String code;
    private String message;

    public RecordException( String code, String message1) {
        super(MessageFormat.format("异常出现在{0}类,异常信息:{1}",code,message1));
    }
}
