package com.anyi.reggie.controller;


import cn.hutool.core.util.RandomUtil;
import com.anyi.reggie.common.R;
import com.anyi.reggie.dto.LoginDto;
import com.anyi.reggie.entity.User;
import com.anyi.reggie.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author anyi
 * @since 2022-05-25
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private RedisTemplate myRedisTemplate;

    @Resource
    private UserService userService;

    /**
     * 获取验证码
     * @param phone
     * @return
     */
    @GetMapping("/code")
    public R getCode(String phone){
        String code = RandomUtil.randomNumbers(6);
        myRedisTemplate.opsForValue().set("code", code,5, TimeUnit.MINUTES);
        return R.success("发送验证码成功！");
    }
    @PostMapping("/login")
    public R login(@RequestBody LoginDto loginDto, HttpSession session){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        User user = userService.getOne(wrapper.eq(User::getPhone, loginDto.getPhone()));
        String  is = (String)myRedisTemplate.opsForValue().get("code");
        if (loginDto.getCode().equals(is)){
            User curUser = new User();
            if (user == null){
                curUser.setPhone(loginDto.getPhone());
                curUser.setStatus(1);
                userService.save(curUser);
            }else {
                curUser = user;
            }
            session.setAttribute("user", curUser);
            return R.success(user);
        }
        return R.error("登录失败");
    }
}

