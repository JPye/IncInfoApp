package sad.group3.service;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sad.group3.utils.DES;
import sad.group3.utils.StreamTool;

public class LogoutService {
	private static String path = null;
	private static URL url = null;
	private static HttpURLConnection conn = null;

	public static String logout() {
		String logoutResult = "Error";
		try {
			path = SettingService.getUrl() + "LogoutCLServlet";
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			String idStr = DES.encryptDES(LoginService.o_id, DES.KEY);
			BufferedOutputStream bus = new BufferedOutputStream(
					conn.getOutputStream());
			bus.write(idStr.getBytes());
			bus.flush();
			bus.close();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				logoutResult = new String(StreamTool.read(is));
				return DES.decryptDES(logoutResult, DES.KEY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return logoutResult;
	}
}
