package com.api.recipemanagement.repositories;

import com.api.recipemanagement.domain.User;
import com.api.recipemanagement.exceptions.AuthException;
import com.api.recipemanagement.exceptions.BadRequestException;

public interface UserRepository {

    Integer create(String userName, String email, String password) throws BadRequestException;

    User findByUserAndPassword(String userName, String password) throws AuthException;

    Integer getCountByUserName(String userName);

    User findById(Integer userId);

}
