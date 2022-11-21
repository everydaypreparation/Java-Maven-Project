package com.cooee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.LoginDevice;
import com.cooee.model.User;

@Repository
public interface LoginDeviceRepository extends JpaRepository<LoginDevice, Long> {
	//List<LoginDevice> findByUserId(Long id);

	List<LoginDevice> findAllByUser(User user);

	List<LoginDevice> findByUserId(Long id);

}