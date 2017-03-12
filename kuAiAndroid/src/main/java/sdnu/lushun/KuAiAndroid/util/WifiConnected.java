package sdnu.lushun.KuAiAndroid.util;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * 一些常用工具
 * 
 * @author Louis
 * 
 */
public class WifiConnected {

	/**
	 * 判断wifi是否连接
	 * 
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return manager.isWifiEnabled();
	}
}
