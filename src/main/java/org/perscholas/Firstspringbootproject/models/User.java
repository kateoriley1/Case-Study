package org.perscholas.Firstspringbootproject.models;


import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j

public class User {

        @Id
//        @NotNull
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;

        @NotBlank(message="Please enter your first name.")
        @NonNull
        String firstname;

        @NotBlank(message="Please enter your last name.")
        @NonNull
        String lastname;

        @NotBlank(message="Please enter your email.")
        @NonNull
        String email;

        @NotBlank(message = "Please enter a username.")
        @NonNull
        String username;

        @NonNull
        //@Pattern(message="Password must be atleast 8 characters, 1 Uppercase, 1 Lowercase, 1 digit, and a special character.", regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
        String password;

        @ManyToMany (cascade = CascadeType.ALL)
        List<Meal> meals;

        Integer totalmealsdonated;


}
