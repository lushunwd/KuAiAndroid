package sdnu.lushun.KuAiAndroid;

/*
 * 欢迎界面
 * 
 * */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import java.util.List;

import sdnu.lushun.KuAiAndroid.bean.UserInfo;
import sdnu.lushun.KuAiAndroid.net.MyJson;

import static sdnu.lushun.KuAiAndroid.util.NetUtils.isNetConnected;


public class Welcome extends Activity {
    private AlphaAnimation start_anima;
    View view;
    private MyJson myJson = new MyJson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.welcome, null);
        setContentView(view);
        initAnimation();
    }

    /**
     * 展现渐变动画效果
     */
    private void initAnimation() {
        start_anima = new AlphaAnimation(0.1f, 1.0f);
        start_anima.setDuration(3000);
        view.startAnimation(start_anima);
        start_anima.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                getUserInfo();
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

    /**
     * 根据网络是否连接，选择要启动的activity
     */
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

    /**
     * 获得用户信息
     */
    private void getUserInfo() {
        SharedPreferences sp = Welcome.this.getSharedPreferences("UserInfo",
                MODE_PRIVATE);
        String result = sp.getString("UserInfoJson", "none");
        Log.e("SharedPreferencesOld", result);
        if (!result.equals("none")) {
            List<UserInfo> newList = myJson.getUserList(result);
            if (newList != null) {
                Model.MYUSERINFO = newList.get(0);
            }
        }
    }

}
