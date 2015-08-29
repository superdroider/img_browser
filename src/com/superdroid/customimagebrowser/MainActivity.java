package com.superdroid.customimagebrowser;

import java.io.File;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.superdroid.customimagebrowser.adapter.PhotoWallAdapter.OnSquareRelativeLayoutClickListener;
import com.superdroid.customimagebrowser.bean.Bucket;
import com.superdroid.customimagebrowser.bean.Thumb;
import com.superdroid.customimagebrowser.fragment.PhotoAlbumFrag;
import com.superdroid.customimagebrowser.fragment.PhotoAlbumFrag.OnLvItemClickListener;
import com.superdroid.customimagebrowser.fragment.PhotoWallFrag;
import com.superdroid.customimagebrowser.fragment.PhotoWallFrag.OnGvItemClickListener;
import com.superdroid.customimagebrowser.utils.ImageSearch;

public class MainActivity extends FragmentActivity implements
		OnGvItemClickListener, OnClickListener,
		OnSquareRelativeLayoutClickListener, OnLvItemClickListener {

	private TextView topbar_title_tv;
	private Button topbar_left_btn;
	private Button topbar_right_btn;

	private boolean isMultiSelect = true;
	private int maxCount = 100;
	private PhotoAlbumFrag photoAlbumFrag;
	private PhotoWallFrag photoWallFrag;

	private List<Thumb> latestThumbs;
	private List<Thumb> selectionList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initView();
		registerListener();
		latestThumbs = ImageSearch.getImages(this, maxCount);
		openPhotoWallFrag(isMultiSelect, maxCount, null);
	}

	private void initView() {
		topbar_title_tv = (TextView) findViewById(R.id.topbar_title_tv);
		topbar_left_btn = (Button) findViewById(R.id.topbar_left_btn);
		topbar_right_btn = (Button) findViewById(R.id.topbar_right_btn);

		topbar_title_tv.setVisibility(View.VISIBLE);
		topbar_title_tv.setText(R.string.latest_image);

		topbar_left_btn.setVisibility(View.VISIBLE);
		topbar_left_btn.setText(R.string.photo_album);

		topbar_right_btn.setVisibility(View.VISIBLE);
		topbar_right_btn.setText(R.string.main_cancel);
	}

	private void registerListener() {
		topbar_left_btn.setOnClickListener(this);
		topbar_right_btn.setOnClickListener(this);
	}

	/**
	 * 打开PhotoWallFrag
	 * 
	 * @param isMultiSelect
	 *            是否支持多选
	 * @param maxCount
	 *            加载图片最大数量，只有在加载最近图片时可用，其他传0
	 * @param bucket_name
	 *            图片文件目录名 ，加载最近图片时传null
	 */
	private void openPhotoWallFrag(boolean isMultiSelect, int maxCount,
			Bucket bucket) {
		photoWallFrag = new PhotoWallFrag();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putBoolean("isMultiSelect", isMultiSelect);
		bundle.putInt("maxCount", maxCount);
		bundle.putSerializable("bucket", bucket);
		photoWallFrag.setArguments(bundle);
		transaction.replace(R.id.fl_content, photoWallFrag);
		transaction.commit();
	}

	private void openPhotoAlbumFrag(int latestImageCount, String firstImgPath) {
		photoAlbumFrag = new PhotoAlbumFrag();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt("bucket_count", latestImageCount);
		bundle.putString("firstImagePath", firstImgPath);
		photoAlbumFrag.setArguments(bundle);
		transaction.replace(R.id.fl_content, photoAlbumFrag);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_left_btn:
			if (latestThumbs == null && latestThumbs.size() == 0) {
				return;
			}
			openPhotoAlbumFrag(latestThumbs.size(), latestThumbs.get(0)
					.getThumb_path());
			topbar_left_btn.setVisibility(View.GONE);
			topbar_title_tv.setText(R.string.select_album);
			if (selectionList!=null) {
				selectionList.clear();
				topbar_right_btn.setText(R.string.main_cancel);
			}
			break;
		case R.id.topbar_right_btn:
			if (getResources().getString(R.string.main_cancel).equals(
					topbar_right_btn.getText().toString())) {
				finish();
			} else {
				for (int i = 0; i < selectionList.size(); i++) {
					System.out.println("position=" + i + " value="
							+ selectionList.get(i).getThumb_path());
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onSquareRelativeLayoutClicked(List<Thumb> selectionList) {
		int size = 0;
		if (selectionList != null && selectionList.size() > 0) {
			size = selectionList.size();
			System.out.println("size=" + size);
			for (int i = 0; i < size; i++) {
				System.out.println("isSelect ="
						+ selectionList.get(i).getImage_id());
			}
			topbar_right_btn.setText(R.string.main_confirm);

		} else {
			topbar_right_btn.setText(R.string.main_cancel);
		}

		if (isMultiSelect) {
			this.selectionList = selectionList;
			for (Thumb thumb : selectionList) {
				String imagePath = ImageSearch.getImagePath(this,
						thumb.getImage_id());
				System.out.println("imagePath = " + imagePath);
			}
		}
	}

	@Override
	public void onGvItemClicked(Thumb thumb) {
		System.out.println("thumb.getImage_id()" + thumb.getImage_id());
		String imagePath = ImageSearch.getImagePath(this, thumb.getImage_id());
		System.out.println("imagePath=" + imagePath);
		File file = new File(imagePath);
		System.out.println(file.length());
	}

	@Override
	public void onLvItemClicked(Bucket bucket, boolean isLatest) {
		int count = 0;
		if (isLatest) {
			count = maxCount;
		} else {
			count = 0;
		}
		System.out.println(bucket.toString());
		openPhotoWallFrag(isMultiSelect, count, bucket);
		topbar_title_tv.setText(bucket.getBucket_name());
		topbar_left_btn.setVisibility(View.VISIBLE);
		topbar_left_btn.setText(R.string.photo_album);
	}
}
