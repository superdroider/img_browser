package com.superdroid.customimagebrowser.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Bucket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Ŀ¼id
	 */
	private long bucket_id;
	/**
	 * ���ͼƬid�ļ���
	 */
	private ArrayList<Long> image_ids;
	/**
	 * Ŀ¼��
	 */
	private String bucket_name;
	/**
	 * Ŀ¼��ͼƬ����
	 */
	private int bucket_count;
	/**
	 * ��Ŀ¼�µ�һ��ͼƬ�ĵ�ַ
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
