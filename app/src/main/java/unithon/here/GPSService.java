package unithon.here;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GPSService extends Service {

	private LocationManager manager;
	String locationProvider;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("INFO", "백그라운드 실행");
		manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		getMyLocation();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub

		return null;
	}

	private void getMyLocation() {

		// provider 기지국||GPS 를 통해서 받을건지 알려주는 Stirng 변수
		// minTime 최소한 얼마만의 시간이 흐른후 위치정보를 받을건지 시간간격을 설정 설정하는 변수
		// minDistance 얼마만의 거리가 떨어지면 위치정보를 받을건지 설정하는 변수
		// manager.requestLocationUpdates(provider, minTime, minDistance,
		// listener);

		// 10초
		if (manager.isProviderEnabled(manager.NETWORK_PROVIDER) == true) {
			locationProvider = manager.NETWORK_PROVIDER;
		} else
			locationProvider = manager.GPS_PROVIDER;

		long minTime = 6000;

		// 거리는 0으로 설정
		// 그래서 시간과 거리 변수만 보면 움직이지않고 10초뒤에 다시 위치정보를 받는다
		float minDistance = 0;

		MyLocationListener listener = new MyLocationListener();

		Log.i("INFO", locationProvider);
		manager.requestLocationUpdates(locationProvider, minTime, minDistance, listener);

	}

	class MyLocationListener implements LocationListener {

		// 위치정보는 아래 메서드를 통해서 전달된다.
		@Override
		public void onLocationChanged(Location location) {
			Log.i("INFO", "함수 호출");

			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			((Globals) getApplication()).setLatitude(latitude);
			((Globals) getApplication()).setLongitude(longitude);
			Log.i("INFO", "현재 위치:" + latitude + "," + longitude);

			Toast.makeText(getApplicationContext(), "현재 위치:" + latitude + "," + longitude, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}
	}

}
