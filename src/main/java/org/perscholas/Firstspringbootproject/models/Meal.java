package org.perscholas.Firstspringbootproject.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meal {

    @Id
//    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer mealcode;

    @NotBlank
    @NonNull
    String mealname;

    @NonNull
    String mealdescription;
    @NonNull
    String mealinstructions;
    @NonNull
    String photourl;

    @ManyToMany(cascade = CascadeType.ALL)
    List<User> users;

}
