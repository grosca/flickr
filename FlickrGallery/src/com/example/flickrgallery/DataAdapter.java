package com.example.flickrgallery;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DataAdapter {

	Context context;

	public DataAdapter(Context context) {
		this.context = context;
	}

	public List<Image> getImageList() {
		List<Image> images = new ArrayList<Image>();

		try {
			JSONArray array = loadImageList();

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);

				images.add(new Image(obj.getInt("width_n"), obj
						.getInt("height_n"), context.getFilesDir()
						.getAbsolutePath() + "/" + obj.getString("id"), obj
						.getString("url_n"), obj.getString("title")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return images;
	}

	public void fetchImage(Image image) throws MalformedURLException,
			IOException {

		if (new File(image.path).exists()) {
			// skip, was pulled
			return;
		}

		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(image.url).openStream());
			fout = new FileOutputStream(image.path);

			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (fout != null) {
				fout.close();
			}
		}
	}

	// TODO: cache this?
	public static Bitmap loadBitmap(Image image) {
		return BitmapFactory.decodeFile(image.path);
	}

	protected JSONArray loadImageList() throws JSONException {
		String json = null;
		try {
			InputStream is = context.getAssets().open("sample-flicr.json");

			int size = is.available();

			byte[] buffer = new byte[size];

			is.read(buffer);

			is.close();

			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

		return new JSONObject(json).getJSONObject("query")
				.getJSONObject("results").getJSONArray("photo");
	}
}
