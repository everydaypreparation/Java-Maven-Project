package com.cooee.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooee.model.AccountNumberDetails;
import com.cooee.model.User;
import com.cooee.payload.EmailAndPasswordRequest;

@Repository
public interface UserRepository extends JpaRepository<User, Long> ,PagingAndSortingRepository<User, Long>{
	
	
	User findByEmailAndPassword(String email, String password);

	User findByEmail(String email);

	User findByEmailAndOtp(String email, Integer otp);
	
	Page<User> findByApproved(Integer approved, Pageable pageable);
	
	User findByDid(Long id);

	Long countByApproved(int i);

	//User findByEmailAndSocialId(String email, String socialId);

	Page<User> findByApprovedAndFirstNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(Integer approved, String keyword1, String keyword2, Pageable paging);

	Long countByApprovedAndFirstNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(Integer approved, String keyword1, String keyword2);

	Page<User> findAllByFirstNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(Pageable paging, String keyword,
			String keyword2);

	List<User> findByActiveAndIdIn(boolean b, Set<Long> ids);

	User findBySocialId(String socialId);
	User findByTempEmail(String email);
	
	User findFirstByOrderByIdDesc();

	User findByDid(String source);
	
}