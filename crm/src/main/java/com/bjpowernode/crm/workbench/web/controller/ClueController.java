package com.bjpowernode.crm.workbench.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;

@Controller
public class ClueController {

	@Autowired
	private UserService userService;

	@Autowired
	private DicValueService dicValueService;

	@Autowired
	private ClueService clueService;

	// 跳转页面
	@RequestMapping("/workbench/clue/index.do")
	public String index(HttpServletRequest request) {// 跳转页面不是访问json，不用加responseBody，前台没有参数，不用定义形参
		// 调用service层方法，查询所有用户
		List<User> userList = userService.queryAllUsers();
		List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");// 称呼写死
		List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");// 线索状态
		List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");// 数据来源
		// 把数据保存到作用域request中,需注入request
		request.setAttribute("userList", userList);
		request.setAttribute("appellationList", appellationList);
		request.setAttribute("clueStateList", clueStateList);
		request.setAttribute("sourceList", sourceList);
		// 请求转发 资源路径
		return "workbench/clue/index";// 自动添加前缀和后缀
	}

	@RequestMapping("/workbench/clue/saveCreateClue.do")
	public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session) {

		User user = (User) session.getAttribute(Contants.SESSION_USER);

		// 封装参数(有几个参数没有传进来)
		clue.setId(UUIDUtils.getUUID());
		clue.setCreateTime(DateUtils.formateDateTime(new Date()));
		clue.setCreateBy(user.getId());

		ReturnObject returnObject = new ReturnObject();
		try {
			// 调用service方法，保存创建的线索
			int ret = clueService.saveCreateClue(clue);
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙,请稍后再试...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙,请稍后再试...");
		}

		return returnObject;
	}

}
