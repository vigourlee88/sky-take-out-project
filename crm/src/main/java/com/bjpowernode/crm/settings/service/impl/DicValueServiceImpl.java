package com.bjpowernode.crm.settings.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicValueService;

@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {

	@Autowired
	private DicValueMapper dicValueMapper;

	@Override
	public List<DicValue> queryDicValueByTypeCode(String typeCode) {

		return dicValueMapper.selectDicValueByTypeCode(typeCode);
	}

}
