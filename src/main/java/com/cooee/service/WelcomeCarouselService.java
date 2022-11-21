package com.cooee.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cooee.payload.WelcomeCarouselReqPayload;
import com.cooee.payload.WelcomeCarouselUpdateReqPayload;

public interface WelcomeCarouselService {

	Map<String, Object> getAllWelcomeCarousel();

	Map<String, Object> addAllWelcomeCarousel(WelcomeCarouselReqPayload welcomeCarouselReqPayload,
			MultipartFile docfile);

	Map<String, Object> updateWelcomeCarousel(WelcomeCarouselUpdateReqPayload welcomeCarouselUpdateReqPayload,
			MultipartFile docfile);

	Map<String, Object> deleteWelcomeCarousel(Long caroselId);
}
