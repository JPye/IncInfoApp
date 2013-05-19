package sad.group3.incinfoapp;

//import sad.group3.service.LoginService;
import sad.group3.service.LogoutService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
//import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "Registered" })
public class TimeBaseActivity extends Activity {
	public static Thread timeThread;
	private ProgressDialog mypDialog;
	private String toastMsg = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 开始
				mypDialog.show();// 让ProgressDialog显示
				break;
			case 2:// 结束
				mypDialog.cancel();// ProgressDialog的消失
				if (toastMsg != null) {
					Toast.makeText(getApplicationContext(), toastMsg,
							Toast.LENGTH_LONG).show();
				}
				break;
			case 3:// 重新计时
				// timeThread.interrupt();
				// timeThread = new Thread(new TimeThread());
				// timeThread.start();
				break;
			case 4:
				timeThread = null;
				break;
			}
		}
	};

	// Confirmation about log out
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("Are you sure to exit?");
		builder.setTitle("Confirmation");
		builder.setPositiveButton("OK",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Thread thread = new Thread() {
							public void run() {
								sendMsgToHandler(1);// start

								try {
									String logoutResult = LogoutService
											.logout();
									if ("Error".equals(logoutResult)) {
										toastMsg = "Logout fail, system exit!";
										sendMsgToHandler(2);
										Thread.sleep(2000);
									}
									System.exit(0);
									handler.sendEmptyMessage(0);
								} catch (Exception e) {
									e.printStackTrace();
								}

								sendMsgToHandler(2);// stop
							}
						};
						thread.start();
					}
				});
		builder.setNegativeButton("Cancel",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void progressDialogShow(String title) {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle(title);// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	// 给handler发送msg
	public void sendMsgToHandler(int what) {
		Message message = new Message();
		message.what = what;
		handler.sendMessage(message);
	}

	public class TimeThread implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(600000000);

				sendMsgToHandler(1);

				try {
					String logoutResult = LogoutService.logout();
					if ("Error".equals(logoutResult)) {
						toastMsg = "Logout fail, system exit!";
						sendMsgToHandler(2);
						Thread.sleep(2000);
					}
					System.exit(0);
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}

				sendMsgToHandler(2);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		progressDialogShow("Log Out");

		timeThread = new Thread(new TimeThread());
		timeThread.start();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("state", "onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if (LoginService.o_id != null && !LoginService.o_id.equals("")) {
		// sendMsgToHandler(3);
		// Log.i("state", "onResume");
		// } else {
		// Log.i("state", "Has loged out!");
		// sendMsgToHandler(4);
		// startActivity(new Intent(this, LoadActivity.class));
		// finish();
		// }

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("state", "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("state", "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("state", "onDestory");
	}

	public void backBtn(View view) {
		this.finish();
	}
}
