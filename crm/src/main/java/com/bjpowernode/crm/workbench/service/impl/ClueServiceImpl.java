package com.bjpowernode.crm.workbench.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.ContactsActivityRelation;
import com.bjpowernode.crm.workbench.domain.ContactsRemark;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.mapper.ContactsActivityRelationMapper;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.mapper.ContactsRemarkMapper;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.CustomerRemarkMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.mapper.TranRemarkMapper;
import com.bjpowernode.crm.workbench.service.ClueService;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

	@Autowired
	private ClueMapper clueMapper;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private ContactsMapper contactsMapper;

	@Autowired
	private ClueRemarkMapper clueRemarkMapper;

	@Autowired
	private CustomerRemarkMapper customerRemarkMapper;

	@Autowired
	private ContactsRemarkMapper contactsRemarkMapper;

	@Autowired
	private ClueActivityRelationMapper clueActivityRelationMapper;

	@Autowired
	private ContactsActivityRelationMapper contactsActivityRelationMapper;

	@Autowired
	private TranMapper tranMapper;

	@Autowired
	private TranRemarkMapper tranRemarkMapper;

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
		// 在clueController里isCreateTran已经被封装到map中
		String isCreateTran = (String) map.get("isCreateTran");

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

		// 根据clueId查询该线索下所有的备注
		List<ClueRemark> crList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

		// 如果该线索下有备注，
		// 把该线索下所有的备注转换到客户备注表中一份,
		// 把该线索下所有的备注转换到联系人备注表中一份
		if (crList != null && crList.size() > 0) {
			// 遍历crList,封装客户备注
			CustomerRemark cur = null;
			ContactsRemark cor = null;

			// 生成客户备注List和联系人备注List
			List<CustomerRemark> curList = new ArrayList<>();
			List<ContactsRemark> corList = new ArrayList<>();

			for (ClueRemark cr : crList) {
				cur = new CustomerRemark();
				cur.setCreateBy(cr.getCreateBy());
				cur.setCreateTime(cr.getCreateTime());
				cur.setCustomerId(c.getId());
				cur.setEditBy(cr.getEditBy());
				cur.setEditFlag(cr.getEditFlag());
				cur.setEditTime(cr.getEditTime());
				cur.setId(UUIDUtils.getUUID());
				cur.setNoteContent(cr.getNoteContent());
				curList.add(cur);

				cor = new ContactsRemark();
				cor.setContactsId(co.getId());
				cor.setCreateBy(cr.getCreateBy());
				cor.setCreateTime(cr.getCreateTime());
				cor.setEditBy(cr.getEditBy());
				cor.setEditFlag(cr.getEditFlag());
				cor.setEditTime(cr.getEditTime());
				cor.setId(UUIDUtils.getUUID());
				cor.setNoteContent(cr.getNoteContent());
				corList.add(cor);

			}
			// 批量保存
			customerRemarkMapper.insertCustomerRemarkByList(curList);
			contactsRemarkMapper.insertContactsRemarkByList(corList);

		}
		// 根据clueId查询该线索和市场活动的关联关系
		List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

		// 遍历carList,把该线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
		if (carList != null && carList.size() > 0) {

			ContactsActivityRelation coar = null;
			List<ContactsActivityRelation> coarList = new ArrayList<>();

			for (ClueActivityRelation car : carList) {
				coar = new ContactsActivityRelation();
				coar.setActivityId(car.getActivityId());
				coar.setContactsId(co.getId());
				coar.setId(UUIDUtils.getUUID());
				coarList.add(coar);
			}

			contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
		}

		// 如果需要创建交易，
		// 则往交易表中添加一条记录,
		// 还需要把该线索下的备注转换到交易备注表中一份
		if ("true".equals(isCreateTran)) {
			Tran tran = new Tran();
			// String activityId = (String) map.get("activityId");
			// tran.setActivityId(activityId);
			tran.setActivityId((String) map.get("activityId"));
			tran.setContactsId(co.getId());
			tran.setCreateBy(user.getId());
			tran.setCreateTime(DateUtils.formateDateTime(new Date()));
			tran.setCustomerId(c.getId());
			tran.setExpectedDate((String) map.get("expectedDate"));
			tran.setId(UUIDUtils.getUUID());
			tran.setMoney((String) map.get("money"));
			tran.setName((String) map.get("name"));
			tran.setOwner(user.getId());
			tran.setStage((String) map.get("stage"));

			tranMapper.insertTran(tran);

			// 遍历线索备注的list
			if (crList != null && crList.size() > 0) {
				TranRemark tr = null;
				List<TranRemark> trList = new ArrayList<>();
				for (ClueRemark cr : crList) {
					tr = new TranRemark();
					tr.setCreateBy(cr.getCreateBy());
					tr.setCreateTime(cr.getCreateTime());
					tr.setEditBy(cr.getEditBy());
					tr.setEditFlag(cr.getEditFlag());
					tr.setEditTime(cr.getEditTime());
					tr.setId(UUIDUtils.getUUID());
					tr.setNoteContent(cr.getNoteContent());
					tr.setTranId(tran.getId());
					trList.add(tr);// 每生成一个交易的对象，封装到交易list中
				}

				tranRemarkMapper.insertTranRemarkByList(trList);
			}
		}

		// 删除该线索下所有的备注
		clueRemarkMapper.deleteClueRemarkByClueId(clueId);
		// 删除该线索和市场活动的关联关系
		clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
		// 删除线索
		clueMapper.deleteClueById(clueId);
	}

}
