package com.bjpowernode.crm.workbench.service;

import java.util.Map;

import com.bjpowernode.crm.workbench.domain.Clue;

public interface ClueService {
	int saveCreateClue(Clue clue);

	Clue queryClueForDetailById(String id);

	// 保存线索的方法
	void saveConvertClue(Map<String, Object> map);

}
