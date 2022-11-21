package com.cooee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.AccountNumberDetails;

@Repository
public interface AccountNumberRepository extends JpaRepository<AccountNumberDetails, Long> {

	AccountNumberDetails findFirstByOrderByIdDesc();
//	AccountNumber findTopByOrderBySeatNumberAsc();
	
	AccountNumberDetails findByUserId(Long id);

	AccountNumberDetails findBySipUsername(String to);

	AccountNumberDetails findByDidNumber(String did);
}