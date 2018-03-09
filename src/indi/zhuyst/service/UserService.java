package indi.zhuyst.service;

import indi.zhuyst.dao.UserDao;
import indi.zhuyst.pojo.User;

public class UserService {

    private UserDao userDao;

    public User get(){
        return this.userDao.get();
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
