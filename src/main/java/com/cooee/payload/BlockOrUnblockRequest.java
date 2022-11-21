package com.cooee.payload;

public class BlockOrUnblockRequest {
private Long id;
private Boolean isBlocked;


public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Boolean getIsBlocked() {
	return isBlocked;
}
public void setIsBlocked(Boolean isBlocked) {
	this.isBlocked = isBlocked;
}

}
