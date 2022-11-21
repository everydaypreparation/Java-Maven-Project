package com.cooee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	//Country findbyIdAndName(Long id, String name);

	//Country findByIdAndName(Long id, String name);

	//List<Country> findAllByIdOrderByAsc(Long id);

}
