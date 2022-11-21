package com.cooee.service;

import java.util.List;

import com.cooee.model.State;

public interface StateService {

	List<State> findByCountryIdOrderByIdAsc(Long id);

}
