package sad.group3.incinfoapp;

import sad.group3.domain.Officer;
import sad.group3.service.LoginService;
import sad.group3.utils.MD5;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity {
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private EditText usernameEditText, pwdEditText;
	private ProgressDialog mypDialog;
	private String toastMsg = "Wrong Police ID/Password";

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 开始
				mypDialog.show();// 让ProgressDialog显示
				break;
			case 2:// 结束
				mypDialog.cancel();// ProgressDialog的消失
				Toast.makeText(getApplicationContext(), toastMsg,
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		uiInit();
		progressDialogShow();
	}

	// 初始化界面widget
	private void uiInit() {
		usernameEditText = (EditText) findViewById(R.id.username_edittext);
		pwdEditText = (EditText) findViewById(R.id.password_edittext);
	}

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Log In");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	// 响应reset button
	public void reset(View view) {
		usernameEditText.setText("");
		pwdEditText.setText("");
	}

	public void testLogin(View view)
	{
		Intent intent = new Intent(LoginActivity.this,
				MenuActivity.class);
		Log.d(TAG, " test log in start");
		startActivity(intent);
		Log.d(TAG, " test log in finish");
		finish();
	}
	
	// 响应login button
	public void login(View view) {
		//输入控制
		final String o_id=usernameEditText.getText()
				.toString();
		final String pwd=pwdEditText
				.getText().toString();
		
		if(o_id.equals("")){
			Toast.makeText(getApplicationContext(), "Police ID cannot be empty",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		if(pwd.equals("")){
			Toast.makeText(getApplicationContext(), "Password cannot be empty",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// 登录操作....
		Thread thread = new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 1;// 开始网络交互
				handler.sendMessage(msg);

				try {
					Officer officer = new Officer(o_id,null, null, MD5.toMD5(pwd));
					String loginResult=LoginService.login(officer);
					if ("Success".equals(loginResult)) {
						toastMsg="Login Success";
						// 登录成功，界面跳转到menu
						Intent intent = new Intent(LoginActivity.this,
								MenuActivity.class);
						startActivity(intent);
						finish();
					}else if("Error".equals(loginResult)){
						toastMsg="Web Connection Error";
					}else if("Warning".equals(loginResult)){
						toastMsg="This ID has already been loged in!";
					}
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}

				msg = new Message();
				msg.what = 2;// 结束
				handler.sendMessage(msg);
			}
		};
		thread.start();
	}

	// add Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Setting").setIcon(R.drawable.setting);
		return super.onCreateOptionsMenu(menu);
	}

	// 响应setting的点击
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			// 跳转到Setting界面
			startActivity(new Intent(this, SettingActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}
}
