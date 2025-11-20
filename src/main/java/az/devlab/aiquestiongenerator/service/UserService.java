package az.devlab.aiquestiongenerator.service;

import az.devlab.aiquestiongenerator.model.User;

import java.util.Optional;

public interface UserService {

    User getById(Long id);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    User save(User user);
}
