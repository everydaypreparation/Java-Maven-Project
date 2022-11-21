package com.cooee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.EmailTemplates;
import com.cooee.model.LoginDevice;
import com.cooee.model.User;

@Repository
public interface EmailTemplatesRepository extends JpaRepository<EmailTemplates, Long> {
	EmailTemplates findByEmailType(String type);

	

}