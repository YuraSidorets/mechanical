package org.patterns.mechanical.service;

import org.patterns.mechanical.dao.UserDao;
import org.patterns.mechanical.model.Credentials;
import org.patterns.mechanical.model.User;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private UserDao userDao;

    public LoginServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(Credentials credentials) {
        User user = userDao.findOne(credentials).orElseThrow(
                () -> new IllegalArgumentException("Invalid credentials"));
        if (!user.getLogin().equals(credentials.getLogin()) ||
                !user.getPassword().equals(credentials.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }
}
