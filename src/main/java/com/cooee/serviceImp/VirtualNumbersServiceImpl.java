package com.cooee.serviceImp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cooee.model.Incoming;
import com.cooee.model.User;
import com.cooee.model.VirtualNumbers;
import com.cooee.payload.BulkUploadRequest;
import com.cooee.payload.MultipleResponse;
import com.cooee.repository.VirtualNumbersRepository;
import com.cooee.service.UserService;
import com.cooee.service.VirtualNumbersService;
import com.cooee.service.asterisk.IncomingService;

@Service
public class VirtualNumbersServiceImpl implements VirtualNumbersService {

	@Autowired
	VirtualNumbersRepository virtualNumbersRepository;

	@Autowired
	UserService userService;

	@Autowired
	IncomingService incomingService;

	@Value("${asteriskUrl}")
	private String asteriskUrl;

	@Value("${asteriskApISecretKey}")
	private String asteriskApISecretKey;

	@Override
	public MultipleResponse mapDID(String did, User user) {
		MultipleResponse response = new MultipleResponse();

		VirtualNumbers number = virtualNumbersRepository.findByVirtualNumber(did);
		if (number != null) {
			if (number.getIsAvailable()) {
				number.setUser(user);
				number.setIsAvailable(false);
				virtualNumbersRepository.save(number);

				String incoming_desnitaion = "from-did-direct," + did + ",1";

				Incoming incFlag = null;
			//	if (number.getCallDestination().equalsIgnoreCase("")) {
					incFlag = incomingService.add("orgUserName", number.getVirtualNumber(), incoming_desnitaion,
							"orgName");
				
//				} else {
//					incFlag = incomingService.update(number.getVirtualNumber(), incoming_desnitaion);
//				}

				if (incFlag != null) {

					number.setCallDestinationId("" + user.getId());
					number.setSmsDestinationId("" + user.getId());

					if (virtualNumbersRepository.save(number) != null) {
						response.setType(1);
					} else {
						response.setType(2);
					}
				} else
					response.setType(3);

				response.setType(1);
			} else {
				response.setType(3);
			}
		} else {
			response.setType(2);
		}

		return response;
	}

	@Override
	public List<VirtualNumbers> fetchVirtualNumbers(int pageFrom, int limit) {
		Pageable paging = PageRequest.of(pageFrom, limit, Sort.by("voiceMailDate").descending());
		return virtualNumbersRepository.findAllByOrderByCreationDateDesc(paging);
	}

	@Override
	public VirtualNumbers addVirtualNumber(String virtualNumber, String country, String state, String ratecenter,
			String numberType) {

		try {
			VirtualNumbers newVirtualNumber = new VirtualNumbers();

			newVirtualNumber.setVirtualNumber(virtualNumber);
			newVirtualNumber.setState(state);
			newVirtualNumber.setRatecenter(ratecenter);
//			newVirtualNumber.setType("");


			newVirtualNumber.setCallDestinationId("");

			newVirtualNumber.setSmsDestinationId("");

			Timestamp currentDT = new Timestamp(System.currentTimeMillis());
			newVirtualNumber.setCreationDate(currentDT);
			newVirtualNumber.setActive(true);

			// User user = userRepository.findById(userId).get();

			newVirtualNumber.setNumberType(numberType);

			newVirtualNumber.setIsAvailable(true);
			newVirtualNumber.setCountry(country);
			return virtualNumbersRepository.save(newVirtualNumber);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public VirtualNumbers getByVirtualNumber(String virtualNumber) {
		return virtualNumbersRepository.findByVirtualNumber(virtualNumber);
	}

	@Override
	public VirtualNumbers updateNote(String virtualNumber, String note) {
		VirtualNumbers virtualNumbers = virtualNumbersRepository.findByVirtualNumber(virtualNumber);
		if (virtualNumbers != null) {
			return virtualNumbersRepository.save(virtualNumbers);
		}
		return null;
	}

	@Override
	public long countByUserId(long id) {
		return 0;
		//return virtualNumbersRepository.countByCallDestinationAndCallDestinationId("EXTENSION", String.valueOf(id));
	}

	@Override
	public VirtualNumbers findByUserIdOld(long userId) {
		return null;
//		return virtualNumbersRepository.findTopByCallDestinationAndCallDestinationIdOrderByIdAsc("EXTENSION",
//				String.valueOf(userId));
	}

	@Override
	public List<VirtualNumbers> findByUserId(long userId) {
		return virtualNumbersRepository.findByUserId(userId);
	}

	@Override
	public VirtualNumbers updateOrganizationNote(String virtualNumber, String orgNote) {
		VirtualNumbers virtualNumbers = virtualNumbersRepository.findByVirtualNumber(virtualNumber);
		if (virtualNumbers != null) {
			return virtualNumbersRepository.save(virtualNumbers);
		}
		return null;
	}

	@Override
	public List<VirtualNumbers> getByTypeAndOrderByDate(String type) {
		return virtualNumbersRepository.findByNumberTypeOrderByCreationDateAsc(type);
	}

	@Override
	public VirtualNumbers setUserOnNumber(String number, User user) {
		VirtualNumbers vNumber = this.getByVirtualNumber(number);
		if (vNumber != null) {
			vNumber.setUser(user);
			return virtualNumbersRepository.save(vNumber);
		}
		return null;
	}

	@Override
	public List<VirtualNumbers> findAllAvailableNumbers() {
		return virtualNumbersRepository.findByIsAvailable(true);
	}

	@Override
	public List<String> checkExistsBuld(BulkUploadRequest request) {

		List<String> listno = new ArrayList<String>();
		try {
			for (String number : request.getDidNumber()) {
				VirtualNumbers vNumber = virtualNumbersRepository.findByVirtualNumber(number);
				if (vNumber != null) {
					listno.add(number);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listno;
	}

	@Override
	public List<VirtualNumbers> bulkUpload(BulkUploadRequest request) {
		List<VirtualNumbers> list = new ArrayList<>();

		try {
			Timestamp currentDT = new Timestamp(System.currentTimeMillis());

			// remove duplicate
			request.setDidNumber(new ArrayList<>(new HashSet<>(request.getDidNumber())));

			for (String number : request.getDidNumber()) {
				if (!number.isEmpty()) {
					VirtualNumbers newVirtualNumber = new VirtualNumbers();

					newVirtualNumber.setVirtualNumber(number);
					newVirtualNumber.setState("");
					newVirtualNumber.setRatecenter("");
					newVirtualNumber.setCallDestinationId("");

					newVirtualNumber.setSmsDestinationId("");

					newVirtualNumber.setCreationDate(currentDT);
					newVirtualNumber.setActive(true);

					newVirtualNumber.setNumberType("");

					newVirtualNumber.setIsAvailable(true);
					newVirtualNumber.setCountry(request.getCountry());

					list.add(newVirtualNumber);
				}
			}

			return virtualNumbersRepository.saveAll(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;// new ArrayList<>();
	}

}