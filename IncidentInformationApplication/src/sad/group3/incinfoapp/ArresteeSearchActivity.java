package sad.group3.incinfoapp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import sad.group3.domain.Arrestee;
import sad.group3.domain.ArresteeSearch;
import sad.group3.service.ArresteeSearchService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ArresteeSearchActivity extends BaseActivity {

	private static final String TAG = ArresteeSearchActivity.class
			.getSimpleName();

	private ProgressDialog mypDialog;
	private String toastMsg = null;

	private String firstName;
	private String lastName;
	private String phoneNum;

	private String gender;
	private String race;

	private EditText firstNameEditText, lastNameEditText, phoneNumEditText;
	private Spinner genderSpinner, raceSpinner;

	private ArresteeSearch arresteeSearch;
	private ArrayList<Arrestee> searchResultList;

	private Gson gson = new Gson();

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrestee_search_layout);
		// 重新计时
		sendMsgToHandler(3);

		init();
		uiInit();
		progressDialogShow();
	}

	private void init() {
		firstName = "";
		lastName = "";
		phoneNum = "";

		gender = "";
		race = "";

		arresteeSearch = null;
	}

	// 初始化界面widget
	private void uiInit() {
		genderSpinner = (Spinner) findViewById(R.id.gender);
		raceSpinner = (Spinner) findViewById(R.id.race);

		firstNameEditText = (EditText) findViewById(R.id.firstname);
		lastNameEditText = (EditText) findViewById(R.id.lastname);
		phoneNumEditText = (EditText) findViewById(R.id.phonenum);
	}

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Search Arrestee");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	public void arresteeSearch(View view) {
		// 输入控制
		if (inputControl()) {
			this.arresteeSearch = new ArresteeSearch(null, this.firstName,
					this.lastName, this.gender, this.race, this.phoneNum);
			Log.d(TAG, "arresteeSearch Instance attribute : " + "first name : "
					+ arresteeSearch.getAp_firstname() + "last number : "
					+ arresteeSearch.getAp_lastname() + "gender : "
					+ arresteeSearch.getAp_gender() + "race : "
					+ arresteeSearch.getRace_num() + "phone number : "
					+ arresteeSearch.getAp_phonenum());

			// 搜索操作....
			Thread thread = new Thread() {
				public void run() {
					Message msg = new Message();
					msg.what = 1;// 开始网络交互
					handler.sendMessage(msg);

					try {
						Log.d(TAG, "start ArresteeSearchService");
						searchResultList = ArresteeSearchService
								.arresteeSearch(arresteeSearch);
						if (searchResultList != null) {
							Log.d(TAG, searchResultList.size()
									+ " Results Found");

							Intent intent = new Intent(
									ArresteeSearchActivity.this,
									ArresteeSearchResultListActivity.class);
							Log.d(TAG, "start sending arrestee list");

							String resultListString = gson
									.toJson(searchResultList);

							writeFile("arresteeFile", resultListString);

							startActivity(intent);

							// finish();
						} else if (searchResultList == null) {
							toastMsg = "No Resutlt Found";
							// finish();
						}
						Log.d(TAG, searchResultList.size() + " strat handler");
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
		} else {
			Toast.makeText(getApplicationContext(),
					"Please enter at least one type of information",
					Toast.LENGTH_LONG).show();
			return;
		}
	}

	public boolean inputControl() {
		boolean name_flag;
		boolean phone_flag;
		boolean race_flag;
		boolean gender_flag;

		// NameEditText
		// control----------------------------------------------------
		if (!this.firstNameEditText.getText().toString().equals("")
				|| !this.lastNameEditText.getText().toString().equals("")) {
			if (this.firstNameEditText.getText().toString().contains("'")
					|| this.firstNameEditText.getText().toString()
							.contains(",")
					|| this.firstNameEditText.getText().toString()
							.contains("\"")
					|| this.firstNameEditText.getText().toString()
							.contains(":")) {
				Toast.makeText(
						getApplicationContext(),
						"First Name can not cotain the following characters : ''' ','  '\"' ':'  ",
						Toast.LENGTH_LONG).show();
				name_flag = false;
				this.firstName = "";
			} else if (this.lastNameEditText.getText().toString().contains("'")
					|| this.lastNameEditText.getText().toString().contains(",")
					|| this.lastNameEditText.getText().toString()
							.contains("\"")
					|| this.lastNameEditText.getText().toString().contains(":")) {
				Toast.makeText(
						getApplicationContext(),
						"Last Name can not cotain the following characters : ''' ','  '\"' ':'  ",
						Toast.LENGTH_LONG).show();
				name_flag = false;
				this.lastName = "";
			} else {
				name_flag = true;
				this.firstName = firstNameEditText.getText().toString().trim();
				this.lastName = lastNameEditText.getText().toString().trim();
				Log.d(TAG, "got name in the editText : "
						+ firstNameEditText.getText().toString()
						+ lastNameEditText.getText().toString());
			}

		} else {
			name_flag = false;
			Log.d(TAG, "no arrestee name in the editText");
			this.firstName = "";
			this.lastName = "";
		}
		// aptNumEditText
		// control----------------------------------------------------
		if (!this.phoneNumEditText.getText().toString().equals("")) {
			if (this.phoneNumEditText.getText().toString().contains("'")
					|| this.phoneNumEditText.getText().toString().contains(",")
					|| this.phoneNumEditText.getText().toString()
							.contains("\"")
					|| this.phoneNumEditText.getText().toString().contains(":")) {
				Toast.makeText(
						getApplicationContext(),
						"Phone Number can not cotain the following characters : ''' ','  '\"' ':'  ",
						Toast.LENGTH_LONG).show();
				phone_flag = false;
				this.phoneNum = "";
			} else {
				phone_flag = true;
				this.phoneNum = phoneNumEditText.getText().toString().trim();
				Log.d(TAG, "got phone number in the editText : "
						+ phoneNumEditText.getText().toString());
			}

		} else {
			phone_flag = false;
			this.phoneNum = "";
			Log.d(TAG, "no phobe Num in the editText");
		}
		// genderSpinnerSpinner
		// control----------------------------------------------------
		if (this.genderSpinner.getSelectedItemPosition() != 0) {
			gender_flag = true;
			if (this.genderSpinner.getSelectedItemPosition() == 1) {
				this.gender = "M";
			} else if (this.genderSpinner.getSelectedItemPosition() == 2) {
				this.gender = "F";
			}
			Log.d(TAG, "got gender in the spinner : " + this.gender);
		} else {
			gender_flag = false;
			this.gender = "";
			Log.d(TAG, "no gender selected in the gender Spinner");
		}
		// raceSpinnerSpinner
		// control----------------------------------------------------
		if (this.raceSpinner.getSelectedItemPosition() != 0) {
			race_flag = true;
			this.race = "" + raceSpinner.getSelectedItemPosition();

			Log.d(TAG, "got race in the spinner : " + this.race);
		} else {
			race_flag = false;
			this.race = "";
			Log.d(TAG, "no race selected in the gender Spinner");
		}

		if (name_flag == true || phone_flag == true || race_flag == true
				|| gender_flag == true) {
			return true;
		} else
			return false;
	}

	public void writeFile(String fileName, String writestr) throws IOException {
		try {
			Log.d(TAG, "arrestee list stored in file");
			FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

			byte[] bytes = writestr.getBytes();

			fout.write(bytes);

			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset(View view) {
		firstNameEditText.setText("");
		lastNameEditText.setText("");
		phoneNumEditText.setText("");

		genderSpinner.setSelection(0);
		raceSpinner.setSelection(0);

		firstName = "";
		lastName = "";
		phoneNum = "";
		gender = "";
		race = "";
	}
}
