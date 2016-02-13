package unithon.here;

import android.app.Application;

public class Globals extends Application {
	private double latitude;
	private double longitude;

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLatitude(double lati) {
		this.latitude = lati;
	}

	public void setLongitude(double longi) {
		this.longitude = longi;
	}
}
