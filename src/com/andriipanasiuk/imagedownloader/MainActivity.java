package com.andriipanasiuk.imagedownloader;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.andriipanasiuk.imagedownloader.model.PreviewAdapter;
import com.andriipanasiuk.imagedownloader.service.DownloadService;
import com.andriipanasiuk.imagedownloader.service.DownloadService.DownloadBinder;

public class MainActivity extends ServiceActivity implements OnClickListener, ServiceConnection {

	public static final String LOG_TAG = "ImageDownloader";
	private DownloadService downloadService;
	private ProgressReceiver receiver;
	private ListView imageListView;
	private Button downloadButton;
	private PreviewAdapter adapter;

	private LruCache<String, Bitmap> memoryCache;

	@Override
	public void onServiceDisconnected(ComponentName name) {
		super.onServiceDisconnected(name);
		disableUI();
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		Log.d(LOG_TAG, "onServiceConnected");
		downloadService = ((DownloadBinder) service).getService();
		enableUI();
		adapter.updateData(downloadService.getDownloads());
		imageListView.setAdapter(adapter);
	}

	private class ProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(LOG_TAG, "onReceive");
			int position = intent.getIntExtra(DownloadService.DOWNLOAD_ID_KEY, -1);
			if (intent.getAction().equals(DownloadService.ACTION_DOWNLOAD_PROGRESS)) {
				adapter.updateItem(imageListView, position);
			} else if (intent.getAction().equals(DownloadService.ACTION_DOWNLOAD_COMPLETE)) {
				adapter.updateItem(imageListView, position);
				Toast.makeText(MainActivity.this, R.string.download_complete, Toast.LENGTH_SHORT).show();
			} else if (intent.getAction().equals(DownloadService.ACTION_DOWNLOAD_ERROR)) {
				adapter.updateItem(imageListView, position);
				Toast.makeText(MainActivity.this, R.string.error_while_downloading, Toast.LENGTH_SHORT).show();
			} else if (intent.getAction().equals(DownloadService.ACTION_DOWNLOAD_CANCELLED)) {
				adapter.notifyDataSetChanged();
			}
		}
	}

	protected Class<? extends Service> getServiceClass() {
		return DownloadService.class;
	}

	protected void registerReceiver() {
		receiver = new ProgressReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadService.ACTION_DOWNLOAD_PROGRESS);
		filter.addAction(DownloadService.ACTION_DOWNLOAD_COMPLETE);
		filter.addAction(DownloadService.ACTION_DOWNLOAD_ERROR);
		filter.addAction(DownloadService.ACTION_DOWNLOAD_CANCELLED);
		registerReceiver(receiver, filter);
	}

	protected void unregisterReceiver() {
		unregisterReceiver(receiver);
	}

	@Override
	protected void stopService() {
		downloadService.stopNow();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RetainFragment retainFragment = RetainFragment.findOrCreateRetainFragment(getFragmentManager());
		memoryCache = retainFragment.mRetainedCache;
		if (memoryCache == null) {
			memoryCache = new LruCache<String, Bitmap>(20);
			retainFragment.mRetainedCache = memoryCache;
		}

		setContentView(R.layout.activity_main);
		downloadButton = (Button) findViewById(R.id.download_button);
		downloadButton.setOnClickListener(this);
		imageListView = (ListView) findViewById(R.id.image_list);
		adapter = new PreviewAdapter(this, memoryCache);
	}

	private void disableUI() {
		downloadButton.setEnabled(false);
	}

	private void enableUI() {
		downloadButton.setEnabled(true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.download_button) {
			if (isServiceBound()) {
				String url = ((EditText) findViewById(R.id.download_url)).getText().toString();
				boolean result = downloadService.downloadImage(url);
				if (result) {
					adapter.updateData(downloadService.getDownloads());
				} else {
					Toast.makeText(this, R.string.service_is_stopped, Toast.LENGTH_SHORT).show();
				}
			} else if (isServiceStopped()) {
				Toast.makeText(this, R.string.service_is_stopped, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.service_is_starting, Toast.LENGTH_SHORT).show();
			}
		}
	}

}
