package com.bjpowernode.crm.workbench.web.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;

@Controller
public class TranController {

	@Autowired
	private DicValueService dicValueService;

	@Autowired
	private UserService userService;

	@RequestMapping("/workbench/transaction/index.do")
	public String index(HttpServletRequest request) {
		// 调用service层方法，查询动态数据
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
		List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
		// 把数据保存到request
		request.setAttribute("stageList", stageList);
		request.setAttribute("transactionTypeList", transactionTypeList);
		request.setAttribute("sourceList", sourceList);

		// 请求转发
		return "workbench/transaction/index";
	}

	@RequestMapping("/workbench/transaction/toSave.do")
	public String toSave(HttpServletRequest request) {
		// 调用service层方法，查询动态数据
		List<User> userList = userService.queryAllUsers();
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
		List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

		// 把数据保存到作用域request中
		request.setAttribute("userList", userList);
		request.setAttribute("stageList", stageList);
		request.setAttribute("transactionTypeList", transactionTypeList);
		request.setAttribute("sourceList", sourceList);

		// 请求转发
		return "workbench/transaction/save";
	}

	@RequestMapping("/workbench/transaction/getPossibilityByStage.do")
	public @ResponseBody Object getPossibilityByStage(String stageValue) {
		// 解析properties配置文件，根据阶段获取可能性
		ResourceBundle bundle = ResourceBundle.getBundle("possibility");
		String possibility = bundle.getString(stageValue);
		// 返回响应信息
		return possibility;
	}

}
