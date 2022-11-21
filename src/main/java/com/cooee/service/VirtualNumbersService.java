package com.cooee.service;

import java.util.List;
import java.util.Set;

import com.cooee.model.User;
import com.cooee.model.VirtualNumbers;
import com.cooee.payload.BulkUploadRequest;
import com.cooee.payload.MultipleResponse;
import com.cooee.payload.MultipleResponseBulkDID;

public interface VirtualNumbersService {

	List<VirtualNumbers> fetchVirtualNumbers(int pageFrom, int limit);

	VirtualNumbers addVirtualNumber(String virtualNumber, String country, String state, String ratecenter,
			String numberType);

	VirtualNumbers getByVirtualNumber(String virtualNumber);

	VirtualNumbers updateNote(String virtualNumber, String note);

	long countByUserId(long id);

	List<VirtualNumbers> findByUserId(long userId);

	VirtualNumbers findByUserIdOld(long userId);
	
	VirtualNumbers updateOrganizationNote(String virtualNumber, String orgNote);

	List<VirtualNumbers> getByTypeAndOrderByDate(String type);

	VirtualNumbers setUserOnNumber(String number, User user);

	List<VirtualNumbers> findAllAvailableNumbers();

	MultipleResponse mapDID(String did, User user);

	List<VirtualNumbers> bulkUpload(BulkUploadRequest request);

	List<String> checkExistsBuld(BulkUploadRequest request);

}
