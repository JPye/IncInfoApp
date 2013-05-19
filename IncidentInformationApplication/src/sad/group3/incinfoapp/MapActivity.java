package sad.group3.incinfoapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MapActivity extends TimeBaseActivity {
	private GoogleMap mMap = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		
		sendMsgToHandler(3);

		Intent intent = this.getIntent();
		String address = intent.getStringExtra("address");
		String content = intent.getStringExtra("content");

		setUpMap(address, content);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void setUpMap(String address, String content) {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				// The Map is verified. It is now safe to manipulate the map.
				UiSettings uiSettings = mMap.getUiSettings();
				uiSettings.setMyLocationButtonEnabled(true);
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				mMap.setMyLocationEnabled(true);

				Geocoder geocoder = new Geocoder(this, Locale.getDefault());
				double longtitude = 0.0;
				double latitute = 0.0;
				try {
					List<Address> addresses = geocoder.getFromLocationName(
							address, 1);
					Log.i("addresssize", addresses.size() + "");
					if (addresses.size() > 0) {
						latitute = addresses.get(0).getLatitude();
						longtitude = addresses.get(0).getLongitude();
						mMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(new CameraPosition(
										new LatLng(latitute, longtitude), 15f,
										30f, 0f)));
						mMap.addMarker(new MarkerOptions()
								.position(new LatLng(latitute, longtitude))
								.title(address)
								.snippet(content)
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.warning)));
					} else {
						// 下面的语句用于调用系统自带的google map
						Toast.makeText(getApplicationContext(),
								"Google Service Unavalable\nUse Default Maps", Toast.LENGTH_LONG).show();
						Intent searchAddress = new Intent(Intent.ACTION_VIEW,
								Uri.parse("geo:0,0?q=" + address));
						startActivity(searchAddress);
					}
				} catch (IOException e) {
					e.printStackTrace();
					// 下面的语句用于调用系统自带的google map
					Toast.makeText(getApplicationContext(),
							"Google Service Unavalable\nUse Default Maps", Toast.LENGTH_LONG).show();
					Intent searchAddress = new Intent(Intent.ACTION_VIEW,
							Uri.parse("geo:0,0?q=" + address));
					startActivity(searchAddress);
				}
			}
		} else {
			Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG)
					.show();
		}
	}
}
