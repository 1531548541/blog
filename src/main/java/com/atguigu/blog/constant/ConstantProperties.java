package com.atguigu.blog.constant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取properties 配置文件信息
 */
@Component
public class ConstantProperties implements InitializingBean {

	@Value("${aliyun.oss.file.endpoint}")
	private String endpoint;

	@Value("${aliyun.oss.file.keyid}")
	private String keyId;

	@Value("${aliyun.oss.file.keysecret}")
	private String keySecret;

	@Value("${aliyun.oss.file.bucketname}")
	private String bucketName;

	public static String END_POINT;
	public static String ACCESS_KEY_ID;
	public static String ACCESS_KEY_SECRET;
	public static String BUCKET_NAME;
	public static String FILE_URL;

	@Override
	public void afterPropertiesSet() throws Exception {
		END_POINT = endpoint;
		ACCESS_KEY_ID = keyId;
		ACCESS_KEY_SECRET = keySecret;
		BUCKET_NAME = bucketName;
		FILE_URL="http://"+bucketName+"."+endpoint+"/";
	}
}
