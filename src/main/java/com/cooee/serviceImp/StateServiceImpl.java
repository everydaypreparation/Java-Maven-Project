package com.cooee.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cooee.model.State;
import com.cooee.repository.StateRepository;
import com.cooee.service.StateService;

@Service
public class StateServiceImpl implements StateService {
	@Autowired
	private StateRepository stateRepository;

	@Override
	public List<State> findByCountryIdOrderByIdAsc(Long id) {

		Sort sort = Sort.by("name").ascending();
		return stateRepository.findByCountryId(id, sort);

	}
}
