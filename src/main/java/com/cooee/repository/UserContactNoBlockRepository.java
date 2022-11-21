package com.cooee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.User;
import com.cooee.model.UserContactNoBlock;

@Repository
public interface UserContactNoBlockRepository extends JpaRepository<UserContactNoBlock, Long> {

	List<UserContactNoBlock> findByUserId(Long id);

	//UserContactNoBlock findByMobileNo(String number);

	UserContactNoBlock findByMobileNumberAndUserId(String number, Long userId);



}
