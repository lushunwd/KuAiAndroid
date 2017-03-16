package sdnu.lushun.KuAiAndroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import sdnu.lushun.KuAiAndroid.bean.UserInfo;
import sdnu.lushun.KuAiAndroid.net.MyJson;
import sdnu.lushun.KuAiAndroid.util.LoginUtil;

public class LoginActivity extends Activity {

    private View view;

    private Button mLogin;
    private EditText mName, mPassword;
    private RelativeLayout mRegister;

    private String username = null;
    private String password = null;
    private MyJson myJson = new MyJson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    username = mName.getText().toString().trim();
                    password = mPassword.getText().toString().trim();
                    if (username.isEmpty()
                            || password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请填写帐号或密码", Toast.LENGTH_LONG).show();
                    } else {
                        LoginUtil.login(username, password, handler, LoginActivity.this);
                    }
                    break;
                case R.id.newUserRegister:
                    Intent intent = new Intent(LoginActivity.this,
                            RegisterActivity.class);
                    startActivityForResult(intent, 1);

            }

        }

    }


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            String result = msg.obj.toString();
            if (msg.what == 200 && null != result && !("nouser").equalsIgnoreCase(result) && !("nopass").equalsIgnoreCase(result)) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                List<UserInfo> newList = myJson.getNearUserList(result);
                if (newList != null) {
                    Model.MYUSERINFO = newList.get(0);
                }
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
                SharedPreferences sp = getSharedPreferences("UserInfo",
                        MODE_PRIVATE);
                SharedPreferences.Editor mSettingsEd = sp.edit();
                mSettingsEd.putString("UserInfoJson", result);
                // 提交保存
                mSettingsEd.commit();

            } else {
                LoginUtil.loginFail(LoginActivity.this, msg);
            }
        }
    };
}