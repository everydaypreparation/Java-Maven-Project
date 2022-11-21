package com.cooee.payload;

import java.util.List;

import com.cooee.model.VirtualNumbers;

public class MultipleResponseBulkDID {

	private List<VirtualNumbers> virtualNumbers;
	private String notToBeAdd;
	private Integer type;

	public List<VirtualNumbers> getVirtualNumbers() {
		return virtualNumbers;
	}

	public void setVirtualNumbers(List<VirtualNumbers> virtualNumbers) {
		this.virtualNumbers = virtualNumbers;
	}

	public String getNotToBeAdd() {
		return notToBeAdd;
	}

	public void setNotToBeAdd(String notToBeAdd) {
		this.notToBeAdd = notToBeAdd;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
