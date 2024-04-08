package com.bjpowernode.crm.workbench.mapper;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;

public interface CustomerRemarkMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_customer_remark
	 *
	 * @mbg.generated Fri Apr 05 00:28:41 JST 2024
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_customer_remark
	 *
	 * @mbg.generated Fri Apr 05 00:28:41 JST 2024
	 */
	int insert(CustomerRemark row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_customer_remark
	 *
	 * @mbg.generated Fri Apr 05 00:28:41 JST 2024
	 */
	int insertSelective(CustomerRemark row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_customer_remark
	 *
	 * @mbg.generated Fri Apr 05 00:28:41 JST 2024
	 */
	CustomerRemark selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_customer_remark
	 *
	 * @mbg.generated Fri Apr 05 00:28:41 JST 2024
	 */
	int updateByPrimaryKeySelective(CustomerRemark row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_customer_remark
	 *
	 * @mbg.generated Fri Apr 05 00:28:41 JST 2024
	 */
	int updateByPrimaryKey(CustomerRemark row);

	/**
	 * 批量保存创建的客户备注
	 * 
	 * @param list
	 * @return
	 */
	int insertCustomerRemarkByList(List<CustomerRemark> list);
}