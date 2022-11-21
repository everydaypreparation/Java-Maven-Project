package com.cooee.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cooee.model.Country;
import com.cooee.repository.CountryRepository;
import com.cooee.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryRepository repo;

	@Override
	public List<Country> getAllCountry() {
		Sort sort = Sort.by("name").ascending();
		return repo.findAll(sort);
	}

}
