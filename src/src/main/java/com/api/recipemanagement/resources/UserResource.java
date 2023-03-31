package com.api.recipemanagement.resources;

import com.api.recipemanagement.Constants;
import com.api.recipemanagement.domain.User;
import com.api.recipemanagement.exceptions.AuthException;
import com.api.recipemanagement.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(RecipeResource.class);

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
        String userName = (String) userMap.get("userName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        logger.info("REQUEST :: registerUser with userName: " + userName + " and email: " + email);
        User user = userService.registerUser(userName, email, password);
        logger.info("RESPONSE :: user created with userId: " + user.getUserId() + " for userName: " + userName + " and email: " + email);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap)  throws AuthException {
        String userName = (String) userMap.get("userName");
        String password = (String) userMap.get("password");
        logger.info("REQUEST :: loginUser for userName: " + userName);
        User user = userService.validateUser(userName, password);
        logger.info("RESPONSE :: token generated for userName: " + userName);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.TOKEN_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getUserId())
                .claim("userName", user.getUserName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

}
