package com.cqfangxin.dao.impl;

import com.cqfangxin.dao.UserDAO;
import com.cqfangxin.domain.LoginBean;
import com.cqfangxin.domain.User;
import com.cqfangxin.utils.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUser(LoginBean loginUser){
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        return jdbcTemplate.query("select * from user where username = ? and password = ? and type != 0 and status = 1",
               new Object[]{username, password}, new UserRowMapper());
    }

    @Override
    public int getNumOfUser(){
        return jdbcTemplate.queryForObject("select count(1) from user where type = 0",
                new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                        Integer numOfUser = resultSet.getInt(1);
                        return numOfUser;
                    }
                });
    }

    @Override
    public List<User> getUsers(Integer type){
        return jdbcTemplate.query("select * from user where type = ? order by last_modified_date desc",
                new Object[]{type}, new UserRowMapper());
    }

    @Override
    public User getUserById(Integer userId) {
        return jdbcTemplate.queryForObject("select * from user where id = ?", new Object[]{userId},
                new UserRowMapper());
    }

    @Override
    public int editUser(User user, String editUser){
        Integer userId = user.getId();
        if(userId == null){
            String username = user.getUsername();
            int userCount = jdbcTemplate.queryForObject("select count(1) from user where username = ?",
                    new Object[]{username}, new RowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                            Integer numOfUsers = resultSet.getInt(1);
                            return numOfUsers;
                        }
                    });
            if(userCount != 0){
                return -1;
            }else {
                return  jdbcTemplate.update("insert into user(type, username, nickname, password, telephone, email," +
                        "status, source, created_by, created_date, last_modified_by, last_modified_date) values (?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{user.getType(), user.getUsername(), user.getNickname(),
                        user.getPassword(), user.getTelephone(), user.getEmail(), user.getStatus(), user.getSource(), editUser,
                        TimeFormatUtil.formatTime(), editUser, TimeFormatUtil.formatTime()});
            }
        }else{
            return jdbcTemplate.update("update user set nickname = ?, realname = ?, sex = ?,  telephone = ?, email = ? where id = ?",
                    new Object[]{user.getNickname(), user.getRealname(), user.getSex(), user.getTelephone(), user.getEmail(), user.getId()});
        }
    }

    @Override
    public int deleteUserById(Integer id){
        return jdbcTemplate.update("delete from user where id = ?", new Object[]{id});
    }

    @Override
    public int editPassword(User user) {
        return jdbcTemplate.update("update user set password = ? where id = ?", new Object[]{user.getPassword(),
                user.getId()});
    }

    class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException{
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setType(resultSet.getInt("type"));
            user.setUsername(resultSet.getString("username"));
            user.setNickname(resultSet.getString("nickname"));
            user.setRealname(resultSet.getString("realname"));
            user.setSex(resultSet.getString("sex"));
            user.setTelephone(resultSet.getString("telephone"));
            user.setEmail(resultSet.getString("email"));
            user.setStatus(resultSet.getInt("status"));
            user.setSource(resultSet.getString("source"));
            user.setCreateBy(resultSet.getString("created_by"));
            user.setCreateDate(TimeFormatUtil.timeFormat(resultSet.getString("created_date")));
            user.setLastModifiedBy(resultSet.getString("last_modified_by"));
            user.setLastModifiedDate(TimeFormatUtil.timeFormat(resultSet.getString("last_modified_date")));
            return user;
        }
    }

}
