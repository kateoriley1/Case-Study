package org.perscholas.Firstspringbootproject.services;

import lombok.extern.slf4j.Slf4j;
import org.perscholas.Firstspringbootproject.dao.UserRepo;
import org.perscholas.Firstspringbootproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserServices {

    private UserRepo userRepo;
    private User user;

    @Autowired
    public UserServices(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public void saveUser(User user) {
        //check is email is long enough
        if (user.getEmail().length() < 3)
        {
            log.warn("Email is too short");
            return;
        }
        userRepo.save(user);
    }

    public List<User> getByUsernameAndPassword(String username, String password)
    {
        if (username == null || password == null) {
            return Collections.emptyList();
        }
        return userRepo.getByUsernameAndPassword(username, password);
    }

    public List<User> findAll()
    {
        return userRepo.findAll();
    }

    public Boolean isValidUser(List<User> users)
    {
        if(users.size() != 1)
        {
            return false;
        }
        return true;
    }

}
