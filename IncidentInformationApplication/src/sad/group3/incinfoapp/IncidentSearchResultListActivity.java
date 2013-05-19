package sad.group3.incinfoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import sad.group3.domain.Incident;
import sad.group3.domain.IncidentSearch;
import sad.group3.service.IncidentDetailService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class IncidentSearchResultListActivity extends TimeBaseActivity {

	private static final String TAG = IncidentSearchResultListActivity.class
			.getSimpleName();

	private ArrayList<Incident> searchResultList;
	private Gson gson = new Gson();
	private ProgressDialog mypDialog;
	private String toastMsg = null;

	private ListView listView;
	private TextView resultNum;

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
		setContentView(R.layout.inc_search_resultlist_layout);
		sendMsgToHandler(3);
		Intent intent = this.getIntent();
		String fromActivity = intent.getStringExtra("fromActivity");
		if (fromActivity != null && !"".equals(fromActivity)) {
			TextView textView = (TextView) this
					.findViewById(R.id.inc_list_title);
			textView.setText("My Tasks");
		}

		progressDialogShow();
		init();
		uiInit();
	}

	// rebuild Result List
	private void init() {
		rebuildResultList();
		Log.d(TAG, "inc_search_resultlist : " + this.searchResultList.size());
	}

	private void uiInit() {
		// create ListView with Result List
		listView = (ListView) findViewById(R.id.inc_result_listView);
		listView.setOnItemClickListener(new ItemClickListener());
		resultNum = (TextView) this.findViewById(R.id.result_title);
		resultNum.setText(searchResultList.size() + " Results");
		showList();
	}

	private void showList() {

		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < searchResultList.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("Item1Title", "Incident Date : ");
			String date = searchResultList.get(i).getDate();
			map.put("Item1Text", date);

			map.put("Item2Title", "Incident Type : ");
			map.put("Item2Text", searchResultList.get(i).getIncidentType());

			map.put("Item3Title", "Incident Address : ");
			map.put("Item3Text", searchResultList.get(i).getStreetNum()+" "+searchResultList.get(i).getStreetName()+" St, Apt "+searchResultList.get(i).getApartmentNum());
			
			map.put("Item4Title", "more...");
			
			mylist.add(map);
		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.string_listitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "Item1Title", "Item1Text", "Item2Title",
						"Item2Text", "Item3Title", "Item3Text", "Item4Title" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.Item1Title, R.id.Item1Text, R.id.Item2Title,
						R.id.Item2Text, R.id.Item3Title, R.id.Item3Text,R.id.Item_more_info_text });
		// 添加并且显示
		listView.setAdapter(mSchedule);
	}

	public void rebuildResultList() {
		String json = this.getIntent().getStringExtra("resultListString");
		this.searchResultList = gson.fromJson(json,
				new TypeToken<List<Incident>>() {
				}.getType());
	}

	private class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Incident selectedIncident = searchResultList.get(position);
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
									IncidentSearchResultListActivity.this,
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

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Search Incident File");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}
}
