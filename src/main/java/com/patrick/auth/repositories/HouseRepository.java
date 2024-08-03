package com.patrick.auth.repositories;


import com.patrick.auth.entities.House;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends ListCrudRepository<House, Integer> {

    House findByName(String name);
}
