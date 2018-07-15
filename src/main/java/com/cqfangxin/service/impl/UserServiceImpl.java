package com.cqfangxin.service.impl;

import com.cqfangxin.dao.UserDAO;
import com.cqfangxin.domain.LoginBean;
import com.cqfangxin.domain.Result;
import com.cqfangxin.domain.User;
import com.cqfangxin.service.UserService;
import com.cqfangxin.utils.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUser(LoginBean loginUser){
       loginUser.setPassword(MD5.digest(loginUser.getPassword()));
       List<User> userList = userDAO.getUser(loginUser);
       if(CollectionUtils.isEmpty(userList)){
           logger.info("unable to find user");
           return null;
       }
       return userList.get(0);
    }

    @Override
    public List<User> getUsers(Integer type){
      return userDAO.getUsers(type);
    }

    @Override
    public User getUserById(Integer userId){
        return userDAO.getUserById(userId);
    }

    @Override
    public int editUser(User user, String username){
        if(user.getPassword() != null){
            user.setPassword(MD5.digest(user.getPassword()));
        }
        return userDAO.editUser(user, username);
    }

    @Override
    public int deleteUserById(Integer id){
        return userDAO.deleteUserById(id);
    }

    @Override
    public int editPassword(User user){
        user.setPassword(MD5.digest(user.getPassword()));
        return userDAO.editPassword(user);
    }
}
