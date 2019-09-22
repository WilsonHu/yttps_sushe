package com.eservice.iot.dao;

import com.eservice.iot.core.Mapper;
import com.eservice.iot.model.user.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends Mapper<User> {
    User requestLogin(@Param("account")String account, @Param("password")String password);

    List<User> getUserInfoByKey(@Param("key") String key);
}