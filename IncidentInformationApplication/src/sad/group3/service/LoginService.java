package sad.group3.service;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import sad.group3.domain.Officer;
import sad.group3.utils.DES;
import sad.group3.utils.StreamTool;

public class LoginService {
	private static Gson gson = new Gson();
	private static String path=null;
	private static URL url=null;
	private static HttpURLConnection conn=null;
	public static String o_id=null;

	public static String login(Officer officer) {
		o_id=officer.getId();
		String loginResult="Error";
		try {
			path = SettingService.getUrl() + "LoginCLServlet";
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			
			String officerStr = DES.encryptDES(gson.toJson(officer), DES.KEY);
			BufferedOutputStream bus = new BufferedOutputStream(
					conn.getOutputStream());
			bus.write(officerStr.getBytes());
			bus.flush();
			bus.close();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				loginResult = new String(StreamTool.read(is));
				return DES.decryptDES(loginResult, DES.KEY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return loginResult;
	}
}
