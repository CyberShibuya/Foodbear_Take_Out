package com.bear.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bear.common.Result;
import com.bear.entity.User;
import com.bear.service.UserService;
import com.bear.utils.MailUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) throws MessagingException{
        String email = user.getEmail();
        log.info(email);

        if (!email.isEmpty()){
            //generate code
            String code = MailUtils.generateCode();
            log.info("generated code: {}",code);
            System.out.println(code);

            //send email
            MailUtils.sendMail(email, code);
            session.setAttribute(email, code);// save session for comparing this code with user's code
            return Result.success("verification message sent successfully");
        }
        return Result.error("fail to send message");
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session){
        String email = map.get("email").toString();
        String userCode = map.get("code").toString();
        //get the code saved in session
        String codeInSession = session.getAttribute(email).toString();

        //compare
        if (userCode != null && userCode.equals(codeInSession)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail, email);
            User user = userService.getOne(queryWrapper);

            // if user not exists, save into database directly
            if (user == null){
                user = new User();
                user.setEmail(email);
                userService.save(user);
                user.setName("user:"+ codeInSession);
            }
            session.setAttribute("user", user.getId());
            return Result.success(user);
        }
        return Result.error("login failed");
    }
}
