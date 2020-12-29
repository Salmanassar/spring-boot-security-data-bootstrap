package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dao.DaoUser;
import web.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private DaoUser daoUser;

    @Autowired
    public void setDaoUser(DaoUser daoUser) {
        this.daoUser = daoUser;
    }

    @Override
    public User createUser(User user) {
        return daoUser.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return daoUser.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        daoUser.deleteUser(id);
    }

    @Override
    public List<User> listUsers() {
        return daoUser.listUsers();
    }

    @Override
    public Optional <User> findUserByEmail(String email) {
        return daoUser.findUserByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return daoUser.getUserById(id).get();
    }

}
