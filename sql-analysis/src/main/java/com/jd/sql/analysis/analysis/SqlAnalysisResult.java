package com.jd.sql.analysis.analysis;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author huhaitao21
 * @Description sql 分析结果dto
 * @Date 20:41 2022/11/1
 **/
@Setter
@Getter
public class SqlAnalysisResult {

		/**
		 * 执行序号
		 */
		private Long id;

		/**
		 * 查询类型
		 */
		private String selectType;

		/**
		 * 表名称
		 */
		private String table;


		/**
		 * 分区
		 */
		private String partitions;

		/**
		 * 访问类型
		 * 依次从好到差：system，const，eq_ref，ref，fulltext，ref_or_null，unique_subquery，index_subquery，range，index_merge，index，ALL，
		 * 除了all之外，其他的type都可以使用到索引，除了index_merge之外，其他的type只可以用到一个索引
		 */
		private String type;

		/**
		 * 查询可能使用到的索引都会在这里列出来,但不一 定被查询实际使用
		 */
		private String possibleKeys;

		/**
		 * 查询真正使用到的索引，select_type为index_merge时，这里可能出现两个以上的索引，其他的select_type这里只会出现一个。如果为NULL，则没有使用索引。
		 */
		private String key;

		/**
		 * 表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度
		 */
		private String keyLen;

		/**
		 * 如果是使用的常数等值查询，这里会显示const，如果是连接查询，被驱动表的执行计划这里会显示驱动表的关联字段，
		 * 如果是条件使用了表达式或者函数，或者条件列发生了内部隐式转换，这里可能显示为func
		 */
		private String ref;

		/**
		 * 这里是执行计划中估算的扫描行数，不是精确值
		 */
		private String rows;

		/**
		 * 返回结果的行数占读取行数的百分比，值越大越好
		 */
		private Double filtered;

		/**
		 * 重点关注：using filesort和using temporary
		 */
		private String extra;

}
