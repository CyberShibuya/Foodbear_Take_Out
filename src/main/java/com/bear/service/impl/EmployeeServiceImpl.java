package com.bear.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bear.entity.Employee;
import com.bear.mapper.EmployeeMapper;
import com.bear.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
