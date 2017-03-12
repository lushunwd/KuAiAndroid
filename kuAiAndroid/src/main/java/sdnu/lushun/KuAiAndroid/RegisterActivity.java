package sdnu.lushun.KuAiAndroid;

import sdnu.lushun.KuAiAndroid.net.HttpPostThread;
import sdnu.lushun.KuAiAndroid.net.ThreadPoolUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appkefu.lib.interfaces.KFIMInterfaces;

public class RegisterActivity extends Activity {
	private ImageView mClose;
	private EditText mName, mPassword;
	private Button mRegester;
	private String url = null;
	private String value = null;
	private String username = null;
	private String password = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
		mClose = (ImageView) findViewById(R.id.R_Close);
		mName = (EditText) findViewById(R.id.R_name);
		mPassword = (EditText) findViewById(R.id.R_password);
		mRegester = (Button) findViewById(R.id.Register);
		MyOnClickListener my = new MyOnClickListener();
		mClose.setOnClickListener(my);
		mRegester.setOnClickListener(my);

	}

	private void reginstet() {
		url = Model.REGISTET;
		value = "{\"uname\":\"" + username + "\",\"upassword\":\"" + password
				+ "\"}";
//		value = "{\"uname\": + username +,\"upassword\": + password}";
		Log.e("lushun", value);
		ThreadPoolUtils.execute(new HttpPostThread(hand, url, value));
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			switch (mId) {
			case R.id.R_Close:
				finish();
				break;
			case R.id.Register:
				username = mName.getText().toString();
				password = mPassword.getText().toString();
				if (!username.equalsIgnoreCase(null)
						&& !password.equalsIgnoreCase(null)
						&& !username.equals("") && !password.equals("")) {
					if (username.length() >= 1 && password.length() >= 1) {
						// reginstet();// 请求注册功能
						registerThread();// 注册app客服
					} else {
						Toast.makeText(RegisterActivity.this, "用户名或密码最少为1位", 1)
								.show();
					}
				} else {
					Toast.makeText(RegisterActivity.this, "请先填写用户名或密码", 1)
							.show();
				}
				break;
			}

		}
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(RegisterActivity.this, "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(RegisterActivity.this, "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				Log.e("qiangpengyu", "result:" + result);
				if (result.equals("ok")) {
					Toast.makeText(RegisterActivity.this, "注册成功,请登陆", 1).show();
					Intent intent = new Intent();
					intent.putExtra("NameValue", username);
					intent.putExtra("PasswordValue", password);
					setResult(2, intent);
					finish();
				} else if (result.trim().equals("no")) {
					mName.setText("");
					mPassword.setText("");
					return;
				} else {
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this, "注册失败", 1).show();
					return;
				}

			}
		};
	};

	public void registerThread() {

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				Integer result = (Integer) msg.obj;

				if (result == 1) {
					Log.d("注册成功", "注册成功");
					reginstet();// 请求注册功能
				} else if (result == -400001) {
					Log.d("注册失败", "用户名长度最少为6");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this,
							"注册失败:用户名长度最少为6(错误码:-400001)", Toast.LENGTH_LONG)
							.show();
				} else if (result == -400002) {
					Log.d("注册失败", "密码长度最少为6");
				//	mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this,
							"注册失败:密码长度最少为6", Toast.LENGTH_LONG)
							.show();
				} else if (result == -400003) {
					Log.d("注册失败", "此用户名已经被注册");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this, "注册失败:此用户名已经被注册",
							Toast.LENGTH_LONG).show();
				} else if (result == -400004) {
					Log.d("注册失败", "用户名含有非法字符");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this, "注册失败:用户名含有非法字符",
							Toast.LENGTH_LONG).show();
				} else if (result == 0) {
					Log.d("注册失败",
							"其他原因：有可能是短时间内重复注册，为防止恶意注册，服务器对同一个IP注册做了时间间隔限制，即10分钟内同一个IP只能注册一个账号");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this, "注册失败:网络断开或注册太频繁",
							Toast.LENGTH_LONG).show();
				}
			}
		};

		new Thread() {
			public void run() {
				Message msg = new Message();
				// 目前用户名为整个微客服唯一，建议开发者在程序内部将appkey做为用户名的后缀，
				// 这样可以保证用户名的唯一性
				// 注册接口，返回结果为int
				msg.obj = KFIMInterfaces.register(Model.APPKEY + username,
						password);
				handler.sendMessage(msg);
			}
		}.start();

	}

}
