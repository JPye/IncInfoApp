package sad.group3.incinfoapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.app.Activity;
import android.content.Intent;

public class LoadActivity extends Activity {

	private static final int LOAD_LENGHT = 3000; // Delay 3 seconds

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.load_layout);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(LoadActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}, LOAD_LENGHT);
	}
}
