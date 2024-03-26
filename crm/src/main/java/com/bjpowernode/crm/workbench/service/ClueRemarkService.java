package com.bjpowernode.crm.workbench.service;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

public interface ClueRemarkService {
	List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);

}
