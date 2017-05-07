package sdnu.lushun.KuAiAndroid.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import sdnu.lushun.KuAiAndroid.Model;
import android.util.Log;

/**
 * 我的post请求方式工具
 * */

public class MyPost {
	public String doPost(String url, String img, String value) {
		String result = null;
		HttpResponse httpResponse = null;
		HttpPost post = new HttpPost(url);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 30000); // 超时设置
		client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 10000);// 连接超时
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// Json字符串拼
		nameValuePairs.add(new BasicNameValuePair("value", value));
		if(img!=""){
			nameValuePairs.add(new BasicNameValuePair("img", img));
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
			httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			} else {
				result = null;
			}
		} catch (UnsupportedEncodingException e) {
			result = null;
		} catch (ClientProtocolException e) {
			result = null;
		} catch (IOException e) {
			result = null;
		}
		return result;
	}
}
