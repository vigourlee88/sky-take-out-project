package com.bjpowernode.crm.workbench.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerMapper customerMapper;

//	@Override
//	public List<String> queryAllCustomerName() {
//		return customerMapper.selectAllCustomerName();
//	}

	@Override
	public List<String> queryCustomerNameByName(String name) {

		return customerMapper.selectCustomerNameByName(name);
	}
}
