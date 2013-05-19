package sad.group3.service;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sad.group3.domain.Incident;
import sad.group3.domain.Officer;
import sad.group3.utils.DES;
import sad.group3.utils.StreamTool;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TaskService {
	private static final String TAG = TaskService.class.getSimpleName();
	private static Gson gson = new Gson();
	private static String path=null;
	private static URL url=null;
	private static HttpURLConnection conn=null;
	public static String i_id=null;

	public static ArrayList<Incident> tasksSearch(Officer officer) {
		ArrayList<Incident> SearchResult = null;
		try {
			Log.d(TAG, "Service Start!!!!!!!!!!!!!!!!!!" );
			path = SettingService.getUrl() + "TaskCLServlet";
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			Log.d(TAG, "Start encrypt" );
			String outputStr = DES.encryptDES(gson.toJson(officer), DES.KEY);
			Log.d(TAG, "Json : " + gson.toJson(officer));
			BufferedOutputStream bus = new BufferedOutputStream(
					conn.getOutputStream());
			bus.write(outputStr.getBytes());
			bus.flush();
			bus.close();
			Log.d(TAG, "Send request" );
			if (conn.getResponseCode() == 200) 
			{
				InputStream is = conn.getInputStream();
				String json = new String(StreamTool.read(is));
				json = DES.decryptDES(json, DES.KEY);
				Log.d(TAG, "Return Search Result : " + json );
				SearchResult = gson.fromJson(json, new TypeToken<List<Incident>>(){}.getType());

				Log.d(TAG, "Return Search Result : " + SearchResult.size() );
				return SearchResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SearchResult;
	}
}
