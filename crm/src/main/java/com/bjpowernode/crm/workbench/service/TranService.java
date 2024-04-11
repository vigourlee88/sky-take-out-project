package com.bjpowernode.crm.workbench.service;

import java.util.Map;

import com.bjpowernode.crm.workbench.domain.Tran;

public interface TranService {
	void saveCreateTran(Map<String, Object> map);

	Tran queryTranForDetailById(String id);
}
