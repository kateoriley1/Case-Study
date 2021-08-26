package org.perscholas.Firstspringbootproject.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrentMeals {
    @Id
    Integer mealcode;

    Boolean highlighted;

    public CurrentMeals(Integer mealcode) {
        this.mealcode = mealcode;
    }
}
