package com.cooee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.SendByEmail;
@Repository
public interface SendByEmailRepo extends JpaRepository<SendByEmail, Long> {

}
