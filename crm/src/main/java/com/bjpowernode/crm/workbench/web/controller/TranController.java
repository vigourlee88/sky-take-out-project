package com.bjpowernode.crm.workbench.web.controller;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranHistoryService;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import com.bjpowernode.crm.workbench.service.TranService;

@Controller
public class TranController {

	@Autowired
	private DicValueService dicValueService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private TranService tranService;

	@Autowired
	private TranRemarkService tranRemarkService;

	@Autowired
	private TranHistoryService tranHistoryService;

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

	@RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
	public @ResponseBody Object queryCustomerNameByName(String customerName) {

		// 调用service层方法，查询所有客户名称
		// List<String> customerNameList = customerService.queryAllCustomerName();
		List<String> customerNameList = customerService.queryCustomerNameByName(customerName);
		// 根据查询结果，返回响应信息
		return customerNameList;// ['xxxx','xxxxx',......
	}

	@RequestMapping("/workbench/transaction/saveCreateTran.do")
	public @ResponseBody Object saveCreateTran(@RequestParam Map<String, Object> map, HttpSession session) {
		// 封装数据
		map.put(Contants.SESSION_USER, session.getAttribute(Contants.SESSION_USER));

		// 根据处理结果，生成响应信息
		ReturnObject returnObject = new ReturnObject();
		try {
			// 调用service层方法，保存创建的交易
			tranService.saveCreateTran(map);
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

		} catch (Exception e) {

			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙，请稍后重试....");
		}

		return returnObject;
	}

	@RequestMapping("/workbench/transaction/detailTran.do")
	public String detailTran(String id, HttpServletRequest request) {// 查看交易明细
		// 定义形参接收，前台发送来的参数
		// 调用service层方法，查询数据，
		// 这样controller就把明细页面所需要的3个数据都查出来了
		// 交易详细信息
		Tran tran = tranService.queryTranForDetailById(id);
		// 交易备注的信息
		List<TranRemark> remarkList = tranRemarkService.queryTranRemarkForDetailByTranId(id);
		// 交易历史的信息
		List<TranHistory> historyList = tranHistoryService.queryTranHistoryForDetailByTranId(id);

		// 根据tran所处 阶段名称tran.getStage() 查询可能性
		// 查询可能性信息在配置文件中
		// 解析 配置文件
		ResourceBundle bundle = ResourceBundle.getBundle("possibility");
		String possibility = bundle.getString(tran.getStage());
		tran.setPossibility(possibility);

		// 把数据保存到作用域中
		request.setAttribute("tran", tran);
		request.setAttribute("remarkList", remarkList);
		request.setAttribute("historyList", historyList);
		// request.setAttribute("possibility", possibility);

		// 调用service方法，查询交易所有的阶段
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		request.setAttribute("stageList", stageList);

		// 请求转发
		return "workbench/transaction/detail";

	}

}
