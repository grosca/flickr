package com.example.flickrgallery;

public class Image {
	public int width;
	public int height;
	public String url;
	public String path;
	public String title;

	public int desiredWidth;
	public int desiredHeight;

	public Image(int width, int height, String path, String url, String title) {
		this.width = width;
		this.height = height;

		this.path = path;
		this.url = url;
		this.title = title;
	}
}
