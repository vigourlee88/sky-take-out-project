package com.bjpowernode.crm.workbench.service;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.TranRemark;

public interface TranRemarkService {
	List<TranRemark> queryTranRemarkForDetailByTranId(String tranId);

}
