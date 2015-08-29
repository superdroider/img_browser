package com.superdroid.customimagebrowser.bean;

public class Image {
	/**
	 * Í¼Æ¬id
	 */
	private long image_id;
	/**
	 * Í¼Æ¬Â·¾¶
	 */
	private String _data;
	/**
	 * Í¼Æ¬ËõÂÔÍ¼Â·¾¶
	 */
	private String thumb_data;

	public Image(long image_id, String _data, String thumb_data) {
		super();
		this.image_id = image_id;
		this._data = _data;
		this.thumb_data = thumb_data;
	}

	public Image() {
		super();
	}

	public long getImage_id() {
		return image_id;
	}

	public void setImage_id(long image_id) {
		this.image_id = image_id;
	}

	public String get_data() {
		return _data;
	}

	public void set_data(String _data) {
		this._data = _data;
	}

	public String getThumb_data() {
		return thumb_data;
	}

	public void setThumb_data(String thumb_data) {
		this.thumb_data = thumb_data;
	}

	@Override
	public String toString() {
		return "Image [image_id=" + image_id + ", _data=" + _data
				+ ", thumb_data=" + thumb_data + "]";
	}
}
