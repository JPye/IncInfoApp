package sad.group3.incinfoapp;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import sad.group3.domain.Incident;
import sad.group3.domain.IncidentSearch;
import sad.group3.service.IncidentSearchService;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class IncidentSearchActivity extends BaseActivity {
	private static final String TAG = IncidentSearchActivity.class.getSimpleName();
	
	private ProgressDialog mypDialog;
	private String toastMsg = null;
	
	private EditText streetNameEditText, streetNumEditText, aptNumEditText, fromDateEditText, toDateEditText;
	private Spinner incTypeSpinner, districtSpinner, infResourceSpinner, incResovledSpinner;
	
	private String districtNum;
	private String incTypeNum;
	private String infResource;
	private String incResolved;
	
	private IncidentSearch incidentSearch;
	private ArrayList<Incident> incidentSearchResult;
	
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
				if(toastMsg!=null){
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
		setContentView(R.layout.inc_search_layout);
		
		sendMsgToHandler(3);
		
		init();
		uiInit();
		progressDialogShow();
	}
	//initiate search information
	private void init()
	{
		
		districtNum = "";
		incTypeNum = "";
		infResource = "";
		incResolved = "";
		
		incidentSearch = null;
		incidentSearchResult = null;
	}	
	
	// 初始化界面widget
	private void uiInit() {
		incTypeSpinner = (Spinner) findViewById(R.id.inc_type);
		districtSpinner = (Spinner) findViewById(R.id.district_spinner);
		infResourceSpinner = (Spinner) findViewById(R.id.info_resource_type);
		incResovledSpinner = (Spinner) findViewById(R.id.inc_resovled_spinner);
		
		streetNameEditText = (EditText) findViewById(R.id.street_name);
		streetNumEditText = (EditText) findViewById(R.id.street_num);
		aptNumEditText = (EditText) findViewById(R.id.apt_num);
		fromDateEditText = (EditText) findViewById(R.id.fromdate);
		toDateEditText = (EditText) findViewById(R.id.todate);
		
		setDate();
	}	
	private void setDate() {
		fromDateEditText.setOnClickListener(new DateOnClickListener());
		toDateEditText.setOnClickListener(new DateOnClickListener());
	}

	private class DateOnClickListener implements OnClickListener {
		public void onClick(View v) {
			final EditText editText = (EditText) v;
			final Calendar calendar = Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			new DatePickerDialog(IncidentSearchActivity.this,
					new OnDateSetListener() {
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							String month=(monthOfYear+1)>=10 ? ((monthOfYear+1)+"") : ("0"+(monthOfYear+1));
							String day=dayOfMonth>=10 ? (dayOfMonth+"") : ("0"+dayOfMonth);
							editText.setText( month+ "-" + day + "-" + year);
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)).show();
		}
	}

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Search Incident");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}
	
	public void cleardate(View view) {
		if (view.getId() == R.id.reset_from_btn) {
			fromDateEditText.setText("");
		} else {
			toDateEditText.setText("");
		}
	}

	public void reset(View view) {
		streetNameEditText.setText("");
		streetNumEditText.setText("");
		aptNumEditText.setText("");
		fromDateEditText.setText("");
		toDateEditText.setText("");

		incTypeSpinner.setSelection(0);
		districtSpinner.setSelection(0);
		infResourceSpinner.setSelection(0);
		incResovledSpinner.setSelection(0);
	}
	

	public void IncidentSearch(View view) {
		//输入控制
		if(inputControl())
		{
			 
			this.incidentSearch = new IncidentSearch(null, fromDateEditText.getText().toString(), toDateEditText.getText().toString(),
					this.incTypeNum , this.streetNameEditText.getText().toString(), this.streetNumEditText.getText().toString(),
					this.aptNumEditText.getText().toString(), this.districtNum, this.infResource,
					this.incResolved);
			Log.d(TAG, "incidentSearch Instance attribute : " 
					+ "street name : " + incidentSearch.getInc_StName()
					+ "street number : " + incidentSearch.getInc_StNum()
					+ "apt number : " + incidentSearch.getInc_AptNum()
					+ "date : " + " from " + incidentSearch.getFrom_inc_date()
					+ " to " + incidentSearch.getTo_inc_date()
					+ "district nmber : " + incidentSearch.getDist_Num()
					+ "incdent type nmber : " + incidentSearch.getIncType_Num()
					+ "infomation resource : " + incidentSearch.getInc_Source()
					);
			
		}
		else
		{
			
			Toast.makeText(getApplicationContext(), "Please enter at least one type of information",
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
					Log.d(TAG, "start IncidentSearchService");
					incidentSearchResult = IncidentSearchService.incidentSearch(incidentSearch);
					if (incidentSearchResult!= null) {
						// 查询成功，界面跳转到incident search Result
						Intent intent = new Intent(IncidentSearchActivity.this,
								IncidentSearchResultListActivity.class);
						
						String resultListString =  gson.toJson(incidentSearchResult);
		                
		                intent.putExtra("resultListString", resultListString); 

						startActivity(intent);
						//finish();
					}else if(incidentSearchResult == null){
						toastMsg="No Resutlt found";
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
	
	public boolean inputControl()
	{
		boolean address_flag;
		boolean date_flag;
		boolean incType_flag;
		boolean district_flag;
		boolean resource_flag;
		boolean resolved_flag;
		// streetNameEditText control----------------------------------------------------
		if(!this.streetNameEditText.getText().toString().equals("")){
			if(this.streetNameEditText.getText().toString().contains("'")||this.streetNameEditText.getText().toString().contains(",")||this.streetNameEditText.getText().toString().contains("\"")||this.streetNameEditText.getText().toString().contains(":"))
			{
				Toast.makeText(getApplicationContext(), "Street Name can not cotain the following characters : ''' ','  '\"' ':'  ",
						Toast.LENGTH_LONG).show();
				address_flag = false;
			}else
			{
				address_flag = true;
				Log.d(TAG, "got street name in the editText : " + streetNameEditText.getText().toString());
			}
			
		}else
		{
			address_flag = false;
			Log.d(TAG, "no street name in the editText");
		} 
		// streetNumEditText control----------------------------------------------------
		if(!this.streetNumEditText.getText().toString().equals("")){

			if(this.streetNumEditText.getText().toString().contains("'")||this.streetNumEditText.getText().toString().contains(",")||this.streetNumEditText.getText().toString().contains("\"")||this.streetNameEditText.getText().toString().contains(":"))
			{
				Toast.makeText(getApplicationContext(), "Street Number can not cotain the following characters : ''' ','  '\"' ':' ",
						Toast.LENGTH_LONG).show();
			}else
			{
				Log.d(TAG, "got street number in the editText : " + streetNumEditText.getText().toString());
			}
		}else
		{
			Log.d(TAG, "no streetNum in the editText");
		} 
		// aptNumEditText control----------------------------------------------------
		if(!this.aptNumEditText.getText().toString().equals("")){
			if(this.aptNumEditText.getText().toString().contains("'")||this.aptNumEditText.getText().toString().contains(",")||this.aptNumEditText.getText().toString().contains("\"")||this.aptNumEditText.getText().toString().contains(":"))
			{
				Toast.makeText(getApplicationContext(), "Apartment Number can not cotain the following characters : ''' ','  '\"' ':'  ",
						Toast.LENGTH_LONG).show();
			}else
			{
				Log.d(TAG, "got aptment number in the editText : " + aptNumEditText.getText().toString());
			}

		}else
		{
			Log.d(TAG, "no aptNum in the editText");
		}
		// dateEditText control----------------------------------------------------
		if(!this.toDateEditText.getText().toString().equals("") || !this.fromDateEditText.getText().toString().equals("")){
			if(!this.toDateEditText.getText().toString().equals("") && !this.fromDateEditText.getText().toString().equals(""))
			{
				if(!this.compareDate(this.fromDateEditText.getText().toString(), this.toDateEditText.getText().toString()))
				{
					Toast.makeText(getApplicationContext(), "Start date must be befroe End date",
							Toast.LENGTH_LONG).show();
					date_flag = false;
				}else
				{
					Log.d(TAG, "got date in the editText :  from date  " + fromDateEditText.getText().toString() + " to date  " + toDateEditText.getText().toString());
					date_flag = true;
				}
			}
			else
			{
				date_flag = true;
				Log.d(TAG, "got date in the editText :  from date  " + fromDateEditText.getText().toString() + " to date  " + toDateEditText.getText().toString());
			}

		}else
		{
			date_flag = false;
			Log.d(TAG, "no date in the editText");
		}
		
		// incTypeSpinner control----------------------------------------------------
		if(this.incTypeSpinner.getSelectedItemPosition() != 0)
		{
			incType_flag = true;
			this.incTypeNum = "" + (this.incTypeSpinner.getSelectedItemPosition()) ;
			Log.d(TAG, "got incTypeNum in the spinner : " + incTypeSpinner.getSelectedItemPosition());
		}else
		{
			incType_flag = false;
			this.incTypeNum = "";
			Log.d(TAG, "no incTpye selected in the incType Spinner");
		}
		// districtSpinner control----------------------------------------------------
		if(this.districtSpinner.getSelectedItemPosition() != 0)
		{
			district_flag = true;
			this.districtNum = "" + (this.districtSpinner.getSelectedItemPosition() - 1) ;
			Log.d(TAG, "got districtNum in the spinner : " + districtSpinner.getSelectedItemPosition());
		}else
		{
			district_flag = false;
			this.districtNum = "";
			Log.d(TAG, "no districtNum selected in the districtNum Spinner");
		}
		// ResourceSpinner control----------------------------------------------------
		if(this.infResourceSpinner.getSelectedItemPosition() != 0)
		{
			resource_flag = true;
			this.infResource = "" + (this.infResourceSpinner.getSelectedItemPosition() - 1) ;
			Log.d(TAG, "got information Resource in the spinner : " + infResourceSpinner.getSelectedItemPosition());
		}else
		{
			resource_flag = false;
			this.infResource = "";
			Log.d(TAG, "no information Resource selected in the infResource Spinner");
		}
		// ResolvedSpinner control----------------------------------------------------
		if(this.incResovledSpinner.getSelectedItemPosition() != 0)
		{
			resolved_flag = true;
			this.incResolved = "" + (this.incResovledSpinner.getSelectedItemPosition() - 1) ;
			Log.d(TAG, "got incident status in the spinner : " + incResovledSpinner.getSelectedItemPosition());
		}else
		{
			resolved_flag = false;
			this.incResolved = "";
			Log.d(TAG, "no incident status selected in the infResource Spinner");
		}
		
		
		if( address_flag == true || incType_flag == true || district_flag == true || date_flag == true||resource_flag == true||resolved_flag==true)
		{
			return true;
		}
		else return false;
	}

	private boolean compareDate(String fromDate, String toDate) {
		boolean flag = false;
		String[] from = fromDate.split("-");
		String[] to = toDate.split("-");
		if (Integer.parseInt(to[2]) > Integer.parseInt(from[2])
				|| Integer.parseInt(to[2]) == Integer.parseInt(from[2])&& Integer.parseInt(to[0]) > Integer.parseInt(from[0])
				|| Integer.parseInt(to[2]) == Integer.parseInt(from[2])&& Integer.parseInt(to[0]) == Integer.parseInt(from[0])
				&& Integer.parseInt(to[1]) > Integer.parseInt(from[1])) {
			flag = true;
		}
		return flag;
	}
}
