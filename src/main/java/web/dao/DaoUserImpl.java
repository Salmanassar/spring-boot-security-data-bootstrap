package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class DaoUserImpl implements DaoUser {

    private final UserRepository userRepository;

    @Autowired
    public DaoUserImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (!isEmptyFields(user)) {
            throw new RuntimeException("There is an empty field(s)");
        } else if (findUserByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("There is the user with the same email");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User updatedUser) {
        User currentUser = userRepository.findById(updatedUser.getId()).get();
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setPassword(updatedUser.getPassword());
        currentUser.setRoles(updatedUser.getRoles());
        if (!isEmptyFields(updatedUser)) {
            throw new RuntimeException("There is an empty field(s)");
        }
        return userRepository.save(currentUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(getUserById(id).get());
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    private boolean isEmptyFields(User user) {
        return Stream.of(user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(), user.getRolesString())
                .noneMatch(String::isEmpty);
    }
}
