package com.cooee.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.AccountNumberDetails;
import com.cooee.model.VirtualNumbers;
@Repository
public interface VirtualNumbersRepository extends JpaRepository<VirtualNumbers, Long> {

	List<VirtualNumbers> findAllByOrderByCreationDateDesc(Pageable paging);

	long countByCreationDateBetween(Date from, Date to);

	List<VirtualNumbers> findAllByOrderByCreationDateDesc();

	VirtualNumbers findByVirtualNumber(String virtualNumber);

	List<VirtualNumbers> findByNumberTypeOrderByCreationDateAsc(String type);

	//long countByCallDestinationAndCallDestinationId(String string, String valueOf);

	//VirtualNumbers findTopByCallDestinationAndCallDestinationIdOrderByIdAsc(String string, String valueOf);

	//List<VirtualNumbers> findByCallDestinationAndCallDestinationIdIn(String string, List<String> ids);

	List<VirtualNumbers> findByIsAvailable(Boolean isAvailable);
	
	List<VirtualNumbers> findByUserId(Long id);
	
	List<VirtualNumbers>  findAllByOrderByIdDesc();

	List<VirtualNumbers> findByIsAvailableOrderByIdDesc(boolean b);
}
