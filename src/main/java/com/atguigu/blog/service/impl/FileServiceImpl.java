package com.atguigu.blog.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.blog.constant.ConstantProperties;
import com.atguigu.blog.service.FileService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String upload(MultipartFile file) {

		//获取阿里云存储相关常量
		String endPoint = ConstantProperties.END_POINT;
		String accessKeyId = ConstantProperties.ACCESS_KEY_ID;
		String accessKeySecret = ConstantProperties.ACCESS_KEY_SECRET;
		String bucketName = ConstantProperties.BUCKET_NAME;

		String uploadUrl = null;

		try {
			//判断oss实例是否存在：如果不存在则创建，如果存在则获取
			OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
			if (!ossClient.doesBucketExist(bucketName)) {
				//创建bucket
				ossClient.createBucket(bucketName);
				//设置oss实例的访问权限：公共读
				ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
			}

			//获取上传文件流
			InputStream inputStream = file.getInputStream();

			//构建日期路径：avatar/2019/02/26/文件名
			String filePath = new DateTime().toString("yyyy/MM/dd");

			//文件名：uuid.扩展名
			String original = file.getOriginalFilename();
			String fileName = UUID.randomUUID().toString();
			String fileType = original.substring(original.lastIndexOf("."));
			String newName = fileName + fileType;
			String fileUrl = filePath + "/" + newName;

			//文件上传至阿里云
			ossClient.putObject(bucketName, fileUrl, inputStream);

			// 关闭OSSClient。
			ossClient.shutdown();

			//获取url地址
//			uploadUrl = "http://" + bucketName + "." + endPoint + "/" + fileUrl;
			uploadUrl = fileUrl;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return uploadUrl;
	}
}
