package com.cooee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.Sms;

@Repository
public interface SmsRepository extends JpaRepository<Sms, Long> {

	Integer countByUserId(Long id);

	List<Sms> findByUserId(Long id);

	//Object findByIdAndUserId(Long id);

	//void findByIdAndUserId(Long id, Long userId);

	Sms findByIdAndUserId(Long id, Long userId);

	//List<Sms> findAllByUserId(Long id);

	List<Sms> findAllByUserIdAndNumber(Long user_id, String did);

	List<Sms> findByUserIdOrderByIdDesc(Long id);
}
