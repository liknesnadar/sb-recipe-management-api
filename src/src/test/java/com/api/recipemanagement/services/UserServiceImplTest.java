package com.api.recipemanagement.services;

import com.api.recipemanagement.domain.User;
import com.api.recipemanagement.repositories.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();

    User user1 = new User(2001,"Tony","Tony@mail.com","Tony@Pass");

    @BeforeEach
    void setMockOutput() {
        when(userRepository.findById(user1.getUserId())).thenReturn(user1);
        when(userRepository.findByUserAndPassword(user1.getUserName(),user1.getPassword())).thenReturn(user1);
        when(userRepository.getCountByUserName(user1.getUserName())).thenReturn(0);
        when(userRepository.create(user1.getUserName(),user1.getEmail(),user1.getPassword())).thenReturn(user1.getUserId());
    }

    @Test
    void registerUser() {
        User registerUser = userService.registerUser(user1.getUserName(),user1.getEmail(),user1.getPassword());
        assert(registerUser.getUserId() > 0);
        assertEquals(registerUser.getUserName(), user1.getUserName());
    }

    @Test
    void validateUser() {
        User validateUser = userService.validateUser(user1.getUserName(),user1.getPassword());
        assert(validateUser.getUserId() > 0);
        assertEquals(validateUser.getUserName(), user1.getUserName());
    }
}