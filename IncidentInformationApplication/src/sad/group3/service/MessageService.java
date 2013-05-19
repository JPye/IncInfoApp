package sad.group3.service;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import sad.group3.domain.Message;
import sad.group3.utils.DES;
import sad.group3.utils.StreamTool;
import android.util.Log;

import com.google.gson.Gson;

public class MessageService {
	private static final String TAG = MessageService.class.getSimpleName();
	private static Gson gson = new Gson();
	private static String path=null;
	private static URL url=null;
	private static HttpURLConnection conn=null;
	public static String i_id=null;

	public static String uploadMessage(Message message) {
		String result = "Connection Error";
		try {
			Log.d(TAG, "Service Start!!!!!!!!!!!!!!!!!!" );
			path = SettingService.getUrl() + "MessageCLServlet?operation=addnewmsg";
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			Log.d(TAG, "Start encrypt" );
			String outputStr = DES.encryptDES(gson.toJson(message), DES.KEY);
			Log.d(TAG, "Json : " + gson.toJson(message));
			BufferedOutputStream bus = new BufferedOutputStream(
					conn.getOutputStream());
			bus.write(outputStr.getBytes());
			bus.flush();
			bus.close();
			Log.d(TAG, "Send request" );
			if (conn.getResponseCode() == 200) 
			{
				InputStream is = conn.getInputStream();
				String resultStr = new String(StreamTool.read(is));
				result = DES.decryptDES(resultStr, DES.KEY);
				Log.d(TAG, "Message Upload Result : " + result );
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static Message messageDetail(Message message) {
		Message messageDetail = null;
		try {
			Log.d(TAG, "Service Start!!!!!!!!!!!!!!!!!!" );
			path = SettingService.getUrl() + "MessageCLServlet";
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			Log.d(TAG, "Start encrypt" );
			String outputStr = DES.encryptDES(gson.toJson(message), DES.KEY);
			Log.d(TAG, "Json : " + gson.toJson(message));
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
				Log.d(TAG, "Return Search Result : " + json );
				json = DES.decryptDES(json, DES.KEY);
				Log.d(TAG, "Return Search Result : " + json );
				messageDetail = gson.fromJson(json, sad.group3.domain.Message.class);

				return messageDetail;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return messageDetail;
	}
}
