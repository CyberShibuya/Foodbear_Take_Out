package com.bear.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bear.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
