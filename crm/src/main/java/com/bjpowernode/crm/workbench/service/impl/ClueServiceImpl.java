package com.bjpowernode.crm.workbench.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.service.ClueService;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

	@Autowired
	private ClueMapper clueMapper;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private ContactsMapper contactsMapper;

	@Override
	public int saveCreateClue(Clue clue) {

		return clueMapper.insertClue(clue);
	}

	@Override
	public Clue queryClueForDetailById(String id) {

		return clueMapper.selectClueForDetailById(id);
	}

	@Override
	public void saveConvertClue(Map<String, Object> map) {
		String clueId = (String) map.get("clueId");
		User user = (User) map.get(Contants.SESSION_USER);
		// 根究id查询线索的信息
		Clue clue = clueMapper.selectClueById(clueId);
		// 把线索中有关公司的信息转换到客户表中
		Customer c = new Customer();
		c.setAddress(clue.getAddress());
		c.setContactSummary(clue.getContactSummary());
		c.setCreateBy(user.getId());
		c.setCreateTime(DateUtils.formateTime(new Date()));
		c.setDescription(clue.getDescription());
		c.setId(UUIDUtils.getUUID());
		c.setName(clue.getCompany());
		c.setNextContactTime(clue.getNextContactTime());
		c.setOwner(user.getId());
		c.setPhone(clue.getPhone());
		c.setWebsite(clue.getWebsite());
		customerMapper.insertCustomer(c);
		// 把该线索中有关个人的信息转换到联系人表中
		Contacts co = new Contacts();
		co.setAddress(clue.getAddress());
		co.setAppellation(clue.getAppellation());
		co.setContactSummary(clue.getContactSummary());
		co.setCreateBy(user.getId());
		co.setCreateTime(DateUtils.formateDateTime(new Date()));
		co.setCustomerId(c.getId());
		co.setDescription(clue.getDescription());
		co.setEmail(clue.getEmail());
		co.setFullname(clue.getFullname());
		co.setId(UUIDUtils.getUUID());
		co.setJob(clue.getJob());
		co.setMphone(clue.getMphone());
		co.setNextContactTime(clue.getNextContactTime());
		co.setOwner(user.getId());
		co.setSource(clue.getSource());
		// 个人信息转入这个表中
		contactsMapper.insertContacts(co);

	}

}
