package com.api.recipemanagement.repositories;

import com.api.recipemanagement.domain.User;
import com.api.recipemanagement.exceptions.AuthException;
import com.api.recipemanagement.exceptions.BadRequestException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO USERS(USER_ID, USER_NAME, EMAIL, PASSWORD) " +
            "VALUES(NEXTVAL('USERS_SEQ'), ?, ?, ?)";
    private static final String SQL_FIND_BY_USERNAME = "SELECT USER_ID, USER_NAME, EMAIL, PASSWORD " +
            "FROM USERS WHERE USER_NAME = ?";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(1) " +
            "FROM USERS WHERE USER_NAME = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, USER_NAME, EMAIL, PASSWORD " +
            "FROM USERS WHERE USER_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String userName, String email, String password) throws BadRequestException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,userName);
                ps.setString(2,email);
                ps.setString(3,hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");
        }
        catch (Exception e) {
            throw new BadRequestException("Invalid request");
        }
    }

    @Override
    public User findByUserAndPassword(String userName, String password) throws AuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_USERNAME, new Object[]{userName}, userRowMapper);
            if(!BCrypt.checkpw(password, user.getPassword()))
                throw new AuthException("Invalid Username/Password");
            return user;
        }
        catch (EmptyResultDataAccessException e) {
            throw new AuthException("Invalid Username/Password");
        }
    }

    @Override
    public Integer getCountByUserName(String userName) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_USERNAME, new Object[]{userName}, Integer.class);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId}, userRowMapper);
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getInt("USER_ID"),
                rs.getString("USER_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"));
    });

}
