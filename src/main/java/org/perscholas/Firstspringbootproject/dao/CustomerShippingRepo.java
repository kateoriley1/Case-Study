package org.perscholas.Firstspringbootproject.dao;

import org.perscholas.Firstspringbootproject.models.CustomerShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerShippingRepo extends JpaRepository<CustomerShipping, Integer> {
    List<CustomerShipping> findAll();
    @Query("select p.id from #{#entityName} p")
    List<Integer> getAllIds();
    CustomerShipping findByCustomerid(Integer id);
}
