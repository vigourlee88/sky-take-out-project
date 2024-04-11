package com.bjpowernode.crm.workbench.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.service.TranHistoryService;

@Service("tranHistoryService")
public class TranHistoryServiceImpl implements TranHistoryService {

	@Autowired
	private TranHistoryMapper tranHistoryMapper;

	@Override
	public List<TranHistory> queryTranHistoryForDetailByTranId(String tranId) {

		return tranHistoryMapper.selectTranHistoryForDetailByTranId(tranId);
	}

}
