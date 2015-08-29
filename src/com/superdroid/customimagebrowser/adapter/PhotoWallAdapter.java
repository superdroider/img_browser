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
	 * ����ѡ����
	 */
	protected static final int MAX_CONUNT = 9;
	private List<Thumb> images;
	private Context context;
	private boolean isMultiSelect = false;
	/**
	 * ��¼�Ƿ�ѡ��
	 */
	private SparseBooleanArray selectionMap;
	/**
	 * ��ű�ѡ�еĵ�Thumb
	 */
	private List<Thumb> selectionList;
	/**
	 * �����ж��Ƿ�ѡ��
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
				.showImageOnLoading(R.drawable.empty_photo) // ����ͼƬ�������ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.empty_photo)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.empty_photo) // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
				.cacheInMemory(true)// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.build();// �������
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
						Toast.makeText(context, "������ѡ����ͼƬ", Toast.LENGTH_SHORT)
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
	 * �ı�item״̬
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
	 * ��ñ�ѡ�е�imageλ��
	 * 
	 * @return
	 */
	public SparseBooleanArray getSparseBooleanArray() {
		return this.selectionMap;
	}

	/**
	 * ���ѡ�м�¼
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
