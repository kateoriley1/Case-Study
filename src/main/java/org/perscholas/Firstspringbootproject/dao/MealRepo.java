package org.perscholas.Firstspringbootproject.dao;

import org.perscholas.Firstspringbootproject.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.List;

@Repository
public interface MealRepo extends JpaRepository<Meal, Integer> {
    List<Meal> findAll();
}
