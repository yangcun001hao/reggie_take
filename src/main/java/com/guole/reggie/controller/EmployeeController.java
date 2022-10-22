package com.guole.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guole.reggie.common.R;
import com.guole.reggie.entity.Employee;
import com.guole.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author 郭乐
 * @date 2022/10/12
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 用户登录
     *
     * @param request  请求
     * @param employee 员工
     * @return {@link R}<{@link Employee}>
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //将密码进行 加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());


        Employee emp = employeeService.getOne(queryWrapper);

        //查看用户是否存在
        if (emp == null) {
            return R.error("登录失败");
        }
        //查看密码是否错误
        if( !password.equals(emp.getPassword())){
            return R.error("密码错误");
        }
        //用户是否禁用
        if (emp.getStatus() == 0 ) {
            return R.error("用户被禁用");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return  R.success(emp);
    }


    /**
     * 注销用户
     *
     * @param request 请求
     * @return {@link R}<{@link String}>
     */
    @RequestMapping("/logout")
    public R<String> logOut(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     * 新增员工
     *
     * @param employee 员工
     * @return {@link R}<{@link String}>
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工");
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getUsername().getBytes()));

/*
        公共字段实现自动填充
        //创建时间
        employee.setCreateTime(LocalDateTime.now());
        //更新时间
        employee.setUpdateTime(LocalDateTime.now());
        //创建用户id
        Long id = (Long) request.getSession().getAttribute("employee");
        //添加创建用户id
        employee.setCreateUser(id);
        //添加更新用户id
        employee.setUpdateUser(id);

        */

        boolean save = employeeService.save(employee);


        if (save == true) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    /**
     * 将信息进行分页展示页面
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @param name     名字
     * @return {@link R}<{@link Page}>
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器
        Page p = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        //添加一个过滤条件
        lqw.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        lqw.orderByDesc(Employee::getUpdateTime);
        employeeService.page(p,lqw);
        return R.success(p);
    }

    /**
     * 修改员工信息
     *
     * @param request  请求
     * @param employee 员工
     * @return {@link R}<{@link String}>
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        Long id = (Long) request.getSession().getAttribute("employee");

        //获得线程id
        long threadId = Thread.currentThread().getId();
        log.info("controller employeeController 类中 线程id为：{}",threadId);

        /*
        公共字段实现自动填充
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(id);

        */

        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> byId(@PathVariable Long id){
        Employee byId = employeeService.getById(id);
        if (byId != null) {
            return R.success(byId);
        }
        return R.error("未知错误");
    }

}
