package sad.group3.incinfoapp;

import java.util.List;

import com.google.gson.Gson;

import sad.group3.domain.Incident;
import sad.group3.domain.Officer;
import sad.group3.service.LoginService;
import sad.group3.service.TaskService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

@SuppressLint({ "Registered", "HandlerLeak" })
public class BaseActivity extends TimeBaseActivity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

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
			}
		}
	};

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendMsgToHandler(3);
		progressDialogShow("Searching my tasks");
	};

	private void progressDialogShow(String title) {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle(title);// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	// 响应所有继承自该类的子类的点击assignment_button事件，跳到assignment_layout界面
	public void toAssignment(View view) {
		// 搜索操作....
		Thread thread = new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 1;// 开始网络交互
				handler.sendMessage(msg);

				try {
					Officer officer = new Officer();
					officer.setId(LoginService.o_id);
					List<Incident> incidentSearchResult = TaskService
							.tasksSearch(officer);
					if (incidentSearchResult != null) {
						// 查询成功，界面跳转到incident search Result
						Intent intent = new Intent(BaseActivity.this,
								IncidentSearchResultListActivity.class);

						String resultListString = new Gson()
								.toJson(incidentSearchResult);

						intent.putExtra("resultListString", resultListString);
						intent.putExtra("fromActivity", "BaseActivity");

						startActivity(intent);
						// finish();
					} else if (incidentSearchResult == null) {
						toastMsg = "No Resutlt found";
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
}
