package com.bjpowernode.crm.workbench.mapper;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_clue_activity_relation
	 *
	 * @mbg.generated Fri Mar 29 00:22:24 JST 2024
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_clue_activity_relation
	 *
	 * @mbg.generated Fri Mar 29 00:22:24 JST 2024
	 */
	int insert(ClueActivityRelation row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_clue_activity_relation
	 *
	 * @mbg.generated Fri Mar 29 00:22:24 JST 2024
	 */
	int insertSelective(ClueActivityRelation row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_clue_activity_relation
	 *
	 * @mbg.generated Fri Mar 29 00:22:24 JST 2024
	 */
	ClueActivityRelation selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_clue_activity_relation
	 *
	 * @mbg.generated Fri Mar 29 00:22:24 JST 2024
	 */
	int updateByPrimaryKeySelective(ClueActivityRelation row);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table tbl_clue_activity_relation
	 *
	 * @mbg.generated Fri Mar 29 00:22:24 JST 2024
	 */
	int updateByPrimaryKey(ClueActivityRelation row);

	/**
	 * 批量保存 创建的线索和市场活动的关联关系
	 * 
	 * @param list
	 * @return
	 */
	int insertClueActivityRelationByList(List<ClueActivityRelation> list);

	/**
	 * 根据clueId和activityId，删除线索和市场活动的关联关系
	 * 
	 * @param clueActivityRelation
	 * @return
	 */
	int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);

	/**
	 * 根据clueId查询线索和市场活动的关联关系
	 * 
	 * @param clueId
	 * @return
	 */
	List<ClueActivityRelation> selectClueActivityRelationByClueId(String clueId);

	/**
	 * 根据clueId来删除线索和市场活动的关联关系
	 * 
	 * @param clueId
	 * @return
	 */
	int deleteClueActivityRelationByClueId(String clueId);
}