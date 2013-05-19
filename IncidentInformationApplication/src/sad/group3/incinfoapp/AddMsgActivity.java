package sad.group3.incinfoapp;

import sad.group3.domain.Message;
import sad.group3.service.MessageService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class AddMsgActivity extends TimeBaseActivity {
	private static final int RESULT_OK=1;
	private TextView msgTextView = null;
	private String officerInvNum = null;
	private String incID=null;

	private ProgressDialog mypDialog;
	private String toastMsg = null;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:// 开始
				mypDialog.show();// 让ProgressDialog显示
				break;
			case 2:// 结束
				mypDialog.cancel();// ProgressDialog的消失
				if (toastMsg != null) {
					Toast.makeText(getApplicationContext(), toastMsg,
							Toast.LENGTH_LONG).show();
					msgTextView.setText("");
				}
				break;
			}
		}
	};

	private void progressDialogShow(String title) {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle(title);// 设置ProgressDialog 标题
		mypDialog.setMessage("Uploading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_message_layout);
		
		sendMsgToHandler(3);

		progressDialogShow("Upload New Message");
		init();
	}

	private void init() {
		msgTextView = (TextView) this.findViewById(R.id.msg_edittext);
		officerInvNum = this.getIntent().getStringExtra("officer_inv_num");
		incID = this.getIntent().getStringExtra("inc_id");
	}

	public void submitMsg(View view) {
		final String msgContent = msgTextView.getText().toString();
		if (msgContent == null || "".equals(msgContent)) {
			Toast.makeText(getApplicationContext(),
					"Message content cannot be empty!", Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			Thread thread = new Thread() {
				public void run() {
					android.os.Message msg = new android.os.Message();
					msg.what = 1;// 开始网络交互
					handler.sendMessage(msg);

					try {
						Message message = new Message();
						message.setOfficerInvNum(officerInvNum);
						message.setMsgContent(msgContent);
						message.setIncID(incID);
						String result=MessageService.uploadMessage(message);
						if(result.equals("Add New Message Fail!")||result.equals("Connection Error")){
							toastMsg = result;
						}else{
							toastMsg = "Add New Message Success!";
							Intent intent=new Intent();
							intent.putExtra("incident_detail", result);
							setResult(RESULT_OK, intent);
							finish();
						}

						handler.sendEmptyMessage(0);
					} catch (Exception e) {
						e.printStackTrace();
					}

					msg = new android.os.Message();
					msg.what = 2;// 结束
					handler.sendMessage(msg);
				}
			};
			thread.start();
		}
	}

	public void reset(View view) {
		msgTextView.setText("");
	}
}
