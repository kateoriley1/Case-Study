package org.perscholas.Firstspringbootproject.services;

import org.perscholas.Firstspringbootproject.dao.MealRepo;
import org.perscholas.Firstspringbootproject.models.Meal;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MealServices {

    private MealRepo mealRepo;

    @Autowired
    public MealServices(MealRepo mealRepo){
        this.mealRepo = mealRepo;
    }

    //one of methods you don't need to type is save

    List<Meal> findAll(){
       return mealRepo.findAll();
    }

}
