package com.cooee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.WelcomeCarousel;

@Repository
public interface WelcomeCarouselRepository extends JpaRepository<WelcomeCarousel, Long> {

	WelcomeCarousel findByCaroselId(Long caroselId);

}
