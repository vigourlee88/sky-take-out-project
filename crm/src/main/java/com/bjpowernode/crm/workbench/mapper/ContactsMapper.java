package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.Contacts;

public interface ContactsMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts
	 *
	 * @mbg.generated Thu Apr 04 22:59:24 JST 2024
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts
	 *
	 * @mbg.generated Thu Apr 04 22:59:24 JST 2024
	 */
	int insert(Contacts row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts
	 *
	 * @mbg.generated Thu Apr 04 22:59:24 JST 2024
	 */
	int insertSelective(Contacts row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts
	 *
	 * @mbg.generated Thu Apr 04 22:59:24 JST 2024
	 */
	Contacts selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts
	 *
	 * @mbg.generated Thu Apr 04 22:59:24 JST 2024
	 */
	int updateByPrimaryKeySelective(Contacts row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_contacts
	 *
	 * @mbg.generated Thu Apr 04 22:59:24 JST 2024
	 */
	int updateByPrimaryKey(Contacts row);

	/**
	 * 保存创建的联系人
	 * 
	 * @param contacts
	 * @return
	 */
	int insertContacts(Contacts contacts);
}