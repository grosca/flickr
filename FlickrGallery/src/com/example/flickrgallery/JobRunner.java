package com.example.flickrgallery;

import android.os.AsyncTask;

/**
 * Just a wrapper on top of AsyncTask
 */
public class JobRunner {

	public static abstract class Job {
		public void doWork() {
		}

		public void doUIBefore() {
		}

		public void doUIAfter() {
		}
	}

	public JobRunner() {
	}

	public static void run(Job job) {
		new AsyncRunnerTask(job).execute();
	}

	private static class AsyncRunnerTask extends
			AsyncTask<Object, Integer, Long> {

		private Job job;

		public AsyncRunnerTask(Job job) {
			super();
			this.job = job;
		}

		@Override
		protected void onPreExecute() {
			job.doUIBefore();
		}

		@Override
		protected Long doInBackground(Object... params) {
			job.doWork();
			return 0L;
		}

		@Override
		protected void onPostExecute(Long result) {
			job.doUIAfter();
		}
	}
}
