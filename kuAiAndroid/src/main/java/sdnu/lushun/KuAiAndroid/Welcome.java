package sdnu.lushun.KuAiAndroid;

/*
 * 欢迎界面
 * 
 * */

import java.util.List;

import sdnu.lushun.KuAiAndroid.bean.UserInfo;
import sdnu.lushun.KuAiAndroid.net.MyJson;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.appkefu.lib.service.KFSettingsManager;

public class Welcome extends Activity {
	private AlphaAnimation start_anima;
	View view;
	private MyJson myJson = new MyJson();
	private KFSettingsManager mSettingsMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.welcome, null);
		setContentView(view);
		init();
		initData();
	}

	private void initData() {
		start_anima = new AlphaAnimation(0.1f, 1.0f);
		start_anima.setDuration(3000);
		view.startAnimation(start_anima);
		start_anima.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				redirectTo();
			}
		});
	}

	private void redirectTo() {
		if (isNetConnected(Welcome.this) == false) {
			startActivity(new Intent(getApplicationContext(),
					LoginActivity.class));
			Toast.makeText(getApplicationContext(), "请检查网络设置",
					Toast.LENGTH_SHORT).show();

		} else {
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
		}

		finish();
	}

	private void init() {

		mSettingsMgr = KFSettingsManager.getSettingsManager(this);

		// 设置开发者调试模式，默认为true，如要关闭开发者模式，请设置为false
		KFIMInterfaces.enableDebugMode(this, true);
		login1();
		SharedPreferences sp = Welcome.this.getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		String result = sp.getString("UserInfoJson", "none");
		Log.e("SharedPreferencesOld", result);
		if (!result.equals("none")) {
			List<UserInfo> newList = myJson.getNearUserList(result);
			if (newList != null) {
				Model.MYUSERINFO = newList.get(0);
				// myUserName.setText(Model.MYUSERINFO.getUname());
			}
		}

	}

	private void login1() {
		// 检查 用户名/密码 是否已经设置,如果已经设置，则登录
		if (!"".equals(mSettingsMgr.getUsername())
				&& !"".equals(mSettingsMgr.getPassword()))
			KFIMInterfaces.login(this, mSettingsMgr.getUsername(),
					mSettingsMgr.getPassword());
		// 设置个人资料"NICKNAME"
	}

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

}
