package com.cooee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooee.model.DeletedSms;

public interface DeletedSmsRepository extends JpaRepository<DeletedSms, Long>{

}
