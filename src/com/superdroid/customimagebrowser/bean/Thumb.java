package com.superdroid.customimagebrowser.bean;

public class Thumb {
	private long image_id;
	private String thumb_path;

	public Thumb(long image_id, String thumb_path) {
		super();
		this.image_id = image_id;
		this.thumb_path = thumb_path;
	}

	public long getImage_id() {
		return image_id;
	}

	public void setImage_id(long image_id) {
		this.image_id = image_id;
	}

	public String getThumb_path() {
		return thumb_path;
	}

	public void setThumb_path(String thumb_path) {
		this.thumb_path = thumb_path;
	}

	@Override
	public String toString() {
		return "Thumb [image_id=" + image_id + ", thumb_path=" + thumb_path
				+ "]";
	}

}
