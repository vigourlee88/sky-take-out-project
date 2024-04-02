package com.bjpowernode.crm.workbench.service;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationService {
	// 批量保存线索关联市场活动
	int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);

	int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
