package com.bjpowernode.crm.workbench.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;

@Service("clueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {

	@Autowired
	private ClueRemarkMapper clueRemarkMapper;

	@Override
	public List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId) {

		return clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);
	}

}
