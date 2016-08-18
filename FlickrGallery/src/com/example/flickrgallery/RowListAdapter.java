package com.example.flickrgallery;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class RowListAdapter extends BaseAdapter implements View.OnClickListener {

	public interface Handler {
		public void onItemClick(Image image);
	}

	private class ViewHolder {
		List<ImageView> imageViews = new ArrayList<ImageView>();
	}

	private LayoutInflater inflater;
	private List<List<Image>> rows = new ArrayList<List<Image>>();
	private Context context;
	private Handler handler;

	public RowListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public int getCount() {
		return rows.size();
	}

	@Override
	public Object getItem(int i) {
		return rows.get(i);
	}

	@Override
	public long getItemId(int i) {
		return getItem(i).hashCode();
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_row, parent, false);

			holder = new ViewHolder();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		List<Image> row = rows.get(i);

		i = 0;
		for (; i < row.size(); i++) {

			if (holder.imageViews.size() == i) {
				ImageView imageView = new ImageView(context);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setPadding(5, 5, 5, 5);
				imageView.setClickable(true);
				imageView.setOnClickListener(this);

				holder.imageViews.add(imageView);
				((ViewGroup) convertView).addView(imageView);
			}
			ImageView imageView = holder.imageViews.get(i);
			imageView.setVisibility(View.VISIBLE);

			Image image = row.get(i);

			imageView.setTag(image);
			imageView.setImageBitmap(DataAdapter.loadBitmap(image));

			imageView.getLayoutParams().height = image.desiredHeight;
			imageView.getLayoutParams().width = image.desiredWidth;
		}

		for (; i < holder.imageViews.size(); i++) {
			holder.imageViews.get(i).setVisibility(View.GONE);
		}

		return convertView;
	}

	public void setImages(List<Image> images, int screenWidth, int screenHeight) {
		rows.clear();

		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		int target = 0;

		for (Image image : images) {
			target += image.height;
			min = Math.min(image.height, min);
			max = Math.max(image.height, max);
		}

		// our target is the median but can be any [min - max]
		// e.g. some articles suggest screenHeight / 2, etc.
		if (images.size() > 0) {
			target /= images.size();
		}

		List<Image> currRow = new ArrayList<Image>();
		int currWidth = 0;
		int currHeight = target;
		for (int i = 0; i < images.size(); i++) {
			Image image = images.get(i);

			image.desiredHeight = currHeight;
			image.desiredWidth = image.width * currHeight / image.height;

			if (image.desiredWidth + currWidth > screenWidth) {
				if (screenWidth - currWidth > image.desiredWidth + currWidth
						- screenWidth) {
					// adjust entire row to fit this and start new row
					currWidth += image.desiredWidth;
					currRow.add(image);
				} else {
					i--;
				}

				// adjust currRow to fit the screen
				currHeight = screenWidth * currHeight / currWidth;

				for (Image img : currRow) {
					img.desiredHeight = currHeight;
					img.desiredWidth = img.width * currHeight / img.height;
				}

				rows.add(currRow);

				// start new row
				currRow = new ArrayList<Image>();
				currWidth = 0;
				currHeight = target;
			} else {
				currWidth += image.desiredWidth;
				currRow.add(image);
			}
		}

		if (currRow.size() > 0) {
			rows.add(currRow);
		}

		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		Image item = (Image) v.getTag();

		if (handler != null)
			handler.onItemClick(item);
	}
}
