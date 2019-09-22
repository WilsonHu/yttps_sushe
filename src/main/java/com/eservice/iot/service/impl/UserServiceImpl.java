package com.eservice.iot.service.impl;

import com.eservice.iot.dao.UserMapper;
import com.eservice.iot.model.user.User;
import com.eservice.iot.service.UserService;
import com.eservice.iot.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Mr.Zhang
* @date 2019/07/14.
*/
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

    public User requestLogin(String account,String password){
        return  userMapper.requestLogin(account,password);
    }

    public List<User> getUserInfoByKey(String key) {
        return userMapper.getUserInfoByKey(key);
    }

}
