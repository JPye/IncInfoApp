package sad.group3.incinfoapp;

import sad.group3.service.SettingService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class SettingActivity extends Activity {
	private CheckBox useDefaultSetting;
	private EditText ipEditText1;
	private EditText ipEditText2;
	private EditText ipEditText3;
	private EditText ipEditText4;
	private EditText portEditText;
	private Button applyBtn;
	private Button resetBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);

		initialize();
		
		if(SettingService.getCustomizedURL()!=null&&!"".equals(SettingService.getCustomizedURL())){
			String ip=SettingService.getCustomizedURL().replace(".", ":");
			String[] ipAll=ip.split(":");
			ipEditText1.setText(ipAll[0]);
			ipEditText2.setText(ipAll[1]);
			ipEditText3.setText(ipAll[2]);
			ipEditText4.setText(ipAll[3]);
			portEditText.setText(SettingService.getPort());
		}

		if (SettingService.getUrl().equals(SettingService.getDefaultURL())) {
			setChecked();
		} else {
			setUnChecked();
		}
	}

	private void initialize() {
		useDefaultSetting = (CheckBox) this.findViewById(R.id.checkBox);
		useDefaultSetting.setChecked(true);

		useDefaultSetting
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							setChecked();
							SettingService.setUrl(SettingService
									.getDefaultURL());
							Toast.makeText(getApplicationContext(),
									"Use Default URL Successfully",
									Toast.LENGTH_LONG).show();
						} else {
							setUnChecked();
						}
					}
				});
		ipEditText1 = (EditText) this.findViewById(R.id.ip_input_one);
		ipEditText2 = (EditText) this.findViewById(R.id.ip_input_two);
		ipEditText3 = (EditText) this.findViewById(R.id.ip_input_three);
		ipEditText4 = (EditText) this.findViewById(R.id.ip_input_four);
		
		portEditText = (EditText) this.findViewById(R.id.port_input);
		applyBtn = (Button) this.findViewById(R.id.setting_apply_btn);
		resetBtn = (Button) this.findViewById(R.id.setting_reset_btn);
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			finish();
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	private void setChecked() {
		useDefaultSetting.setChecked(true);
		ipEditText1.clearFocus();
		ipEditText2.clearFocus();
		ipEditText3.clearFocus();
		ipEditText4.clearFocus();
		portEditText.clearFocus();
		ipEditText1.setEnabled(false);
		ipEditText2.setEnabled(false);
		ipEditText3.setEnabled(false);
		ipEditText4.setEnabled(false);
		portEditText.setEnabled(false);
		applyBtn.setEnabled(false);
		resetBtn.setEnabled(false);
	}

	private void setUnChecked() {
		useDefaultSetting.setChecked(false);
		ipEditText1.setEnabled(true);
		ipEditText2.setEnabled(true);
		ipEditText3.setEnabled(true);
		ipEditText4.setEnabled(true);
		portEditText.setEnabled(true);
		applyBtn.setEnabled(true);
		resetBtn.setEnabled(true);
	}

	public void apply(View view) {
		if ("".equals(ipEditText1.getText().toString())
				||"".equals(ipEditText2.getText().toString()) ||"".equals(ipEditText3.getText().toString())||"".equals(ipEditText4.getText().toString())) {
			Toast.makeText(this.getApplicationContext(), "IP cannot be empty!",
					Toast.LENGTH_LONG).show();
			return;
		}
		if ("".equals(portEditText.getText().toString())
				|| portEditText.getText().toString() == null) {
			Toast.makeText(this.getApplicationContext(),
					"Port cannot be empty!", Toast.LENGTH_LONG).show();
			return;
		}
		SettingService.setCustomizedURL(ipEditText1.getText().toString()+"."+ipEditText2.getText().toString()+"."+ipEditText3.getText().toString()+"."+ipEditText4.getText().toString());
		SettingService.setPort(portEditText.getText().toString());
		SettingService.setUrl("http://" + SettingService.getCustomizedURL()
				+ ":" + SettingService.getPort()
				+ "/IncidentInformationApplicationServer/");
		Toast.makeText(this.getApplicationContext(),
				"Apply Customized URL Successfully", Toast.LENGTH_LONG).show();
	}

	public void reset(View view) {
		ipEditText1.setText("");
		ipEditText2.setText("");
		ipEditText3.setText("");
		ipEditText4.setText("");
		portEditText.setText("");
	}

	public void backBtn(View view) {
		this.finish();
	}
}
