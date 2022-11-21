package com.cooee.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "welcome_carousel")
public class WelcomeCarousel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long caroselId;
	private String imgUrl;
	private String title;
	@Lob
	private String descriptions;
	private Date creationDate;
	private Date lastUpdationDate;
	private Boolean isDeleted;

	private String title_font_size;
	private String title_font_color;
	private String title_font_style;
	private String descriptions_font_size;
	private String descriptions_font_color;
	private String descriptions_font_style;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdationDate() {
		return lastUpdationDate;
	}

	public void setLastUpdationDate(Date lastUpdationDate) {
		this.lastUpdationDate = lastUpdationDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getCaroselId() {
		return caroselId;
	}

	public void setCaroselId(Long caroselId) {
		this.caroselId = caroselId;
	}

	public String getTitle_font_size() {
		return title_font_size;
	}

	public void setTitle_font_size(String title_font_size) {
		this.title_font_size = title_font_size;
	}

	public String getTitle_font_color() {
		return title_font_color;
	}

	public void setTitle_font_color(String title_font_color) {
		this.title_font_color = title_font_color;
	}

	public String getTitle_font_style() {
		return title_font_style;
	}

	public void setTitle_font_style(String title_font_style) {
		this.title_font_style = title_font_style;
	}

	public String getDescriptions_font_size() {
		return descriptions_font_size;
	}

	public void setDescriptions_font_size(String descriptions_font_size) {
		this.descriptions_font_size = descriptions_font_size;
	}

	public String getDescriptions_font_color() {
		return descriptions_font_color;
	}

	public void setDescriptions_font_color(String descriptions_font_color) {
		this.descriptions_font_color = descriptions_font_color;
	}

	public String getDescriptions_font_style() {
		return descriptions_font_style;
	}

	public void setDescriptions_font_style(String descriptions_font_style) {
		this.descriptions_font_style = descriptions_font_style;
	}

}
