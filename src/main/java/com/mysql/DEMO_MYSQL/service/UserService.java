package com.mysql.DEMO_MYSQL.service;

import com.mysql.DEMO_MYSQL.dto.request.UserCreationRequest;
import com.mysql.DEMO_MYSQL.entity.User;
import com.mysql.DEMO_MYSQL.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request) {
        User user = new User();
        user.setUserName(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassWord(request.getPassWord());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return  userRepository.findAll();

    }
}
