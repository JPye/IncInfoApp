package sad.group3.incinfoapp;

import java.lang.reflect.Field;

import sad.group3.service.LogoutService;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class MenuActivity extends TabActivity {
	private TabHost tabHost;
	private TabWidget tabWidget;
	private ProgressDialog mypDialog;
	// 到各个界面的Intents
	private Intent incIntent, arresteeIntent, lawIntent, reportIntent;

	private static final int width = 0;
	private static final int height = 80;

	private String toastMsg = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 开始
				mypDialog.setTitle("Log Out");
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
		setContentView(R.layout.menu_layout);
		tabHost = getTabHost();
		tabWidget = getTabWidget();
		intentInit();
		addSpec();
		progressDialogShow("Log In");
	}

	private void progressDialogShow(String title) {
		mypDialog = new ProgressDialog(this);// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle(title);// 设置ProgressDialog 标题
		mypDialog.setMessage("Loading...");// 设置ProgressDialog 提示信息
		mypDialog.setIndeterminate(false);// 设置ProgressDialog
		mypDialog.setCancelable(true);// 设置ProgressDialog是否可以按退回按键取消
		mypDialog.setCanceledOnTouchOutside(false);
	}

	// 初始化intent
	private void intentInit() {
		incIntent = new Intent(this, IncidentSearchActivity.class);
		arresteeIntent = new Intent(this, ArresteeSearchActivity.class);
		lawIntent = new Intent(this, ChargeSearchActivity.class);
		reportIntent = new Intent(this, ReportSearchActivity.class);
	}

	// 给各个tab添加标签
	private void addSpec() {
		tabHost.addTab(this.buildTagSpec("inc_search", R.string.tab_inc_search,
				R.drawable.incident_icon, incIntent));
		tabHost.addTab(this.buildTagSpec("arrestee_search",
				R.string.tab_arrestee_search, R.drawable.arrestee_icon, arresteeIntent));
		tabHost.addTab(this.buildTagSpec("charge_search", R.string.tab_charge_search,
				R.drawable.charge_icon, lawIntent));
		tabHost.addTab(this.buildTagSpec("report_search",
				R.string.tab_report_search, R.drawable.report_icon, reportIntent));

		tabHost.setOnTabChangedListener(change);

		Field mBottomLeftStrip;
		Field mBottomRightStrip;

		for (int i = 0; i < tabWidget.getChildCount(); i++) {

			tabWidget.getChildAt(i).getLayoutParams().width = width;
			tabWidget.getChildAt(i).getLayoutParams().height = height;

			TextView text = (TextView) tabWidget.getChildAt(i).findViewById(
					android.R.id.title);
			text.setTextColor(this.getResources().getColorStateList(
					android.R.color.white));

			try {
				if (Float.valueOf(Build.VERSION.RELEASE.substring(0, 3)) <= 2.1) {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mBottomLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mBottomRightStrip");

				} else {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mRightStrip");
				}
				if (!mBottomLeftStrip.isAccessible())
					mBottomLeftStrip.setAccessible(true);
				if (!mBottomRightStrip.isAccessible())
					mBottomRightStrip.setAccessible(true);

				mBottomLeftStrip.set(tabWidget,
						getResources().getDrawable(R.drawable.empty));
				mBottomRightStrip.set(tabWidget,
						getResources().getDrawable(R.drawable.empty));

			} catch (Exception e) {
				e.printStackTrace();
			}
			// Background
			View v = tabWidget.getChildAt(i);
			if (tabHost.getCurrentTab() == i) {
				v.setBackgroundResource(R.drawable.home_btn_bg);
			} else {
				v.setBackgroundResource(R.drawable.empty);
			}
		}

	}

	/**
	 * 自定义创建标签的方法
	 * 
	 * @param tagName
	 *            标签标识
	 * @param tagLable
	 *            标签文字
	 * @param icon
	 *            标签的图标
	 * @param content
	 *            标签对应的intent
	 * @return
	 */
	private TabHost.TabSpec buildTagSpec(String tagName, int tagLable,
			int icon, Intent content) {
		return tabHost
				.newTabSpec(tagName)
				.setIndicator(getResources().getString(tagLable),
						getResources().getDrawable(icon)).setContent(content);
	}

	OnTabChangeListener change = new OnTabChangeListener() {
		@Override
		public void onTabChanged(String tabId) {
			// TODO Auto-generated method stub
			tabHost.setCurrentTabByTag(tabId);
			for (int i = 0; i < tabWidget.getChildCount(); i++) {
				View v = tabWidget.getChildAt(i);
				if (tabHost.getCurrentTab() == i) {
					v.setBackgroundResource(R.drawable.home_btn_bg);
				} else {
					v.setBackgroundResource(R.drawable.empty);
				}
			}
		}
	};

	// Confirmation about log out
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("Are you sure to exit?");
		builder.setTitle("Confirmation");
		builder.setPositiveButton("OK",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Thread thread = new Thread() {
							public void run() {
								Message msg = new Message();
								msg.what = 1;// 开始网络交互
								handler.sendMessage(msg);

								try {
									String logoutResult = LogoutService
											.logout();
									if ("Error".equals(logoutResult)) {
										toastMsg = "Logout Fail";
										Thread.sleep(2000);
									}
									System.exit(0);
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
				});
		builder.setNegativeButton("Cancel",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	// add Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "About Us").setIcon(R.drawable.about_us);
		menu.add(0, 2, 1, "Exit").setIcon(R.drawable.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			// 转向About Us界面
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case 2:
			dialog();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
