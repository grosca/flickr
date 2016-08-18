package com.example.flickrgallery;

import com.example.flickrgallery.JobRunner.Job;

import android.app.Activity;
import android.view.View;

public abstract class BaseActivity extends Activity {

	protected void run(Job job) {
		JobRunner.run(job);
	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshUI();
	}

	protected abstract void refreshUI();

	protected enum ViewType {
		Content, Loading, NoNet
	};

	protected void show(int viewId, boolean show) {
		View view = findViewById(viewId);
		if (view != null) {
			view.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}

	protected void show(ViewType view) {
		show(R.id.frame_content, view == ViewType.Content);
		show(R.id.frame_loading, view == ViewType.Loading);
		show(R.id.frame_nonet, view == ViewType.NoNet);
	}
}
