package com.superdroid.customimagebrowser.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Bucket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 目录id
	 */
	private long bucket_id;
	/**
	 * 存放图片id的集合
	 */
	private ArrayList<Long> image_ids;
	/**
	 * 目录名
	 */
	private String bucket_name;
	/**
	 * 目录下图片个数
	 */
	private int bucket_count;
	/**
	 * 此目录下第一张图片的地址
	 */
	private String firstImagePath;

	public Bucket(long bucket_id, String bucket_name, int bucket_count,
			String firstImagePath, ArrayList<Long> image_ids) {
		super();
		this.bucket_id = bucket_id;
		this.bucket_name = bucket_name;
		this.bucket_count = bucket_count;
		this.firstImagePath = firstImagePath;
		this.image_ids = image_ids;
	}

	public long getBucket_id() {
		return bucket_id;
	}

	public void setBucket_id(int bucket_id) {
		this.bucket_id = bucket_id;
	}

	public String getBucket_name() {
		return bucket_name;
	}

	public void setBucket_name(String bucket_name) {
		this.bucket_name = bucket_name;
	}

	public int getBucket_count() {
		return bucket_count;
	}

	public void setBucket_count(int bucket_count) {
		this.bucket_count = bucket_count;
	}

	public String getFirstImagePath() {
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath) {
		this.firstImagePath = firstImagePath;
	}

	public ArrayList<Long> getImage_ids() {
		return image_ids;
	}

	public void setImage_ids(ArrayList<Long> image_ids) {
		this.image_ids = image_ids;
	}

	@Override
	public String toString() {
		return "Bucket [bucket_id=" + bucket_id + ", image_ids=" + image_ids
				+ ", bucket_name=" + bucket_name + ", bucket_count="
				+ bucket_count + ", firstImagePath=" + firstImagePath + "]";
	}

}
