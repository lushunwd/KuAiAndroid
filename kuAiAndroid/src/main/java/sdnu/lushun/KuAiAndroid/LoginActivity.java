package sdnu.lushun.KuAiAndroid;

import java.util.List;

import sdnu.lushun.KuAiAndroid.bean.UserInfo;
import sdnu.lushun.KuAiAndroid.net.HttpPostThread;
import sdnu.lushun.KuAiAndroid.net.MyJson;
import sdnu.lushun.KuAiAndroid.net.ThreadPoolUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFSLog;

public class LoginActivity extends Activity {

	private AlphaAnimation start_anima;
	View view;

	private Button mLogin;
	private EditText mName, mPassword;
	private RelativeLayout mRegister;

	private String NameValue = null;
	private String PasswordValue = null;
	private String url = null;
	private String value = null;
	private MyJson myJson = new MyJson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(this, R.layout.login, null);
		setContentView(view);
		initView();
	}

	private void initView() {
		MyOnClickListener my = new MyOnClickListener();
		mLogin = (Button) findViewById(R.id.login);
		mName = (EditText) findViewById(R.id.accounts);
		mPassword = (EditText) findViewById(R.id.password);
		mRegister = (RelativeLayout) findViewById(R.id.newUserRegister);
		mLogin.setOnClickListener(my);
		mRegister.setOnClickListener(my);

	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			switch (mId) {

			case R.id.login:
				NameValue = mName.getText().toString();
				PasswordValue = mPassword.getText().toString();
				Log.e("lushun", "NameValue" + NameValue + "  PasswordValue"
						+ PasswordValue);
				if (NameValue.equalsIgnoreCase(null)
						|| PasswordValue.equalsIgnoreCase(null)
						|| NameValue.equals("") || PasswordValue.equals("")) {
					Toast.makeText(LoginActivity.this, "请填写帐号或密码", 1).show();
				} else {
					// 登录接口
					KFIMInterfaces.login(LoginActivity.this, Model.APPKEY
							+ NameValue, PasswordValue);
					login();
				}
				break;

			case R.id.newUserRegister:
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivityForResult(intent, 1);

			}

		}

	}

	private void login() {
		url = Model.LOGIN;
		value = "{\"uname\":\"" + NameValue + "\",\"upassword\":\""
				+ PasswordValue + "\"}";
		Log.e("qianpengyu", value);

		ThreadPoolUtils.execute(new HttpPostThread(hand, url, value));
	}

	String result = null;
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(LoginActivity.this, "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(LoginActivity.this, "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				result = (String) msg.obj;

				if (isNetConnected(LoginActivity.this) == true
						&& result == null) {
					Toast.makeText(getApplicationContext(), "服务器请求失败", 0)
							.show();

				} else if (isNetConnected(LoginActivity.this) == true
						&& result.equalsIgnoreCase("NOUSER")) {
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(LoginActivity.this, "用户名不存在", 1).show();
					return;

				} else if (isNetConnected(LoginActivity.this) == true
						&& result.equalsIgnoreCase("NOPASS")) {
					mPassword.setText("");
					Toast.makeText(LoginActivity.this, "密码错误", 1).show();
					return;
				} else if (isNetConnected(LoginActivity.this) == true
						&& result != null) {
					Toast.makeText(LoginActivity.this, "登录成功", 1).show();

					List<UserInfo> newList = myJson.getNearUserList(result);
					if (newList != null) {
						Model.MYUSERINFO = newList.get(0);
					}
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					// Bundle bund = new Bundle();

					// bund.putSerializable("UserInfo", Model.MYUSERINFO);
					// intent.putExtra("value", bund);
					startActivity(intent);
					finish();

					// loginSetting(1);

					SharedPreferences sp = getSharedPreferences("UserInfo",
							MODE_PRIVATE);
					Log.e("SharedPreferencesOld",
							sp.getString("UserInfoJson", "none"));
					SharedPreferences.Editor mSettinsEd = sp.edit();
					mSettinsEd.putString("UserInfoJson", result);
					// 提交保存
					mSettinsEd.commit();

					// 设置个人资料"NICKNAME"
					KFIMInterfaces.setVCardField(LoginActivity.this,
							"NICKNAME", Model.MYUSERINFO.getUname());

					Log.e("SharedPreferencesNew",
							sp.getString("UserInfoJson", "none"));
					// finish();
				} else {
					Toast.makeText(getApplicationContext(), "请检查网络",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
	};

	public boolean isNetConnected(Activity act) {

		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 2 && data != null) {
			NameValue = data.getStringExtra("NameValue");
			mName.setText(NameValue);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		KFSLog.d("onStart");

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
		registerReceiver(mXmppreceiver, intentFilter);

	}

	@Override
	protected void onStop() {
		super.onStop();

		KFSLog.d("onStop");
		unregisterReceiver(mXmppreceiver);
	}

	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED)) {
				updateStatus(intent.getIntExtra("new_state", 0));
			}

		}
	};

	private void updateStatus(int status) {
		switch (status) {
		case KFXmppManager.CONNECTED:
			KFSLog.d("登录成功");
			break;
		case KFXmppManager.DISCONNECTED:
			KFSLog.d("未登录");
			break;
		case KFXmppManager.CONNECTING:
			KFSLog.d("登录中");
			break;
		case KFXmppManager.DISCONNECTING:
			KFSLog.d("登出中");
			break;
		case KFXmppManager.WAITING_TO_CONNECT:
		case KFXmppManager.WAITING_FOR_NETWORK:
			KFSLog.d("waiting to connect");
			break;
		default:
			throw new IllegalStateException();
		}
	}

}