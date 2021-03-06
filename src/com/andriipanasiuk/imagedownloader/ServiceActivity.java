package com.andriipanasiuk.imagedownloader;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Base activity that handles posibility of service starting/stopping and all
 * the work concerning it. Also shows menu with this option.
 * 
 * @author andriipanasiuk
 * 
 */
public abstract class ServiceActivity extends ActionBarActivity implements ServiceConnection {

	private boolean bound = false;
	private boolean serviceStopped = false;

	protected boolean isServiceBound() {
		return bound;
	}

	protected boolean isServiceStopped() {
		return serviceStopped;
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		bound = false;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		bound = true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			serviceStopped = savedInstanceState.getBoolean("serviceStoppedKey");
		}
		if (!serviceStopped) {
			startService();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle state) {
		state.putBoolean("serviceStoppedKey", serviceStopped);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!serviceStopped) {
			bindServiceInternal();
		}
		registerReceiver();
	}

	@Override
	public void onStop() {
		if (!serviceStopped) {
			unbindService();
		}
		unregisterReceiver();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (serviceStopped) {
			menu.add(R.string.start_service);
			menu.getItem(0).setChecked(false);
		} else {
			menu.add(R.string.stop_service);
			menu.getItem(0).setChecked(true);
		}
		return true;
	}

	protected void bindServiceInternal() {
		Intent intent = new Intent(this, getServiceClass());
		bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	protected void unbindService() {
		if (bound) {
			unbindService(this);
			bound = false;
		}
	}

	protected abstract void registerReceiver();

	protected abstract void unregisterReceiver();

	protected abstract Class<? extends Service> getServiceClass();

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		int order = item.getOrder();
		if (order == 0) {
			if (item.isChecked()) {
				stopServiceInternal(true);
				Toast.makeText(this, R.string.service_was_stopped, Toast.LENGTH_SHORT).show();
				item.setChecked(false);
				item.setTitle(R.string.start_service);
			} else {
				startService();
				bindServiceInternal();
				item.setChecked(true);
				item.setTitle(R.string.stop_service);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected abstract void stopService(boolean immediately);

	private void stopServiceInternal(boolean immediately) {
		stopService(immediately);
		unbindService();
		serviceStopped = true;
	}

	private void startService() {
		Intent intent = new Intent(this, getServiceClass());
		startService(intent);
		serviceStopped = false;
	}

	private boolean waitForBack = false;
	private Handler backButtonHandler = new Handler();
	private Runnable closeRunnable = new Runnable() {

		@Override
		public void run() {
			waitForBack = false;
			ServiceActivity.super.onBackPressed();
		}
	};

	@Override
	public void onBackPressed() {
		if (waitForBack) {
			backButtonHandler.removeCallbacks(closeRunnable);
			stopServiceInternal(false);
			Toast.makeText(this, R.string.service_will_be_stopped, Toast.LENGTH_SHORT).show();
			invalidateOptionsMenu();
			waitForBack = false;
			return;
		}
		Toast.makeText(this, R.string.once_more_back, Toast.LENGTH_SHORT).show();
		waitForBack = true;
		backButtonHandler.postDelayed(closeRunnable, 500);
	}
}