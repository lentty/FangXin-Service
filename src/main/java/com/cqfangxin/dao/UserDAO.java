package com.cqfangxin.dao;

import com.cqfangxin.domain.LoginBean;
import com.cqfangxin.domain.User;

import java.util.List;

public interface UserDAO {
    List<User> getUser(LoginBean loginUser);
    int getNumOfUser();
    List<User> getUsers(Integer type);
    User getUserById(Integer userId);
    int editUser(User user, String username);
    int deleteUserById(Integer id);
    int editPassword(User user);
}
