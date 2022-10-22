package com.guole.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guole.reggie.entity.Employee;
import com.guole.reggie.mapper.EmployeeMapper;
import com.guole.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author 郭乐
 * @date 2022/10/12
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
