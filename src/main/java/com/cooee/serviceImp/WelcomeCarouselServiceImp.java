
package com.cooee.serviceImp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cooee.model.WelcomeCarousel;
import com.cooee.payload.WelcomeCarouselReqPayload;
import com.cooee.payload.WelcomeCarouselUpdateReqPayload;
import com.cooee.repository.WelcomeCarouselRepository;
import com.cooee.service.WelcomeCarouselService;
import com.cooee.util.Constant;
import com.cooee.util.FileUploader;

@Service
public class WelcomeCarouselServiceImp implements WelcomeCarouselService {

	@Value("${file-upload-location}")
	private String uploadDir;

	@Value("${file-location-url}")
	private String fileLocationURL;

	@Autowired
	private WelcomeCarouselRepository welcomeCarouselRepository;

	@Override
	public Map<String, Object> getAllWelcomeCarousel() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<WelcomeCarousel> list = welcomeCarouselRepository.findAll();
			if (list.size() > 0) {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.EMPTY);
				map.put(Constant.DATA, list);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, list);
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}

		return map;
	}

	@Override
	public Map<String, Object> addAllWelcomeCarousel(WelcomeCarouselReqPayload welcomeCarouselReqPayload,
			MultipartFile docfile) {
		Map<String, Object> map = new HashMap<String, Object>();

		// FileUploadDetail carousalFiles = null;
		// if (docfile != null && !docfile.isEmpty()) {
		// String filename =FileUploader.uploadProfileImage(docfile, UploadDir);
		// if (filename != "") {
		// carousalFiles = new FileUploadDetail(); // carousalFiles.setUrl(url +
		// filename);
		// carousalFiles.setIsInUsed(false);
		// carousalFiles = fileUploadRepo.save(carousalFiles);
		// // map.put(IConstant.RESPONSE, IConstant.SUCCESS);
		// map.put(IConstant.MESSAGE, "login successfuly");

		// WelcomeCarouselReqPayload welcomeCarouselReqPayloadJson = new
		// WelcomeCarouselReqPayload();

		try {

//			ObjectMapper objectMapper = new ObjectMapper();

			// welcomeCarouselReqPayloadJson =
			// objectMapper.readValues(welcomeCarouselReqPayload,
			// WelcomeCarouselReqPayload.class);

//			String url = "";
			System.out.println(uploadDir);
//			System.out.println(url); //
//			WelcomeCarousel carousalFiles = null;
			String filename = "";

			if (docfile != null && !docfile.isEmpty()) {
				filename = FileUploader.uploadProfileImage(docfile, uploadDir);

			}

			// if (filename != "") { // carousalFiles = new FileUploadDetail();
			// carousalFiles.setUrl(url + filename);
			// carousalFiles.setIsInUsed(false);
			//// carousalFiles = fileUploadRepo.save(carousalFiles);

			if (filename != "" && welcomeCarouselReqPayload != null) {

				WelcomeCarousel welcomeCarousel = new WelcomeCarousel();
				welcomeCarousel.setCreationDate(new Date());
				welcomeCarousel.setDescriptions(welcomeCarouselReqPayload.getDescriptions());
				welcomeCarousel.setImgUrl(fileLocationURL + filename);
				welcomeCarousel.setIsDeleted(Constant.FALSE); //
				welcomeCarousel.setLastUpdationDate(new Date());
				welcomeCarousel.setTitle(welcomeCarouselReqPayload.getTitle());
				welcomeCarousel.setTitle_font_size(welcomeCarouselReqPayload.getTitle_font_size());
				welcomeCarousel.setTitle_font_style(welcomeCarouselReqPayload.getTitle_font_style());
				welcomeCarousel.setTitle_font_color(welcomeCarouselReqPayload.getTitle_font_color());
				welcomeCarousel.setDescriptions_font_size(welcomeCarouselReqPayload.getDescriptions_font_size());
				welcomeCarousel.setDescriptions_font_style(welcomeCarouselReqPayload.getDescriptions_font_style());
				welcomeCarousel.setDescriptions_font_color(welcomeCarouselReqPayload.getDescriptions_font_color());

				welcomeCarousel = welcomeCarouselRepository.save(welcomeCarousel);
				map.put(Constant.DATA, welcomeCarousel);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.WELCOME_CAROUSEL_ADDED_SUCCESS_MESSAGE);

			} else {

				map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, null);

			}

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}

		return map;
	}

	@Override
	public Map<String, Object>

			updateWelcomeCarousel(WelcomeCarouselUpdateReqPayload welcomeCarouselUpdateReqPayload,
					MultipartFile docfile) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			String filename = "";
			WelcomeCarousel welcomeCarousel = welcomeCarouselRepository
					.findByCaroselId(welcomeCarouselUpdateReqPayload.getCaroselId());

			if (welcomeCarousel != null) {
				if (docfile != null && !docfile.isEmpty()) {

					filename = FileUploader.uploadProfileImage(docfile, uploadDir);

					welcomeCarousel.setDescriptions(welcomeCarouselUpdateReqPayload.getDescriptions());
					welcomeCarousel.setImgUrl(fileLocationURL + filename);
					welcomeCarousel.setIsDeleted(Constant.FALSE);
					welcomeCarousel.setLastUpdationDate(new Date());
					welcomeCarousel.setTitle(welcomeCarouselUpdateReqPayload.getTitle());
					welcomeCarousel.setTitle_font_size(welcomeCarouselUpdateReqPayload.getTitle_font_size());
					welcomeCarousel.setTitle_font_style(welcomeCarouselUpdateReqPayload.getTitle_font_style());
					welcomeCarousel.setTitle_font_color(welcomeCarouselUpdateReqPayload.getTitle_font_color());
					welcomeCarousel
							.setDescriptions_font_size(welcomeCarouselUpdateReqPayload.getDescriptions_font_size());
					welcomeCarousel
							.setDescriptions_font_style(welcomeCarouselUpdateReqPayload.getDescriptions_font_style());
					welcomeCarousel
							.setDescriptions_font_color(welcomeCarouselUpdateReqPayload.getDescriptions_font_color());
				} else {
					welcomeCarousel.setDescriptions(welcomeCarouselUpdateReqPayload.getDescriptions());
					welcomeCarousel.setImgUrl(welcomeCarousel.getImgUrl());
					welcomeCarousel.setIsDeleted(Constant.FALSE);
					welcomeCarousel.setLastUpdationDate(new Date());
					welcomeCarousel.setTitle(welcomeCarouselUpdateReqPayload.getTitle());
					welcomeCarousel.setTitle_font_size(welcomeCarouselUpdateReqPayload.getTitle_font_size());
					welcomeCarousel.setTitle_font_style(welcomeCarouselUpdateReqPayload.getTitle_font_style());
					welcomeCarousel.setTitle_font_color(welcomeCarouselUpdateReqPayload.getTitle_font_color());
					welcomeCarousel
							.setDescriptions_font_size(welcomeCarouselUpdateReqPayload.getDescriptions_font_size());
					welcomeCarousel
							.setDescriptions_font_style(welcomeCarouselUpdateReqPayload.getDescriptions_font_style());
					welcomeCarousel
							.setDescriptions_font_color(welcomeCarouselUpdateReqPayload.getDescriptions_font_color());
				}
				WelcomeCarousel WelcomeCarousel2 = new WelcomeCarousel();
				BeanUtils.copyProperties(welcomeCarousel, WelcomeCarousel2);
				WelcomeCarousel2 = welcomeCarouselRepository.save(WelcomeCarousel2);

				map.put(Constant.DATA, WelcomeCarousel2);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.WELCOME_CAROUSEL_UPDATED_SUCCESS_MESSAGE);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, null);
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public Map<String, Object> deleteWelcomeCarousel(Long caroselId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (caroselId != null) {
				WelcomeCarousel welcomeCarousel1 = welcomeCarouselRepository.findByCaroselId(caroselId);
//				welcomeCarouselRepository.deleteById(caroselId);

				if (welcomeCarousel1 != null)
					welcomeCarouselRepository.delete(welcomeCarousel1);

				map.put(Constant.MESSAGE, "delete successfull");
				map.put(Constant.RESPONSE_CODE, Constant.OK);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, null);
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

}
