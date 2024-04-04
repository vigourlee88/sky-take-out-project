package com.bjpowernode.crm.workbench.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;

@Controller
public class ClueController {

	@Autowired
	private UserService userService;

	@Autowired
	private DicValueService dicValueService;

	@Autowired
	private ClueService clueService;

	@Autowired
	private ClueRemarkService clueRemarkService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ClueActivityRelationService clueActivityRelationService;

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

	// 查看线索明细，跳转页面
	@RequestMapping("/workbench/clue/detailClue.do")
	public String detailClue(String id, HttpServletRequest request) {
		// 调用service层方法，查询数据(一个参数不需要封装参数)
		// 首先查基本信息
		Clue clue = clueService.queryClueForDetailById(id);
		List<ClueRemark> remarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
		List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
		// 把动态数据保存到页面作用域中request
		request.setAttribute("clue", clue);
		request.setAttribute("remarkList", remarkList);
		request.setAttribute("activityList", activityList);
		// 请求转发
		return "workbench/clue/detail";
	}

	@RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
	public @ResponseBody Object queryActivityForDetailByNameClueId(String activityName, String clueId) {
		// 封装参数(activityName,clueId)放入map中
		Map<String, Object> map = new HashMap<>();
		map.put("activityName", activityName);
		map.put("clueId", clueId);
		// 调用service层方法，查询市场活动信息
		List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
		// 根据查询结果，返回响应信息
		return activityList;// [],对象使用{}
	}

	@RequestMapping("/workbench/clue/saveBund.do")
	public @ResponseBody Object saveBund(String[] activityId, String clueId) {
		// 封装参数(把参数封装在关联关系的多个实体类对象中)
		ClueActivityRelation car = null;
		List<ClueActivityRelation> relationList = new ArrayList<>();

		for (String ai : activityId) {
			car = new ClueActivityRelation();
			car.setActivityId(ai);
			car.setClueId(clueId);
			car.setId(UUIDUtils.getUUID());

			relationList.add(car);
		}

		ReturnObject returnObject = new ReturnObject();
		try {
			// 调用service层方法，批量保存线索和市场活动的关联关系(注入)
			int ret = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

				List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
				returnObject.setRetData(activityList);

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

	@RequestMapping("/workbench/clue/saveUnbund.do")
	public @ResponseBody Object saveUnbund(ClueActivityRelation relation) {// 定义实体类的参数，在接收的同时封装好了

		ReturnObject returnObject = new ReturnObject();
		try {
			// 调用service层方法，删除线索和市场活动的关联关系
			int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
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

	@RequestMapping("/workbench/clue/toConvert.do")
	public String toConvert(String id, HttpServletRequest request) {
		// 调用 service层方法，查询线索的明细信息
		Clue clue = clueService.queryClueForDetailById(id);
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		// 把数据保存到作用域request中
		request.setAttribute("clue", clue);
		request.setAttribute("stageList", stageList);
		// 请求转发
		return "workbench/clue/convert";
	}

	@RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
	public @ResponseBody Object queryActivityForConvertByNameClueId(String activityName, String clueId) {
		// 封装参数
		Map<String, Object> map = new HashMap<>();
		map.put("activityName", activityName);
		map.put("clueId", clueId);
		// 调用service层方法,查询市场活动
		List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
		// 根据查询结果，返回反应信息
		return activityList;

	}

}
