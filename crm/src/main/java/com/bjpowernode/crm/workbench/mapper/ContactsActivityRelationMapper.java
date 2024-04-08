package com.bjpowernode.crm.workbench.mapper;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ContactsActivityRelation;

public interface ContactsActivityRelationMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts_activity_relation
	 *
	 * @mbg.generated Fri Apr 05 23:52:41 JST 2024
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts_activity_relation
	 *
	 * @mbg.generated Fri Apr 05 23:52:41 JST 2024
	 */
	int insert(ContactsActivityRelation row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts_activity_relation
	 *
	 * @mbg.generated Fri Apr 05 23:52:41 JST 2024
	 */
	int insertSelective(ContactsActivityRelation row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts_activity_relation
	 *
	 * @mbg.generated Fri Apr 05 23:52:41 JST 2024
	 */
	ContactsActivityRelation selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts_activity_relation
	 *
	 * @mbg.generated Fri Apr 05 23:52:41 JST 2024
	 */
	int updateByPrimaryKeySelective(ContactsActivityRelation row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts_activity_relation
	 *
	 * @mbg.generated Fri Apr 05 23:52:41 JST 2024
	 */
	int updateByPrimaryKey(ContactsActivityRelation row);

	/**
	 * 批量保存创建的联系人和市场活动的关联关系
	 * 
	 * @param list
	 * @return
	 */
	int insertContactsActivityRelationByList(List<ContactsActivityRelation> list);
}