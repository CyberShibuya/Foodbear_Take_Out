package com.bear.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bear.common.Result;
import com.bear.entity.Employee;
import com.bear.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        // now this employee object have only two properties(username and password)
        // 1, use md5 to encrypt the password submitted from user login page
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2, query full employee object from database
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        log.info("get query"+queryWrapper);
        Employee emp = employeeService.getOne(queryWrapper);
        log.info("get employee successfully");

        if (emp == null) {
            return Result.error("Account not found");
        }
        if (!emp.getPassword().equals(password)){
            log.info("password wrong");
            return Result.error("Password not correct");
        }
        if (emp.getStatus() == 0){
            return Result.error("account is disabled");
        }

        // 4, save account id in Session
        request.getSession().setAttribute("employee", emp.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("logout successfully");
    }



    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("new employee added: {}", employee.toString());



        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return Result.success("add successfully");
    }

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){
        log.info("page: {}, pagesize:{}, name:{}", page, pageSize, name);
        //Construct pagination builder
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //Construct query wrapper
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        //add filtering condition
        wrapper.like(!(name == null || "".equals(name)), Employee::getName, name);
        //sort the query results in descending order based on the update time
        wrapper.orderByDesc(Employee::getUpdateTime);
        //Execute mybatis query
        employeeService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    @PutMapping
    public Result<String> update(@RequestBody Employee employee, HttpServletRequest request){
        log.info(employee.toString());
        Long id = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(id);
//        employee.setUpdateTime(LocalDateTime.now());

        long threadID = Thread.currentThread().getId();
        log.info("current thread id is {}", threadID);

        employeeService.updateById(employee);
        return Result.success("updated status successfully");
    }

    @GetMapping("/{id}")
    public Result<Employee> edit(@PathVariable Long id){
        log.info("query employee by id");
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }
}
