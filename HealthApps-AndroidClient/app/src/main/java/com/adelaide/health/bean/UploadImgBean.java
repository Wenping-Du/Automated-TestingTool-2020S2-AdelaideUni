package com.adelaide.health.bean;

import java.io.Serializable;


/**
 * @author Wenping(Deb) Du
 */
public class UploadImgBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//图片url
	private String url;
	
	//图片宽度
	private String imageWidth;
	
	//高度
	private String imageHeight;
	
	//类型
	private String imageType;
	
	public UploadImgBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UploadImgBean(String url, String imageWidth, String imageHeight,
			String imageType) {
		super();
		this.url = url;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.imageType = imageType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}

	public String getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(String imageHeight) {
		this.imageHeight = imageHeight;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	
}