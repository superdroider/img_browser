package com.superdroid.customimagebrowser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superdroid.customimagebrowser.R;
import com.superdroid.customimagebrowser.adapter.PhotoWallAdapter;
import com.superdroid.customimagebrowser.bean.Bucket;
import com.superdroid.customimagebrowser.bean.Thumb;
import com.superdroid.customimagebrowser.utils.ImageSearch;

public class PhotoWallFrag extends Fragment implements OnItemClickListener,
		Runnable, OnScrollListener {

	private static final int LOAD_SUCCESS = 0;
	private static final int LOAD_FAILURE = 1;
	private TextView tv_empty;
	private GridView gv_photo;
	private ProgressBar loading;

	private PhotoWallAdapter adapter;
	private List<Thumb> images;
	private List<Thumb> tempImages;
	private boolean isMultiSelect;
	private int maxCount = 100;

	private OnGvItemClickListener mlListener;
	private Bucket bucket;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUCCESS:
				tv_empty.setVisibility(View.GONE);
				gv_photo.setVisibility(View.VISIBLE);
				images.addAll(tempImages);
				if (adapter == null) {
					adapter = new PhotoWallAdapter(getActivity(), images,
							isMultiSelect);
					gv_photo.setAdapter(adapter);
					loading.setVisibility(View.GONE);
				} else {
					adapter.refreshData(images);
				}

				if (image_ids != null) {
					if (image_ids.size() > images.size()) {
						page++;
						new Thread(PhotoWallFrag.this).start();
					}
				}

				break;

			case LOAD_FAILURE:
				tv_empty.setVisibility(View.VISIBLE);
				gv_photo.setVisibility(View.GONE);
				loading.setVisibility(View.GONE);
				break;
			}
		};
	};
	private ArrayList<Long> image_ids;
	private int page = 1;

	public interface OnGvItemClickListener {
		/**
		 * 被点击的item
		 * 
		 * @param image
		 *            被点击的图片
		 */
		public void onGvItemClicked(Thumb thumb);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mlListener = (OnGvItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getSimpleName()
					+ "can not be cast to OnGvItemClickListener");
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_photo_wall, null);
		initView(view);
		registerListener();
		Bundle bundle = getArguments();
		if (bundle != null) {
			isMultiSelect = bundle.getBoolean("isMultiSelect");
			maxCount = bundle.getInt("maxCount", 0);
			bucket = (Bucket) bundle.getSerializable("bucket");
			if (maxCount == 0) {
				image_ids = bucket.getImage_ids();
			}
		}
		images = new ArrayList<Thumb>();
		new Thread(this).start();
		return view;
	}

	private void registerListener() {
		gv_photo.setOnItemClickListener(this);
		gv_photo.setOnScrollListener(this);
	}

	private void initView(View view) {
		tv_empty = (TextView) view.findViewById(R.id.tv_empty);
		gv_photo = (GridView) view.findViewById(R.id.gv_photo);
		loading = (ProgressBar) view.findViewById(R.id.loading);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mlListener.onGvItemClicked(images.get(position));
	}

	@Override
	public void run() {
		System.out.println("page = " + page);
		Log.i("tag", "getActivity()="+getActivity());
		if (getActivity()==null) {
			return;
		}
		if (maxCount > 0) {
			tempImages = ImageSearch.getImages(getActivity(), maxCount);
		} else {
			tempImages = ImageSearch.getThumbPath(getActivity(), image_ids,
					page);
		}
		if (tempImages == null || tempImages.size() <= 0) {
			handler.sendEmptyMessage(LOAD_FAILURE);
		} else {
			handler.sendEmptyMessage(LOAD_SUCCESS);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
}
