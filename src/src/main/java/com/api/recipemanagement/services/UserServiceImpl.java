package com.api.recipemanagement.services;

import com.api.recipemanagement.domain.User;
import com.api.recipemanagement.exceptions.AuthException;
import com.api.recipemanagement.exceptions.BadRequestException;
import com.api.recipemanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User registerUser(String userName, String email, String password) throws BadRequestException {
        if(userName == null || password == null)
            throw new BadRequestException("Invalid request");
        Integer count = userRepository.getCountByUserName(userName);
        if(count > 0)
            throw new BadRequestException("Username already in use");

        Integer userId = userRepository.create(userName, email, password);
        return userRepository.findById(userId);
    }

    @Override
    public User validateUser(String userName, String password) throws AuthException {
        if(userName == null || password == null)
            throw new BadRequestException("Invalid request");
        return userRepository.findByUserAndPassword(userName, password);
    }

}
