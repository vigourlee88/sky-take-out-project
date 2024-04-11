package com.bjpowernode.crm.workbench.service;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.TranHistory;

public interface TranHistoryService {

	List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);
}
