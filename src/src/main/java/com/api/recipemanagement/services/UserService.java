package com.api.recipemanagement.services;

import com.api.recipemanagement.domain.User;
import com.api.recipemanagement.exceptions.AuthException;
import com.api.recipemanagement.exceptions.BadRequestException;

public interface UserService {

    User registerUser(String userName, String email, String password) throws BadRequestException;

    User validateUser(String userName, String password) throws AuthException;

}
