package sad.group3.incinfoapp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sad.group3.domain.Arrestee;
import sad.group3.domain.Charge;
import sad.group3.service.ChargeDetailService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
public class ChargeSearchResultListActivity extends TimeBaseActivity {

	private static final String TAG = ChargeSearchResultListActivity.class
			.getSimpleName();

	private ArrayList<Charge> searchResultList;
	private Gson gson = new Gson();
	private ProgressDialog mypDialog;
	private String toastMsg = null;

	private ListView listView;
	private TextView resultNumTextView;

	private Charge chargeSearch;

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
		setContentView(R.layout.charge_search_resultlist_layout);
		sendMsgToHandler(3);
		progressDialogShow();

		init();
		uiInit();

	}

	// rebuild Result List
	private void init() {
		searchResultList = new ArrayList<Charge>();
		rebuildResultList();
		Log.d(TAG, "charge_search_resultlist : " + this.searchResultList.size());
	}

	private void uiInit() {
		Log.d(TAG, "UI initiate start");
		// create ListView with Result List
		resultNumTextView = (TextView) this.findViewById(R.id.result_title);
		resultNumTextView.setText(searchResultList.size() + " Results");
		listView = (ListView) findViewById(R.id.charge_result_listView);
		listView.setOnItemClickListener(new ItemClickListener());
		showList();
	}

	private void showList() {

		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < searchResultList.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("Item1Title", "Charge No. ：");
			map.put("Item1Text", searchResultList.get(i).getChargeNum());

			map.put("Item2Title", "Charge Content: ");
			map.put("Item2Text", searchResultList.get(i).getChargeDesc());

			map.put("Item3Title", "Quantity of Arrestee: ");
			map.put("Item3Text", searchResultList.get(i).getArresteeQty());
			
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
				new TypeToken<List<Charge>>() {
				}.getType());
	}

	private class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			final String charge_num = searchResultList.get(position)
					.getChargeNum();
			final String charge_content = searchResultList.get(position)
					.getChargeDesc();

			chargeSearch = new Charge();
			chargeSearch.setChargeNum(charge_num);
			Log.d(TAG, "jump to get detail");

			// 搜索操作....
			Thread thread = new Thread() {
				public void run() {
					Message msg = new Message();
					msg.what = 1;// 开始网络交互
					handler.sendMessage(msg);

					try {
						ArrayList<Arrestee> resultList = ChargeDetailService
								.chargeDetail(chargeSearch);
						if (resultList != null) {
							Intent intent = new Intent(
									ChargeSearchResultListActivity.this,
									ChargeDetailActivity.class);
							Log.d(TAG, "start sending arrestee list");

							String resultListString = gson.toJson(resultList);

							writeFile("arresteeFile", resultListString);

							intent.putExtra("chargeNum", charge_num);
							intent.putExtra("chargeContent", charge_content);

							startActivity(intent);

						} else if (resultList == null) {
							toastMsg = "Can not receive arrestee List";
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

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Search Charged Arrestees");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}
}
