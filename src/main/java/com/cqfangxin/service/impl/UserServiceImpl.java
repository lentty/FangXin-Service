package com.cqfangxin.service.impl;

import com.cqfangxin.dao.UserDAO;
import com.cqfangxin.domain.LoginBean;
import com.cqfangxin.domain.User;
import com.cqfangxin.service.UserService;
import com.cqfangxin.utils.MD5;
import com.cqfangxin.utils.WxMappingJackson2HttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private Environment env;

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

    @Override
    public int getUserId(String code) {
        String appId = env.getProperty("app.appId");
        String appSecrect = env.getProperty("app.appSecrect");
        String apiPrefix = "https://api.weixin.qq.com/sns/jscode2session?";
        String url = apiPrefix + "appid=" + appId + "&secret=" + appSecrect + "&js_code=" + code + "&grant_type=authorization_code";
        logger.info("wechat api url: " + url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        LoginBean loginBean = restTemplate.getForObject(url, LoginBean.class);
        if(loginBean != null){
            String openId = loginBean.getOpenid();
            logger.info("open id is: " + openId);
            return userDAO.saveOpenId(openId);
        }
        return -1;
    }
}
