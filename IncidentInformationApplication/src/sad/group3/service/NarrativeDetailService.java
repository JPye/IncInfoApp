package sad.group3.service;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;
import com.google.gson.Gson;
import sad.group3.domain.Narrative;
import sad.group3.utils.DES;
import sad.group3.utils.StreamTool;

public class NarrativeDetailService {
	private static final String TAG = NarrativeDetailService.class.getSimpleName();
	private static Gson gson = new Gson();
	private static String path=null;
	private static URL url=null;
	private static HttpURLConnection conn=null;
	public static String i_id=null;

	public static Narrative narrativeDetail(Narrative narrative) {
		Narrative narrativeDetail = null;
		try {
			Log.d(TAG, "Service Start!!!!!!!!!!!!!!!!!!" );
			path = SettingService.getUrl() + "NarrativeCLServlet";
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			Log.d(TAG, "Start encrypt" );
			String outputStr = DES.encryptDES(gson.toJson(narrative), DES.KEY);
			Log.d(TAG, "Json : " + gson.toJson(narrative));
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
				narrativeDetail = gson.fromJson(json, Narrative.class);

				return narrativeDetail;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return narrativeDetail;
	}


}
