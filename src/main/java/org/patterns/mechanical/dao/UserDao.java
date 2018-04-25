package org.patterns.mechanical.dao;

import org.patterns.mechanical.model.Credentials;
import org.patterns.mechanical.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll();
    Optional<User> findOne(long userId);
    Optional<User> findOne(Credentials credentials);
    User save(User user);
    void delete(long userId);
}
