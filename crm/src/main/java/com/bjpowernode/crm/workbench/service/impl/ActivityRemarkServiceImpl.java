package com.bjpowernode.crm.workbench.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;

@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

	@Autowired
	private ActivityRemarkMapper activityRemarkMapper;

	@Override
	public List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId) {

		return activityRemarkMapper.selectActivityRemarkForDetailById(activityId);
	}

	@Override
	public int saveCreateActivityRemark(ActivityRemark activityRemark) {

		return activityRemarkMapper.insertActivityRemark(activityRemark);
	}

}
