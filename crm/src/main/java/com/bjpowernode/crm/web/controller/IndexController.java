package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	/*
	 * 理论上，给Controller分配url:http://127.0.0.1:8080/crm/ 为了简便，协议:
	 * ip:port/应用名称(项目名)必须省去，用/代表应用根目录下的/
	 */
	@RequestMapping("/")
	public String index() {// 不带参数，不用定义形参
		// 请求转发页面
		// return "/WEB-INF/pages/index.jsp";//不能从外面访问，但可直接内部访问
		return "index";// springmvc.xml配置视图解析器

	}

}
