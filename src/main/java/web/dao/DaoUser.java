package web.dao;


import web.model.User;

import java.util.List;
import java.util.Optional;


public interface DaoUser {

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> listUsers();

    Optional<User> findUserByEmail(String email);

    Optional<User> getUserById(Long id);
}
