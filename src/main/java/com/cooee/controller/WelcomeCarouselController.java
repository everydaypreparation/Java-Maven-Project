package com.cooee.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cooee.payload.WelcomeCarouselReqPayload;
import com.cooee.payload.WelcomeCarouselUpdateReqPayload;
import com.cooee.service.WelcomeCarouselService;
import com.cooee.util.Constant;

import io.swagger.v3.oas.annotations.Operation;
@CrossOrigin
@RestController
public class WelcomeCarouselController {

	@Autowired
	private WelcomeCarouselService welcomeCarouselService;

	@GetMapping(value = "/get_welcome_carousel")
//	@ApiOperation(value = "Get welcome screen content with img url,title,description")
	@Operation(summary = "Get Welcome Carousel", description = "Get welcome screen content with img url,title,description")
	public Map<String, Object> getWelcomeCarousel() {
		return welcomeCarouselService.getAllWelcomeCarousel();
	}

	@PostMapping(value = "/add_welcome_carousel", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	 //@ApiOperation(value = "Add welcome screen content")
	@Operation(summary = "Add Welcome Carousel", description = "Add welcome screen content")
	public Map<String, Object> addWelcomeCarousel(WelcomeCarouselReqPayload welcomeCarouselReqPayload,
			@RequestParam(value = "docfile", required = false) MultipartFile docfile) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (docfile != null && !docfile.isEmpty()) {
			return welcomeCarouselService.addAllWelcomeCarousel(welcomeCarouselReqPayload, docfile);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.WELCOME_CAROUSEL_FILE_NOT_FOUND_MESSAGE);
			return map;
		}
	}

	@PutMapping(value = "/update_welcome_carousel", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	// @ApiOperation(value = "Update welcome screen content")
	@Operation(summary = "Update Welcome Carousel", description = "Update welcome screen content")
	public Map<String, Object> updateWelcomeCarousel(WelcomeCarouselUpdateReqPayload welcomeCarouselUpdateReqPayload,
			@RequestPart(value = "docfile", required = false) MultipartFile docfile) {
		if (docfile != null && !docfile.isEmpty()) {
			return welcomeCarouselService.updateWelcomeCarousel(welcomeCarouselUpdateReqPayload, docfile);
		} else {
			return welcomeCarouselService.updateWelcomeCarousel(welcomeCarouselUpdateReqPayload, docfile);
		}
	}

	@DeleteMapping(value = "/delete_welcome_carousel")
	// @ApiOperation(value = "Delete welcome screen content")
	@Operation(summary = "Delete Welcome Carousel", description = "Delete welcome screen content")
	public Map<String, Object> deleteWelcomeCarousel(@RequestParam Long caroselId) {
		return welcomeCarouselService.deleteWelcomeCarousel(caroselId);
	}
}
