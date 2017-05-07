package sdnu.lushun.KuAiAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import sdnu.lushun.KuAiAndroid.net.HttpPostThread;
import sdnu.lushun.KuAiAndroid.net.ThreadPoolUtils;

public class RegisterActivity extends Activity {
    private ImageView mClose;
    private EditText mName, mPassword;
    private Button mRegester;
    private String username = null;
    private String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    /**
     * 执行注册功能
     */
    private void register() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uname", username);
            jsonObject.put("upassword", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ThreadPoolUtils.execute(new HttpPostThread(hand, Model.REGISTET, jsonObject.toString()));
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
                    username = mName.getText().toString().trim();
                    password = mPassword.getText().toString().trim();
                    if (!username.isEmpty() && !password.isEmpty()) {
                        register();// 请求注册功能
                    } else {
                        Toast.makeText(RegisterActivity.this, "请先填写用户名或密码", Toast.LENGTH_SHORT).show();
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
                if (result.equals("ok")) {
                    Toast.makeText(RegisterActivity.this, "注册成功,请登陆", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("NameValue", username);
                    intent.putExtra("PasswordValue", password);
                    setResult(2, intent);
                    finish();
                } else if (result.trim().equals("no")) {
                    mName.setText("");
                    mPassword.setText("");
                    Toast.makeText(RegisterActivity.this, "此用户名已被注册！", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    mName.setText("");
                    mPassword.setText("");
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    };
}
