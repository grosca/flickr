package com.example.flickrgallery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.List;

import com.example.flickrgallery.JobRunner.Job;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements RowListAdapter.Handler {

	DataAdapter dataAdapter;
	RowListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dataAdapter = new DataAdapter(this);
		listAdapter = new RowListAdapter(this);
		listAdapter.setHandler(this);

		ListView list = (ListView) findViewById(R.id.frame_content);
		list.setEmptyView(findViewById(R.id.emptyView));
		list.setAdapter(listAdapter);

		System.loadLibrary("FlickrGallery");
		
		int a = 3, b = 7;
		Toast.makeText(this, String.format("%d + %d = %d", a, b, sum(a, b)), Toast.LENGTH_SHORT).show();
	}

	public void onClickRetry(View view) {
		refreshUI();
	}

	@Override
	protected void refreshUI() {
		run(new Job() {

			private boolean noNet = false;
			private List<Image> images;

			@Override
			public void doUIBefore() {
				show(ViewType.Loading);
				images = dataAdapter.getImageList();
			}

			@Override
			public void doWork() {
				for (Image image : images) {
					try {
						dataAdapter.fetchImage(image);
					} catch (MalformedURLException ex) {
						// TODO: handle
					} catch (IOException ex) {
						noNet = !isInternet();
						if (noNet) {
							images.clear();
							break;
						}
					}
				}
			}

			@Override
			public void doUIAfter() {
				if (images.size() > 0) {
					show(ViewType.Content);
				} else if (noNet) {
					show(ViewType.NoNet);
				}

				final View view = findViewById(R.id.main);

				ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
				if (viewTreeObserver.isAlive()) {
					viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

							listAdapter.setImages(images, view.getWidth(), view.getHeight());
						}
					});
				}
			}
		});
	}

	protected boolean isInternet() {
		try {
			return InetAddress.getByName("avg.com").isReachable(30);
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
		return false;
	}

	@Override
	public void onItemClick(Image image) {
		String more = String.format("// TODO: %s", image.title);
		Toast.makeText(this, more, Toast.LENGTH_SHORT).show();
	}

	public native int sum(int a, int b);
}
