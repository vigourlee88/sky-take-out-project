package com.bjpowernode.crm.workbench.web.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;

@Controller
public class ActivityRemarkController {

	@Autowired
	private ActivityRemarkService activityRemarkService;

	@RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
	public @ResponseBody Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session) {
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		remark.setId(UUIDUtils.getUUID());
		remark.setCreateTime(DateUtils.formateDateTime(new Date()));
		remark.setCreateBy(user.getId());
		remark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);

		ReturnObject returnObject = new ReturnObject();
		try {
			// 调用service层方法，保存创建的市场活动备注
			int ret = activityRemarkService.saveCreateActivityRemark(remark);

			// 根据处理结果，返回响应信息
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				// 返回拼接的对象
				returnObject.setRetData(remark);
			} else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙，请稍后重试....");
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙，请稍后重试....");

		}

		return returnObject;
	}
}