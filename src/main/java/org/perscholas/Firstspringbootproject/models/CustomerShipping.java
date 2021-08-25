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
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerShipping {
    @Id
    @NonNull
    Integer customerid;

    String customershipadd1;

    String customershipadd2;

    String customercity;

    String customerstate;

    String customerzip;

}
