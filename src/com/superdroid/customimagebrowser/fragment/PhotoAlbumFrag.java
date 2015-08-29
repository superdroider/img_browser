package com.superdroid.customimagebrowser.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.superdroid.customimagebrowser.R;
import com.superdroid.customimagebrowser.adapter.PhotoAlbumAdapter;
import com.superdroid.customimagebrowser.bean.Bucket;
import com.superdroid.customimagebrowser.utils.ImageSearch;

public class PhotoAlbumFrag extends Fragment implements OnItemClickListener,
		Runnable {
	private ListView select_img_listView;
	private ProgressBar loading;

	private PhotoAlbumAdapter adapter;
	private List<Bucket> buckets;
	private OnLvItemClickListener mListener;
	private int bucket_count;
	private String firstImagePath;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adapter = new PhotoAlbumAdapter(getActivity(), buckets);
			select_img_listView.setAdapter(adapter);
			loading.setVisibility(View.GONE);
			select_img_listView.setVisibility(View.VISIBLE);
		};
	};

	public interface OnLvItemClickListener {
		public void onLvItemClicked(Bucket bucket, boolean isLatest);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnLvItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getSimpleName()
					+ " can not be cast OnLvItemClickListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View
				.inflate(getActivity(), R.layout.frag_photo_album, null);
		initView(view);
		registerListener();
		Bundle bundle = getArguments();
		if (bundle != null) {
			bucket_count = bundle.getInt("bucket_count");
			firstImagePath = bundle.getString("firstImagePath");
		}
		new Thread(this).start();
		return view;
	}

	private void initView(View view) {
		select_img_listView = (ListView) view
				.findViewById(R.id.select_img_listView);
		loading = (ProgressBar) view.findViewById(R.id.loading);
	}

	private void registerListener() {
		select_img_listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mListener != null) {
			if (position == 0) {
				mListener.onLvItemClicked(buckets.get(position), true);
			} else {
				mListener.onLvItemClicked(buckets.get(position), false);

			}
		}
	}

	@Override
	public void run() {
		if (getActivity()==null) {
			return;
		}
		buckets = new ArrayList<Bucket>();
		buckets.add(new Bucket(0, getResources().getString(
				R.string.latest_image), bucket_count, firstImagePath, null));
		buckets.addAll(ImageSearch.getBuckets(getActivity()));
		handler.sendEmptyMessage(0);
	}
}
