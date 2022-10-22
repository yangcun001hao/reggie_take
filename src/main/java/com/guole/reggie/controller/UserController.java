package com.guole.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guole.reggie.common.R;
import com.guole.reggie.entity.User;
import com.guole.reggie.service.UserService;
import com.guole.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 郭乐
 * @date 2022/10/20
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码短信
     *
     * @param user 用户
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成验证码
            String s = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("生成的验证码是：{}",s);

            //发送验证码

            //需要将生成验证码保存起来
            session.setAttribute(phone,s);
            return R.success("短信发送成功");
        }

        return R.error("短信发送失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //进行验证码比对
        Object codeInSession = session.getAttribute(phone);
        if (codeInSession != null && codeInSession.equals(code)){
            //登录成功
            //手机号是否为新用户,如果为新用户，自动注册
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            User user = userService.getOne(wrapper);
            if (user == null ) {
                //新用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }

        //登录失败

        return R.error("登录失败");
    }
}
