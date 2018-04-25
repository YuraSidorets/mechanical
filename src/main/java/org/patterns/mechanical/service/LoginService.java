package org.patterns.mechanical.service;

import org.patterns.mechanical.model.Credentials;
import org.patterns.mechanical.model.User;

public interface LoginService {
    User login(Credentials credentials);
}
