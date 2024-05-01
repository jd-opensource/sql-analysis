package com.jd.sql.analysis_samples.mapper;

import com.jd.sql.analysis_samples.po.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Task task);

    Task selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Task task);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param task 实例对象
     * @return 对象列表
     */
    List<Task> queryAll(Task task);

    List<Task> queryAllM(@Param("task") Task task, @Param("id")Long id);

}