package sad.group3.incinfoapp;

import java.util.ArrayList;

import com.google.gson.Gson;

import sad.group3.domain.Charge;
import sad.group3.service.ChargeSearchService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ChargeSearchActivity extends BaseActivity {
	private static final String TAG = ChargeSearchActivity.class
			.getSimpleName();

	private ProgressDialog mypDialog;
	private String toastMsg = null;

	private EditText chargedescEditText;

	private Charge chargeSearch;
	private ArrayList<Charge> searchResultList;

	private Gson gson = new Gson();

	private Handler handler = new Handler() {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_search_layout);
		// 重新计时
		sendMsgToHandler(3);
		progressDialogShow();

		init();
		uiInit();
	}

	private void init() {
		chargeSearch = null;
	}

	// 初始化界面widget
	private void uiInit() {

		chargedescEditText = (EditText) this.findViewById(R.id.chargedesc);
	}

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Search Charge");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	public void chargeSearch(View view) {
		// 输入控制
		if (inputControl()) {

			this.chargeSearch = new Charge(null, null, this.chargedescEditText
					.getText().toString());

			Log.d(TAG, "chargeSearch Instance attribute : "
					+ "charge description : " + chargeSearch.getChargeDesc());

		} else {

			Toast.makeText(getApplicationContext(),
					"Please enter at least one keyword you want to search",
					Toast.LENGTH_LONG).show();
			return;
		}

		// 搜索操作....
		Thread thread = new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 1;// 开始网络交互
				handler.sendMessage(msg);

				try {
					Log.d(TAG, "start ChargeSearchService");
					searchResultList = ChargeSearchService
							.chargeSearch(chargeSearch);
					if (searchResultList != null) {
						// 查询成功，界面跳转到charge search Result
						Intent intent = new Intent(ChargeSearchActivity.this,
								ChargeSearchResultListActivity.class);

						String resultListString = gson.toJson(searchResultList);

						intent.putExtra("resultListString", resultListString);

						startActivity(intent);
						// finish();
					} else if (searchResultList == null) {
						toastMsg = "No Resutlt Found";
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

	public void reset(View view) {
		chargedescEditText.setText("");

	}

	public boolean inputControl() {
		boolean chargedescription;
		if (!this.chargedescEditText.getText().toString().equals("")) {
			if (this.chargedescEditText.getText().toString().contains("'")
					|| this.chargedescEditText.getText().toString()
							.contains(",")
					|| this.chargedescEditText.getText().toString()
							.contains("\"")
					|| this.chargedescEditText.getText().toString()
							.contains(":")) {
				Toast.makeText(
						getApplicationContext(),
						"Charge description can not cotain the following characters : ''' ','  '\"' ':'  ",
						Toast.LENGTH_LONG).show();
				chargedescription = false;
			} else {
				chargedescription = true;
				Log.d(TAG, "got the charge description in the editText : "
						+ chargedescEditText.getText().toString());
			}

		} else {
			chargedescription = false;
			Log.d(TAG,
					"the charge content description can not be null, please input");
		}

		return chargedescription;
	}

}
