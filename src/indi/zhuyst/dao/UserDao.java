package indi.zhuyst.dao;

import indi.zhuyst.pojo.User;

public class UserDao {

    public User get(){
        User user = new User();
        user.setUsername("假装是个用户");
        return user;
    }
}
