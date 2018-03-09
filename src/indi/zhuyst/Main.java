package indi.zhuyst;

import indi.zhuyst.ioc.ApplicationContext;
import indi.zhuyst.pojo.User;
import indi.zhuyst.service.UserService;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();
        UserService userService = (UserService) context.getBean("userService");

        User user = userService.get();
        System.out.println(user.getUsername());
    }
}
