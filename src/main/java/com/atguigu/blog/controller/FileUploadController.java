package com.atguigu.blog.controller;

import com.atguigu.blog.service.FileService;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

//"阿里云文件管理"
@CrossOrigin //跨域
@RestController
@RequestMapping("/oss/file")
public class FileUploadController {

	@Autowired
	private FileService fileService;

	/**
	 * 单个文件上传
	 * @param file
	 */
	@PostMapping("/upload")
	public R upload(
			@RequestParam("file") MultipartFile file) {

		String uploadUrl = fileService.upload(file);
		System.out.println(uploadUrl);
		//返回r对象
		return R.ok(uploadUrl);
	}

	/**
	 * 多个文件上传
	 * @param files
	 */
	@PostMapping("/uploads")
	public R upload(MultipartFile[] files) {
		List<String> uploadUrls=new ArrayList<>();
		for (MultipartFile file : files) {
			String uploadUrl = fileService.upload(file);
			uploadUrls.add(uploadUrl);
		}
		//返回r对象
		return R.ok(uploadUrls);
	}
}
