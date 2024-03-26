package com.bjpowernode.crm.settings.service;

import java.util.List;

import com.bjpowernode.crm.settings.domain.DicValue;

public interface DicValueService {

	List<DicValue> queryDicValueByTypeCode(String typeCode);

}
