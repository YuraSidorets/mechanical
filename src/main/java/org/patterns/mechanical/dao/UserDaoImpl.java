package org.patterns.mechanical.dao;

import org.patterns.mechanical.model.Credentials;
import org.patterns.mechanical.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserDaoImpl implements UserDao {
    private AtomicInteger idGenerator;
    private List<User> items;

    public UserDaoImpl() {
        items = new ArrayList<>();
        idGenerator = new AtomicInteger(0);
    }


    @Override
    public List<User> findAll() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public Optional<User> findOne(long userId) {
        return items.stream().filter(r -> r.getId().equals(userId))
                .findFirst()
                .map(User::clone);
    }

    @Override
    public Optional<User> findOne(Credentials credentials) {
        return items.stream()
                .filter(r ->
                        r.getLogin().equals(credentials.getLogin()) &&
                                r.getPassword().equals(credentials.getPassword()))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.incrementAndGet());
        } else {
            items.removeIf(r -> r.getId().equals(user.getId()));
        }
        items.add(user.clone());
        return user;
    }

    @Override
    public void delete(long userId) {
        items.removeIf(r -> r.getId().equals(userId));
    }
}
