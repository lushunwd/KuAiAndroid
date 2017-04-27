package sdnu.lushun.KuAiAndroid.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sdnu.lushun.KuAiAndroid.bean.Info;
import sdnu.lushun.KuAiAndroid.bean.UserInfo;

/**
 * Json字符串解析工具类
 * 
 */

public class MyJson {

	// 解析分享的说说
	public List<Info> getValueList(String value) {
		List<Info> list = null;
		try {
			JSONArray jay = new JSONArray(value);
			list = new ArrayList<Info>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				Info info = new Info();
				info.setQid(job.getString("aid"));
				info.setUid(job.getString("uid"));
				info.setAimg(job.getString("aimg"));
				info.setQvalue(job.getString("avalue"));
				info.setQlike(job.getString("alike"));
				info.setQunlike(job.getString("aunlike"));
				info.setUname(job.getString("uname"));
				info.setUhead(job.getString("uhead"));
				list.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	//解析用户列表
	public List<UserInfo> getUserList(String result) {
		List<UserInfo> list = null;
		try {
			JSONArray jay = new JSONArray(result);
			list = new ArrayList<UserInfo>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				UserInfo info = new UserInfo();
				info.setUserid(job.getString("userid"));
				info.setUname(job.getString("uname"));
				info.setUhead(job.getString("uhead"));
				info.setUtime(job.getString("utime"));
				info.setUsex(job.getString("usex"));
				list.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
