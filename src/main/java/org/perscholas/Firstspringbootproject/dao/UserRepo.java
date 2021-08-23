package org.perscholas.Firstspringbootproject.dao;

import org.perscholas.Firstspringbootproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    List<User> getByUsernameAndPassword(String username, String Password);
    List<User> findAll();
}
