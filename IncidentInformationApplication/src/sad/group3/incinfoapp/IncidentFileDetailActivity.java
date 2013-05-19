package sad.group3.incinfoapp;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import sad.group3.domain.Arrestee;
import sad.group3.domain.ArresteeSearch;
import sad.group3.domain.Incident;
import sad.group3.domain.Narrative;
import sad.group3.service.ArresteeDetailService;
import sad.group3.service.MessageService;
import sad.group3.service.NarrativeDetailService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class IncidentFileDetailActivity extends TimeBaseActivity {

	private static final String TAG = IncidentFileDetailActivity.class
			.getSimpleName();
	private static final int REQUEST_CODE=0;
	private static final int RESULT_OK=1;

	private Gson gson = new Gson();

	private Incident incidentDetail;

	private TextView incNoTextView, incDateTextView, incAddressTextView,
			incTypeTextView, infResourceTextView;
	private ListView arresteeListView, officerListView, messageListView,
			narrativeListView, callerListView;
	private String officerInvNum = null;

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

	// private ArrayAdapter<String> adapter;
	// Click Arrestee List to Arrestee File Interface
	private class ArresteesItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Log.d(TAG, "listView listener started");

			final ArresteeSearch arresteeSearch = new ArresteeSearch();

			Log.d(TAG, "arrestee list view position : " + position);

			arresteeSearch.setAp_num(incidentDetail.getArrestees()
					.get(position).getArresteeNum());

			Log.d(TAG, "arrestee selected, jump to get detail");
			mypDialog.setTitle("Search Arrestee File");

			// 搜索操作....
			Thread thread = new Thread() {
				public void run() {
					Message msg = new Message();
					msg.what = 1;// 开始网络交互
					handler.sendMessage(msg);

					try {
						Log.d(TAG, "start ArresteeDetailService");
						Arrestee arresteeDetail = ArresteeDetailService
								.arresteeDetail(arresteeSearch);
						if (arresteeDetail != null) {
							Intent intent = new Intent(
									IncidentFileDetailActivity.this,
									ArresteeFileDetailActivity.class);
							String DetailString = gson.toJson(arresteeDetail);

							intent.putExtra("DetailString", DetailString);
							Log.d(TAG,
									"ArresteeDetail send to Incident Detail activity");
							startActivity(intent);

						} else if (arresteeDetail == null) {
							toastMsg = "No arrestee file Found";
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

	// Click Narrative List to Narrative Detail Interface
	private class NarrativesItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Log.d(TAG, "listView listener started");

			final Narrative narrative = new Narrative();

			Log.d(TAG, "narrative list view position : " + position);

			narrative.setNarrNum(incidentDetail.getNarratives().get(position)
					.getNarrNum());

			Log.d(TAG, "narrative selected, jump to get detail");
			mypDialog.setTitle("Search Narrative File");

			// 搜索操作....
			Thread thread = new Thread() {
				public void run() {
					Message msg = new Message();
					msg.what = 1;// 开始网络交互
					handler.sendMessage(msg);

					try {
						Log.d(TAG, "start NarrativeDetailService");
						Narrative narrativeDetail = NarrativeDetailService
								.narrativeDetail(narrative);
						if (narrativeDetail != null) {
							Intent intent = new Intent(
									IncidentFileDetailActivity.this,
									NarrativeFileDetailActivity.class);
							String DetailString = gson.toJson(narrativeDetail);

							intent.putExtra("DetailString", DetailString);
							intent.putExtra("inc_id", incidentDetail.getId());
							Log.d(TAG,
									"NarrativeDetail send to Narrative Detail activity");
							startActivity(intent);

						} else if (narrativeDetail == null) {
							toastMsg = "No narrative file found";
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

	// Click Message List to Message Detail Interface
	private class MessagesItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Log.d(TAG, "listView listener started");

			final sad.group3.domain.Message message = new sad.group3.domain.Message();

			Log.d(TAG, "message list view position : " + position);

			message.setMsgNum(incidentDetail.getMessages().get(position)
					.getMsgNum());

			Log.d(TAG, "message selected, jump to get detail, Message Num="
					+ message.getMsgNum());
			mypDialog.setTitle("Search Message File");

			// 搜索操作....
			Thread thread = new Thread() {
				public void run() {
					Message msg = new Message();
					msg.what = 1;// 开始网络交互
					handler.sendMessage(msg);

					try {
						Log.d(TAG, "start MessageDetailService");
						sad.group3.domain.Message messageDetail = MessageService
								.messageDetail(message);
						if (messageDetail != null) {
							Intent intent = new Intent(
									IncidentFileDetailActivity.this,
									MessageFileDetailActivity.class);
							String DetailString = gson.toJson(messageDetail);
							Log.d(TAG, "DetailString:" + DetailString);
							intent.putExtra("DetailString", DetailString);
							Log.d(TAG,
									"MessageDetail send to Message Detail activity");
							startActivity(intent);

						} else if (messageDetail == null) {
							toastMsg = "No message file found";
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

	// Click Message List to Message Detail Interface
	private class CallersItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			String phoneNum = incidentDetail.getCallers().get(position)
					.getPhoneNum().trim();
			if (phoneNum == null || "".equals(phoneNum)
					|| "Unknown".equals(phoneNum)) {
				return;
			} else {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.CALL");
				intent.setData(Uri.parse("tel:" + phoneNum));
				startActivity(intent);
			}
		}
	}

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Search Arrestee File");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inc_file_detail_layout);
		sendMsgToHandler(3);
		
		// 判断是否是需要显示message 添加的incident file 界面
		officerInvNum = this.getIntent().getStringExtra("officer_inv_num");
		if (officerInvNum != null && !"".equals(officerInvNum)) {
			Button writeMsgBtn = (Button) this.findViewById(R.id.writemsg_btn);
			writeMsgBtn.setVisibility(View.VISIBLE);
		}

		init();
		uiInit();
		progressDialogShow();
		showInf(this.incidentDetail);
	}

	//
	private void init() {
		rebuildIncidentDetail();
	}

	public void rebuildIncidentDetail() {
		String json = this.getIntent().getStringExtra("DetailString");
		this.incidentDetail = gson.fromJson(json, Incident.class);
	}

	// 初始化界面widget
	private void uiInit() {

		incNoTextView = (TextView) findViewById(R.id.incident_no);
		incDateTextView = (TextView) findViewById(R.id.incident_date);
		incAddressTextView = (TextView) findViewById(R.id.incident_address);
		incTypeTextView = (TextView) findViewById(R.id.incident_type);
		infResourceTextView = (TextView) findViewById(R.id.incident_reason);
		// incCallerTextView = (TextView) findViewById(R.id.incident_caller);

		arresteeListView = (ListView) findViewById(R.id.arrests_listview);
		officerListView = (ListView) findViewById(R.id.officer_listview);
		messageListView = (ListView) findViewById(R.id.message_listview);
		narrativeListView = (ListView) findViewById(R.id.narrative_listview);
		callerListView = (ListView) findViewById(R.id.callers_listview);

		arresteeListView
				.setOnItemClickListener(new ArresteesItemClickListener());
		narrativeListView
				.setOnItemClickListener(new NarrativesItemClickListener());
		messageListView.setOnItemClickListener(new MessagesItemClickListener());
		callerListView.setOnItemClickListener(new CallersItemClickListener());
	}

	private void showInf(Incident incident) {
		incNoTextView.setText(incident.getId() + " ");
		incDateTextView.setText(incident.getDate());
		incAddressTextView.setText(incident.getStreetNum() + " "
				+ incident.getStreetName() + " St, Apt "
				+ incident.getApartmentNum() + " Worcester, MA");
		incTypeTextView.setText(incident.getIncidentType());
		infResourceTextView.setText(incident.getSource());
		// if (incident.getCallers().size() > 0) {
		// incCallerTextView.setText(incident.getCallers().get(0)
		// .getPhoneNum());
		// } else
		// incCallerTextView.setText("");
		showCallerList();
		showArresteeList();
		showOfficerList();
		showMessageList();
		showNarrativeList();
	}

	private void showArresteeList() {

		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < incidentDetail.getArrestees().size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			String arrestee_id = incidentDetail.getArrestees().get(i)
					.getArresteeNum();
			map.put("Item1Title", "Arrestee ID ：");
			map.put("Item1", arrestee_id);

			String arrestee_name = incidentDetail.getArrestees().get(i)
					.getFirstName()
					+ " " + incidentDetail.getArrestees().get(i).getLastName();
			map.put("Item2Title", "Name ：");
			map.put("Item2", arrestee_name);

			String arrestee_birth = incidentDetail.getArrestees().get(i)
					.getBirthdate();
			map.put("Item3Title", "Arrest Date ：");
			map.put("Item3", arrestee_birth);
			
			map.put("Item4", "more...");
			
			mylist.add(map);
		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.string_listitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "Item1Title", "Item1", "Item2Title", "Item2",
						"Item3Title", "Item3","Item4" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.Item1Title, R.id.Item1Text, R.id.Item2Title,
						R.id.Item2Text, R.id.Item3Title, R.id.Item3Text,R.id.Item_more_info_text });
		// 添加并且显示
		arresteeListView.setAdapter(mSchedule);
		setListViewHeightBasedOnChildren(arresteeListView);
	}

	private void showOfficerList() {

		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < incidentDetail.getOfficers().size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			String officer_id = incidentDetail.getOfficers().get(i).getId();
			map.put("Item1Title", "Officer ID ：");
			map.put("Item1", officer_id);

			String officer_name = incidentDetail.getOfficers().get(i)
					.getFirstName()
					+ " " + incidentDetail.getOfficers().get(i).getLastName();
			map.put("Item2Title", "Name ：");
			map.put("Item2", officer_name);

			String officer_primary = incidentDetail.getOfficers().get(i)
					.getPrimary();
			map.put("Item3Title", "Primary ：");
			map.put("Item3", officer_primary);
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
						R.id.Item2Text, R.id.Item3Title, R.id.Item3Text });
		// 添加并且显示
		officerListView.setAdapter(mSchedule);
		setListViewHeightBasedOnChildren(officerListView);
	}

	private void showMessageList() {
		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < incidentDetail.getMessages().size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			String msgauthor = incidentDetail.getMessages().get(i)
					.getMsgOfficerName();
			map.put("Item1Title", "Message Author ：");
			map.put("Item1", msgauthor);

			String msgdate = incidentDetail.getMessages().get(i).getMsgDate();
			map.put("Item2Title", "Submit Date ：");
			map.put("Item2", msgdate);

			map.put("Item4", "more...");

			mylist.add(map);
		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.string_two_listitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "Item1Title", "Item1", "Item2Title", "Item2",
						"Item4" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.Item1Title, R.id.Item1Text, R.id.Item2Title,
						R.id.Item2Text, R.id.Item_more_info_text });
		// 添加并且显示
		messageListView.setAdapter(mSchedule);
		setListViewHeightBasedOnChildren(messageListView);
	}

	private void showNarrativeList() {

		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < incidentDetail.getNarratives().size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			String narrativeauthor = incidentDetail.getNarratives().get(i)
					.getNarrOfficerName();
			map.put("Item1Title", "Narrative Author ：");
			map.put("Item1", narrativeauthor);

			String narrative_date = incidentDetail.getNarratives().get(i)
					.getNarrDate();
			map.put("Item2Title", "Submit Date ：");
			map.put("Item2", narrative_date);
			
			map.put("Item4", "more...");

			mylist.add(map);
		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.string_two_listitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "Item1Title", "Item1", "Item2Title", "Item2",
						"Item4" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.Item1Title, R.id.Item1Text, R.id.Item2Title,
						R.id.Item2Text, R.id.Item_more_info_text });
		// 添加并且显示
		narrativeListView.setAdapter(mSchedule);
		setListViewHeightBasedOnChildren(narrativeListView);
	}

	private void showCallerList() {

		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < incidentDetail.getCallers().size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			if (incidentDetail.getSource().equals("Patrol")) {
				String officerID = incidentDetail.getCallers().get(i).getId();
				map.put("Item1Title", "Officer ID ：");
				map.put("Item1", officerID);

				String officer_name = incidentDetail.getCallers().get(i)
						.getFullName();
				map.put("Item2Title", "Name ：");
				map.put("Item2", officer_name);

				String call_date = incidentDetail.getCallers().get(i)
						.getCallDate();
				map.put("Item3Title", "Call Date ：");
				map.put("Item3", call_date);
				
			} else {
				String callerName = incidentDetail.getCallers().get(i)
						.getFullName();
				map.put("Item1Title", "Caller Name ：");
				map.put("Item1", callerName);

				String phoneNum = incidentDetail.getCallers().get(i)
						.getPhoneNum().trim();
				map.put("Item2Title", "Phone Number ：");
				map.put("Item2", phoneNum);

				String call_date = incidentDetail.getCallers().get(i)
						.getCallDate();
				map.put("Item3Title", "Call Date ：");
				map.put("Item3", call_date);
				if(!"Unknown".equals(phoneNum)){
					map.put("Item4", "call...");
				}else {
					map.put("Item4", "");
				}
			}

			mylist.add(map);
		}
		SimpleAdapter mSchedule = null;
		if (incidentDetail.getSource().equals("Patrol")) {
			// 生成适配器，数组===》ListItem
			mSchedule = new SimpleAdapter(this, // 没什么解释
					mylist,// 数据来源
					R.layout.string_three_listitem,// ListItem的XML实现
					new String[] { "Item1Title", "Item1", "Item2Title",
							"Item2", "Item3Title", "Item3" }, new int[] {
							R.id.Item1Title, R.id.Item1Text, R.id.Item2Title,
							R.id.Item2Text, R.id.Item3Title, R.id.Item3Text });
		} else {
			mSchedule = new SimpleAdapter(this, // 没什么解释
					mylist,// 数据来源
					R.layout.string_listitem,// ListItem的XML实现
					new String[] { "Item1Title", "Item1", "Item2Title",
							"Item2", "Item3Title", "Item3", "Item4" }, new int[] {
							R.id.Item1Title, R.id.Item1Text, R.id.Item2Title,
							R.id.Item2Text, R.id.Item3Title, R.id.Item3Text,R.id.Item_more_info_text });
		}
		// 添加并且显示
		callerListView.setAdapter(mSchedule);
		setListViewHeightBasedOnChildren(callerListView);
	}

	public void toMap(View view) {
		// 界面跳转
		Intent intent = new Intent(IncidentFileDetailActivity.this,
				MapActivity.class);

		intent.putExtra("address", incidentDetail.getStreetNum() + " "
				+ incidentDetail.getStreetName() + " St, Worcester, MA");
		intent.putExtra("content", incidentDetail.getDate());
		startActivity(intent);
	}

	// 跳转到write message界面
	public void toWriteMsg(View view) {
		Intent intent = new Intent(IncidentFileDetailActivity.this,
				AddMsgActivity.class);
		intent.putExtra("officer_inv_num", officerInvNum);
		intent.putExtra("inc_id", incidentDetail.getId());
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
			if(resultCode==RESULT_OK){
				String incidentStr=data.getStringExtra("incident_detail");
				incidentDetail=new Gson().fromJson(incidentStr, Incident.class);
				showInf(this.incidentDetail);
			}
		}
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
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += 5;// if without this statement,the listview will be a
							// little short
		listView.setLayoutParams(params);
	}
}
