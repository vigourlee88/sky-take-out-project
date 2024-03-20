package com.bjpowernode.crm.workbench.service;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

public interface ActivityRemarkService {
	List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

	int saveCreateActivityRemark(ActivityRemark activityRemark);
}
