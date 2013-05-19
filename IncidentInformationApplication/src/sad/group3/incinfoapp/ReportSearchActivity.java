package sad.group3.incinfoapp;

import java.util.Calendar;

import com.google.gson.Gson;

import sad.group3.domain.Report;
import sad.group3.service.ReportService;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ReportSearchActivity extends BaseActivity {

	private static final String TAG = ReportSearchActivity.class
			.getSimpleName();

	private String reportType;
	private String reportDate;

	private EditText yearEditText;
	private Spinner reportTypeSpinner;

	private Report reportSearch;
	private Report searchResult;

	private ProgressDialog mypDialog;
	private DatePickerDialog dateDialog;
	private String toastMsg = null;

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
		setContentView(R.layout.report_search_layout);
		sendMsgToHandler(3);
		init();
		uiInit();

		progressDialogShow();
	}

	private void init() {
		reportType = "";

	}

	// 初始化界面widget
	private void uiInit() {
		yearEditText = (EditText) findViewById(R.id.report_year_picker);
		reportTypeSpinner = (Spinner) findViewById(R.id.report_type_select_spinner);
		setDate();
	}

	// 响应reset button
	public void reset(View view) {
		yearEditText.setText("");
		reportTypeSpinner.setSelection(0);
	}

	public void createReport(View view) {
		toastMsg=null;
		// 输入控制
		if (inputControl()) {
			this.reportSearch = new Report();
			reportSearch.setYear(this.reportDate);
			reportSearch.setReportType(reportType);
			Log.d(TAG, "report attribute : " + "Year : " + reportDate
					+ "Report Type : " + reportType);
		} else {
			return;
		}

		// 搜索操作....
		Thread thread = new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 1;// 开始网络交互
				handler.sendMessage(msg);

				try {
					Log.d(TAG, "start IncidentSearchService");
					searchResult = ReportService.createReport(reportSearch);
					if (searchResult != null) {
						// 查询成功，界面跳转到incident search Result
						Intent intent = new Intent(ReportSearchActivity.this,
								ReportShowActivity.class);

						String resultString = gson.toJson(searchResult);

						intent.putExtra("resultString", resultString);
						intent.putExtra("reportType", reportType);

						startActivity(intent);
						// finish();
					} else if (searchResult == null) {
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

	private boolean inputControl() {
		boolean date_flag;
		boolean type_flag;

		if (!this.yearEditText.getText().toString().equals("")) {
			this.reportDate = this.yearEditText.getText().toString();
			date_flag = true;
		} else {
			date_flag = false;
		}

		if (this.reportTypeSpinner.getSelectedItemPosition() != 0) {
			if (reportTypeSpinner.getSelectedItemPosition() == 1) {
				this.reportType = "IncQtySector";
			} else if (reportTypeSpinner.getSelectedItemPosition() == 2) {
				this.reportType = "IncQtyMonth";
			} else if (reportTypeSpinner.getSelectedItemPosition() == 3) {
				this.reportType = "CallQtyPeriod";
			}
			type_flag = true;
		} else {
			type_flag = false;
		}

		if (type_flag == false && date_flag == false) {
			Toast.makeText(getApplicationContext(),
					"Please select report year and type!", Toast.LENGTH_LONG)
					.show();
			return false;
		} else if (date_flag == false) {
			Toast.makeText(getApplicationContext(),
					"Please select report year!", Toast.LENGTH_LONG).show();
			return false;
		} else if (type_flag == false) {
			Toast.makeText(getApplicationContext(),
					"Please select report type!", Toast.LENGTH_LONG).show();
			return false;
		} else
			return true;
	}

	private void setDate() {
		yearEditText.setOnClickListener(new YearOnClickListener());
	}

	private class YearOnClickListener implements OnClickListener {

		public void onClick(View v) {
			final EditText editText = (EditText) v;
			final Calendar calendar = Calendar.getInstance();
			dateDialog = new DatePickerDialog(ReportSearchActivity.this,
					new OnDateSetListener() {
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							editText.setText(year + "");
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog.setTitle("Set Year");
			dateDialog.show();

			DatePicker dp = findDatePicker((ViewGroup) dateDialog.getWindow()
					.getDecorView());
			if (dp != null) {
				((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
						.getChildAt(0).setVisibility(View.GONE);
				((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
						.getChildAt(1).setVisibility(View.GONE);
			}
		}
	}

	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Search Report");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

}
