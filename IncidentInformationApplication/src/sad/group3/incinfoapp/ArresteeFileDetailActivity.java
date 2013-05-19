package sad.group3.incinfoapp;

import java.util.ArrayList;
import java.util.HashMap;

import sad.group3.domain.Arrestee;
import sad.group3.domain.Incident;
import sad.group3.domain.IncidentSearch;
import sad.group3.service.IncidentDetailService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;

@SuppressLint({ "CutPasteId", "HandlerLeak" })
public class ArresteeFileDetailActivity extends TimeBaseActivity {

	private static final String TAG = ArresteeFileDetailActivity.class
			.getSimpleName();

	private Gson gson = new Gson();

	private Arrestee arresteeDetail;
	private ProgressDialog mypDialog;
	private String toastMsg = null;

	private TextView arresteeNameTextView, arresteeIdTextView,
			birthDateTextView, arresteeGenderTextView, raceTextView,
			hairColorTextView, heightTextView, weightTextView,
			phoneNumTextView, licenseTextView, otherTextView;
	private ListView chargesListView, incidentListView;
	private ImageView photoImageView;
	
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
	
	private class IncidentsOnItemclickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Incident selectedIncident = arresteeDetail.getIncidents().get(position);
			String inc_id = selectedIncident.getId();
			final String officerInvNum = selectedIncident.getOfficerInvNum();

			final IncidentSearch incidentSearch = new IncidentSearch();
			incidentSearch.setInc_id(inc_id);

			// 搜索操作....
			Thread thread = new Thread() {
				public void run() {
					Message msg = new Message();
					msg.what = 1;// 开始网络交互
					handler.sendMessage(msg);

					try {
						Log.d(TAG, "start IncidentDetailService");
						Incident incidentDetail = IncidentDetailService
								.incidentDetail(incidentSearch);
						if (incidentDetail != null) {
							Intent intent = new Intent(
									ArresteeFileDetailActivity.this,
									IncidentFileDetailActivity.class);
							String DetailString = gson.toJson(incidentDetail);

							intent.putExtra("DetailString", DetailString);
							intent.putExtra("officer_inv_num", officerInvNum);

							Log.d(TAG,
									"IncidentDetail send to Incident Detail activity");
							startActivity(intent);
							// finish();

						} else if (incidentDetail == null) {
							toastMsg = "No incident file found";
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrestee_file_detail_layout);
		
		sendMsgToHandler(3);

		Log.d(TAG, "arrestee file detail activity started");

		init();
		uiInit();
		progressDialogShow();

		showInf(this.arresteeDetail);
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

	private void init() {
		this.arresteeDetail = new Arrestee();
		Log.d(TAG, " starte to receive arrestee file");
		rebuildArresteeDetail();
		Log.d(TAG, " finish to receive arrestee file");
	}

	public void rebuildArresteeDetail() {
		String json = this.getIntent().getStringExtra("DetailString");
		Log.d(TAG, " receive arrestee detail string : " + json);
		this.arresteeDetail = gson.fromJson(json, Arrestee.class);
	}

	// 初始化界面widget
	private void uiInit() {
		Log.d(TAG, "start initiate UI");

		photoImageView = (ImageView) findViewById(R.id.person_photo);
		;

		arresteeNameTextView = (TextView) findViewById(R.id.arrestt_name);
		arresteeIdTextView = (TextView) findViewById(R.id.arrestee_number);
		birthDateTextView = (TextView) findViewById(R.id.arrestee_birthdate);
		arresteeGenderTextView = (TextView) findViewById(R.id.arrestee_gender);
		raceTextView = (TextView) findViewById(R.id.arrestt_race);
		hairColorTextView = (TextView) findViewById(R.id.person_haircolor);
		heightTextView = (TextView) findViewById(R.id.person_height);
		weightTextView = (TextView) findViewById(R.id.person_weights);
		phoneNumTextView = (TextView) findViewById(R.id.person_phonenum);
		licenseTextView = (TextView) findViewById(R.id.person_license);
		otherTextView = (TextView) findViewById(R.id.arrestee_othercharacteristics);

		chargesListView = (ListView) findViewById(R.id.charge_listView);
		incidentListView = (ListView) findViewById(R.id.incident_listView);
		
		incidentListView.setOnItemClickListener(new IncidentsOnItemclickListener());

		Log.d(TAG, "finish initiate UI");
	}
	
	private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height += 5;//if without this statement,the listview will be a little short
        listView.setLayoutParams(params);
    }

	private void showInf(Arrestee arresteeDetail) {
		Log.d(TAG,
				"arresteeDetail first name : " + arresteeDetail.getFirstName()
						+ "\n" + "arresteeDetail last name : "
						+ arresteeDetail.getLastName() + "\n"
						+ "arresteeDetail birth date : "
						+ arresteeDetail.getBirthdate() + "\n"
						+ "arresteeDetail gender : "
						+ arresteeDetail.getGender() + "\n"
						+ "arresteeDetail race : " + arresteeDetail.getRace()
						+ "\n" + "arresteeDetail hair color : "
						+ arresteeDetail.getHairColor() + "\n"
						+ "arresteeDetail height : "
						+ arresteeDetail.getHeight() + "\n"
						+ "arresteeDetail weight : "
						+ arresteeDetail.getWeight() + "\n"
						+ "arresteeDetail phone Number : "
						+ arresteeDetail.getPhoneNum() + "\n"
						+ "arresteeDetail license : "
						+ arresteeDetail.getLicenseID() + "\n"
						+ "arresteeDetail other : "
						+ arresteeDetail.getOtherCharacteristics() + "\n"
						+ "arresteeDetail photo : " + arresteeDetail.getPhoto()
						+ "\n");

		Log.d(TAG, "textView set started");
		arresteeNameTextView.setText(arresteeDetail.getFirstName() + " "
				+ arresteeDetail.getLastName());
		arresteeIdTextView.setText(arresteeDetail.getArresteeNum());
		birthDateTextView.setText(arresteeDetail.getBirthdate());
		arresteeGenderTextView.setText(arresteeDetail.getGender());
		raceTextView.setText(arresteeDetail.getRace());
		hairColorTextView.setText(arresteeDetail.getHairColor());
		heightTextView.setText(arresteeDetail.getHeight());
		weightTextView.setText(arresteeDetail.getWeight());
		phoneNumTextView.setText(arresteeDetail.getPhoneNum());
		if (arresteeDetail.getLicenseID() != null) {
			licenseTextView.setText(arresteeDetail.getLicenseID());
		} else {
			licenseTextView.setText("---");
		}
		if (arresteeDetail.getOtherCharacteristics() != null) {
			otherTextView.setText(arresteeDetail.getOtherCharacteristics());
		} else {
			otherTextView.setText("---");
		}
		Log.d(TAG, "textView set ended");

		Log.d(TAG, "bitmap decode ByteArray started");
		Bitmap bitMap = BitmapFactory.decodeByteArray(
				arresteeDetail.getPhoto(), 0, arresteeDetail.getPhoto().length,
				null);
		Log.d(TAG, "bitmap decode ByteArray ended");
		photoImageView.setImageBitmap(bitMap);
		Log.d(TAG, "ImageView set ended");

		Log.d(TAG, "textView set started");

		Log.d(TAG, "show ChargesList started");
		showChargesList();
		Log.d(TAG, "show ChargesList ended");

		Log.d(TAG, "show IncidentList started");
		showIncidentList();
		Log.d(TAG, "show IncidentList ended");
	}

	private void showChargesList() {
		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < arresteeDetail.getCharges().size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			String inc_id = arresteeDetail.getCharges().get(i).getIncID();
			map.put("Item1Title", "Incident No. ：");
			map.put("Item1", inc_id);

			String charge_num = arresteeDetail.getCharges().get(i)
					.getChargeNum();
			map.put("Item2Title", "Charge No. ：");
			map.put("Item2", charge_num);

			String charge_desc = arresteeDetail.getCharges().get(i)
					.getChargeDesc();
			map.put("Item3Title", "Charge Desc ：");
			map.put("Item3", charge_desc);

			mylist.add(map);
		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.string_three_listitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "Item1Title", "Item1", "Item2Title", "Item2",
						"Item3Title", "Item3" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.Item1Title, R.id.Item1Text, R.id.Item2Title,
						R.id.Item2Text, R.id.Item3Title, R.id.Item3Text});
		// 添加并且显示
		chargesListView.setAdapter(mSchedule);
		
		setListViewHeightBasedOnChildren(chargesListView);
	}

	private void showIncidentList() {
		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < arresteeDetail.getIncidents().size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			String inc_id = arresteeDetail.getIncidents().get(i).getId();
			map.put("Item1Title", "Incident No. ：");
			map.put("Item1", inc_id);

			String inc_date = arresteeDetail.getIncidents().get(i).getDate();
			map.put("Item2Title", "Incident Date ：");
			map.put("Item2", inc_date);

			String inc_type = arresteeDetail.getIncidents().get(i)
					.getIncidentType();
			map.put("Item3Title", "Incident Type ：");
			map.put("Item3", inc_type);
			
			map.put("Item4", "more...");

			mylist.add(map);
		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.string_listitem,// ListItem的XML实现

				new String[] { "Item1Title", "Item1", "Item2Title", "Item2",
						"Item3Title", "Item3","Item4" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.Item1Title, R.id.Item1Text, R.id.Item2Title,
						R.id.Item2Text, R.id.Item3Title, R.id.Item3Text,R.id.Item_more_info_text});
		// 添加并且显示
		incidentListView.setAdapter(mSchedule);
		
		setListViewHeightBasedOnChildren(incidentListView);
	}

	public void call(View view) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:" + arresteeDetail.getPhoneNum()));
		startActivity(intent);
	}

}
