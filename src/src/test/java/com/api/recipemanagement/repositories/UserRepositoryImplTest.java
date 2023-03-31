package com.api.recipemanagement.repositories;

import com.api.recipemanagement.domain.User;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRepositoryImpl userRepository = new UserRepositoryImpl();

    private static final String SQL_CREATE = "INSERT INTO USERS(USER_ID, USER_NAME, EMAIL, PASSWORD) " +
            "VALUES(NEXTVAL('USERS_SEQ'), ?, ?, ?)";
    private static final String SQL_FIND_BY_USERNAME = "SELECT USER_ID, USER_NAME, EMAIL, PASSWORD " +
            "FROM USERS WHERE USER_NAME = ?";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(1) " +
            "FROM USERS WHERE USER_NAME = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, USER_NAME, EMAIL, PASSWORD " +
            "FROM USERS WHERE USER_ID = ?";

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getInt("USER_ID"),
                rs.getString("USER_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"));
    });

    User user1 = new User(2001,"Tony","Tony@mail.com","Tony@Pass");

    @BeforeEach
    void setMockOutput() {
        when(jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{user1.getUserId()}, userRowMapper)).thenReturn(user1);
        when(jdbcTemplate.queryForObject(SQL_FIND_BY_USERNAME, new Object[]{user1.getUserName()}, userRowMapper)).thenReturn(user1);
        when(jdbcTemplate.update(anyString())).thenReturn(2001);

    }

    @Test
    void create() {
        Integer newUserId = userRepository.create(user1.getUserName(),user1.getEmail(), user1.getPassword());
        System.out.println(newUserId);
    }

    @Test
    void findByUserAndPassword() {
        User foundUser = userRepository.findByUserAndPassword(user1.getUserName(), user1.getPassword());
        assertEquals(foundUser.getUserId(),user1.getUserId());
        assertEquals(foundUser.getUserName(),user1.getUserName());
    }

    @Test
    void getCountByUserName() {
        ReflectionTestUtils.setField(userRepository, "jdbcTemplate", jdbcTemplate);
        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.eq(Integer.class))).thenReturn(1);
        assertEquals(1,userRepository.getCountByUserName(user1.getUserName()));
    }

    @Test
    void findById() {
        User foundUser = userRepository.findById(user1.getUserId());
        assertEquals(foundUser.getUserId(),user1.getUserId());
        assertEquals(foundUser.getUserName(),user1.getUserName());
    }

}