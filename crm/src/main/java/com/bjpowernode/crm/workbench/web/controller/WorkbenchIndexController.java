package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchIndexController {

	/**
	 * 跳转到业务的主页面 url要和访问index.html的资源路径一致 从视图解析器往下找/workbench/index.do
	 * 
	 * @return
	 */
	@RequestMapping("/workbench/index.do") // 处理请求，需要添加requestMapping
	public String index() {// 没有参数，不用定义形参，不要@ResponseBody
		// 跳转到业务主页面
		return "workbench/index";// 自动添加前缀和后缀
	}

}
