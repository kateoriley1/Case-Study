package org.perscholas.Firstspringbootproject.dao;

import org.perscholas.Firstspringbootproject.models.CurrentMeals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentMealsRepo extends JpaRepository<CurrentMeals, Integer> {
    List<CurrentMeals> findAll();
}
