package sad.group3.incinfoapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import sad.group3.domain.Arrestee;
import sad.group3.domain.ArresteeSearch;
import sad.group3.service.ArresteeDetailService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressLint("HandlerLeak")
public class ArresteeSearchResultListActivity extends TimeBaseActivity {

	private static final String TAG = ArresteeSearchResultListActivity.class
			.getSimpleName();

	private ArrayList<Arrestee> searchResultList;
	private Gson gson = new Gson();
	private ProgressDialog mypDialog;
	private String toastMsg = null;

	private ListView listView;
	private TextView resultNumTextView;

	private ArresteeSearch arresteeSearch;

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
		setContentView(R.layout.arrestee_search_resultlist_layout);
		sendMsgToHandler(3);
		progressDialogShow();

		init();
		uiInit();

	}

	// rebuild Result List
	private void init() {
		Log.d(TAG, "initiate values");
		searchResultList = new ArrayList<Arrestee>();
		rebuildResultList();
	}

	// get the arrestee listS
	public void rebuildResultList() {
		String json = "";
		try {
			json = this.readFile("arresteeFile");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(TAG, "Receive string : " + json);
		this.searchResultList = gson.fromJson(json,
				new TypeToken<List<Arrestee>>() {
				}.getType());

	}

	// 读数据
	public String readFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
			File file = new File(fileName);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}

	private void uiInit() {
		Log.d(TAG, "UI initiate start");
		// create ListView with Result List
		resultNumTextView = (TextView) this.findViewById(R.id.result_title);
		resultNumTextView.setText(searchResultList.size() + " Results");
		listView = (ListView) findViewById(R.id.arrestee_list_listView);
		listView.setOnItemClickListener(new ItemClickListener());
		showList();
		Log.d(TAG, "showList");

		Log.d(TAG, "listener setted");
	}

	private void showList() {
		Log.d(TAG, "show " + searchResultList.size() + " arestees");

		// 1 生成动态数组，并且转载数据
		ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < searchResultList.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			// 图片

			if (searchResultList.get(i).getPhoto() != null) {
				Log.d(TAG, "get photo from Arrestee");
				Bitmap pic = BitmapFactory.decodeByteArray(searchResultList
						.get(i).getPhoto(), 0, searchResultList.get(i)
						.getPhoto().length, null);
				Log.d(TAG, "Value of photo : "
						+ searchResultList.get(i).getPhoto().toString());
				map.put("ItemImage", pic);
				Log.d(TAG, "photo added to listView");
			} else {
				Log.d(TAG, "no photo founded!");
			}

			String name = searchResultList.get(i).getFirstName() + " "
					+ searchResultList.get(i).getLastName();
			map.put("Item1Text", name);
			map.put("Item2Text", searchResultList.get(i).getGender());
			map.put("Item3Text", searchResultList.get(i).getBirthdate());
			map.put("Item4Text", "more...");
			mylist.add(map);

		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.pic_listitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "ItemImage", "Item1Text", "Item2Text",
						"Item3Text","Item4Text" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.ItemImage, R.id.Item1Text, R.id.Item2Text,
						R.id.Item3Text,R.id.Item_more_info_text });
		// 设定addapter并且显示
		mSchedule.setViewBinder(new MyViewBinder());
		listView.setAdapter(mSchedule);
	}

	public void backBtn(View view) {
		this.finish();
	}

	private class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Log.d(TAG, "listView listener started");

			arresteeSearch = new ArresteeSearch();

			Log.d(TAG, "arrestee list view position : " + position);

			arresteeSearch.setAp_num(searchResultList.get(position)
					.getArresteeNum());

			Log.d(TAG, "arrestee selected, jump to get detail");

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
									ArresteeSearchResultListActivity.this,
									ArresteeFileDetailActivity.class);
							String DetailString = gson.toJson(arresteeDetail);

							intent.putExtra("DetailString", DetailString);
							Log.d(TAG,
									"ArresteeDetail send to Incident Detail activity");
							startActivity(intent);
							// finish();

						} else if (arresteeDetail == null) {
							toastMsg = "No arrestee file found";
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

	// add Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Setting").setIcon(R.drawable.setting);
		return super.onCreateOptionsMenu(menu);
	}

	// 响应setting的点击
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			// 跳转到Setting界面
			startActivity(new Intent(this, SettingActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void progressDialogShow() {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("Search Arrstee File");// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	// 实现ViewBinder接口
	class MyViewBinder implements ViewBinder {
		/**
		 * view：要板顶数据的视图 data：要绑定到视图的数据
		 * textRepresentation：一个表示所支持数据的安全的字符串，结果是data.toString()或空字符串，但不能是Null
		 * 返回值：如果数据绑定到视图返回真，否则返回假
		 */
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if ((view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bmp = (Bitmap) data;
				iv.setImageBitmap(bmp);
				return true;
			}
			return false;
		}
	}
}