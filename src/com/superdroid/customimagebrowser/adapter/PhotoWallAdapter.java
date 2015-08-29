package com.superdroid.customimagebrowser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.superdroid.customimagebrowser.R;
import com.superdroid.customimagebrowser.bean.Thumb;
import com.superdroid.customimagebrowser.customviews.SquareRelativeLayout;

public class PhotoWallAdapter extends BaseAdapter {
	/**
	 * 最大可选数量
	 */
	protected static final int MAX_CONUNT = 9;
	private List<Thumb> images;
	private Context context;
	private boolean isMultiSelect = false;
	/**
	 * 记录是否被选中
	 */
	private SparseBooleanArray selectionMap;
	/**
	 * 存放被选中的的Thumb
	 */
	private List<Thumb> selectionList;
	/**
	 * 用来判断是否被选中
	 */
	private boolean isSelected = false;
	private ImageLoaderConfiguration configuration;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private OnSquareRelativeLayoutClickListener mListener;

	public interface OnSquareRelativeLayoutClickListener {

		public void onSquareRelativeLayoutClicked(List<Thumb> selectionList);
	}

	public void refreshData(List<Thumb> images) {
		this.images = images;
		notifyDataSetChanged();
	}

	public PhotoWallAdapter(Context context, List<Thumb> images,
			boolean isMultiSelect) {
		Log.i("tag", "new PhotoWallAdapter");
		if (images == null) {
			images = new ArrayList<Thumb>();
		}
		this.isMultiSelect = isMultiSelect;
		this.images = images;
		this.context = context;
		if (context == null) {
			return;
		}
		try {
			mListener = (OnSquareRelativeLayoutClickListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.getClass().getSimpleName()
					+ " can not be cast to OnSquareRelativeLayoutClickListener");
		}
		selectionMap = new SparseBooleanArray();
		selectionList = new ArrayList<Thumb>();
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
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		final ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.photo_wall_item, null);
			holder = new ViewHolder();
			holder.photo_wall_item_photo = (ImageView) view
					.findViewById(R.id.photo_wall_item_photo);
			holder.iv_select_status = (ImageView) view
					.findViewById(R.id.iv_select_status);
			holder.srl_layout = (SquareRelativeLayout) view
					.findViewById(R.id.srl_layout);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		if (isMultiSelect) {

			holder.iv_select_status.setVisibility(View.VISIBLE);
			changeSelectedStatus(holder, selectionMap.get(position, false));
			holder.srl_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isSelected = selectionMap.get(position, false);
					if (selectionMap.size() >= MAX_CONUNT && !isSelected) {
						Toast.makeText(context, "最多可以选九张图片", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					System.out.println("position=" + position + " imageid="
							+ images.get(position).getImage_id());
					if (isSelected) {
						isSelected = false;
						selectionMap.delete(position);
						selectionList.remove(images.get(position));
					} else {
						isSelected = true;
						selectionMap.put(position, isSelected);
						selectionList.add(images.get(position));
					}
					changeSelectedStatus(holder, isSelected);
					mListener.onSquareRelativeLayoutClicked(selectionList);
				}

			});
		} else {
			holder.iv_select_status.setVisibility(View.GONE);
		}
		imageLoader.displayImage("file://"
				+ images.get(position).getThumb_path(),
				holder.photo_wall_item_photo, options);
		return view;
	}

	/**
	 * 改变item状态
	 * 
	 * @param holder
	 * @param isSelected
	 */
	private void changeSelectedStatus(ViewHolder holder, boolean isSelected) {
		if (isSelected) {
			holder.iv_select_status
					.setImageResource(R.drawable.checkbox_checked);
			holder.photo_wall_item_photo.setColorFilter(context.getResources()
					.getColor(R.color.image_checked_bg));
		} else {
			holder.iv_select_status
					.setImageResource(R.drawable.checkbox_normal);
			holder.photo_wall_item_photo.setColorFilter(null);
		}

	}

	/**
	 * 获得被选中的image位置
	 * 
	 * @return
	 */
	public SparseBooleanArray getSparseBooleanArray() {
		return this.selectionMap;
	}

	/**
	 * 清空选中记录
	 */
	public void clearSelection() {
		if (selectionMap != null) {
			selectionMap.clear();
		}
		if (selectionList != null) {
			selectionList.clear();
		}
	}

	static class ViewHolder {
		private ImageView photo_wall_item_photo;
		private ImageView iv_select_status;
		private SquareRelativeLayout srl_layout;
	}
}
