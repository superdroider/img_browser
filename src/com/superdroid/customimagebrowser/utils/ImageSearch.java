package com.superdroid.customimagebrowser.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.superdroid.customimagebrowser.bean.Bucket;
import com.superdroid.customimagebrowser.bean.Image;
import com.superdroid.customimagebrowser.bean.Thumb;

public class ImageSearch extends AsyncTask<Context, Integer, List<String>> {
	/**
	 * ͨ��ͼƬid���Ҷ�Ӧ����ͼ·��
	 * 
	 * @param context
	 *            ������
	 * @param imageIds
	 *            ͼƬid
	 * @return �������ͼ���ڷ�������ͼ·�������򷵻�null
	 */
	public static ArrayList<Thumb> getThumbPath(Context context,
			ArrayList<Long> imageIds, int page) {

		/**
		 * ����ͼ·��
		 */
		ArrayList<Thumb> thumbs = new ArrayList<Thumb>();

		Thumb thumb = null;
		/**
		 * ����ͼ���ݿ��URI
		 */
		Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
		/**
		 * ָ����Ҫ��ѯ���ֶ�
		 */
		String[] projection = new String[] {
				MediaStore.Images.Thumbnails.IMAGE_ID,
				MediaStore.Images.Thumbnails.DATA };
		/**
		 * ��ѯ����
		 */
		String selection = MediaStore.Images.Thumbnails.IMAGE_ID + " =?";
		/**
		 * ��ѯ�������ʺŶ�Ӧ��ֵ
		 */
		String[] selectionArgs = null;
		/**
		 * ����ʽ
		 */
		String sortOrder = null;
		long imageId;
		int size = imageIds.size();
		// ÿҳ����30������
		int count = 30;
		int startPosition = size - 1 - count * (page - 1);
		int endPosition = size - count * page;
		if (endPosition < 0) {
			endPosition = 0;
		}
		if (startPosition < 0) {
			startPosition = 0;
		}
		int index;
		for (index = startPosition; index >= endPosition; index--) {
			imageId = imageIds.get(index);
			selectionArgs = new String[] { String.valueOf(imageId) };
			Log.i("tag", "context = "+context);
			Cursor cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, sortOrder);
			if (cursor != null) {
				boolean moveToFirst = cursor.moveToFirst();
				if (moveToFirst) {
					String thumbPath = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
					if (thumbPath == null) {
						thumbPath = getImagePath(context, imageId);
						System.out.println("thumbPath null =" + thumbPath);
					}
					thumb = new Thumb(imageId, thumbPath);
				} else {
					thumb = new Thumb(imageId, getImagePath(context, imageId));
				}
				thumbs.add(thumb);
				cursor.close();
			}
		}

		return thumbs;
	}

	/**
	 * ����ͼƬid��ȡͼƬ��ʵ·��
	 * 
	 * @param context
	 * @param imageId
	 * @return
	 */
	public static String getImagePath(Context context, long imageId) {
		String imagePath = null;

		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		String selection = MediaStore.Images.Media._ID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(imageId) };
		String sortOrder = null;
		Cursor cursor = context.getContentResolver().query(uri, projection,
				selection, selectionArgs, sortOrder);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				imagePath = cursor.getString(0);
			}
			cursor.close();
		}

		return imagePath;
	}

	/**
	 * ��ñ������ͼƬ
	 * 
	 * @param context
	 * @param maxCount
	 * @return
	 */
	public static List<Thumb> getImages(Context context, int maxCount) {
		List<Image> images = null;
		List<Thumb> thumbs = null;
		/**
		 * ͼƬ���ݿ��URI
		 */
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		/**
		 * ָ����Ҫ��ѯ���ֶ�
		 */
		String[] projection = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		/**
		 * ��ѯ����
		 */
		String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
				+ MediaStore.Images.Media.MIME_TYPE + "=? or "
				+ MediaStore.Images.Media.MIME_TYPE + "=?";
		/**
		 * ��ѯ�������ʺŶ�Ӧ��ֵ
		 */
		String[] selectionArgs = new String[] { "image/jpg", "image/jpeg",
				"image/png" };
		/**
		 * ����ʽ
		 */
		String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
		Cursor cursor = context.getContentResolver().query(uri, projection,
				selection, selectionArgs, sortOrder);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				images = new ArrayList<Image>();
				while (cursor.moveToNext()) {
					long image_id = cursor.getLong(0);
					String _data = cursor.getString(1);
					if (images.size() < maxCount && isPathEnable(_data)) {
						images.add(new Image(image_id, _data, null));
					} else {
						break;
					}
				}
			}
			cursor.close();
			thumbs = new ArrayList<Thumb>();
			Thumb thumb = null;
			for (Image image : images) {

				Cursor query = context.getContentResolver().query(
						MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.Thumbnails.DATA },
						MediaStore.Images.Thumbnails.IMAGE_ID + " ="
								+ image.getImage_id(), null, null);
				if (query != null) {
					if (query.moveToFirst()) {
						String thumb_path = query.getString(0);
						if (thumb_path == null) {
							thumb_path = image.get_data();
						}
						thumb = new Thumb(image.getImage_id(), thumb_path);
					} else {
						thumb = new Thumb(image.getImage_id(), image.get_data());
					}
					query.close();
					thumbs.add(thumb);
				}
			}
			return thumbs;
		} else {
			return null;
		}
	}

	/**
	 * ��ȡ����ͼƬ��·����
	 * 
	 * @param context
	 *            ������
	 * @return ���·�������ڷ��ذ���ͼƬ·�����ļ��Ϸ��򷵻�null
	 */
	public static List<Bucket> getBuckets(Context context) {
		List<Bucket> buckets = null;
		/**
		 * ͼƬ���ݿ��URI
		 */
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		/**
		 * ָ����Ҫ��ѯ���ֶ�
		 */
		String[] projection = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
		/**
		 * ��ѯ����
		 */
		String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
				+ MediaStore.Images.Media.MIME_TYPE + "=? or "
				+ MediaStore.Images.Media.MIME_TYPE + "=?";
		/**
		 * ��ѯ�������ʺŶ�Ӧ��ֵ
		 */
		String[] selectionArgs = new String[] { "image/jpg", "image/jpeg",
				"image/png" };
		/**
		 * ����ʽ
		 */
		String sortOrder = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
		Cursor cursor = context.getContentResolver().query(uri, projection,
				selection, selectionArgs, sortOrder);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				Bucket bucket = null;
				long bucketId = 0;
				long image_id;
				String firstImagePath = null;
				String bucket_display_name = null;
				ArrayList<Long> image_ids = null;
				buckets = new ArrayList<Bucket>();
				while (cursor.moveToNext()) {
					int count = 1;
					bucket_display_name = cursor.getString(2);
					firstImagePath = cursor.getString(1);
					image_id = cursor.getLong(0);
					if (!isPathEnable(firstImagePath)) {
						continue;
					}
					if (bucket == null
							|| !bucket.getBucket_name().equals(
									bucket_display_name)) {
						image_ids = new ArrayList<Long>();
						image_ids.add(image_id);
						bucket = new Bucket(bucketId, bucket_display_name,
								count, firstImagePath, image_ids);
						buckets.add(bucket);
					} else if (bucket.getBucket_name().equals(
							bucket_display_name)) {
						bucket.setBucket_name(bucket_display_name);
						int temp = bucket.getBucket_count();
						temp++;
						bucket.setBucket_count(temp);
						bucket.getImage_ids().add(image_id);
						bucket.setFirstImagePath(firstImagePath);
					}
				}
			}
			cursor.close();
			return buckets;
		} else {
			return null;
		}
	}

	/**
	 * �ж�·���Ƿ����
	 * 
	 * @param firstImagePath
	 *            ͼƬ·��
	 * @return
	 */
	private static boolean isPathEnable(String firstImagePath) {
		try {
			File file = new File(firstImagePath);
			if (file.length() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected List<String> doInBackground(Context... params) {
		return null;
	}
}