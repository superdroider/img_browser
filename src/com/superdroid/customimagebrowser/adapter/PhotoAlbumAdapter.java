package com.superdroid.customimagebrowser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.superdroid.customimagebrowser.R;
import com.superdroid.customimagebrowser.bean.Bucket;

/**
 * 选择相册页面,ListView的adapter
 */
public class PhotoAlbumAdapter extends BaseAdapter {
	private Context context;
	private List<Bucket> buckets;
	ImageLoaderConfiguration configuration;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	public PhotoAlbumAdapter(Context context, List<Bucket> buckets) {
		this.context = context;
		this.buckets = buckets;
		if (buckets == null) {
			buckets = new ArrayList<Bucket>();
		}
		if (context == null) {
			return;
		}
		configuration = ImageLoaderConfiguration.createDefault(context);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(configuration);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.empty_photo) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.empty_photo)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.empty_photo) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.build();// 构建完成
	}

	@Override
	public int getCount() {
		return buckets.size();
	}

	@Override
	public Object getItem(int position) {
		return buckets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.photo_album_lv_item, null);
			holder = new ViewHolder();
			holder.select_img_gridView_img = (ImageView) convertView
					.findViewById(R.id.select_img_gridView_img);
			holder.select_img_gridView_path = (TextView) convertView
					.findViewById(R.id.select_img_gridView_path);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String bucketName = buckets.get(position).getBucket_name();
		System.out.println("posiyion = " + buckets.get(position) + position);
		imageLoader.displayImage("file://"
				+ buckets.get(position).getFirstImagePath(),
				holder.select_img_gridView_img, options);
		holder.select_img_gridView_path.setText(bucketName + "("
				+ buckets.get(position).getBucket_count() + ")");
		return convertView;
	}

	private static class ViewHolder {
		ImageView select_img_gridView_img;
		TextView select_img_gridView_path;
	}

}
