package com.example.springbootkata.services;

import com.example.springbootkata.dao.UserDao;
import com.example.springbootkata.models.Role;
import com.example.springbootkata.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    private UserDao dao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserServiceImpl(UserDao dao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.dao = dao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void initAdmin(){
//        User admin = new User();
//        admin.setEmail("admin@admin.ru");
//        admin.setPassword(bCryptPasswordEncoder.encode("admin"));
//        Set<Role> roleSet = new HashSet<>();
//        Role adminRole = new Role()
//        dao.addUser(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return dao.getUserById(id);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        User userFromDB = dao.getUserByEmail(user.getEmail());

        if (userFromDB != null) {
            throw new RuntimeException("User already exists");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        dao.addUser(user);
    }

    @Override
    @Transactional
    public void updateUser(int id, User user) {
        dao.update(id, user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        dao.deleteUser(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return dao.getUserByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return user;
    }
}
