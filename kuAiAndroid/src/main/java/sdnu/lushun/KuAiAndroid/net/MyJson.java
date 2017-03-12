package sdnu.lushun.KuAiAndroid.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sdnu.lushun.KuAiAndroid.bean.CommentsInfo;
import sdnu.lushun.KuAiAndroid.bean.Info;
import sdnu.lushun.KuAiAndroid.bean.UserInfo;

/**
 * Json字符串解析工具类
 * 
 */

public class MyJson {

	// 解析方法
	public List<Info> getAshamedList(String value) {
		List<Info> list = null;
		try {
			JSONArray jay = new JSONArray(value);
			list = new ArrayList<Info>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				Info info = new Info();
				info.setQid(job.getString("QID"));
				info.setUid(job.getString("UID"));
				info.setTid(job.getString("TID"));
				info.setQimg(job.getString("QIMG"));
				info.setAimg(job.getString("AIMG"));
				info.setQvalue(job.getString("QVALUE"));
				info.setQlike(job.getString("QLIKE"));
				info.setQunlike(job.getString("QUNLIKE"));
				info.setQshare(job.getString("QSHARE"));
				info.setUname(job.getString("UNAME"));
				info.setUhead(job.getString("UHEAD"));
				list.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 解析评论的方法
	public List<CommentsInfo> getAhamedCommentsList(String value) {
		List<CommentsInfo> list = null;
		try {
			JSONArray jay = new JSONArray(value);
			list = new ArrayList<CommentsInfo>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				CommentsInfo info = new CommentsInfo();
				info.setCid(job.getString("CID"));
				info.setCvalue(job.getString("CVALUE"));
				info.setQid(job.getString("QID"));
				info.setUid(job.getString("UID"));
				info.setCtime(job.getString("CTIME"));
				info.setUname(job.getString("UNAME"));
				info.setUhead(job.getString("UHEAD"));
				list.add(info);
			}
		} catch (JSONException e) {
		}
		return list;
	}

	// 解析附近用户的方法
	public List<UserInfo> getNearUserList(String result) {
		List<UserInfo> list = null;
		try {
			JSONArray jay = new JSONArray(result);
			list = new ArrayList<UserInfo>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				UserInfo info = new UserInfo();
				info.setUserid(job.getString("USERID"));
				info.setUname(job.getString("UNAME"));
				info.setUhead(job.getString("UHEAD"));
				info.setUage(job.getString("UAGE"));
				info.setUhobbles(job.getString("UHOBBIES"));
				info.setUplace(job.getString("UPLACE"));
				info.setUexplain(job.getString("UEXPLAIN"));
				info.setUtime(job.getString("UTIME"));
				info.setUbrand(job.getString("UBRAND"));
				info.setUsex(job.getString("USEX"));
				list.add(info);
			}
		} catch (JSONException e) {
		}
		return list;
	}

}
