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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import sdnu.lushun.KuAiAndroid.bean.UserInfo;
import sdnu.lushun.KuAiAndroid.net.HttpPostThread;
import sdnu.lushun.KuAiAndroid.net.MyJson;
import sdnu.lushun.KuAiAndroid.net.ThreadPoolUtils;

public class LoginActivity extends Activity {

    private View view;

    private Button mLogin;
    private EditText mName, mPassword;
    private RelativeLayout mRegister;

    private String NameValue = null;
    private String PasswordValue = null;
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
                    NameValue = mName.getText().toString();
                    PasswordValue = mPassword.getText().toString();
                    if (NameValue.trim().isEmpty()
                            || PasswordValue.trim().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请填写帐号或密码", Toast.LENGTH_LONG).show();
                    } else {
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uname", NameValue.trim());
            jsonObject.put("upassword", PasswordValue.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ThreadPoolUtils.execute(new HttpPostThread(hand, Model.LOGIN, jsonObject.toString()));
    }


    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            String result;
            if (msg.what == 404) {
                Toast.makeText(LoginActivity.this, "请求失败，服务器故障", 1).show();
            } else if (msg.what == 100) {
                Toast.makeText(LoginActivity.this, "服务器无响应", 1).show();
            } else if (msg.what == 200) {
                result = (String) msg.obj;

                if (result == null) {
                    Toast.makeText(getApplicationContext(), "服务器请求失败", 0)
                            .show();

                } else if (result.equalsIgnoreCase("NOUSER")) {
                    mName.setText("");
                    mPassword.setText("");
                    Toast.makeText(LoginActivity.this, "用户名不存在", 1).show();
                    return;

                } else if (result.equalsIgnoreCase("NOPASS")) {
                    mPassword.setText("");
                    Toast.makeText(LoginActivity.this, "密码错误", 1).show();
                    return;
                } else if (result != null && !"".equals(result)) {
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
                    Toast.makeText(getApplicationContext(), "登录失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}