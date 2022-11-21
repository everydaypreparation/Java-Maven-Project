package com.cooee.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploader {

	public static String uploadIdCard(MultipartFile mulfile, String path, Long userId) {

		if (!mulfile.isEmpty()) {
			Long timestamp = System.currentTimeMillis();
				String filename = mulfile.getOriginalFilename();
			String extension = FilenameUtils.getExtension(filename);
			filename = userId + "_" + timestamp.toString().concat("." + extension);
					try {
				byte[] bytes = mulfile.getBytes();

				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + filename, true));
				buffStream.write(bytes);
				buffStream.close();
				return filename;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "";

	}

	public static String uploadProfileImage(MultipartFile mulfile, String path) {

		if (!mulfile.isEmpty()) {
			Long timestamp = System.currentTimeMillis();

			String filename = mulfile.getOriginalFilename();
			String extension = FilenameUtils.getExtension(filename);
			path = path.trim();
			filename = timestamp.toString().concat("." + extension);
			filename=filename.trim();
			try {
				byte[] bytes = mulfile.getBytes();

				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + filename, true));
				buffStream.write(bytes);
				buffStream.close();
				return filename;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "";

	}

	public static String uploadCasesImage(MultipartFile mulfile, String path, Long custId, Long caseId) {

		if (!mulfile.isEmpty()) {
			Long timestamp = System.currentTimeMillis();

			String filename = mulfile.getOriginalFilename();
			String extension = FilenameUtils.getExtension(filename);

			filename = custId + "_" + caseId + "_" + timestamp.toString().concat("." + extension);

			try {
				byte[] bytes = mulfile.getBytes();

				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + filename, true));
				buffStream.write(bytes);
				buffStream.close();
				return filename;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "";

	}

	public String errorLogFileUpload(MultipartFile mulfile, String path, Long custId) {
		if (!mulfile.isEmpty()) {
			Long timestamp = System.currentTimeMillis();
			String originalFileName = mulfile.getOriginalFilename();
			String filename = custId + "_" + timestamp.toString() + "_" + originalFileName;
			try {
				byte[] bytes = mulfile.getBytes();
				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + filename, true));
				buffStream.write(bytes);
				buffStream.close();
				return filename;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}

}
