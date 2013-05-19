package sad.group3.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SettingService {
	private static String url = null;
	private static String defaultURL = null;
	private static String customizedURL = null;
	private static String port = null;
	private static InputStream is = null;
	private static Properties pp = null;

	static {
		try {
			pp = new Properties();
			is = SettingService.class.getResourceAsStream(
					"/assets/setting.properties");
			pp.load(is);
			defaultURL = pp.getProperty("defaultURL");
			url = defaultURL;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		SettingService.url = url;
	}

	public static String getDefaultURL() {
		return defaultURL;
	}

	public static void setDefaultURL(String defaultURL) {
		SettingService.defaultURL = defaultURL;
	}

	public static String getCustomizedURL() {
		return customizedURL;
	}

	public static void setCustomizedURL(String customizedURL) {
		SettingService.customizedURL = customizedURL;
	}

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		SettingService.port = port;
	}

}
